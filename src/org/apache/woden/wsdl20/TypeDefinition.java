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
 * Represents the TypeDefinition component described in the WSDL 2.0 component model 
 * (within the Description Component section of the W3C WSDL 2.0 spec). 
 * This component represents global data type definitions such as top-level,
 * named type definitions in W3C XML Schema (e.g. &lt;xs:simpleType&gt; or 
 * &lt;xs:complexType&gt;).
 * <p>
 * However, it does not mandate W3C XML Schema as the type system. 
 * It defines behaviour to query the type system and the underlying content
 * model or API being used to represent the type definitions, and to 
 * return a java.lang.Object representing the actual type definition object.
 * Based on the type system and content model, the application must cast this
 * Object to the appropriate type to manipulate its contents.
 * <p>
 * Note that while TypeDefinition is described along with the Component model
 * in the W3C WSDL 2.0 specification, it is not a WSDL component itself. 
 * It simply provides a way of representing components from the underlying type 
 * system within the WSDL Component model.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface TypeDefinition {
    
    /**
     * A constant representing the DOM API. This may be used to indicate the 
     * content model of the underlying type definition.
     */
    public static final String API_W3C_DOM =
        "org.w3c.dom";

    /**
     * A constant representing the Apache WS-Commons XmlSchema API. This may be used to 
     * indicate the content model of the underlying type definition.
     */
    public static final String API_APACHE_WS_XS =
        "org.apache.ws.commons.schema";

    /**
     * Representing the {name} property of the TypeDefinition component, this 
     * method returns the qualified name of this TypeDefinition.
     * 
     * @return the QName that identifies this TypeDefinition
     */
    public QName getName();
    
    /**
     * Representing the {system} property of the TypeDefinition component, this
     * method indicates the type system from which this type definition is derived.
     * For example, "http://www.w3.org/2001/XMLSchema" indicates the W3C XML Schema
     * type system.
     *  
     * @return the URI identifying the type system
     */
    public URI getSystem();
    
    /**
     * Indicates the type of object model or API which should be used to 
     * access the content of the underlying type definition.
     * This can be used to determine how to cast the Object returned by
     * the <code>getContent()</code> method.
     * <p>
     * For example:
     * <ul>
     * <li>The content model "org.w3c.dom" indicates that the DOM API should be used, 
     * so the type org.w3c.dom.Element will be used to represent the content of the
     * type definition.
     * <li>The content model "org.apache.ws.commons.schema" indicates that the 
     * WS-Commons XmlSchema API from the Apache WebServices project is used, 
     * so an org.apache.ws.commons.schema.XmlSchemaType will be used to represent the 
     * content of the type definition. 
     * </ul>
     * 
     * @return a String identifying the type definition's content model
     */
    public String getContentModel();
    
    /**
     * Returns the underlying type definition as a java.lang.Object, which should
     * be cast to the appropriate type as indicated by the <code>getContentModel()</code> 
     * method.
     * 
     * @return the Object representing the content of the type definition
     */
    public Object getContent();

}
