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

/**
 * Represents WSDL 2.0 elements that can have &lt;documentation&gt; child elements.
 * That is, all WSDL 2.0 elements except the &lt;documentation&gt; element itself.
 * The Java interfaces that correspond to these WSDL elements will extend this 
 * interface to inherit the behaviour for getting, adding and removing 
 * DocumentationElements. 
 * <p>
 * TODO a removeDocmentationElement method
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface DocumentableElement extends WSDLElement 
{
    /**
     * Create a DocumentationElement with this WSDLElement as its parent and
     * return a reference to it.
     * This equates to adding a &lt;documentation&gt; element to any of the other
     * WSDL elements (except the &lt;documentation&gt; element itself).
     * 
     * @return the DocumentationElement
     */
    public DocumentationElement addDocumentationElement();
    
    /**
     * Return the set of DocumentationElements defined directly within this WSDLElement. 
     * This equates to the set of &lt;documentation&gt; elements declared within a WSDL 
     * element.
     * If no DocumentationElements exist, an empty array is returned.
     * 
     * @return array of DocumentationElement
     */
    public DocumentationElement[] getDocumentationElements();
    
    //TODO a removeDocmentationElement method
}
