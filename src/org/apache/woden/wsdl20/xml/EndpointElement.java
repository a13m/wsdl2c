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
 * Represents the WSDL 2.0 &lt;endpoint&gt; element, 
 * declared as a child of the &lt;service&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface EndpointElement extends DocumentableElement,
                                         NestedElement 
{
    /**
     * Set the name of this EndpointElement to the specified NCName.
     * This corresponds to the <code>name</code> attribute of the &lt;endpoint&gt; element.
     * 
     * @param name the NCName that represents the local name of this endpoint
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this EndpointElement, which consists of its
     * local name and the targetNamespace of the enclosing DescriptionElement.
     * 
     * @return the endpoint QName
     */
    public NCName getName();
    
    /**
     * Specify the name of the BindingElement referred to by this EndpointElement.
     * The specified QName corresponds to the <code>binding</code> attribute of the 
     * &lt;endpoint&gt; element.
     *
     * @param bindingName the QName of the binding
     */
    public void setBindingName(QName bindingName);
    
    /**
     * Return the name of the BindingElement referred to by this EndpointElement.
     * This corresponds to the <code>binding</code> attribute of the 
     * &lt;endpoint&gt; element.
     *
     * @return the QName of the binding
     */
    public QName getBindingName();
    
    /**
     * Return the BindingElement referred to by this EndpointElement. 
     * This equates to the &lt;binding&gt; element referred to by the <code>binding</code> 
     * attribute of the &lt;endpoint&gt; element.
     * If this reference cannot be resolved to a BindingElement, this method will return null.
     * 
     * @return the BindingElement
     */
    public BindingElement getBindingElement();
    
    /**
     * Set the endpoint address of this EndpointElement to the specified URI.
     * This corresponds to the <code>address</code> attribute of the &lt;endpoint&gt; element.
     * 
     * @param address the endpoint address URI
     */
    public void setAddress(URI address);
    
    /**
     * Return the URI representing the endpoint address of this EndpointElement.
     * This corresponds to the <code>address</code> attribute of the &lt;endpoint&gt; element.
     * 
     * @return the endpoint address URI
     */
    public URI getAddress();
}
