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

import org.apache.woden.wsdl20.xml.DescriptionElement;


/**
 * Represents the Description component from the WSDL 2.0 Component model, 
 * as described in the W3C WSDL 2.0 specification.  It provides an abstract
 * view of a WSDL document by flattening the composite document structure created 
 * by the use of &lt;wsdl:import&gt; or &lt;wsdl:include&gt; elements into a single
 * WSDL Description component containing the WSDL components declared within 
 * the root &lt;description&gt; and within any imported or included descriptions.
 * <p>
 * In other words, if a WSDL component model is derived from composite WSDL document
 * made up of WSDL imports or includes, then its Description component acts as a 
 * container for all of the top-level WSDL components in the WSDL tree, starting with the 
 * root &lt;description&gt; element. These top-level WSDL components include Interface,
 * Binding, Service, ElementDeclaration and TypeDefinition.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Description extends WSDLComponent 
{
    
    /**
     * Represents the {interfaces} property of the Description component. This is the set of
     * all interfaces available to the Description, including those that are declared in the
     * root WSDL document and any declared in included or imported WSDL documents.
     * 
     * @return an array of Interface objects
     */
    public Interface[] getInterfaces();
    
    /**
     * Returns an Interface with the specified name from the {interfaces} property of this 
     * Description.
     * 
     * @param name the QName of the required Interface
     * @return an Interface with the specified name
     */
    public Interface getInterface(QName name);
    
    /**
     * Represents the {bindings} property of the Description component. This is the set of
     * all bindings available to the Description, including those that are declared in the
     * root WSDL document and any declared in included or imported WSDL documents.
     * 
     * @return an array of Binding objects
     */
    public Binding[] getBindings();
    
    /**
     * Returns a Binding with the specified name from the {bindings} property of this 
     * Description.
     * 
     * @param name the QName of the required Binding
     * @return a Binding with the specified name
     */
    public Binding getBinding(QName name);
    
    /**
     * Represents the {services} property of the Description component. This is the set of
     * all services available to the Description, including those that are declared in the
     * root WSDL document and any declared in included or imported WSDL documents.
     * 
     * @return an array of Service objects
     */
    public Service[] getServices();

    /**
     * Returns a Service with the specified name from the {services} property of this 
     * Description.
     * 
     * @param name the QName of the required Service
     * @return a Service with the specified name
     */
    public Service getService(QName name);

    /**
     * Represents the {element declarations} property of the Description component. This is the set of
     * all global element declarations available to the Description, including those that are declared 
     * by schemas inlined or imported by the root WSDL document and those declared by schemas inlined 
     * or imported by WSDL documents that the root WSDL document includes or imports, directly or 
     * indirectly.
     * 
     * @return an array of ElementDeclaration objects
     */
    public ElementDeclaration[] getElementDeclarations();
    
    /**
     * Returns the ElementDeclaration with the specified name from the set of ElementDeclarations 
     * represented by the {element declarations} property of this Description.
     * 
     * @param name the QName of the required ElementDeclaration
     * @return the named ElementDeclaration
     */
    public ElementDeclaration getElementDeclaration(QName name);
    
    /**
     * Represents the {type definitions} property of the Description component. This is the set of
     * all global type definitions available to the Description, including those that are defined 
     * by schemas inlined or imported by the root WSDL document and those defined by schemas inlined 
     * or imported by WSDL documents that the root WSDL document includes or imports, directly or 
     * indirectly.
     * 
     * @return an array of TypeDefinition objects
     */
    public TypeDefinition[] getTypeDefinitions();
    
    /**
     * Returns the TypeDefinition with the specified name from the set of TypeDefinitions 
     * represented by the {type definitions} property of this Description.
     * 
     * @param name the QName of the required TypeDefinition
     * @return the named TypeDefinition
     */
    public TypeDefinition getTypeDefinition(QName name);
    
    /**
     * Returns a WSDLElement that represents the element information item from the WSDL 2.0 
     * infoset that maps to this WSDLComponent. 
     * <p>
     * The Description component is unlike other WSDL components which map neatly to a single 
     * WSDL element. Description represents a 'flattened' view of the entire WSDL infoset tree, 
     * including the WSDL components from any imported or included WSDL documents, so it could 
     * contain the content of multiple &lt;wsdl:description&gt; elements.
     * <p>
     * If the Component model has been derived from such a composite WSDL infoset, the behaviour 
     * of this method is to return the DescriptionElement that represents the root 
     * &lt;wsdl:description&gt; element.  
     * 
     * @return the DescriptionElement that maps to this Description
     */
    public DescriptionElement toElement();
}
