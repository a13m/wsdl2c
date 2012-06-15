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

import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

/**
 * Represents the InterfaceMessageReference component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceOperation extends NestedComponent {

    /**
     * Returns the qualified name representing the {name} property of this InterfaceOperation.
     * 
     * @return QName representing the name of this InterfaceOperation
     */
    public QName getName();
    
    /**
     * Returns a URI representing the {message exchange pattern} property of this 
     * InterfaceOperation.
     * 
     * @return URI of the message exchange pattern.
     */
    public URI getMessageExchangePattern();
    
    /**
     * Represents the {interface message references} property of the InterfaceOperation component.
     * This is the set of interface message references declared by this interface operation.
     * This method will return an empty array if there are no interface message references for
     * this interface operation.
     * 
     * @return an array of InterfaceMessageReference
     */
    public InterfaceMessageReference[] getInterfaceMessageReferences();
    
    /**
     * Represents the {interface fault references} property of the InterfaceOperation component.
     * This is the set of interface fault references defined by this interface operation.
     * This method will return an empty array if there are no interface fault references for
     * this interface operation.
     * 
     * @return an array of InterfaceFaultReference
     */
    public InterfaceFaultReference[] getInterfaceFaultReferences();
    
    /**
     * Represents the {style} property of the InterfaceOperation component.
     * This is a set of URIs which specify the rules that constrain the content of
     * input and output messages and faults of the interface operation.
     * This method will return an empty array if there are no style URIs.
     * 
     * @return an array of URI
     */
    public URI[] getStyle();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the InterfaceOperationElement that maps to this InterfaceOperation
     */
    public InterfaceOperationElement toElement();
    
}
