/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.woden.wsdl20.fragids;

import org.apache.woden.types.NCName;

/**
 * <code>EndpointPart</code> is a Endpoint Pointer Part for the Endpoint WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.endpoint">http://www.w3.org/TR/wsdl20/#wsdl.endpoint</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class EndpointPart implements ComponentPart {
    private final NCName service;   //Local name of the parent Service component.
    private final NCName endpoint;  //Name of the Endpoint component
    
    /**
     * Constructs an EndpointPart class from the values given.
     * 
     * @param service the local name of the parent Service component.
     * @param endpoint the name of the Endpoint component.
     * @throws IllegalArgumentException if service or endpoint are null.
     */
    public EndpointPart(NCName service, NCName endpoint) {
        if (service == null | endpoint == null) {
            throw new IllegalArgumentException();
        }
        this.service = service;
        this.endpoint = endpoint;
    }
    
    /**
     * Returns a String of the serialised Endpoint Pointer Part.
     * 
     * @return a String the serialised Endpoint Pointer Part.
     */
    public String toString() {
        return "wsdl.endpoint(" + service + "/" + endpoint + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }

}
