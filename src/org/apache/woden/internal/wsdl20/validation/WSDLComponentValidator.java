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
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingMessageReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.InterfaceFaultReference;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.WSDLComponent;

/**
 * The WSDL component validator can validate a WSDL 2.0 component model
 * against the assertions defined in the WSDL 2.0 specification. The
 * WSDL 2.0 component assertions are currently available in the
 * non-normative version of the WSDL 2.0 specification and can be viewed at
 * http://dev.w3.org/cvsweb/~checkout~/2002/ws/desc/wsdl20/wsdl20.html?content-type=text/html;%20charset=utf-8#assertionsummary
 */
public class WSDLComponentValidator 
{
  /**
   * Validate the WSDL 2.0 component model described by the
   * description component.
   * 
   * @param desc The description component of the WSDL 2.0 component model.
   * @param errorReporter An error reporter to be used for reporting errors found with the model.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the WSDL 2.0 component model.
   */
  public void validate(Description desc, ErrorReporter errorReporter) throws WSDLException
  {
    validateInterfaces(desc.getInterfaces(), errorReporter);
    validateBindings(desc.getBindings(), desc, errorReporter);
    validateServices(desc.getServices(), desc, errorReporter);	
  }
	
  /**
   * Validate the interfaces in the WSDL component model.
   * 
   * @param interfaces The interfaces in the WSDL component model.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the interface components.
   */
  protected void validateInterfaces(Interface[] interfaces, ErrorReporter errorReporter) throws WSDLException
  {
	testAssertionInterface1010(interfaces, errorReporter);
	  
	int numInterfaces = interfaces.length;
	for(int i = 0; i < numInterfaces; i++)
	{
      Interface interfac = interfaces[i];
      
	  testAssertionInterface1009(interfac, errorReporter);
	  
	  validateInterfaceOperations(interfac, interfac.getInterfaceOperations(), errorReporter);
	}
  }
  
  /**
   * Validate the interface operations in the WSDL component model.
   * 
   * @param interfac The interface that contains the interface operations.
   * @param interfaceOperations An array of the interface operations.
   * @param errorReporter An error reporter.
   * @throws WSDLException
   */
  protected void validateInterfaceOperations(Interface interfac, InterfaceOperation[] interfaceOperations, ErrorReporter errorReporter) throws WSDLException
  {
	int numInterfaceOperations = interfaceOperations.length;
	for(int j = 0; j < numInterfaceOperations; j++)
	{
      InterfaceOperation interfaceOperation = interfaceOperations[j];
		
      testAssertionMEP1022(interfaceOperation.getMessageExchangePattern(), errorReporter);
		
      validateInterfaceMessageReferences(interfaceOperation.getInterfaceMessageReferences(), errorReporter);
		
      validateInterfaceFaultReferences(interfaceOperation.getInterfaceFaultReferences(), errorReporter);
    }
  }
  
  /**
   * Validate the interface message references in the WSDL component model.
   * 
   * @param messageReferences An array containing the interface message references.
   * @param errorReporter An error reporter.
   * @throws WSDLException
   */
  protected void validateInterfaceMessageReferences(InterfaceMessageReference[] messageReferences, ErrorReporter errorReporter) throws WSDLException
  {
    testAssertionInterfaceMessageReference1029(messageReferences, errorReporter);
		
    int numMessageReferences = messageReferences.length;
    for(int k = 0; k < numMessageReferences; k++)
    {
      InterfaceMessageReference messageReference = messageReferences[k];
		  
      testAssertionInterfaceMessageReference1028(messageReference, errorReporter);
    }
  }
  
  /**
   * Validate the interface fault references in the WSDL component model.
   * 
   * @param faultReferences An array containing the interface fault references.
   * @param errorReporter An error reporter.
   * @throws WSDLException
   */
  protected void validateInterfaceFaultReferences(InterfaceFaultReference[] faultReferences, ErrorReporter errorReporter) throws WSDLException
  {
	testAssertionInterfaceFaultReference1039(faultReferences, errorReporter);
		
//    int numFaultReferences = faultReferences.length;
//    for(int k = 0; k < numFaultReferences; k++)
//    {
//      InterfaceFaultReference faultReference = faultReferences[k];
//      //TODO check if any validation is required here
//    }
  }
  
  /**
   * Validate the bindings in the WSDL component model.
   * 
   * @param bindings The bindings in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the binding components.
   */
  protected void validateBindings(Binding[] bindings, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  testAssertionBinding1049(bindings, errorReporter);
	  
	  int numBindings = bindings.length;
	  for(int i = 0; i < numBindings; i++)
	  {
		  Binding binding = bindings[i];
		  testAssertionBinding1044(binding, errorReporter);
		  testAssertionBinding1045(binding, errorReporter);
		  testAssertionBinding1048(binding, errorReporter);
		  
		  validateBindingOperations(binding.getBindingOperations(), desc, errorReporter);
		  
		  validateBindingFaults(binding.getBindingFaults(), desc, errorReporter);
	  }
  }
  
  /**
   * Validate the binding operations in the WSDL component model.
   *  
   * @param bindingOperations The bindings operations in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the binding operation components.
   */
  protected void validateBindingOperations(BindingOperation[] bindingOperations, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  testAssertionBindingOperation1051(bindingOperations, errorReporter);
	  
	  int numBindingOperations = bindingOperations.length;
	  for(int i = 0; i < numBindingOperations; i++)
	  {
		  BindingOperation bindingOperation = bindingOperations[i];
		  validateBindingMessageReferences(bindingOperation.getBindingMessageReferences(), desc, errorReporter);
		  validateBindingFaultReferences(bindingOperation.getBindingFaultReferences(), desc, errorReporter);
	  }
	  
  }
  
  /**
   * Validate the binding message references in the WSDL component model.
   * 
   * @param bindingMessageReferences The bindings message references in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the binding message reference components.
   */
  protected void validateBindingMessageReferences(BindingMessageReference[] bindingMessageReferences, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  testAssertionBindingMessageReference1052(bindingMessageReferences, errorReporter);
	  
	  int numBindingMessageReferences = bindingMessageReferences.length;
	  
	  for(int i = 0; i < numBindingMessageReferences; i++)
	  {
		BindingMessageReference bindingMessageReference = bindingMessageReferences[i];
        //TODO check if any validation is required here
	  }
  }
  
  /**
   * Validate the binding fault references in the WSDL component model.
   * 
   * @param bindingFaultReferences The bindings fault references in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the binding fault reference components.
   */
  protected void validateBindingFaultReferences(BindingFaultReference[] bindingFaultReferences, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  testAssertionBindingFaultReference1055(bindingFaultReferences, errorReporter);
	  
	  int numBindingFaultReferences = bindingFaultReferences.length;
	  for(int i = 0; i < numBindingFaultReferences; i++)
	  {
		BindingFaultReference bindingFaultReference = bindingFaultReferences[i];
	    testAssertionBindingFaultReference1059(bindingFaultReference, errorReporter);
	  }
  }
  
  /**
   * Validate the binding faults in the WSDL component model.
   * 
   * @param bindingFaults The bindings faults in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the binding fault components.
   */
  protected void validateBindingFaults(BindingFault[] bindingFaults, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	testAssertionBindingFault1050(bindingFaults, errorReporter);
	
	int numBindingFaults = bindingFaults.length;
	
	for(int i = 0; i < numBindingFaults; i++)
	{
	  BindingFault bindingFault = bindingFaults[i];
      //TODO check if any validation is required here
	}
  }
  
  /**
   * Validate the services in the WSDL component model.
   * 
   * @param services The services in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the service components.
   */
  protected void validateServices(Service[] services, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  testAssertionService1060(services, errorReporter);
	  
	  int numServices = services.length;
	  for(int i = 0; i < numServices; i++)
	  {
		  Service service = services[i];
		  
		  validateEndpoints(service.getEndpoints(), desc, errorReporter);
	  }
  }
  
  /**
   * Validate the endpoints in a service component in the WSDL component model.
   * 
   * @param endpoints The endpoints in a service in the WSDL component model.
   * @param desc The WSDL 2.0 description component.
   * @param errorReporter An error reporter to be used for reporting errors.
   * @throws WSDLException A WSDLException is thrown if a problem occurs while validating the endpoint components.
   */
  protected void  validateEndpoints(Endpoint[] endpoints, Description desc, ErrorReporter errorReporter) throws WSDLException
  {
	  int numEndpoints = endpoints.length;
	  for(int i = 0; i < numEndpoints; i++)
	  {
		  Endpoint endpoint = endpoints[i];
		  testAssertionEndpoint1061(endpoint, errorReporter);
		  testAssertionEndpoint1062(endpoint, errorReporter);
	  }
  }
  
  /**
   * Test assertion Interface-1009. An interface cannot appear, either directly or indirectly,
   * in the list of interfaces it extends.
   * 
   * @param interfac The interface to check.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterface1009(Interface interfac, ErrorReporter errorReporter) throws WSDLException
  {
	Interface[] extendedInterfaces = interfac.getExtendedInterfaces();
	Interface extendedInterface = containsInterface(interfac, extendedInterfaces);
	if(extendedInterface != null)
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "Interface-1009", new Object[]{extendedInterface.getName()}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
	return true;
  }
	
  /**
   * Helper method for testAssertionInterface0027.
   * Check whether the specified interface is in the array of provided
   * interfaces or in an array of interfaces one of the interfaces extends.
   * 
   * @param interfac The interface to check for.
   * @param extendedInterfaces An array of extened interfaces to check for this interface.
   * @return The interface that is equal to or extends this interface, or null if the interface is not contained.
   */
  private Interface containsInterface(Interface interfac, Interface[] extendedInterfaces)
  {
	int numExtInterfaces = extendedInterfaces.length;
	for(int i = 0; i < numExtInterfaces; i++)
	{
	  if(interfac.isEquivalentTo(extendedInterfaces[i]))
	    return extendedInterfaces[i];
	  else if(containsInterface(interfac, extendedInterfaces[i].getExtendedInterfaces()) != null)
		return extendedInterfaces[i];
	}
	return null;
  }
  
  /**
   * Test assertion Interface-1010. An interface must have a unique name out of all the interfaces
   * in the description component.
   * 
   * @param interfaces The interfaces in the description component.
   * @param errorReporter The error reporter.
   * @return True if all the interfaces have unique names, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterface1010(Interface[] interfaces, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List names = new ArrayList();
	int numInterfaces = interfaces.length;
	for(int i = 0; i < numInterfaces; i++)
	{
	  QName name = interfaces[i].getName();
      if(name == null)
        continue;
	  if(names.contains(name))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "Interface-1010", new Object[]{name}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		names.add(name);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion MEP-1022. A message exchange pattern must be an absolute IRI.
   * 
   * @param pattern The message exchange pattern to check.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionMEP1022(URI pattern, ErrorReporter errorReporter) throws WSDLException
  {
	if(!pattern.isAbsolute())
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "MEP-1022", new Object[]{pattern}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
	return true;
  }
  
  /**
   * Test assertion InterfaceMessageReference-1028. When the {message content model} property 
   * has the value #any or #none the {element declaration} property MUST be empty.
   * 
   * @param messageReference The interface message reference to check the message content model and element declarations.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterfaceMessageReference1028(InterfaceMessageReference messageReference, ErrorReporter errorReporter) throws WSDLException
  {
	String messContentModel = messageReference.getMessageContentModel();
	if((messContentModel.equals(Constants.NMTOKEN_ANY) || messContentModel.equals(Constants.NMTOKEN_NONE)) 
		&& messageReference.getElementDeclaration() != null)
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "InterfaceMessageReference-1028", new Object[]{}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
	return true;
  }
  
  /**
   * Test assertion InterfaceMessageReference-1029. For each Interface Message Reference 
   * component in the {interface message references} property of an Interface Operation 
   * component, its {message label} property MUST be unique.
   * 
   * @param messageReferences The message references to check for duplicate names.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterfaceMessageReference1029(InterfaceMessageReference[] messageReferences, ErrorReporter errorReporter) throws WSDLException
  {
	List messageLabels = new ArrayList();
	int numMessageReferences = messageReferences.length;
	for(int i = 0; i < numMessageReferences; i++)
	{
	  NCName messageLabel = messageReferences[i].getMessageLabel();
      if(messageLabel == null)
          continue;
	  if(messageLabels.contains(messageLabel))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "InterfaceMessageReference-1029", new Object[]{messageLabel}, ErrorReporter.SEVERITY_ERROR);
		return false;
	  }
	  else
	  {
		messageLabels.add(messageLabel);
	  }
	}
	return true;
  }
  
  /**
   * Test assertion InterfaceFaultReference-1039. For each Interface Fault Reference 
   * component in the {interface fault references} property of an Interface Operation 
   * component, the combination of its {interface fault} and {message label} properties 
   * MUST be unique.
   * 
   * @param faultReferences The fault references to check for duplicate fault/message label pairs.
   * @param errorReporter The error reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionInterfaceFaultReference1039(InterfaceFaultReference[] faultReferences, ErrorReporter errorReporter) throws WSDLException
  {
	Hashtable identifiers = new Hashtable();
	
	int numFaultReferences = faultReferences.length;
	for(int i = 0; i < numFaultReferences; i++)
	{
	  InterfaceFault fault = faultReferences[i].getInterfaceFault();
	  NCName messageLabel = faultReferences[i].getMessageLabel();
      if(fault == null || messageLabel == null)
    	continue;
	  List messageLabels = (List)identifiers.get(fault);
	  if(messageLabels != null && messageLabels.contains(messageLabel))
	  {
	    errorReporter.reportError(new ErrorLocatorImpl(), "InterfaceFaultReference-1039", new Object[]{fault, messageLabel}, ErrorReporter.SEVERITY_ERROR);
		return false;
      }
	  else
	  {
		if(messageLabels == null)
		  messageLabels = new ArrayList();
		messageLabels.add(messageLabel);
		identifiers.put(fault, messageLabels);
	  }
	}
	return true;
  }
  
  /**
   * Test assertion Binding-1044. If a Binding component specifies any 
   * operation-specific binding details (by including Binding Operation 
   * components) or any fault binding details (by including Binding Fault 
   * components) then it MUST specify an interface the Binding  component 
   * applies to, so as to indicate which interface the operations come from.
   * 
   * @param binding The binding for which to check the contstraint.
   * @param errorReporter The error Reporter.
   * @return True if the assertion passes, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBinding1044(Binding binding, ErrorReporter errorReporter) throws WSDLException
  {
	BindingOperation[] bindingOperations = binding.getBindingOperations();
	BindingFault[] bindingFaults = binding.getBindingFaults();
	Interface bindingInterface = binding.getInterface();
	if(((bindingOperations != null && bindingOperations.length > 0) || 
		(bindingFaults != null && bindingFaults.length > 0)) && 
		 bindingInterface == null)
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "Binding-1044", new Object[]{}, ErrorReporter.SEVERITY_ERROR);
	  return false;
	}
    return true;
  }
  
  /**
   * Test assertion Binding-1045. A Binding component that defines bindings 
   * for an Interface component MUST define bindings for all the operations 
   * of that Interface component.
   * 
   * @param binding The binding of which to check the binding operations.
   * @param errorReporter The error reporter.
   * @return True if the all the operations specified on the interface have bindings defined, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBinding1045(Binding binding, ErrorReporter errorReporter) throws WSDLException
  {
	boolean allInterfaceOperationsHaveBinding = true;
    QName bindingQN = binding.getName();
	String bindingName = bindingQN != null ? bindingQN.getLocalPart() : null;
	
	Interface interfac = binding.getInterface();
	if(interfac == null)
	  return true;
	
	BindingOperation[] bindingOperations = binding.getBindingOperations();
	int numBindingOperations = bindingOperations.length;
	List usedInterfaceOperationList = new ArrayList();
	for(int i = 0; i < numBindingOperations; i++)
	{
	  InterfaceOperation io = bindingOperations[i].getInterfaceOperation();
	  if(io != null)
		usedInterfaceOperationList.add(io);
	}
    // Check the interface operations.
	if(!checkAllInterfaceOperationsHaveBinding(bindingName, interfac, usedInterfaceOperationList, errorReporter))
	  allInterfaceOperationsHaveBinding = false;
	
	Interface[] extendedInterfaces = interfac.getExtendedInterfaces();
	if(extendedInterfaces != null)
	{
	  int numExtendedInterfaces = extendedInterfaces.length;
	  for(int i = 0; i < numExtendedInterfaces; i++)
  	  {
	    if(!checkAllInterfaceOperationsHaveBinding(bindingName, extendedInterfaces[i], usedInterfaceOperationList, errorReporter))
	      allInterfaceOperationsHaveBinding = false;
	  }
	}
	
    return allInterfaceOperationsHaveBinding;
  }
  
  /**
   * Helper method for testAssertionBinding0055. This method checks that
   * each interface operation in the provided interface has a binding 
   * defined.
   * 
   * @param bindingName The name of the binding. Used in error reporting.
   * @param interfac The interface of which to check the operations.
   * @param usedInterfaceOperations A list of interface operations that have bindings specified.
   * @param errorReporter The error reporter.
   * @return True if all the interface operations have bindings defined, false otherwise.
   * @throws WSDLException
   */
  private boolean checkAllInterfaceOperationsHaveBinding(String bindingName, Interface interfac, List usedInterfaceOperations, ErrorReporter errorReporter) throws WSDLException
  {
	boolean allInterfaceOperationsHaveBinding = true;
	InterfaceOperation[] interfaceOperations = interfac.getInterfaceOperations();
	if(interfaceOperations == null)
	  return true;
	
	int numInterfaceOperations = interfaceOperations.length;
	
	// Check the interface operations.
	for(int i = 0; i < numInterfaceOperations; i++)
	{
	  if(!usedInterfaceOperations.contains(interfaceOperations[i]))
	  {
	    errorReporter.reportError(new ErrorLocatorImpl(), "Binding-1045", new Object[]{bindingName, interfaceOperations[i].getName()}, ErrorReporter.SEVERITY_ERROR);
		allInterfaceOperationsHaveBinding = false;
	  }
    }
	return allInterfaceOperationsHaveBinding;
  }
  
  /**
   * Test assertion Binding-1048. The binding type xs:anyURI MUST be an 
   * absolute IRI as defined by [IETF RFC 3987].
   * 
   * @param binding The binding of which to check the type.
   * @param errorReporter The error reporter.
   * @return True if the type is absolute, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBinding1048(Binding binding, ErrorReporter errorReporter) throws WSDLException
  {
	URI type = binding.getType();
    if(type != null && !type.isAbsolute())
    {
      errorReporter.reportError(new ErrorLocatorImpl(), "Binding-1048", new Object[]{type}, ErrorReporter.SEVERITY_ERROR);
	  return false;
    }
    return true;
  }
  
  /**
   * Test assertion Binding-1049. For each Binding component in the {bindings} property of a 
   * Description component, the {name} property MUST be unique.
   * 
   * @param bindings The bindings in the description component.
   * @param errorReporter The error reporter.
   * @return True if the all the bindings have unique names, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBinding1049(Binding[] bindings, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List names = new ArrayList();
	int numBindings = bindings.length;
	for(int i = 0; i < numBindings; i++)
	{
	  QName name = bindings[i].getName();
      if(name == null)
        continue;
	  if(names.contains(name))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "Binding-1049", new Object[]{name}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		names.add(name);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion BindingFault-1050. For each Binding Fault component in the 
   * {binding faults} property of a Binding component, the {interface fault} 
   * property MUST be unique.
   * 
   * @param bindingFaults The binding faults in the description component.
   * @param errorReporter The error reporter.
   * @return True if the all the bindings have unique names, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBindingFault1050(BindingFault[] bindingFaults, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List usedInterfaceFaults = new ArrayList();
	int numBindingFaults = bindingFaults.length;
	for(int i = 0; i < numBindingFaults; i++)
	{
	  InterfaceFault interfaceFault = bindingFaults[i].getInterfaceFault();
	  if(interfaceFault == null) 
	    continue;
	  if(usedInterfaceFaults.contains(interfaceFault))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "BindingFault-1050", new Object[]{}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		usedInterfaceFaults.add(interfaceFault);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion BindingOperation-1051. For each Binding Operation component 
   * in the {binding operations} property of a Binding component, the {interface 
   * operation} property MUST be unique.
   * 
   * @param bindingOperations The binding operations to check for unique interface operations.
   * @param errorReporter The error reporter.
   * @return True if the all the binding operations have specified unique interface operations, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBindingOperation1051(BindingOperation[] bindingOperations, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List specifiedInterfaceOperations = new ArrayList();
	int numBindingOperations = bindingOperations.length;
	for(int i = 0; i < numBindingOperations; i++)
	{
	  InterfaceOperation interfaceOperation = bindingOperations[i].getInterfaceOperation();
	  if(interfaceOperation == null)
	    continue;
	  if(specifiedInterfaceOperations.contains(interfaceOperation))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "BindingOperation-1051", new Object[]{interfaceOperation.getName()}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		specifiedInterfaceOperations.add(interfaceOperation);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion BindingMessageReference-1052. For each Binding Message 
   * Reference component in the {binding message references} property of a 
   * Binding Operation component, the {interface message reference} property 
   * MUST be unique.
   * 
   * @param bindingMessageReferences The binding message references to check for unique interface message references.
   * @param errorReporter The error reporter.
   * @return True if the all the binding message references have specified unique interface message references, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBindingMessageReference1052(BindingMessageReference[] bindingMessageReferences, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List specifiedInterfaceMessageReferences = new ArrayList();
	int numBindingMessageReferences = bindingMessageReferences.length;
	for(int i = 0; i < numBindingMessageReferences; i++)
	{
	  InterfaceMessageReference interfaceMessageReference = bindingMessageReferences[i].getInterfaceMessageReference();
	  if(interfaceMessageReference == null)
	    continue;
	  if(specifiedInterfaceMessageReferences.contains(interfaceMessageReference))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "BindingMessageReference-1052", new Object[]{interfaceMessageReference.getMessageLabel()}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		specifiedInterfaceMessageReferences.add(interfaceMessageReference);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion BindingFaultReference-1055. For each Binding Fault Reference 
   * component in the {binding fault references} property of a Binding Operation 
   * component, the {interface fault reference} property MUST be unique.
   * 
   * @param bindingFaultReferences The binding fault references to check for unique interface fault references.
   * @param errorReporter The error reporter.
   * @return True if the all the binding fault references have specified unique interface fault references, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBindingFaultReference1055(BindingFaultReference[] bindingFaultReferences, ErrorReporter errorReporter) throws WSDLException
  {
	boolean duplicateFound = false;
	List specifiedInterfaceFaultReferences = new ArrayList();
	int numBindingFaultReferences = bindingFaultReferences.length;
	for(int i = 0; i < numBindingFaultReferences; i++)
	{
	  InterfaceFaultReference interfaceFaultReference = bindingFaultReferences[i].getInterfaceFaultReference();
	  if(interfaceFaultReference == null)
	    continue;
	  if(specifiedInterfaceFaultReferences.contains(interfaceFaultReference))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "BindingFaultReference-1055", new Object[]{interfaceFaultReference.getMessageLabel()}, ErrorReporter.SEVERITY_ERROR);
		duplicateFound = true;
	  }
	  else
	  {
		specifiedInterfaceFaultReferences.add(interfaceFaultReference);
	  }
	}
	return !duplicateFound;
  }
  
  /**
   * Test assertion BindingFaultReference-1059. There MUST be an Interface Fault 
   * Reference component in the {interface fault references} of the Interface 
   * Operation being bound with {message label} equal to the effective message 
   * label and with {interface fault} equal to an Interface Fault component with 
   * {name} equal to the actual value of the ref attribute information item.
   * 
   * @param bindingFaultReference The binding fault reference to check if the specified interface fault reference exists.
   * @param errorReporter The error reporter.
   * @return True if the binding fault reference specifies a valid interface fault reference, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionBindingFaultReference1059(BindingFaultReference bindingFaultReference, ErrorReporter errorReporter) throws WSDLException
  {
	InterfaceFaultReference interfaceFaultReference = bindingFaultReference.getInterfaceFaultReference();
	if(interfaceFaultReference == null)
	{
	  errorReporter.reportError(new ErrorLocatorImpl(), "BindingFaultReference-1059", new Object[]{}, ErrorReporter.SEVERITY_ERROR);
      return false;
	}
	return true;
  }
  
  /**
   * Test assertion Service-1060. For each Service  component in the {services} property 
   * of a Description component, the {name} property MUST be unique.
   * 
   * @param services An array containing all the services in the description component.
   * @param errorReporter The error reporter.
   * @return True if all services contain unique names, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionService1060(Service[] services, ErrorReporter errorReporter) throws WSDLException
  {
	List names = new ArrayList();
	int numServices = services.length;
	for(int i = 0; i < numServices; i++)
	{
	  QName name = services[i].getName();
      if(name == null)
        continue;
	  if(names.contains(name))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "Service-1060", new Object[]{name}, ErrorReporter.SEVERITY_ERROR);
		return false;
	  }
	  else
	  {
		names.add(name);
	  }
	}
	return true;
  }
  
  /**
   * Test assertion Endpoint-1061. This xs:anyURI MUST be an absolute IRI as 
   * defined by [IETF RFC 3987]. This xs:anyURI refers to the address IRI.
   * 
   * @param endpoint The endpoint of which the address should be checked.
   * @param errorReporter The error reporter.
   * @return True if the address IRI is absolute, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionEndpoint1061(Endpoint endpoint, ErrorReporter errorReporter) throws WSDLException
  {
	URI address = endpoint.getAddress();
    if(address != null && !address.isAbsolute())
    {
      errorReporter.reportError(new ErrorLocatorImpl(), "Endpoint-1061", new Object[]{address}, ErrorReporter.SEVERITY_ERROR);
	  return false;
    }
    return true;
  }
  
  /**
   * Test assertion Endpoint-1062. For each Endpoint component in the {endpoints} property 
   * of a Service component, the {binding} property MUST either be a Binding component with 
   * an unspecified {interface} property or a Binding component with an {interface} property 
   * equal to the {interface} property of the Service component.
   * 
   * @param endpoint The endpoint of which the binding should be checked.
   * @param errorReporter The error reporter.
   * @return True if the binding specified the interface specified by the service or no interface, false otherwise.
   * @throws WSDLException
   */
  protected boolean testAssertionEndpoint1062(Endpoint endpoint, ErrorReporter errorReporter) throws WSDLException
  {
	Binding binding = endpoint.getBinding();
	// If no binding has been specified this assertion does not apply.
	if(binding == null)
	  return true;
	
	Interface bindingInterface = binding.getInterface();
	WSDLComponent parent = endpoint.getParent();
	if(parent != null)
	{
	  Service service = (Service)parent;
	  Interface serviceInterface = service.getInterface();
	  
	  // If an interface hasn't been specified on the service this assertion doesn't apply.
	  // If the binding interface is null this assertion passes.
	  if(serviceInterface != null && bindingInterface != null && !serviceInterface.isEquivalentTo(bindingInterface))
	  {
		errorReporter.reportError(new ErrorLocatorImpl(), "Endpoint-1062", new Object[]{binding, bindingInterface, serviceInterface}, ErrorReporter.SEVERITY_ERROR);
		return false;  
	  }
	}
	return true;
  }
}
