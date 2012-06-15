/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axiom.util.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/**
 * Partial implementation of the {@link XMLReader} interface. It implements all the getters and
 * setters so that subclasses only need to implement {@link XMLReader#parse(InputSource)} and
 * {@link XMLReader#parse(String)}. Subclasses can access the various handlers and properties set on
 * the reader through protected attributes.
 */
public abstract class AbstractXMLReader implements XMLReader {
    private static final String URI_LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
    
    protected boolean namespaces = true;
    protected boolean namespacePrefixes = false;
    
    protected ContentHandler contentHandler;
    protected LexicalHandler lexicalHandler;
    protected DTDHandler dtdHandler;
    protected EntityResolver entityResolver;
    protected ErrorHandler errorHandler;
    
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public DTDHandler getDTDHandler() {
        return dtdHandler;
    }

    public void setDTDHandler(DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public boolean getFeature(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException(name);
    }

    public void setFeature(String name, boolean value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        
        if ("http://xml.org/sax/features/namespaces".equals(name)) {
            namespaces = value;
        } else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            namespacePrefixes = value;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }

    public Object getProperty(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        
        if ("http://xml.org/sax/features/namespaces".equals(name)) {
            return Boolean.valueOf(namespaces);
        } else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            return Boolean.valueOf(namespacePrefixes);
        } else if (URI_LEXICAL_HANDLER.equals(name)) {
            return lexicalHandler;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }

    public void setProperty(String name, Object value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (URI_LEXICAL_HANDLER.equals(name)) {
            lexicalHandler = (LexicalHandler)value;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }


}
