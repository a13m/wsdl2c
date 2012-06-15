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
 * Represents a WSDL 2.0 component extension property. That is, a property derived from WSDL 2.0 
 * extension elements or attributes. This is a generic representation of an extension property 
 * that simply provides the property's name and namespace and its content as a 
 * <code>java.lang.Object</code>.
 * The caller must know what to do with this <i>content</i> Object. 
 * For example, what Java type to cast it to or whether it provides a useful 
 * <code>toString()</code> implementation.
 * <p>
 * WSDL 2.0 extensions should be defined by their own specification, which may include this type 
 * of information.
 * Implementors of WSDL 2.0 extensions in Woden should also specify the names and Java types of 
 * their extension properties using Javadoc comments in their implementations of the 
 * <code>ComponentExtensionContext</code> interface. 
 * For examples, see the SOAP and HTTP binding extensions provided by Woden.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 *
 * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext
 * 
 */
public interface ExtensionProperty {
    
    /**
     * Returns a String representing the name of the extension property.
     */
    public String getName();
    
    /**
     * Returns a URI representing the namespace the extension property belongs to.
     */
    public URI getNamespace();
    
    /**
     * Returns the content of the extension property as a <code>java.lang.Object</code>.
     */
    public Object getContent();

}
