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
package org.apache.woden.internal.wsdl20;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.WSDLElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.InterfacePart;

/**
 * This class represents the Interface component from the 
 * WSDL 2.0 Component Model and &lt;interface&gt; element.
 * 
 * @author jkaputin@apache.org
 */
public class InterfaceImpl extends WSDLComponentImpl
                           implements Interface, InterfaceElement 
{
    private WSDLElement fParentElem = null;
    
    /* This field refers to the Description component which contains this Interface
     * component in its {interfaces} property. It is set whenever this Interface is 
     * returned by that Description's getInterfaces() or getInterface(QName) methods. 
     * Note that with modularization via a wsdl import or include, this 
     * reference may be different to fDescriptionElement because it refers to the 
     * importing or including description at the top of the wsdl tree (whereas the 
     * latter refers to the description in which this interface is directly declared).
     * This field is used to retrieve components that are available (i.e. in-scope) 
     * to the top-level Description component. e.g. it is used with interface extension 
     * to retrieve Interface components in this Interface's {extended interfaces} 
     * property from the set of Interfaces available (i.e. in-scope) to the Description.
     */ 
    private Description fDescriptionComponent = null;
    
    private NCName fName = null;
    private List fExtends = new Vector();
    private List fStyleDefault = new Vector();
    private List fInterfaceFaultElements = new Vector();
    private List fInterfaceOperationElements = new Vector();

    /* ************************************************************
     *  Interface interface methods (the WSDL Component model)
     * ************************************************************/
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getName()
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getName()
     */
    public QName getName() 
    {
        QName name = null;
        if (fName != null) {
            String[] tns = DescriptionImpl.getTargetNamespaceAndPrefix(this);
            name = new QName(tns[0], fName.toString(), tns[1]);
        }
        return name;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getExtendedInterface(javax.xml.namespace.QName)
     */
    public Interface getExtendedInterface(QName qname) 
    {
        Interface intface = fDescriptionComponent.getInterface(qname); 
        return intface;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getExtendedInterfaces()
     */
    public Interface[] getExtendedInterfaces() 
    {
        List interfaces = new Vector();
        for(Iterator it = fExtends.iterator(); it.hasNext();)
        {
            QName qn = (QName)it.next();
            Interface intface = getExtendedInterface(qn);
            if(intface != null) interfaces.add(intface);
        }
        
        Interface[] array = new Interface[interfaces.size()];
        interfaces.toArray(array);
        return array;
    }

    /* 
     * @see org.apache.woden.wsdl20.Interface#getInterfaceFaults()
     */
    public InterfaceFault[] getInterfaceFaults() 
    {
        InterfaceFault[] array = new InterfaceFault[fInterfaceFaultElements.size()];
        fInterfaceFaultElements.toArray(array);
        return array;
    }

    /* 
     * @see org.apache.woden.wsdl20.Interface#getInterfaceFault(javax.xml.namespace.QName)
     */
    public InterfaceFault getInterfaceFault(QName faultName) 
    {
        return (InterfaceFault)getInterfaceFaultElement(faultName);
    }
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getAllInterfaceFaults()
     */
    public InterfaceFault[] getAllInterfaceFaults() 
    {
        List allInterfaces = new Vector();
        List allInterfaceFaults = new Vector();
        getAllInterfaceFaults(this, allInterfaces, allInterfaceFaults);
        
        InterfaceFault[] array = new InterfaceFault[allInterfaceFaults.size()];
        allInterfaceFaults.toArray(array);
        return array;
    }

    /* 
     * @see org.apache.woden.wsdl20.Interface#getFromAllInterfaceFaults(javax.xml.namespace.QName)
     */
    public InterfaceFault getFromAllInterfaceFaults(QName faultName) 
    {
        InterfaceFault theFault = null;
        if(faultName != null) {
            InterfaceFault[] faults = getAllInterfaceFaults();
            for(int i=0; i<faults.length; i++) {
                InterfaceFault fault = faults[i];
                if(faultName.equals(fault.getName())) {
                    theFault = fault;
                    break;
                }
            }
        }
        return theFault;
    }

    /* 
    /* 
     * @see org.apache.woden.wsdl20.Interface#getInterfaceOperations()
     */
    public InterfaceOperation[] getInterfaceOperations() 
    {
        InterfaceOperation[] array = new InterfaceOperation[fInterfaceOperationElements.size()];
        fInterfaceOperationElements.toArray(array);
        return array;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getInterfaceOperation(javax.xml.namespace.QName)
     */
    public InterfaceOperation getInterfaceOperation(QName operName) 
    {
        return (InterfaceOperation)getInterfaceOperationElement(operName);
    }
    
    /* 
     * @see org.apache.woden.wsdl20.Interface#getAllInterfaceOperations()
     */
    public InterfaceOperation[] getAllInterfaceOperations() 
    {
        List allInterfaces = new Vector();
        List allInterfaceOperations = new Vector();
        getAllInterfaceOperations(this, allInterfaces, allInterfaceOperations);
        
        InterfaceOperation[] array = new InterfaceOperation[allInterfaceOperations.size()];
        allInterfaceOperations.toArray(array);
        return array;
    }

    /* 
     * @see org.apache.woden.wsdl20.Interface#getFromAllInterfaceOperations(javax.xml.namespace.QName)
     */
    public InterfaceOperation getFromAllInterfaceOperations(QName operName) 
    {
        InterfaceOperation theOper = null;
        if(operName != null) {
            InterfaceOperation[] opers = getAllInterfaceOperations();
            for(int i=0; i<opers.length; i++) {
                InterfaceOperation oper = opers[i];
                if(operName.equals(oper.getName())) {
                    theOper = oper;
                    break;
                }
            }
        }
        return theOper;
    }

    /* 
     * @see org.apache.woden.wsdl20.Interface#toElement()
     */
    public InterfaceElement toElement() {
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#isEquivalentTo(WSDLComponent)
     */
    public boolean isEquivalentTo(WSDLComponent comp)
    {
        //compare object refs
        if(this == comp) return true;
        
        if(!(comp instanceof Interface)) {
            return false;
        }
        
        Interface other = (Interface)comp;
        
        //compare {name}
        QName myName = getName();
        if(myName != null && !myName.equals(other.getName())) return false;
        
        /* To compare {extended interfaces} we cannot just retrieve and compare the two sets 
         * of extended Interface components because we'd enter a recursive loop. To get the
         * extended interfaces (i.e. to resolve the qnames in the 'extends' attribute)
         * we need to get the set of interfaces available to the Description, which in turn 
         * invokes this equivalence checking method.
         * Instead, compare just the qnames in the 'extends' attributes, but we first 
         * eliminate any duplicate qnames to ensure we make a logical test for 
         * equivalence (i.e. use Set comparison).
         */
        Set thisExtendsSet = new HashSet(fExtends);
        QName[] otherExtends = ((InterfaceElement)other).getExtendedInterfaceNames();
        Set otherExtendsSet = new HashSet(); 
        for(int i=0; i<otherExtends.length; i++)
        {
            otherExtendsSet.add(otherExtends[i]);
        }
        if(thisExtendsSet.size() != otherExtendsSet.size()) return false;
        if(!(thisExtendsSet.containsAll(otherExtendsSet) && otherExtendsSet.containsAll(thisExtendsSet)))
        {
            return false;
        }
        
        //TODO compare {interface faults}
        //TODO compare {interface operations}
            
        return true;    
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#equals(WSDLComponent)
     * @deprecated Use isEquivalentTo(WSDLComponent)
     * 
     * TODO Remove. Deprecated, replaced by isEquivalentTo.
     */
    public boolean equals(WSDLComponent comp)
    {
        //compare object refs
        if(this == comp) return true;
        
        if(!(comp instanceof Interface)) {
            return false;
        }
        
        Interface other = (Interface)comp;
        
        //compare {name}
        QName myName = getName();
        if(myName != null && !myName.equals(other.getName())) return false;
        
        /* To compare {extended interfaces} we cannot just retrieve and compare the two sets 
         * of extended Interface components because we'd enter a recursive loop. To get the
         * extended interfaces (i.e. to resolve the qnames in the 'extends' attribute)
         * we need to get the set of interfaces available to the Description, which in turn 
         * invokes this equivalence checking method.
         * Instead, compare just the qnames in the 'extends' attributes, but we first 
         * eliminate any duplicate qnames to ensure we make a logical test for 
         * equivalence (i.e. use Set comparison).
         */
        Set thisExtendsSet = new HashSet(fExtends);
        QName[] otherExtends = ((InterfaceElement)other).getExtendedInterfaceNames();
        Set otherExtendsSet = new HashSet(); 
        for(int i=0; i<otherExtends.length; i++)
        {
            otherExtendsSet.add(otherExtends[i]);
        }
        if(thisExtendsSet.size() != otherExtendsSet.size()) return false;
        if(!(thisExtendsSet.containsAll(otherExtendsSet) && otherExtendsSet.containsAll(thisExtendsSet)))
        {
            return false;
        }
        
        //TODO compare {interface faults}
        //TODO compare {interface operations}
            
        return true;    
    }
    
    /* ************************************************************
     *  InterfaceElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#setName(NCName)
     */
    public void setName(NCName name) {
        fName = name;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#addStyleDefaultURI(URI)
     */
    public void addStyleDefaultURI(URI uri)
    {
        if(uri != null) {
            fStyleDefault.add(uri);
        }
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getStyleDefault()
     */
    public URI[] getStyleDefault()
    {
        URI[] array = new URI[fStyleDefault.size()];
        fStyleDefault.toArray(array);
        return array;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#addExtendedInterfaceName(javax.xml.namespace.QName)
     */
    public void addExtendedInterfaceName(QName interfaceName)
    {
        if(interfaceName != null) {
            fExtends.add(interfaceName);
        }
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#removeExtendedInterfaceName(javax.xml.namespace.QName)
     */
    public void removeExtendedInterfaceName(QName interfaceName)
    {
        if(interfaceName != null) {
            fExtends.remove(interfaceName);
        }
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getExtendedInterfaceNames()
     */
    public QName[] getExtendedInterfaceNames()
    {
        QName[] array = new QName[fExtends.size()];
        fExtends.toArray(array);
        return array;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getExtendedInterfaceElement(javax.xml.namespace.QName)
     */
    public InterfaceElement getExtendedInterfaceElement(QName interfaceName)
    {
        //Cast the containing description element to a description component to re-use its
        //logic for navigating a composite wsdl to retrieve the in-scope top-level components.
        Description desc = (Description)fParentElem;
        InterfaceElement intface = (InterfaceElement)desc.getInterface(interfaceName); 
        return intface;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getExtendedInterfaceElements()
     */
    public InterfaceElement[] getExtendedInterfaceElements()
    {
        List interfaces = new Vector();
        for(Iterator it = fExtends.iterator(); it.hasNext();)
        {
            QName qn = (QName)it.next();
            InterfaceElement intface = getExtendedInterfaceElement(qn);
            if(intface != null) interfaces.add(intface);
        }
        
        InterfaceElement[] array = new InterfaceElement[interfaces.size()];
        interfaces.toArray(array);
        return array;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#addInterfaceFaultElement()
     */
    public InterfaceFaultElement addInterfaceFaultElement()
    {
        InterfaceFaultImpl fault = new InterfaceFaultImpl();
        fInterfaceFaultElements.add(fault);
        fault.setParentElement(this);
        return fault;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getInterfaceFaultElement(javax.xml.namespace.QName)
     */
    public InterfaceFaultElement getInterfaceFaultElement(QName faultName)
    {
        InterfaceFaultElement fault = null;
        
        if(faultName != null)
        {
            InterfaceFaultElement tempFault = null;
            for(Iterator i=fInterfaceFaultElements.iterator(); i.hasNext(); )
            {
                tempFault = (InterfaceFaultElement)i.next();
                if(faultName.equals(tempFault.getName()))
                {
                    fault = tempFault;
                    break;
                }
            }
        }
        
        return fault;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getInterfaceFaultElements()
     */
    public InterfaceFaultElement[] getInterfaceFaultElements()
    {
        InterfaceFaultElement[] array = new InterfaceFaultElement[fInterfaceFaultElements.size()];
        fInterfaceFaultElements.toArray(array);
        return array;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#addInterfaceOperationElement()
     */
    public InterfaceOperationElement addInterfaceOperationElement()
    {
        InterfaceOperationImpl operation = new InterfaceOperationImpl();
        fInterfaceOperationElements.add(operation);
        operation.setParentElement(this);
        return operation;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getInterfaceOperationElement(javax.xml.namespace.QName)
     */
    public InterfaceOperationElement getInterfaceOperationElement(QName operName)
    {
        InterfaceOperationElement oper = null;
        
        if(operName != null)
        {
            InterfaceOperationElement tempOper = null;
            for(Iterator i=fInterfaceOperationElements.iterator(); i.hasNext(); )
            {
                tempOper = (InterfaceOperationElement)i.next();
                if(operName.equals(tempOper.getName()))
                {
                    oper = tempOper;
                    break;
                }
            }
        }
        
        return oper;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceElement#getInterfaceOperationElements()
     */
    public InterfaceOperationElement[] getInterfaceOperationElements()
    {
        InterfaceOperationElement[] array = new InterfaceOperationElement[fInterfaceOperationElements.size()];
        fInterfaceOperationElements.toArray(array);
        return array;
    }

    /* 
     * package private, used only by factory methods in this package
     */
    void setParentElement(WSDLElement parent) {
        fParentElem = parent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.NestedElement#getParentElement()
     */
    public WSDLElement getParentElement() {
        return fParentElem;
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/

    /*
     * Retrieve all the interface operations declared by a specified interface or derived
     * from interfaces that it extends, directly or indirectly, and accumulate them in
     * the specified List. Eliminate duplicate operations.
     */
    private void getAllInterfaceOperations(Interface interfac, List allInterfaces, List allOpers) {
        //check for circular Interface references to avoid infinite loop
        if(containsComponent(interfac, allInterfaces)) {
            return;
        } else {
            allInterfaces.add(interfac);
        }
        
        //get the declared operations for the specified Interface
        InterfaceOperation[] declaredOpers = interfac.getInterfaceOperations();
        for(int i=0; i<declaredOpers.length; i++) {
            InterfaceOperation oper = declaredOpers[i];
            if(!containsComponent(oper, allOpers)) {
                allOpers.add(oper);
            }
        }
        
        //get the derived operations from each extended interface
        Interface[] extInts = interfac.getExtendedInterfaces();
        for(int j=0; j<extInts.length; j++) {
            getAllInterfaceOperations(extInts[j], allInterfaces, allOpers);
        }
    }
    
    /*
     * Retrieve all the interface faults declared by a specified interface or derived
     * from interfaces that it extends, directly or indirectly, and accumulate them in
     * the specified List. Eliminate duplicate faults.
     */
    private void getAllInterfaceFaults(Interface interfac, List allInterfaces, List allFaults) {
        //check for circular Interface references to avoid infinite loop
        if(containsComponent(interfac, allInterfaces)) {
            return;
        } else {
            allInterfaces.add(interfac);
        }
        
        //get the declared faults for the specified Interface
        InterfaceFault[] declaredFaults = interfac.getInterfaceFaults();
        for(int i=0; i<declaredFaults.length; i++) {
            InterfaceFault fault = declaredFaults[i];
            if(!containsComponent(fault, allFaults)) {
                allFaults.add(fault);
            }
        }
        
        //get the derived faults from each extended interface
        Interface[] extInts = interfac.getExtendedInterfaces();
        for(int j=0; j<extInts.length; j++) {
            getAllInterfaceFaults(extInts[j], allInterfaces, allFaults);
        }
    }
    
    /*
     * These package private accessors refer to the Description component
     * in which this Interface is contained (i.e. contained in its {interfaces}
     * property). They are declared package private so that they can be used by the
     * Woden implementation without exposing them to the API (i.e. by DescriptionImpl)
     */
    void setDescriptionComponent(Description desc)
    {
        fDescriptionComponent = desc;
    }
    
    Description getDescriptionComponent() {
        return fDescriptionComponent;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        return new FragmentIdentifier(new InterfacePart(fName));
    }
    
}
