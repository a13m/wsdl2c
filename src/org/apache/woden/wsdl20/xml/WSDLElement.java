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
import org.apache.woden.types.NamespaceDeclaration;
import org.apache.woden.wsdl20.extensions.AttributeExtensible;
import org.apache.woden.wsdl20.extensions.ElementExtensible;

/**
 * Represents all WSDL 2.0 elements. The Java interfaces representing the WSDL 2.0 
 * elements will directly or indirectly extend this interface.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface WSDLElement extends AttributeExtensible, ElementExtensible
{
    /*
     * All elements in the WSDL 2.0 namespace support attribute extensibility and
     * element extensibility, so by inheriting directly or indirectly from this 
     * interface they also inherit the extensibility interfaces.
     */
    
    /**
     * Associate the specified prefix with the specified namespace URI to this WSDL element.
     * This equates to adding an <code>xmlns</code> namespace declaration to this 
     * WSDL element. 
     * To define the default namespace, specify null or the empty string "" for the prefix.
     * If null is specified for the namespace URI, the prefix/namespace association will be
     * removed (i.e. the same behaviour as the <code>removeNamespace</code> method).
     * If the specified prefix is already associated with a namespace URI, 
     * that association will be replaced by the specified prefix/namespace association. 
     *
     * @param prefix the prefix String associated with <code>namespaceURI</code>
     * @param namespaceURI the namespace URI associated with <code>prefix</code>
     */
    public void addNamespace(String prefix, URI namespaceURI);
    
    /**
     * Remove the namespace URI associated with the specified prefix from this WSDL element.
     * This equates to removing an <code>xmlns</code> namespace declaration from this
     * WSDL element.
     * To remove the default namespace, specify null or the empty string "" for the prefix.
     * 
     * @param prefix the prefix String associated with the namespace to be removed
     * @return the removed namespace URI or null if no prefix/namespace association exists
     */
    public URI removeNamespace(String prefix);
    
    
    /**
     * Return the namespace URI associated with the specified prefix, or null if there is no
     * such namespace declaration.
     * The scope of the search corresponds to the scope of namespace declarations 
     * in XML. That is, from the current element upwards to the root element
     * (to the wsdl:description).
     * To request the default namespace, specify null or the empty string "" for the prefix.
     *  
     * @param prefix the prefix whose associated namespace URI is required
     * @return the associated namespace URI
     */
    public URI getNamespaceURI(String prefix);
    
    /**
     * Return the prefix associated with the specified namespace URI.
     * The scope of the search corresponds to the scope of namespace declarations
     * in XML. That is, from the current element upwards to the root element
     * (to the wsdl:description).
     * 
     * @param namespaceURI the namespace URI whose associated prefix is required
     * @return the associated prefix String
     */
    public String getNamespacePrefix(URI namespaceURI);
    
    /**
     * Return the namespaces and their associated prefixes declared directly
     * within this WSDL element.
     * 
     * @return an array of NamespaceDeclaration
     */
    public NamespaceDeclaration[] getDeclaredNamespaces();
    
    /**
     * Return all namespaces and their associated prefixes that are in-scope
     * to this WSDL element. That is, those declared directly within this element
     * and those declared in ancestor elements upwards to the root element
     * (to the wsdl:description).
     * 
     * @return an array of NamespaceDeclaration
     */
    public NamespaceDeclaration[] getInScopeNamespaces();
    
}
