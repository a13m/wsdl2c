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
package org.apache.woden.wsdl20;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;


/**
 * Represents the InterfaceMessageReference component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceMessageReference extends NestedComponent {
    
    /**
     * Returns an NCName representing the {message label} property of this 
     * InterfaceMessageReference. This associates the message with a placeholder message
     * in the message exchange pattern specified by the parent interface operation.
     * 
     * @return an NCName representing the message label
     */
    public NCName getMessageLabel();
    
    /**
     * Returns an enumerated type, Direction, that indicates the direction of this message.
     * Direction.IN corresponds to an input message.
     * Direction.OUT corresponds to an output message.
     * 
     * @return the Direction of this message
     */
    public Direction getDirection();
    
    /**
     * Returns a String representing the {message content model} property of this
     * InterfaceMessageReference. This specifies a token indicating the type of message content. 
     * '#any' means any single element, '#none' means no message content, 
     * '#other' means non-XML extension type system and '#element' means 
     * XML Schema global element definition.
     *   
     * @return String representing the type of message content
     */
    public String getMessageContentModel();
    
    /**
     * Returns the ElementDeclaration representing the {element declaration} property
     * of this InterfaceMessageReference. This describes the content or "payload" of the message.
     * 
     * @return the ElementDeclaration that describes the message content.
     */
    public ElementDeclaration getElementDeclaration();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the InterfaceMessageReferenceElement that maps to this InterfaceMessageReference
     */
    public InterfaceMessageReferenceElement toElement();
    
}
