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

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.WSDLElement;

/**
 * This abstract class defines the common behaviour for referencing WSDL
 * documents via the &lt;wsdl:import&gt; and &lt;wsdl:include&gt; elements.
 * It is extended by the concrete implementation classes for those two elements.
 * 
 * TODO consider whether to expose a WSDLReferenceElement interface on the API too
 * to provide a common handle for import and include elements (is there a use case?).
 * 
 * @author jkaputin@apache.org
 */
public abstract class WSDLReferenceImpl extends DocumentableImpl 
{
    private List fDocumentationElements = new Vector();
    private URI fLocation = null;
    private DescriptionElement fDescriptionElement = null;
    private WSDLElement fParentElem = null;

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
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ImportElement#setLocation(java.net.URI)
     */
    public void setLocation(URI locURI) {
        fLocation = locURI;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ImportElement#getLocation()
     */
    public URI getLocation() {
        return fLocation;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ImportElement#setDescriptionElement(org.apache.woden.wsdl20.xml.DescriptionElement)
     */
    public void setDescriptionElement(DescriptionElement desc) {
        fDescriptionElement = desc;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ImportElement#getDescriptionElement()
     */
    public DescriptionElement getDescriptionElement() {
        return fDescriptionElement;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.DocumentableElement#addDocumentationElement(org.apache.woden.wsdl20.xml.DocumentationElement)
     */
    public void addDocumentationElement(DocumentationElement docEl) 
    {
        if(docEl != null) {
            fDocumentationElements.add(docEl);
        }
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
