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
package org.apache.woden.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLReader;
import org.apache.woden.WSDLSource;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.resolver.DOMSchemaResolverAdapter;
import org.apache.woden.internal.resolver.EntityResolverAdapter;
import org.apache.woden.internal.schema.ImportedSchemaImpl;
import org.apache.woden.internal.schema.InlinedSchemaImpl;
import org.apache.woden.internal.schema.SchemaConstants;
import org.apache.woden.internal.util.StringUtils;
import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.internal.wsdl20.validation.WSDLComponentValidator;
import org.apache.woden.internal.wsdl20.validation.WSDLDocumentValidator;
import org.apache.woden.internal.wsdl20.validation.WSDLValidator;
import org.apache.woden.internal.xpointer.DOMXMLElementEvaluator;
import org.apache.woden.schema.Schema;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.ImportElement;
import org.apache.woden.wsdl20.xml.IncludeElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.woden.xml.XMLAttr;
import org.apache.woden.xpointer.InvalidXPointerException;
import org.apache.woden.xpointer.XPointer;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Implements the WSDLReader behaviour for DOM-based parsing.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class DOMWSDLReader extends BaseWSDLReader {
    
    private static final String emptyString = "".intern();
    
    static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema"; 
    
    static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    // TODO: This external schema location should be removed once an URI resolution framework
    // with a catalog is added to Woden.
    static final String WSDL120_SCHEMA_SOURCE = 
        "http://www.w3.org/2007/03/wsdl/wsdl20.xsd";
    static final String WSDL120_EXTENSIONS_SCHEMA_SOURCE = 
        "http://www.w3.org/2007/03/wsdl/wsdl20-extensions.xsd";
    static final String W3C_XML_SCHEMA_SCHEMA_SOURCE = 
        "http://www.w3.org/2001/XMLSchema.xsd";

    static final String[] schemas = {
        WSDL120_SCHEMA_SOURCE,
        WSDL120_EXTENSIONS_SCHEMA_SOURCE, 
        W3C_XML_SCHEMA_SCHEMA_SOURCE,
        };
    
    /** SLF based logger. */
    private static final Log logger=LogFactory.getLog(DOMWSDLReader.class);
    
    //a map of imported schema definitions keyed by schema location URI
    private Map fImportedSchemas = new Hashtable();
    
    /**
     * WSDL document validator. Only one instance is needed.
     */
    private WSDLDocumentValidator docValidator = null;
    
    /**
     * WSDL component validator. Only one instance is needed.
     */
    private WSDLComponentValidator compValidator = null;
    
    DOMWSDLReader(WSDLContext wsdlContext) throws WSDLException {
        super(wsdlContext);
    }
    
    /* ************************************************************
     *  API public methods
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.WSDLReader#createWSDLSource()
     */
    public WSDLSource createWSDLSource() {
        return new DOMWSDLSource(getErrorReporter());
    }
    
    /*
     * @see org.apache.woden.WSDLReader#readWSDL(String)
     */
    public Description readWSDL(String wsdlURI) throws WSDLException 
    {
        URL url;
        try {
            url = StringUtils.getURL(null, wsdlURI);
            
        } catch (MalformedURLException e) {
            
            String msg = getErrorReporter().getFormattedMessage(
                            "WSDL516", new Object[] {wsdlURI});
            throw new WSDLException(WSDLException.PARSER_ERROR, msg, e);
        }
        String wsdlURL = url.toString();
           
        // ensure InputSource runs thru the URI Resolver
        InputSource inputSource = new InputSource(resolveURI(wsdlURL));
        return readWSDL(wsdlURL, inputSource);
    }
        
    /* (non-Javadoc)
     * @see org.apache.woden.WSDLReader#readWSDL(org.apache.woden.WSDLSource)
     */
    public Description readWSDL(WSDLSource wsdlSource) throws WSDLException {
        
        //TODO decide on how to handle null args in readWSDL methods (e.g.
        //IllegalArgExc, WSDLExc, return null, etc).

        Object source = wsdlSource.getSource();
        URI baseURI = wsdlSource.getBaseURI();
        
        String wsdlURL = null;
        if(baseURI != null)
        {
            URL url;
            try {
                url = StringUtils.getURL(null, baseURI.toString());
                
            } catch (MalformedURLException e) {
                
                String msg = getErrorReporter().getFormattedMessage(
                        "WSDL516", new Object[] {baseURI.toString()});
                throw new WSDLException(WSDLException.PARSER_ERROR, msg, e);
            }
            wsdlURL = url.toString();
        }
        
        if(source instanceof Element) {
            return readWSDL(wsdlURL, (Element)source);
        }
        else if(source instanceof Document) {
            return readWSDL(wsdlURL, (Document)source);
        }
        else if(source instanceof InputSource) {
            return readWSDL(wsdlURL, (InputSource)source);
        }
        else {
            //This exception is checked in WSDLSource.setSource but we check
            //again here in case the wrong type of WSDLSource has been used
            //with this type of WSDLReader.
            String sourceClass = source.getClass().getName();
            String readerClass = this.getClass().getName();
            String msg = getErrorReporter().getFormattedMessage(
                    "WSDL017", new Object[] {sourceClass, readerClass});
            throw new WSDLException(WSDLException.PARSER_ERROR, msg);
        }
    }
    
    /*
     * Helper method for readWSDL(WSDLSource)
     */
    private Description readWSDL(String wsdlURL, Element docEl) 
        throws WSDLException {
        
    	XMLElement descEl = createXMLElement(docEl);
        DescriptionElement descElem = parseDescription(wsdlURL, descEl, null);
        Description descComp = descElem.toComponent();
                  
        //TODO if schema errors, don't do any further validation (i.e. assertions assume WSDL is schema valid)
        
        // Validate the model if validation is enabled.
        if(features.getValue(WSDLReader.FEATURE_VALIDATION))
        {
            /*
            if(docValidator == null)
            {
                docValidator = new WSDLDocumentValidator();
            }
            if(docValidator.validate(descElem, getErrorReporter()))
            {
                if(compValidator == null)
                {
                    compValidator = new WSDLComponentValidator();
                }
                compValidator.validate(descComp, getErrorReporter());
            }
            */
            (new WSDLValidator()).validate(descComp, fWsdlContext);
        }
        
        return descComp;
    }
    
    /*
     * Helper method for readWSDL(WSDLSource)
     */
    private Description readWSDL(String wsdlURI, Document domDoc) 
        throws WSDLException {
        
        //Try to find an element the XPointer points to if a Fragment Identifier exists.
        URI uri = null;
        try {
            uri = new URI(wsdlURI);
        } catch (URISyntaxException e) {
            String msg = getErrorReporter().getFormattedMessage(
                    "WSDL506", new Object[] {null, wsdlURI});
            throw new WSDLException(WSDLException.PARSER_ERROR, msg, e);
        }
        
        String fragment = uri.getFragment();
        
        
        if (fragment == null) { //No fragment identifier so just use the root element.
            return readWSDL(wsdlURI, domDoc.getDocumentElement());//Use document root if no WSDL20 root found.
        } else {
            XPointer xpointer;
            try {
                xpointer = new XPointer(fragment);
            } catch(InvalidXPointerException e) {
                String msg = getErrorReporter().getFormattedMessage(
                        "WSDL530", new Object[] {fragment, wsdlURI});
                throw new WSDLException(WSDLException.PARSER_ERROR, msg, e);
            }
            Element root = domDoc.getDocumentElement();
            
            DOMXMLElementEvaluator evaluator = new DOMXMLElementEvaluator(xpointer, root, getErrorReporter());
            Element result = evaluator.evaluateElement();
            
            if (result != null) { //Element from XPointer evaluation.
                return readWSDL(wsdlURI, result);
            } else {
                String msg = getErrorReporter().getFormattedMessage(
                        "WSDL531", new Object[] {fragment, wsdlURI});
                throw new WSDLException(WSDLException.PARSER_ERROR, msg);
            }
        }
    }
    
    /*
     * Helper method for readWSDL(WSDLSource)
     */
    private Description readWSDL(String wsdlURI, InputSource inputSource) 
        throws WSDLException {
    
        try
        {
            Document wsdlDocument = getDocument(inputSource, wsdlURI);
            
            return readWSDL(wsdlURI, wsdlDocument);
            
        } catch (IOException e) {
            String msg = getErrorReporter().getFormattedMessage(
                    "WSDL503", new Object[] {wsdlURI});
            throw new WSDLException(WSDLException.PARSER_ERROR, msg, e);
        }
    }

    /* ************************************************************
     *  Parsing methods - e.g. parseXXXX()
     * ************************************************************/
    

    protected Schema parseSchemaInline(XMLElement schemaEl,
                                     DescriptionElement desc) 
                                     throws WSDLException
    {
        InlinedSchemaImpl schema = new InlinedSchemaImpl();
        schema.setXMLElement(schemaEl);
        
        schema.setId(schemaEl.getAttributeValue(SchemaConstants.ATTR_ID));
        
        String tns = schemaEl.getAttributeValue(SchemaConstants.ATTR_TARGET_NAMESPACE);
        if(tns != null) {
            schema.setNamespace(getURI(tns));
        }
        
        String baseURI = desc.getDocumentBaseURI() != null ?
                         desc.getDocumentBaseURI().toString() : null;
        XmlSchema schemaDef = null;
        
        try {
        	Element domSchemaEl = (Element)schemaEl.getSource();
            XmlSchemaCollection xsc = new XmlSchemaCollection();
            xsc.setBaseUri(baseURI);
            
            // Plug in the selected woden URI Resolver
            xsc.setSchemaResolver(new DOMSchemaResolverAdapter(getURIResolver(), schemaEl));         
            schemaDef = xsc.read(domSchemaEl, baseURI);
        } 
        catch (XmlSchemaException e) 
        {
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL521", 
                    new Object[] {baseURI}, 
                    ErrorReporter.SEVERITY_WARNING,
                    e);
        }
        catch (RuntimeException e)
        {
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL521", 
                    new Object[] {baseURI}, 
                    ErrorReporter.SEVERITY_ERROR,
                    e);            
        }
        
        if(schemaDef != null) {
            schema.setSchemaDefinition(schemaDef);
        } else {
            schema.setReferenceable(false);
        }
        
        return schema;
    }

    /*
     * Parse the &lt;xs:import&gt; element and resolve the import to an
     * XML Schema definition. Failure to retrieve 
     * the schema will only matter if any WSDL components contain elements or
     * constraints that refer to the schema, and typically this will be 
     * determined later by WSDL validation. So just report any such errors
     * and return the SchemaImport object (i.e. with a null schema property).
     * 
     * WSDL 2.0 spec validation:
     * - namespace attribute is REQUIRED
     * - imported schema MUST have a targetNamespace
     * - namespace and targetNamespace MUST be the same
     * 
     * TODO implement a framework for caching schemas by namespace and resolving xs:import 
     */
    protected Schema parseSchemaImport(XMLElement importEl,
                                     DescriptionElement desc) 
                                     throws WSDLException
    {
        ImportedSchemaImpl schema = new ImportedSchemaImpl();
        schema.setXMLElement(importEl);
        
        String importNS = importEl.getAttributeValue(SchemaConstants.ATTR_NAMESPACE);
        if(importNS != null) {
            schema.setNamespace(getURI(importNS));
        }
        
        String schemaLoc = importEl.getAttributeValue(SchemaConstants.ATTR_SCHEMA_LOCATION);
        if(schemaLoc != null) {
            schema.setSchemaLocation(getURI(schemaLoc));
        }
        
        if(schema.getNamespace() == null)
        {
            //The namespace attribute is REQUIRED on xs:import, so don't continue.
            schema.setReferenceable(false);
            return schema;
        }
        
        XmlSchema schemaDef = null;
        
        if(schema.getSchemaLocation() != null)
        {
            schemaDef = retrieveSchema(importEl, desc.getDocumentBaseURI(), schemaLoc);
        }
        
        if(schemaDef == null) {
            //Either there was no schemaLocation or it did not resolve to a schema,
            //so try to retrieve a schema at the namespace.
            schemaDef = retrieveSchema(importEl, null, importNS);
        }
        
        if(schemaDef == null) {
            //Check if any WSDL imports contain a schema with this namespace.
            //TODO there may be multiple schemas that this namespace import could resolve to. This is a temporary solution pending WODEN- post M7.
            ImportElement[] imports = desc.getImportElements();
            for(int i=0; i<imports.length; i++) {
                ImportElement importElem = (ImportElement) imports[i];
                DescriptionElement nestedDesc = importElem.getDescriptionElement();
                if(nestedDesc != null) {
                    TypesElement typesElem = nestedDesc.getTypesElement();
                    if(typesElem != null) {
                        Schema[] schemas = typesElem.getSchemas(schema.getNamespace());
                        for(int j=0; j<schemas.length; j++) {
                            Schema s = (Schema)schemas[i];
                            XmlSchema x = s.getSchemaDefinition();
                            if(x != null) {
                                schemaDef = x;
                                break; 
                            }
                        }
                    }
                }
            }
        }
        
        if(schemaDef == null) {
            //Check if any WSDL includes contain a schema with this namespace.
            //TODO there may be multiple schemas that this namespace import could resolve to. This is a temporary solution pending WODEN- post M7.
            IncludeElement[] includes = desc.getIncludeElements();
            for(int i=0; i<includes.length; i++) {
                IncludeElement includeElem = (IncludeElement) includes[i];
                DescriptionElement nestedDesc = includeElem.getDescriptionElement();
                if(nestedDesc != null) {
                    TypesElement typesElem = nestedDesc.getTypesElement();
                    if(typesElem != null) {
                        Schema[] schemas = typesElem.getSchemas(schema.getNamespace());
                        for(int j=0; j<schemas.length; j++) {
                            Schema s = (Schema)schemas[i];
                            XmlSchema x = s.getSchemaDefinition();
                            if(x != null) {
                                schemaDef = x;
                                break; 
                            }
                        }
                    }
                }
            }
        }
        
        if(schemaDef != null) {
            schema.setSchemaDefinition(schemaDef);
        } else {
            schema.setReferenceable(false);
        }
        
        return schema;
    }
    
    protected void parseExtensionAttributes(XMLElement extEl, 
                                            Class wsdlClass, 
                                            WSDLElement wsdlObj,
                                            DescriptionElement desc)
                                            throws WSDLException
    {
    	Element domEl = (Element)extEl.getSource();
        NamedNodeMap nodeMap = domEl.getAttributes();
        int length = nodeMap.getLength();
        
        for (int i = 0; i < length; i++)
        {
            Attr domAttr = (Attr)nodeMap.item(i);
            String localName = domAttr.getLocalName();
            String namespaceURI = domAttr.getNamespaceURI();
            String prefix = domAttr.getPrefix();
            QName attrType = new QName(namespaceURI, localName, (prefix != null ? prefix : emptyString));
            String attrValue = domAttr.getValue();
                    
            if (namespaceURI != null && !namespaceURI.equals(Constants.NS_STRING_WSDL20))
            {
                if (!namespaceURI.equals(Constants.NS_STRING_XMLNS) && 
                    !namespaceURI.equals(Constants.NS_STRING_XSI))  //TODO handle xsi attrs elsewhere, without need to register
                {
                    //TODO reg namespaces at appropriate element scope, not just at desc.
                    //DOMUtils.registerUniquePrefix(prefix, namespaceURI, desc);
                    
                    ExtensionRegistry extReg = fWsdlContext.extensionRegistry;
                    XMLAttr xmlAttr = extReg.createExtAttribute(wsdlClass, attrType, extEl, attrValue);
                    if(xmlAttr != null) //TODO use an 'UnknownAttr' class in place of null
                    {
                        wsdlObj.setExtensionAttribute(attrType, xmlAttr);
                    }
                }
                else
                {
                    //TODO parse xmlns namespace declarations - here or elsewhere?
                }
            }
            else
            {
                //TODO confirm non-native attrs in WSDL 2.0 namespace will be detected by schema validation,
                //so no need to handle error here.
            }
        }
        
    }
    

    /* ************************************************************
     *  Utility/helper methods
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.internal.BaseWSDLReader#createXMLElement(java.lang.Object)
     */
    protected XMLElement createXMLElement(Object elem) {
        DOMXMLElement domXMLElement = new DOMXMLElement(getErrorReporter());
        domXMLElement.setSource(elem);
        return domXMLElement;
    }
    
    //TODO when refactoring DOMWSDLReader into BaseWSDLReader, make this method abstract in BaseWSDLReader
    //and keep this concrete implementation in DOMWSDLReader.
    protected void parseNamespaceDeclarations(XMLElement xmlElem, WSDLElement wsdlElem) 
        throws WSDLException {

    	Element elem = (Element)xmlElem.getSource();
    	    	
        NamedNodeMap attrs = elem.getAttributes();
        int size = attrs.getLength();

        for (int i = 0; i < size; i++)
        {
          Attr attr = (Attr)attrs.item(i);
          String namespaceURI = attr.getNamespaceURI();
          String localPart = attr.getLocalName();
          String value = attr.getValue();

          if ((Constants.NS_STRING_XMLNS).equals(namespaceURI))
          {
            if (!(Constants.ATTR_XMLNS).equals(localPart))
            {
              wsdlElem.addNamespace(localPart, getURI(value));  //a prefixed namespace
            }
            else
            {
              wsdlElem.addNamespace(null, getURI(value));       //the default namespace
            }
          }
        }
    }

    protected void parseSchemaForXMLSchema(DescriptionElement desc) throws WSDLException {
    	
        // Parse the schema for schema to include the built in schema types in the Woden model.
        // TODO: As there are a finite number of built in schema types it may be better to create
        // constants rather than reading the schema for schema on the creation of every model. 
        // Also, this method currently requires that the schema elements exist in the types element.
        // This may not be the best idea as it may imply that this schema contains an actual import
        // statement in a WSDL 2.0 document. This method also does not work for when building the
        // model programmatically.
        // This method should be reevaluated at a later point.
        TypesElement types = desc.getTypesElement();
        if (types == null) {
            types = desc.addTypesElement();
        }
        if (types.getTypeSystem() == null)
        {
          types.setTypeSystem(Constants.TYPE_XSD_2001);
        }

        try
        {
          Document schemaDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
          Element schemaElem = schemaDoc.
              createElementNS(SchemaConstants.NS_STRING_SCHEMA, SchemaConstants.ELEM_IMPORT);
          schemaElem.setAttribute(SchemaConstants.ATTR_NAMESPACE, SchemaConstants.NS_STRING_SCHEMA);
          schemaElem.setAttribute(SchemaConstants.ATTR_SCHEMA_LOCATION, resolveURI("http://www.w3.org/2001/XMLSchema.xsd"));
          
          XMLElement xmlEl = createXMLElement(schemaElem);
          desc.getTypesElement().addSchema(parseSchemaImport(xmlEl, desc));
        }
        catch(Exception e)
        {
          logger.error("A problem was encountered while creating the build in XML schema types: " + e);
        }
    }
    
    private XmlSchema retrieveSchema(XMLElement contextElement, URI contextURI, String schemaSpec) 
    throws WSDLException {
        
        Document importedSchemaDoc = null;
        Element schemaEl = null;
        String schemaLoc = null;
        URL url = null;
        
        try 
        {
            /*
             * For simple resolvers, we resolve the parent (Description) URI
             * to be used as the context. This allows for relative locationURIs
             * to be resolved implicitly - they are considered to be located 
             * relative to the resolved parent. Therefore, relative URIs such as these
             * need not be listed in the catalog file.
             */
            
            /* TODO
             * OASIS-style catalogs have a convenience notation to define root URIs
             * thus grouping related URLs together. In this case the context URI here
             * should be left alone, but the resultant locationURL resolved instead.
             * 
             * Implement a boolean system property like org.apache.woden.resolver.useRelativeURLs
             * (set by the resolver ctor). SimpleURIResolver (et al) should set this to true,
             * OASISCatalogResolver should set to false. 
             */
            URL contextURL = (contextURI != null) ? contextURI.toURL() : null;
            url = StringUtils.getURL(contextURL, schemaSpec);
                    
        } catch (MalformedURLException e) {
 
            String baseLoc = contextURI != null ? contextURI.toString() : null;
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL502", 
                    new Object[] {baseLoc, schemaLoc}, 
                    ErrorReporter.SEVERITY_ERROR);
            //can't continue schema retrieval with a bad URL.
            return null;
        }
        
        String schemaURL = url.toString();
        
        //If the schema has already been imported, reuse it.
        XmlSchema schemaDef = (XmlSchema)fImportedSchemas.get(schemaURL); 
        
        if(schemaDef == null)
        {
            //not previously imported, so retrieve it now.
            String resolvedLoc = null;
            try {
                URI resolvedURI = resolveURI(getURI(schemaURL));
                resolvedLoc = resolvedURI.toString();
                importedSchemaDoc = getDocument(new InputSource(resolvedLoc), resolvedLoc);
                
            } catch (IOException e4) {
                
                //schema retrieval failed (e.g. 'not found')
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL504", 
                        new Object[] {schemaURL}, 
                        ErrorReporter.SEVERITY_WARNING, 
                        e4);
                //cannot continue without resolving the URL
                return null;
            }
            
            schemaEl = importedSchemaDoc.getDocumentElement();
            
            try {
                //String baseLoc = contextURI != null ? contextURI.toString() : null;
                String baseLoc = resolvedLoc;
                XmlSchemaCollection xsc = new XmlSchemaCollection();
                xsc.setBaseUri(resolvedLoc);
                
                // Plug in the selected woden URI Resolver
                xsc.setSchemaResolver(new DOMSchemaResolverAdapter(getURIResolver(), contextElement));   
                
                schemaDef = xsc.read(schemaEl, baseLoc);
                fImportedSchemas.put(schemaURL, schemaDef);
            } 
            catch (XmlSchemaException e) 
            {
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL522", 
                        new Object[] {schemaURL}, 
                        ErrorReporter.SEVERITY_WARNING,
                        e);
            }
        } 
        
        return schemaDef;
    }
    
    // replaced with JAXP API
    /*
    private Document getDocument(InputSource inputSource, String desc) throws WSDLException,
            IOException {
        //TODO use 'desc' URL in any error message(s) for problem resolution.

        //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //
        //factory.setNamespaceAware(true);

        DOMParser parser = new DOMParser();
        parser.setEntityResolver(new EntityResolverAdapter(getURIResolver()));

        try {
            parser.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX
                    + org.apache.xerces.impl.Constants.NAMESPACES_FEATURE, true);
            parser.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX
                    + org.apache.xerces.impl.Constants.NAMESPACE_PREFIXES_FEATURE, true);
        } catch (SAXNotRecognizedException e) {

        } catch (SAXNotSupportedException e) {

        }

        // Enable validation on the XML parser if it has been enabled 
        // for the Woden parser.
        if (features.getValue(WSDLReader.FEATURE_VALIDATION)) {
            //factory.setValidating(true);
            try {
                parser.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX
                        + org.apache.xerces.impl.Constants.VALIDATION_FEATURE, true);
                parser.setFeature(org.apache.xerces.impl.Constants.XERCES_FEATURE_PREFIX
                        + org.apache.xerces.impl.Constants.SCHEMA_VALIDATION_FEATURE, true);
                // TODO: This external schema location should be removed once an URI resolution framework
                // with a catalog is added to Woden.

                parser
                        .setProperty(
                                org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX
                                        + org.apache.xerces.impl.Constants.SCHEMA_LOCATION,
                                "http://www.w3.org/ns/wsdl "
                                        + resolveURI("http://www.w3.org/2007/03/wsdl/wsdl20.xsd")
                                        + " http://www.w3.org/ns/wsdl-extensions "
                                        + resolveURI("http://www.w3.org/2007/03/wsdl/wsdl20-extensions.xsd")
                                        + " http://www.w3.org/2001/XMLSchema "
                                        + resolveURI("http://www.w3.org/2001/XMLSchema.xsd"));
            } catch (SAXNotRecognizedException e) {
                 logger.error("validation not supported by parser.");
            } catch (SAXNotSupportedException e) {

            }
        } else {
            //factory.setValidating(false);
        }

        Document doc = null;

        try {

            //DocumentBuilder builder = factory.newDocumentBuilder();
            //builder.getDOMImplementation().hasFeature();
            //builder.setErrorHandler(new ErrorHandlerWrapper(getErrorReporter()));
            //builder.setEntityResolver(new DefaultHandler());
            //doc = builder.parse(inputSource);
            parser.parse(inputSource);
            doc = parser.getDocument();

        }
        //catch (ParserConfigurationException e) 
        //{
        //String msg = getErrorReporter().getFormattedMessage("WSDL002", new Object[] {"XML"});
        //throw new WSDLException(WSDLException.CONFIGURATION_ERROR, msg, e);
        //} 
        catch (SAXException e) {
            getErrorReporter().reportError(new ErrorLocatorImpl(), //TODO line&col nos.
                    "WSDL500", new Object[] { "SAX", desc }, ErrorReporter.SEVERITY_FATAL_ERROR, e);
        }

        //TODO - potentially returns null. correct after deciding how 
        //to handle exceptions (e.g. return inside try block).
        return doc;
    }
    
    */
    
    private Document getDocument(InputSource inputSource, String desc) throws WSDLException,
            IOException {
        //TODO use 'desc' URL in any error message(s) for problem resolution.             
        Document doc = null;
        try {
            DocumentBuilderFactory factory = createDocumentBuilderFactory(true);
            EntityResolverAdapter entityResolver = new EntityResolverAdapter(getURIResolver());
            ErrorHandler errorHandler = new ErrorHandlerWrapper(getErrorReporter());
            DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
            doc = builder.parse(inputSource);

        } catch (ParserConfigurationException e) {
            String msg = getErrorReporter().getFormattedMessage("WSDL002", new Object[] { "XML" });
            throw new WSDLException(WSDLException.CONFIGURATION_ERROR, msg, e);
        } catch (SAXException e) {
            getErrorReporter().reportError(new ErrorLocatorImpl(), //TODO line&col nos.
                    "WSDL500", new Object[] { "SAX", desc }, ErrorReporter.SEVERITY_FATAL_ERROR, e);
        }

        //TODO - potentially returns null. correct after deciding how 
        //to handle exceptions (e.g. return inside try block).
        return doc;
    }

    /*
     * Retrieve a WSDL document by resolving the location URI specified 
     * on a WSDL &lt;import&gt; or &lt;include&gt; element.
     * 
     * TODO add support for a URL Catalog Resolver
     */
    protected DescriptionElement getWSDLFromLocation(String locationURI,
                                               DescriptionElement desc,
                                               Map wsdlModules)
                                               throws WSDLException
    {
        DescriptionElement referencedDesc = null;
        Element docEl;
        URL locationURL = null;
        URI contextURI = null;
        
        try 
        {         
        	/*
        	 * For simple resolvers, we resolve the parent (Description) URI
        	 * to be used as the context. This allows for relative locationURIs
        	 * to be resolved implicitly - they are considered to be located 
        	 * relative to the resolved parent. Therefore, relative URIs such as these
        	 * need not be listed in the catalog file.
        	 */
        	
        	/* TODO
        	 * OASIS-style catalogs have a convenience notation to define root URIs
        	 * thus grouping related URLs together. In this case the context URI here
        	 * should be left alone, but the resultant locationURL resolved instead.
        	 * 
        	 * Implement a boolean system property like org.apache.woden.resolver.useRelativeURLs
        	 * (set by the resolver ctor). SimpleURIResolver (et al) should set this to true,
        	 * OASISCatalogResolver should set to false. 
        	 */
        	// contextURI = desc.getDocumentBaseURI();
        	contextURI = resolveURI(desc.getDocumentBaseURI());
            URL contextURL = (contextURI != null) ? contextURI.toURL() : null;
            locationURL = StringUtils.getURL(contextURL, locationURI);
        } 
        catch (MalformedURLException e) 
        {
            String baseURI = contextURI != null ? contextURI.toString() : null;
                    
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL502", 
                    new Object[] {baseURI, locationURI}, 
                    ErrorReporter.SEVERITY_ERROR);
            
            //can't continue import with a bad URL.
            return null;
        }
        
        String locationStr = locationURL.toString();

        //Check if WSDL imported or included previously from this location.
        referencedDesc = (DescriptionElement)wsdlModules.get(locationStr);
        
        if(referencedDesc == null)
        {
            //not previously imported or included, so retrieve the WSDL.
            try {
                Document doc = getDocument(
                        new InputSource(locationStr), locationStr);
                docEl = doc.getDocumentElement();
            } 
            catch (IOException e) 
            {
                //document retrieval failed (e.g. 'not found')
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL503", 
                        new Object[] {locationStr}, 
                        ErrorReporter.SEVERITY_WARNING, 
                        e);
                
                //cannot continue without the referenced document
                return null;
            }
            
            //The referenced document should contain a WSDL <description>
            QName docElQN = new QName(docEl.getNamespaceURI(), docEl.getLocalName());
            
            if(!Constants.Q_ELEM_DESCRIPTION.equals(docElQN))
            {
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL501", 
                        new Object[] {Constants.Q_ELEM_DESCRIPTION, docElQN},
                        ErrorReporter.SEVERITY_ERROR);
                
                //cannot continue without a <description> element
                return null;
            }
            
            XMLElement descEl = createXMLElement(docEl);
            
            referencedDesc = parseDescription(locationStr,
            		                          descEl,
                                              wsdlModules);
            
            if(!wsdlModules.containsKey(locationStr))
            {
                wsdlModules.put(locationStr, referencedDesc);
            }
        }
            
        return referencedDesc;
    }
    
    /**
     * Create the JAXP DocumentBuilderFactory instance.Use JAXP 1.2 API for validation.     
     * @param namespaceAware whether the returned factory is to provide support for XML namespaces
     * @return the JAXP DocumentBuilderFactory
     * @throws ParserConfigurationException if we failed to build a proper DocumentBuilderFactory
     */
    protected DocumentBuilderFactory createDocumentBuilderFactory(boolean namespaceAware)
    throws ParserConfigurationException, WSDLException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);
        
        // Enable validation on the XML parser if it has been enabled 
        // for the Woden parser.
        if (features.getValue(WSDLReader.FEATURE_VALIDATION)) {
           factory.setValidating(true);
            // Enforce namespace aware for XSD...
            factory.setNamespaceAware(true);
            try {
                factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                factory.setAttribute(JAXP_SCHEMA_SOURCE, schemas);
                
            } catch (IllegalArgumentException e) {                
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL515", 
                        new Object[] {factory.getClass().getName()}, 
                        ErrorReporter.SEVERITY_FATAL_ERROR, 
                        e);
                }
        }else{
            factory.setValidating(false);
        }

        return factory;
    }
    
    /**
     * Create a JAXP DocumentBuilder will use for parsing XML documents. 
     * @param factory the JAXP DocumentBuilderFactory that the DocumentBuilder
     * should be created with
     * @param entityResolver the SAX EntityResolver to use
     * @param errorHandler the SAX ErrorHandler to use
     * @return the JAXP DocumentBuilder
     * @throws ParserConfigurationException if thrown by JAXP methods
     */
    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory,
            EntityResolver entityResolver, ErrorHandler errorHandler)
            throws ParserConfigurationException {

        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        if (entityResolver != null) {
            docBuilder.setEntityResolver(entityResolver);
        }
        if (errorHandler != null) {
            docBuilder.setErrorHandler(errorHandler);
        }
        return docBuilder;
    }
    
    /**
     * A wrapper that plugs Woden's error reporter mechanism into the
     * XML parser used to parse the WSDL document.
     */
    class ErrorHandlerWrapper implements org.xml.sax.ErrorHandler
    {
    	/**
    	 * The error reporter used to report errors in Woden.
    	 */
    	private ErrorReporter errorReporter;
    	
    	/**
    	 * Constructor.
    	 * 
    	 * @param errorReporter The error reporter to be wrapped.
    	 */
    	public ErrorHandlerWrapper(ErrorReporter errorReporter)
    	{
    		this.errorReporter = errorReporter;
    	}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
		 */
		public void error(SAXParseException error) throws SAXException 
		{
			ErrorLocatorImpl locator = new ErrorLocatorImpl();
			locator.setLineNumber(error.getLineNumber());
			locator.setColumnNumber(error.getColumnNumber());
			try
			{
			    errorReporter.reportError(locator, null, error.getMessage(), ErrorReporter.SEVERITY_ERROR, error.getException());
			}
			catch(WSDLException e)
			{
				throw new SAXException("A problem occurred setting the error in the Woden error reporter wrapper.", e);
			}
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
		 */
		public void fatalError(SAXParseException error) throws SAXException 
		{
			ErrorLocatorImpl locator = new ErrorLocatorImpl();
			locator.setLineNumber(error.getLineNumber());
			locator.setColumnNumber(error.getColumnNumber());
			try
			{
			    errorReporter.reportError(locator, null, error.getMessage(), ErrorReporter.SEVERITY_FATAL_ERROR, error.getException());
			}
			catch(WSDLException e)
			{
				throw new SAXException("A problem occurred setting the error in the Woden error reporter wrapper.", e);
			}
			
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
		 */
		public void warning(SAXParseException warning) throws SAXException 
		{
			ErrorLocatorImpl locator = new ErrorLocatorImpl();
			locator.setLineNumber(warning.getLineNumber());
			locator.setColumnNumber(warning.getColumnNumber());
			try
			{
			    errorReporter.reportError(locator, null, warning.getMessage(), ErrorReporter.SEVERITY_WARNING, warning.getException());
			}
			catch(WSDLException e)
			{
				throw new SAXException("A problem occurred setting the error in the Woden error reporter wrapper.", e);
			}
			
		}


    	
    }
    
    class WSDLEntityResolver implements org.xml.sax.EntityResolver
    {

		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			// TODO Auto-generated method stub
			
			return null;
		}
    	
    }
    
}
