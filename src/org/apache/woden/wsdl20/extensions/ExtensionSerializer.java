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

import java.io.PrintWriter;

import javax.xml.namespace.QName;

import org.apache.woden.WSDLException;
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * This interface should be implemented by classes which serialize
 * extension-specific instances of ExtensibilityElement into the
 * PrintWriter.
 * <p>
 * Copied from WSDL4J.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public interface ExtensionSerializer
{
  /**
   * This method serializes extension-specific instances of
   * ExtensibilityElement into the PrintWriter.
   *
   * @param parentType a class object indicating where in the WSDL
   * definition this extension was encountered. For
   * example, org.apache.woden.Binding.class would be used to indicate
   * this extensibility element was found in the list of
   * extensibility elements belonging to a org.apache.woden.Binding.
   * @param elementType the qname of the extensibility element
   * @param extension the extensibility element to serialize
   * @param pw the print writer on which to serialize the extension
   * @param desc the description element this extensibility element was
   * encountered in
   * @param extReg the ExtensionRegistry to use (if needed again)
   */
  public void marshall(Class parentType,
                       QName elementType,
                       ExtensionElement extension,
                       PrintWriter pw,
                       DescriptionElement desc,
                       ExtensionRegistry extReg)
                         throws WSDLException;
}