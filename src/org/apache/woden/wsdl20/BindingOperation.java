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

import org.apache.woden.wsdl20.xml.BindingOperationElement;

/**
 * Represents the BindingOperation component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingOperation extends NestedComponent 
{
    /**
     * Returns an InterfaceOperation representing the {interface operation} property
     * of the BindingOperation component. That is, the interface operation that this
     * binding operation provides concrete bindings for.
     * 
     * @return InterfaceOperation bound by this BindingOperation
     */
    public InterfaceOperation getInterfaceOperation();
    
    /**
     * Represents the {binding message references} property of the BindingOperation component.
     * This is the set of binding message references declared by this binding operation.
     * This method will return an empty array if there are no binding message references for
     * this binding operation.
     * 
     * @return an array of BindingMessageReference
     */
    public BindingMessageReference[] getBindingMessageReferences();
    
    /**
     * Represents the {binding fault references} property of the BindingOperation component.
     * This is the set of binding fault references declared by this binding operation.
     * This method will return an empty array if there are no binding fault references for
     * this binding operation.
     * 
     * @return an array of BindingFaultReference
     */
    public BindingFaultReference[] getBindingFaultReferences();

    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the BindingOperationElement that maps to this BindingOperation
     */
    public BindingOperationElement toElement();
}
