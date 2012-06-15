/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.apache.woden.types;

import java.net.URI;

/**
 * Represents an XML namespace declaration, consisting of a namespace prefix
 * and a namespace URI. This is an immutable class.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class NamespaceDeclaration {
    
    public static final String XMLNS_NS_STRING = "http://www.w3.org/2000/xmlns/";
    
    public static final URI XMLNS_NS_URI  = URI.create("http://www.w3.org/2000/xmlns/");
    
    private final String prefix;
    private final URI namespaceURI;
    
    public NamespaceDeclaration(String prefix, URI namespaceURI) {
        this.prefix = prefix;
        this.namespaceURI = namespaceURI;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public URI getNamespaceURI() {
        return this.namespaceURI;
    }

}
