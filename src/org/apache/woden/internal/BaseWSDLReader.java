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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.resolver.SimpleURIResolver;
import org.apache.woden.internal.schema.SchemaConstants;
import org.apache.woden.internal.util.PropertyUtils;
import org.apache.woden.internal.util.StringUtils;
import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.resolver.URIResolver;
import org.apache.woden.schema.Schema;
import org.apache.woden.types.NCName;
import org.apache.woden.types.QNameTokenUnion;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.enumeration.MessageLabel;
import org.apache.woden.wsdl20.extensions.ExtensionDeserializer;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.BindingFaultReferenceElement;
import org.apache.woden.wsdl20.xml.BindingMessageReferenceElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.DocumentableElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.EndpointElement;
import org.apache.woden.wsdl20.xml.ImportElement;
import org.apache.woden.wsdl20.xml.IncludeElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.ServiceElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.woden.wsdl20.xml.WSDLElement;


/**
 * This abstract class contains properties and methods common 
 * to WSDLReader implementations.
 * 
 * <p>
 * TODO a Template Inheritance pattern that ensures WSDL validation gets invoked 
 * (if turned on by some property) after the subclass has parsed the WSDL. Note, 
 * this class is currently WSDL version-independent and XML parser-independent;
 * should try to keep it that way. 
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public abstract class BaseWSDLReader implements WSDLReader {
	
    private final String DEFAULT_RESOLVER_PROPERTY="org.apache.woden.resolver.default";
    
    private String fFactoryImplName = null; //TODO deprecate/remove?
    
    protected WSDLContext fWsdlContext;
    final protected ReaderFeatures features;

    protected BaseWSDLReader(WSDLContext wsdlContext) throws WSDLException {
        //TODO decide what to do with fact impl name...re- only known use case is to change newDescription factory method
        fFactoryImplName = wsdlContext.wsdlFactory.getClass().getName();
        features = new ReaderFeatures();

        /* Establish the default URIResolver.
         * 
         * Find the class representing the default:
         * Search for a property setting for the default URI resolver.
         * The property can be set via:
         * 1. JVM system properties (e.g. java -D arguments)
         * 		use -Dorg.apache.woden.resolver.default=<fully qualified class name> on the command line
         * 		where RHS is the name of the chosen default URIResolver implementation.
         * 2. application properties defined in /META-INF/services (e.g. in a jar file)
         *      (when implemented in PropertyUtils.findProperty(String p)...)
         * 3. properties defined in a wsdl properties file located in JAVAHOME/lib directory
         */
       
        String defaultURIResolver = PropertyUtils.findProperty(DEFAULT_RESOLVER_PROPERTY);
        URIResolver resolver;
        if (defaultURIResolver == null)
        {
        	// property not set (an allowable condition)
        	// use the "default default" URI resolver
        	resolver = new SimpleURIResolver();
        }
        else
        {
	        try 
	        {
	        	Class resolverClass = Class.forName(defaultURIResolver);
	        	resolver = (URIResolver) resolverClass.newInstance();
	        } 
	        catch (Exception e)
	        {
                /*
                Catches and wraps:
                ClassNotFoundException
                InstantiationException
                IllegalAccessException
                */
               
               throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                       "Problem instantiating a URIResolver implementation " +
                       "using classname '" + defaultURIResolver + "' ",
                       e);
           }
        }
        
        fWsdlContext = new WSDLContext(
                wsdlContext.wsdlFactory,
                wsdlContext.errorReporter,
                wsdlContext.extensionRegistry,
                resolver);
    }
    
    /* ************************************************************
     *  API public methods
     * ************************************************************/
    
    /**
     * @return Returns the fErrorReporter.
     */
    public ErrorReporter getErrorReporter() 
    {
        return fWsdlContext.errorReporter;
    }
    
    /**
     * Get the cached WSDLFactory if there is one, otherwise
     * create and cache a new one.
     * 
     * TODO see setFactoryImplName todo
     * 
     * @return Returns a.
     */
    protected WSDLFactory getFactory() throws WSDLException {
        return fWsdlContext.wsdlFactory;
    }

    /**
     * Stores the name of the WSDLFactory implementation class to be used for
     * any subsequent WSDLFactory requests, first discarding any cached factory 
     * object.
     * 
     * TODO the use case is changing the factory that creates Description objects. Decide if/how this works with WSDLContext.
     * 
     * @param factoryImplName the WSDLFactory implementation classname
     */
    public void setFactoryImplName(String factoryImplName) {
        
        //fFactory = null;
        
        fFactoryImplName = factoryImplName;
    }
    
    /**
     * @return the WSDLFactory implementation classname
     */
    public String getFactoryImplName() {
        return fFactoryImplName;
    }
    
    public void setExtensionRegistry(ExtensionRegistry extReg)
    {
        if(extReg == null) {
            String msg = fWsdlContext.errorReporter.getFormattedMessage(
                    "WSDL014", null);
            throw new NullPointerException(msg);
        }
        
        fWsdlContext = new WSDLContext(
                fWsdlContext.wsdlFactory,
                fWsdlContext.errorReporter,
                extReg,
                fWsdlContext.uriResolver);
    }
    
    public ExtensionRegistry getExtensionRegistry() {
        return fWsdlContext.extensionRegistry;    
    }
    
    /**
     * Set a named feature on or off with a boolean.
     * <p>
     * All feature names should be fully-qualified, Java package style to 
     * avoid name clashes. All names starting with org.apache.woden. are 
     * reserved for features defined by the Woden implementation. 
     * Features specific to other implementations should be fully-qualified  
     * to match the package name structure of that implementation. 
     * For example: com.abc.featureName
     * 
     * @param name the name of the feature to be set
     * @param value a boolean value where true sets the feature on, false sets it off
     * @throws IllegalArgumentException if the feature name is not recognized.
     */
    public void setFeature(String name, boolean value) 
    {
        if(name == null) 
        {
            //name must not be null
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL005", null));
        }
        try
        {
        	features.setValue(name, value);
        }
        catch(IllegalArgumentException e)
        {
        	// Feature name is not recognized, so throw an exception.
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL006", new Object[] {name}));
        }
    }

    /**
     * Returns the on/off setting of the named feature, represented as a boolean. 
     * 
     * @param name the name of the feature to get the value of
     * @return a boolean representing the on/off state of the named feature
     * @throws IllegalArgumentException if the feature name is not recognized.
     */
    public boolean getFeature(String name)
    {
        if(name == null) 
        {
            //name must not be null
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL005", null));
        }
        
        try
        {
        	return features.getValue(name);
        }
        catch(IllegalArgumentException e)
        {
        	// Feature name is not recognized, so throw an exception.
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL006", new Object[] {name}));
        }
    }
    
    /**
     * Set a named property to the specified object.
     * <p>
     * All property names should be fully-qualified, Java package style to 
     * avoid name clashes. All names starting with org.apache.woden. are 
     * reserved for properties defined by the Woden implementation. 
     * Properties specific to other implementations should be fully-qualified  
     * to match the package name structure of that implementation. 
     * For example: com.abc.propertyName
     * 
     * @param name the name of the property to be set
     * @param value an Object representing the value to set the property to
     * @throws IllegalArgumentException if the property name is not recognized.
     */
    public void setProperty(String name, Object value) 
    {
        if(name == null) 
        {
            //name must not be null
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL007", null));
        }
        else if(name.equals("xyz"))
        {
            //TODO determine the required properties and
            //create an if block for each one to set the value.
        }
        else
        {
            //property name is not recognized, so throw an exception
            Object[] args = new Object[] {name};
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL008", args));
        }
    }

    /**
     * Returns the value of the named property.
     * 
     * @param name the name of the property to get the value of
     * @return an Object representing the property's value
     * @throws IllegalArgumentException if the property name is not recognized.
     */
    public Object getProperty(String name)
    {
        if(name == null) 
        {
            //name must not be null
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL007", null));
        }
        
        //Return the property's value or throw an exception if the property
        //name is not recognized
        
        if(name.equals("xyz"))
        {
            //TODO determine the required properties and
            //create an if block for each one to get the value.
            return null;
        }
        else
        {
            //property name is not recognized, so throw an exception
            throw new IllegalArgumentException(
                    fWsdlContext.errorReporter.getFormattedMessage("WSDL008", new Object[] {name}));
        }
    }

    /* ************************************************************
     *  Parsing methods - e.g. parseXXXX()
     * ************************************************************/

    /* Parse the attributes and child elements of the <description> element.
     * As per the WSDL 2.0 spec, the child elements must be in the 
     * following order if present:
     * <documentation>
     * <import> <include> or WSDL extension elements in any order
     * <types>
     * <interface> <binding> <service> or WSDL extension elements in any order.
     * TODO validate that the elements are in correct order
     */ 
    protected DescriptionElement parseDescription(
            String documentBaseURI, 
            XMLElement descEl, 
            Map wsdlModules) 
            throws WSDLException {

         if(Constants.Q_ELEM_DEFINITIONS.equals(descEl.getQName())){
            //To identify wsdl1.1 documents
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL514", 
                    new Object[] {Constants.Q_ELEM_DEFINITIONS, descEl.getQName()},
                    ErrorReporter.SEVERITY_FATAL_ERROR);
        }
		if (!Constants.Q_ELEM_DESCRIPTION.equals(descEl.getQName()))
        {
            getErrorReporter().reportError(
                new ErrorLocatorImpl(),  //TODO line&col nos.
                "WSDL501", 
                new Object[] {Constants.Q_ELEM_DESCRIPTION, descEl.getQName()},
                ErrorReporter.SEVERITY_FATAL_ERROR);
        }
        
        //Get a new description in the context of this reader 
        DescriptionElement desc = 
            ((BaseWSDLFactory)getFactory()).newDescriptionElement(fWsdlContext);
        
        if(wsdlModules == null) 
        {
            //This is the initial WSDL document. No imports or includes yet.
            //TODO this might be the place to flag the initial Desc if necessary.
            wsdlModules = new HashMap();
        }
        
        desc.setDocumentBaseURI(getURI(documentBaseURI));

        String targetNamespace = 
            descEl.getAttributeValue(Constants.ATTR_TARGET_NAMESPACE);
        
        if(targetNamespace != null)
        {
            desc.setTargetNamespace(getURI(targetNamespace));
        }
        
        parseNamespaceDeclarations(descEl, desc);
        
        parseExtensionAttributes(descEl, DescriptionElement.class, desc, desc);
        
        //parse the child elements
        XMLElement[] children = descEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();
        	
            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, desc);
            }
            else if (Constants.Q_ELEM_IMPORT.equals(tempElQN))
            {
                if(documentBaseURI != null && !wsdlModules.containsKey(documentBaseURI)) 
                {
                    wsdlModules.put(documentBaseURI, desc);
                }
                parseImport(tempEl, desc, wsdlModules);
            }
            else if (Constants.Q_ELEM_INCLUDE.equals(tempElQN))
            {
                if(documentBaseURI != null && !wsdlModules.containsKey(documentBaseURI)) 
                {
                    wsdlModules.put(documentBaseURI, desc);
                }
                parseInclude(tempEl, desc, wsdlModules);
            }
            else if (Constants.Q_ELEM_TYPES.equals(tempElQN))
            {
                parseTypes(tempEl, desc);
            }
            else if (Constants.Q_ELEM_INTERFACE.equals(tempElQN))
            {
                parseInterface(tempEl, desc);
            }
            else if (Constants.Q_ELEM_BINDING.equals(tempElQN))
            {
                parseBinding(tempEl, desc);
            }
            else if (Constants.Q_ELEM_SERVICE.equals(tempElQN))
            {
                parseService(tempEl, desc);
            }
            else
            {
                desc.addExtensionElement(
                    parseExtensionElement(DescriptionElement.class, desc, tempEl, desc) );
            }
        }
        
        parseSchemaForXMLSchema(desc);  //TODO temporary - see comments within the method
        
        return desc;
    }

    protected DocumentationElement parseDocumentation(
            XMLElement docEl, 
            DescriptionElement desc, 
            DocumentableElement parent) 
            throws WSDLException {

        DocumentationElement documentation = parent.addDocumentationElement();

        //TODO store docEl as below, or just extract any text? 
        documentation.setContent(docEl);

        //Now parse any extensibility attributes or elements

        parseExtensionAttributes(docEl, DocumentationElement.class, documentation, desc);

        XMLElement[] children = docEl.getChildElements();
        XMLElement tempEl = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            documentation.addExtensionElement(
                    parseExtensionElement(DocumentationElement.class, documentation, tempEl, desc) );
        }

        return documentation;
    }

    private ImportElement parseImport(
            XMLElement importEl,
            DescriptionElement desc,
            Map wsdlModules) 
            throws WSDLException {

        ImportElement imp = desc.addImportElement();

        String namespaceURI = importEl.getAttributeValue(Constants.ATTR_NAMESPACE);
        String locationURI = importEl.getAttributeValue(Constants.ATTR_LOCATION);

        parseExtensionAttributes(importEl, ImportElement.class, imp, desc);

        if(namespaceURI != null) 
        {
            //TODO handle missing namespace attribute (REQUIRED attr)
            imp.setNamespace(getURI(namespaceURI));
        }

        if(locationURI != null)
        {
            //TODO handle missing locationURI (OPTIONAL attr)
            URI resolvedLocationURI = resolveURI(getURI(locationURI));
            imp.setLocation(resolvedLocationURI);
            DescriptionElement importedDesc = 
                getWSDLFromLocation(resolvedLocationURI.toString(), desc, wsdlModules);
            imp.setDescriptionElement(importedDesc);
        }

        return imp;
    }

    private IncludeElement parseInclude(
            XMLElement includeEl,
            DescriptionElement desc,
            Map wsdlModules) 
            throws WSDLException {

        IncludeElement include = desc.addIncludeElement();

        String locationURI = includeEl.getAttributeValue(Constants.ATTR_LOCATION);

        parseExtensionAttributes(includeEl, IncludeElement.class, include, desc);

        if(locationURI != null)
        {
            URI resolvedLocationURI = resolveURI(getURI(locationURI));
            include.setLocation(resolvedLocationURI);
            DescriptionElement includedDesc = 
                getWSDLFromLocation(resolvedLocationURI.toString(), desc, wsdlModules);
            include.setDescriptionElement(includedDesc);
        }

        return include;
    }

    /*
     * TODO Initial schema parsing is specific to XML Schema. 
     * Need generic support for other type systems.
     * Consider extension architecture with serializer/deserializer.
     */
    private TypesElement parseTypes(
            XMLElement typesEl,
            DescriptionElement desc) 
            throws WSDLException {

        TypesElement types = desc.addTypesElement();

        //TODO for now set to W3 XML Schema. Later, add support for non-XML Schema type systems
        types.setTypeSystem(Constants.TYPE_XSD_2001);

        parseExtensionAttributes(typesEl, TypesElement.class, types, desc);

        XMLElement[] children = typesEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            //TODO validate element order? <documentation> must be first.

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, types);
            }
            else if (SchemaConstants.Q_ELEM_SCHEMA_IMPORT.equals(tempElQN))
            {
                types.addSchema(parseSchemaImport(tempEl, desc));
            }
            else if (SchemaConstants.Q_ELEM_SCHEMA.equals(tempElQN))
            {
                types.addSchema(parseSchemaInline(tempEl, desc));
            }
            else
            {
                types.addExtensionElement(
                        parseExtensionElement(TypesElement.class, types, tempEl, desc) );
            }
        }

        return types;
    }

    protected abstract Schema parseSchemaImport(
            XMLElement importEl,
            DescriptionElement desc) 
            throws WSDLException;

    protected abstract Schema parseSchemaInline(
            XMLElement schemaEl,
            DescriptionElement desc) 
            throws WSDLException;

    private InterfaceElement parseInterface(
            XMLElement interfaceEl,
            DescriptionElement desc) 
            throws WSDLException {
        
        InterfaceElement intface = desc.addInterfaceElement();

        String name = interfaceEl.getAttributeValue(Constants.ATTR_NAME);

        if(name != null)
        {
            intface.setName(new NCName(name));
        }

        String styleDefault = interfaceEl.getAttributeValue(Constants.ATTR_STYLE_DEFAULT);
        if(styleDefault != null)
        {
            List stringList = StringUtils.parseNMTokens(styleDefault);
            String uriString = null;
            Iterator it = stringList.iterator();
            while(it.hasNext())
            {
                uriString = (String)it.next();
                intface.addStyleDefaultURI(getURI(uriString));
            }
        }

        String extendsAtt = interfaceEl.getAttributeValue(Constants.ATTR_EXTENDS);
        if(extendsAtt != null)
        {
            List stringList = StringUtils.parseNMTokens(extendsAtt);
            String qnString = null;
            Iterator it = stringList.iterator();
            while(it.hasNext())
            {
                qnString = (String)it.next();
                intface.addExtendedInterfaceName(interfaceEl.getQName(qnString));
            }
        }

        parseExtensionAttributes(interfaceEl, InterfaceElement.class, intface, desc);

        /* Parse the child elements of <interface>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation>
         * <fault> <operation> or extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = interfaceEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, intface);
            }
            else if (Constants.Q_ELEM_FAULT.equals(tempElQN))
            {
                parseInterfaceFault(tempEl, desc, intface);
            }
            else if (Constants.Q_ELEM_OPERATION.equals(tempElQN))
            {
                parseInterfaceOperation(tempEl, desc, intface);
            }
            else
            {
                intface.addExtensionElement(
                        parseExtensionElement(InterfaceElement.class, intface, tempEl, desc) );
            }
        }

        return intface;
    }

    /* Parse the attributes and child elements of interface <fault>. 
     * As per WSDL 2.0 spec, the child elements must be in the following order if present:
     * <documentation>
     * extension elements in any order
     * 
     * TODO validate that the elements are in correct order
     */ 
    private InterfaceFaultElement parseInterfaceFault(
            XMLElement faultEl,
            DescriptionElement desc,
            InterfaceElement parent) 
            throws WSDLException {

        InterfaceFaultElement fault = parent.addInterfaceFaultElement();

        String name = faultEl.getAttributeValue(Constants.ATTR_NAME);
        if(name != null)
        {
            fault.setName(new NCName(name));
        }

        String element = faultEl.getAttributeValue(Constants.ATTR_ELEMENT);
        if(element != null)
        {
            if(element.equals(Constants.NMTOKEN_ANY)) {
                fault.setElement(QNameTokenUnion.ANY);
            } else if(element.equals(Constants.NMTOKEN_NONE)) {
                fault.setElement(QNameTokenUnion.NONE);
            } else if(element.equals(Constants.NMTOKEN_OTHER)) {
                fault.setElement(QNameTokenUnion.OTHER);
            } else {
                //It is not one of the allowed xs:Token values, so it must be an xs:QName
                try {
                    QName qname = faultEl.getQName(element);
                    fault.setElement(new QNameTokenUnion(qname));
                } catch (WSDLException e) {
                    getErrorReporter().reportError( 
                            new ErrorLocatorImpl(),  //TODO line&col nos.
                            "WSDL505",
                            new Object[] {element, faultEl.getQName()},
                            ErrorReporter.SEVERITY_ERROR);
                }
            }
        }

        parseExtensionAttributes(faultEl, InterfaceFaultElement.class, fault, desc);

        XMLElement[] children = faultEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, fault);
            }
            else
            {
                fault.addExtensionElement(
                        parseExtensionElement(InterfaceFaultElement.class, fault, tempEl, desc) );
            }
        }

        return fault;
    }

    private InterfaceOperationElement parseInterfaceOperation(
            XMLElement operEl, 
            DescriptionElement desc,
            InterfaceElement parent) 
            throws WSDLException {

        InterfaceOperationElement oper = parent.addInterfaceOperationElement();

        String name = operEl.getAttributeValue(Constants.ATTR_NAME);
        if(name != null)
        {
            oper.setName(new NCName(name));
        }

        String style = operEl.getAttributeValue(Constants.ATTR_STYLE);
        if(style != null)
        {
            List stringList = StringUtils.parseNMTokens(style);
            String uriString = null;
            Iterator it = stringList.iterator();
            while(it.hasNext())
            {
                uriString = (String)it.next();
                oper.addStyleURI(getURI(uriString));
            }
        }

        String pat = operEl.getAttributeValue(Constants.ATTR_PATTERN);
        if(pat != null)
        {
            oper.setPattern(getURI(pat));
        }

        parseExtensionAttributes(operEl, InterfaceOperationElement.class, oper, desc);

        /* Parse the child elements of interface <operation>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * <input> <output> <infault> <outfault> or extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = operEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_INPUT.equals(tempElQN))
            {
                parseInterfaceMessageReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_OUTPUT.equals(tempElQN))
            {
                parseInterfaceMessageReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_INFAULT.equals(tempElQN))
            {
                parseInterfaceFaultReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_OUTFAULT.equals(tempElQN))
            {
                parseInterfaceFaultReference(tempEl, desc, oper);
            }
            else
            {
                oper.addExtensionElement(
                        parseExtensionElement(InterfaceOperationElement.class, oper, tempEl, desc) );
            }
        }

        return oper;
    }

    private InterfaceFaultReferenceElement parseInterfaceFaultReference(
            XMLElement faultRefEl,
            DescriptionElement desc,
            InterfaceOperationElement parent)
            throws WSDLException {

        InterfaceFaultReferenceElement faultRef = parent.addInterfaceFaultReferenceElement();

        if(Constants.ELEM_INFAULT.equals(faultRefEl.getLocalName())) {
            faultRef.setDirection(Direction.IN);
        } 
        else if(Constants.ELEM_OUTFAULT.equals(faultRefEl.getLocalName())){
            faultRef.setDirection(Direction.OUT);
        }

        String ref = faultRefEl.getAttributeValue(Constants.ATTR_REF);
        if(ref != null)
        {
            try {
                QName qname = faultRefEl.getQName(ref);
                faultRef.setRef(qname);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {ref, faultRefEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        String msgLabel = faultRefEl.getAttributeValue(Constants.ATTR_MESSAGE_LABEL);
        if(msgLabel != null)
        {
            faultRef.setMessageLabel(new NCName(msgLabel));
        }
        else
        {
            //This is a limited solution supporting the 3 MEPs in the Part 2 spec. 
            //TODO generic support for user-defined, extensible MEPs.
            
            InterfaceOperationElement iop = (InterfaceOperationElement)faultRef.getParentElement();
            URI mep = iop.getPattern();
            
            if(Constants.MEP_URI_IN_OUT.equals(mep))
            {
                //Ruleset is fault-replaces-message, so fault is in same direction as msg.
                //The <output> is replaced by an <outfault>.
                //The <input> is replaced by an <infault>.
                //The <outfault> msg label should match the <output> msg label.
                if(Constants.ELEM_OUTFAULT.equals(faultRefEl.getLocalName()))
                {
                    faultRef.setMessageLabel(MessageLabel.OUT);
                }
                else
                {
                    faultRef.setMessageLabel(MessageLabel.IN);
                }
            }
            else if(Constants.MEP_URI_ROBUST_IN_ONLY.equals(mep))  
            {
                //Ruleset is message-triggers-fault, so fault is opposite direction to msg.
                //The <input> can trigger an <outfault>. 
                //The <outfault> msg label should match the <input> msg label.
                if(Constants.ELEM_OUTFAULT.equals(faultRefEl.getLocalName()))
                {
                    faultRef.setMessageLabel(MessageLabel.IN); //the <outfault> is triggered by the <input>
                }
                else
                {
                    //TODO this MEP may have only <outfault>s, not <infault>s, so treat this as an error.
                    faultRef.setMessageLabel(MessageLabel.OUT);
                }
            }
            else if(Constants.MEP_URI_IN_ONLY.equals(mep))
            {
                //TODO Ruleset is no-faults, so treat this as an error.
            }
        }

        parseExtensionAttributes(faultRefEl, InterfaceFaultReferenceElement.class, faultRef, desc);

        XMLElement[] children = faultRefEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, faultRef);
            }
            else
            {
                faultRef.addExtensionElement(
                        parseExtensionElement(InterfaceFaultReferenceElement.class, faultRef, tempEl, desc) );
            }
        }

        return faultRef;
    }

    private InterfaceMessageReferenceElement parseInterfaceMessageReference(
            XMLElement msgRefEl,
            DescriptionElement desc,
            InterfaceOperationElement parent)
            throws WSDLException {

        InterfaceMessageReferenceElement message = parent.addInterfaceMessageReferenceElement();

        if(Constants.ELEM_INPUT.equals(msgRefEl.getLocalName())) {
            message.setDirection(Direction.IN);
        } 
        else if(Constants.ELEM_OUTPUT.equals(msgRefEl.getLocalName())) {
            message.setDirection(Direction.OUT);
        }

        String msgLabel = msgRefEl.getAttributeValue(Constants.ATTR_MESSAGE_LABEL);
        if(msgLabel != null) 
        {
            message.setMessageLabel(new NCName(msgLabel));
        }
        else
        {
            //This is a limited solution supporting the 3 MEPs in the Part 2 spec. 
            //TODO generic support for user-defined, extensible MEPs.
            if(message.getDirection().equals(Direction.IN))
            {
                message.setMessageLabel(MessageLabel.IN);
            }
            else
            {
                message.setMessageLabel(MessageLabel.OUT);
            }
        }

        String element = msgRefEl.getAttributeValue(Constants.ATTR_ELEMENT);
        if(element != null)
        {
            if(element.equals(Constants.NMTOKEN_ANY)) {
                message.setElement(QNameTokenUnion.ANY);
            } else if(element.equals(Constants.NMTOKEN_NONE)) {
                message.setElement(QNameTokenUnion.NONE);
            } else if(element.equals(Constants.NMTOKEN_OTHER)) {
                message.setElement(QNameTokenUnion.OTHER);
            } else {
                //It is not one of the allowed xs:Token values, so it must be an xs:QName
                try {
                    QNameTokenUnion qname = new QNameTokenUnion(msgRefEl.getQName(element));
                    message.setElement(qname);
                } catch (WSDLException e) {
                    getErrorReporter().reportError( 
                            new ErrorLocatorImpl(),  //TODO line&col nos.
                            "WSDL505",
                            new Object[] {element, msgRefEl.getQName()},
                            ErrorReporter.SEVERITY_ERROR);
                }
            }
        }

        parseExtensionAttributes(msgRefEl, InterfaceMessageReferenceElement.class, message, desc);

        XMLElement[] children = msgRefEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, message);
            }
            else
            {
                message.addExtensionElement(
                        parseExtensionElement(InterfaceMessageReferenceElement.class, message, tempEl, desc) );
            }
        }

        return message;
    }

    private BindingElement parseBinding(
            XMLElement bindEl,
            DescriptionElement desc)
            throws WSDLException {

        BindingElement binding = desc.addBindingElement();

        String name = bindEl.getAttributeValue(Constants.ATTR_NAME);
        if(name != null)
        {
            binding.setName(new NCName(name));
        }

        QName intfaceQN = null;
        String intface = bindEl.getAttributeValue(Constants.ATTR_INTERFACE);
        if(intface != null)
        {
            try {
                intfaceQN = bindEl.getQName(intface);
                binding.setInterfaceName(intfaceQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {intface, bindEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        String type = bindEl.getAttributeValue(Constants.ATTR_TYPE);
        if(type != null) {
            binding.setType(getURI(type));
        }

        parseExtensionAttributes(bindEl, BindingElement.class, binding, desc);

        /* Parse the child elements of <binding>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation>
         * <fault> <operation> or extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = bindEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, binding);
            }
            else if (Constants.Q_ELEM_FAULT.equals(tempElQN))
            {
                parseBindingFault(tempEl, desc, binding);
            }
            else if (Constants.Q_ELEM_OPERATION.equals(tempElQN))
            {
                parseBindingOperation(tempEl, desc, binding);
            }
            else
            {
                binding.addExtensionElement(
                        parseExtensionElement(BindingElement.class, binding, tempEl, desc) );
            }
        }

        return binding;
    }

    private BindingFaultElement parseBindingFault(XMLElement bindFaultEl,
            DescriptionElement desc,
            BindingElement parent)
            throws WSDLException {

        BindingFaultElement fault = parent.addBindingFaultElement();

        QName intFltQN = null;
        String ref = bindFaultEl.getAttributeValue(Constants.ATTR_REF);
        if(ref != null)
        {
            try {
                intFltQN = bindFaultEl.getQName(ref);
                fault.setRef(intFltQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {ref, bindFaultEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        parseExtensionAttributes(bindFaultEl, BindingFaultElement.class, fault, desc);

        /* Parse the child elements of binding <fault>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = bindFaultEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, fault);
            }
            else
            {
                fault.addExtensionElement(
                        parseExtensionElement(BindingFaultElement.class, fault, tempEl, desc) );
            }
        }

        return fault;
    }

    private BindingOperationElement parseBindingOperation(
            XMLElement bindOpEl,
            DescriptionElement desc,
            BindingElement parent)
            throws WSDLException {

        BindingOperationElement oper = parent.addBindingOperationElement();

        QName refQN = null;
        String ref = bindOpEl.getAttributeValue(Constants.ATTR_REF);
        if(ref != null)
        {
            try {
                refQN = bindOpEl.getQName(ref);
                oper.setRef(refQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {ref, bindOpEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        parseExtensionAttributes(bindOpEl, BindingOperationElement.class, oper, desc);

        /* Parse the child elements of binding <operation>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * <input> <output> <infault> <outfault> or extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = bindOpEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_INPUT.equals(tempElQN))
            {
                parseBindingMessageReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_OUTPUT.equals(tempElQN))
            {
                parseBindingMessageReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_INFAULT.equals(tempElQN))
            {
                parseBindingFaultReference(tempEl, desc, oper);
            }
            else if (Constants.Q_ELEM_OUTFAULT.equals(tempElQN))
            {
                parseBindingFaultReference(tempEl, desc, oper);
            }
            else
            {
                oper.addExtensionElement(
                        parseExtensionElement(BindingOperationElement.class, oper, tempEl, desc) );
            }
        }

        return oper;
    }

    private BindingFaultReferenceElement parseBindingFaultReference(
            XMLElement faultRefEl,
            DescriptionElement desc,
            BindingOperationElement parent)
            throws WSDLException {

        BindingFaultReferenceElement faultRef = parent.addBindingFaultReferenceElement();

        QName refQN = null;
        String ref = faultRefEl.getAttributeValue(Constants.ATTR_REF);
        if(ref != null)
        {
            try {
                refQN = faultRefEl.getQName(ref);
                faultRef.setRef(refQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {ref, faultRefEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        String msgLabel = faultRefEl.getAttributeValue(Constants.ATTR_MESSAGE_LABEL);
        if(msgLabel != null)
        {
            faultRef.setMessageLabel(new NCName(msgLabel));
        } 
        else
        {
            //This is a limited solution supporting the 3 MEPs in the Part 2 spec. 
            //TODO generic support for user-defined, extensible MEPs.
            
            BindingOperationElement bop = (BindingOperationElement)faultRef.getParentElement();
            InterfaceOperationElement iop = bop.getInterfaceOperationElement();
            URI mep = (iop != null ? iop.getPattern() : null); //iop might be null if the WSDL is invalid
            
            if(Constants.MEP_URI_IN_OUT.equals(mep))
            {
                //Ruleset is fault-replaces-message, so fault is in same direction as msg.
                //The <output> is replaced by an <outfault>.
                //The <input> is replaced by an <infault>.
                //The <outfault> msg label should match the <output> msg label.
                if(Constants.ELEM_OUTFAULT.equals(faultRefEl.getLocalName()))
                {
                    faultRef.setMessageLabel(MessageLabel.OUT);
                }
                else
                {
                    faultRef.setMessageLabel(MessageLabel.IN);
                }
            }
            else if(Constants.MEP_URI_ROBUST_IN_ONLY.equals(mep))  
            {
                //Ruleset is message-triggers-fault, so fault is opposite direction to msg.
                //The <input> can trigger an <outfault>. 
                //The <outfault> msg label should match the <input> msg label.
                if(Constants.ELEM_OUTFAULT.equals(faultRefEl.getLocalName()))
                {
                    faultRef.setMessageLabel(MessageLabel.IN); //the <outfault> is triggered by the <input>
                }
                else
                {
                    //TODO this MEP may have only <outfault>s, not <infault>s, so treat this as an error.
                    faultRef.setMessageLabel(MessageLabel.OUT);
                }
            }
            else if(Constants.MEP_URI_IN_ONLY.equals(mep))
            {
                //TODO Ruleset is no-faults, so treat this as an error.
            }
        }

        parseExtensionAttributes(faultRefEl, BindingFaultReferenceElement.class, faultRef, desc);

        /* Parse the child elements of binding operation <infault> or <outfault>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = faultRefEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, faultRef);
            }
            else
            {
                faultRef.addExtensionElement(
                        parseExtensionElement(BindingFaultReferenceElement.class, faultRef, tempEl, desc) );
            }
        }

        return faultRef;
    }

    private BindingMessageReferenceElement parseBindingMessageReference(
            XMLElement msgRefEl,
            DescriptionElement desc,
            BindingOperationElement parent)
            throws WSDLException {

        BindingMessageReferenceElement message = parent.addBindingMessageReferenceElement();

        if(Constants.ELEM_INPUT.equals(msgRefEl.getLocalName())) {
            message.setDirection(Direction.IN);
        } 
        else if(Constants.ELEM_OUTPUT.equals(msgRefEl.getLocalName())) {
            message.setDirection(Direction.OUT);
        }

        String msgLabel = msgRefEl.getAttributeValue(Constants.ATTR_MESSAGE_LABEL);
        if(msgLabel != null) 
        {
            message.setMessageLabel(new NCName(msgLabel));
        }
        else
        {
            //This is a limited solution supporting the 3 MEPs in the Part 2 spec. 
            //TODO generic support for user-defined, extensible MEPs.
            if(message.getDirection().equals(Direction.IN))
            {
                message.setMessageLabel(MessageLabel.IN);
            }
            else
            {
                message.setMessageLabel(MessageLabel.OUT);
            }
        }

        parseExtensionAttributes(msgRefEl, BindingMessageReferenceElement.class, message, desc);

        /* Parse the child elements of binding operation <input> or <output>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = msgRefEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, message);
            }
            else
            {
                message.addExtensionElement(
                        parseExtensionElement(BindingMessageReferenceElement.class, message, tempEl, desc) );
            }
        }

        return message;
    }

    private ServiceElement parseService(
            XMLElement serviceEl,
            DescriptionElement desc)
            throws WSDLException {

        ServiceElement service = desc.addServiceElement();

        String name = serviceEl.getAttributeValue(Constants.ATTR_NAME);
        if(name != null)
        {
            service.setName(new NCName(name));
        }

        QName intfaceQN = null;
        String intface = serviceEl.getAttributeValue(Constants.ATTR_INTERFACE);
        if(intface != null)
        {
            try {
                intfaceQN = serviceEl.getQName(intface);
                service.setInterfaceName(intfaceQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {intface, serviceEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

		parseExtensionAttributes(serviceEl, ServiceElement.class, service, desc);

		/* Parse the child elements of <service>. 
		 * As per WSDL 2.0 spec, they must be in the following order if present:
		 * <documentation> 
		 * <endpoint>
		 * extension elements in any order
		 * TODO validate that the elements are in correct order
		 */ 

        XMLElement[] children = serviceEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

			if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
			{
				parseDocumentation(tempEl, desc, service);
			}
			else if (Constants.Q_ELEM_ENDPOINT.equals(tempElQN))
			{
				parseEndpoint(tempEl, desc, service);
			}
			else
			{
				service.addExtensionElement(
						parseExtensionElement(ServiceElement.class, service, tempEl, desc) );
			}
		}

		return service;
	}
            
    private EndpointElement parseEndpoint(
            XMLElement endpointEl,
            DescriptionElement desc,
            ServiceElement parent)
            throws WSDLException {

        EndpointElement endpoint = parent.addEndpointElement();

        String name = endpointEl.getAttributeValue(Constants.ATTR_NAME);
        if(name != null)
        {
            endpoint.setName(new NCName(name));
        }

        QName bindingQN = null;
        String binding = endpointEl.getAttributeValue(Constants.ATTR_BINDING);
        if(binding != null)
        {
            try {
                bindingQN = endpointEl.getQName(binding);
                endpoint.setBindingName(bindingQN);
            } catch (WSDLException e) {
                getErrorReporter().reportError( 
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL505",
                        new Object[] {binding, endpointEl.getQName()},
                        ErrorReporter.SEVERITY_ERROR);
            }
        }

        String address = endpointEl.getAttributeValue(Constants.ATTR_ADDRESS);

        if(address != null)
        {
            endpoint.setAddress(getURI(address));
        }

        parseExtensionAttributes(endpointEl, EndpointElement.class, endpoint, desc);

        /* Parse the child elements of <endpoint>. 
         * As per WSDL 2.0 spec, they must be in the following order if present:
         * <documentation> 
         * extension elements in any order
         * TODO validate that the elements are in correct order
         */ 

        XMLElement[] children = endpointEl.getChildElements();
        XMLElement tempEl = null;
        QName tempElQN = null;

        for(int i=0; i<children.length; i++)
        {
            tempEl = children[i];
            tempElQN = tempEl.getQName();

            if (Constants.Q_ELEM_DOCUMENTATION.equals(tempElQN))
            {
                parseDocumentation(tempEl, desc, endpoint);
            }
            else
            {
                endpoint.addExtensionElement(
                        parseExtensionElement(EndpointElement.class, endpoint, tempEl, desc) );
            }
        }

        return endpoint;
    }


    /* --- */

    protected ExtensionElement parseExtensionElement(
            Class parentType,
            WSDLElement parent,
            XMLElement el,
            DescriptionElement desc)
            throws WSDLException {

        QName elementType = el.getQName();
        URI namespaceURI = el.getNamespaceURI();

        try
        {
            //check that ext element is not in the WSDL 2.0 namespace.
            if (Constants.NS_URI_WSDL20.equals(namespaceURI))
            {
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL520",
                        new Object[] {elementType, parentType.getName()},
                        ErrorReporter.SEVERITY_ERROR);
                return null;
            }

            ExtensionRegistry extReg = fWsdlContext.extensionRegistry;

            ExtensionDeserializer extDS = extReg.queryDeserializer(parentType, elementType);

            return extDS.unmarshall(parentType, parent, elementType, el, desc, extReg);
        }
        catch (WSDLException e)
        {
            if (e.getLocation() == null)
            {
                //TODO fix this after refactoring into BaseWSDLReader
                //e.setLocation(XPathUtils.getXPathExprFromNode(el));
            }
            throw e;
        }
    }

    protected abstract void parseExtensionAttributes(
            XMLElement extEl, 
            Class wsdlClass, 
            WSDLElement wsdlObj,
            DescriptionElement desc)
            throws WSDLException;



    protected abstract void parseNamespaceDeclarations(
            XMLElement xmlElem, 
            WSDLElement wsdlElem) 
            throws WSDLException;

    /* Parse the schema for schema to include the built in schema types in the Woden model.
     * TODO: As there are a finite number of built in schema types it may be better to create
     * constants rather than reading the schema for schema on the creation of every model. 
     * Also, this method currently requires that the schema elements exist in the types element.
     * This may not be the best idea as it may imply that this schema contains an actual import
     * statement in a WSDL 2.0 document. This method also does not work for when building the
     * model programmatically.
     * This method should be reevaluated at a later point.
     */
    protected void parseSchemaForXMLSchema(DescriptionElement desc) throws WSDLException{
        //This method is subject to reevaluation and a different approach,
        //so the default implementation is empty.
        //Subclasses can override this to do something useful.
    }

    /* ************************************************************
     *  Helper methods
     * ************************************************************/

    /**
     * Returns an XMLElement object representing the specified element
     * object. The element object argument must be compatible with the 
     * concrete reader implementation. If so, this method returns an XMLElement
     * object that is type-compatible with the reader implementatation (e.g. a
     * DOMXMLElement for DOMWSDLReader).
     * 
     * @throws IllegalArgumentException if elem is not a type recognized by the 
     * WSDLReader implementation.
     */
    abstract protected XMLElement createXMLElement(Object elem) throws IllegalArgumentException;

    /*
     * Convert a string of type xs:anyURI to a java.net.URI.
     * An empty string argument will return an empty string URI.
     * A null argument will return a null.
     */
    protected URI getURI(String anyURI) throws WSDLException {
        URI uri = null;
        if(anyURI != null)
        {
            try {
                uri = new URI(anyURI);
            } catch (URISyntaxException e) {
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL506", 
                        new Object[] {anyURI}, 
                        ErrorReporter.SEVERITY_ERROR, 
                        e);
            }
        }
        return uri;
    }

    /*
     * Retrieve a WSDL document by resolving the location URI specified 
     * on a WSDL &lt;import&gt; or &lt;include&gt; element.
     */
    protected abstract DescriptionElement getWSDLFromLocation(
            String locationURI,
            DescriptionElement desc,
            Map wsdlModules)
            throws WSDLException;

    /**
     * Provides the capability of setting a custom URI Resolver.
     * 
     * @param resolver the custom URIResolver
     * @throws NullPointerException if the 'resolver' parameter is null.
     */
    public void setURIResolver(URIResolver resolver) {
       
        if(resolver == null) {
            String msg = fWsdlContext.errorReporter.getFormattedMessage(
                    "WSDL026", new Object[] {"resolver"});
            throw new NullPointerException(msg);
        }
        
        fWsdlContext = new WSDLContext(
                fWsdlContext.wsdlFactory,
                fWsdlContext.errorReporter,
                fWsdlContext.extensionRegistry,
                resolver);
    }

    /*
     * convenience method to call the URI resolver and handle common exceptions.
     * Unlike the actual Resolver, returns the original URI if no resolution is 
     * available.
     * 
     * TODO move to o.a.w.internal.util ?
     */
    protected URI resolveURI(URI uri) throws WSDLException {
        
        if (uri != null) {
            URI resolvedUri = null;
            try {
                resolvedUri = getURIResolver().resolveURI(uri);
            } catch (IOException e) {
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL524",
                        new Object[] {uri}, 
                        ErrorReporter.SEVERITY_ERROR, 
                        e);
            }
            if (resolvedUri != null) {
                uri = resolvedUri;
            }
        }
        
        return uri;
    }

    /*
     * Convenience method for resolving URI's to Strings
     * 
     * @param uri
     * @return
     */
    protected String resolveURI(String uri) {
        String resolvedUri;
        try {
            resolvedUri = getURIResolver().resolveURI(new URI(uri)).toString();
        } catch (Exception e) {
            // revert to "default" behaviour
            resolvedUri = uri;
        }
        
        return resolvedUri; 
    }
 
    
    /**
     * Find the current URI Resolver
     */
    public URIResolver getURIResolver() {
        return fWsdlContext.uriResolver;
    }
    
    
}