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
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.BindingFaultPart;

/**
 * This class represents the BindingFault component from the WSDL 2.0 Component Model 
 * and the &lt;fault&gt; child element of the WSDL &lt;binding&gt; element.
 * 
 * @author jkaputin@apache.org
 */
public class BindingFaultImpl extends NestedImpl 
                              implements BindingFault, BindingFaultElement 
{
    private QName fRef = null;
    
    /* ************************************************************
     *  BindingFault interface methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.BindingFault#getInterfaceFault()
     */
    public InterfaceFault getInterfaceFault() 
    {
        InterfaceFault fault = null;
        Binding binding = (Binding)getParent();
        Interface interfac = binding.getInterface();
        if(interfac != null) {
            fault = interfac.getFromAllInterfaceFaults(fRef);
        }
        return fault;
    }

    /*
     * @see org.apache.woden.wsdl20.BindingFault#toElement()
     */
    public BindingFaultElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  BindingFaultElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultElement#setRef(javax.xml.namespace.QName)
     */
    public void setRef(QName qname) {
        fRef = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultElement#getRef()
     */
    public QName getRef() {
        return fRef;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.BindingFaultElement#getInterfaceFaultElement()
     */
    public InterfaceFaultElement getInterfaceFaultElement() 
    {
        InterfaceFaultElement fault = null;
        BindingElement binding = (BindingElement)getParentElement();
        InterfaceElement interfac = binding.getInterfaceElement();
        if(interfac != null) {
            InterfaceFault faultComp = ((Interface)interfac).getFromAllInterfaceFaults(fRef);
            if (faultComp != null) {
                fault = faultComp.toElement();
            }
        }
        return fault;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        //Find properties needed.
        Binding bindingComp = (Binding)getParent();
        NCName binding = new NCName(bindingComp.getName().getLocalPart());
        
        //Return a new FragmentIdentifier.
        return new FragmentIdentifier(new BindingFaultPart(binding,  fRef));
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    

}
