/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.apache.woden.internal.wsdl20;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.internal.WSDLContext;
import org.apache.woden.internal.wsdl20.extensions.AttributeExtensibleImpl;
import org.apache.woden.internal.wsdl20.extensions.ElementExtensibleImpl;
import org.apache.woden.types.NamespaceDeclaration;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.xml.NestedElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.XMLAttr;

/**
 * This abstract class defines the behaviour common to all WSDL elements.
 * That is, it implements support for extension attributes and elements.
 * This interface can be used as a common reference for all WSDL elements
 * represented by the Element API.
 * 
 * @author jkaputin@apache.org
 */
public class WSDLElementImpl implements WSDLElement 
{
    private static final String emptyString = "".intern();
    private AttributeExtensibleImpl fAttrExt = new AttributeExtensibleImpl();
    private ElementExtensibleImpl fElemExt = new ElementExtensibleImpl();
    private Map namespaceToPrefixMap = new HashMap();
    private Map prefixToNamespaceMap = new HashMap();
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#setExtensionAttribute(javax.xml.namespace.QName, org.apache.woden.xml.XMLAttr)
     */
    public void setExtensionAttribute(QName attrType, XMLAttr attr) 
    {
        fAttrExt.setExtensionAttribute(attrType, attr);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttribute(javax.xml.namespace.QName)
     */
    public XMLAttr getExtensionAttribute(QName attrType) 
    {
        return fAttrExt.getExtensionAttribute(attrType);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributesForNamespace(java.net.URI)
     */
    public XMLAttr[] getExtensionAttributesForNamespace(URI namespace) 
    {
        return fAttrExt.getExtensionAttributesForNamespace(namespace);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributes()
     */
    public XMLAttr[] getExtensionAttributes() 
    {
        return fAttrExt.getExtensionAttributes();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#hasExtensionAttributesForNamespace(java.net.URI)
     */
    public boolean hasExtensionAttributesForNamespace(URI namespace) 
    {
        return fAttrExt.hasExtensionAttributesForNamespace(namespace);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#addExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void addExtensionElement(ExtensionElement extEl) 
    {
        fElemExt.addExtensionElement(extEl);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#removeExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void removeExtensionElement(ExtensionElement extEl) 
    {
        fElemExt.removeExtensionElement(extEl);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElements()
     */
    public ExtensionElement[] getExtensionElements() 
    {
        return fElemExt.getExtensionElements();
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElementsOfType(javax.xml.namespace.QName)
     */
    public ExtensionElement[] getExtensionElementsOfType(QName elemType) 
    {
        return fElemExt.getExtensionElementsOfType(elemType);
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#hasExtensionElementsForNamespace(java.net.URI)
     */
    public boolean hasExtensionElementsForNamespace(URI namespace) 
    {
        return fElemExt.hasExtensionElementsForNamespace(namespace);
    }

    //package private
    WSDLContext getWsdlContext() {
        return getWsdlContext(this);
    }
    
    static private WSDLContext getWsdlContext(WSDLElement wElem) {
        if (wElem instanceof NestedElement) {
            WSDLElement parent = ((NestedElement) wElem).getParentElement();
            return getWsdlContext(parent);
        }
        
        //This is not a nested element, so the WSDL context is in this element, at the top of the tree.
        //This element will override the getWsdlContext() method defined in WSDLElementImpl.
        return ((WSDLElementImpl)wElem).getWsdlContext();
    }
    
    public void addNamespace(String prefix, URI namespace) {
        prefix = (prefix != null) ? prefix : emptyString;
        if (namespace == null) {
            removeNamespace(prefix);
        } else {
            namespaceToPrefixMap.put(namespace, prefix);
            prefixToNamespaceMap.put(prefix, namespace);
        }
    }
    
    public URI removeNamespace(String prefix) {
        prefix = (prefix != null) ? prefix : emptyString;
        URI namespaceURI = (URI)prefixToNamespaceMap.remove(prefix);
        namespaceToPrefixMap.remove(namespaceURI);
        return namespaceURI;
    }
    
    public String getNamespacePrefix(URI namespace) {
        //See if the prefix is local.
        String prefix = (String)namespaceToPrefixMap.get(namespace);
        if (prefix == null && this instanceof NestedElement) { //If not call parents to find prefix if I'm nested.
            return ((NestedElement)this).getParentElement().getNamespacePrefix(namespace);
        } else { //Otherwise return the found prefix or null.
            return prefix;
        }
    }
    
    public URI getNamespaceURI(String prefix) {
        //See if the prefix is local.
        prefix = (prefix != null) ? prefix : emptyString;
        URI namespace = (URI)prefixToNamespaceMap.get(prefix);
        if (namespace == null && this instanceof NestedElement) { //If not call parents to find prefix if I'm nested.
            return ((NestedElement)this).getParentElement().getNamespaceURI(prefix);
        } else { //Otherwise return the found namespace or null.
            return namespace;
        }
    }
    
    public NamespaceDeclaration[] getInScopeNamespaces() {
        ArrayList namespaces = addInScopeNamespaces(new ArrayList());
        return (NamespaceDeclaration[])namespaces.toArray(new NamespaceDeclaration[namespaces.size()]);
    }
    
    private ArrayList addInScopeNamespaces(ArrayList namespaces) {
         //Add my namespaces. 
        Iterator it = namespaceToPrefixMap.keySet().iterator();
        while(it.hasNext()){
            URI namespace = (URI)it.next();
            namespaces.add(new NamespaceDeclaration((String)namespaceToPrefixMap.get(namespace), namespace));
        }
        //Add my parent namespaces if I'm a child.
        if (this instanceof NestedElement) {
            return ((WSDLElementImpl)((NestedElement)this).getParentElement()).addInScopeNamespaces(namespaces);
        } else {
            return namespaces;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.WSDLElement#getLocalNamespaceDeclarations()
     */
    public NamespaceDeclaration[] getDeclaredNamespaces() {
        ArrayList namespaces = new ArrayList();
        Iterator it = namespaceToPrefixMap.keySet().iterator();
        while(it.hasNext()){
            URI namespace = (URI)it.next();
            namespaces.add(new NamespaceDeclaration((String)namespaceToPrefixMap.get(namespace), namespace));
        }
        return (NamespaceDeclaration[])namespaces.toArray(new NamespaceDeclaration[namespaces.size()]);
    }
}
