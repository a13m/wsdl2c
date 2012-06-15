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

import org.apache.woden.wsdl20.xml.BindingFaultElement;

/**
 * Represents the BindingFault component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingFault extends NestedComponent 
{
    /**
     * Returns an InterfaceFault representing the {interface fault} property of the
     * BindingFault component. That is, the interface fault that this binding fault
     * provides concrete bindings for.
     * 
     * @return an InterfaceFault bound by this BindingFault
     */
    public InterfaceFault getInterfaceFault();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the BindingFaultElement that maps to this BindingFault
     */
    public BindingFaultElement toElement();
}
