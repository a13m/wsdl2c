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
package org.apache.woden.internal.wsdl20.extensions.http;

import java.net.URI;

import org.apache.woden.ErrorReporter;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;
import org.apache.woden.wsdl20.extensions.http.HTTPAuthenticationScheme;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.extensions.http.HTTPEndpointExtensions;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.HTTPAuthenticationSchemeAttr;
import org.apache.woden.xml.StringAttr;

/**
 * This class defines the properties from the HTTP namespace added to the WSDL
 * <code>Endpoint</code> component as part of the HTTP binding extension
 * defined by the WSDL 2.0 spec.
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 * 
 */
public class HTTPEndpointExtensionsImpl extends BaseComponentExtensionContext
		implements HTTPEndpointExtensions {

    public HTTPEndpointExtensionsImpl(WSDLComponent parent, 
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
                getProperty(HTTPConstants.PROP_HTTP_AUTHENTICATION_SCHEME),
                getProperty(HTTPConstants.PROP_HTTP_AUTHENTICATION_REALM)};
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        if(HTTPConstants.PROP_HTTP_AUTHENTICATION_SCHEME.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_AUTHENTICATION_SCHEME, getHttpAuthenticationScheme());
            
        } else if(HTTPConstants.PROP_HTTP_AUTHENTICATION_REALM.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_AUTHENTICATION_REALM, getHttpAuthenticationRealm());
            
        } else {
            return null; //the specified property name does not exist
        }
        
    }
    
    /* ************************************************************
     *  Additional methods declared by HTTPEndpointExtensions
     * ************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.woden.wsdl20.extensions.http.HTTPEndpointExtensions#getHttpAuthenticationScheme()
	 */
	public HTTPAuthenticationScheme getHttpAuthenticationScheme() {

		HTTPAuthenticationSchemeAttr scheme = (HTTPAuthenticationSchemeAttr) ((WSDLElement) getParent())
				.getExtensionAttribute(HTTPConstants.Q_ATTR_AUTHENTICATION_SCHEME);

		return scheme != null ? scheme.getScheme() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.woden.wsdl20.extensions.http.HTTPEndpointExtensions#getHttpAuthenticationRealm()
	 */
	public String getHttpAuthenticationRealm() {

		StringAttr realm = (StringAttr) ((WSDLElement) getParent())
				.getExtensionAttribute(HTTPConstants.Q_ATTR_AUTHENTICATION_REALM);

		return realm != null ? realm.getString() : null;
	}

}
