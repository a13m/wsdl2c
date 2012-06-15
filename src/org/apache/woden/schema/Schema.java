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
package org.apache.woden.schema;


import java.net.URI;

import org.apache.woden.XMLElement;
import org.apache.ws.commons.schema.XmlSchema;

/**
 * This interface provides an abstract representation of an XML Schema referenced 
 * within the &lt;wsdl:types&gt; element. For example, via &lt;xs:schema&gt; or 
 * &lt;xs:import&gt;.
 * It provides the namespace used as the target namespace of an inlined schema
 * or as the imported namespace of a schema import. 
 * It provides a reference to the actual schema definition, represented by
 * <code>org.apache.ws.commons.schema.XmlSchema</code>.
 * For applications that use other representations for XML Schema content,
 * it also provides a reference to the <code>org.apache.woden.XMLElement</code>
 * object that wraps the underlying &lt;xs:schema&gt; or &lt;xs:import&gt; 
 * element.
 * It also indicates whether the schema is 'referenceable' by the surrounding
 * WSDL document, as defined by the schema referenceability rules in the 
 * WSDL 2.0 spec.
 * <p>
 * 
 * NOTE: non-XML type systems like DTD are not handled by this interface. They must be
 * handled by WSDL 2.0 extension mechanisms.
 * <br />
 * TODO Need to determine if this interface is suitable for use with other xml-based 
 * schema types like Relax NG or if some type of schema extension mechanism is required. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface Schema {
    
    /**
     * Returns a URI representing the <code>targetNamespace</code> attribute of a 
     * &lt;xs:schema&gt; element or the <code>namespace</code> attribute of a 
     * &lt;xs:import&gt; element.
     * 
     * @return a URI representing the schema's namespace
     */
    public URI getNamespace();
    
    /**
     * Set the <code>targetNamespace</code> attribute of a &lt;xs:schema&gt; element
     * or the <code>namespace</code> attribute of a &lt;xs:import&gt; element.
     * @param namespace
     */
    public void setNamespace(URI namespace);
    
    /**
     * Returns an <code>XmlSchema</code> representing the schema definition inlined by
     * a &lt;xs:schema&gt; element or imported by a &lt;xs:import&gt; element.
     * 
     * @return the <code>XmlSchema</code> representing schema definition.
     */
    public XmlSchema getSchemaDefinition();
    
    /**
     * Sets the schema definition for an inlined schema or schema import to the specified
     * <code>XmlSchema</code>.
     *  
     * @param schemaDef the <code>XmlSchema</code> representing this schema
     */
    public void setSchemaDefinition(XmlSchema schemaDef);
    
    /**
     * Returns the XMLElement representing the <code>xs:schema</code> or <code>xs:import</code>
     * element within the <code>wsdl:types</code> element. This provides an 'wrapper' to the 
     * underlying XML Schema infoset for applications that need schema processing alternatives to
     * Apache WS Commons XmlSchema.
     * 
     * @return the XMLElement that wraps the underlying schema or schema import element
     */
    public XMLElement getXMLElement();
    
    /**
     * Sets the XMLElement representing the underlying <code>xs:schema</code> or <code>xs:import</code>
     * element.
     * 
     * @param xsdElement the XMLElement
     */
    public void setXMLElement(XMLElement xsdElement);
}
