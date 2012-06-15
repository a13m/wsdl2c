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
package org.apache.woden.wsdl20.enumeration;

import org.apache.woden.types.NCName;


/**
 * This class defines the values of the {message label} property of
 * InterfaceMessageReference and InterfaceFaultReference as used in the
 * WSDL 2.0 specification. Other NCName values are also valid. 

 * @author jkaputin@apache.org, hughesj@apache.org
 */
public class MessageLabel 
{
    public static final NCName IN = new NCName("In");
    public static final NCName OUT = new NCName("Out");
}
