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
import org.apache.woden.wsdl20.extensions.http.HTTPAuthenticationScheme;

/**
 * There are no WSDL 2.0 SOAP extension properties (from the namespace
 * <code>http://www.w3.org/ns/wsdl/soap</code>) attached to the Endpoint component.
 * <p>
 * The purpose of this interface is to maintain consistency across the 
 * WSDL 2.0 SOAP extensions in Woden, which define accessor methods for the 
 * HTTP properties that are added to WSDL 2.0 components, along with 
 * the SOAP properties, when a SOAP binding specifies HTTP as the underlying
 * protocol.
 * <p>
 * For this interface, the generic ExtensionProperty accessor methods, 
 * <code>getProperties</code> and <code>getProperty</code>, return null 
 * (because they only apply to SOAP extension properties, which are not present 
 * for the Endpoint component).
 * <p>
 * This interface defines additional extension-specific accessor methods for the 
 * HTTP extension properties that are added to the Endpoint component by a SOAP binding 
 * when the underlying protocol is HTTP.
 * <p>
 * These are:
 * <ul>
 * <li>{http authentication scheme}</li>
 * <li>{http authentication realm}</li>
 * </ul>
 * <p> 
 * TODO Re HTTP methods, consider WODEN-158 which proposes keeping extension interfaces namespace-specific, not binding-type-specific
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 * 
 */
public interface SOAPEndpointExtensions extends ComponentExtensionContext {

    /**
     * If the SOAP binding specifies HTTP as the underlying protocol, the 
     * {http authentication scheme} property is added to the Endpoint component
     * and it will be returned by this method. 
     * If the underlying protocol is not HTTP, this method will return null.
     * 
     * @return the HTTPAuthenticationScheme if present, otherwise null
     */
	public HTTPAuthenticationScheme getHttpAuthenticationScheme();

    /**
     * If the SOAP binding specifies HTTP as the underlying protocol, the 
     * {http authentication realm} property is added to the Endpoint component
     * and it will be returned by this method. 
     * If the underlying protocol is not HTTP, this method will return null.
     * 
     * @return a String representing the {http authentication realm} property if present, otherwise null.
     */
	public String getHttpAuthenticationRealm();
}
