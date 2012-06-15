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

import java.net.URI;

import javax.xml.namespace.QName;

public class SOAPConstants {

    // Extension namespace
    public static final String NS_STRING_SOAP = "http://www.w3.org/ns/wsdl/soap";
    public static final URI NS_URI_SOAP = URI.create(NS_STRING_SOAP);
    
    // Extension namespace prefix
    public static final String PFX_WSOAP = "wsoap";

    // Extension element names.
    public static final String ELEM_HEADER = "header";
    public static final String ELEM_MODULE = "module";
    
    // Extension attribute names
    public static final String ATTR_ACTION = "action";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_MEP = "mep";
    public static final String ATTR_MEPDEFAULT = "mepDefault";
    public static final String ATTR_MUSTUNDERSTAND = "mustUnderstand";
    public static final String ATTR_PROTOCOL = "protocol";
    public static final String ATTR_SUBCODES = "subcodes";
    public static final String ATTR_VERSION = "version";
    
    // Extension element QNames
    public static final QName Q_ELEM_SOAP_HEADER = new QName(NS_STRING_SOAP, ELEM_HEADER, PFX_WSOAP);
    public static final QName Q_ELEM_SOAP_MODULE = new QName(NS_STRING_SOAP, ELEM_MODULE, PFX_WSOAP);
    
    // Extension attribute QNames
    public static final QName Q_ATTR_SOAP_ACTION = new QName(NS_STRING_SOAP, ATTR_ACTION, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_CODE = new QName(NS_STRING_SOAP, ATTR_CODE, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_MEP = new QName(NS_STRING_SOAP, ATTR_MEP, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_MEPDEFAULT = new QName(NS_STRING_SOAP, ATTR_MEPDEFAULT, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_PROTOCOL = new QName(NS_STRING_SOAP, ATTR_PROTOCOL, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_SUBCODES = new QName(NS_STRING_SOAP, ATTR_SUBCODES, PFX_WSOAP);
    public static final QName Q_ATTR_SOAP_VERSION = new QName(NS_STRING_SOAP, ATTR_VERSION, PFX_WSOAP);
    
    // Extension property names
    public static final String PROP_SOAP_ACTION = "soap action";
    public static final String PROP_SOAP_FAULT_CODE = "soap fault code";
    public static final String PROP_SOAP_FAULT_SUBCODES = "soap fault subcodes";
    public static final String PROP_SOAP_HEADERS = "soap headers";
    public static final String PROP_SOAP_MEP = "soap mep";
    public static final String PROP_SOAP_MEP_DEFAULT = "soap mep default";
    public static final String PROP_SOAP_MODULES = "soap modules";
    public static final String PROP_SOAP_UNDERLYING_PROTOCOL = "soap underlying protocol";
    public static final String PROP_SOAP_VERSION = "soap version";

    // Protocol values
    public static final String PROTOCOL_STRING_SOAP11_HTTP = "http://www.w3.org/2006/01/soap11/bindings/HTTP/";
    public static final String PROTOCOL_STRING_SOAP12_HTTP = "http://www.w3.org/2003/05/soap/bindings/HTTP/";
    public static final URI PROTOCOL_URI_SOAP11_HTTP = URI.create(PROTOCOL_STRING_SOAP11_HTTP);
    public static final URI PROTOCOL_URI_SOAP12_HTTP = URI.create(PROTOCOL_STRING_SOAP12_HTTP);
    
    // Version values
    public static final String VERSION_1_1 = "1.1";
    public static final String VERSION_1_2 = "1.2";
    

}
