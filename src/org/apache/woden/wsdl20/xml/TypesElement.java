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

import org.apache.woden.schema.ImportedSchema;
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.schema.Schema;

/**
 * Represents the WSDL 2.0 &lt;types&gt; element. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface TypesElement extends DocumentableElement, NestedElement
{
    
    /**
     * Indicates the type system used within the &lt;types&gt; element. 
     * Typically the W3C XML Schema type system will be used, indicated by 
     * the namespace "http://www.w3.org/2001/XMLSchema". An alternative
     * schema-like type system is Relax NG (http://www.relaxng.org/).
     */
    public void setTypeSystem(String typeSystem);
    
    /**
     * Get the string indicating the type system used within the &lt;types&gt;
     * element.
     */
    public String getTypeSystem();
    
    /**
     * Add a Schema object for a schema inlined or imported within the &lt;types&gt; element.
     * 
     * @param schema the Schema object.
     */
    public void addSchema(Schema schema);
    
    /**
     * Delete the specified Schema object.
     */
    public void removeSchema(Schema schema);
    
    /**
     * Return the schemas inlined or imported directly within this &lt;types&gt; element.
     * 
     * @return an array of Schema objects
     */
    public Schema[] getSchemas();
    
    /**
     * Return the schemas inlined or imported directly within this &lt;types&gt; element
     * whose target namespace matches the specified namespace. 
     * <p>
     * A null namespace argument will return schemas that have no target namespace.
     * 
     * @return an array of Schema objects with the specified target namespace.
     */
    public Schema[] getSchemas(URI namespace);
    
    /**
     * Return the schemas inlined directly within this &lt;types&gt; element
     * in the order in which they occur.
     * 
     * @return an array of Schema objects.
     */
    public InlinedSchema[] getInlinedSchemas();
    
    /**
     * Return the schemas imported directly by this &lt;types&gt; element 
     * in the order in which they occur.
     * 
     * @return an array of Schema objects.
     */
    public ImportedSchema[] getImportedSchemas();
        
}
