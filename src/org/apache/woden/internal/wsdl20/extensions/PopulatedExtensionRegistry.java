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
package org.apache.woden.internal.wsdl20.extensions;

import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.util.PropertyUtils;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPBindingExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPBindingFaultExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPBindingMessageReferenceExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPBindingOperationExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPEndpointExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPHeaderDeserializer;
import org.apache.woden.internal.wsdl20.extensions.http.HTTPHeaderImpl;
import org.apache.woden.internal.wsdl20.extensions.rpc.RPCInterfaceOperationExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPBindingExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPBindingFaultExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPBindingFaultReferenceExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPBindingOperationExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPEndpointExtensionsImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPHeaderBlockDeserializer;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPHeaderBlockImpl;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPModuleDeserializer;
import org.apache.woden.internal.wsdl20.extensions.soap.SOAPModuleImpl;
import org.apache.woden.internal.xml.ArgumentArrayAttrImpl;
import org.apache.woden.internal.xml.BooleanAttrImpl;
import org.apache.woden.internal.xml.HTTPAuthenticationSchemeAttrImpl;
import org.apache.woden.internal.xml.IntOrTokenAnyAttrImpl;
import org.apache.woden.internal.xml.QNameListOrTokenAnyAttrImpl;
import org.apache.woden.internal.xml.QNameOrTokenAnyAttrImpl;
import org.apache.woden.internal.xml.StringAttrImpl;
import org.apache.woden.internal.xml.URIAttrImpl;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingMessageReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.extensions.WSDLExtensionConstants;
import org.apache.woden.wsdl20.extensions.ExtensionRegistrar;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.extensions.http.HTTPConstants;
import org.apache.woden.wsdl20.extensions.rpc.RPCConstants;
import org.apache.woden.wsdl20.extensions.soap.SOAPConstants;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.BindingFaultReferenceElement;
import org.apache.woden.wsdl20.xml.BindingMessageReferenceElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.EndpointElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;

/**
 * This class extends ExtensionRegistry and pre-registers
 * serializers/deserializers for the SOAP, HTTP and MIME extensions. Java impl
 * types are also registered for all the SOAP and HTTP extensions defined in the
 * WSDL 2.0 Spec.
 * 
 * This class was copied from WSDL4J and modified for Woden.
 * 
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 * @author John Kaputin (jkaputin@apache.org) - Woden changes
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com) - added
 *         wsdlx:safe and wrpc:signature to Interface Operation, - added
 *         HTTPAuthenticationSchemeAttrImpl
 */
public class PopulatedExtensionRegistry extends ExtensionRegistry {

	public PopulatedExtensionRegistry(ErrorReporter errorReporter) throws WSDLException {
        
        super(errorReporter);
        
		// ------------ Default type for unregistered extension attributes
		// ------------

		/*
		 * If a default XMLAttr type other than UnknownAttr is to be used for
		 * unregisterd attributes, uncomment this code and replace <someAttr>
		 * with the default type. registerExtAttributeType(WSDLElement.class,
		 * new QName("http://ws.apache.org/woden", "DefaultAttr"),
		 * <someAttr>Impl.class);
		 */

		// ------------ WSDL extension attributes ------------
		registerExtAttributeType(InterfaceOperationElement.class,
				WSDLExtensionConstants.Q_ATTR_SAFE, BooleanAttrImpl.class);

		// ------------ WSDL Component Extensions ------------

		registerComponentExtension(InterfaceOperation.class,
				WSDLExtensionConstants.NS_URI_WSDL_EXTENSIONS,
				InterfaceOperationExtensionsImpl.class);

		// ------------ RPC extension attributes ------------

		registerExtAttributeType(InterfaceOperationElement.class,
				RPCConstants.Q_ATTR_RPC_SIGNATURE, ArgumentArrayAttrImpl.class);

		// ------------ RPC Component Extensions ------------
		registerComponentExtension(InterfaceOperation.class,
		        RPCConstants.NS_URI_RPC,
				RPCInterfaceOperationExtensionsImpl.class);

		// ------------ SOAP extension attributes ------------

		registerExtAttributeType(BindingElement.class,
				SOAPConstants.Q_ATTR_SOAP_VERSION, StringAttrImpl.class);

		registerExtAttributeType(BindingElement.class,
				SOAPConstants.Q_ATTR_SOAP_PROTOCOL, URIAttrImpl.class);

		registerExtAttributeType(BindingElement.class,
				SOAPConstants.Q_ATTR_SOAP_MEPDEFAULT, URIAttrImpl.class);

		registerExtAttributeType(BindingFaultElement.class,
				SOAPConstants.Q_ATTR_SOAP_CODE, QNameOrTokenAnyAttrImpl.class);

		registerExtAttributeType(BindingFaultElement.class,
				SOAPConstants.Q_ATTR_SOAP_SUBCODES,
				QNameListOrTokenAnyAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				SOAPConstants.Q_ATTR_SOAP_MEP, URIAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				SOAPConstants.Q_ATTR_SOAP_ACTION, URIAttrImpl.class);

		// ------------ SOAPModule extension elements ------------

		SOAPModuleDeserializer soapModuleDeser = new SOAPModuleDeserializer();

		// registerSerializer(BindingElement.class,
		// SOAPConstants.Q_ELEM_SOAP_MODULE,
		// <serializer to be implemented>);
		registerDeserializer(BindingElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, soapModuleDeser);
		registerExtElementType(BindingElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, SOAPModuleImpl.class);

		registerDeserializer(BindingFaultElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, soapModuleDeser);
		registerExtElementType(BindingFaultElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, SOAPModuleImpl.class);

		registerDeserializer(BindingOperationElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, soapModuleDeser);
		registerExtElementType(BindingOperationElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, SOAPModuleImpl.class);

		registerDeserializer(BindingMessageReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, soapModuleDeser);
		registerExtElementType(BindingMessageReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, SOAPModuleImpl.class);

		registerDeserializer(BindingFaultReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, soapModuleDeser);
		registerExtElementType(BindingFaultReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_MODULE, SOAPModuleImpl.class);

		// ------------ SOAPHeaderBlock extension elements ------------

		SOAPHeaderBlockDeserializer soapHeaderBlockDeser = new SOAPHeaderBlockDeserializer();

		// registerSerializer(BindingFaultElement.class,
		// SOAPConstants.Q_ELEM_SOAP_HEADER,
		// <serializer to be implemented>);
		registerDeserializer(BindingFaultElement.class,
				SOAPConstants.Q_ELEM_SOAP_HEADER, soapHeaderBlockDeser);
		registerExtElementType(BindingFaultElement.class,
				SOAPConstants.Q_ELEM_SOAP_HEADER, SOAPHeaderBlockImpl.class);

		// registerSerializer(BindingMessageReferenceElement.class,
		// SOAPConstants.Q_ELEM_SOAP_HEADER,
		// <serializer to be implemented>);
		registerDeserializer(BindingMessageReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_HEADER, soapHeaderBlockDeser);
		registerExtElementType(BindingMessageReferenceElement.class,
				SOAPConstants.Q_ELEM_SOAP_HEADER, SOAPHeaderBlockImpl.class);

		// ------------ SOAP Component Extensions ------------

		registerComponentExtension(Binding.class,
				SOAPConstants.NS_URI_SOAP,
				SOAPBindingExtensionsImpl.class);

		registerComponentExtension(BindingFault.class,
		        SOAPConstants.NS_URI_SOAP,
				SOAPBindingFaultExtensionsImpl.class);

		registerComponentExtension(BindingOperation.class,
		        SOAPConstants.NS_URI_SOAP,
				SOAPBindingOperationExtensionsImpl.class);

		registerComponentExtension(BindingMessageReference.class,
		        SOAPConstants.NS_URI_SOAP,
				SOAPBindingMessageReferenceExtensionsImpl.class);

		registerComponentExtension(BindingFaultReference.class,
		        SOAPConstants.NS_URI_SOAP,
				SOAPBindingFaultReferenceExtensionsImpl.class);

        registerComponentExtension(Endpoint.class,
                SOAPConstants.NS_URI_SOAP,
                SOAPEndpointExtensionsImpl.class);

		// ------------ HTTP extension attributes ------------

		registerExtAttributeType(BindingElement.class,
				HTTPConstants.Q_ATTR_METHOD_DEFAULT, StringAttrImpl.class);

		registerExtAttributeType(BindingElement.class,
				HTTPConstants.Q_ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT,
				StringAttrImpl.class);

		registerExtAttributeType(BindingElement.class,
				HTTPConstants.Q_ATTR_COOKIES, BooleanAttrImpl.class);

		registerExtAttributeType(BindingElement.class,
				HTTPConstants.Q_ATTR_CONTENT_ENCODING_DEFAULT,
				StringAttrImpl.class);

		registerExtAttributeType(BindingFaultElement.class,
				HTTPConstants.Q_ATTR_CODE, IntOrTokenAnyAttrImpl.class);

		registerExtAttributeType(BindingFaultElement.class,
				HTTPConstants.Q_ATTR_CONTENT_ENCODING, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_LOCATION, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_IGNORE_UNCITED, BooleanAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_METHOD, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_INPUT_SERIALIZATION, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_OUTPUT_SERIALIZATION, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_FAULT_SERIALIZATION, StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_QUERY_PARAMETER_SEPARATOR,
				StringAttrImpl.class);

		registerExtAttributeType(BindingOperationElement.class,
				HTTPConstants.Q_ATTR_CONTENT_ENCODING_DEFAULT,
				StringAttrImpl.class);

		registerExtAttributeType(BindingMessageReferenceElement.class,
				HTTPConstants.Q_ATTR_CONTENT_ENCODING, StringAttrImpl.class);

		registerExtAttributeType(EndpointElement.class,
				HTTPConstants.Q_ATTR_AUTHENTICATION_SCHEME,
				HTTPAuthenticationSchemeAttrImpl.class);

		registerExtAttributeType(EndpointElement.class,
				HTTPConstants.Q_ATTR_AUTHENTICATION_REALM, StringAttrImpl.class);

		// ------------ HTTPHeader extension elements ------------

		HTTPHeaderDeserializer httpHeaderDeser = new HTTPHeaderDeserializer();

		// registerSerializer(BindingFaultElement.class,
		// HTTPConstants.Q_ELEM_HTTP_HEADER,
		// <serializer to be implemented>);
		registerDeserializer(BindingFaultElement.class,
				HTTPConstants.Q_ELEM_HTTP_HEADER, httpHeaderDeser);
		registerExtElementType(BindingFaultElement.class,
				HTTPConstants.Q_ELEM_HTTP_HEADER, HTTPHeaderImpl.class);

        // registerSerializer(BindingMessageReferenceElement.class,
        // HTTPConstants.Q_ELEM_HTTP_HEADER,
        // <serializer to be implemented>);
        registerDeserializer(BindingMessageReferenceElement.class,
                HTTPConstants.Q_ELEM_HTTP_HEADER, httpHeaderDeser);
        registerExtElementType(BindingMessageReferenceElement.class,
                HTTPConstants.Q_ELEM_HTTP_HEADER, HTTPHeaderImpl.class);

		// ------------ HTTP Component Extensions ------------

		registerComponentExtension(Binding.class,
				HTTPConstants.NS_URI_HTTP,
				HTTPBindingExtensionsImpl.class);

		registerComponentExtension(BindingFault.class,
				HTTPConstants.NS_URI_HTTP,
				HTTPBindingFaultExtensionsImpl.class);

		registerComponentExtension(BindingOperation.class,
				HTTPConstants.NS_URI_HTTP,
				HTTPBindingOperationExtensionsImpl.class);

		registerComponentExtension(BindingMessageReference.class,
				HTTPConstants.NS_URI_HTTP,
				HTTPBindingMessageReferenceExtensionsImpl.class);

		registerComponentExtension(Endpoint.class,
				HTTPConstants.NS_URI_HTTP,
				HTTPEndpointExtensionsImpl.class);
        
        //Register other, user-defined WSDL extensions.
        registerExtensions();
	}
    
    /* 
     * Registers WSDL extensions with this extension registry,
     * via the ExtensionRegistrar callback interface.
     * 
     */
    private void registerExtensions() throws WSDLException {
        Enumeration registrarNames = getRegistrarNames();
        if (registrarNames != null) {
            while (registrarNames.hasMoreElements()) {
                String extensionRegistrarClassName = ((String)registrarNames.nextElement()).trim();
                try {
                    Class clazz = Class.forName(extensionRegistrarClassName);
                    ExtensionRegistrar registrar = (ExtensionRegistrar)clazz.newInstance();
                    registrar.registerExtensions(this);
                } catch (ClassNotFoundException cnfe) {
                    ErrorReporter er = getErrorReporter();
                    er.reportError(
                            null,
                            "WSDL020",
                            new Object[] {extensionRegistrarClassName}, 
                            ErrorReporter.SEVERITY_ERROR, 
                            cnfe);
                } catch (ClassCastException cce) {
                    getErrorReporter().reportError(
                            null,
                            "WSDL021",
                            new Object[] {extensionRegistrarClassName}, 
                            ErrorReporter.SEVERITY_ERROR, 
                            cce);
                } catch (Exception e) {
                    getErrorReporter().reportError(
                            null,
                            "WSDL022",
                            new Object[] {extensionRegistrarClassName}, 
                            ErrorReporter.SEVERITY_ERROR, 
                            e);
                }
            }
        }
    }

    /*
     * Returns an Enumeration of ExtensionRegistrar class names.
     * The <code>REGISTRAR_PROPERTY</code> property contains a comma-separated list of
     * ExtensionRegistrar class names.
     * @return an Enumeration of ExtensionRegistrar class names.
     */
    private Enumeration getRegistrarNames() {
        String registrarNames = PropertyUtils.findProperty(ExtensionRegistry.REGISTRAR_PROPERTY);
        return registrarNames == null ? null : new StringTokenizer(registrarNames, ",");
    }

}