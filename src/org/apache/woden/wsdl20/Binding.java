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

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.xml.BindingElement;

/**
 * Represents the Binding component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Binding extends WSDLComponent
{
    /**
     * Returns a QName representing the {name} property of the Binding component.
     *  
     * @return QName the qualified name of the Binding
     */
    public QName getName();
    
    /**
     * Represents the {interface} property of the Binding component. This is the Interface component
     * that this Binding defines concrete bindings for.  For an 'interface-less' or 'generic' binding, 
     * this method will return null.
     * 
     * @return Interface for which bindings are provided by this Binding
     */
    public Interface getInterface();
    
    /**
     * Returns a URI representing the {type} property of the Binding component. 
     * <p>
     * For example:
     * <br/>
     * For a SOAP binding this will be the uri "http://www.w3.org/ns/wsdl/soap".
     * <br/>
     * For an HTTP binding this will be the uri "http://www.w3.org/ns/wsdl/http".
     * 
     * @return URI representing the binding type
     */
    public URI getType();
    
    /**
     * Represents the {binding faults} property of the Binding component. This is the set of
     * binding faults declared by this binding. The method will return an empty array if there
     * are no binding faults.
     * 
     * @return an array of BindingFault objects
     */
    public BindingFault[] getBindingFaults();
    
    /**
     * Represents the {binding operations} property of the Binding component. This is the set of
     * binding operations declared by this binding. The method will return an empty array if there
     * are no binding operations.
     * 
     * @return an array of BindingOperation objects
     */
    public BindingOperation[] getBindingOperations();

    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the BindingElement that maps to this Binding
     */
    public BindingElement toElement();
}
