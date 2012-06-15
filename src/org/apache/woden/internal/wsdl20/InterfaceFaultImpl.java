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
import org.apache.woden.types.QNameTokenUnion;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.ws.commons.schema.XmlSchemaElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.InterfaceFaultPart;

/**
 * This class represents the InterfaceFault component from the WSDL 2.0 Component 
 * Model and the &lt;fault&gt; child element of the &lt;interface&gt; element. 
 * 
 * @author jkaputin@apache.org
 */
public class InterfaceFaultImpl extends NestedImpl
                                implements InterfaceFault,
                                           InterfaceFaultElement 
{
    //WSDL Component model data
    private NCName fName = null;

    //XML Element model data
    private QNameTokenUnion fElement = null;
    
    /* ************************************************************
     *  InterfaceFault methods (i.e. WSDL Component model)
     * ************************************************************/
    
    /*
     * @see org.apache.woden.wsdl20.InterfaceFault#getName()
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultElement#getName()
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
     * @see org.apache.woden.wsdl20.InterfaceFault#getMessageContentModel()
     */
    public String getMessageContentModel() {
        String model = Constants.NMTOKEN_OTHER;;
        
        if (fElement != null) {
            if (fElement.isQName()) {
                model = Constants.NMTOKEN_ELEMENT;
            } else if(fElement.isToken()) {
                model = fElement.getToken();
            }
        }
        return model;
    }

    /*
     * @see org.apache.woden.wsdl20.InterfaceFault#getElementDeclaration()
     */
    public ElementDeclaration getElementDeclaration() 
    {
        ElementDeclaration elemDecl = null;
        
        if(fElement != null && fElement.isQName()) {
            Interface interfac = (Interface)getParent();
            Description desc = ((InterfaceImpl)interfac).getDescriptionComponent();
            elemDecl = desc.getElementDeclaration(fElement.getQName());
        }
        return elemDecl;
    }
    
    /*
     * @see org.apache.woden.wsdl20.InterfaceFault#toElement()
     */
    public InterfaceFaultElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  InterfaceFaultElement methods (i.e. XML Element model)
     * ************************************************************/
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultElement#setName(NCName)
     */
    public void setName(NCName name)
    {
        fName = name;
    }
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultElement#setElementName(QName)
     */
    public void setElement(QNameTokenUnion qnameTokenUnion)
    {
        fElement = qnameTokenUnion;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultElement#getElementName()
     */
    public QNameTokenUnion getElement() 
    {
        return fElement;
    }

    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceFaultElement#getElement()
     */
    public XmlSchemaElement getXmlSchemaElement() 
    {
        XmlSchemaElement xse = null;
        if (fElement != null && fElement.isQName()) {
            InterfaceElement interfac = (InterfaceElement)getParentElement();
            DescriptionElement desc = (DescriptionElement)interfac.getParentElement();
            TypesElement types = desc.getTypesElement();
            if(types != null) {
                xse = ((TypesImpl)types).getElementDeclaration(fElement.getQName());
            }
        }
        return xse;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        Interface interfaceComp = (Interface)getParent();
        
        NCName interfaceName = new NCName(interfaceComp.getName().getLocalPart());
        
        return new FragmentIdentifier(new InterfaceFaultPart(interfaceName, fName));
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
}
