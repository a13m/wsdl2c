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
package org.apache.woden.internal.wsdl20.extensions.http;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.internal.wsdl20.BindingImpl;
import org.apache.woden.internal.wsdl20.TypesImpl;
import org.apache.woden.internal.wsdl20.extensions.AttributeExtensibleImpl;
import org.apache.woden.internal.wsdl20.extensions.ElementExtensibleImpl;
import org.apache.woden.internal.wsdl20.extensions.ExtensionElementImpl;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.NestedComponent;
import org.apache.woden.wsdl20.TypeDefinition;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.http.HTTPHeader;
import org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.NestedElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.XMLAttr;
import org.apache.ws.commons.schema.XmlSchemaType;

/**
 * This class represents the HTTPHeader Component and the &lt;whttp:header&gt; 
 * extension element that can appear within a Binding Fault or Binding Message 
 * Reference.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class HTTPHeaderImpl implements HTTPHeader, HTTPHeaderElement 
{
    private WSDLElement fParent = null;
    private List fDocumentationElements = new Vector();
    private ExtensionElement fExtElem = new ExtensionElementImpl();
    private AttributeExtensibleImpl fAttrExt = new AttributeExtensibleImpl();
    private ElementExtensibleImpl fElemExt = new ElementExtensibleImpl();
    private String fName = null;
    private QName fTypeName = null;

    /* ***********************************************************************
     *  Component model methods (HTTPHeader interface), some shared with Element model
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeader#getName()
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#getName()
     */
    public String getName() {
        return fName;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeader#getTypeDefinition()
     */
    public TypeDefinition getTypeDefinition() 
    {
        TypeDefinition typeDef = null;
        Description desc = getDescriptionComponent(getParent());
        typeDef = desc.getTypeDefinition(fTypeName);
        return typeDef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeader#isRequired()
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#isRequired()
     */
    public Boolean isRequired() {
        return fExtElem.isRequired();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeader#getParent()
     */
    public WSDLComponent getParent() {
        return (WSDLComponent)fParent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeader#toElement()
     */
    public HTTPHeaderElement toElement() {
        return this;
    }

    /* ***********************************************************************
     *  Element model-only methods (HTTPHeaderElement interface)
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#setName(java.lang.String)
     */
    public void setName(String name) {
        fName = name;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#setTypeName(javax.xml.namespace.QName)
     */
    public void setTypeName(QName qname) {
        fTypeName = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#getTypeName()
     */
    public QName getTypeName() {
        return fTypeName;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#getType()
     */
    public XmlSchemaType getType() 
    {
        XmlSchemaType xst = null;
        DescriptionElement desc = getDescriptionElement(getParentElement());
        TypesImpl types = (TypesImpl)desc.getTypesElement();
        if(types != null) {
            xst = types.getTypeDefinition(fTypeName);
        }
        return xst;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#setParentElement(org.apache.woden.wsdl20.xml.WSDLElement)
     */
    public void setParentElement(WSDLElement wsdlEl) {
        fParent = wsdlEl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#getParentElement()
     */
    public WSDLElement getParentElement() {
        return fParent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#addDocumentationElement(org.apache.woden.wsdl20.xml.DocumentationElement)
     */
    public void addDocumentationElement(DocumentationElement docEl) {
        if(docEl != null) {
            fDocumentationElements.add(docEl);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPHeaderElement#getDocumentationElements()
     */
    public DocumentationElement[] getDocumentationElements() {
        DocumentationElement[] array = new DocumentationElement[fDocumentationElements.size()];
        fDocumentationElements.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setExtensionType(javax.xml.namespace.QName)
     */
    public void setExtensionType(QName qname) {
        fExtElem.setExtensionType(qname);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#getExtensionType()
     */
    public QName getExtensionType() {
        return fExtElem.getExtensionType();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setRequired(java.lang.Boolean)
     */
    public void setRequired(Boolean required) {
        fExtElem.setRequired(required);
    }

    /* ***********************************************************************
     *  Extensibility methods
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#setExtensionAttribute(javax.xml.namespace.QName, org.apache.woden.xml.XMLAttr)
     */
    public void setExtensionAttribute(QName attrType, XMLAttr attr) {
        fAttrExt.setExtensionAttribute(attrType, attr);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttribute(javax.xml.namespace.QName)
     */
    public XMLAttr getExtensionAttribute(QName attrType) {
        return fAttrExt.getExtensionAttribute(attrType);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributesForNamespace(java.net.URI)
     */
    public XMLAttr[] getExtensionAttributesForNamespace(URI namespace) {
        return fAttrExt.getExtensionAttributesForNamespace(namespace);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributes()
     */
    public XMLAttr[] getExtensionAttributes() {
        return fAttrExt.getExtensionAttributes();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#hasExtensionAttributesForNamespace(java.net.URI)
     */
    public boolean hasExtensionAttributesForNamespace(URI namespace) {
        return fAttrExt.hasExtensionAttributesForNamespace(namespace);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#addExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void addExtensionElement(ExtensionElement extEl) {
        fElemExt.addExtensionElement(extEl);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#removeExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void removeExtensionElement(ExtensionElement extEl) {
        fElemExt.removeExtensionElement(extEl);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElements()
     */
    public ExtensionElement[] getExtensionElements() {
        return fElemExt.getExtensionElements();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElementsOfType(javax.xml.namespace.QName)
     */
    public ExtensionElement[] getExtensionElementsOfType(QName extType) {
        return fElemExt.getExtensionElementsOfType(extType);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#hasExtensionElementsForNamespace(java.net.URI)
     */
    public boolean hasExtensionElementsForNamespace(URI namespace) {
        return fElemExt.hasExtensionElementsForNamespace(namespace);
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
    /*
     * This method traverses up the element graph to get the root <description>.
     * TODO consider its usefulness to user-defined extensions and whether to refactor it.
     * e.g. declared in ExtensionElement and implemented in ExtensionElementImpl
     */
    private DescriptionElement getDescriptionElement(WSDLElement wsdlElem)
    {
        if(wsdlElem instanceof BindingImpl) 
        {
            return (DescriptionElement) ((NestedElement)wsdlElem).getParentElement();
        }
        else
        {
            WSDLElement parentElem = ((NestedElement)wsdlElem).getParentElement();
            return getDescriptionElement(parentElem);
        }
    }
    
    /* 
     * TODO ditto previous comment about possibly refactoring this for user-defined extensions to reuse. 
     */
    private Description getDescriptionComponent(WSDLComponent wsdlComp)
    {
        if(wsdlComp instanceof BindingImpl) 
        {
            return ((BindingImpl)wsdlComp).getDescriptionComponent();
        }
        else 
        {
            WSDLComponent parentComp = ((NestedComponent)wsdlComp).getParent();
            return getDescriptionComponent(parentComp);
        } 
    }
    
}
