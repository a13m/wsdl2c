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

import org.apache.woden.WSDLException;
import org.apache.woden.internal.schema.SchemaConstants;
import org.apache.woden.schema.Schema;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingMessageReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.extensions.ComponentExtensionContext;
import org.apache.woden.wsdl20.extensions.WSDLExtensionConstants;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.extensions.rpc.RPCConstants;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.BindingFaultReferenceElement;
import org.apache.woden.wsdl20.xml.BindingMessageReferenceElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.EndpointElement;
import org.apache.woden.wsdl20.xml.ImportElement;
import org.apache.woden.wsdl20.xml.IncludeElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.ServiceElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaExternal;
import org.apache.ws.commons.schema.XmlSchemaImport;
import org.apache.ws.commons.schema.XmlSchemaInclude;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaObjectTable;
import org.apache.ws.commons.schema.utils.NamespacePrefixList;

/**
 * Converts the xml representation of a WSDL document to the WSDL component
 * model representation defined by the W3C WSDL 2.0 spec. The xml model is
 * contained within a DescriptionElement object. The component model is
 * contained within a Description object.
 * 
 * TODO consider moving this logic inside DescriptionImpl, maybe as an inner
 * class.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com) - added
 *         Interface Operation extensions, - added Endpoint extensions
 */
public class ComponentModelBuilder {
    
    private static final String emptyString = "".intern();

	// TODO support for other (non-Schema) type systems

	private DescriptionImpl fDesc;

	// TODO private ErrorReporter fErrorRpt; see todo in
	// buildElementDeclarations()
    private List fDescTypesDone = new Vector();
    
	private List fSchemasDone = new Vector();

	private List fInterfacesDone = new Vector();

	private List fBindingsDone = new Vector();

	private List fServicesDone = new Vector();
    
    private URI fBindingType = null;
    
	public ComponentModelBuilder(DescriptionImpl desc) {
		fDesc = desc;
		// TODO fErrorRpt = errorRpt; see todo in buildElementDeclarations()
		initComponents(fDesc);
	}

	private void initComponents(DescriptionImpl desc) {

		buildElementsAndTypes(desc);
		buildInterfaces(desc);
		buildBindings(desc);
		buildServices(desc);

		IncludeElement[] includes = desc.getIncludeElements();
		for (int i = 0; i < includes.length; i++) {
			DescriptionElement inclDesc = includes[i].getDescriptionElement();
			if (inclDesc != null) {
				initComponents((DescriptionImpl) inclDesc);
			}
		}

		ImportElement[] imports = desc.getImportElements();
		for (int i = 0; i < imports.length; i++) {
			DescriptionElement impDesc = imports[i].getDescriptionElement();
			if (impDesc != null) {
				initComponents((DescriptionImpl) impDesc);
			}
		}
	}

	/***************************************************************************
	 * TYPES
	 **************************************************************************/

	/*
	 * Extract from the collections of in-scope schemas the element declarations
	 * and type definitions.
	 */
	private void buildElementsAndTypes(DescriptionImpl desc) {
        
        if(fDescTypesDone.contains(desc)) {
            return;
        } else {
            fDescTypesDone.add(desc);
        }
        
        //process the schema components declared within this description's types element
		URI typeSystemURI = URI.create(Constants.TYPE_XSD_2001); //TODO support other type systems?
        TypesElement types = desc.getTypesElement();
		if (types != null) {
            //first, get the list of imported schema namespaces
            Schema[] importedSchemas = types.getImportedSchemas();
            List importedNSpaces = new Vector();
            for(int j=0; j<importedSchemas.length; j++) {
                URI nsURI = importedSchemas[j].getNamespace();
                if(nsURI != null) {
                    String ns = nsURI.toString();
                    if(!importedNSpaces.contains(ns)) {
                        importedNSpaces.add(ns);
                    }
                }
            }
            //second, process all schemas inlined or imported directly by <types>
            Schema[] schemas = types.getSchemas();
            XmlSchema xmlSchema;
            for(int i=0; i<schemas.length; i++) {
                xmlSchema = schemas[i].getSchemaDefinition();
                if(xmlSchema != null && !fSchemasDone.contains(xmlSchema)) {
                    buildElementsAndTypes(
                            xmlSchema, xmlSchema.getTargetNamespace(), typeSystemURI, importedNSpaces);
                }
            }
        }
        
        //process the schema components declared within any included descriptions
        IncludeElement[] includes = desc.getIncludeElements();
        DescriptionElement includedDesc;
        for(int i = 0; i < includes.length; i++)
        {
            includedDesc = includes[i].getDescriptionElement();
            if(includedDesc != null) 
            {
                buildElementsAndTypes((DescriptionImpl)includedDesc);
            }
        }
        
        //process the schema components declared within any imported descriptions
        ImportElement[] imports = desc.getImportElements();
        DescriptionElement importedDesc;
        for(int i = 0; i < imports.length; i++)
        {
            importedDesc = imports[i].getDescriptionElement();
            if(importedDesc != null) 
            {
                buildElementsAndTypes((DescriptionImpl)importedDesc);
            }
        }
            
            
			//List referenceableSchemaDefs = ((TypesImpl) types)
			//		.getReferenceableSchemaDefs();
			//Iterator i = referenceableSchemaDefs.iterator();
			//while (i.hasNext()) {
			//	XmlSchema schemaDef = (XmlSchema) i.next();
            //    buildElementsAndTypes(schemaDef, schemaDef.getTargetNamespace(), typeSystemURI);
			//}
	}
    
    private void buildElementsAndTypes(XmlSchema schemaDef, String schemaTns, URI typeSystemURI, List importedNSpaces) {
        
        if(fSchemasDone.contains(schemaDef)) {
            return;
        } else {
            fSchemasDone.add(schemaDef);
        }
        
        //process elements and types declared directly in this schema
        
        if(!SchemaConstants.NS_STRING_SCHEMA.equals(schemaDef.getTargetNamespace())) {
            //XML Schema namespace is implicitly imported to get built-in types...we don't want the elements.
            //TODO detect if the XML Schema NS has been explicitly imported (if so, we do want the elements) 
            buildElementDeclarations(schemaDef, schemaTns, typeSystemURI);
        }
        buildTypeDefinitions(schemaDef, schemaTns, typeSystemURI);
            
        //process elements and types declared in any included or imported schemas.
        //note that XmlSchema keeps included and imported schemas together, via getIncludes().
        
        XmlSchemaObjectCollection includeColl = schemaDef.getIncludes();
        Iterator includes = includeColl.getIterator();
        while(includes.hasNext()) {
            Object o = includes.next();
            XmlSchemaExternal externalSchema = (XmlSchemaExternal)o;
            XmlSchema schema = externalSchema.getSchema();
            if(schema != null )
            {
                String schemaTNS = schema.getTargetNamespace();
                if( externalSchema instanceof XmlSchemaInclude ||
                   (externalSchema instanceof XmlSchemaImport && importedNSpaces.contains(schemaTNS)) ) {
                    buildElementsAndTypes(schema, schemaTNS, typeSystemURI, importedNSpaces);
                }
            }
        }
    }

	/*
	 * Extract the element declarations from the given schema.
	 */
	private void buildElementDeclarations(XmlSchema schemaDef, String schemaTns, URI typeSystemURI) {
        
	    XmlSchemaObjectTable elementTable = schemaDef.getElements();
        NamespacePrefixList prefixes = schemaDef.getNamespaceContext();
	    Iterator qnames = elementTable.getNames();
	    while (qnames.hasNext()) {
	        QName xseQN = (QName) qnames.next();
            if(fDesc.getElementDeclaration(xseQN) != null) {
                //The Description already contains this Element Declaration.
                continue;
                //This check is necessary because the XmlSchema.equals method, which gets used
                //to evaluate fSchemas.contains(..), cannot detect the equivalence of a schema 
                //that is xs:imported within <wsdl:types> and also xs:imported within by 
                //an inlined schema within the same <wsdl:types> element.
                //Error case is result.xsd in the W3C WSDL 2.0 test case SparqlQuerySimplified-1G.
                //This check may be necessary anyway, because if the document assertion Schema-1073
                //is violated, we don't want the duplicate schema components in the component model.
                //TODO check that this behaviour is correct (eliminating duplicates)
            }
            QName edQN = xseQN;
            if(xseQN.getNamespaceURI() == null && schemaTns != null) {
                //this is how XmlSchema represents tns for chameleon xs:includes,
                //so replace it with the including schema's tns.
                edQN = new QName(schemaTns, xseQN.getLocalPart(), xseQN.getPrefix());
            }
            if(edQN.getPrefix() == "" || edQN.getPrefix() == null) {
                //if a prefix has been declared for this NS uri, include it in the qname
                String pfx = prefixes.getPrefix(edQN.getNamespaceURI());
                if(pfx != null) {
                    edQN = new QName(edQN.getNamespaceURI(), edQN.getLocalPart(), pfx);
                }
            }
	        if(schemaTns == null || schemaTns.equals(edQN.getNamespaceURI())) //TODO test with schema imports, may be incorrect.
            {
	            ElementDeclarationImpl ed = new ElementDeclarationImpl();
	            ed.setName(edQN);
	            ed.setSystem(typeSystemURI);
	            ed.setContentModel(Constants.API_APACHE_WS_XS);
	            ed.setContent(elementTable.getItem(xseQN));
	            fDesc.addElementDeclaration(ed);
	        }
	    }
	}

	/*
	 * Extract the type definitions from the given schema.
	 */
	private void buildTypeDefinitions(XmlSchema schemaDef, String schemaTns, URI typeSystemURI) {
        
	    XmlSchemaObjectTable typeTable = schemaDef.getSchemaTypes();
        NamespacePrefixList prefixes = schemaDef.getNamespaceContext();
	    Iterator qnames = typeTable.getNames();
	    while (qnames.hasNext()) {
	        QName xstQN = (QName) qnames.next();
            
            if(SchemaConstants.NS_STRING_SCHEMA.equals(schemaTns) && 
               !SchemaConstants.LIST_Q_BUILT_IN_TYPES.contains(xstQN)) {
                //XML Schema namespace is implicitly imported to get built-in types...we don't want non-built-in types.
                //TODO detect if the XML Schema NS has been explicitly imported (if so, we want ALL type defs) 
                continue;
            }
            
            if(fDesc.getTypeDefinition(xstQN) != null) {
                //The Description already contains this Type Definition.
                continue;
                //The same comments apply here as stated in the buildElementDeclarations method.
                //TODO check that this behaviour is correct (per assertion Schema-1073).
            }
            QName tdQN = xstQN;
            if(xstQN.getNamespaceURI() == null && schemaTns != null) {
                //this is how XmlSchema represents tns for chameleon xs:includes,
                //so replace it with the including schema's tns.
                tdQN = new QName(schemaTns, xstQN.getLocalPart(), xstQN.getPrefix());
            }
            if(tdQN.getPrefix() == emptyString || tdQN.getPrefix() == null) {
                //if a prefix has been declared for this NS uri, include it in the qname
                String pfx = prefixes.getPrefix(tdQN.getNamespaceURI());
                if(pfx != null) {
                    tdQN = new QName(tdQN.getNamespaceURI(), tdQN.getLocalPart(), pfx);
                }
            }
	        if (schemaTns == null || schemaTns.equals(tdQN.getNamespaceURI())) 
            {
	            TypeDefinitionImpl td = new TypeDefinitionImpl();
	            td.setName(tdQN);
	            td.setSystem(typeSystemURI);
	            td.setContentModel(Constants.API_APACHE_WS_XS);
	            td.setContent(typeTable.getItem(xstQN));
	            fDesc.addTypeDefinition(td);
	        }
	    }
	}

	/***************************************************************************
	 * INTERFACE
	 **************************************************************************/

	/*
	 * Initialize the Interface component and its child components from the
	 * InterfaceElement and its child elements.
	 */
	private void buildInterfaces(DescriptionImpl desc) {
		InterfaceElement[] interfaceEls = desc.getInterfaceElements();
		for (int i = 0; i < interfaceEls.length; i++) {
			InterfaceImpl interfaceImpl = (InterfaceImpl) interfaceEls[i];
			if (!fInterfacesDone.contains(interfaceImpl)) {
				buildInterfaceOperations(interfaceImpl);
				fInterfacesDone.add(interfaceImpl);
			}
		}
	}

	private void buildInterfaceOperations(InterfaceImpl interfaceImpl) {
		InterfaceOperationElement[] operations = interfaceImpl
				.getInterfaceOperationElements();
		for (int i = 0; i < operations.length; i++) {
			InterfaceOperationImpl oper = (InterfaceOperationImpl) operations[i];
			buildInterfaceOperationExtensions(oper);
		}
	}

	private void buildInterfaceOperationExtensions(InterfaceOperationImpl oper) {
        
        /*
         * Create a ComponentExtensions object for each registered extension
         * namespace used within this operation by extension elements or attributes.
         */
		ExtensionRegistry er = fDesc.getWsdlContext().extensionRegistry;
		URI[] extNamespaces = er
				.queryComponentExtensionNamespaces(InterfaceOperation.class);

		for (int i = 0; i < extNamespaces.length; i++) {
			URI extNS = extNamespaces[i];
			if (oper.hasExtensionAttributesForNamespace(extNS)) {
				ComponentExtensionContext compExt = createComponentExtensions(
						InterfaceOperation.class, oper, extNS);
				oper.setComponentExtensionContext(extNS, compExt);
			}
		}
        
        /*
         * {safety} is a REQUIRED extension property on interface operation
         * so if an InterfaceOperationExtensions object has not already been
         * created, create one now.
         */
        if (oper.getComponentExtensionContext(
                WSDLExtensionConstants.NS_URI_WSDL_EXTENSIONS) == null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    InterfaceOperation.class, oper,
                    WSDLExtensionConstants.NS_URI_WSDL_EXTENSIONS);
            oper.setComponentExtensionContext(
                    WSDLExtensionConstants.NS_URI_WSDL_EXTENSIONS, compExt);
        }
        
        /*
         * If interface operation style includes RPC then if an
         * RPCInterfaceOperationExtensions object has not already been
         * created, create one now.
         */
        boolean isRPCStyle = false;
        URI[] style = oper.getStyle();
        for(int i=0; i<style.length; i++)
        {
            URI temp = style[i];
            if(RPCConstants.STYLE_URI_RPC.equals(temp)) {
                isRPCStyle = true;
                break;
            }
        }
        
        if(isRPCStyle) {
            if (oper.getComponentExtensionContext(
                    RPCConstants.NS_URI_RPC) == null) {
                ComponentExtensionContext compExt = createComponentExtensions(
                        InterfaceOperation.class, oper,
                        RPCConstants.NS_URI_RPC);
                oper.setComponentExtensionContext(
                        RPCConstants.NS_URI_RPC, compExt);
            }
        }
	}

	/***************************************************************************
	 * BINDING
	 **************************************************************************/

	/*
	 * Initialize the Binding component and its child components from the
	 * BindingElement and its child elements.
	 */
	private void buildBindings(DescriptionImpl desc) {
		BindingElement[] bindingEls = desc.getBindingElements();
		for (int i = 0; i < bindingEls.length; i++) {
			BindingImpl bindImpl = (BindingImpl) bindingEls[i];
			if (!fBindingsDone.contains(bindImpl)) {
                buildBindingExtensions(bindImpl);
				buildBindingFaults(bindImpl);
				buildBindingOperations(bindImpl);
				fBindingsDone.add(bindImpl);
			}
		}
	}

	private void buildBindingFaults(BindingImpl binding) {
		BindingFaultElement[] bindFaults = binding.getBindingFaultElements();
		for (int i = 0; i < bindFaults.length; i++) {
			BindingFaultImpl bindFault = (BindingFaultImpl) bindFaults[i];
			buildBindingFaultExtensions(bindFault);
		}
	}

	private void buildBindingOperations(BindingImpl binding) {
		BindingOperationElement[] operations = binding
				.getBindingOperationElements();
		for (int i = 0; i < operations.length; i++) {
			BindingOperationImpl oper = (BindingOperationImpl) operations[i];
			buildBindingFaultReferences(oper);
			buildBindingMessageReferences(oper);
			buildBindingOperationExtensions(oper);
		}
	}

	private void buildBindingFaultReferences(BindingOperationImpl oper) {
		BindingFaultReferenceElement[] faultRefs = oper
				.getBindingFaultReferenceElements();
		for (int i = 0; i < faultRefs.length; i++) {
			BindingFaultReferenceImpl faultRef = (BindingFaultReferenceImpl) faultRefs[i];

			buildBindingFaultReferenceExtensions(faultRef);

		}
	}

	private void buildBindingMessageReferences(BindingOperationImpl oper) {
		BindingMessageReferenceElement[] messages = oper
				.getBindingMessageReferenceElements();
		for (int i = 0; i < messages.length; i++) {
			BindingMessageReferenceImpl message = (BindingMessageReferenceImpl) messages[i];

			buildBindingMessageReferenceExtensions(message);
		}
	}

	private void buildBindingExtensions(BindingImpl binding) {
        
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        fBindingType = binding.getType();
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    Binding.class, binding, fBindingType);
            binding.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	private void buildBindingFaultExtensions(BindingFaultImpl bindFault) {
        
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    BindingFault.class, bindFault, fBindingType);
            bindFault.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	private void buildBindingOperationExtensions(BindingOperationImpl bindOper) {
        
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    BindingOperation.class, bindOper, fBindingType);
            bindOper.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	private void buildBindingMessageReferenceExtensions(
			BindingMessageReferenceImpl bindMsgRef) {
        
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    BindingMessageReference.class, bindMsgRef, fBindingType);
            bindMsgRef.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	private void buildBindingFaultReferenceExtensions(
			BindingFaultReferenceImpl bindFaultRef) {
        
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    BindingFaultReference.class, bindFaultRef, fBindingType);
            bindFaultRef.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	private void buildServices(DescriptionImpl desc) {

		ServiceElement[] serviceEls = desc.getServiceElements();
		for (int i = 0; i < serviceEls.length; i++) {
			ServiceImpl serviceImpl = (ServiceImpl) serviceEls[i];
			if (!fServicesDone.contains(serviceImpl)) {
				buildEndpoints(serviceImpl);
				fServicesDone.add(serviceImpl);
			}
		}
	}

	private void buildEndpoints(ServiceImpl serviceImpl) {

		EndpointElement[] endpoints = serviceImpl.getEndpointElements();
		for (int i = 0; i < endpoints.length; i++) {
			EndpointImpl endpoint = (EndpointImpl) endpoints[i];
			buildEndpointExtensions(endpoint);
		}
	}

	private void buildEndpointExtensions(EndpointImpl endpoint) {
		
        /*
         * Create a ComponentExtensions subtype specific to the binding type.
         */
        
        if(fBindingType != null) {
            ComponentExtensionContext compExt = createComponentExtensions(
                    Endpoint.class, endpoint, fBindingType);
            endpoint.setComponentExtensionContext(fBindingType, compExt);
        }
	}

	/*
	 * This helper method factors out common code for creating
	 * ComponentExtensionContexts registered in the ExtensionRegistry.
	 */
	private ComponentExtensionContext createComponentExtensions(Class parentClass,
			WSDLComponent parentComp, URI extNS) {
		ExtensionRegistry er = fDesc.getWsdlContext().extensionRegistry;
		ComponentExtensionContext compExt = null;
		try {
			compExt = er.createComponentExtension(parentClass, parentComp, extNS);
			//TODO remove with woden-47 ((ComponentExtensionsImpl) compExt).init(parentElem, extNS);
		} catch (WSDLException e) {
			// This exception occurs if there is no Java class registered for
			// the namespace, but
			// this namespace was obtained from the extension registry so we
			// know that a Java class is
			// registered and that this exception cannot occur. Ignore the catch
			// block.
		}
		return compExt;
	}

}
