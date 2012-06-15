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

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.types.QNameTokenUnion;
import org.apache.ws.commons.schema.XmlSchemaElement;

/**
 * Represents the WSDL 2.0 &lt;fault&gt; element, declared as a child of the 
 * &lt;interface&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceFaultElement extends DocumentableElement, 
                                               NestedElement
{
    /**
     * Set the name of this InterfaceFaultElement to the specified NCName.
     * This corresponds to the <code>name</code> attribute of the interface &lt;fault&gt; element.
     * 
     * @param name the NCName that represents the local name of this interface fault
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this InterfaceFaultElement, which consists of its
     * local name and the targetNamespace of the enclosing DescriptionElement.
     * 
     * @return the interface fault QName
     */
    public QName getName();
    
    /**
     * Specify the union of the xs:token and xs:QName of the global schema element declaration referred to by this 
     * InterfaceFaultElement.
     * The specified QName corresponds to the <code>element</code> attribute of the interface 
     * &lt;fault&gt; element.
     *
     * @param elementName the QNameTokenUnion of the element declaration
     */
    public void setElement(QNameTokenUnion elementName);
    
    /**
     * Return the union of the xs:token and xs:QName of the global schema element declaration referred to by this 
     * InterfaceFaultElement.
     * This corresponds to the <code>element</code> attribute of the interface 
     * &lt;fault&gt; element.
     * 
     * @return the QNameTokenUnion of the element declaration
     */
    public QNameTokenUnion getElement();
    
    /**
     * Return the XmlSchemaElement representing the global schema element declaration
     * referred to by this InterfaceFaultElement.
     * This equates to the &lt;xs:element&gt; element referred to by the <code>element</code> 
     * attribute of the interface &lt;fault&gt; element.
     * If this reference cannot be resolved to an element declaration in a schema that 
     * is visible to the enclosing &lt;description&gt;, this method will return null. 
     * To be visible, the schema must have been correctly imported or inlined within 
     * the &lt;types&gt; element.
     * 
     * @return the XmlSchemaElement
     */
    public XmlSchemaElement getXmlSchemaElement();

}
