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
 * <code>InterfacePart</code> is a Interface Pointer Part for the Interface WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.interface">http://www.w3.org/TR/wsdl20/#wsdl.interface</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class InterfacePart implements ComponentPart {
    private final NCName interfaceName;     //Local name of the parent Interface component.

    /**
     * Constructs a InterfacePart class from the value given.
     * 
     * @param interfaceName the local name of the Interface component.
     * @throws IllegalArgumentException if interfaceName is null.
     */
    public InterfacePart(NCName interfaceName) {
        if (interfaceName == null) {
            throw new IllegalArgumentException();
        }
        this.interfaceName = interfaceName;
    }
    
    /**
     * Returns a String of the serialised Interface Pointer Part.
     * 
     * @return a String the serialised Interface Pointer Part.
     */
    public String toString() {
        return "wsdl.interface(" + interfaceName + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }
}
