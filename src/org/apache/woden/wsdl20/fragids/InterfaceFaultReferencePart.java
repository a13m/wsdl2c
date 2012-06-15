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
 * <code>InterfaceFaultReferencePart</code> is a Interface Fault Reference Pointer Part for the Interface Fault Reference WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.interfaceFaultReference">http://www.w3.org/TR/wsdl20/#wsdl.interfaceFaultReference</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class InterfaceFaultReferencePart implements ComponentPart {
    private static final String emptyString = "".intern();
    private final NCName interfaceName; //Local name of the parent Interface component.
    private final NCName operation;      //Name of the parent Interface Operation component.
    private final NCName message;       //Message Label of the Interface Fault Reference component.
    private QName fault;          //Name of the Interface Fault component referred to by the Interface Fault Reference component.
    
    /**
     * Constructs a InterfaceFaultReferencePart class from the values given.
     * 
     * @param interfaceName the local name of the parent Interface component.
     * @param operation the name of the parent Interface Operation component.
     * @param message the message label of the Interface Fault Reference component.
     * @param fault the name of the Interface Fault component referred to by the Interface Fault Reference component.
     * @throws IllegalArgumentException if interfaceName, operation, message or fault are null.
     */
    public InterfaceFaultReferencePart(NCName interfaceName, NCName operation, NCName message, QName fault) {
        if (interfaceName == null | operation == null | message == null | fault == null) {
            throw new IllegalArgumentException();
        }
        this.interfaceName = interfaceName;
        this.operation = operation;
        this.message = message;
        this.fault = fault;
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return new InterfaceFaultReferencePart(interfaceName, operation, message, fragmentIdentifier.prefixQNameNamespace(fault));
    }
    
    /**
     * Returns a String of the serialised Binding Fault Reference Pointer Part.
     * 
     * @return a String the serialised Binding Fault Reference Pointer Part.
     */
    public String toString() {
        String faultString = (fault.getPrefix() != null && !fault.getPrefix().equals(emptyString) ? fault.getPrefix() + ":" + fault.getLocalPart() : fault.getLocalPart());
        return "wsdl.interfaceFaultReference(" + interfaceName + "/" + operation + "/" + message + "/" + faultString + ")";
    }
}
