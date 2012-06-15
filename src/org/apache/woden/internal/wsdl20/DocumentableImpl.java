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

import java.util.List;
import java.util.Vector;

import org.apache.woden.wsdl20.xml.DocumentableElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;

/**
 * An abstract superclass for WSDL 2.0 elements which can have &lt;documentation&gt; 
 * child elements. That is, all WSDL 2.0 elements except the &lt;documentation&gt; 
 * element itself. Defines accessor methods for manipulating DocumentationElements.
 * 
 * @author jkaputin@apache.org
 */
public abstract class DocumentableImpl extends WSDLElementImpl
                                       implements DocumentableElement 
{
    private List fDocumentationElements = new Vector();
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.DocumentationElement org.apache.woden.wsdl20.xml.DocumentableElement#addDocumentationElement()
     */
    public DocumentationElement addDocumentationElement() 
    {
        DocumentationImpl docEl = new DocumentationImpl();
        fDocumentationElements.add(docEl);
        docEl.setParentElement(this);
        return docEl;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.DocumentableElement#getDocumentationElements()
     */
    public DocumentationElement[] getDocumentationElements() {
        DocumentationElement[] array = new DocumentationElement[fDocumentationElements.size()];
        fDocumentationElements.toArray(array);
        return array;
    }

}
