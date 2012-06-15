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
import org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.BooleanAttr;
import org.apache.woden.xml.StringAttr;

/**
 * This class defines the properties from the HTTP namespace
 * added to the WSDL <code>Binding</code> component as part 
 * of the HTTP binding extension defined by the WSDL 2.0 spec. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class HTTPBindingExtensionsImpl extends BaseComponentExtensionContext
                                       implements HTTPBindingExtensions 
{

    public HTTPBindingExtensionsImpl(WSDLComponent parent, 
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
                getProperty(HTTPConstants.PROP_HTTP_METHOD_DEFAULT),
                getProperty(HTTPConstants.PROP_HTTP_QUERY_PARAMETER_SEPARATOR_DEFAULT),
                getProperty(HTTPConstants.PROP_HTTP_COOKIES),
                getProperty(HTTPConstants.PROP_HTTP_CONTENT_ENCODING_DEFAULT)};
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        if(HTTPConstants.PROP_HTTP_METHOD_DEFAULT.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_METHOD_DEFAULT, getHttpMethodDefault());
            
        } else if(HTTPConstants.PROP_HTTP_QUERY_PARAMETER_SEPARATOR_DEFAULT.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_QUERY_PARAMETER_SEPARATOR_DEFAULT, 
                    getHttpQueryParameterSeparatorDefault());
            
        } else if(HTTPConstants.PROP_HTTP_COOKIES.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_COOKIES, isHttpCookies());
            
        } else if(HTTPConstants.PROP_HTTP_CONTENT_ENCODING_DEFAULT.equals(propertyName)) {
            return newExtensionProperty(HTTPConstants.PROP_HTTP_CONTENT_ENCODING_DEFAULT, getHttpContentEncodingDefault());
            
        } else {
            return null; //the specified property name does not exist
        }
        
    }
    
    /* ************************************************************
     *  Additional methods declared by HTTPBindingExtensions
     * ************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions#getHttpMethodDefault()
     */
    public String getHttpMethodDefault() 
    {
        StringAttr methodDef = (StringAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(HTTPConstants.Q_ATTR_METHOD_DEFAULT);
        return methodDef != null ? methodDef.getString() : null;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions#getHttpQueryParameterSeparatorDefault()
     */
    public String getHttpQueryParameterSeparatorDefault() 
    {
        //TODO monitor ws-desc proposal 19May06 on changing handling of defaults in spec Part 2
        StringAttr qpsDef = (StringAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(HTTPConstants.Q_ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT);
        return qpsDef != null ? qpsDef.getString() : HTTPConstants.QUERY_SEP_AMPERSAND;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions#isHttpCookies()
     */
    public Boolean isHttpCookies() 
    {
        BooleanAttr cookiesUsed = (BooleanAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(HTTPConstants.Q_ATTR_COOKIES);
        return cookiesUsed != null ? cookiesUsed.getBoolean() : new Boolean(false); //defaults to false if omitted
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions#getHttpContentEncodingDefault()
     */
    public String getHttpContentEncodingDefault() 
    {
        StringAttr ceDef = (StringAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(HTTPConstants.Q_ATTR_CONTENT_ENCODING_DEFAULT);
        return ceDef != null ? ceDef.getString() : null;
    }

}
