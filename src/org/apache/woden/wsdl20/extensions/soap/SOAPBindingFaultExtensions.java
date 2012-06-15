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

import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.http.HTTPHeader;

/**
 * Provides access to the extension properties of the Binding Fault component 
 * that are in the <code>http://www.w3.org/ns/wsdl/soap</code> namespace.
 * These extension properties can be accessed as <code>ExtensionProperty</code> objects 
 * via the <code>getProperties</code> and <code>getProperty</code> methods  
 * using the property names and Java types shown in the following table.
 * <p>
 * <table border="1">
 * <tr>
 * <th>Property name</th>
 * <th>Java type</th>
 * </tr>
 * <tr>
 * <td>soap fault code</td>
 * <td>org.apache.woden.wsdl20.extensions.soap.SOAPFaultCode</td>
 * </tr>
 * <tr>
 * <td>soap fault subcodes</td>
 * <td>org.apache.woden.wsdl20.extensions.soap.SOAPFaultSubcodes</td>
 * </tr>
 * <tr>
 * <td>soap modules</td>
 * <td>org.apache.woden.wsdl20.extensions.soap.SOAPModule[]</td>
 * </tr>
 * <tr>
 * <td>soap headers</td>
 * <td>org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock[]</td>
 * </tr>
 * </table>
 * <p>
 * In addition to the <code>getProperties</code> and <code>getProperty</code> methods, 
 * this interface defines accessor methods specific to each SOAP extension property. 
 * It also provides accessor methods for some additional HTTP extension properties 
 * that are present in a SOAP binding when the underlying protocol is HTTP.
 * <p>
 * These are:
 * <ul>
 * <li>{http content encoding}</li>
 * <li>{http headers}</li>
 * </ul>
 * <p> 
 * TODO Re HTTP methods, consider WODEN-158 which proposes keeping extension interfaces namespace-specific, not binding-type-specific
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface SOAPBindingFaultExtensions extends ComponentExtensionContext 
{
    /**
     * Returns an object representing the {soap fault code} property, which may
     * contain either an xs:QName or the xs:token "#any".
     */
    public SOAPFaultCode getSoapFaultCode();
    
    /**
     * Returns an object representing the {soap fault subcodes} property, 
     * which contains a List of xs:QName or the xs:token "#any".
     */
    public SOAPFaultSubcodes getSoapFaultSubcodes();
    
    public SOAPModule[] getSoapModules();

    public SOAPHeaderBlock[] getSoapHeaders();
    
    public String getHttpContentEncoding();
    
    public HTTPHeader[] getHttpHeaders();
    
}
