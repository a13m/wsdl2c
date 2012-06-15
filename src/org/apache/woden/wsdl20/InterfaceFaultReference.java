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

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;

/**
 * Represents the InterfaceFaultReference component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceFaultReference extends NestedComponent {
    
    
    /**
     * Returns an InterfaceFault representing the {interface fault} property of this
     * InterfaceFaultReference. It identifies the interface fault that is associated 
     * with the parent interface operation by this interface fault reference.
     * 
     * @return an InterfaceFault associated by this InterfaceFaultReference
     */
    public InterfaceFault getInterfaceFault();
    
    /**
     * Returns an NCName representing the {message label} property of this InterfaceFaultReference.
     * This associates the fault with a placeholder message in the message exchange pattern
     * identified by the parent interface operation.
     * 
     * @return an NCName representing the message label
     */
    public NCName getMessageLabel();
    
    /**
     * Returns an enumerated type, Direction, that represents the {direction} property
     * of this InterfaceFaultReference. This indicates the direction in which this fault
     * is used; 'in' or 'out'.
     * 
     * @return the Direction of this fault
     */
    public Direction getDirection();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the InterfaceFaultReferenceElement that maps to this InterfaceFaultReference
     */
    public InterfaceFaultReferenceElement toElement();
    
}
