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
 * Defines behaviour for accessing the extension properties 
 * attached to WSDL 2.0 components.
 * To be extended by each WSDL 2.0 component interface.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface PropertyExtensible {
    
    /**
     * Returns all of the component's extension properties. These may span multiple namespaces.
     */
    public ExtensionProperty[] getExtensionProperties();

    /**
     * Returns the component's extension properties from a particular namespace.
     * 
     * @param namespace URI representing the namespace of the required extension properties
     * @return extension properties from the specified namespace 
     */
    public ExtensionProperty[] getExtensionProperties(URI namespace);
    
    /**
     * Returns the component's named extension property from the specified namespace.
     * Within the WSDL 2.0-defined extensions, the extension property name itself
     * is unique, but it is possible that property name collisions could occur across
     * different user-defined extensions, so the extension namespace is used with
     * property name to ensure uniqueness.
     * 
     * @param namespace the namespace of the named extension property
     * @param name the name of the required extension property
     * @return the named extension property
     */
    public ExtensionProperty getExtensionProperty(URI namespace, String name);
    
}
