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
 * Represents the WSDL 2.0 &lt;infault&gt; and &lt;outfault&gt; elements, 
 * declared as child elements of the binding &lt;operation&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingFaultReferenceElement extends DocumentableElement,
                                                      NestedElement 
{
    /**
     * Specify the name of the InterfaceFaultReferenceElement referred to by this 
     * BindingFaultReferenceElement.
     * The specified QName corresponds to the <code>ref</code> attribute of the binding operation
     * &lt;infault&gt; or &lt;outfault&gt; element.
     *
     * @param faultRefName the QName of the interface fault reference.
     */
    public void setRef(QName faultRefName);
    
    /**
     * Return the name of the InterfaceFaultReferenceElement referred to by this 
     * BindingFaultReferenceElement.
     * This corresponds to the <code>ref</code> attribute of the binding operation 
     * &lt;infault&gt; or &lt;outfault&gt; element.
     * 
     * @return the QName of the interface fault reference
     */
    public QName getRef();
    
    /**
     * Return the InterfaceFaultReferenceElement referred to by this BindingFaultReferenceElement.
     * This equates to an &lt;infault&gt; or &lt;outfault&gt; element of the interface operation being
     * bound whose message label is equal to the effective message label of this binding fault
     * reference and whose associated interface fault is identified by the <code>ref</code> attribute
     * if this binding fault reference.
     * If such an element does not exist, this method will return null.
     * 
     * @return the InterfaceFaultReferenceElement
     */
    public InterfaceFaultReferenceElement getInterfaceFaultReferenceElement();
    
    /**
     * Set the message label to the specified NCName. This corresponds to a placeholder message
     * in the message exchange pattern specified by the parent binding &lt;operation&gt; element.
     * 
     * @param msgLabel the NCName representing the message label
     */
    public void setMessageLabel(NCName msgLabel);
    
    /**
     * Return the NCName representing the message label. This corresponds to a placeholder message
     * in the message exchange pattern specified by the parent binding &lt;operation&gt; element.
     * 
     * @return NCName the message label
     */
    public NCName getMessageLabel();
}
