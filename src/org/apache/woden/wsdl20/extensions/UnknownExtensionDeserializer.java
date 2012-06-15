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
package org.apache.woden.wsdl20.extensions;

import javax.xml.namespace.QName;

import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
//TODO remove internals from the API: import org.apache.woden.internal.util.dom.DOMUtils;
//TODO remove internals from the API: import org.apache.woden.internal.wsdl20.Constants;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

/**
 * This class is used to deserialize arbitrary elements into
 * UnknownExtensionElement instances.
 * <p>
 * Copied from WSDL4J.
 *
 * @see UnknownExtensionElement
 * @see UnknownExtensionSerializer
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class UnknownExtensionDeserializer implements ExtensionDeserializer
{
  public ExtensionElement unmarshall(Class parentType,
                                     Object parent,
                                     QName extType,
                                     XMLElement extEl,
                                     DescriptionElement desc,
                                     ExtensionRegistry extReg)
                                           throws WSDLException
  {
    UnknownExtensionElement unknownExt = new UnknownExtensionElement();
    //TODO remove internals from the API ... DOMUtils and Constants
    //String requiredStr = DOMUtils.getAttributeNS(el,
    //                                             Constants.NS_URI_WSDL20,
    //                                             Constants.ATTR_REQUIRED);
    String requiredStr = getAttributeNS(extEl,
                                        "http://www.w3.org/ns/wsdl",
                                        "required");

    unknownExt.setExtensionType(extType);

    if (requiredStr != null)
    {
      unknownExt.setRequired(new Boolean(requiredStr));
    }

    unknownExt.setElement(extEl);

    return unknownExt;
  }

  //Method copied from DOMUtils, to avoid using internal classes
  //here in the API packages (it was breaking the API build).
  //TODO workaround for M2, replace with new XMLElement behaviour.
  private String getAttributeNS (XMLElement xmlElement,
                                 String namespaceURI,
                                 String localPart) {
      String sRet = null;

      if (xmlElement.getSource() instanceof Element){
          Element el = (Element)xmlElement.getSource();
          Attr   attr = el.getAttributeNodeNS (namespaceURI, localPart);

          if (attr != null) {
              sRet = attr.getValue ();
          }
      }

      return sRet;
  }

}