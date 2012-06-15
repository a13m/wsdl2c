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
package org.apache.woden.wsdl20.extensions;

import java.net.URI;

/**
 * This class implements the ExtensionProperty interface to provide a 
 * generic representation of a component extension property. 
 * <p>
 * This class may be used by implementors of WSDL 2.0 extensions when implementing the
 * ExtensionProperty accessor methods of the ComponentExtensionContext interface.
 * For example, when they extend the abstract class BaseComponentExtensionContext,
 * which partially implements the ComponentExtensionContext interface.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 * 
 * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext
 * @see org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext
 *
 */
public class GenericExtensionProperty implements ExtensionProperty {
    private String fName;
    private URI fNamespace;
    private Object fContent;
    
    /**
     * This public constructor stores the extension property's name, namespace and
     * content. The name and namespace parameters must not be null.
     * 
     * @param name the String name of the extension property
     * @param namespace the namespace URI of the extension property
     * @param content an Object representing the content of the extension property
     * 
     * @throws NullPointerException if the name or namespace parameter is null
     */
    public GenericExtensionProperty(String name, 
            URI namespace, 
            Object content) {
        
        if(name == null) {
            throw new NullPointerException("name=null");
        } 
        if(namespace == null) {
            throw new NullPointerException("namespace=null");
        }
        fName = name;
        fNamespace = namespace;
        fContent = content;
    }
    
    /**
     * @see org.apache.woden.wsdl20.extensions.ExtensionProperty#getName()
     */
    public String getName() {
        return fName;
    }

    /**
     * @see org.apache.woden.wsdl20.extensions.ExtensionProperty#getNamespace()
     */
    public URI getNamespace() {
        return fNamespace;
    }

    /**
     * @see org.apache.woden.wsdl20.extensions.ExtensionProperty#getContent()
     */
    public Object getContent() {
        return fContent;
    }

}
