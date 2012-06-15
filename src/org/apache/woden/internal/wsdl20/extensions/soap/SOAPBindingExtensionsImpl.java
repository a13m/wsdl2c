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
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPModule;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.BooleanAttr;
import org.apache.woden.xml.StringAttr;
import org.apache.woden.xml.URIAttr;

/**
 * This class defines the properties from the SOAP namespace
 * added to the WSDL <code>Binding</code> component as part 
 * of the SOAP binding extension defined by the WSDL 2.0 spec. 
 * 
 * @author jkaputin@apache.org
 */
public class SOAPBindingExtensionsImpl extends BaseComponentExtensionContext
                                       implements SOAPBindingExtensions 
{

    public SOAPBindingExtensionsImpl(WSDLComponent parent, 
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
                getProperty(SOAPConstants.PROP_SOAP_VERSION),
                getProperty(SOAPConstants.PROP_SOAP_UNDERLYING_PROTOCOL),
                getProperty(SOAPConstants.PROP_SOAP_MEP_DEFAULT),
                getProperty(SOAPConstants.PROP_SOAP_MODULES)};
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        if(SOAPConstants.PROP_SOAP_VERSION.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_VERSION, getSoapVersion());
            
        } else if(SOAPConstants.PROP_SOAP_UNDERLYING_PROTOCOL.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_UNDERLYING_PROTOCOL, 
                    getSoapUnderlyingProtocol());
            
        } else if(SOAPConstants.PROP_SOAP_MEP_DEFAULT.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_MEP_DEFAULT, getSoapMepDefault());
            
        } else if(SOAPConstants.PROP_SOAP_MODULES.equals(propertyName)) {
            return newExtensionProperty(SOAPConstants.PROP_SOAP_MODULES, getSoapModules());
            
        } else {
            return null; //the specified property name does not exist
        }
        
    }
    
    /* ************************************************************
     *  Additional methods declared by SOAPBindingExtensions
     * ************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getSoapVersion()
     */
    public String getSoapVersion() 
    {
        StringAttr version = (StringAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(SOAPConstants.Q_ATTR_SOAP_VERSION);
        return version != null ? version.getString() : "1.2";
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getSoapUnderlyingProtocol()
     */
    public URI getSoapUnderlyingProtocol() 
    {
        URIAttr protocol = (URIAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(SOAPConstants.Q_ATTR_SOAP_PROTOCOL);
        return protocol != null ? protocol.getURI() : null;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getSoapMepDefault()
     */
    public URI getSoapMepDefault() 
    {
        URIAttr mepDefault = (URIAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(SOAPConstants.Q_ATTR_SOAP_MEPDEFAULT);
        return mepDefault != null ? mepDefault.getURI() : null;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getSoapModules()
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
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getHttpQueryParameterSeparatorDefault()
     */
    public String getHttpQueryParameterSeparatorDefault() 
    {
        URI protocol = getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
        
        if( ("1.2".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP12_HTTP)) ||
            ("1.1".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP11_HTTP)) )
        {
            StringAttr qpsDef = (StringAttr) ((WSDLElement)getParent())
                .getExtensionAttribute(HTTPConstants.Q_ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT);
            return qpsDef != null ? qpsDef.getString() : HTTPConstants.QUERY_SEP_AMPERSAND;
        } 
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#isHttpCookies()
     */
    public Boolean isHttpCookies() 
    {
        URI protocol = getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
        
        if( ("1.2".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP12_HTTP)) ||
            ("1.1".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP11_HTTP)) )
        {
            BooleanAttr cookiesUsed = (BooleanAttr) ((WSDLElement)getParent())
                .getExtensionAttribute(HTTPConstants.Q_ATTR_COOKIES);
            return cookiesUsed != null ? cookiesUsed.getBoolean() : new Boolean(false); //defaults to false if omitted
        } 
        else 
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions#getHttpContentEncodingDefault()
     */
    public String getHttpContentEncodingDefault() 
    {
        URI protocol = getSoapUnderlyingProtocol();
        if(protocol == null) {
            return null;
        }
        
        if( ("1.2".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP12_HTTP)) ||
            ("1.1".equals(getSoapVersion()) && protocol.toString().equals(SOAPConstants.PROTOCOL_STRING_SOAP11_HTTP)) )
        {
            StringAttr ceDef = (StringAttr) ((WSDLElement)getParent())
                .getExtensionAttribute(HTTPConstants.Q_ATTR_CONTENT_ENCODING_DEFAULT);
            return ceDef != null ? ceDef.getString() : null;
        } 
        else
        {
            return null;
        }
    }

}
