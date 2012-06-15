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

import org.apache.woden.internal.wsdl20.BindingImpl;
import org.apache.woden.internal.wsdl20.TypesImpl;
import org.apache.woden.internal.wsdl20.extensions.AttributeExtensibleImpl;
import org.apache.woden.internal.wsdl20.extensions.ElementExtensibleImpl;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.NestedComponent;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock;
import org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.NestedElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.XMLAttr;
import org.apache.ws.commons.schema.XmlSchemaElement;

/**
 * This class represents the SOAPHeaderBlock Component and the &lt;wsoap:header&gt; 
 * extension element that can appear within a Binding Fault or Binding Message 
 * Reference.
 * 
 * @author jkaputin@apache.org
 */
public class SOAPHeaderBlockImpl implements SOAPHeaderBlock,
                                            SOAPHeaderBlockElement 
{
    private WSDLElement fParent = null;
    private List fDocumentationElements = new Vector();
    private QName fExtElementType = null;
    private Boolean fRequired = null;
    private AttributeExtensibleImpl fAttrExt = new AttributeExtensibleImpl();
    private ElementExtensibleImpl fElemExt = new ElementExtensibleImpl();
    private QName fElementDeclQN = null;
    private Boolean fMustUnderstand = null;
    
    private TypesImpl fTypes = null;


    /* ***********************************************************************
     *  Component model methods (SOAPHeaderBlock interface), some shared with Element model
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock#getElementDeclaration()
     */
    public ElementDeclaration getElementDeclaration() 
    {
        ElementDeclaration elemDecl = null;
        Description desc = getDescriptionComponent(getParent());
        elemDecl = desc.getElementDeclaration(fElementDeclQN);
        return elemDecl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock#mustUnderstand()
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#mustUnderstand()
     */
    public Boolean mustUnderstand() {
        return fMustUnderstand;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock#isRequired()
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#isRequired()
     */
    public Boolean isRequired() {
        return fRequired;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock#getParent()
     */
    public WSDLComponent getParent() {
        return (WSDLComponent)fParent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock#toElement()
     */
    public SOAPHeaderBlockElement toElement() {
        return this;
    }

    
    /* ***********************************************************************
     *  Element model-only methods (SOAPHeaderBlockElement interface)
     * ***********************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#setElementName(javax.xml.namespace.QName)
     */
    public void setElementName(QName qname) {
        fElementDeclQN = qname;

    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#getElementName()
     */
    public QName getElementName() {
        return fElementDeclQN;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#getElement()
     */
    public XmlSchemaElement getElement() 
    {
        XmlSchemaElement xse = null;
        if(fTypes != null) {
            xse = fTypes.getElementDeclaration(fElementDeclQN);
        }
        return xse;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#setMustUnderstand(java.lang.Boolean)
     */
    public void setMustUnderstand(Boolean understood) {
        fMustUnderstand = understood;

    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#setParent(org.apache.woden.wsdl20.xml.WSDLElement)
     */
    public void setParentElement(WSDLElement wsdlEl) {
        fParent = wsdlEl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#getParent()
     */
    public WSDLElement getParentElement() {
        return fParent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#addDocumentationElement(org.apache.woden.wsdl20.xml.DocumentationElement)
     */
    public void addDocumentationElement(DocumentationElement docEl) 
    {
        if(docEl != null) {
            fDocumentationElements.add(docEl);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlockElement#getDocumentationElements()
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

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
    public void setTypes(TypesElement types) {
        fTypes = (TypesImpl)types;
    }

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
