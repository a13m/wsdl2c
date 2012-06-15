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
package org.apache.woden.wsdl20.extensions.rpc;

import java.net.URI;

import javax.xml.namespace.QName;

public class RPCConstants {

    // Extension namespace
    public static final String NS_STRING_RPC = "http://www.w3.org/ns/wsdl/rpc";
    public static final URI NS_URI_RPC = URI.create(NS_STRING_RPC);
    
    // Style
    public static final String STYLE_STRING_RPC = "http://www.w3.org/ns/wsdl/style/rpc";
    public static final URI STYLE_URI_RPC = URI.create(STYLE_STRING_RPC);

    // Extension namespace prefix
    public static final String PFX_WRPC = "wrpc";

    // Extension attribute names
    public static final String ATTR_SIGNATURE = "signature";
    
    // Extension attribute QNames
    public static final QName Q_ATTR_RPC_SIGNATURE = new QName(NS_STRING_RPC, ATTR_SIGNATURE, PFX_WRPC);
    
    // Extension property names
    public static final String PROP_RPC_SIGNATURE = "rpc signature";
}
