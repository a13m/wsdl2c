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
 * <code>BindingFaultReferencePart</code> is a Binding Fault Reference Pointer Part for the Binding Fault Reference WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.bindingFaultReference">http://www.w3.org/TR/wsdl20/#wsdl.bindingFaultReference</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class BindingFaultReferencePart implements ComponentPart {
    private static final String emptyString = "".intern();
    private final NCName binding;     //Local name of the parent Binding component.
    private QName operation;    //Name of the Interface Operation referred to by the parent Binding Operation component.
    private final NCName message;     //Message Label of the Interface Fault Reference component referred to by this Binding Fault Reference component.
    private QName fault;        //Name of the Interface Fault component referred to by the Interface Fault Reference component referred to by this Binding Fault Reference component.
    
    /**
     * Constructs an BindingFaultReferencePart class from the values given.
     * 
     * @param binding the local name of the parent Binding component.
     * @param operation the name of the Interface Operation referred to by the parent Binding Operation component.
     * @param message the message label of the Interface Fault Reference component referred to by this Binding Message Reference component.
     * @param fault the name of the Interface Fault component referred to by the Interface Fault Reference component referred to by this Binding Fault Reference component.
     * @throws IllegalArgumentException if binding, operation, message or fault are null.
     */
    public BindingFaultReferencePart(NCName binding, QName operation, NCName message, QName fault) {
        if (binding == null | operation == null | message == null | fault == null) {
            throw new IllegalArgumentException();
        }
        this.binding = binding;
        this.operation = operation;
        this.message = message;
        this.fault = fault;
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        QName nOperation = fragmentIdentifier.prefixQNameNamespace(operation);
        QName nFault = fragmentIdentifier.prefixQNameNamespace(fault);
        return new BindingFaultReferencePart(binding, nOperation, message, nFault);
    }
    
    /**
     * Returns a String of the serialised Binding Fault Reference Pointer Part.
     * 
     * @return a String the serialised Binding Fault Reference Pointer Part.
     */
    public String toString() {
        String operationString = (operation.getPrefix() != null  && !operation.getPrefix().equals(emptyString) ? operation.getPrefix() + ":" + operation.getLocalPart() : operation.getLocalPart());
        String faultString = (fault.getPrefix() != null  && !fault.getPrefix().equals(emptyString) ? fault.getPrefix() + ":" + fault.getLocalPart() : fault.getLocalPart());
        return "wsdl.bindingFaultReference(" + binding + "/" + operationString + "/" + message + "/" + faultString + ")";
    }
}
