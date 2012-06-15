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

/**
 * <code>InterfaceOperationExtensions</code> represents the WSDL 2.0
 * predefined extensions, as specified by WSDL 2.0 Part 2: Adjuncts, for the Interface
 * Operation component.
 * <p>
 * Provides access to the extension properties of the Interface Operation component
 * that are in the <code>http://www.w3.org/ns/wsdl-extensions</code> namespace.
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
 * <td>safe</td>
 * <td>java.lang.Boolean</td>
 * </tr>
 * </table>
 * <p>
 * In addition to the <code>getProperties</code> and <code>getProperty</code> methods, 
 * this interface defines accessor methods specific to each extension property. 
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com)
 * 
 */
public interface InterfaceOperationExtensions extends ComponentExtensionContext {

	/**
	 * Returns the value of the {safe} extension property of Interface
	 * Operation as defined by the <code>wsdlx:safe</code> attribute.
	 */
	public boolean isSafe();

}
