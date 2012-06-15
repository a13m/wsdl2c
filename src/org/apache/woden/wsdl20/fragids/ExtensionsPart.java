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

import java.net.URI;


/**
 * <code>ExtensionPart</code> is a Extension Pointer Part for the Extension WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.extension">http://www.w3.org/TR/wsdl20/#wsdl.extension</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class ExtensionsPart implements ComponentPart {
    private final URI namespace;        //Namespace IRI that identifies the Extension component.
    private final String identifier;    //Defined by the extension.

    /**
     * Constructs a Extensions Part class for an Extension component with an XMLScheme type system.
     * 
     * @param namespace the namespace IRI that identifies the Extension component.
     * @param identifier Defined by the extension.
     * @throws IllegalArgumentException if namespace or identifier are null.
     */
    public ExtensionsPart(URI namespace, String identifier) {
        if (namespace == null | identifier == null) {
            throw new IllegalArgumentException();
        }
        this.namespace = namespace;
        this.identifier = identifier;
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }

    /**
     * Returns a String of the serialised Extension Pointer Part.
     * 
     * @return a String the serialised Extension Pointer Part.
     */
    public String toString() {
        return "wsdl.extension(" + namespace + "/" + identifier + ")";
    } 
}
