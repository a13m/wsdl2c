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
package org.apache.woden.internal.wsdl20.validation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.schema.ImportedSchema;
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.schema.Schema;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.ImportElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaObjectTable;

/**
 * The WSDL document validator validates a WSDL XML model against the
 * document assertions specified in the WSDL 2.0 specification.
 */
public class WSDLDocumentValidator 
{
  /**
   * Validate the document representation of the WSDL document against
   * the WSDL 2.0 specification.
   * 
   * @param descElement The WSDL 2.0 XML model description element.
   * @param errorReporter The error reporter to use for any errors.
   * @return True if the WSDL document representation is valid, false otherwise.
   * @throws WSDLException
   */
  public boolean validate(DescriptionElement descElement, ErrorReporter errorReporter) throws WSDLException
  {
    boolean isValid = true;
    
    // Test the description element.
    isValid = testAssertionDescription1006(descElement, errorReporter);
    
    // Test the import elements.
    ImportElement[] imports = descElement.getImportElements();
    int numImports = imports.length;
    for(int i = 0; i < numImports; i++)
    {
      // TODO: Implement methods once import elements are supported.
//	  if(!testAssertionImport0001(imports[i], errorReporter))
//		isValid = false;
//	  if(!testAssertionImport0003(imports[i], errorReporter))
//		isValid = false;
    }
	if(!validateTypes(descElement.getTypesElement(), errorReporter))
	  isValid = false;
	
	if(!validateInterfaces(descElement, descElement.getInterfaceElements(), errorReporter))
	  isValid = false;

		// 1. Call the validators for specific namespaces
		//    - Does this need to be broken up into XML specific and compoent model?
		// 2. Call post validators
	//	validateTypes(descElement.getTypesElement(), errorReporter);
//		Description descComponent = descElement.getDescriptionComponent();
//		validateInterfaces(descComponent.getInterfaces(), errorReporter);
//		
		// TODO: validate bindings, services, and extension elements
	  
    return isValid;
  }
	
  /**
   * Validate the contents of the types element. This method runs the assertion
   * tests for inline and imported types.
   * 
   * @param types The types element of which to validate the contents.
   * @param errorReporter The error reporter.
   * @return True if all the types related assertions pass, false otherwise.
   * @throws WSDLException
   */
  protected boolean validateTypes(TypesElement types, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	
	// If there is no types element all types assertions are true.
	if(types == null)
	  return true;
	
	// Test imported schema assertions.
	ImportedSchema[] importedSchemas = types.getImportedSchemas();
	int numImportedSchemas = importedSchemas.length;
	for(int i = 0; i < numImportedSchemas; i++)
	{
	  ImportedSchema schema = (ImportedSchema)importedSchemas[i];
		
	  if(!testAssertionSchema1069(schema, errorReporter))
		isValid = false;
		
	  if(!testAssertionSchema1070(schema, errorReporter))
		isValid = false;
		  
	}
	
	// Test inlined schema assertions.
	InlinedSchema[] inlinedSchemas = types.getInlinedSchemas();
	if(!testAssertionSchema1073(inlinedSchemas, errorReporter))
	  isValid = false;
	return isValid;
  }
  
  /**
   * Validate the contents of the interface element. This method runs the assertion
   * tests for interface element components.
   * 
   * @param interfaces An array of interface elements for which to validate the contents.
   * @param errorReporter The error reporter.
   * @return True if all the interface related assertions pass, false otherwise.
   * @throws WSDLException
   */
  protected boolean validateInterfaces(DescriptionElement descElement, InterfaceElement[] interfaces, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	
	int numInterfaceElements = interfaces.length;
	for(int i = 0; i < numInterfaceElements; i++)
	{
	  InterfaceElement interfaceElem = interfaces[i];
	  
	  if(!testAssertionInterface1012(interfaceElem, errorReporter))
		isValid = false;
	  
	  if(!validateInterfaceFaults(descElement, interfaceElem.getInterfaceFaultElements(), errorReporter))
	    isValid = false;
	  
	  if(!validateInterfaceOperations(descElement, interfaceElem.getInterfaceOperationElements(), errorReporter))
		isValid = false;
	}
	
	return isValid;
  }
  
  /**
   * Validate the InterfaceFault elements.
   * 
   * @param descElement The root description element.
   * @param faultElements An array of fault elements.
   * @param errorReporter An error reporter.
   * @return True if the interface fault elements are all valid, false otherwise.
   * @throws WSDLException
   */
  protected boolean validateInterfaceFaults(DescriptionElement descElement, InterfaceFaultElement[] faultElements, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	int numFaultElements = faultElements.length;
	for(int j = 0; j < numFaultElements; j++)
	{
	  InterfaceFaultElement faultElement = faultElements[j];
	  if(!testAssertionSchema1066(descElement, faultElement.getElement().getQName(), errorReporter))
	    isValid = false;
	  }  
	return isValid;
  }
  
  /**
   * Validate the InterfaceOperation elements.
   * 
   * @param descElement The root description element.
   * @param interfaceOperations An array of interface operation elements.
   * @param errorReporter An error reporter.
   * @return True if the interface operation elements are all valid, false otherwise.
   * @throws WSDLException
   */
  protected boolean validateInterfaceOperations(DescriptionElement descElement, InterfaceOperationElement[] interfaceOperations, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	int numInterfaceOperations = interfaceOperations.length;
	for(int j = 0; j < numInterfaceOperations; j++)
	{
	  InterfaceOperationElement interfaceOperation = interfaceOperations[j];
	  
	  if(!validateInterfaceMessageReferences(descElement, interfaceOperation.getInterfaceMessageReferenceElements(), errorReporter))
		isValid = false;
	  
	  if(!validateInterfaceFaultReferences(descElement, interfaceOperation.getInterfaceFaultReferenceElements(), errorReporter))
			isValid = false;
	}
    return isValid;
  }
  
  /**
   * Validate the InterfaceMessageReference elements.
   * 
   * @param descElement The root description element.
   * @param messageReferences An array of interface message reference elements.
   * @param errorReporter An error reporter.
   * @return True if the interface message reference elements are all valid, false otherwise.
   * @throws WSDLException
   */
  protected boolean validateInterfaceMessageReferences(DescriptionElement descElement, InterfaceMessageReferenceElement[] messageReferences, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
    //WODEN-149 removed assertion Schema-0020 from this method. InterfaceMessageReference assertions to be implemented.
	return isValid;
  }
  
   /**
     * Validate the InterfaceFaultReference elements.
     * 
     * @param descElement The root description element.
     * @param faultReferences An array of interface fault reference elements.
     * @param errorReporter An error reporter.
     * @return True if the interface fault reference elements are all valid, false otherwise.
     * @throws WSDLException
     */
  protected boolean validateInterfaceFaultReferences(DescriptionElement descElement, InterfaceFaultReferenceElement[] faultReferences, ErrorReporter errorReporter) throws WSDLException
  {
     boolean isValid = true;
     
     int numFaultReferences = faultReferences.length;
     for(int k = 0; k < numFaultReferences; k++)
     {
        InterfaceFaultReferenceElement faultReference = faultReferences[k];
        if(!testAssertionQNameResolution1064ForInterfaceFaultReference(faultReference, errorReporter))
          isValid = false;
     }
     
     return isValid;
  }
  
  /**
   * Test assertion Description-1006. Tests whether the target namespace
   * specified is an absolute IRI.
   * 
   * @param descElement The description element for which to check the target namespace.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionDescription1006(DescriptionElement descElement, ErrorReporter errorReporter) throws WSDLException
  {
    URI targetNS = descElement.getTargetNamespace();
    if(!targetNS.isAbsolute())
    {
      errorReporter.reportError(new ErrorLocatorImpl(), "Description-1006", new Object[]{targetNS}, ErrorReporter.SEVERITY_ERROR);
      return false;
	}
    return true;
  }

  /**
   * Test assertion Schema-1069. An imported schema must contain a
   * target namespace.
   * 
   * @param schema The imported schema to check.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionSchema1069(ImportedSchema schema, ErrorReporter errorReporter) throws WSDLException
  {
	XmlSchema schemaDef = schema.getSchemaDefinition();
	// The assertion is true if the schema definition is not available.
	// Problems locating the schema will be reported elseware and are
	// not part of this assertion.
	if(schemaDef == null)
	  return true;
	
	String targetNS = schemaDef.getTargetNamespace();
	if(targetNS == null || targetNS.equals(""))
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1069", new Object[]{schema.getSchemaLocation()}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
	return true;
  }
  
  /**
   * Test assertion Schema-1070. An imported schema must specify the
   * same target namespace as the import element.
   * 
   * @param schema The imported schema to check.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionSchema1070(ImportedSchema schema, ErrorReporter errorReporter) throws WSDLException
  {
	XmlSchema schemaDef = schema.getSchemaDefinition();
    // The assertion is true if the schema definition is not available.
	// Problems locating the schema will be reported elseware and are
	// not part of this assertion.
	if(schemaDef == null)
	  return true;
	
	String importedSchemaTargetNS = schemaDef.getTargetNamespace();
	String specifiedTargetNS = schema.getNamespace().toString();
	if(specifiedTargetNS != null && !specifiedTargetNS.equals(importedSchemaTargetNS))
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1070", new Object[]{specifiedTargetNS}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
	return true;
  }
  
  /**
   * Test assertion Schema-1073. Inlined XML Schemas must not define
   * an element that has already been defined by another inline schema
   * with the same target namespace.
   * 
   * @param schema An array containing all the inline schemas in the order in which they are defined.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionSchema1073(InlinedSchema[] schema, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	int numInlineSchemas = schema.length;
	Hashtable schemas = new Hashtable();
	for(int i = 0; i < numInlineSchemas; i++)
	{
	  InlinedSchema iSchema = schema[i];
	  URI iSchemaNs = iSchema.getNamespace();
	  // If the namespace isn't defined this assertion doesn't apply.
	  if(iSchemaNs == null)
		continue;
	  String ns = iSchemaNs.toString();
	  
	  if(schemas.containsKey(ns))
	  {
		List schemaList = (List)schemas.get(ns);
		XmlSchemaObjectTable elements = iSchema.getSchemaDefinition().getElements();
		Iterator elementNames = elements.getNames();
		while(elementNames.hasNext())
		{
		  QName elementName = (QName)elementNames.next();
		  Iterator otherInlineSchemas = schemaList.iterator();
		  while(otherInlineSchemas.hasNext())
		  {
			if(((InlinedSchema)otherInlineSchemas.next()).getSchemaDefinition().getElementByName(elementName) != null)
			{
			  // Duplicate element defined.
			  errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1073", new Object[]{elementName, ns}, ErrorReporter.SEVERITY_ERROR);
			  isValid = false;
			}
		  }
		
		}
		
		XmlSchemaObjectTable types = iSchema.getSchemaDefinition().getSchemaTypes();
		Iterator typeNames = types.getNames();
		while(typeNames.hasNext())
		{
		  QName typeName = (QName)typeNames.next();
		  Iterator otherInlineSchemas = schemaList.iterator();
		  while(otherInlineSchemas.hasNext())
		  {
		    if(((InlinedSchema)otherInlineSchemas.next()).getSchemaDefinition().getTypeByName(typeName) != null)
		    {
			  // Duplicate type defined.
			  errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1073b", new Object[]{typeName, ns}, ErrorReporter.SEVERITY_ERROR);
			  isValid = false;
		    }
		  }
			
		}
		  //Check if another element has been defined.
		  //check if another type has been defined.
		  //add to the existing list of schemas
		schemaList.add(iSchema);
	  }
	  else
	  {
		List schemaList = new ArrayList();
		schemaList.add(iSchema);
		schemas.put(ns, schemaList);
	  }
		 
	}
	return isValid;
  }
  
  /**
   * Test assertion Interface-1012. All style defaults specified on an interface
   * element must be absolute.
   * 
   * @param interfaceElem The interface element to check the style default list.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterface1012(InterfaceElement interfaceElem, ErrorReporter errorReporter) throws WSDLException
  {
	boolean isValid = true;
	
	URI[] styleDefaults = interfaceElem.getStyleDefault();
	int numStyleDefaults = styleDefaults.length;
	for(int i = 0; i < numStyleDefaults; i++)
	{
	  if(!styleDefaults[i].isAbsolute())
	  {
	    errorReporter.reportError(new ErrorLocatorImpl(), "Interface-1012", new Object[]{styleDefaults[i].toString()}, ErrorReporter.SEVERITY_ERROR);
	    isValid = false;
	  }
	}
	return isValid;
  }
  
  /**
   * Test assertion Schema-1066. References to XML schema components must only refer
   * to elements and types in namespaces that have been imported or inlined or that
   * are part of the XML schema namespace.
   * 
   * @param descElement The description element of the document.
   * @param namespace Check this namespace to see if it has been defined.
   * @param errorReporter The error Reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionSchema1066(DescriptionElement descElement, QName qualifiedName, ErrorReporter errorReporter) throws WSDLException
  {
	// If the qualifiedName is null it can't be checked.
    if(qualifiedName != null && !qualifiedName.getNamespaceURI().equals(Constants.TYPE_XSD_2001))
    {
      //Get the namespace and the local name from the qualified name.
      String namespace = qualifiedName.getNamespaceURI();
	  String localname = qualifiedName.getLocalPart();    	

      TypesElement types = descElement.getTypesElement();
      if(types == null)
      {
    	errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1066", new Object[]{localname, namespace}, ErrorReporter.SEVERITY_ERROR);
        return false;
      }
      Schema[] schemas = types.getSchemas();
      int numSchemas = schemas.length;
      boolean schemaNotFound = true;
      // TODO: This linear search should be improved for performance.
      for(int i = 0; i < numSchemas; i++)
      {
    	URI schemaNs = schemas[i].getNamespace();
    	// If the schema namespace is null continue to the next one. This is not the
    	// schema we're looking for.
    	if(schemaNs == null)
    	  continue;
    	if(schemaNs.toString().equals(namespace))
    	{
          schemaNotFound = false;
    	  break;
    	}
      }
      if(schemaNotFound)
      {
        errorReporter.reportError(new ErrorLocatorImpl(), "Schema-1066", new Object[]{localname, namespace}, ErrorReporter.SEVERITY_ERROR);
      	return false;
      } 
    }
    return true;
  }
  
  /**
   * Test assertion QName-resolution-1064 for an InterfaceFaultReference element. \
   * A Description component must not contain broken QName references.
   * 
   * @param faultReference The interface fault reference to check for a broken reference.
   * @param errorReporter An error reporter.
   * @return True if the assertion passes, false otherwise. 
   * @throws WSDLException
   */
  protected boolean testAssertionQNameResolution1064ForInterfaceFaultReference(InterfaceFaultReferenceElement faultReference, ErrorReporter errorReporter) throws WSDLException
  {
	QName ref = faultReference.getRef();
	if(ref != null)
	{
	  InterfaceFaultElement fault = faultReference.getInterfaceFaultElement();
	  if(fault == null)
	  {
	    errorReporter.reportError(new ErrorLocatorImpl(), 
	            "QName-resolution-1064", 
	            new Object[]{ref.toString(), "interface fault reference", "interface fault"}, 
	            ErrorReporter.SEVERITY_ERROR);
	    return false;
	  }
	}
	return true;
  }
}
