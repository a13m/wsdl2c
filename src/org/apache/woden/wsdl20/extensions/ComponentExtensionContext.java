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

import org.apache.woden.wsdl20.WSDLComponent;

/**
 * This interface defines a generic API for accessing the extension properties
 * from a particular extension namespace that are attached to a particular 
 * WSDL 2.0 component. That is, for accessing extension properties by namespace
 * per component.
 * <p>
 * It provides accessor methods that return <code>ExtensionProperty</code> objects, 
 * which callers can use to access the content of an extension property.
 * <p>
 * Implementors of WSDL 2.0 extensions must, as a minimum, implement this interface
 * for each WSDL 2.0 component they extend. They must also <i>register</i> their
 * extension implementations using the <code>ExtensionRegistry</code>. 
 * Implementors may <i>optionally</i> extend this interface to add their own extension-specific 
 * property accessor methods to the generic accessor methods declared by this interface.
 * For examples of this, see the SOAP and HTTP binding extensions provided by Woden.
 * <p>
 * To document extensions consistently, implementors should copy the following Javadoc 
 * fragment to use for their implementation classes or for their sub-interfaces of this
 * interface, replacing the square bracket parts [...] accordingly.
 * <p>
 * <i>start of fragment:</i>
 * <p>
 * Provides access to the extension properties of the [parent component name] component 
 * that are in the <code>[extension namespace]</code> namespace.
 * These extension properties can be accessed as <code>ExtensionProperty</code> objects 
 * via the <code>getProperties</code> and <code>getProperty</code> methods  
 * using the property names and Java types shown in the following table.
 * <p>
 * <table border="1">
 * <tr>
 * <th>Property name</th>
 * <th>Java type</th>
 * </tr>
 * <tr>
 * <td>[property name]</td>
 * <td>[java type]</td>
 * </tr>
 * <tr>
 * <td>...</td>
 * <td>...</td>
 * </tr>
 * </table>
 * <p>
 * <i>end of fragment:</i>
 * 
 * @author John Kaputin (jkaputin@apache.org)
 * 
 * @see org.apache.woden.wsdl20.extensions.ExtensionProperty
 */
public interface ComponentExtensionContext {

    public WSDLComponent getParent();
    
    public URI getNamespace();

    public ExtensionProperty[] getProperties();
    
    public ExtensionProperty getProperty(String propertyName);
}
