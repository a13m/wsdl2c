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

import org.apache.woden.wsdl20.Binding;

/**
 * <code>BindingPart</code> is a Binding Pointer Part for the Binding WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.binding">http://www.w3.org/TR/wsdl20/#wsdl.binding</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class BindingPart implements ComponentPart {
    private final NCName binding; //Local name of the Binding component.
    
    /**
     * Constructs a BindingPart class using values from the Binding WSDL 2.0 component.
     * 
     * @param binding the local name of the Binding component.
     * @throws IllegalArgumentException if binding is null.
     */
    public BindingPart(Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException();
        }
        this.binding = new NCName(binding.getName().getLocalPart());
    }
    
    /**
     * Constructs a BindingPart from the given value.
     * 
     * @param binding
     */
    public BindingPart(NCName binding) {
        if (binding == null) {
            throw new IllegalArgumentException();
        }
        this.binding = binding;
    }
    
    /**
     * Returns a String of the serialised Binding Pointer Part.
     * 
     * @return a String the serialised Binding Pointer Part.
     */
    public String toString() {
        return "wsdl.binding(" + binding + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }
}
