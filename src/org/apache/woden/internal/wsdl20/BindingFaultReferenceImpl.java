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

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.InterfaceFaultReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.BindingFaultReferenceElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.BindingFaultReferencePart;

/**
 * This class represents the BindingFaultReference component of the
 * WSDL 2.0 Component model and the &lt;infault&gt; or &lt;outfault&gt; 
 * child element of a WSDL binding &lt;operation&gt;.
 * 
 * @author jkaputin@apache.org
 */
public class BindingFaultReferenceImpl extends NestedImpl 
                                       implements BindingFaultReference, BindingFaultReferenceElement 
{
    private QName fRef = null;
    private Direction fDirection = null;
    private NCName fMessageLabel = null;

    /* ************************************************************
     *  BindingFaultReference interface methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingFaultReference#getInterfaceFaultReference()
     * 
     * The "effective" message label of the binding fault reference must be equal to the message label 
     * of an interface fault reference and the interface fault reference must refer to an interface 
     * fault with its {name} equal to the 'ref' qname of the binding fault reference.
     * 
     * The WSDL 2.0 Part 1 spec says "Define the effective message label of a binding fault reference element 
     * information item  to be either the actual value of the messageLabel attribute information item if it 
     * is present, or the {message label} of the unique placeholder message with {direction} equal to the 
     * message direction if the attribute information item is absent."
     * 
     * The code in this method currently just supports the first type of "effective" message label,
     * where the message label property IS present in the binding fault reference.
     * 
     * TODO effective message label based on message exchange pattern placeholder message, 
     * where the message label property IS NOT present in the binding fault reference.
     * 
     */
    public InterfaceFaultReference getInterfaceFaultReference() 
    {
        InterfaceFaultReference intFaultRef = null;
        
        if(fRef != null) //if 'ref' is null, we cannot match against an interface fault qname.
        {
            BindingOperation bindOp = (BindingOperation)getParent();
            InterfaceOperation intOp = bindOp.getInterfaceOperation();
            if(intOp != null)
            {
                //Determine the "effective" msg label for this binding fault ref.
                NCName effectiveMsgLabel = null;
                if(fMessageLabel != null) 
                {
                    effectiveMsgLabel = fMessageLabel;
                } 
                else 
                {
                    //implement placeholder effective msg label, as per the todo comment above
                }
                
                //Now match the effective msg label against the msg label of an interface fault reference
                //that refers to an interface fault whose qname matches the 'ref' attribute.
                if(effectiveMsgLabel != null)
                {
                    InterfaceFaultReference[] intFaultRefs = intOp.getInterfaceFaultReferences();
                    for(int i=0; i<intFaultRefs.length; i++)
                    {
                        InterfaceFaultReference tempIntFaultRef = intFaultRefs[i];
                        InterfaceFault tempIntFault = tempIntFaultRef.getInterfaceFault();
                        QName intFaultName = (tempIntFault != null ? tempIntFault.getName() : null); 
                        if(fRef.equals(intFaultName) &&
                           effectiveMsgLabel.equals(tempIntFaultRef.getMessageLabel()))
                        {
                            intFaultRef = tempIntFaultRef;
                            break;
                        }
                    }
                }
            }
        }
        
        return intFaultRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingFaultReference#toElement()
     */
    public BindingFaultReferenceElement toElement() {
        return this;
    }

    /* ************************************************************
     *  BindingFaultReferenceElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultReferenceElement#setRef(javax.xml.namespace.QName)
     */
    public void setRef(QName qname) {
        fRef = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultReferenceElement#getRef()
     */
    public QName getRef() {
        return fRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultReferenceElement#getInterfaceFaultReferenceElement()
     * 
     * TODO added 'effective msg label' matching, as per the component model method getInterfFaultRef.
     */
    public InterfaceFaultReferenceElement getInterfaceFaultReferenceElement() 
    {
        InterfaceFaultReferenceElement intFaultRef = null;
        
        if(fRef != null && fMessageLabel != null)
        {
            BindingOperationElement bindOp = (BindingOperationElement)getParentElement();
            InterfaceOperationElement intOp = bindOp.getInterfaceOperationElement();
            if(intOp != null)
            {
                InterfaceFaultReferenceElement[] intFaultRefs = intOp.getInterfaceFaultReferenceElements();
                for(int i=0; i<intFaultRefs.length; i++)
                {
                    InterfaceFaultReferenceElement temp = intFaultRefs[i];
                    if(fRef.equals(temp.getRef()) &&
                       fMessageLabel.equals(temp.getMessageLabel()))
                    {
                        intFaultRef = temp;
                        break;
                    }
                }
            }
        }
        
        return intFaultRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultReferenceElement#setMessageLabel(org.apache.woden.wsdl20.enumeration.MessageLabel)
     */
    public void setMessageLabel(NCName msgLabel) {
        fMessageLabel = msgLabel;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultReferenceElement#getMessageLabel()
     */
    public NCName getMessageLabel() {
        return fMessageLabel;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        //Find parent components.
        BindingOperation bindingOperationComp = (BindingOperation)getParent();
        Binding bindingComp = (Binding)bindingOperationComp.getParent();
        InterfaceOperation interfaceOperationComp = bindingOperationComp.getInterfaceOperation();
        
        //Get needed properties.
        NCName binding = new NCName(bindingComp.getName().getLocalPart());
        QName interfaceOperation = interfaceOperationComp.getName();
       
        //Return a new Fragment Identifier.
        return new FragmentIdentifier(new BindingFaultReferencePart(binding, interfaceOperation, fMessageLabel, fRef));
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
}
