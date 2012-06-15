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
 * <code>InterfaceMessageReferencePart</code> is a Interface Message Reference Pointer Part for the Interface Message Reference WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.interfaceMessageReference">http://www.w3.org/TR/wsdl20/#wsdl.interfaceMessageReference</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class InterfaceMessageReferencePart implements ComponentPart{
    private final NCName interfaceName; //Local name of the parent Interface component.
    private final NCName operation;     //Name of the parent Interface Operation component.
    private final NCName message;       //Message Label of the Interface Message Reference component.
    
    /**
     * Constructs a InterfaceMessageReferencePart class from the values given.
     * 
     * @param interfaceName the local name of the parent Interface component.
     * @param operation the name of the parent Interface Operation component.
     * @param message the message label of the Interface Message Reference component.
     * @throws IllegalArgumentException if interfaceName, operation or message are null.
     */
    public InterfaceMessageReferencePart(NCName interfaceName, NCName operation, NCName message) {
        if (interfaceName == null | operation == null | message == null) {
            throw new IllegalArgumentException();
        }
        this.interfaceName = interfaceName;
        this.operation = operation;
        this.message = message;
    }
    
    /**
     * Returns a String of the serialised Binding Message Reference Pointer Part.
     * 
     * @return a String the serialised Binding Message Reference Pointer Part.
     */
    public String toString() {
        return "wsdl.interfaceMessageReference(" + interfaceName + "/" + operation + "/" + message + ")";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }

}
