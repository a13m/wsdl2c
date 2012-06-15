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

import org.apache.woden.XMLElement;

import javax.xml.namespace.*;

/**
 * This class is used to wrap arbitrary elements.
 * <p>
 * Based on the same class from WSDL4J.
 *
 * @see UnknownExtensionSerializer
 * @see UnknownExtensionDeserializer
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 * @author jkaputin@apache.org
 */
public class UnknownExtensionElement implements ExtensionElement
{
  protected QName elementQN = null;
  // Uses the wrapper type so we can tell if it was set or not.
  protected Boolean required = null;
  protected XMLElement element = null;

  /**
   * Set the type of this extensibility element.
   *
   * @param elementQN the type
   */
  public void setExtensionType(QName elementQN)
  {
    this.elementQN = elementQN;
  }

  /**
   * Get the type of this extensibility element.
   *
   * @return the extensibility element's type
   */
  public QName getExtensionType()
  {
    return elementQN;
  }

  /**
   * Set whether or not the semantics of this extension
   * are required. Relates to the wsdl:required attribute.
   */
  public void setRequired(Boolean required)
  {
    this.required = required;
  }

  /**
   * Get whether or not the semantics of this extension
   * are required. Relates to the wsdl:required attribute.
   */
  public Boolean isRequired()
  {
    return required;
  }

  /**
   * Set the Element for this extensibility element.
   *
   * @param element the unknown element that was encountered
   */
  public void setElement(XMLElement element)
  {
    this.element = element;
  }

  /**
   * Get the Element for this extensibility element.
   *
   * @return the unknown element that was encountered
   */
  public XMLElement getElement()
  {
    return element;
  }

  public String toString()
  {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append("UnknownExtensionElement (" + elementQN + "):");
    strBuf.append("\nrequired=" + required);

    if (element != null)
    {
      strBuf.append("\nelement=" + element);
    }

    return strBuf.toString();
  }
}