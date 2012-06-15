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
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.InterfaceFaultReference;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.InterfaceOperationPart;

/**
 * This class represents the InterfaceOperation component from the WSDL 2.0 Component 
 * Model and the &lt;operation&gt; child element of the &lt;interface&gt; element. 
 * 
 * @author jkaputin@apache.org
 */
public class InterfaceOperationImpl extends NestedImpl
                                    implements InterfaceOperation, 
                                               InterfaceOperationElement 
{
    //Component data
    private NCName fName = null;
    private URI fMessageExchangePattern = null;
    private List fStyle = new Vector();
    
    //XML data
    private List fMessageRefs = new Vector();
    private List fFaultRefs = new Vector();

    /* ************************************************************
     *  InterfaceOperation methods (the WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceOperation#getName()
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#getName()
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

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceOperation#getMessageExchangePattern()
     */
    public URI getMessageExchangePattern() 
    {
        return (fMessageExchangePattern != null) ?
                fMessageExchangePattern : Constants.MEP_URI_IN_OUT;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceOperation#getInterfaceMessageReferences()
     */
    public InterfaceMessageReference[] getInterfaceMessageReferences() 
    {
        InterfaceMessageReference[] array = new InterfaceMessageReference[fMessageRefs.size()];
        fMessageRefs.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceOperation#getInterfaceFaultReferences()
     */
    public InterfaceFaultReference[] getInterfaceFaultReferences() 
    {
        InterfaceFaultReference[] array = new InterfaceFaultReference[fFaultRefs.size()];
        fFaultRefs.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.InterfaceOperation#getStyle()
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#getStyle()
     */
    public URI[] getStyle() 
    {
        URI[] array = new URI[0];
        if(fStyle.size() > 0)
        {
            array = new URI[fStyle.size()];
            fStyle.toArray(array);
        } else {
            InterfaceElement intf = (InterfaceElement)getParentElement();
            URI[] styleDef = intf.getStyleDefault();
            if(styleDef.length > 0) {
                array = styleDef;
            }
        }
        return array;
    }

    /*
     * @see org.apache.woden.wsdl20.InterfaceOperation#toElement()
     */
    public InterfaceOperationElement toElement() {
        return this;
    }
    
    /* ************************************************************
     *  InterfaceOperationElement methods (the XML model)
     * ************************************************************/
    
    /* 
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#setName(NCName)
     */
    public void setName(NCName name) 
    {
        fName = name;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#setPattern(URI)
     */
    public void setPattern(URI uri)
    {
        fMessageExchangePattern = uri;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#getPattern()
     */
    public URI getPattern()
    {
        return fMessageExchangePattern;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#addStyleURI(URI)
     */
    public void addStyleURI(URI uri)
    {
        if(uri != null) {
            fStyle.add(uri);
        }
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#removeStyleURI(URI)
     */
    public void removeStyleURI(URI uri)
    {
        fStyle.remove(uri);
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#addInterfaceMessageReferenceElement()
     */
    public InterfaceMessageReferenceElement addInterfaceMessageReferenceElement()
    {
        InterfaceMessageReferenceImpl msgRef = new InterfaceMessageReferenceImpl();
        fMessageRefs.add(msgRef);
        msgRef.setParentElement(this);
        return msgRef;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#removeInterfaceMessageReferenceElement(InterfaceMessageReferenceElement)
     */
    public void removeInterfaceMessageReferenceElement(InterfaceMessageReferenceElement msgRef)
    {
        fMessageRefs.remove(msgRef);
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#getInterfaceMessageReferenceElements()
     */
    public InterfaceMessageReferenceElement[] getInterfaceMessageReferenceElements()
    {
        InterfaceMessageReferenceElement[] array = new InterfaceMessageReferenceElement[fMessageRefs.size()];
        fMessageRefs.toArray(array);
        return array;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#addInterfaceFaultReferenceElement()
     */
    public InterfaceFaultReferenceElement addInterfaceFaultReferenceElement()
    {
        InterfaceFaultReferenceImpl faultRef = new InterfaceFaultReferenceImpl();
        fFaultRefs.add(faultRef);
        faultRef.setParentElement(this);
        return faultRef;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#removeInterfaceFaultReferenceElement(InterfaceFaultReferenceElement)
     */
    public void removeInterfaceFaultReferenceElement(InterfaceFaultReferenceElement faultRef)
    {
        fFaultRefs.remove(faultRef);
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.InterfaceOperationElement#getInterfaceFaultReferenceElements()
     */
    public InterfaceFaultReferenceElement[] getInterfaceFaultReferenceElements()
    {
        InterfaceFaultReferenceElement[] array = new InterfaceFaultReferenceElement[fFaultRefs.size()];
        fFaultRefs.toArray(array);
        return array;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getFragmentIdentifier()
     */
    public FragmentIdentifier getFragmentIdentifier() {
        NCName interfaceName = new NCName(((Interface)this.getParent()).getName().getLocalPart());
        return new FragmentIdentifier(new InterfaceOperationPart(interfaceName, fName));
    }
}
