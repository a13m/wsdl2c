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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.xml.EndpointElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.ServiceElement;
import org.apache.woden.wsdl20.xml.WSDLElement;

import org.apache.woden.wsdl20.fragids.ServicePart;

/**
 * This class represents the Service component and the 
 * &lt;service&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class ServiceImpl extends WSDLComponentImpl 
                         implements Service, ServiceElement 
{
    private WSDLElement fParentElem = null;
    
    /* This field refers to the Description component which contains this Service
     * component in its {services} property. It is set whenever this Service is 
     * returned by that Description's getServices() or getService(QName) methods. 
     * Note that with modularization via a wsdl import or include, this 
     * reference may be different to fDescriptionElement because it refers to the 
     * importing or including description at the top of the wsdl tree (whereas the 
     * latter refers to the description in which this service is directly declared).
     * This field is used to retrieve components that are available (i.e. in-scope) 
     * to the top-level Description component.
     */ 
    private Description fDescriptionComponent = null;
    
    private NCName fName = null;
    private QName fInterfaceName = null;
    private List fEndpoints = new Vector();

    /* ************************************************************
     *  Service interface methods (the WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Service#getName()
     * @see org.apache.woden.wsdl20.xml.ServiceElement#getName()
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
     * @see org.apache.woden.wsdl20.Service#getInterface()
     */
    public Interface getInterface() 
    {
        Description desc = fDescriptionComponent;
        Interface interfac = desc.getInterface(fInterfaceName);
        return interfac;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Service#getEndpoint(org.apache.woden.types.NCName)
     * 
     * TODO add a testcase for this method
     */
    public Endpoint getEndpoint(NCName name) 
    {
        Endpoint endpoint = null;
        if(name != null)
        {
            String nameString = name.toString();
            for(Iterator i=fEndpoints.iterator(); i.hasNext(); )
            {
                Endpoint tmpEP = (Endpoint)i.next();
                String tmpStr = tmpEP.getName() != null ? tmpEP.getName().toString() : null;
                if(nameString.equals(tmpStr))
                {
                    endpoint = tmpEP;
                    break;
                }
            }
        }
        return endpoint;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.Service#getEndpoints()
     */
    public Endpoint[] getEndpoints() {
        Endpoint[] array = new Endpoint[fEndpoints.size()];
        fEndpoints.toArray(array);
        return array;
    }

    /*
     * @see org.apache.woden.wsdl20.Service#toElement()
     */
    public ServiceElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  ServiceElement interface methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ServiceElement#setName(NCName)
     */
    public void setName(NCName name) {
        fName = name;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ServiceElement#setInterfaceName(javax.xml.namespace.QName)
     */
    public void setInterfaceName(QName qname) {
        fInterfaceName = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ServiceElement#getInterfaceName()
     */
    public QName getInterfaceName() {
        return fInterfaceName;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ServiceElement#getInterfaceElement()
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
     * @see org.apache.woden.wsdl20.xml.ServiceElement#addEndpointElement()
     */
    public EndpointElement addEndpointElement() 
    {
        EndpointImpl endpoint = new EndpointImpl();
        fEndpoints.add(endpoint);
        endpoint.setParentElement(this);
        return endpoint;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.ServiceElement#getEndpointElements()
     */
    public EndpointElement[] getEndpointElements() {
        EndpointElement[] array = new EndpointElement[fEndpoints.size()];
        fEndpoints.toArray(array);
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
     * These package private accessors refer to the Description component
     * in which this Service is contained (i.e. contained in its {services}
     * property). They are declared package private so that they can be used by the
     * Woden implementation without exposing them to the API (i.e. by DescriptionImpl)
     */
    void setDescriptionComponent(Description desc) {
        fDescriptionComponent = desc;
    }

    Description getDescriptionComponent() {
        return fDescriptionComponent;
    }
    
    public FragmentIdentifier getFragmentIdentifier() {
        return new FragmentIdentifier(new ServicePart(fName));
    }

}
