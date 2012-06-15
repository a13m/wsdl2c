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
package org.apache.woden.wsdl20.extensions;

import java.net.URI;

import javax.xml.namespace.QName;


public class WSDLExtensionConstants {

    // Extension namespace Strings for extensions defined by WSDL 2.0
    public static final String NS_STRING_WSDL_EXTENSIONS = "http://www.w3.org/ns/wsdl-extensions".intern();        

    // Extension namespace URIs for extensions defined by WSDL 2.0
    public static final URI NS_URI_WSDL_EXTENSIONS = URI.create(NS_STRING_WSDL_EXTENSIONS);
    
    // Extension namespace prefixes
    public static final String PFX_WSDLX = "wsdlx";

    // Extension attribute names
    public static final String ATTR_SAFE = "safe";
    
    // Extension attribute QNames
    public static final QName Q_ATTR_SAFE = new QName(NS_STRING_WSDL_EXTENSIONS, ATTR_SAFE, PFX_WSDLX);
    
    // Extension property names.
    public static final String PROP_SAFE = "safe";
}
