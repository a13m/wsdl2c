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
package org.apache.woden.wsdl20.validation;

import org.apache.woden.WSDLException;


/**
 * Represents an assertion about the WSDL document or component model.
 * This assertion may be defined by the WSDL 2.0 specification or by
 * a WSDL extension specification.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 *
 */
public interface Assertion {
    
    /**
     * Return the assertion identifier. 
     * For example, "Description-1001".
     * The WSDL 2.0 specification defines assertion ids for the WSDL infoset and
     * component model and for the extensions defined in Part 2: Adjuncts
     * (wsdlx, wrpc, wsoap, whttp).
     * Providers of other extensions must define unique identifiers for their 
     * assertions.
     * 
     * @return the String representing the ID of this assertion.
     */
    public String getId();
    
    /**
     * Validates the specified WSDL object against this assertion.
     * 
     * @param target the WSDL object that is the target of the assertion
     * @param wodenCtx WodenContext containing helper objects useful to 
     * Assertion implementors
     */
    public void validate(Object target, WodenContext wodenCtx) throws WSDLException;

}
