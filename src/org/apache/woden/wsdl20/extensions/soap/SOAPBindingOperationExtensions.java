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

import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.http.HTTPLocation;

/**
 * Provides access to the extension properties of the Binding Operation component 
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
 * <td>soap mep</td>
 * <td>java.net.URI</td>
 * </tr>
 * <tr>
 * <td>soap action</td>
 * <td>java.net.URI</td>
 * </tr>
 * <td>soap modules</td>
 * <td>org.apache.woden.wsdl20.extensions.soap.SOAPModule[]</td>
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
 * <li>{http location}</li>
 * <li>{http query parameter separator}</li>
 * <li>{http content encoding default}</li>
 * </ul>
 * <p> 
 * TODO Re HTTP methods, consider WODEN-158 which proposes keeping extension interfaces namespace-specific, not binding-type-specific
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface SOAPBindingOperationExtensions extends ComponentExtensionContext 
{
    /**
     * Returns an object representing the {soap mep} property, of type xs:anyURI.
     */
    public URI getSoapMep();
    
    /**
     * Returns an object representing the {soap action} property, of type xs:anyURI.
     */
    public URI getSoapAction();
    
    /**
     * Returns an array representing the {soap modules} property, of type wsoap:module.
     */
    public SOAPModule[] getSoapModules();

    /**
     * If the SOAP version is "1.1" or "1.2" and the underlying protocol is HTTP, returns the
     * {http location} extension property represented by the 
     * whttp:location extension attribute , otherwise null. 
     * 
     * @return HTTPLocation the {http location} extension property
     */
    public HTTPLocation getHttpLocation();
    
    /**
     * If the SOAP version is "1.1" or "1.2" and the underlying protocol is HTTP, returns the
     * {http query parameter separator} extension property represented by the 
     * whttp:queryParameterSeparator extension attribute , otherwise null. 
     * 
     * @return String the {http query parameter separator} extension property
     */
    public String getHttpQueryParameterSeparator();
    
    /**
     * If the SOAP version is "1.1" or "1.2" and the underlying protocol is HTTP, returns the
     * {http content encoding default} extension property represented by the 
     * whttp:contentEncodingDefault extension attribute , otherwise null. 
     * 
     * @return String the {http content encoding default} extension property
     */
    public String getHttpContentEncodingDefault();
    
}
