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
package org.apache.woden.internal.resolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.woden.XMLElement;
import org.apache.woden.internal.schema.SchemaConstants;
import org.apache.woden.resolver.URIResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class that adds DOM specific behaviour to the SchemaResolverAdapter class.
 * 
 * TODO consider some approach other than inheritance as part of a broader consideration
 * of resolving imports and fragids.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 *
 */

public class DOMSchemaResolverAdapter extends SchemaResolverAdapter {

    public DOMSchemaResolverAdapter(URIResolver actualResolver, XMLElement schemaElement) {
        super(actualResolver, schemaElement);
    }
    
    protected InputStream resolveFragId(String fragId) {
        Element contextEl = (Element)fContextElement.getSource();
        Document doc = contextEl.getOwnerDocument();
        String id = fragId.substring(1);
        Element schemaEl = doc.getElementById(id);
        
        if(schemaEl == null) {
            //the fragid does not identify any element
            //TODO suitable error message
            return null;
        }
        
        String localName = schemaEl.getLocalName();
        if(!localName.equals(SchemaConstants.ELEM_SCHEMA)) {
            //the fragid does not point to a schema element
            //TODO suitable error message
            return null;
        }
        
        String prefix = schemaEl.getPrefix();
        if(prefix != null) {
            //check if we need to add a schema NS declaration
            String nsUri = schemaEl.getNamespaceURI();
            String schemaNSDecl = "xmlns:" + prefix;
            boolean isSchemaNSDeclared = schemaEl.hasAttribute(schemaNSDecl);
            if(!isSchemaNSDeclared) {
                schemaEl.setAttribute(schemaNSDecl, nsUri);
            }
        }
        
        //TODO need to check for other prefixes requiring NS decls to be added to the schema element
        //replaced with JAXP 
        /*
        OutputFormat format = new OutputFormat(doc);
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(oStream, format);
        try {
            serializer.serialize(schemaEl);
        } catch (IOException e) {
            // TODO this conforms to parent, but needs an error message via ErrorReporter and maybe it should be handled differently?
            throw new RuntimeException(e);
        } 
        */
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(oStream));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }  
        ByteArrayInputStream iStream = new ByteArrayInputStream(oStream.toByteArray());
        return iStream;
    }
}
