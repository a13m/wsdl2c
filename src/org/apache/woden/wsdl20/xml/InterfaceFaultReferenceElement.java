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
import org.apache.woden.wsdl20.enumeration.Direction;

/**
 * Represents the WSDL 2.0 &lt;infault&gt; and &lt;outfault&gt; elements,
 * declared as child elements of the interface &lt;operation&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceFaultReferenceElement extends DocumentableElement,
                                                        NestedElement 
{
    /**
     * Specify the name of the InterfaceFaultElement referred to by this 
     * InterfaceFaultReferenceElement.
     * The specified QName corresponds to the <code>ref</code> attribute of the interface operation
     * &lt;infault&gt; or &lt;outfault&gt; element.
     *
     * @param faultName the QName of the interface fault
     */
    public void setRef(QName faultName);
    
    /**
     * Return the name of the InterfaceFaultElement referred to by this 
     * InterfaceFaultReferenceElement.
     * This corresponds to the <code>ref</code> attribute of the interface operation 
     * &lt;infault&gt; or &lt;outfault&gt; element.
     * 
     * @return the QName of the interface fault
     */
    public QName getRef();
    
    /**
     * Return the InterfaceFaultElement referred to by this InterfaceFaultReferenceElement.
     * This equates to the interface &lt;fault&gt; element referred to by the <code>ref</code>
     * attribute of the interface operation &lt;infault&gt; or &lt;outfault&gt; element.
     * If this reference cannot be resolved to an InterfaceFaultElement, this method will
     * return null.
     * 
     * @return the InterfaceFaultElement
     */
    public InterfaceFaultElement getInterfaceFaultElement();
    
    /**
     * Set the message label to the specified NCName. 
     * This corresponds to the <code>messageLabel</code> attribute of the interface operation 
     * &lt;infault&gt; and &lt;outfault&gt; elements. 
     * It represents a placeholder message in the message exchange pattern specified by the 
     * parent interface &lt;operation&gt; element.
     * 
     * @param msgLabel the NCName representing the message label
     */
    public void setMessageLabel(NCName msgLabel);
    
    /**
     * Return the NCName representing the message label. 
     * This corresponds to the <code>messageLabel</code> attribute of the interface operation 
     * &lt;infault&gt; and &lt;outfault&gt; elements. 
     * It represents a placeholder message in the message exchange pattern specified by the 
     * parent interface &lt;operation&gt; element.
     * 
     * @return NCName the message label
     */
    public NCName getMessageLabel();
    
    /**
     * Set the message direction using the specified enumerated type, Direction,
     * which indicates the direction of the fault.
     * Direction.IN corresponds to the interface operation &lt;infault&gt; element.
     * Direction.OUT corresponds to the interface operation &lt;outfault&gt; element.
     * 
     * @param dir the Direction of the fault
     */
    public void setDirection(Direction dir);
    
    /**
     * Returns an enumerated type, Direction, that indicates the direction of this fault.
     * Direction.IN corresponds to the interface operation &lt;infault&gt; element.
     * Direction.OUT corresponds to the interface operation &lt;outfault&gt; element.
     * 
     * @return the Direction of the fault
     */
    public Direction getDirection();
    
}
