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

/**
 * Represents the WSDL 2.0 &lt;operation&gt; element, declared as a child 
 * of the &lt;binding&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingOperationElement extends DocumentableElement,
                                                 NestedElement 
{
    /*
     * Attributes
     */
    
    /**
     * Specify the name of the InterfaceOperationElement referred to by this BindingOperationElement.
     * The specified QName corresponds to the <code>ref</code> attribute of the binding 
     * &lt;operation&gt; element.
     *
     * @param operName the QName of the interface operation.
     */
    public void setRef(QName operName);
    
    /**
     * Return the name of the InterfaceOperationElement referred to by this BindingOperationElement.
     * This corresponds to the <code>ref</code> attribute of the binding &lt;operation&gt; element.
     * 
     * @return the QName of the interface operation
     */
    public QName getRef();

    /**
     * Return the InterfaceOperationElement referred to by this BindingOperationElement.
     * This equates to the interface &lt;operation&gt; element referred to by the 
     * <code>ref</code> attribute of the binding &lt;operation&gt; element.
     * If this reference cannot be resolved to an InterfaceOperationElement, this method will
     * return null.
     * 
     * @return the InterfaceOperationElement
     */
    public InterfaceOperationElement getInterfaceOperationElement();
    
    /*
     * Elements
     */
    
    /**
     * Create a BindingMessageReferenceElement with this BindingOperationElement as its parent
     * and return a reference to it.
     * This equates to adding an &lt;input&gt; or &lt;output&gt; element
     * to the binding &lt;operation&gt; element.
     * 
     * @return the BindingMessageReferenceElement
     */
    public BindingMessageReferenceElement addBindingMessageReferenceElement();
    
    /**
     * Remove the specified BindingMessageReferenceElement from the set of 
     * BindingMessageReferenceElements within this BindingOperationElement.
     * This equates to removing an &lt;input&gt; or &lt;output&gt; element
     * from the binding &lt;operation&gt; element.
     * If the specified BindingMessageReferenceElement does not exist or if a 
     * null value is specified, no action is performed.
     * 
     * @param msgRef the BindingMessageReferenceElement to be removed
     */
    public void removeBindingMessageReferenceElement(BindingMessageReferenceElement msgRef);
    
    /**
     * Return the set of BindingMessageReferenceElements within this BindingOperationElement.
     * This equates to the set of &lt;input&gt; and &lt;output&gt; elements
     * within the binding &lt;operation&gt; element.
     * If no BindingMessageReferenceElements exist, an empty array is returned. 
     * 
     * @return an array of BindingMessageReferenceElement
     */
    public BindingMessageReferenceElement[] getBindingMessageReferenceElements();

    /**
     * Create a BindingFaultReferenceElement with this BindingOperationElement as its parent
     * and return a reference to it.
     * This equates to adding an &lt;infault&gt; or &lt;outfault&gt; element
     * to the binding &lt;operation&gt; element.
     * 
     * @return the BindingFaultReferenceElement
     */
    public BindingFaultReferenceElement addBindingFaultReferenceElement();
    
    /**
     * Remove the specified BindingFaultReferenceElement from the set of 
     * BindingFaultReferenceElements within this BindingOperationElement.
     * This equates to removing an &lt;infault&gt; or &lt;outfault&gt; element
     * from the binding &lt;operation&gt; element.
     * If the specified BindingFaultReferenceElement does not exist or if a 
     * null value is specified, no action is performed.
     * 
     * @param faultRef the BindingFaultReferenceElement to be removed
     */
    public void removeBindingFaultReferenceElement(BindingFaultReferenceElement faultRef);
    
    /**
     * Return the set of BindingFaultReferenceElements within this BindingOperationElement.
     * This equates to the set of &lt;infault&gt; and &lt;outfault&gt; elements
     * within the binding &lt;operation&gt; element.
     * If no BindingFaultReferenceElements exist, an empty array is returned. 
     * 
     * @return an array of BindingFaultReferenceElement
     */
    public BindingFaultReferenceElement[] getBindingFaultReferenceElements();
    
}
