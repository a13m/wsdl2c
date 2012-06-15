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
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * This interface should be implemented by classes which deserialize
 * org.w3c.dom.Elements into extension-specific instances of ExtensionElement.
 * <p>
 * Copied from WSDL4J.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 * @author jkaputin@apache.org (modified for Woden)
 */
public interface ExtensionDeserializer
{
  /**
   * This method deserializes elements into instances of classes
   * which implement the ExtensionElement interface. The
   * return value should be explicitly cast to the more-specific
   * implementing type.
   *
   * @param parentType a class object indicating where in the WSDL
   * document this extension element was encountered. For
   * example, org.apache.woden.Binding.class would be used to indicate
   * this element was encountered as an immediate child of
   * a &lt;wsdl:binding&gt; element.
   * @param parent the parent object of this extension element 
   * @param extType the qname of the extension element to deserialize
   * @param extEl the extension element
   * @param desc the &lt;description&gt; this extension element was
   * encountered in
   * @param extReg the ExtensionRegistry to use (if needed again)
   */
  public ExtensionElement unmarshall(Class parentType,
                                     Object parent,
                                     QName extType,
                                     XMLElement extEl,
                                     DescriptionElement desc,
                                     ExtensionRegistry extReg)
                                       throws WSDLException;
}