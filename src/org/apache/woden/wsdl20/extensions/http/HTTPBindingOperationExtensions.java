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
 * Provides access to the extension properties of the Binding Operation component 
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
 * <td>http location</td>
 * <td>org.apache.woden.wsdl20.extensions.http.HTTPLocation</td>
 * </tr>
 * <tr>
 * <td>http location ignore uncited</td>
 * <td>java.lang.Boolean</td>
 * </tr>
 * <tr>
 * <td>http method</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http input serialization</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http output serialization</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http fault serialization</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http query parameter separator</td>
 * <td>java.lang.String</td>
 * </tr>
 * <tr>
 * <td>http content encoding default</td>
 * <td>java.lang.String</td>
 * </tr>
 * </table>
 * <p>
 * In addition to the <code>getProperties</code> and <code>getProperty</code> methods, 
 * this interface defines accessor methods specific to each HTTP extension property. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 * - added support for {http location ignore uncited}
 */
public interface HTTPBindingOperationExtensions extends ComponentExtensionContext 
{
    /**
     * @return HTTPLocation the {http location} property, represented by the whttp:location extension attribute
     */
    public HTTPLocation getHttpLocation();
    
    /**
     * @return Boolean the {http location ignore uncited} property, represented by the whttp:ignoreUncited extension attribute
     */
    public Boolean isHttpLocationIgnoreUncited();
    
    /**
     * @return String the {http method} property, represented by the whttp:method extension attribute
     */
    public String getHttpMethod();
    
    /**
     * @return String the {http input serialization} property, represented by the whttp:inputSerialization extension attribute
     */
    public String getHttpInputSerialization();
    
    /**
     * @return String the {http output serialization} property, represented by the whttp:outputSerialization extension attribute
     */
    public String getHttpOutputSerialization();
    
    /**
     * @return String the {http fault serialization} property, represented by the whttp:faultSerialization extension attribute
     */
    public String getHttpFaultSerialization();
    
    /**
     * @return String the {http query parameter separator}, represented by the whttp:queryParameterSeparator extension attribute
     */
    public String getHttpQueryParameterSeparator();
    
    /**
     * @return String the {http content encoding default}, represented by the whttp:contentEncodingDefault extension attribute
     */
    public String getHttpContentEncodingDefault();
    
}
