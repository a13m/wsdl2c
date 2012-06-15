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

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.xml.EndpointElement;

/**
 * Represents the Endpoint component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Endpoint extends NestedComponent 
{
    /**
     * Returns the local name representing the {name} property of this Endpoint.
     * 
     * @return NCName representing the name of this Endpoint
     */
    public NCName getName();
    
    /**
     * Represents the {binding} property of the Endpoint component. This is the Binding component
     * that this Endpoint is associated with.
     * 
     * @return Binding associated with this Endpoint
     */
    public Binding getBinding();
    
    /**
     * Returns a URI that represents the {address} property of this Endpoint.
     * This is the network address at which the parent service can be found using the
     * binding associated with this endpoint.
     * 
     * @return the URI address of the Service via this Endpoint
     */
    public URI getAddress();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the EndpointElement that maps to this Endpoint
     */
    public EndpointElement toElement();
}
