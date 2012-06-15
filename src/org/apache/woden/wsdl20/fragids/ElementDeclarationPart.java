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

import org.apache.woden.WSDLReader;

import java.net.URI;

/**
 * <code>ElementDeclarationPart</code> is a Element Declaration Pointer Part for the Element Declaration WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.elementDeclaration">http://www.w3.org/TR/wsdl20/#wsdl.elementDeclaration</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class ElementDeclarationPart implements ComponentPart {
    private static final String emptyString = "".intern();
    private QName element;    //Name of the Element Declaration component.
    private final URI system;       //Namespace absolute IRI of the extension type system used for the Element Declaration component.

    /**
     * Constructs a ElementDeclarationPart class for an Element Declaration component with an XMLScheme type system.
     *
     * @param element the name of the Element Declaration component.
     * @param system namespace absolute IRI of the extension type system used for the Element Declaration component.
     * @throws IllegalArgumentException if element or system are null.
     */
    public ElementDeclarationPart(QName element, URI system) {
        if (element == null | system == null) {
            throw new IllegalArgumentException();
        }
        this.element = element;
        if (system.toString().equals(WSDLReader.TYPE_XSD_2001)) {
            this.system = null;
        } else {
            this.system = system;
        }
    }
    
    /**
     * Constructs a ElementDeclarationPart class for an Element Declaration component with another type system.
     * 
     * @param element the name of the Element Declaration component.
     * @throws IllegalArgumentException if element is null.
     */
    public ElementDeclarationPart(QName element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        this.element = element;
        this.system = null;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.fragids.ComponentPart#prefixNamespaces(org.apache.woden.wsdl20.fragids.FragmentIdentifier)
     */
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        if (system == null) {
            return new ElementDeclarationPart(fragmentIdentifier.prefixQNameNamespace(element));
        } else {
            return new ElementDeclarationPart(fragmentIdentifier.prefixQNameNamespace(element), system);
        }
    }
    
    /**
     * Returns a String of the serialised Element Declaration Pointer Part.
     * 
     * @return a String the serialised Element Declaration Pointer Part.
     */
    public String toString() {
        String elementString = (element.getPrefix() != null && !element.getPrefix().equals(emptyString) ? element.getPrefix() + ":" + element.getLocalPart() : element.getLocalPart());
        if (system == null) {
            return "wsdl.elementDeclaration(" + elementString + ")";
        } else {
            return "wsdl.elementDeclaration(" + elementString + "," + system + ")";
        }
    }
}
