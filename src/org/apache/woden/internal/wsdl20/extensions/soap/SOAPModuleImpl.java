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
package org.apache.woden.internal.wsdl20.extensions.soap;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.internal.wsdl20.extensions.AttributeExtensibleImpl;
import org.apache.woden.internal.wsdl20.extensions.ElementExtensibleImpl;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.soap.SOAPModule;
import org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.XMLAttr;

/**
 * This class represents the SOAPModule Component and the &lt;wsoap:module&gt; 
 * extension element that can appear within a Binding, Binding Fault, 
 * Binding Operation, Binding Fault Reference or Binding Message Reference.
 * 
 * @author jkaputin@apache.org
 */
public class SOAPModuleImpl implements SOAPModule, SOAPModuleElement 
{
    private WSDLElement fParent = null;
    private List fDocumentationElements = new Vector();
    private QName fExtElementType = null;
    private Boolean fRequired = null;
    private AttributeExtensibleImpl fAttrExt = new AttributeExtensibleImpl();
    private ElementExtensibleImpl fElemExt = new ElementExtensibleImpl();
    private URI fRef = null;
    

    /* ***********************************************************************
     *  Component model methods (SOAPModule interface), shared with Element model
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModule#getRef()
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#getRef()
     */
    public URI getRef() {
        return fRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModule#isRequired()
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#isRequired()
     */
    public Boolean isRequired() {
        return fRequired;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModule#getParent()
     */
    public WSDLComponent getParent() {
        return (WSDLComponent)fParent;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModule#toElement()
     */
    public SOAPModuleElement toElement() {
        return this;
    }
    

    /* ***********************************************************************
     *  Element model-only methods (SOAPModuleElement interface)
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#setRef(java.net.URI)
     */
    public void setRef(URI uri) {
        fRef = uri;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#setParentElement(org.apache.woden.wsdl20.xml.WSDLElement)
     */
    public void setParentElement(WSDLElement wsdlEl) {
        fParent = wsdlEl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#getParentElement()
     */
    public WSDLElement getParentElement() {
        return fParent;
    }
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#addDocumentationElement(org.apache.woden.wsdl20.xml.DocumentationElement)
     */
    public void addDocumentationElement(DocumentationElement docEl) 
    {
        if(docEl != null) {
            fDocumentationElements.add(docEl);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPModuleElement#getDocumentationElements()
     */
    public DocumentationElement[] getDocumentationElements() 
    {
        DocumentationElement[] array = new DocumentationElement[fDocumentationElements.size()];
        fDocumentationElements.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setElementQName(javax.xml.namespace.QName)
     */
    public void setExtensionType(QName qname) {
        fExtElementType = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#getElementQName()
     */
    public QName getExtensionType() {
        return fExtElementType;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setRequired(java.lang.Boolean)
     */
    public void setRequired(Boolean required) {
        fRequired = required;
    }
    
    /* ***********************************************************************
     *  Extensibility methods
     * ***********************************************************************/

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
    public ExtensionElement[] getExtensionElementsOfType(QName extType) 
    {
        return fElemExt.getExtensionElementsOfType(extType);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#hasExtensionElementsForNamespace(java.net.URI)
     */
    public boolean hasExtensionElementsForNamespace(URI namespace) 
    {
        return fElemExt.hasExtensionElementsForNamespace(namespace);
    }

}
