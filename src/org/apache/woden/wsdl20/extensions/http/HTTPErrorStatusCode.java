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
 * This class represents the {http error status code} property that forms part of the 
 * HTTP extensions to the WSDL <code>BindingFault</code> component.
 * This property may contain either an integer representing the error status code used
 * with this fault or the xs:token #any indicating that no claim is made by the service
 * about the use of error status codes.
 * <p>
 * It provides methods to query whether the property specifies an error status code
 * (i.e. an integer) and to retrieve the property value.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class HTTPErrorStatusCode 
{
    private final String fToken;
    private final Integer fCode;
    public static final HTTPErrorStatusCode ANY = new HTTPErrorStatusCode("#any");
    
    private HTTPErrorStatusCode(String token) 
    { 
        fToken = token; 
        fCode = null;
    }
    
    public HTTPErrorStatusCode(Integer errorStatusCode) 
    { 
        fToken = null; 
        fCode = errorStatusCode;
    }
    
    public boolean isCodeUsed() {return fCode != null;}
    
    public Integer getCode() {return fCode;}
    
    public String toString() {return isCodeUsed() ? fCode.toString() : fToken;}
    
}
