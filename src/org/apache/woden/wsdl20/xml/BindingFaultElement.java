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
 * Represents the WSDL 2.0 &lt;fault&gt; element, declared as a child
 * of the &lt;binding&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface BindingFaultElement extends DocumentableElement,
                                             NestedElement
{
    /**
     * Specify the name of the InterfaceFaultElement referred to by this BindingFaultElement.
     * The specified QName corresponds to the <code>ref</code> attribute of the binding 
     * &lt;fault&gt; element.
     *
     * @param faultName the QName of the interface fault
     */
    public void setRef(QName faultName);
    
    /**
     * Return the name of the InterfaceFaultElement referred to by this BindingFaultElement.
     * This corresponds to the <code>ref</code> attribute of the binding &lt;fault&gt; element.
     * 
     * @return the QName of the interface fault
     */
    public QName getRef();
    
    /**
     * Return the InterfaceFaultElement referred to by this BindingFaultElement.
     * This equates to the interface &lt;fault&gt; element referred to by the
     * <code>ref</code> attribute of the binding &lt;fault&gt; element.
     * If this reference cannot be resolved to an InterfaceFaultElement, this method will
     * return null.
     * 
     * @return the InterfaceFaultElement
     */
    public InterfaceFaultElement getInterfaceFaultElement();
}
