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
package org.apache.woden.wsdl20.xml;

import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;

/**
 * Represents the WSDL 2.0 &lt;binding&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingElement extends DocumentableElement,
                                        NestedElement
{
    /*
     * Attributes
     */

    /**
     * Set the name of this BindingElement to the specified NCName. 
     * This corresponds to the <code>name</code> attribute of the &lt;binding&gt; element.
     * 
     * @param name the NCName that represents this binding.
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this BindingElement, which consists of its
     * local name and the targetNamespace of the parent DescriptionElement.
     * 
     * @return the binding QName
     */
    public QName getName();
    
    /**
     * Specify the name of the InterfaceElement referred to by this BindingElement.
     * This corresponds to the <code>interface</code> attribute of the &lt;binding&gt; element.
     * 
     * @param interfaceName the QName of the interface
     */
    public void setInterfaceName(QName interfaceName);
    
    /**
     * Return the name of the InterfaceElement referred to by this BindingElement.
     * This corresponds to the <code>interface</code> attribute of the &lt;binding&gt; element.
     * 
     * @return the interface QName
     */
    public QName getInterfaceName();
    
    /**
     * Return the InterfaceElement referred to by this BindingElement. 
     * This equates to the &lt;interface&gt; element referred to by the <code>interface</code> 
     * attribute of the &lt;binding&gt; element.
     * If this reference cannot be resolved to an InterfaceElement or if this BindingElement
     * is a generic (interface-less) binding, this method will return null.
     * 
     * @return the InterfaceElement
     */
    public InterfaceElement getInterfaceElement();
    
    /**
     * Set the binding type to the specified URI. 
     * This identifies the type of WSDL extensions used with this binding.
     * This corresponds to the <code>type</code> attribute of the &lt;binding&gt; element.
     * 
     * @param type the URI indicating the binding type
     */
    public void setType(URI type);
    
    /**
     * Return the URI that identifies the binding type.
     * This corresponds to the <code>type</code> attribute of the &lt;binding&gt; element.
     * 
     * @return the binding type URI
     */
    public URI getType();
    
    /*
     * Elements
     */
    
    /**
     * Create a BindingFaultElement with this BindingElement as its parent and
     * return a reference to it.
     * This equates to adding a &lt;fault&gt; element to the &lt;binding&gt; element.
     * 
     * @return the BindingFaultElement
     */
    public BindingFaultElement addBindingFaultElement();
    
    /**
     * Return the set of BindingFaultElements within this BindingElement.
     * This equates to the set of &lt;fault&gt; elements within the &lt;binding&gt; element.
     * If no BindingFaultElements exist, an empty array is returned.
     * 
     * @return an array of BindingFaultElement
     */
    public BindingFaultElement[] getBindingFaultElements();
    
    //TODO removeBindingFaultElement method
    
    /**
     * Create a BindingOperationElement with this BindingElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;operation&gt; element to the &lt;binding&gt; element.
     * 
     * @return the BindingOperationElement
     */
    public BindingOperationElement addBindingOperationElement();
    
    /**
     * Return the set of BindingOperationElements within this BindingElement
     * This equates to the set of &lt;operation&gt; elements within the &lt;binding&gt; element.
     * If no BindingOperationElements exist, an empty array is returned.
     * 
     * @return an array of BindingOperationElement
     */
    public BindingOperationElement[] getBindingOperationElements();
    
    //TODO removeBindingOperationElement method
    
}
