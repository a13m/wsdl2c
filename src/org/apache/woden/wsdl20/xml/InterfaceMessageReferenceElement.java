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


import org.apache.woden.types.NCName;
import org.apache.woden.types.QNameTokenUnion;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.ws.commons.schema.XmlSchemaElement;

/**
 * Represents the WSDL 2.0 &lt;input&gt; and &lt;output&gt; elements, 
 * declared as child elements of interface &lt;operation&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceMessageReferenceElement extends DocumentableElement,
                                                          NestedElement
{
    /**
     * Set the message label to the specified NCName. 
     * This corresponds to the <code>messageLabel</code> attribute of the interface operation 
     * &lt;input&gt; and &lt;output&gt; elements. 
     * It represents a placeholder message in the message exchange pattern specified by the 
     * parent interface &lt;operation&gt; element.
     * 
     * @param msgLabel the NCName representing the message label
     */
    public void setMessageLabel(NCName msgLabel);
    
    /**
     * Return the NCName representing the message label. 
     * This corresponds to the <code>messageLabel</code> attribute of the interface operation 
     * &lt;input&gt; and &lt;output&gt; elements. 
     * It represents a placeholder message in the message exchange pattern specified by the 
     * parent interface &lt;operation&gt; element.
     * 
     * @return NCName the message label
     */
    public NCName getMessageLabel();
    
    /**
     * Specify the union of the xs:token and xs:QName of the global schema element declaration referred to by this 
     * InterfaceMessageReferenceElement.
     * The specified QNameTokenEnum corresponds to the <code>element</code> attribute of the interface 
     * operation &lt;input&gt; or &lt;output&gt; element.
     *
     * @param element the QNameTokenEnum of the element declaration.
     */
    public void setElement(QNameTokenUnion element);
    
    /**
     * Return the union of the xs:token and xs:QName of the global schema element declaration referred to by this 
     * InterfaceMessageReferenceElement.
     * This corresponds to the <code>element</code> attribute of the interface 
     * operation &lt;input&gt; or &lt;output&gt; element.
     * 
     * @return the QNameTokenEnum of the element declaration
     */
    public QNameTokenUnion getElement();
    
    /**
     * Return the XmlSchemaElement representing the global schema element declaration
     * referred to by this InterfaceMessageReferenceElement.
     * This equates to the &lt;xs:element&gt; element referred to by the <code>element</code> 
     * attribute of the interface operation &lt;input&gt; or &lt;output&gt; element.
     * If this reference cannot be resolved to an element declaration in a schema that 
     * is visible to the enclosing &lt;description&gt;, this method will return null. 
     * To be visible, the schema must have been correctly imported or inlined within 
     * the &lt;types&gt; element.
     * 
     * @return the XmlSchemaElement
     */
    public XmlSchemaElement getXmlSchemaElement();
    
    /**
     * Set the message direction using the specified enumerated type, Direction,
     * which indicates the direction of the message.
     * Direction.IN corresponds to the interface operation &lt;input&gt; element.
     * Direction.OUT corresponds to the interface operation &lt;output&gt; element.
     * 
     * @param dir the Direction of the message
     */
    public void setDirection(Direction dir);
    
    /**
     * Returns an enumerated type, Direction, that indicates the direction of this message.
     * Direction.IN corresponds to the interface operation &lt;input&gt; element.
     * Direction.OUT corresponds to the interface operation &lt;output&gt; element.
     * 
     * @return the Direction of the message
     */
    public Direction getDirection();

}
