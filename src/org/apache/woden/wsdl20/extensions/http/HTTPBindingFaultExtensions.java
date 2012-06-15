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

import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;

/**
 * Provides access to the extension properties of the Binding Fault component 
 * that are in the <code>http://www.w3.org/ns/wsdl/http</code> namespace.
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
 * <td>http error status code</td>
 * <td>org.apache.woden.wsdl20.extensions.http.HTTPErrorStatusCode</td>
 * </tr>
 * <tr>
 * <td>http content encoding</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http headers</td>
 * <td>org.apache.woden.wsdl20.extensions.http.HTTPHeader[]</td>
 * </tr>
 * </table>
 * <p>
 * In addition to the <code>getProperties</code> and <code>getProperty</code> methods, 
 * this interface defines accessor methods specific to each HTTP extension property. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface HTTPBindingFaultExtensions extends ComponentExtensionContext 
{
    public HTTPErrorStatusCode getHttpErrorStatusCode();
    
    public String getHttpContentEncoding();
    
    public HTTPHeader[] getHttpHeaders();
    
}
