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
package org.apache.woden.wsdl20.xml;

import java.net.URI;

import org.apache.woden.WSDLException;
import org.apache.woden.wsdl20.Description;

/**
 * Represents the WSDL 2.0 &lt;description&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface DescriptionElement extends DocumentableElement 
{

    /**
     * Set the document base URI to the specified URI. 
     * This is the base URI used to locate the &lt;description&gt; element that this 
     * DescriptionElement corresponds to.
     * This is used to resolve relative paths specified within this WSDL description that
     * refer to other WSDL or Schema documents. For example, via &lt;xs:import&gt;, &lt;wsdl:import&gt; 
     * or &lt;wsdl:include&gt; elements.
     * 
     * @param documentBaseURI the base URI of the WSDL
     */
    public void setDocumentBaseURI(URI documentBaseURI);
    
    /**
     * Return the document base URI that was used to locate the &lt;description&gt; element that 
     * this DescriptionElement corresponds to. This is used to resolve relative paths within this
     * WSDL description that refer to other WSDL or Schema documents.
     * 
     * @return the document base URI
     */
    public URI getDocumentBaseURI();
    
    /*
     * Attributes and namespaces
     */
    
    /**
     * Set the target namespace to the specified URI. 
     * This corresponds to the <code>targetNamespace</code> attribute of the &lt;description&gt;
     * element.
     * 
     * @param namespaceURI the target namespace URI
     */
    public void setTargetNamespace(URI namespaceURI);
    
    /**
     * Return the target namespace URI.
     * This corresponds to the <code>targetNamespace</code> attribute of the &lt;description&gt;
     * element.
     * 
     * @return the target namespace URI.
     */
    public URI getTargetNamespace();

    /*
     * Element factory methods
     */
    
    /**
     * Create an ImportElement with this DescriptionElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;import&gt; element to the &lt;description&gt; element.
     * 
     * @return the ImportElement
     */
    public ImportElement addImportElement();
    
    /**
     * Create an IncludeElement with this DescriptionElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;include&gt; element to the &lt;description&gt; element.
     * 
     * @return the IncludeElement
     */
    public IncludeElement addIncludeElement();

    /**
     * Create an InterfaceElement with this DescriptionElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;interface&gt; element to the &lt;description&gt; element.
     * 
     * @return the InterfaceElement
     */
    public InterfaceElement addInterfaceElement();

    /**
     * Create a BindingElement with this DescriptionElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;binding&gt; element to the &lt;description&gt; element.
     * 
     * @return the BindingElement
     */
    public BindingElement addBindingElement();

    /**
     * Create a ServiceElement with this DescriptionElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;service&gt; element to the &lt;description&gt; element.
     * 
     * @return the ServiceElement
     */
    public ServiceElement addServiceElement();
    
    /*
     * Element accessor and modifier methods
     * 
     * TODO removeXXX(obj), getXXX(key) methods
     * 
     */
    
    /**
     * Return the set of ImportElements within this DescriptionElement.
     * This equates to the set of &lt;import&gt; elements within the &lt;description&gt; element.
     * If no ImportElements exist, an empty array is returned.
     * 
     * @return an array of ImportElement
     */
    public ImportElement[] getImportElements();
    
    /**
     * Return the set of IncludeElements within this DescriptionElement.
     * This equates to the set of &lt;include&gt; elements within the &lt;description&gt; element.
     * If no IncludeElements exist, an empty array is returned.
     * 
     * @return an array of IncludeElement
     */
    public IncludeElement[] getIncludeElements();
    
    /**
     * Return the TypesElement within this DescriptionElement.
     * This corresponds to the &lt;types&gt; element within the &lt;description&gt; element.
     * If no TypesElement exists, this method will return null.
     * 
     * @return the TypesElement
     */
    public TypesElement getTypesElement();
    
    /**
     * Create a TypesElement with this DescriptionElement as its parent and
     * return a reference to it.
     * If a TypesElement already exists for this DescriptionElement a WSDLException will be thrown.
     * 
     * @return the TypesElement
     */
    public TypesElement addTypesElement() throws WSDLException;
 
    
    /**
     * Return the set of InterfaceElements within this DescriptionElement.
     * This equates to the set of &lt;interface&gt; elements within the &lt;description&gt; element.
     * If no InterfaceElements exist, an empty array is returned.
     * 
     * @return an array of InterfaceElement
     */
    public InterfaceElement[] getInterfaceElements();
    
    /**
     * Return the set of BindingElements within this DescriptionElement.
     * This equates to the set of &lt;binding&gt; elements within the &lt;description&gt; element.
     * If no BindingElements exist, an empty array is returned.
     * 
     * @return an array of BindingElement
     */
    public BindingElement[] getBindingElements();
    
    /**
     * Return the set of ServiceElements within this DescriptionElement.
     * This equates to the set of &lt;service&gt; elements within the &lt;description&gt; element.
     * If no ServiceElements exist, an empty array is returned.
     * 
     * @return an array of ServiceElement
     */
    public ServiceElement[] getServiceElements();

    /**
     * Return the Description component derived from this DescriptionElement.
     * <p>
     * The Description component is unlike other WSDL components which map neatly to a single 
     * WSDL element. Description represents a 'flattened' view of the entire WSDL infoset tree, 
     * including the WSDL components from any imported or included WSDL documents, so it could 
     * contain the content of multiple &lt;description&gt; elements.
     * <p>
     * If this DescriptionElement imports or includes other DescriptionElements (that is,
     * if it is the root of a composite WSDL document), the behaviour of this method is equivalent 
     * to traversing the WSDL tree recursively and aggregating the WSDL content of each 
     * DescriptionElement into a single Description component.
     * <p>
     * This equates to retrieving the root &lt;description&gt; element and traversing any
     * &lt;wsdl:import&gt; or &lt;wsdl:include&gt; elements to retrieve WSDL content of
     * any nested &lt;description&gt; elements. 
     * <p>
     * The <code>toComponent()</code> method is only defined for DescriptionElement, not for 
     * other WSDL Element interfaces. This is because the WSDL 2.0 Component model is a nested
     * hierarchy with a single Description component at the top. To access WSDL components,
     * we must start at the Description and traverse the component model from there.

     * @return the Description component derived from this DescriptionElement
     */
    public Description toComponent();
    
    
}
