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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.internal.schema.SchemaImpl;
import org.apache.woden.schema.ImportedSchema;
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.schema.Schema;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaType;

/**
 * This class represents the WSDL &lt;types&gt; element. 
 * 
 * TODO consider methods to get directly declared schemas vs getting all 'in-scope' schemas from the wsdl tree.
 * 
 * @author jkaputin@apache.org
 */
public class TypesImpl extends DocumentableImpl
                       implements TypesElement 
{
    private WSDLElement fParentElem = null;
    private String fTypeSystem = null;
    private List fSchemas = new Vector();
    
    //TODO extension attributes and elements

    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#setTypeSystem(java.lang.String)
     */
    public void setTypeSystem(String typeSystem)
    {
        fTypeSystem = typeSystem;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#getTypeSystem()
     */
    public String getTypeSystem()
    {
        return fTypeSystem;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#addSchema(org.apache.woden.schema.Schema)
     */
    public void addSchema(Schema schema)
    {
        if(schema != null) {
            fSchemas.add(schema);
            //reset flag so ComponentModelBuilder will rebuild the ElementDeclarations and TypeDefinitions
            ((DescriptionImpl)getParentElement()).resetComponentsInitialized();
        }
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#removeSchema(org.apache.woden.schema.Schema)
     */
    public void removeSchema(Schema schema)
    {
        fSchemas.remove(schema);
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#getSchemas()
     */
    public Schema[] getSchemas()
    {
        Schema[] array = new Schema[fSchemas.size()];
        fSchemas.toArray(array);
        return array;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#getSchemas(java.net.URI)
     */
    public Schema[] getSchemas(URI namespace)
    {
        List schemas = new Vector();
        Iterator i = fSchemas.iterator();
        
        if(namespace != null)
        {
            while(i.hasNext()) {
                Schema s = (Schema)i.next();
                if(namespace.equals(s.getNamespace())) {
                    schemas.add(s);
                }
            }
        } 
        else 
        {
            //get schemas whose namespace is missing
            while(i.hasNext()) {
                Schema s = (Schema)i.next();
                if(s.getNamespace() == null) {
                    schemas.add(s);
                }
            }
        }
        
        Schema[] array = new Schema[schemas.size()];
        schemas.toArray(array);
        return array;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#getInlinedSchemas()
     */
    public InlinedSchema[] getInlinedSchemas()
    {
        List schemas = new Vector();
        Iterator i = fSchemas.iterator();
        while(i.hasNext()) {
            Schema s = (Schema)i.next();
            if(s instanceof InlinedSchema) {
                schemas.add(s);
            }
        }
        
        InlinedSchema[] array = new InlinedSchema[schemas.size()];
        schemas.toArray(array);
        return array;
    }
    
    /*
     * @see org.apache.woden.wsdl20.xml.TypesElement#getImportedSchemas()
     */
    public ImportedSchema[] getImportedSchemas()
    {
        List schemas = new Vector();
        Iterator i = fSchemas.iterator();
        while(i.hasNext()) {
            Schema s = (Schema)i.next();
            if(s instanceof ImportedSchema) {
                schemas.add(s);
            }
        }
        
        ImportedSchema[] array = new ImportedSchema[schemas.size()];
        schemas.toArray(array);
        return array;
    }
    
    /* 
     * package private, used only by factory methods in this package
     */
    void setParentElement(WSDLElement parent) {
        fParentElem = parent;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.xml.NestedElement#getParentElement()
     */
    public WSDLElement getParentElement() {
        return fParentElem;
    }
    
    /* ************************************************************
     *  Non-API implementation methods
     * ************************************************************/
    
    /* 
     * TODO decide if this helper method should be on the API, either as-is or replaced by method that returns all accessible schemas.
     * 
     * Returns the schema element declaration identified by the QName,
     * providing the element declaration is referenceable to the 
     * WSDL description (i.e. visible). This means it must exist in a
     * Schema that has been inlined or resolved from a schema import
     * within the &lt;types&gt; element according to the schema 
     * referenceability rules in the WSDL 2.0 spec.
     * If the element declaration is not referenceable, null is returned.
     * If validation is disabled, the referenceability rules do not apply
     * so all schemas are considered referenceable by the WSDL. 
     * 
     * TODO tbd - @see org.apache.woden.wsdl20.xml.TypesElement#getElementDeclaration(javax.xml.namespace.QName)
     */
    public XmlSchemaElement getElementDeclaration(QName qname)
    {
    	// Can't resolve the element if the QName is null.
    	if(qname == null)
    	  return null;
    	
        XmlSchemaElement xmlSchemaElement = null;
        List schemas = getReferenceableSchemaDefs(qname.getNamespaceURI());
        if(schemas != null) 
        {
            //search the schemas with this qname's namespace
            Iterator i = schemas.iterator();
            while(i.hasNext())
            {
                XmlSchema xmlSchema = (XmlSchema)i.next();
                xmlSchemaElement = xmlSchema.getElementByName(qname);
                if(xmlSchemaElement != null) {
                    break;
                }
            }
        }
        return xmlSchemaElement;
    }
    
    /*
     * TODO decide if this helper method should be on the API, either as-is or replaced by method that returns all accessible schemas.
     * 
     * Returns the schema type definition identified by the QName,
     * providing the type definition is referenceable by the 
     * WSDL description (i.e. visible). This means it must exist in a
     * Schema that has been inlined or resolved from a schema import
     * within the &lt;types&gt; element according to the schema
     * referenceability rules in the WSDL 2.0 spec.
     * If the type definition is not referenceable, null is returned.
     * If validation is disabled, the referenceability rules do not apply
     * so all schemas are considered referenceable by the WSDL.
     *  
     * TODO tbd - @see org.apache.woden.wsdl20.xml.TypesElement#getTypeDefinition(javax.xml.namespace.QName)
     */
    public XmlSchemaType getTypeDefinition(QName qname)
    {
        XmlSchemaType xmlSchemaType = null;
        if(qname != null)
        {
            List schemaRefs = getReferenceableSchemaDefs(qname.getNamespaceURI());
            if(schemaRefs != null) 
            {
                //search the schemas with this qname's namespace
                Iterator i = schemaRefs.iterator();
                while(i.hasNext())
                {
                    XmlSchema xmlSchema = (XmlSchema)i.next();
                    xmlSchemaType = xmlSchema.getTypeByName(qname);
                    if(xmlSchemaType != null) {
                        break;
                    }
                }
            }
        }
        return xmlSchemaType;
    }
    
    /*
     * Returns a List of XmlSchema objects for all schemas that are referenceable 
     * by the containing WSDL. Examples of schemas that are not referenceable include
     * schemas without a target namespace or schemas resolved from a schema import
     * whose target namespace does not match the imported namespace. Referenceability
     * is determined by validation.
     * NOTE: This is an implementation-only method used to build the ElementDeclarations
     * components (i.e. it is not an API method). If it is required on the API it must be 
     * changed to use a type safe return value.
     * 
     * TODO t.b.c. remove if made redundant by WODEN-123 
     */
    private List getReferenceableSchemaDefs()
    {
        List schemas = new Vector();
        Iterator i = fSchemas.iterator();
        while(i.hasNext())
        {
            SchemaImpl s = (SchemaImpl)i.next();
            if(s.isReferenceable() &&
               s.getSchemaDefinition() != null) 
            {
                schemas.add(s.getSchemaDefinition());
            }
        }
        return schemas;
    }
    
    /*
     * Return a Lists of XmlSchema for all schemas with the specified target namespace 
     * or import namespace that are referenceable by the WSDL.
     * Note, this method requires a non-null namespace argument.
     * 
     * TODO t.b.d. remove the notion of referenceability - just get ALL schemas?
     */
    private List getReferenceableSchemaDefs(String namespace)
    {
        
        List schemas = new Vector();
        if(namespace != null)
        {
            Iterator i = fSchemas.iterator();
            while(i.hasNext())
            {
                SchemaImpl s = (SchemaImpl)i.next();
                if(s.isReferenceable() && 
                   namespace.equals(s.getNamespaceAsString()) &&
                   s.getSchemaDefinition() != null) 
                {
                    schemas.add(s.getSchemaDefinition());
                }
            }
        }
        return schemas;
    }
    
}
