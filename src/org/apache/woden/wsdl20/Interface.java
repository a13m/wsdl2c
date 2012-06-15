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

import org.apache.woden.wsdl20.xml.InterfaceElement;

/**
 * Represents the Interface component from the WSDL 2.0 Component model.
 * Defines behaviour for accessing the WSDL components nested within
 * the Interface component, including those inherited via Interface extension.
 *
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Interface  extends WSDLComponent 
{
    /**
     * Returns the qualified name representing the {name} property of this Interface.
     * 
     * @return QName representing the name of this Interface
     */
    public QName getName();
    
    /**
     * Represents the {extended interfaces} property of the Interface component. 
     * This is the set of declared Interface components that this Interface directly 
     * extends, but does not include any Interfaces that those Interfaces extend.
     * The method will return an empty array if there are no extended interfaces.
     * 
     * @return an array of Interface components
     */
    public Interface[] getExtendedInterfaces();
    
    /**
     * Return the named Interface from the {extended interfaces} property of this Interface.
     * That is, from the set of declared Interfaces that this Interface directly extends.
     * If null is specified for the name, this method will return null.
     * 
     * @param interfaceName the qualified name of the required Interface
     * @return the named Interface
     */
    public Interface getExtendedInterface(QName interfaceName);
    
    /**
     * Represents the {interface faults} property of the Interface component. This is the set of
     * interface faults declared directly by this interface, but not those defined by any 
     * interfaces that this interface extends.
     * The method will return an empty array if there are no interface faults.
     * 
     * @return array of InterfaceFault components
     */
    public InterfaceFault[] getInterfaceFaults();
    
    /**
     * Returns the InterfaceFault with the specified name from the {interface faults}
     * property of this Interface. That is, from the set of InterfaceFaults declared 
     * directly by this Interface and excluding any inherited directly or indirectly 
     * from extended Interfaces.
     * If the name parameter is null, this method will return null.
     * 
     * @param faultName the qualified name of the InterfaceFault
     * @return the InterfaceFault object
     */
    public InterfaceFault getInterfaceFault(QName faultName);
    
    /**
     * Returns the set of all InterfaceFault components available to this Interface,
     * which includes those declared by this Interface and those defined by any Interfaces 
     * it extends, directly or indirectly. 
     * The method will return an empty array if there are no interface faults.
     * 
     * @return array of InterfaceFault components
     */
    public InterfaceFault[] getAllInterfaceFaults();
    
    /**
     * Returns the InterfaceFault with the specified name from the set of all InterfaceFaults
     * available to this Interface, which includes those declared by this Interface and those
     * defined by any Interfaces it extends, directly or indirectly.
     * If the name parameter is null, this method will return null.
     * 
     * @param faultName the qualified name of the InterfaceFault
     * @return the InterfaceFault object
     */
    public InterfaceFault getFromAllInterfaceFaults(QName faultName);
    
    /**
     * Represents the {interface operations} property of the Interface component. This is the set 
     * of interface operations declared directly by this interface, but not those defined by any 
     * interfaces that this interface extends.
     * The method will return an empty array if there are no interface operations.
     * 
     * @return array of InterfaceOperation components
     */
    public InterfaceOperation[] getInterfaceOperations();
    
    /**
     * Returns the InterfaceOperation with the specified name from the {interface operations}
     * property of this Interface. That is, from the set of InterfaceOperations declared directly
     * by this Interface and excluding any inherited directly or indirectly from extended 
     * Interfaces.
     * If the name parameter is null, this method will return null.
     * 
     * @param operName the qualified name of the required InterfaceOperation
     * @return the InterfaceOperation object
     */
    public InterfaceOperation getInterfaceOperation(QName operName);
    
    /**
     * Returns the set of all InterfaceOperation components available to this Interface,
     * which includes those declared by this Interface and those defined by the Interfaces 
     * it extends, directly or indirectly.
     * The method will return an empty array if there are no interface operations.
     * 
     * @return array of InterfaceOperation components
     */
    public InterfaceOperation[] getAllInterfaceOperations();
    
    /**
     * Returns the InterfaceOperation with the specified name from the set of all InterfaceOperations
     * available to this Interface, which includes those declared by this Interface and those
     * defined by any Interfaces it extends, directly or indirectly. 
     * If the name parameter is null, this method will return null.
     * 
     * @param operName the qualified name of the InterfaceOperation
     * @return the InterfaceOperation object
     */
    public InterfaceOperation getFromAllInterfaceOperations(QName operName);
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the InterfaceElement that maps to this Interface
     */
    public InterfaceElement toElement();
    
}
