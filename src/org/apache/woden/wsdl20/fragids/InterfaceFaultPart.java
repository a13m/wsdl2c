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
 * <code>InterfaceFaultPart</code> is a Interface Fault Pointer Part for the Interface Fault WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.interfaceFault">http://www.w3.org/TR/wsdl20/#wsdl.interfaceFault</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class InterfaceFaultPart implements ComponentPart {
    private final NCName interfaceName; //Local name of the parent Interface component.
    private final NCName fault;         //Local name of the Interface Fault component.
    
    /**
     * Constructs a InterfaceFaultPart class from the values given.
     * 
     * @param interfaceName the local name of the parent Interface component.
     * @param fault the name of the Interface Fault component.
     * @throws IllegalArgumentException if interfaceName or fault are null.
     */
    public InterfaceFaultPart(NCName interfaceName, NCName fault) {
        if (interfaceName == null | fault == null) {
            throw new IllegalArgumentException();
        }
        this.interfaceName = interfaceName;
        this.fault = fault;
    }
    
    /**
     * Returns a String of the serialised Binding Fault Pointer Part.
     * 
     * @return a String the serialised Binding Fault Pointer Part.
     */
    public String toString() {
        return "wsdl.interfaceFault(" + interfaceName + "/" + fault + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }
    
}
