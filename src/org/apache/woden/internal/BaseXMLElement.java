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
package org.apache.woden.internal;

import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;

/**
 * This abstract class implements methods of the XMLElement interface that are
 * common across all concrete implementations.
 * 
 */
public abstract class BaseXMLElement implements XMLElement {

    protected Object fSource = null;
    protected ErrorReporter fErrorReporter = null;

    protected BaseXMLElement(ErrorReporter errorReporter) {
        fErrorReporter = errorReporter;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#setSource(java.lang.Object)
     */
    abstract public void setSource(Object elem);

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getSource()
     */
    public final Object getSource() {
        return fSource;
    }
    
    //TODO refactor getAttributes() here
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getAttribute(java.lang.String)
     */
    public final String getAttributeValue(String attrName) {
    	
    	if(fSource != null) {
    		return doGetAttributeValue(attrName);
    	} else { 
    		return null;
    	}
    	
    }
    
    protected abstract String doGetAttributeValue(String attrName);

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getNamespaceURI()
     */
    public final URI getNamespaceURI() throws WSDLException {
    	
    	if(fSource != null) {
    		return doGetNamespaceURI();
    	} else {
    		return null;
    	}
    }
    
    protected abstract URI doGetNamespaceURI() throws WSDLException;

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getLocalName()
     */
    public final String getLocalName() {
    	
    	if(fSource != null) {
    		return doGetLocalName();
    	} else {
    		return null;
    	}
    }
    
    protected abstract String doGetLocalName();

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getQName()
     */
    public final QName getQName() {
    	
    	if(fSource != null) {
    		return doGetQName();
    	} else {
    		return null;
    	}
    }
    
    protected abstract QName doGetQName();
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getQName(java.lang.String)
     */
    public final QName getQName(String prefixedValue) throws WSDLException {
    	
    	if(fSource != null) {
    		return doGetQName(prefixedValue);
    	} else {
    		return null;
    	}
    }

    protected abstract QName doGetQName(String prefixedValue) throws WSDLException;
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getFirstChildElement()
     */
    public final XMLElement getFirstChildElement() {
    	
    	if(fSource != null) {
    		return doGetFirstChildElement();
    	} else {
    		return null;
    	}
    }
    
    protected abstract XMLElement doGetFirstChildElement();
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getNextSiblingElement()
     */
    public final XMLElement getNextSiblingElement() {
    	
    	if(fSource != null) {
    		return doGetNextSiblingElement();
    	} else {
    		return null;
    	}
    }
    
    protected abstract XMLElement doGetNextSiblingElement();

    /*
     * (non-Javadoc)
     * @see org.apache.woden.XMLElement#getChildElements()
     */
    public final XMLElement[] getChildElements() {
        
        if(fSource != null) {
            return doGetChildElements();
        } else {
            return null;
        }
    }
    
    protected abstract XMLElement[] doGetChildElements();
    
}
