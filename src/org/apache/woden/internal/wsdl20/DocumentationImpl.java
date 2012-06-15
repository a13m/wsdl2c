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

import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.XMLElement;

/**
 * This class implements support for parsing, creating and manipulating a
 * WSDL 2.0 &lt;wsdl:documentation&gt; XML element.
 * The &lt;wsdl:documentation&gt; element may contain mixed content, but this 
 * class does not attempt to understand that content. Instead it just wraps
 * the &lt;wsdl:documentation&gt; element's content model as a java.lang.Object. 
 * 
 * TODO chg inheritance hierachy so this too extends WSDL20ObjectImpl? 
 * (i.e. move documentation methods from WSDL20ObjectImpl to a DocumentableImpl class)
 * Currently, this class is a WSDL20Element (interface) but it is not a subclass of
 * WSDL20ObjectImpl.
 * 
 * @author jkaputin@apache.org
 */
public class DocumentationImpl extends WSDLElementImpl implements DocumentationElement 
{
    private XMLElement fContent;
    private WSDLElement fParentElem = null;
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.DocumentationElement#setContentModel(java.lang.Object)
     */
    public void setContent(XMLElement docEl) {
        fContent = docEl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.DocumentationElement#getContentModel()
     */
    public XMLElement getContent() {
        return fContent;
    }

    /* 
     * package private, used only by factory methods in this package
     */
    void setParentElement(WSDLElement parent) {
        fParentElem = parent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.NestedElement#getParentElement()
     */
    public WSDLElement getParentElement() {
        return fParentElem;
    }
}
