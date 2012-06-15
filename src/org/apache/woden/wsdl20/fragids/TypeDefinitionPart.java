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


import java.net.URI;

/**
 * <code>TypeDefinitionPart</code> is a Type Definition Pointer Part for the Type Definition WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.typeDefinition">http://www.w3.org/TR/wsdl20/#wsdl.typeDefinition</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class TypeDefinitionPart implements ComponentPart{
    private static final String emptyString = "".intern();
    private QName type;   //Name of the Type Definition component.
    private final URI system;   //Namespace absolute IRI of the extension type system used for the Type Definition component.

    /**
     * Constructs a TypeDefinitionPart class for an Type Definition component with an XMLScheme type system.
     * 
     * @param type the name of the Type Definition component.
     * @param system namespace absolute IRI of the extension type system used for the Type Definition component.
     * @throws IllegalArgumentException if type or system are null.
     */
    public TypeDefinitionPart(QName type, URI system) {
        if (type == null | system == null) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.system = system;
    }
    
    /**
     * Constructs an TypeDefinitionPart class for an Type Definition component with another type system.
     * 
     * @param type the name of the Definition component.
     * @throws IllegalArgumentException if type is null.
     */
    public TypeDefinitionPart(QName type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.system = null;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.fragids.ComponentPart#prefixNamespaces(org.apache.woden.wsdl20.fragids.FragmentIdentifier)
     */
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        if (system == null) {
            return new TypeDefinitionPart(fragmentIdentifier.prefixQNameNamespace(type));
        } else {
            return new TypeDefinitionPart(fragmentIdentifier.prefixQNameNamespace(type), system);
        }
    }
    
    /**
     * Returns a String of the serialised Type Definition Pointer Part.
     * 
     * @return a String the serialised Type Definition Pointer Part.
     */
    public String toString() {
        String typeString = (type.getPrefix() != null && !type.getPrefix().equals(emptyString) ? type.getPrefix() + ":" + type.getLocalPart() : type.getLocalPart());
        if (system == null) {
            return "wsdl.typeDefinition(" + typeString + ")";
        } else {
            return "wsdl.typeDefinition(" + typeString + "," + system + ")";
        }
    }
    
}
