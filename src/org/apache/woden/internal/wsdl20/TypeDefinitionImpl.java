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
package org.apache.woden.internal.wsdl20;

import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.WSDLReader;
import org.apache.woden.wsdl20.TypeDefinition;

import org.apache.woden.wsdl20.fragids.FragmentIdentifier;
import org.apache.woden.wsdl20.fragids.TypeDefinitionPart;

/**
 * This class represents a TypeDefinition property of the Description component.
 * It refers to a global type definition provided by the underlying type
 * system (e.g. XML Schema) 
 * 
 * @author jkaputin@apache.org
 */
public class TypeDefinitionImpl implements TypeDefinition {
    
    private QName fName = null;
    private URI fSystem = null;
    private String fContentModel = null;
    private Object fContent = null;

    /* ************************************************************
     *  TypeDefinition interface methods (the WSDL Component model)
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.TypeDefinition#getName()
     */
    public QName getName() 
    {
        return fName;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.TypeDefinition#getSystem()
     */
    public URI getSystem() 
    {
        return fSystem;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.TypeDefinition#getContentModel()
     */
    public String getContentModel() 
    {
        return fContentModel;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.TypeDefinition#getContent()
     */
    public Object getContent() 
    {
        return fContent;
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/

    public void setName(QName name)
    {
        fName = name;
    }

    public void setSystem(URI typeSystemURI)
    {
        fSystem = typeSystemURI;
    }

    public void setContentModel(String contentModel)
    {
        fContentModel = contentModel;
    }

    public void setContent(Object typeDefContent)
    {
        fContent = typeDefContent;
    }
    
    public FragmentIdentifier getFragmentIdentifier() {
        if (fSystem == null | fSystem.toString().equals(WSDLReader.TYPE_XSD_2001)) {
            return new FragmentIdentifier(new TypeDefinitionPart(fName));  
        } else {
            return new FragmentIdentifier(new TypeDefinitionPart(fName, fSystem));  
        }
    }
    
    public String toString() {
        return getFragmentIdentifier().toString();
    }

}
