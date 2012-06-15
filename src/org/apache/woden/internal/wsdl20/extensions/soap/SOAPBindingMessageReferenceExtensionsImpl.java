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
package org.apache.woden.internal.wsdl20.extensions.soap;

import java.net.URI;

import org.apache.woden.ErrorReporter;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.NestedComponent;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.extensions.http.HTTPHeader;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock;
import org.apache.woden.wsdl20.extensions.soap.SOAPModule;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.StringAttr;

/**
 * This class defines the properties from the SOAP namespace
 * added to the WSDL <code>BindingMessageReference</code> component as part 
 * of the SOAP binding extension defined by the WSDL 2.0 spec. 
 * 
 * @author jkaputin@apache.org
 */
public class SOAPBindingMessageReferenceExtensionsImpl extends BaseComponentExtensionContext 
                                                       implements SOAPBindingMessageReferenceExtensions 
{

    public SOAPBindingMessageReferenceExtensionsImpl(WSDLComponent parent, 
            URI extNamespace, ErrorReporter errReporter) {
        
        super(parent, extNamespace, errReporter);
    }
    
    /* ************************************************************
     *  Methods declared by ComponentExtensionContext
     *  
     *  These are the abstract methods inherited from BaseComponentExtensionContext,
     *  to be implemented by this subclass.
     * ************************************************************/

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperties()
     */
    public ExtensionProperty[] getProperties() {
        
        return new ExtensionProperty[] {
                getProperty(SOAPConstants.PROP_SOAP_MODULES),
                getProperty(SOAPConstants.PROP_SOAP_HEADERS)};
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        if(SOAPConstants.PROP_SOAP_MODULES.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_MODULES, getSoapModules());
            
        } else if(SOAPConstants.PROP_SOAP_HEADERS.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_HEADERS, getSoapHeaders());
            
        } else {
            return null; //the specified property name does not exist
        }
        
    }
    
    /* ************************************************************
     *  Additional methods declared by SOAPBindingFaultExtensions
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions#getSoapModules()
     */
    public SOAPModule[] getSoapModules() 
    {
        ExtensionElement[] extEls = ((WSDLElement)getParent())
            .getExtensionElementsOfType(SOAPConstants.Q_ELEM_SOAP_MODULE);
        int len = extEls.length;
        SOAPModule[] soapMods = new SOAPModule[len];
        System.arraycopy(extEls, 0, soapMods, 0, len);
        return soapMods;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions#getSoapHeaders()
     */
    public SOAPHeaderBlock[] getSoapHeaders() 
    {
        ExtensionElement[] extEls = ((WSDLElement)getParent())
            .getExtensionElementsOfType(SOAPConstants.Q_ELEM_SOAP_HEADER);
        int len = extEls.length;
        SOAPHeaderBlock[] soapHeaders = new SOAPHeaderBlock[len];
        System.arraycopy(extEls, 0, soapHeaders, 0, len);
        return soapHeaders;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions#getHttpContentEncoding()
     * 
     */
    public String getHttpContentEncoding() 
    {
        BindingOperation bindingOp = (BindingOperation) ((NestedComponent)getParent()).getParent();
        Binding binding = (Binding) bindingOp.getParent();
        SOAPBindingExtensions soapBindExt = (SOAPBindingExtensions)binding
           .getComponentExtensionContext(SOAPConstants.NS_URI_SOAP);
        String version = soapBindExt.getSoapVersion();
        URI protocol = soapBindExt.getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
        
        if( (SOAPConstants.VERSION_1_2.equals(version) && protocol.equals(SOAPConstants.PROTOCOL_URI_SOAP12_HTTP)) ||
            (SOAPConstants.VERSION_1_1.equals(version) && protocol.equals(SOAPConstants.PROTOCOL_URI_SOAP11_HTTP)) )
        {
            StringAttr contEncoding = (StringAttr) ((WSDLElement)getParent())
               .getExtensionAttribute(HTTPConstants.Q_ATTR_CONTENT_ENCODING);
            return contEncoding != null ? contEncoding.getString() : null;
        } 
        else 
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions#getHttpHeaders()
     */
    public HTTPHeader[] getHttpHeaders() 
    {
        BindingOperation bindingOp = (BindingOperation) ((NestedComponent)getParent()).getParent();
        Binding binding = (Binding) bindingOp.getParent();
        SOAPBindingExtensions soapBindExt = (SOAPBindingExtensions)binding
           .getComponentExtensionContext(SOAPConstants.NS_URI_SOAP);
        String version = soapBindExt.getSoapVersion();
        URI protocol = soapBindExt.getSoapUnderlyingProtocol();
        if(protocol == null) {
            return new HTTPHeader[0];
        }
        
        if( (SOAPConstants.VERSION_1_2.equals(version) && protocol.equals(SOAPConstants.PROTOCOL_URI_SOAP12_HTTP)) ||
            (SOAPConstants.VERSION_1_1.equals(version) && protocol.equals(SOAPConstants.PROTOCOL_URI_SOAP11_HTTP)) )
        {
            ExtensionElement[] extEls = ((WSDLElement)getParent())
                .getExtensionElementsOfType(HTTPConstants.Q_ELEM_HTTP_HEADER);
            int len = extEls.length;
            HTTPHeader[] httpHeaders = new HTTPHeader[len];
            System.arraycopy(extEls, 0, httpHeaders, 0, len);
            return httpHeaders;
        } 
        else 
        {
            return new HTTPHeader[0];
        }
    }

}
