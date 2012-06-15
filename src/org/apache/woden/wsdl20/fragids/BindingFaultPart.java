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

import javax.xml.namespace.QName;
import org.apache.woden.types.NCName;
/**
 * <code>BindingFaultPart</code> is a Binding Fault Pointer Part for the Binding Fault WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.bindingFault">http://www.w3.org/TR/wsdl20/#wsdl.bindingFault</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class BindingFaultPart implements ComponentPart {
    private static final String emptyString = "".intern();
    private final NCName binding; //Local name of the parent Binding component.
    private QName fault;    //Name of the Interface Fault referred to by this Binding Fault component.

    /**
     * Constructs a BindingFaultPart class from the values given.
     * 
     * @param binding the local name of the parent Binding component.
     * @param fault the name of the Interface Fault component referred to by this Binding Fault component.
     * @throws IllegalArgumentException if binding or fault are null.
     */
    public BindingFaultPart(NCName binding, QName fault) {
        if (binding == null | fault == null) {
            throw new IllegalArgumentException();
        }
        this.binding = binding;
        this.fault = fault; 
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return new BindingFaultPart(binding, fragmentIdentifier.prefixQNameNamespace(fault));
    }

    /**
     * Returns a String of the serialised Binding Fault Pointer Part.
     * 
     * @return a String the serialised Binding Fault Pointer Part.
     */
    public String toString() {
        String faultString = (fault.getPrefix() != null && !fault.getPrefix().equals(emptyString) ? fault.getPrefix() + ":" + fault.getLocalPart() : fault.getLocalPart());
        return "wsdl.bindingFault(" + binding + "/" + faultString + ")";
    }
}
