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
package org.apache.woden.wsdl20.extensions.soap;

import javax.xml.namespace.QName;

/**
 * This class represents the {soap fault code} property that forms part of the 
 * SOAP extensions to the WSDL <code>BindingFault</code> component.
 * This property may contain either a QName representing the code or the xs:token #any.
 * <p>
 * This class will restrict the possible values to a QName reference or the string "#any".
 * It provides methods to query whether the property contains a QName or a token and 
 * methods to retrieve the property value of the appropriate type.
 * 
 * @author jkaputin@apache.org
 */
public class SOAPFaultCode 
{
    private final String fToken;
    private final QName fCodeQN;
    public static final SOAPFaultCode ANY = new SOAPFaultCode("#any");
    
    private SOAPFaultCode(String token) 
    { 
        fToken = token; 
        fCodeQN = null;
    }
    
    public SOAPFaultCode(QName codeQN) 
    { 
        fToken = null; 
        fCodeQN = codeQN;
    }
    
    public boolean isQName() {return fCodeQN != null;}
    
    public boolean isToken() {return fToken != null;}
    
    public QName getQName() {return fCodeQN;}
    
    public String getToken() {return fToken;}
    
}
