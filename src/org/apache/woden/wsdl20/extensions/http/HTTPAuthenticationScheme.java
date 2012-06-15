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

/**
 * This class defines the values of the {http authentication scheme} property of
 * Endpoint as defined by the HTTP Binding extension. This property indicates
 * the HTTP authentication scheme used.
 * 
 * <p>
 * The property is one of:
 * <ul>
 * <li>basic</li>
 * <li>digest</li
 * </ul>
 * </p>
 * 
 * <p>
 * This class uses the typesafe enum pattern. Applications should use the public
 * static final constants defined in this class to specify or to evaluate the
 * HTTP authentication scheme.
 * </p>
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 */
public class HTTPAuthenticationScheme {

	private final String fValue;

	private HTTPAuthenticationScheme(String value) {
		fValue = value;
	}

	public String toString() {
		return fValue;
	}

	public static final HTTPAuthenticationScheme BASIC = new HTTPAuthenticationScheme(
			"basic");

	public static final HTTPAuthenticationScheme DIGEST = new HTTPAuthenticationScheme(
			"digest");
}
