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
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;
import org.apache.woden.wsdl20.extensions.http.HTTPAuthenticationScheme;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPEndpointExtensions;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.HTTPAuthenticationSchemeAttr;
import org.apache.woden.xml.StringAttr;

/**
 * This interface represents the properties from the HTTP namespace added to the
 * WSDL 2.0 <code>Endpoint</code> component when the binding type is SOAP and the
 * underlying protocol is HTTP.
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 * 
 */
public class SOAPEndpointExtensionsImpl extends BaseComponentExtensionContext
		implements SOAPEndpointExtensions {


    public SOAPEndpointExtensionsImpl(WSDLComponent parent, 
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
        
        return new ExtensionProperty[0]; //no SOAP extensions properties for Endpoint
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        return null; //no SOAP extension properties for Endpoint
        
    }
    
    /* ************************************************************
     *  Additional methods declared by SOAPEndpointExtensions
     * ************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.woden.wsdl20.extensions.soap.SOAPEndpointExtensions#getHttpAuthenticationScheme()
	 */
	public HTTPAuthenticationScheme getHttpAuthenticationScheme() {

        Endpoint endpoint = (Endpoint) getParent();
        Binding binding = endpoint.getBinding();
        if(binding == null) {
            return null; //the wsdl is invalid  TODO - remove this check? (extensions require valid wsdl?)
        }
        
        SOAPBindingExtensions soapBindExt = (SOAPBindingExtensions)binding
           .getComponentExtensionContext(SOAPConstants.NS_URI_SOAP);
        String version = soapBindExt.getSoapVersion();
        URI protocol = soapBindExt.getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
        
        if( ("1.2".equals(version) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP12_HTTP)) ||
            ("1.1".equals(version) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP11_HTTP)) )
        {
		    HTTPAuthenticationSchemeAttr scheme = (HTTPAuthenticationSchemeAttr) ((WSDLElement) getParent())
				.getExtensionAttribute(HTTPConstants.Q_ATTR_AUTHENTICATION_SCHEME);
		    return scheme != null ? scheme.getScheme() : null;
        }
        else
        {
            return null;
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.woden.wsdl20.extensions.soap.SOAPEndpointExtensions#getHttpAuthenticationRealm()
	 */
	public String getHttpAuthenticationRealm() {

        Endpoint endpoint = (Endpoint) getParent();
        Binding binding = endpoint.getBinding();
        if(binding == null) {
            return null; //the wsdl is invalid TODO - remove this check? (extensions require valid wsdl?)
        }
        
        SOAPBindingExtensions soapBindExt = (SOAPBindingExtensions)binding
            .getComponentExtensionContext(SOAPConstants.NS_URI_SOAP);
        String version = soapBindExt.getSoapVersion();
        URI protocol = soapBindExt.getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
     
        if( ("1.2".equals(version) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP12_HTTP)) ||
            ("1.1".equals(version) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP11_HTTP)) )
        {
		    StringAttr realm = (StringAttr) ((WSDLElement) getParent())
				.getExtensionAttribute(HTTPConstants.Q_ATTR_AUTHENTICATION_REALM);
		    return realm != null ? realm.getString() : null;
        }
        else
        {
            return null;
        }
	}

}
