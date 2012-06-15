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
package org.apache.woden.wsdl20.extensions.soap;

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.extensions.AttributeExtensible;
import org.apache.woden.wsdl20.extensions.ElementExtensible;
import org.apache.woden.wsdl20.extensions.ExtensionElement;
import org.apache.woden.wsdl20.xml.DocumentationElement;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.apache.ws.commons.schema.XmlSchemaElement;

/**
 * This interface represents the &lt;wsoap:header&gt; extension element that 
 * can appear within a Binding Fault or Binding Message Reference.
 * 
 * @author jkaputin@apache.org
 */
public interface SOAPHeaderBlockElement extends ExtensionElement, 
                                                AttributeExtensible, 
                                                ElementExtensible
{
    /**
     * Set the QName that identifies the Schema element declaration
     * relating to this soap header.
     * 
     * @param qname the QName that identifies a Schema element declaration
     */
    public void setElementName(QName qname);
    public QName getElementName();
    
    /**
     * Returns the Schema element declaration identified by the QName in the 'element' 
     * attribute of the &lt;wsoap:header&gt; element. 
     * If this QName does not resolve to an element declaration in a schema that is visible 
     * to the containing WSDL description, null will be returned by this method. 
     * To be visible, the Schema must have been correctly imported or inlined within 
     * the &lt;types&gt; element.
     * 
     * @return the XmlSchemaElement identified by the 'element' attribute
     */
    public XmlSchemaElement getElement();
  
    public void setMustUnderstand(Boolean understood);
    
    public Boolean mustUnderstand();
    
    public void setParentElement(WSDLElement wsdlEl);
    
    public WSDLElement getParentElement();

    public void addDocumentationElement(DocumentationElement docEl);
    
    public DocumentationElement[] getDocumentationElements();
}
