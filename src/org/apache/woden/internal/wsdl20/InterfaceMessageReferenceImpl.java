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

import org.apache.woden.types.NCName;
import org.apache.woden.types.QNameTokenUnion;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.InterfaceMessageReferencePart;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.ws.commons.schema.XmlSchemaElement;

/**
 * This class represents the &lt;input&gt; and &lt;output&gt; 
 * child elements of interface operation. 
 * 
 * @author jkaputin@apache.org
 */
public class InterfaceMessageReferenceImpl extends NestedImpl
                                  implements InterfaceMessageReference, 
                                             InterfaceMessageReferenceElement 
{
    //WSDL Component model data
    private NCName fMessageLabel = null;
    private Direction fDirection = null;
    private QNameTokenUnion fElement = null;   
    
    /* ************************************************************
     *  InterfaceMessageReference methods (the WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceMessageReference#getMessageLabel()
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#getMessageLabel()
     */
    public NCName getMessageLabel() {
        return fMessageLabel;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceMessageReference#getDirection()
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#getDirection()
     */
    public Direction getDirection() {
        return fDirection;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceMessageReference#getMessageContentModel()
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

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceMessageReference#getElementDeclaration()
     */
    public ElementDeclaration getElementDeclaration() 
    {
        ElementDeclaration elemDecl = null;
        
        if(fElement != null && fElement.isQName()) {
            InterfaceOperation oper = (InterfaceOperation)getParent();
            Interface interfac = (Interface)oper.getParent();
            Description desc = ((InterfaceImpl)interfac).getDescriptionComponent();
            elemDecl = desc.getElementDeclaration(fElement.getQName());
        }
        return elemDecl;
    }

    /*
     * @see org.apache.woden.wsdl20.InterfaceMessageReference#toElement()
     */
    public InterfaceMessageReferenceElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  InterfaceMessageReferenceElement methods (the XML Element model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#setMessageLabel(org.apache.woden.wsdl20.enumeration.MessageLabel)
     */
    public void setMessageLabel(NCName msgLabel) {
        fMessageLabel = msgLabel;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#setElementName(javax.xml.namespace.QName)
     */
    public void setElement(QNameTokenUnion element) {
        fElement = element;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#getElementName()
     */
    public QNameTokenUnion getElement() {
        return fElement;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#getElement()
     */
    public XmlSchemaElement getXmlSchemaElement() 
    {
        XmlSchemaElement xse = null;
        if(fElement != null && fElement.isQName()) {
            InterfaceOperationElement oper = (InterfaceOperationElement)getParentElement();
            InterfaceElement interfac = (InterfaceElement)oper.getParentElement();
            DescriptionElement desc = (DescriptionElement)interfac.getParentElement();
            TypesElement types = desc.getTypesElement();
            if(types != null) {
                xse = ((TypesImpl)types).getElementDeclaration(fElement.getQName());
            }
        }
        return xse;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement#setDirection(org.apache.woden.wsdl20.enumeration.Direction)
     */
    public void setDirection(Direction dir) {
        fDirection = dir;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        InterfaceOperation interfaceOperationComp = (InterfaceOperation)getParent();
        Interface interfaceComp = (Interface)interfaceOperationComp.getParent();
        
        NCName interfaceName = new NCName(interfaceComp.getName().getLocalPart());
        NCName interfaceOperation = new NCName(interfaceOperationComp.getName().getLocalPart());
        
        return new FragmentIdentifier(new InterfaceMessageReferencePart(interfaceName, interfaceOperation, fMessageLabel));
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
}
