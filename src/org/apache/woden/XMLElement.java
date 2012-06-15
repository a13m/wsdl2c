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
package org.apache.woden;

import java.net.URI;

import javax.xml.namespace.QName;

/**
 * This interface represents an XML element information item in a format to 
 * be interpreted by the Woden implementation. 
 * It permits different representations of an element to be used as method
 * arguments or return values without making the Woden API dependent on 
 * any particular XML parser or XML object model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface XMLElement {

    /**
     * Accepts an Object representing an XML element. The implementation should 
     * check that it is of a type appropriate for the underlying XML parser or
     * XML Object model being used. For example, a DOM implementation might expect
     * an org.w3c.dom.Element while an AXIOM implementation might expect 
     * org.apache.axiom.om.OMElement.
     * 
     * @param elem the Object representing the XML element
     * 
     * @throws IllegalArgumentException if elem is not a type supported by the implementation.
     */
    public void setSource(Object elem);

    /**
     * Returns an Object representing an XML element, which the caller must
     * cast to the expected type. For example, for DOM implementation we might
     * cast it to an org.w3c.dom.Element whereas an AXIOM implementation might 
     * cast it to an org.apache.axiom.om.OMElement.
     * 
     * @return an Object representing the XML element 
     */
    public Object getSource();


    /* **********************************************************************
     * Methods for Accessing the contents of the element
     * **********************************************************************/

    /* TODO This method is not yet needed but can be added if it's required.
     * It will require the creation of XMLAttribute with the methods getLocalName,
     * getNamespaceURI, getPrefix and getValue. Then, the parseExtensionAttributes
     * method can be refactored into BaseWSDLReader. Will also need to consider
     * overlap/synergy with the XMLAttr classes used for extension attributes.
     * 
     * @return a list of attributes associated with the XML element
     *
    public XMLAttribute[] getAttributes();
    */

    /**
     * Returns the value of the specified attribute or null if it is not found.
     *
     * @param attrName name of attribute to look for
     * @return the attribute value including prefix if present
     */
    public String getAttributeValue(String attrName);

    /**
     *
     * @return the namespace URI of the element or null if it has no namespace
     */
    public URI getNamespaceURI() throws WSDLException;

    /**
     *
     * @return the local name of the element
     */
    public String getLocalName();

    /**
     * Return the qualified name of this element.
     * 
     * @return QName this element's qualified name.
     */
    public QName getQName();
    /**
     * Return the qualified name from the specified prefixed value.
     *
     * @param prefixedValue of the form [namespace prefix]:[local name]
     * @return the QName corresponding to the prefix
     * @throws WSDLException
     */
    public QName getQName(String prefixedValue) throws WSDLException;

    /**
     * Return the first child element of this element or null if no
     * children are found.
     *
     * @return the first child element.
     */
    public XMLElement getFirstChildElement();

    /**
     * Return the next sibling element of this element or null if no
     * siblings are found.
     *
     * @return the next sibling element.
     */
    public XMLElement getNextSiblingElement();

    /**
     * Return an array of the child elements of this element or
     * an empty array if no siblings are found.
     *
     * @return an XMLElement array containing the child elements.
     */
    public XMLElement[] getChildElements();


}
