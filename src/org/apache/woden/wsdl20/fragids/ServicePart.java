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
 * <code>ServicePart</code> is a Service Pointer Part for the Service WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.service">http://www.w3.org/TR/wsdl20/#wsdl.service</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class ServicePart implements ComponentPart {
    private final NCName service; //Local name of the Service component.
    
    /**
     * Constructs an ServicePart class from the value given.
     * 
     * @param service the local name of the Service component.
     * @throws IllegalArgumentException if service is null.
     */
    public ServicePart(NCName service) {
        if (service == null) {
            throw new IllegalArgumentException();
        }
        this.service = service;
    }
    
    /**
     * Returns a String of the serialised Service Pointer Part.
     * 
     * @return a String the serialised Service Pointer Part.
     */
    public String toString() {
        return "wsdl.service(" + service + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }

}
