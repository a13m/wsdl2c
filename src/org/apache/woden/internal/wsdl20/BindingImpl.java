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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.WSDLElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.BindingPart;

/**
 * This class represents the Binding component from the WSDL 2.0 Component Model 
 * and the WSDL &lt;binding&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class BindingImpl extends WSDLComponentImpl 
                         implements Binding, BindingElement 
{
    private WSDLElement fParentElem = null;
    
    /* This field refers to the Description component which contains this Binding
     * component in its {bindings} property. It is set whenever this Binding is 
     * returned by that Description's getBindings() or getBinding(QName) methods. 
     * Note that with modularization via a wsdl import or include, this 
     * reference may be different to fDescriptionElement because it refers to the 
     * importing or including description at the top of the wsdl tree (whereas the 
     * latter refers to the description in which this binding is directly declared).
     * This field is used to retrieve components that are available (i.e. in-scope) 
     * to the top-level Description component.
     */ 
    private Description fDescriptionComponent = null;
    
    private NCName fName = null;
    private QName fInterfaceName = null;
    private URI fType = null;
    private List fFaults = new Vector();
    private List fOperations = new Vector();
    
    /* ************************************************************
     *  Binding interface methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Binding#getName()
     * @see org.apache.woden.wsdl20.xml.BindingElement#getName()
     */
    public QName getName() {
        QName name = null;
        if (fName != null) {
            String[] tns = DescriptionImpl.getTargetNamespaceAndPrefix(this);
            name = new QName(tns[0], fName.toString(), tns[1]);
        }
        return name;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Binding#getInterface()
     */
    public Interface getInterface() 
    {
        Interface interfac = fDescriptionComponent.getInterface(fInterfaceName); 
        return interfac;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Binding#getType()
     * @see org.apache.woden.wsdl20.xml.BindingElement#getType()
     */
    public URI getType() {
        return fType;
    }

    /*
     * @see org.apache.woden.wsdl20.Binding#getBindingFaults()
     */
    public BindingFault[] getBindingFaults() 
    {
        BindingFault[] array = new BindingFault[fFaults.size()];
        fFaults.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Binding#getBindingOperations()
     */
    public BindingOperation[] getBindingOperations() 
    {
        BindingOperation[] array = new BindingOperation[fOperations.size()];
        fOperations.toArray(array);
        return array;
    }

    /*
     * @see org.apache.woden.wsdl20.Binding#toElement()
     */
    public BindingElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  BindingElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#setName(javax.xml.namespace.QName)
     */
    public void setName(NCName name) {
        fName = name;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#setInterfaceName(javax.xml.namespace.QName)
     */
    public void setInterfaceName(QName qname) {
        fInterfaceName = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#getInterfaceName()
     */
    public QName getInterfaceName() {
        return fInterfaceName;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.BindingElement#getInterfaceElement()
     */
    public InterfaceElement getInterfaceElement() 
    {
        //Cast the containing description element to a description component to re-use its
        //logic for navigating a composite wsdl to retrieve the in-scope top-level components.
        Description desc = (Description)fParentElem;
        InterfaceElement interfac = (InterfaceElement)desc.getInterface(fInterfaceName); 
        return interfac;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#setType(java.net.URI)
     */
    public void setType(URI type) {
        fType = type;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.BindingElement#addBindingFaultElement()
     */
    public BindingFaultElement addBindingFaultElement() 
    {
        BindingFaultImpl fault = new BindingFaultImpl();
        fFaults.add(fault);
        fault.setParentElement(this);
        return fault;
    }

    /* 
     * @see org.apache.woden.wsdl20.xml.BindingElement#getBindingFaultElements()
     */
    public BindingFaultElement[] getBindingFaultElements() 
    {
        BindingFaultElement[] array = new BindingFaultElement[fFaults.size()];
        fFaults.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#addBindingOperationElement()
     */
    public BindingOperationElement addBindingOperationElement() 
    {
        BindingOperationImpl operation = new BindingOperationImpl();
        fOperations.add(operation);
        operation.setParentElement(this);
        return operation;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingElement#getBindingOperationElements()
     */
    public BindingOperationElement[] getBindingOperationElements() 
    {
        BindingOperationElement[] array = new BindingOperationElement[fOperations.size()];
        fOperations.toArray(array);
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
     * Get the binding fault with the specified 'ref' attribute qname.
     * 
     * TODO decide if this type of qname-based accessor is needed, either internally or on API.
     * TODO also consider if getBindingFaultWithRef is needed (i.e. component model)
     */
    public BindingFaultElement getBindingFaultElementWithRef(QName qname) 
    {
        BindingFaultElement fault = null;
        if(qname != null)
        {
            BindingFaultElement bindFault = null;
            Iterator i = fFaults.iterator();
            while(i.hasNext())
            {
                bindFault = (BindingFaultElement)i.next();
                if(qname.equals(bindFault.getRef())) 
                {
                    fault = bindFault;
                    break;
                }
            }
        }
        return fault;
    }

    /*
     * Get the binding operation with the specified 'ref' attribute qname.
     * 
     * TODO decide if this type of qname-based accessor is needed, either internally or on API.
     * TODO also consider if getBindingOperationWithRef is needed (i.e. component model)
     */
    public BindingOperationElement getBindingOperationElementWithRef(QName qname) 
    {
        BindingOperationElement operation = null;
        if(qname != null)
        {
            BindingOperationElement bindOp = null;
            Iterator i = fOperations.iterator();
            while(i.hasNext())
            {
                bindOp = (BindingOperationElement)i.next();
                if(qname.equals(bindOp.getRef())) 
                {
                    operation = bindOp;
                    break;
                }
            }
        }
        return operation;
    }
    
    /*
     * This method sets specifies the Description component in which this Binding is contained 
     * (i.e. this Binding is contained in the Description's {bindings} property). 
     * It should only be invoked by DescriptionImpl when the getBindings() method is called,
     * so it is declared package private to restrict access to it.
     */
    void setDescriptionComponent(Description desc) {
        fDescriptionComponent = desc;
    }

    /*
     * This is method returns the Description component in which this Binding is contained.
     * (i.e. the Description specified on the setDescriptionComponent method above).
     * Ideally it would be package private too, but it is needed by component extensions for
     * resolving qnames to ElementDeclarations and TypeDefinitions contained within the same
     * Description, so it has been defined as a non-API public method.
     * 
     * TODO see if there is a way to make this method non-public. 
     */
    public Description getDescriptionComponent() {
        return fDescriptionComponent;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        return new FragmentIdentifier(new BindingPart(this));
    }

}
