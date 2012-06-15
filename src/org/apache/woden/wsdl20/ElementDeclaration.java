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

import java.net.URI;

import javax.xml.namespace.QName;

/**
 * Represents the ElementDeclaration component described in the WSDL 2.0 component 
 * model (within the Description Component section of the W3C WSDL 2.0 spec). 
 * It describes the content of WSDL input, output and fault messages.
 * This component represents global element declarations such as top-level,
 * named element declarations in W3C XML Schema (i.e. &lt;xs:element&gt;).
 * <p>
 * However, it does not mandate W3C XML Schema as the type system. 
 * It defines behaviour to query the type system and the underlying content
 * model or API being used to represent the element declarations, and to 
 * return a java.lang.Object representing the actual element declaration object.
 * Based on the type system and content model, the application must cast this
 * Object to the appropriate type to manipulate its contents.
 * <p>
 * Note that while ElementDeclaration is described along with the Component model
 * in the W3C WSDL 2.0 specification, it is not a WSDL component itself. 
 * It simply provides a way of representing components from the underlying type 
 * system within the WSDL Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface ElementDeclaration {
    
    /**
     * A constant representing the DOM API. This may be used to indicate the 
     * content model of the underlying element declaration.
     */
    public static final String API_W3C_DOM =
        "org.w3c.dom";

    /**
     * A constant representing the Apache WS-Commons XmlSchema API. This may be used to 
     * indicate the content model of the underlying element declaration.
     */
    public static final String API_APACHE_WS_XS =
        "org.apache.ws.commons.schema";

    /**
     * Representing the {name} property of the ElementDeclaration component, this 
     * method returns the qualified name of this ElementDeclaration.
     * 
     * @return the QName that identifies this ElementDeclaration
     */
    public QName getName();
    
    /**
     * Representing the {system} property of the ElementDeclaration component, this
     * method indicates the type system from which this element declaration is derived.
     * For example, "http://www.w3.org/2001/XMLSchema" indicates the W3C XML Schema
     * type system.
     *  
     * @return the URI identifying the type system
     */
    public URI getSystem();
    
    /**
     * Indicates the type of object model or API which should be used to 
     * access the content of the underlying element declaration.
     * This can be used to determine how to cast the Object returned by
     * the <code>getContent()</code> method.
     * <p>
     * For example:
     * <ul>
     * <li>The content model "org.w3c.dom" indicates that the DOM API should be used, 
     * so the type org.w3c.dom.Element will be used to represent the content of the
     * element declaration.
     * <li>The content model "org.apache.ws.commons.schema" indicates that the 
     * WS-Commons XmlSchema API from the Apache WebServices project is used, 
     * so an org.apache.ws.commons.schema.XmlSchemaElement will be used to represent the 
     * content of the element declaration. 
     * </ul>
     * 
     * @return a String identifying the element declaration's content model
     */
    public String getContentModel();
    
    /**
     * Returns the underlying element declaration as a java.lang.Object, which should
     * be cast to the appropriate type as indicated by the <code>getContentModel()</code> 
     * method.
     * 
     * @return the Object representing the content of the element declaration
     */
    public Object getContent();
    
}
