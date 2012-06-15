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

package org.apache.woden.xpointer;

import org.apache.woden.types.NCName;

/**
 * Represents a fragment identifier conforming to the XML Pointer Language Framework.
 * 
 * This class is based upon a class of the same name in the Apache Coccon.
 *
 * @author Dan Harvey (danharvey42@gmail.com)
 */
public class XmlnsPointerPart implements PointerPart {
    private final NCName prefix;
    private final String namespace;

    public XmlnsPointerPart(NCName prefix, String namespace) {
        if (prefix == null | namespace == null) {
            throw new IllegalArgumentException();
        }
        this.prefix = prefix;
        this.namespace = namespace;
    }

    public String toString() {
        return "xmlns(" + prefix + "=" + namespace + ")";
    }
    
    public void prefixNamespaces(XPointer xpointer) {
        //This PointerPart does not have any namespaces.
    }
}
