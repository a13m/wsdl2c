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
package org.apache.woden.wsdl20.extensions.http;


/**
 * This class represents a template specified within the HTTP Location string used as the value
 * of the <code>whttp:location</code> extension attribute. The template cites an element from 
 * the instance data of the message to be serialized in the request IRI by enclosing the element's
 * local name in curly braces. The template is then matched against an element in the instance
 * data and replaced in the HTTP Location string by that element's String value.
 * <p>
 * For example, the template "{postcode}" is matched to the instance data element
 * &lt;postcode&gt;90210&lt;/postcode&gt; and the String value "90210" replaces "{postcode}"
 * in the formatted HTTP Location string.
 * <p>
 * The WSDL 2.0 HTTP binding extension requires certain encoding be performed on the content of
 * the HTTP Location string. Templates of the form "{localname}" are called <it>encoded</it> 
 * templates because these encoding rules also apply to any element values substituted
 * for these templates. A <it>raw</it> template is used to indicate that encoding is not to
 * be performed on the substituted value. Raw templates are identified by the exclamated curly 
 * brace syntax "{!localname}".
 * <p>
 * A template that appears in the HTTP Location String before the first occurrence of '?' is
 * in the URI Path portion of the HTTP Location. One that appears after the first occurrence of
 * '?' is in the URI Query portion.
 * <p>
 * This class has a single constructor which takes three parameters that indicate the local name 
 * of the element cited in the template, whether the template is encoded and whether it appears 
 * in the Query portion of the HTTP Location.
 * <p>
 * The class has the following characteristics:
 * <ul>
 * <li>
 * It returns the element local name cited by the template.
 * </li>
 * <li>
 * It does not permit the cited element name to be modified.
 * </li>
 * <li>
 * It accepts a String value for the element cited by the template.
 * </li>
 * <li>
 * It differentiates an encoded template from a raw template.
 * </li>
 * <li>
 * It differentiates a template that appears in the Path from one that appears in the Query.
 * </li>
 * </ul>
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class HTTPLocationTemplate {
    
    private String fName;
    private String fValue = null;
    private boolean fIsEncoded;
    private boolean fIsQuery;
    
    /**
     * Creates an HTTP Location template that cites the specified element. The three parameters must
     * be specified, they cannot be null.
     *  
     * @param name a String representing the local name of the element cited by this template.
     * @param isEncoded a boolean value 'true' if it is an encoded template or 'false' if it is a
     *        raw template.
     * @param isQuery a boolean value 'true' if the template appears in the URI Query portion of the 
     *        HTTP Location string or 'false' if it appears in the URI Path.
     *        So 'true' means it appears after first occurrence of '?'.
     */
    public HTTPLocationTemplate(String name, boolean isEncoded, boolean isQuery) {
        
        //TODO - check syntax, not null, not empty string...throw IllegalArgExc
        
        fName = name;
        fIsEncoded = isEncoded;
        fIsQuery = isQuery;
    }
    
    /**
     * Returns a String representing the local name of the element cited by this template.
     */
    public String getName() {
        return fName;
    }
    
    /**
     * Returns a String representing the value of the element cited by this template.
     */
    public String getValue() {
        return fValue;
    }
    
    /**
     * Sets the String value of the element cited by this template. This is the String 
     * substituted for the template in the formatted HTTP Location string.
     * 
     * @param value the cited element's String value
     */
    public void setValue(String value) {
        fValue = value;
    }
    
    /**
     * Returns 'true' if the template is encoded, otherwise 'false'. 
     * That is, 'true' if it is of the form "{localname}".
     * So, 'false' indicates a 'raw' template of the form "{!localname}".
     */
    public boolean isEncoded() {
        return fIsEncoded;
    }
    
    /**
     * Returns 'true' if the template appears in the URI Query portion of the HTTP Location string,
     * otherwise false. That is, 'true' if it appears after the first occurrence of '?'. 
     * So, 'false' indicates a template that appears in the URI Path portion of the HTTP Location 
     * string.
     */
    public boolean isQuery() {
        return fIsQuery;
    }
    
}
