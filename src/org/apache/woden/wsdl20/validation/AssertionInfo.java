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



/**
 * Represents an immutable object containing the information about an assertion 
 * which is needed to perform WSDL validation. 
 * This information consists of the Assertion object and the target Class
 * for the WSDL component that this assertion applies to.
 * <p>
 * TODO assertion dependencies to be added later.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 *
 */
public class AssertionInfo {

    public final Assertion assertion;
    public final Class targetClass;
    //TODO private List dependencies;
    
    public AssertionInfo(Assertion assertion, Class targetClass) {
        this.assertion = assertion;
        this.targetClass = targetClass;
    }
    
}
