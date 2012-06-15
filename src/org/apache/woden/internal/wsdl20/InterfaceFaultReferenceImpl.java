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
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.InterfaceFaultReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.InterfaceFaultReferencePart;

/**
 * This class represents the InterfaceFaultReference component of the 
 * WSDL 2.0 Component model and the &lt;infault&gt; and &lt;outfault&gt; 
 * child elements of an interface &lt;operation&gt;. 
 * 
 * @author jkaputin@apache.org
 */
public class InterfaceFaultReferenceImpl extends NestedImpl
                                         implements InterfaceFaultReference,
                                                    InterfaceFaultReferenceElement 
{
    private NCName fMessageLabel = null;
    private Direction fDirection = null;
    private QName fRef = null;

    /* ************************************************************
     *  InterfaceFaultReference methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceFaultReference#getInterfaceFault()
     */
    public InterfaceFault getInterfaceFault() 
    {
        InterfaceOperation oper = (InterfaceOperation)getParent();
        Interface interfac = (Interface)oper.getParent();
        InterfaceFault intFault = interfac.getFromAllInterfaceFaults(fRef);
        return intFault;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceFaultReference#getMessageLabel()
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement#getMessageLabel()
     */
    public NCName getMessageLabel() {
        return fMessageLabel;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceFaultReference#getDirection()
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement#getDirection()
     */
    public Direction getDirection() {
        return fDirection;
    }

    /*
     * @see org.apache.woden.wsdl20.InterfaceFaultReference#toElement()
     */
    public InterfaceFaultReferenceElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  InterfaceFaultReferenceElement methods (i.e. XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement#setRef(javax.xml.namespace.QName)
     */
    public void setRef(QName faultQName) {
        fRef = faultQName;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement#getRef()
     */
    public QName getRef() {
        return fRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement#getInterfaceFaultElement()
     */
    public InterfaceFaultElement getInterfaceFaultElement() 
    {
        InterfaceFaultElement fault = null;
        InterfaceOperationElement oper = (InterfaceOperationElement)getParentElement();
        InterfaceElement interfac = (InterfaceElement)oper.getParentElement();
        if (interfac != null) {
            InterfaceFault faultComp = ((Interface)interfac).getFromAllInterfaceFaults(fRef);
            if (faultComp != null) {
                fault = faultComp.toElement();
            }
        }
        return fault;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.FaultReferenceElement#setMessageLabel(org.apache.woden.wsdl20.enumeration.MessageLabel)
     */
    public void setMessageLabel(NCName msgLabel) {
        fMessageLabel = msgLabel;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.FaultReferenceElement#setDirection(org.apache.woden.wsdl20.enumeration.Direction)
     */
    public void setDirection(Direction dir) {
        fDirection = dir;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        //Find parent components.
        InterfaceOperation interfaceOperationComp = (InterfaceOperation)getParent();
        Interface interfaceComp = (Interface)interfaceOperationComp.getParent();
        //Get needed properties.
        NCName interfaceName = new NCName(interfaceComp.getName().getLocalPart());
        NCName interfaceOperation = new NCName(interfaceOperationComp.getName().getLocalPart());
        //Return a new FragmentIdentifier.
        return new FragmentIdentifier(new InterfaceFaultReferencePart(interfaceName, interfaceOperation, fMessageLabel, fRef));
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
}
