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
package org.apache.woden.internal.wsdl20.extensions;

import java.net.URI;

import org.apache.woden.ErrorReporter;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.WSDLExtensionConstants;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;
import org.apache.woden.wsdl20.extensions.InterfaceOperationExtensions;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.BooleanAttr;

/**
 * This class defines the properties from the WSDL extensions namespace added to
 * the WSDL <code>Interface Operation</code> component as part of the WSDL
 * extension defined by the WSDL 2.0 spec.
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com)
 */

public class InterfaceOperationExtensionsImpl extends BaseComponentExtensionContext
		implements InterfaceOperationExtensions {

    public InterfaceOperationExtensionsImpl(WSDLComponent parent, 
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
        return new ExtensionProperty[] {getProperty(WSDLExtensionConstants.PROP_SAFE)};
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    public ExtensionProperty getProperty(String propertyName) {
        
        if(WSDLExtensionConstants.PROP_SAFE.equals(propertyName) ) {
            BooleanAttr safe = (BooleanAttr) ((WSDLElement)getParent())
            .getExtensionAttribute(WSDLExtensionConstants.Q_ATTR_SAFE);
            return newExtensionProperty(WSDLExtensionConstants.PROP_SAFE, 
                    safe != null ? safe.getBoolean() : Boolean.FALSE);
        } else {
            return null; //the specified property name does not exist
        }
    }
    
    /* ************************************************************
     *  Additional methods declared by InterfaceOperationExtensions
     * ************************************************************/

    /*
     * (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.InterfaceOperationExtensionContext#isSafe()
     */
	public boolean isSafe() {
		Boolean safe = (Boolean)getProperty(WSDLExtensionConstants.PROP_SAFE).getContent();
		return safe == Boolean.TRUE ? true : false;
	}
}
