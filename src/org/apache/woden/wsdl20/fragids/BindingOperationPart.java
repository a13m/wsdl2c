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
 * <code>BindingOperationPart</code> is a Binding Operation Pointer Part for the Binding Operation WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.bindingOperation">http://www.w3.org/TR/wsdl20/#wsdl.bindingOperation</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class BindingOperationPart implements ComponentPart {
    private static final String emptyString = "".intern();
    private final NCName binding;     //Local name of the parent Binding component.
    private QName operation;    //Name of the Interface Operation component referred to by this Binding Operation component.
    
    /**
     * Constructs a BindingOperationPart class from the values given.
     * 
     * @param binding the local name of the parent Binding component.
     * @param operation the name of the Interface Operation component referred to by this Binding Operation component.
     * @throws IllegalArgumentException if binding or operation are null.
     */
    public BindingOperationPart(NCName binding, QName operation) {
        if (binding == null | operation == null) {
            throw new IllegalArgumentException();
        }
        this.binding = binding;
        this.operation = operation;
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return new BindingOperationPart(binding, fragmentIdentifier.prefixQNameNamespace(operation));
    }
    
    /**
     * Returns a String of the serialised Binding Operation Pointer Part.
     * 
     * @return a String the serialised Binding Operation Pointer Part.
     */
    public String toString() {
        String operationString = (operation.getPrefix() != null && !operation.getPrefix().equals(emptyString) ? operation.getPrefix() + ":" + operation.getLocalPart() : operation.getLocalPart());
        return "wsdl.bindingOperation(" + binding + "/" + operationString + ")";
    }
}
