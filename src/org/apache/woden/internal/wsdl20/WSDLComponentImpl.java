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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.BaseComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.ExtensionProperty;

/**
 * All classes implementing the WSDL 2.0 Component and Element
 * model interfaces directly or indirectly extend this 
 * abstract class. It implements the WSDL20Component interface
 * which just provides a common reference for objects from the WSDL 2.0 
 * Component API. This class also inherits common behaviour for 
 * WSDL Elements from WSDLElementImpl, which in turn provides a common
 * reference for objects from the the WSDL 2.0 Element API. 
 * 
 * @author jkaputin@apache.org
 */
public abstract class WSDLComponentImpl extends DocumentableImpl
                                     implements WSDLComponent
{
    private Map fCompExtensionContexts = new HashMap(); //key=extNS, value=ComponentExtensionsContext
    
    /* ************************************************************
     *  WSDLComponent interface methods (i.e. WSDL Component API)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#isEquivalentTo(WSDLComponent)
     * 
     * TODO implement this method in all concrete component classes and make this
     * implementation abstract or throw UnsupportedExc.
     */
    public boolean isEquivalentTo(WSDLComponent comp)
    {
        return super.equals(comp);
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#equals(WSDLComponent)
     * @deprecated Use isEquivalentTo(WSDLComponent)

     * TODO - deprecated. Remove. Replaced by isEquivalentTo.
     * 
     * TODO implement this method in all concrete component classes and make this
     * implementation abstract or throw UnsupportedExc.
     */
    public boolean equals(WSDLComponent comp)
    {
        return super.equals(comp);
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#setComponentExtensionContext(java.net.URI, org.apache.woden.wsdl20.extensions.ComponentExtensionsContext)
     */
    public void setComponentExtensionContext(URI extNamespace, ComponentExtensionContext compExtCtx) {
        
        if(extNamespace == null) {
            String msg = getWsdlContext().errorReporter.getFormattedMessage("WSDL023", null);
            throw new NullPointerException(msg);
        }
        
        if(compExtCtx != null) {
            fCompExtensionContexts.put(extNamespace.toString(), compExtCtx);
        } else {
            fCompExtensionContexts.remove(extNamespace.toString());
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.WSDLComponent#getComponentExtensionContext(java.net.URI)
     */
    public ComponentExtensionContext getComponentExtensionContext(URI extNamespace) {
        if(extNamespace == null) {
            return null;
        }
        return (ComponentExtensionContext) fCompExtensionContexts.get(extNamespace.toString());
    }
        
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.PropertyExtensible#getExtensionProperties()
     */
    public ExtensionProperty[] getExtensionProperties() {
        int i, len;
        List properties = new Vector();
        Collection compExtCtxs = fCompExtensionContexts.values();
        Iterator it = compExtCtxs.iterator();
        while(it.hasNext()) {
            BaseComponentExtensionContext compExtCtx = (BaseComponentExtensionContext)it.next();
            ExtensionProperty[] extProps = compExtCtx.getProperties();
            len = extProps.length;
            for(i=0; i<len; i++) {
                properties.add(extProps[i]);
            }
        }
        
        ExtensionProperty[] array = new ExtensionProperty[properties.size()];
        properties.toArray(array);
        return array;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.PropertyExtensible#getExtensionProperties(java.net.URI)
     */
    public ExtensionProperty[] getExtensionProperties(URI extNamespace) {
        if(extNamespace == null) {
            return new ExtensionProperty[] {};
        }
        
        ComponentExtensionContext compExtCtx = getComponentExtensionContext(extNamespace);
        if(compExtCtx == null) {
            return new ExtensionProperty[] {};
        }
        
        return compExtCtx.getProperties();
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.PropertyExtensible#getExtensionProperty(java.net.URI, java.lang.String)
     */
    public ExtensionProperty getExtensionProperty(URI extNamespace, String propertyName) {
        if(extNamespace == null || propertyName == null) {
            return null;
        }
        
        ComponentExtensionContext compExtCtx = getComponentExtensionContext(extNamespace);
        if(compExtCtx == null) {
            return null;
        }
        
        return compExtCtx.getProperty(propertyName);
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/

    /* 
     * Check if a component already exists in a list of those components. Used when 
     * retrieving sets of components to de-duplicate logically equivalent components.
     */
    protected boolean containsComponent(WSDLComponent comp, List components)
    {
        for(Iterator i=components.iterator(); i.hasNext(); )
        {
            WSDLComponent tempComp = (WSDLComponent)i.next();
            if(tempComp.isEquivalentTo(comp)) {
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        return getFragmentIdentifier().toString();
    }
    
    
}
