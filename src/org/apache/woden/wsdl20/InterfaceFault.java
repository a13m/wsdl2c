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

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.xml.InterfaceFaultElement;


/**
 * Represents the InterfaceFault component from the WSDL 2.0 Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface InterfaceFault extends NestedComponent {

    /**
     * Returns the qualified name representing the {name} property of this InterfaceFault.
     * 
     * @return QName representing the name of this InterfaceFault
     */
    public QName getName();
    
    /**
     * Returns a String representing the {message content model} property of this
     * InterfaceFault. This specifies a token indicating the type of message content. 
     * '#any' means any single element, '#none' means no message content, 
     * '#other' means non-XML extension type system and '#element' means 
     * XML Schema global element definition.
     * 
     * TODO CR138 adds this property, so check Spec when its updated to ensure javadoc is accurate
     *   
     * @return String representing the type of message content
     */
    public String getMessageContentModel();
    
    /**
     * Returns the ElementDeclaration representing the {element declaration} property
     * of this InterfaceFault. This describes the content or "payload" of the fault.
     * 
     * @return the ElementDeclaration that describes the fault content.
     */
    public ElementDeclaration getElementDeclaration();
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * 
     * @return the InterfaceFaultElement that maps to this InterfaceFault
     */
    public InterfaceFaultElement toElement();
    
}
