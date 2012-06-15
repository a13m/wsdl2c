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
//TODO remove internals from the API: import org.apache.woden.internal.util.dom.DOM2Writer;
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * This class is used to serialize UnknownExtensionElement instances
 * into the PrintWriter.
 * <p>
 * Copied from WSDL4J.
 *
 * @see UnknownExtensionElement
 * @see UnknownExtensionDeserializer
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public class UnknownExtensionSerializer implements ExtensionSerializer
{
  public void marshall(Class parentType,
                       QName elementType,
                       ExtensionElement extension,
                       PrintWriter pw,
                       DescriptionElement desc,
                       ExtensionRegistry extReg)
                         throws WSDLException
  {
//    UnknownExtensionElement unknownExt =
//      (UnknownExtensionElement)extension;

    pw.print("    ");

   //TODO remove internals from the API: DOM2Writer.serializeAsXML(unknownExt.getElement(), pw);

    pw.println();
  }
}