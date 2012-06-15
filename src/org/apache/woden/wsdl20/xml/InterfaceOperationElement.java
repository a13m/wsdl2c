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
 * Represents the WSDL 2.0 &lt;operation&gt; element, declared as a child  
 * of the &lt;interface&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceOperationElement extends DocumentableElement,
                                                   NestedElement
{
    /*
     * Attributes
     */
    
    /**
     * Set the name of this InterfaceOperationElement to the specified NCName.
     * This corresponds to the <code>name</code> attribute of the interface &lt;operation&gt; element.
     * 
     * @param name the NCName that represents the local name of this interface operation
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this InterfaceOperationElement, which consists of its
     * local name and the targetNamespace of the enclosing DescriptionElement.
     * 
     * @return the interface operation QName
     */
    public QName getName();
    
    /**
     * Set the message exchange pattern used by this InterfaceOperationElement to the specified
     * URI.
     * This corresponds to the <code>pattern</code> attribute of the interface &lt;operation&gt; 
     * element.
     * 
     * @param mep the message exchange pattern URI
     */
    public void setPattern(URI mep);
    
    /**
     * Return the URI representing the message exchange pattern used by this InterfaceOperationElement.
     * This corresponds to the <code>name</code> attribute of the interface &lt;operation&gt; element.
     * 
     * @return the URI representing the message exchange pattern
     */
    public URI getPattern();
    
    /**
     * Add the specified URI to the set of style URIs used by this InterfaceOperationElement. 
     * This equates to adding a URI to the <code>style</code> attribute of the interface 
     * &lt;operation&gt; element.
     * If a null style URI is specified, no action is performed. 
     * 
     * @param style a URI representing an operation style
     */
    public void addStyleURI(URI style);
    
    
    /**
     * Remove the specified URI from the set of style URIs used by this InterfaceOperationElement. 
     * This equates to removing a URI from the <code>style</code> attribute of the interface 
     * &lt;operation&gt; element.
     * If the specified URI is not present in the set of style URIs or if a null URI is specified,
     * no action is performed.
     * 
     * @param style a URI representing an operation style
     */
    public void removeStyleURI(URI style);
    
    /**
     * Return the set of URIs representing the operation style. 
     * This corresponds to the URIs defined in the <code>style</code> attribute of the interface 
     * &lt;operation&gt; element.
     * If no style URIs exist, an empty array is returned.
     * 
     * @return an array of URI representing the operation style
     */
    public URI[] getStyle();
    
    /*
     * Elements
     */
    
    /**
     * Create an InterfaceMessageReferenceElement with this InterfaceOperationElement as its parent
     * and return a reference to it.
     * This equates to adding an &lt;input&gt; or &lt;output&gt; element to the interface 
     * &lt;operation&gt; element.
     * 
     * @return the InterfaceMessageReferenceElement
     */
    public InterfaceMessageReferenceElement addInterfaceMessageReferenceElement();
    
    /**
     * Remove the specified InterfaceMessageReferenceElement from the set of 
     * InterfaceMessageReferenceElements within this InterfaceOperationElement.
     * This equates to removing an &lt;input&gt; or &lt;output&gt; element
     * from the interface &lt;operation&gt; element.
     * If the specified InterfaceMessageReferenceElement does not exist or if a 
     * null value is specified, no action is performed.
     * 
     * @param msgRef the InterfaceMessageReferenceElement to be removed
     */
    public void removeInterfaceMessageReferenceElement(InterfaceMessageReferenceElement msgRef);
    
    /**
     * Return the set of InterfaceMessageReferenceElements within this InterfaceOperationElement.
     * This equates to the set of &lt;input&gt; and &lt;output&gt; elements
     * within the interface &lt;operation&gt; element.
     * If no InterfaceMessageReferenceElements exist, an empty array is returned. 
     * 
     * @return an array of InterfaceMessageReferenceElement
     */
    public InterfaceMessageReferenceElement[] getInterfaceMessageReferenceElements();

    /**
     * Create an InterfaceFaultReferenceElement with this InterfaceOperationElement as its parent
     * and return a reference to it.
     * This equates to adding an &lt;infault&gt; or &lt;outfault&gt; element to the interface 
     * &lt;operation&gt; element.
     * 
     * @return the InterfaceFaultReferenceElement
     */
    public InterfaceFaultReferenceElement addInterfaceFaultReferenceElement();
    
    /**
     * Remove the specified InterfaceFaultReferenceElement from the set of 
     * InterfaceFaultReferenceElements within this InterfaceOperationElement.
     * This equates to removing an &lt;infault&gt; or &lt;outfault&gt; element
     * from the interface &lt;operation&gt; element.
     * If the specified InterfaceFaultReferenceElement does not exist or if a 
     * null value is specified, no action is performed.
     * 
     * @param faultRef the InterfaceFaultReferenceElement to be removed
     */
    public void removeInterfaceFaultReferenceElement(InterfaceFaultReferenceElement faultRef);
    
    /**
     * Return the set of InterfaceFaultReferenceElement within this InterfaceOperationElement.
     * This equates to the set of &lt;infault&gt; and &lt;outfault&gt; elements
     * within the interface &lt;operation&gt; element.
     * If no InterfaceFaultReferenceElements exist, an empty array is returned. 
     * 
     * @return an array of InterfaceFaultReferenceElement
     */
    public InterfaceFaultReferenceElement[] getInterfaceFaultReferenceElements();

}
