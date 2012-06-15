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
 * Represents the WSDL 2.0 &lt;documentation&gt; element.
 * <p>
 * The &lt;documentation&gt; element may contain mixed content, but this 
 * interface does not define methods that interpret that content. Instead it just wraps
 * the &lt;documentation&gt; element as a java.lang.Object.
 * <p>
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */

import org.apache.woden.XMLElement;

public interface DocumentationElement extends NestedElement {
    
    /**
     * Sets the documentationElement XMLElement for this DocumentationElement.
     * 
     * @param documentationElement A XMLElement representing the DocumentationElement xml element.
     */
    public void setContent(XMLElement documentationElement);
    
    /**
     * Returns a XMLElement representing the DocumentationElement xml element.
     * 
     * @return XMLElement the xml representation of the DocumentationElement.
     */
    public XMLElement getContent();
}
