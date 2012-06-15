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
 * Represents the WSDL 2.0 &lt;interface&gt; element.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceElement extends DocumentableElement, 
                                          NestedElement
{
    /*
     * Attributes
     */

    /**
     * Set the name of this InterfaceElement to the specified NCName.
     * This corresponds to the <code>name</code> attribute of the &lt;interface&gt; element.
     * 
     * @param name the NCName that represents the local name of this interface
     */
    public void setName(NCName name);
    
    /**
     * Return the qualified name of this InterfaceElement, which consists of its
     * local name and the targetNamespace of the parent DescriptionElement.
     * 
     * @return the interface QName
     */
    public QName getName();
    
    /**
     * Add the named InterfaceElement to the set of InterfaceElements that this InterfaceElement
     * extends. This equates to adding a QName to the <code>extends</code> attribute
     * of the &lt;interface&gt; element.
     * If a null interface name is specified, no action is performed. 
     * 
     * @param interfaceName the QName of an extended interface
     */
    public void addExtendedInterfaceName(QName interfaceName);
    
    /**
     * Remove the named InterfaceElement from the set of InterfaceElements that this 
     * InterfaceElement extends. This equates to removing a QName from the <code>extends</code>
     * attribute of the &lt;interface&gt; element.
     * If the named InterfaceElement is not extended by this InterfaceElement or if a null 
     * interface name is specified, no action is performed. 
     * 
     * @param interfaceName the QName of an extended interface
     */
    public void removeExtendedInterfaceName(QName interfaceName);
    
    /**
     * Return the qualified names of the InterfaceElements that this InterfaceElement extends.
     * This equates to the set of QNames defined in the <code>extends</code> attribute
     * of the &lt;interface&gt; element.
     * If no extended interfaces exist, an empty array is returned.
     * 
     * @return an array of QName
     */
    public QName[] getExtendedInterfaceNames();
    
    /**
     * Return the named InterfaceElement from the set of IntefaceElements extended by this
     * InterfaceElement. The specified QName should equate to a QName defined in the 
     * <code>extends</code> attribute of the &lt;interface&gt; element. 
     * If the named InterfaceElement is not extended by this InterfaceElement or if a null 
     * interface name is specified, this method will return null. 
     * 
     * @param interfaceName the QName of an extended interface
     * @return the named InterfaceElement
     */
    public InterfaceElement getExtendedInterfaceElement(QName interfaceName);
    
    /**
     * Return the set of InterfaceElements extended by this InterfaceElement.
     * This equates to the set of QNames defined in the <code>extends</code> attribute
     * of the &lt;interface&gt; element.
     * If no extended interfaces exist, an empty array is returned.
     * 
     * @return an array of InterfaceElement
     */
    public InterfaceElement[] getExtendedInterfaceElements();
    
    /**
     * Add the specified URI to the set of default operation style URIs. 
     * This equates to adding a URI to the <code>styleDefault</code> attribute 
     * of the &lt;interface&gt; element.
     * If a null style URI is specified, no action is performed. 
     * 
     * @param style a URI representing an operation style
     */
    public void addStyleDefaultURI(URI style);
    
    //TODO public void removeStyleDefaultURI(URI uri);

    /**
     * Return the set of URIs representing the default operation style. 
     * This equates to the URIs defined in the <code>styleDefault</code> attribute 
     * of the &lt;interface&gt; element.
     * If no style default URIs exist, an empty array is returned.
     * 
     * @return an array of URI representing the default operation style
     */
    public URI[] getStyleDefault();
    
    //TODO add a remove method
    
    /*
     * Elements
     */
    
    /**
     * Create an InterfaceFaultElement with this InterfaceElement as its parent and
     * return a reference to it.
     * This equates to adding a &lt;fault&gt; element to the &lt;interface&gt; element.
     * 
     * @return the InterfaceFaultElement
     */
    public InterfaceFaultElement addInterfaceFaultElement();
    
    //TODO public void removeInterfaceFaultElement(QName faultName);
    
    /**
     * Return the named InterfaceFaultElement from the set of InterfaceFaultElements defined 
     * directly within this InterfaceElement. This equates to a named &lt;fault&gt; element
     * declared within the &lt;interface&gt; element. 
     * This set does not include faults derived from extended interfaces.
     * If the named InterfaceFaultElement does not exist or if a null value is specified,
     * this method will return null.
     * 
     * @param faultName the QName of the required InterfaceFaultElement
     * @return the named InterfaceFaultElement
     */
    public InterfaceFaultElement getInterfaceFaultElement(QName faultName);
    
    /**
     * Return the set of InterfaceFaultElements defined directly within this InterfaceElement. 
     * This equates to the set of &lt;fault&gt; elements declared within this &lt;interface&gt; 
     * element.
     * This set does not include faults derived from extended interfaces.
     * If no InterfaceFaultElements exist, an empty array is returned.
     * 
     * @return array of InterfaceFaultElement
     */
    public InterfaceFaultElement[] getInterfaceFaultElements();
    
    /**
     * Create an InterfaceOperationElement with this InterfaceElement as its parent and
     * return a reference to it.
     * This equates to adding an &lt;operation&gt; element to the &lt;interface&gt; element.
     * If no InterfaceOperationElements exist, an empty array is returned.
     * 
     * @return the InterfaceOperationElement
     */
    public InterfaceOperationElement addInterfaceOperationElement();
    
    //TODO public void removeInterfaceOperationElement(QName operName);
    
    /**
     * Return the named InterfaceOperationElement from the set of InterfaceOperationElements defined 
     * directly within this InterfaceElement. This equates to a named &lt;operation&gt; element
     * declared within the &lt;interface&gt; element. 
     * This set does not include operations derived from extended interfaces.
     * If the named InterfaceOperationElement does not exist or if a null value is specified,
     * this method will return null.
     * 
     * @param operName the QName of the required InterfaceOperationElement
     * @return the named InterfaceOperationElement
     */
    public InterfaceOperationElement getInterfaceOperationElement(QName operName);
    
    /**
     * Return the set of InterfaceOperationElements defined directly within this InterfaceElement. 
     * This equates to the set of &lt;operation&gt; elements declared within this &lt;interface&gt; 
     * element.
     * This set does not include operations derived from extended interfaces.
     * If no InterfaceOperationElements exist, an empty array is returned.
     * 
     * @return array of InterfaceOperationElement
     */
    public InterfaceOperationElement[] getInterfaceOperationElements();
    
}
