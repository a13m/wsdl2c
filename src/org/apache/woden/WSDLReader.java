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
package org.apache.woden;

import org.apache.woden.resolver.URIResolver;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;

/**
 * This interface declares an API for reading WSDL descriptions from different sources 
 * such as WSDL documents or files, parsed XML elements and input byte streams.
 * It contains various <code>readWSDL</code> methods that obtain the WSDL infoset from 
 * the specified source, parse it into the Woden object model and return this as a WSDL 
 * <code>Description</code> component containing the WSDL components derived from that infoset.
 * If the WSDL is comprised of a composite set of documents nested via wsdl:import
 * or wsdl:include, the Description component will represent the abstract, 'flattened' view
 * of the WSDL tree, containing all of the WSDL components derived from the various WSDL 
 * documents, but without the document structure. 
 *   
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface WSDLReader {
    
    
    //TODO Create wsdl-version-independent methods. 
    //E.g. public WSDLDocument readWSDL(String uri); where
    //WSDLDocument is a wrapper for a 1.1 Definition and a 
    //2.0 Description. The client app could/should then use
    //WSDLDocument to determine which version is has and use
    //the appropriate WSDL API.

    //for the 2.0 prototype, just use 2.0 specific methods
    
    /**
     * Constants for reader configuration feature names. 
     * Features are associated with a boolean value (true if they are 
     * enabled, false if not).
     * 
     * TODO Decide if these features should be exposed on the Woden API
     * as constants defined here and used via the generic set/getFeature 
     * methods, or whether we should have a finite set of feature-specific
     * methods on the WSDLReader interface for the features we know about 
     * (e.g. setValidationFeature(boolean) or setValidationFeatureOn() / 
     * setValidationFeatureOff()) and remove the constants defined here.
     * Note - even in the latter case, we still need the generic 
     * set/getFeature methods.
     */
    
    
    /**
     * Set to <code>true</code> to enable verbose diagnostic tracing, <code>false</code> otherwise.
     */
    public static String FEATURE_VERBOSE = 
        "http://ws.apache.org/woden/features/verbose";
    
    /**
     * Set to <code>true</code> to enable the WSDL validation feature, <code>false</code> otherwise.
     */
    public static String FEATURE_VALIDATION = 
        "http://ws.apache.org/woden/features/validation";
    
    /**
     * Set to <code>true</code> if parsing should continue after 
     * encountering a non-fatal error in the WSDL which might result
     * in incomplete WSDL model being returned by the reader, 
     * <code>false</code> otherwise.
     */
    public static String FEATURE_CONTINUE_ON_ERROR =
        "http://ws.apache.org/woden/features/continue_on_error";
    
    /**
     * Constants for reader configuration property names.
     * Properties have a value represented by an object. 
     * 
     * TODO ditto the comment on features, about whether to define 
     * property constants here and use generic set/getProperty methods
     * or remove the constants and use property-specific methods.
     */
    public static String PROPERTY_XML_PARSER_API = 
        "http://ws.apache.org/woden/property/xml_parser_api";

    public static String PROPERTY_TYPE_SYSTEM_API = 
        "http://ws.apache.org/woden/property/type_system_api";
    
    /**
     * A constant representing the W3C XML Schema type system. All
     * implementations of the Woden API must support W3C XML Schema.
     * An implementation configured to use this type system can 
     * use this constant to specify its value for the property
     * "http://ws.apache.org/woden/property/type_system_api".
     */
    public static final String TYPE_XSD_2001 =
        "http://www.w3.org/2001/XMLSchema";
    
    /**
     * Read the WSDL document at the specified URI and return a WSDL Description 
     * component containing the WSDL components derived from that document.
     * 
     * @param wsdlURI a URI (absolute filename or URL) pointing to a
     * WSDL document.
     * @return the Description component
     * @throws WSDLException for terminating errors and as wrapper
     * for checked exceptions.
     */
    public Description readWSDL(String wsdlURI) throws WSDLException;
   
    /**
     * Read the WSDL obtained from the specified WSDLSource object and return a WSDL Description 
     * component containing the WSDL components derived from that WSDL source.
     * <p>
     * The WSDLSource object must represent the WSDL using a type that the WSDLReader
     * implementation can understand. That is, the WSDLSource and WSDLReader implementations
     * must be compatible.
     * For example, a DOM-based WSDLReader implementation will likely require the WSDLSource 
     * object to represent the WSDL as a DOM Document or Element (org.w3c.dom.Document 
     * or org.w3c.dom.Element), both of which should be type compatible the DOM-based reader.
     * 
     * @param wsdlSource contains an object representing the WSDL
     * @return the Description component
     * @throws WSDLException for terminating errors and as a wrapper
     * for checked exceptions
     */
    public Description readWSDL(WSDLSource wsdlSource) throws WSDLException;
      
    //TODO - decide if an API method createXMLElement() is needed.
    
    /**
     * Returns a WSDLSource object that is compatible with the WSDLReader implementation. 
     * That is, a WSDLSource implementation that represents WSDL using types that
     * the WSDLReader implementation can understand.
     * For example, DOM-based WSDLReader implementation will likely return a WSDLSource
     * object that represents the WSDL as a DOM Document or Element (org.w3c.dom.Document 
     * or org.w3c.dom.Element), both of which should be type compatible the DOM-based reader.
     * <p>
     * This WSDLSource object can be used to encapsulate the WSDL and is then passed to the 
     * WSDLReader as a parameter to its <code>readWSDL</code> methods.
     * 
     * @return the WSDLSource class compatible with the WSDLReader implementation.
     */
    public WSDLSource createWSDLSource();
    
    /**
     * @return the ErrorReporter used by this reader
     */
    public ErrorReporter getErrorReporter();
    
    /**
     * Store the name of the WSDLFactory implementation class to be used for  
     * any subsequent WSDLFactory requests. The named factory class will 
     * replace any existing factory object in use.
     * 
     * @param factoryImplName the WSDLFactory implementation classname
     */
    public void setFactoryImplName(String factoryImplName);
    
    /**
     * @return the WSDLFactory implementation classname
     */
    public String getFactoryImplName();
    
    /**
     * Set the extension registry to the specified registry reference.
     * The registry parameter cannot be null.
     * 
     * @param extReg an ExtensionRegistry
     * @throws NullPointerException if extReg is null
     */
    public void setExtensionRegistry(ExtensionRegistry extReg);
    
    public ExtensionRegistry getExtensionRegistry();
    
    /**
     * Set a named feature on or off with a boolean. Note, this relates to 
     * features of the Woden framework, not to WSDL-specific features such
     * as the WSDL 2.0 Feature component.
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
    public void setFeature(String name, boolean value);
    
    /**
     * Returns the on/off setting of the named feature, represented as a boolean. 
     * 
     * @param name the name of the feature to get the value of
     * @return a boolean representing the on/off state of the named feature
     * @throws IllegalArgumentException if the feature name is not recognized.
     */
    public boolean getFeature(String name);
    
    
    /**
     * Set a named property to the specified object.  Note, this relates to 
     * properties of the Woden implementation, not to WSDL-specific properties
     * such as the WSDL 2.0 Property component.
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
    public void setProperty(String name, Object value);
    
    /**
     * Returns the value of the named property.
     * 
     * @param name the name of the property to get the value of
     * @return an Object representing the property's value
     * @throws IllegalArgumentException if the property name is not recognized.
     */
    public Object getProperty(String name);

    /**
     * Sets the URI resolver to be used.
     * 
     * @param resolver
     */
    public void setURIResolver(URIResolver resolver);
    
    /**
     * Gets the URI Resolver currently in use
     * @return the URI resolver currently in use
     */
    public URIResolver getURIResolver();

    
}
