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

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.xml.ServiceElement;

/**
 * Represents the Service component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Service extends WSDLComponent
{
    /**
     * Returns the qualified name representing the {name} property of this Service.
     * 
     * @return QName representing the name of this Service
     */
    public QName getName();
    
    /**
     * Represents the {interface} property of the Service component. This is the Interface component
     * that this Service defines Endpoints for.
     * 
     * @return Interface associated with this Service
     */
    public Interface getInterface();
    
    /**
     * Represents the {endpoints} property of the Service component. This is the set of
     * Endpoints declared by this Service. The method will return an empty array if there
     * are no endpoints.
     * 
     * @return an array of Endpoint objects
     */
    public Endpoint[] getEndpoints();
    
    
    /**
     * Returns the Endpoint with the specified local name from the {endpoints}
     * property of this Service. That is, from the set of Endpoints defined 
     * by this Service.
     * If the name parameter is null, this method will return null.
     * 
     * @param name the local name of the Endpoint
     * @return the named Endpoint
     */
    public Endpoint getEndpoint(NCName name);
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the ServiceElement that maps to this Service
     */
    public ServiceElement toElement();
}
