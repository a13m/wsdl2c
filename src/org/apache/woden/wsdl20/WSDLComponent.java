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
package org.apache.woden.wsdl20;

import java.net.URI;

import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.PropertyExtensible;
import org.apache.woden.wsdl20.fragids.FragmentIdentifier;

/**
 * Represents the top-level super-type of all WSDL 2.0 Components. 
 * Every WSDL 2.0 Component interface must extend this interface, directly or indirectly.
 * It provides a common way to refer to any type of WSDL Component.
 * It defines behaviour common to all WSDL components, such as testing for 
 * equivalence and accessing extension properties. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface WSDLComponent extends PropertyExtensible
{
    /**
     * Tests whether this component is logically equivalent the specified component.
     * Equivalence is determined per spec WSDL 2.0 Part 1 Section 2.17 Equivalence
     * of Components.
     * 
     * @param comp the WSDL component that this component will be compared to
     * @return true if the components are logically equivalent
     */
    public boolean isEquivalentTo(WSDLComponent comp);
    
    /**
     * Tests whether this component is logically equivalent the specified component.
     * Equivalence is determined per spec WSDL 2.0 Part 1 Section 2.17 Equivalence
     * of Components.
     * 
     * @param comp the WSDL component that this component will be compared to
     * @return true if the components are logically equivalent
     * @deprecated Use isEquivalentTo(WSDLComponent)
     * @see isEquivalentTo(WSDLComponent)
     */
    public boolean equals(WSDLComponent comp);
    
    /**
     * Stores the ComponentExtensionContext object that provides access to this WSDL component's
     * extension properties from the specified namespace.
     * 
     * @param extNamespace the namespace URI of the extension properties 
     * @param compExtCtx the ComponentExtensionContext object for accessing the extension properties
     */
    public void setComponentExtensionContext(URI extNamespace, ComponentExtensionContext compExtCtx);
    
    /**
     * Returns the ComponentExtensionContext object that provides access to this WSDL component's
     * extension properties from the specified namespace.
     * 
     * @param extNamespace the namespace URI of the extension properties 
     * @return the ComponentExtensionContext object for accessing the extension properties
     */
    public ComponentExtensionContext getComponentExtensionContext(URI extNamespace);
    
   
    /**
     * Returns the fragment identifier for this WSDL 2.0 component.
     * 
     * @return a FragmentIdentifier for this WSDL 2.0 component.
     */
    public FragmentIdentifier getFragmentIdentifier();
    
    /**
     * Returns a String serialisation of the fragment identifier for this WSDL 2.0 component.
     * 
     * @return a String the serialisation of the fragment identifier for this WSDL 2.0 component.
     */
    public String toString();
    
}
