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

import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingMessageReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultReferenceElement;
import org.apache.woden.wsdl20.xml.BindingMessageReferenceElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.BindingOperationPart;

/**
 * This class represents the BindingOperation component from the WSDL 2.0 Component Model 
 * and the &lt;operation&gt; child element of the WSDL &lt;binding&gt; element.
 * 
 * @author jkaputin@apache.org
 */
public class BindingOperationImpl extends NestedImpl 
                                  implements BindingOperation, BindingOperationElement 
{
    private QName fRef = null;
    private List fMessageRefs = new Vector();
    private List fFaultRefs = new Vector();

    /* ************************************************************
     *  BindingOperation interface methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingOperation#getInterfaceOperation()
     */
    public InterfaceOperation getInterfaceOperation() 
    {
        InterfaceOperation oper = null;
        Binding binding = (Binding)getParent();
        Interface interfac = binding.getInterface();
        if(interfac != null) {
            oper = interfac.getFromAllInterfaceOperations(fRef);
        }
        return oper;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingOperation#getBindingMessageReferences()
     */
    public BindingMessageReference[] getBindingMessageReferences() {
        BindingMessageReference[] array = new BindingMessageReference[fMessageRefs.size()];
        fMessageRefs.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingOperation#getBindingFaultReferences()
     */
    public BindingFaultReference[] getBindingFaultReferences() {
        BindingFaultReference[] array = new BindingFaultReference[fFaultRefs.size()];
        fFaultRefs.toArray(array);
        return array;
    }

    /*
     * @see org.apache.woden.wsdl20.BindingOperation#toElement()
     */
    public BindingOperationElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  BindingOperationElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#setRef(javax.xml.namespace.QName)
     */
    public void setRef(QName qname) {
        fRef = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#getRef()
     */
    public QName getRef() {
        return fRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#getInterfaceOperationElement()
     */
    public InterfaceOperationElement getInterfaceOperationElement() 
    {
        InterfaceOperationElement oper = null;
        BindingElement binding = (BindingElement)getParentElement();
        InterfaceElement interfac = binding.getInterfaceElement();
        if(interfac != null) {
            InterfaceOperation operComp = ((Interface)interfac).getFromAllInterfaceOperations(fRef);
            if(operComp != null) {
                oper = operComp.toElement();
            }
        }
        return oper;

    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#addBindingMessageReferenceElement()
     */
    public BindingMessageReferenceElement addBindingMessageReferenceElement() 
    {
        BindingMessageReferenceImpl msgRef = new BindingMessageReferenceImpl();
        fMessageRefs.add(msgRef);
        msgRef.setParentElement(this);
        return msgRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#removeBindingMessageReferenceElement(org.apache.woden.wsdl20.xml.BindingMessageReferenceElement)
     */
    public void removeBindingMessageReferenceElement(BindingMessageReferenceElement msgRef) {
        fMessageRefs.remove(msgRef);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#getBindingMessageReferenceElements()
     */
    public BindingMessageReferenceElement[] getBindingMessageReferenceElements() {
        BindingMessageReferenceElement[] array = new BindingMessageReferenceElement[fMessageRefs.size()];
        fMessageRefs.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#addBindingFaultReferenceElement()
     */
    public BindingFaultReferenceElement addBindingFaultReferenceElement() 
    {
        BindingFaultReferenceImpl faultRef = new BindingFaultReferenceImpl();
        fFaultRefs.add(faultRef);
        faultRef.setParentElement(this);
        return faultRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#removeBindingFaultReferenceElement(org.apache.woden.wsdl20.xml.BindingFaultReferenceElement)
     */
    public void removeBindingFaultReferenceElement(BindingFaultReferenceElement faultRef) {
        fFaultRefs.remove(faultRef);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingOperationElement#getBindingFaultReferenceElements()
     */
    public BindingFaultReferenceElement[] getBindingFaultReferenceElements() {
        BindingFaultReferenceElement[] array = new BindingFaultReferenceElement[fFaultRefs.size()];
        fFaultRefs.toArray(array);
        return array;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        //Find parent component and get needed properties.
        Binding bindingComp = (Binding)getParent();
        NCName binding = new NCName(bindingComp.getName().getLocalPart());
        
        //Return a new FragmentIdentifier.
        return new FragmentIdentifier(new BindingOperationPart (binding , fRef));
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
}
