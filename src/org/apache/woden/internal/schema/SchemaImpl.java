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
package org.apache.woden.internal.schema;

import java.net.URI;

import org.apache.woden.XMLElement;
import org.apache.woden.schema.Schema;
import org.apache.ws.commons.schema.XmlSchema;

/**
 * Abstract implementation of an XML Schema.
 * 
 * @author jkaputin@apache.org
 */
public abstract class SchemaImpl implements Schema 
{
    private URI fNamespace = null;
    private XmlSchema fSchemaDefinition = null;
    private boolean fIsReferenceable = true;
    private XMLElement fXMLElement;

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.schema.Schema#setNamespace(java.net.URI)
     */
    public void setNamespace(URI namespace) 
    {
        fNamespace = namespace;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.schema.Schema#getNamespace()
     */
    public URI getNamespace() 
    {
        return fNamespace;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.schema.Schema#setSchemaDefinition(org.apache.ws.commons.schema.XmlSchema)
     */
    public void setSchemaDefinition(XmlSchema schemaDef) {
        fSchemaDefinition = schemaDef;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.schema.Schema#getSchemaDefinition()
     */
    public XmlSchema getSchemaDefinition() {
        return fSchemaDefinition;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.schema.Schema#getXMLElement()
     */
    public XMLElement getXMLElement() {
        return fXMLElement;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.schema.Schema#setXMLElement(org.apache.woden.XMLElement)
     */
    public void setXMLElement(XMLElement xsdElement) {
        fXMLElement = xsdElement;
    }

    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
    /* 
     * Indicates whether the schema is referenceable by the containing WSDL, as
     * defined by the WSDL 2.0 spec. In brief, a schema is referenceable if it is 
     * inlined directly under &lt;types&gt; and has a target namespace or if it is
     * resolved from a schema import directly under &lt;types&gt; and the import 
     * namespace matches the schema's target namespace.
     * So for example, when the API is used to retrieve the schema element 
     * declaration identified by a QName attribute, the implementation checks this
     * referenceable property to determine which schemas can be used to resolve the 
     * qname. 
     * Referenceability is determined during validation. If the validation feature
     * is disabled, any inlined or imported schema will be considered referenceable.
     */
    public void setReferenceable(boolean referenceable) {
        fIsReferenceable = referenceable;
    }

    public boolean isReferenceable() {
        return fIsReferenceable;
    }
    
    public String getNamespaceAsString()
    {
        return fNamespace != null ? fNamespace.toString() : null;
    }
}
