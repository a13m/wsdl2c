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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.util.dom.DOM2Writer;
import org.apache.woden.internal.util.dom.DOMUtils;
import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.schema.ImportedSchema;
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.types.NCName;
import org.apache.woden.types.NamespaceDeclaration;
import org.apache.woden.types.QNameTokenUnion;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.extensions.ExtensionSerializer;
import org.apache.woden.wsdl20.xml.BindingElement;
import org.apache.woden.wsdl20.xml.BindingFaultElement;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
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
import org.apache.woden.xml.XMLAttr;
import org.w3c.dom.Node;

/**
 * This is a concrete class derived from BaseWSDLWriter
 * class and implements method to serialize each WSDL2.0
 * element based on DOM and also WriteWSDL() methods of
 * the  WSDLWriter interface .
 *
 *
 * @author Sagara Gunathunga (sagara.gunathunga@gmail.com)
 */

public class DOMWSDLWriter extends BaseWSDLWriter
{
    public DOMWSDLWriter(WSDLContext wsdlContext)
    {
        super(wsdlContext);

    }


    /**
     * This method write the specified WSDL Description to
     * the specified Writer.
     *
     * @param wsdlDes the WSDL Description to be written.
     * @param sink the Writer to write the xml to.
     */
    public void writeWSDL(DescriptionElement wsdlDes, Writer sink)
        throws WSDLException {

        PrintWriter pw = new PrintWriter(sink);
        String javaEncoding = (sink instanceof OutputStreamWriter)
                              ? ((OutputStreamWriter)sink).getEncoding()
                              : null;
        String xmlEncoding = DOM2Writer.java2XMLEncoding(javaEncoding);
        if (xmlEncoding == null)
       {
           throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                   "Unsupported Java encoding for writing " +
                   "wsdl file: '" + javaEncoding + "'.");
       }
        pw.println(Constants.XML_DECL_START +
               xmlEncoding +
               Constants.XML_DECL_END);
        printDescription(wsdlDes, pw);
    }



    /**
     * This method write the specified WSDL Description to
     * the specified Writer.
     *
     * @param wsdlDes the WSDL Description to be written.
     * @param sink the OutputStream to write the xml to.
     */
    public void writeWSDL(DescriptionElement wsdlDes, OutputStream sink)
        throws WSDLException {

        Writer writer = null;
        try{
            writer = new OutputStreamWriter(sink, "UTF8");

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            writer = new OutputStreamWriter(sink);
        }
        writeWSDL(wsdlDes, writer);
    }


    /**
     * Write the specified WSDL DescriptionElement and it's
     * child elements to  the specified Writer.
     *
     * @param desEle the WSDL Description to be written.
     * @param sink the Writer to write the xml to.
     */
    protected void printDescription(DescriptionElement desEle, PrintWriter pw)
        throws WSDLException {

        if (desEle == null){
            return;
        }
        if (desEle.getNamespacePrefix(Constants.NS_URI_WSDL20) == null){
            String prefix = "wsdl";
            int subscript = 0;
            while (desEle.getNamespaceURI(prefix) != null){
                prefix = "wsdl" + subscript++;
            }
            desEle.addNamespace(prefix, Constants.NS_URI_WSDL20);
        }
        String tagName =DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                Constants.ELEM_DESCRIPTION, desEle);
        pw.print('<' + tagName);
        URI targetNamespace=desEle.getTargetNamespace();
        if(targetNamespace!=null){
            String targetNamespaceStr = targetNamespace.toString();            
            DOMUtils.printAttribute(Constants.ATTR_TARGET_NAMESPACE,
                                    targetNamespaceStr,
                                    pw);            
            
        } 
        NamespaceDeclaration[] namespaces = desEle.getDeclaredNamespaces();
        printExtensibilityAttributes(desEle.getExtensionAttributes(), desEle, pw);
        printNamespaceDeclarations(namespaces, pw);
        pw.println('>');
        printDocumentation(desEle.getDocumentationElements(), desEle, pw);
        printImports(desEle.getImportElements(), desEle, pw);
        printIncludes(desEle.getIncludeElements(), desEle, pw);
        printTypes(desEle.getTypesElement(), desEle, pw);
        printInterfaces(desEle.getInterfaceElements(), desEle, pw);
        printBindings(desEle.getBindingElements(), desEle, pw);
        printServices(desEle.getServiceElements(), desEle, pw);
        printExtensibilityElements(desEle.getClass(), desEle.getExtensionElements(), desEle, pw);
        pw.println("</" + tagName + '>');
        pw.flush();
    }


    /**
     * Serialize  the namespace declarations of the  WSDL Description.
     *
     * @param namespaces a java.util.Map contains namespace of WSDL Description.
     * @param pw the Writer to write the xml to.
     */
    protected void printNamespaceDeclarations(NamespaceDeclaration[] namespaces, PrintWriter pw)
        throws WSDLException {

        if (namespaces != null){
            int len = namespaces.length;
            for(int i=0; i<len; i++){
                String prefix = namespaces[i].getPrefix();
                if (prefix == null){
                    prefix = "";
                }
                DOMUtils.printAttribute(Constants.ATTR_XMLNS +
                        (!prefix.equals("") ? ":" + prefix : ""),
                        namespaces[i].getNamespaceURI().toString(),
                        pw);
            }
        }

    }


    /**
     * Serialize  the ImportElements of the  WSDL element model.
     *
     * @param imports an array of ImportElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printImports(ImportElement[] imports,
                                DescriptionElement des,
                                PrintWriter pw)
                                throws WSDLException
    {
        if (imports != null){
            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                           Constants.ELEM_IMPORT,
                           des);
            for(int ind=0;ind<imports.length;ind++){
                ImportElement importEle = imports[ind];
                if (importEle!=null){
                    pw.print("  <" + tagName);

                    String namespace = importEle.getNamespace().toString();
                    if (namespace != null){
                        DOMUtils.printAttribute(Constants.ATTR_NAMESPACE,
                                namespace,
                                pw);
                    }
                    String location = importEle.getLocation().toString();
                    if (location != null){
                        DOMUtils.printAttribute(Constants.ATTR_LOCATION,
                                location,
                                pw);
                    }
                    printExtensibilityAttributes(importEle.getExtensionAttributes(), importEle, pw);
                    pw.println('>');
                    printDocumentation(importEle.getDocumentationElements(), des, pw);
                    printExtensibilityElements(importEle.getClass(), importEle.getExtensionElements(), des, pw);
                    pw.println("  </" + tagName + '>');
                }
            }
        }

    }

    /**
     * Serialize  the IncludeElements of the  WSDL element model.
     *
     * @param imports an array of IncludeElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printIncludes(IncludeElement[] includes,
                                 DescriptionElement des,
                                 PrintWriter pw)
                                 throws WSDLException
     {
        if (includes != null){
            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                           Constants.ELEM_INCLUDE,
                           des);
            for(int ind=0;ind<includes.length;ind++){
                IncludeElement includeEle = includes[ind];
                if (includeEle!=null){
                    pw.print("  <" + tagName);

                    String location = includeEle.getLocation().toString();
                    if (location != null){
                        DOMUtils.printAttribute(Constants.ATTR_LOCATION,
                                location,
                                pw);
                    }
                    printExtensibilityAttributes(includeEle.getExtensionAttributes(),includeEle, pw);
                    pw.println('>');
                    printDocumentation(includeEle.getDocumentationElements(), des, pw);
                    printExtensibilityElements(includeEle.getClass(), includeEle.getExtensionElements(), des, pw);
                    pw.println("  </" + tagName + '>');
                }
            }
        }


    }


    /**
     * Serialize  the InterfaceElements of the  WSDL element model.
     *
     * @param intrfaces an array of intrfacesElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printInterfaces(InterfaceElement[] intrfaces,
                                   DescriptionElement des,
                                   PrintWriter pw)
                                   throws WSDLException
     {
        if (intrfaces != null){
            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                           Constants.ELEM_INTERFACE,
                           des);
            for(int ind=0;ind<intrfaces.length;ind++){
                InterfaceElement intrface = intrfaces[ind];
                if (intrface!=null){
                    pw.print("  <" + tagName);
                    QName name = intrface.getName();
                    if (name != null){
                        DOMUtils.printAttribute(Constants.ATTR_NAME,
                                name.getLocalPart(),
                                pw);
                    }

                    QName[] extendedInterfaces = intrface.getExtendedInterfaceNames();
                    String attrExtendedInterfaces="";
                    for(int i=0;i<extendedInterfaces.length;i++){                    	
                        if(extendedInterfaces[i]!=null){
                        	attrExtendedInterfaces=attrExtendedInterfaces + DOMUtils.
                        					getQualifiedValue(extendedInterfaces[i].getNamespaceURI(), 
                        							extendedInterfaces[i].getLocalPart(), des);
                        	if(i!=extendedInterfaces.length-1){
                        		attrExtendedInterfaces=attrExtendedInterfaces+ " ";
                        		
                        	}
                           
                        }
                    }
                    if(extendedInterfaces.length>0){
                    	DOMUtils.printAttribute(Constants.ATTR_EXTENDS,attrExtendedInterfaces,pw);                    	
                    }
                    

                    URI[] styleDefaults = intrface.getStyleDefault();
                    for(int i=0;i<styleDefaults.length;i++){

                        URI styleDefault=styleDefaults[i];

                        if(styleDefault!=null){

                            DOMUtils.printAttribute(
                                    Constants.ATTR_STYLE_DEFAULT,
                                    styleDefault.toString(),
                                    pw);
                        }
                    }
                    printExtensibilityAttributes(intrface.getExtensionAttributes(), intrface, pw);
                    pw.println('>');
                    printDocumentation(intrface.getDocumentationElements(), des, pw);
                    printOperations(intrface.getInterfaceOperationElements(), des, pw);
                    printFaults(intrface.getInterfaceFaultElements(), des, pw);                   
                    printExtensibilityElements(intrface.getClass(), intrface.getExtensionElements(), des, pw);
                    pw.println("  </" + tagName + '>');
                }
            }
        }
     }

    /**
     * Serialize  the InterfaceOperationElements of the  WSDL element model.
     *
     * @param operations an array of InterfaceOperationElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printOperations(InterfaceOperationElement[] operations,
                                   DescriptionElement des,
                                   PrintWriter pw)
                                   throws WSDLException
     {
        if (operations != null){

            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                          Constants.ELEM_OPERATION,
                          des);
            for(int ind=0;ind<operations.length;ind++){

                InterfaceOperationElement operation =operations[ind] ;
                if (operation!=null){

                    pw.print("    <" + tagName);

                    QName name=operation.getName();
                    if(name!=null){
                        DOMUtils.printAttribute(Constants.ATTR_NAME,
                                name.getLocalPart(),
                                pw);
                    }

                    URI pattern=operation.getPattern();
                    if(pattern!=null){
                        DOMUtils.printAttribute(Constants.ATTR_PATTERN,
                                pattern.toString(),
                                pw);
                    }

                    URI[] styles=operation.getStyle();
                    for(int i=0;i<styles.length;i++){
                        if(styles[i]!=null){

                            DOMUtils.printAttribute(
                                    Constants.ATTR_STYLE,
                                    styles[i].toString(),
                                    pw);
                        }
                    }
                    printExtensibilityAttributes(operation.getExtensionAttributes(), operation, pw);
                    pw.println('>');
                    printDocumentation(operation.getDocumentationElements(), des, pw);
                    printInterfaceMessageReferences(operation.getInterfaceMessageReferenceElements(),des, pw);
                    printInterfaceFaultReferences(operation.getInterfaceFaultReferenceElements(),des,pw);
                    printExtensibilityElements(operation.getClass(), operation.getExtensionElements(), des, pw);
                    pw.println("    </" + tagName + '>');

                }

            }


        }



     }

    /**
     * Serialize  the InterfaceMessageReferenceElements of the  WSDL element model.
     *
     * @param msgrefs an array of InterfaceMessageReferenceElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printInterfaceMessageReferences( InterfaceMessageReferenceElement[] msgrefs,
            DescriptionElement des,
            PrintWriter pw)
              throws WSDLException
     {

        for(int ind=0;ind<msgrefs.length;ind++){
            InterfaceMessageReferenceElement msgRef=msgrefs[ind];
            if(msgRef!=null){

                String tagName =null;
                Direction msgDirection=msgRef.getDirection();
                if(msgDirection==Direction.IN){
                    tagName=DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                             Constants.ELEM_INPUT,
                             msgRef);
                }else if(msgDirection==Direction.OUT){
                    tagName=DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                             Constants.ELEM_OUTPUT,
                             msgRef);            }

            pw.print("      <" + tagName);

            String msglable=msgRef.getMessageLabel().toString();
            DOMUtils.printAttribute(Constants.ATTR_MESSAGE_LABEL, msglable, pw);

            QNameTokenUnion qtu = msgRef.getElement();
            if(qtu != null) {
                if(qtu.isQName()) {
                    QName element=qtu.getQName();
                    URI ns=msgRef.getNamespaceURI(element.getPrefix());
                    String attrName;
                    if(ns!=null){
                        attrName=
                            DOMUtils.getQualifiedValue(msgRef.getNamespaceURI(element.getPrefix()).toString(),
                                    element.getLocalPart(), msgRef);
                    }else{
                        attrName=element.getLocalPart();
                    }
                    DOMUtils.printAttribute(Constants.ATTR_ELEMENT, attrName, pw);
                } else {
                    //qtu is a Token
                    DOMUtils.printAttribute(Constants.ATTR_ELEMENT, qtu.getToken(), pw);
                }
            }
            printExtensibilityAttributes(msgRef.getExtensionAttributes(), msgRef, pw);
            pw.println('>');
            printDocumentation(msgRef.getDocumentationElements(), des, pw);
            printExtensibilityElements(msgRef.getClass(), msgRef.getExtensionElements(), des, pw);
            pw.println("    </" + tagName + '>');
        }
      }
    }

    /**
     * Serialize  the InterfaceFaultReferenceElements of the  WSDL element model.
     *
     * @param faulRefs an array of InterfaceFaultReferenceElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printInterfaceFaultReferences(InterfaceFaultReferenceElement[] faulRefs,
                                                        DescriptionElement des,
                                                        PrintWriter pw)
                                                        throws WSDLException{
        for(int ind=0;ind<faulRefs.length;ind++){

            InterfaceFaultReferenceElement faulRef=faulRefs[ind];
            if(faulRef!=null){

                String tagName =null;
                Direction msgDirection=faulRef.getDirection();
                if(msgDirection==Direction.IN){
                    tagName=DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                            Constants.ELEM_INFAULT,
                            faulRef);
                }else if(msgDirection==Direction.OUT){

                    tagName=DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                            Constants.ELEM_OUTFAULT,
                            faulRef);

                }

                pw.print("      <" + tagName);

                String msglable=faulRef.getMessageLabel().toString();

                DOMUtils.printAttribute(Constants.ATTR_MESSAGE_LABEL, msglable, pw);


                QName attrQName=faulRef.getRef();
                String attrName=
                    DOMUtils.getQualifiedValue(faulRef.getNamespaceURI(attrQName.getPrefix()).toString(),
                        attrQName.getLocalPart(),
                        faulRef);
                DOMUtils.printAttribute(Constants.ATTR_REF, attrName, pw);
                
                printExtensibilityAttributes(faulRef.getExtensionAttributes(), faulRef, pw);
                pw.println('>');
                printDocumentation(faulRef.getDocumentationElements(), des, pw);
                printExtensibilityElements(faulRef.getClass(), faulRef.getExtensionElements(), des, pw);
                pw.println("    </" + tagName + '>');
            }



        }


    }
    
   
    /**
     * Serialize  the InterfaceInterfaceFaultElement of the  WSDL element model.
     *
     * @param faulEles an array of InterfaceFaultElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printFaults(InterfaceFaultElement[] faulEles,
                                                        DescriptionElement des,
                                                        PrintWriter pw)
                                                        throws WSDLException{
        for(int ind=0;ind<faulEles.length;ind++){

        	InterfaceFaultElement faulEle=faulEles[ind];
            if(faulEle!=null){

                String tagName =null;
                
                    tagName=DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                            Constants.ELEM_FAULT,
                            faulEle);
               

                

                pw.print("      <" + tagName);
                
                String attrName=faulEle.getName().getLocalPart();                   
                DOMUtils.printAttribute(Constants.ATTR_NAME, attrName, pw);
                //TODO check here - returns  QNameTokenUnion but only take QName
                QName attrElement=faulEle.getElement().getQName();
                String attrEle=
                    DOMUtils.getQualifiedValue(faulEle.getNamespaceURI(attrElement.getPrefix()).toString(),
                    		attrElement.getLocalPart(),
                        faulEle);
                DOMUtils.printAttribute(Constants.ATTR_ELEMENT, attrEle, pw);
                
                printExtensibilityAttributes(faulEle.getExtensionAttributes(), faulEle, pw);
                pw.println('>');
                printDocumentation(faulEle.getDocumentationElements(), des, pw);
                printExtensibilityElements(faulEle.getClass(), faulEle.getExtensionElements(), des, pw);
                pw.println("    </" + tagName + '>');
            }



        }


    }


    /**
     * Serialize  the printBindings of the  WSDL element model.
     *
     * @param bindings an array of printBindings.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printBindings(BindingElement[] bindings,
                                 DescriptionElement des,
                                 PrintWriter pw)
                                 throws WSDLException
    {
        String tagName =
            DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                        Constants.ELEM_BINDING,
                        des);
        for(int ind=0;ind<bindings.length;ind++){

            BindingElement binding=bindings[ind];
            if (bindings != null){

                pw.print("  <" + tagName);
                QName name = binding.getName();
                if (name != null){
                    DOMUtils.printAttribute(Constants.ATTR_NAME,
                            name.getLocalPart(),
                            pw);
                }

                InterfaceElement intrface = binding.getInterfaceElement();
                if (intrface != null){


                    QName interfaceName=intrface.getName();
                    if(interfaceName!=null)
                    DOMUtils.printQualifiedAttribute(Constants.ATTR_INTERFACE,
                            interfaceName,
                            des,
                            pw);

                    DOMUtils.printAttribute(Constants.ATTR_TYPE,
                            binding.getType().toString(),
                            pw);
                    
                   printExtensibilityAttributes(binding.getExtensionAttributes(), binding, pw);                   
                }
                pw.println('>');
                printDocumentation(binding.getDocumentationElements(), des, pw);
                printExtensibilityElements(binding.getClass(), binding.getExtensionElements(), des, pw);
                printBindingOperations(binding.getBindingOperationElements(), des, pw);
                printBindingFaults(binding.getBindingFaultElements(), des, pw);
                pw.println("  </" + tagName + '>');
            }
        }
    }

    /**
     * Serialize  the BindingFaultElements of the  WSDL element model.
     *
     * @param faults an array of BindingFaultElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printBindingFaults(BindingFaultElement[] faults,
                                             DescriptionElement des,
                                             PrintWriter pw)
                                             throws WSDLException{
        if (faults != null){

            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                          Constants.ELEM_FAULT,
                          des);
            for(int ind=0;ind<faults.length;ind++){

                BindingFaultElement fault =faults[ind] ;
                if (fault!=null){

                    pw.print("    <" + tagName);
                    DOMUtils.printQualifiedAttribute(Constants.ATTR_REF,
                            fault.getRef(),
                             des,
                             pw);

                    printExtensibilityAttributes(fault.getExtensionAttributes(), fault, pw);
                    pw.println("/>");

                }
            }


        }

    }

    /**
     * Serialize  the BindingOperationElements of the  WSDL element model.
     *
     * @param operations an array of BindingOperationElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printBindingOperations(BindingOperationElement[] operations,
                                                 DescriptionElement des,
                                                 PrintWriter pw)
                                                 throws WSDLException
     {
        if (operations != null){

            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                          Constants.ELEM_OPERATION,
                          des);
            for(int ind=0;ind<operations.length;ind++){

                BindingOperationElement operation =operations[ind] ;
                if (operation!=null){

                    pw.print("    <" + tagName);
                    DOMUtils.printQualifiedAttribute(Constants.ATTR_REF,
                             operation.getRef(),
                             des,
                             pw);

                    printExtensibilityAttributes(operation.getExtensionAttributes(), operation, pw);
                    pw.println("/>");

                }
            }


        }

     }

    /**
     * Serialize  the ServiceElements of the  WSDL element model.
     *
     * @param services an array of ServiceElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printServices(ServiceElement[] services,
                                 DescriptionElement des,
                                 PrintWriter pw)
                                 throws WSDLException
     {
        String tagName =
            DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                        Constants.ELEM_SERVICE,
                        des);
        for(int ind=0;ind<services.length;ind++){

            ServiceElement service=services[ind];
            if(service!=null){

                pw.print("  <" + tagName);
                QName name = service.getName();
                if (name != null){
                    DOMUtils.printAttribute(Constants.ATTR_NAME,
                            name.getLocalPart(),
                            pw);
                }
                QName interfaceName = service.getInterfaceName();
                if (name != null){

                    DOMUtils.printQualifiedAttribute(Constants.ATTR_INTERFACE,
                            interfaceName,des, pw);
                }
                printExtensibilityAttributes(service.getExtensionAttributes(), service, pw);
                pw.println('>');
                printEndpoints(service.getEndpointElements(), des, pw);
                printDocumentation(service.getDocumentationElements(), des, pw);
                printExtensibilityElements(service.getClass(),service.getExtensionElements(), des, pw);
                pw.println("    </" + tagName + '>');
            }
        }
     }

    /**
     * Serialize  the EndpointElements of the  WSDL element model.
     *
     * @param endpoints an array of IncludeElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printEndpoints(EndpointElement[] endpoints,
                                 DescriptionElement des,
                                 PrintWriter pw)
                                 throws WSDLException
      {
        String tagName =
            DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                                       Constants.ELEM_ENDPOINT,
                                       des);
        for(int ind=0;ind<endpoints.length;ind++){

            EndpointElement endPoint=endpoints[ind];
            if(endPoint!=null){

                pw.print("    <" + tagName);
                NCName ncName=endPoint.getName();
                if(ncName!=null){
                    String name=ncName.toString();
                    DOMUtils.printAttribute(Constants.ATTR_NAME,
                            name, pw);
                    
                }                

                BindingElement binding =endPoint.getBindingElement();
                if (binding != null){
                    DOMUtils.printQualifiedAttribute(Constants.ATTR_BINDING,
                            binding.getName(),
                            des,
                            pw);
                }

                URI address =endPoint.getAddress();
                if (address != null){
                    DOMUtils.printAttribute(Constants.ATTR_ADDRESS,
                            address.toString(),
                            pw);
                }
                printExtensibilityAttributes(endPoint.getExtensionAttributes(), endPoint, pw);
                pw.println('>');
                printDocumentation(endPoint.getDocumentationElements(), des, pw);
                printExtensibilityElements(endPoint.getClass(), endPoint.getExtensionElements(), des, pw);
                pw.println("    </" + tagName + '>');
            }
        }
      }

    /**
     * Serialize  the TypesElements of the  WSDL element model.
     *
     *
     * @param types an array of TypesElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printTypes(TypesElement types,
                              DescriptionElement des,
                              PrintWriter pw)
                              throws WSDLException
    {
        if (types != null){


            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                                           Constants.ELEM_TYPES,
                                           des);
            pw.print("<" + tagName);
            printExtensibilityAttributes(types.getExtensionAttributes(), types, pw);
            pw.println('>');
            ExtensionElement[] extElements = types.getExtensionElements();
            printExtensibilityElements(types.getClass(), extElements, des, pw);
            printImportedSchemas(types.getImportedSchemas(),des,pw);
            printInlinedSchemas(types.getInlinedSchemas(),des,pw);
            pw.println("</" + tagName + '>');
        }
    }


    /**
     * Serialize  the InlinedSchemas of the  WSDL element model.
     *
     * @param inlinedSchema an array of InlinedSchemas.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printInlinedSchemas(InlinedSchema[] inlinedSchema,
                                              DescriptionElement des,
                                              PrintWriter pw)
                                              throws WSDLException
    {
       /* previous method
        *
        *  XmlSchema xs=null;
        // TODO used XmlSchema serialiser.Cause extra info like
        // attributeFormDefault="unqualified" elementFormDefault="unqualified" ..etc
        for(int i=0;i<inlinedSchema.length;i++){
            xs=inlinedSchema[i].getSchemaDefinition();
            xs.write(pw);
        }*/

    	for(int i=0;i<inlinedSchema.length;i++){
    		InlinedSchema schema=inlinedSchema[i];
    		XMLElement ele=schema.getXMLElement();
    		DOM2Writer.serializeAsXML(((Node)ele.getSource()), pw);

    	}
    }

    /**
     * Serialize  the ImportedSchemas of the  WSDL element model.
     *
     * @param importedSchema an array of ImportedSchemas.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printImportedSchemas(ImportedSchema[] importedSchema,
                                               DescriptionElement des,
                                               PrintWriter pw )
                                               throws WSDLException
    {
    	 // TODO Hard coded for XML schema 2000,complete for both 2000 and 2001
        String tagname=DOMUtils.getQualifiedValue(Constants.TYPE_XSD_2001,
                                                  Constants.ELEM_IMPORT,des);
        for(int i=0;i<importedSchema.length;i++){

            ImportedSchema schema=importedSchema[i];
            String ns=schema.getNamespace().toString();

            /*
        	 * This  ignore the schema import if it's for
        	 *  the W3C schema for XML Schema.
        	 */
            if(Constants.TYPE_XSD_2001.equals(ns)){

                // to be removed

            }else{

            pw.println("<"+tagname);
            DOMUtils.printAttribute(Constants.ATTR_NAMESPACE,
                    importedSchema[i].getNamespace().toString(),
                    pw);
            DOMUtils.printAttribute(Constants.ATTR_LOCATION,
                    importedSchema[i].getSchemaLocation().toString(),
                    pw);
            pw.print(" />");
            }
        }
    }

    /**
     * Serialize  the ExtensibilityElements of the  WSDL element model.
     *
     * @param parentType  parent class of the ExtensibilityElements.
     * @param extensibilityElements an array of ExtensibilityElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printExtensibilityElements(Class parentType,
                                              ExtensionElement[] extensibilityElements,
                                              DescriptionElement def,
                                              PrintWriter pw)
                                              throws WSDLException{
        if (extensibilityElements != null){
            for(int ind=0;ind<extensibilityElements.length;ind++){
                ExtensionElement ext =extensibilityElements[ind];
                QName elementType = ext.getExtensionType();
                ExtensionRegistry extReg = fWsdlContext.extensionRegistry;
                if (extReg == null){
                    throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                            "No ExtensionRegistry set for this " +
                            "Description, so unable to serialize a '" +
                            elementType +
                            "' element in the context of a '" +
                            parentType.getName() + "'.");
                }
                ExtensionSerializer extSer = extReg.querySerializer(parentType,
                        elementType);
                extSer.marshall(parentType, elementType, ext, pw, def, extReg);
            }

        }

    }



    /**
     * Serialize  the printExtensibilityAttributess of the  WSDL element model.
     *
     * @param attrExts an array of XMLAttrs.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printExtensibilityAttributes(
                                      XMLAttr[] attrExts,
                                      WSDLElement ownerElem,
                                      PrintWriter pw)
                                      throws WSDLException
       {
        if(attrExts!=null){
            for(int index=0;index<attrExts.length;index++){
                XMLAttr attrExt=attrExts[index];
                if(attrExt!=null){
                    QName ns=attrExt.getAttributeType();
                    String attrName;
                    String attrLocalName=ns.getLocalPart();
                    String attrValue=attrExt.toExternalForm();
                    if(ns!=null){
                        String attrPrefix=ns.getPrefix();
                        attrName=
                            DOMUtils.getQualifiedValue(ownerElem.getNamespaceURI(attrPrefix).toString(),
                                    attrLocalName, ownerElem);
                    }else{
                        attrName=attrLocalName;
                    }
                    DOMUtils.printAttribute(attrName, attrValue, pw);
                }
            }

        }
    }



    /**
     * Serialize  the DocumentationElements of the  WSDL element model.
     *
     * @param docEles an array of DocumentationElements.
     * @param des corresponding  DescriptionElement.
     * @param pw the Writer to write the xml to.
     */
    protected void printDocumentation(DocumentationElement[] docEles,
                                       DescriptionElement def,
                                       PrintWriter pw)
                                       throws WSDLException
    {
        if (docEles != null){

            String tagName =
                DOMUtils.getQualifiedValue(Constants.NS_URI_WSDL20,
                          Constants.ELEM_DOCUMENTATION,
                          def);
            for(int ind=0;ind<docEles.length;ind++){
                DocumentationElement docEle =docEles[ind] ;
                if (docEle!=null){
                    pw.print("    <" + tagName);

                    // TODO
                    //chek is documentation requred these two methods

                    //printExtensibilityAttributes(Operation.class, docEles.get, def, pw);
                    pw.println('>');
                    Object o = docEle.getContent().getSource();
                    org.w3c.dom.Element domEl = (org.w3c.dom.Element)o;
                    /*
                     * This is not enough to handle DocumentationElement
                     * with nested DocumentationElements,if it is required
                     * a recursive method based on getNodeType()
                     * (org.w3c.dom.Node) can be used to solve this issue.
                     */
                    DOM2Writer.serializeAsXML(domEl.getFirstChild(), pw);
                  //  printExtensibilityElements(Operation.class, extElements, def, pw);
                    pw.println("    </" + tagName + '>');

                }
            }
        }
    }

}
