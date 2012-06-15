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

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;

/**
 * Represents the WSDL 2.0 &lt;service&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface ServiceElement extends DocumentableElement,
                                        NestedElement
{
    /**
     * Set the name of this ServiceElement to the specified NCName.
     * This corresponds to the <code>name</code> attribute of the &lt;service&gt; element.
     * 
     * @param name the NCName that represents the local name of this service
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this ServiceElement, which consists of its
     * local name and the targetNamespace of the enclosing DescriptionElement.
     * 
     * @return the service QName
     */
    public QName getName();
    
    /**
     * Specify the name of the InterfaceElement referred to by this ServiceElement.
     * The specified QName corresponds to the <code>interface</code> attribute of the 
     * &lt;service&gt; element.
     *
     * @param interfaceName the QName of the interface
     */
    public void setInterfaceName(QName interfaceName);
    
    /**
     * Return the name of the InterfaceElement referred to by this ServiceElement.
     * This corresponds to the <code>interface</code> attribute of the 
     * &lt;service&gt; element.
     *
     * @return the QName of the interface
     */
    public QName getInterfaceName();
    
    /**
     * Return the InterfaceElement referred to by this ServiceElement. 
     * This equates to the &lt;interface&gt; element referred to by the <code>interface</code> 
     * attribute of the &lt;service&gt; element.
     * If this reference cannot be resolved to an InterfaceElement, this method will return null.
     * 
     * @return the InterfaceElement
     */
    public InterfaceElement getInterfaceElement();
    
    /**
     * Create an EndpointElement with this ServiceElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;endpoint&gt; element to the &lt;service&gt; element.
     * 
     * @return the EndpointElement
     */
    public EndpointElement addEndpointElement();
    
    /**
     * Return the set of EndpointElements within this ServiceElement.
     * This equates to the set of &lt;endpoint&gt; elements within the &lt;service&gt; element.
     * If no EndpointElements exist, an empty array is returned.
     * 
     * @return an array of EndpointElement
     */
    public EndpointElement[] getEndpointElements();
}
