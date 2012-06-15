/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.axiom.om;

public interface OMDocument extends OMContainer {

    /** Field XML_10 XML Version 1.0 */
    final static String XML_10 = "1.0";

    /** Field XML_11 XML Version 1.1 */
    final static String XML_11 = "1.1";

    /**
     * Returns the document element.
     *
     * @return Returns OMElement.
     */
    OMElement getOMDocumentElement();

    /**
     * Sets the document element of the XML document.
     *
     * @param rootElement
     */
    // TODO: this method and its implementations need review:
    //        - LLOM doesn't add the element as a child (!!!)
    //        - Neither LLOM nor DOOM updates the parent of the element
    // Note that OMSourcedElementImpl seems to depend on this behavior
    void setOMDocumentElement(OMElement rootElement);

    /**
     * Get the character set encoding scheme. This is the encoding that was used used for this
     * document at the time of the parsing. This is <code>null</code> when it is not known, such as
     * when the document was created in memory or from a character stream.
     * 
     * @return the charset encoding for this document, or <code>null</code> if the encoding is not
     *         known
     */
    String getCharsetEncoding();

    /**
     * Sets the character set encoding scheme to be used.
     *
     * @param charsetEncoding
     */
    void setCharsetEncoding(String charsetEncoding);

    /**
     * Returns the XML version.
     *
     * @return Returns String.
     */
    String getXMLVersion();

    /**
     * Sets the XML version.
     *
     * @param version
     * @see org.apache.axiom.om.impl.llom.OMDocumentImpl#XML_10 XML 1.0
     * @see org.apache.axiom.om.impl.llom.OMDocumentImpl#XML_11 XML 1.1
     */
    void setXMLVersion(String version);

    /**
     * Get the charset encoding of this document as specified in the XML declaration.
     * 
     * @return the charset encoding specified in the XML declaration, or <code>null</code> if the
     *         document didn't have an XML declaration or if the <code>encoding</code> attribute was
     *         not specified in the XML declaration
     */
    String getXMLEncoding();

    /**
     * Set the charset encoding for the XML declaration of this document.
     * 
     * @param encoding
     *            the value of the <code>encoding</code> attribute of the XML declaration
     */
    void setXMLEncoding(String encoding);
    
    /**
     * XML standalone value. This will be yes, no or null (if not available)
     *
     * @return Returns boolean.
     */
    String isStandalone();

    void setStandalone(String isStandalone);
}
