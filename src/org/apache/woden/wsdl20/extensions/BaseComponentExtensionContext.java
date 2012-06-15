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
package org.apache.woden.wsdl20.extensions;

import java.net.URI;

import org.apache.woden.ErrorReporter;
import org.apache.woden.wsdl20.WSDLComponent;

/**
 * This abstract class partially implements the ComponentExtensionContext interface.
 * It implements common behaviour, leaving the extension-specific methods abstract. 
 * That is, it leaves the <code>getProperties</code> and <code>getProperty</code> methods 
 * abstract, for subclasses to implement.
 * <p>
 * Implementors of WSDL 2.0 extensions may extend this class to reuse common behaviour 
 * for accessing the parent component and the extension namespace. 
 * It provides a constructor which subclasses should call that stores the parent component 
 * and extension namespace.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public abstract class BaseComponentExtensionContext implements ComponentExtensionContext {
    
    private WSDLComponent parent;
    private URI extNamespace;
    protected ErrorReporter errorReporter;

    /**
     * Constructor accepts the parent component, the extension namespace and an error reporter.
     * These parameters cannot be null. 
     * <p>
     * Normal behaviour is for these 3 parameters to be provided by the ExtensionRegistry when 
     * it calls this constructor. 
     * Woden client code should not need to call this constructor directly. 
     * However, this assumes that extension properties from the required namespace have been 
     * registered in the ExtensionRegistry. 
     * This is done automatically by Woden for the extensions defined by the 
     * WSDL 2.0 Recommendation (SOAP, HTTP, RPC, WSDLX), but implementors of other WSDL 2.0 
     * extensions must ensure they register their extension properties so that this constructor 
     * gets called by the ExtensionRegistry.
     * 
     * @param parent WSDLComponent containing these extension properties
     * @param extNamespace extension namespace URI
     * @param errorReporter ErrorReporter available to subclasses for reporting errors
     * 
     * @throws NullPointerException if any of the parameters are null
     */
    protected BaseComponentExtensionContext(WSDLComponent parent, 
            URI extNamespace, ErrorReporter errorReporter) {
        
        if(errorReporter == null) {
            throw new NullPointerException("ErrorReporter=null");
        }
        if(parent == null) {
            throw new NullPointerException(errorReporter.getFormattedMessage("WSDL025", null));
        }
        if(extNamespace == null) {
            throw new NullPointerException(errorReporter.getFormattedMessage("WSDL023", null));
        }
        
        this.parent = parent;
        this.extNamespace = extNamespace;
        this.errorReporter = errorReporter;
    }
    
    /* ************************************************************
     *  Methods declared by ComponentExtensionContext
     * ************************************************************/

    /**
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getParent()
     */
    public WSDLComponent getParent() {
        return this.parent;
    }
    
    /**
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getNamespace()
     */
    public URI getNamespace() {
        return this.extNamespace;
    }

    /**
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperties()
     */
    abstract public ExtensionProperty[] getProperties();
    
    /**
     * @see org.apache.woden.wsdl20.extensions.ComponentExtensionContext#getProperty(java.lang.String)
     */
    abstract public ExtensionProperty getProperty(String propertyName);

    /* ************************************************************
     *  Helper methods
     * ************************************************************/

    /**
     * A factory-type method for instantiating and initialising ExtensionProperty objects. 
     * This is a helper method for subclasses, as it automatically provides the extension namespace. 
     * The only additional information the caller must provide is the property name and its content.
     * The property name parameter must not be null.
     * 
     * @throws NullPointerException if the <code>name</code> parameter is null.
     */
    protected ExtensionProperty newExtensionProperty(String name, Object content) {
            return new GenericExtensionProperty(name, getNamespace(), content);
    }
}
