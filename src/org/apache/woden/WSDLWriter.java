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
package org.apache.woden;

import java.io.Writer;
import java.io.OutputStream;
import org.apache.woden.wsdl20.xml.DescriptionElement;
/**
 * This interface describes a collection of methods
 * that allow a WSDL model to be written to a writer
 * in an XML format that follows the WSDL schema.
 *
 * Based on wsdl4j WSDLWriter.
 *
 *@author Sagara Gunathunga (sagara.gunathunga@gmail.com)
 */
public interface WSDLWriter {

    /**
     * Sets the specified feature to the specified value.
     * <p>
     * There are no minimum features that must be supported.
     * <p>
     * All feature names must be fully-qualified, Java package style. All
     * names starting with javax.wsdl. are reserved for features defined
     * by the JWSDL specification. It is recommended that implementation-
     * specific features be fully-qualified to match the package name
     * of that implementation. For example: com.abc.featureName
     *
     * @param name the name of the feature to be set.
     * @param value the value to set the feature to.
     * @throws IllegalArgumentException if the feature name is not recognized.
     * @see #getFeature(String)
     */

    public void setFeature(String name, boolean value)
    throws IllegalArgumentException;

    /**
     * Gets the value of the specified feature.
     *
     * @param name the name of the feature to get the value of.
     * @return the value of the feature.
     * @throws IllegalArgumentException if the feature name is not recognized.
     * @see #setFeature(String, boolean)
     */

    public boolean getFeature(String name) throws IllegalArgumentException;

    /**
     * Write the specified WSDL Description to the specified Writer.
     *
     * @param wsdlDes the WSDL description to be written.
     * @param sink the Writer to write the xml to.
     */
    public void writeWSDL(DescriptionElement wsdlDes, Writer sink)
      throws WSDLException;
    /**
     * Write the specified WSDL Description to the specified OutputStream.
     *
     * @param wsdlDes the WSDL description to be written.
     * @param sink the OutputStream to write the xml to.
     */
    public void writeWSDL(DescriptionElement wsdlDes, OutputStream sink)
      throws WSDLException;

}
