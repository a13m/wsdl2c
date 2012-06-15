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

import java.net.URI;

/**
 * This interface represents WSDL source in a format to be interpreted by the 
 * WSDLReader implementation. It permits WSDL source in various forms to be 
 * passed to the WSDLReader without making the WSDLReader API dependent on 
 * any particular XML parser or XML object model.
 * <p>
 * Each concrete implementation of WSDLReader will have a concrete implementation
 * of WSDLSource that can handle the types of WSDL source formats that are
 * compatible with the WSDLReader implementation. For example, a DOM-based 
 * implementation of WSDLReader will return a DOM-based implementation of 
 * WSDLSource via its <code>WSDLReader.createWSDLSource</code> method and this
 * DOM-based WSDLSource implemenation will accept as WSDL source an
 * org.w3c.dom.Element or org.w3c.dom.Document object via its
 * <code>WSDLSource.setSource</code> method.
 * <p>
 * The WSDL source is set via the <code>setSource(java.lang.Object)</code>
 * method. Runtime type safety should be provided in the implementation of 
 * the <code>setSource</code> method, which should check that the Object 
 * argument is of a type compatible with the WSDLReader implementation that 
 * created the WSDLSource object.
 * <p>
 * Programming example:
 * <pre>
 *   //wsdlURI is the URI of the base wsdl document.
 *   //domReader is a DOM-based implementation of WSDLReader
 *   //domElement is an org.w3c.dom.Element representing a &lt;wsdl:description&gt; element.
 * 
 *   WSDLSource wsdlSource = domReader.createWSDLSource();
 *   wsdlSource.setBaseURI(wsdlURI);
 *   wsdlSource.setSource(domElement);
 *   DescriptionElement desc = reader.readWSDL(wsdlSource);
 * </pre>
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface WSDLSource {
    
	/**
	 * Store the specified source object representing the WSDL.
	 * 
	 * @param wsdlSource the WSDL source object
	 * 
	 * @throws IllegalArgumentException if the specified object type is not 
	 * recognized by the WSDLSource implementation. 
	 */
    public void setSource(Object wsdlSource);
    
    /**
     * Returns the implementation specific object representing the WSDL source
     * (for example, a DOM Element or Document or an Axiom OMElement). The caller should
     * cast this object to the appropriate type to use its interface.
     * 
     * @return the Object representing the WSDL source
     */
    public Object getSource();
    
    /**
     * Store the base URI of the WSDL source document.
     * 
     * @param baseURI the URI of the WSDL document.
     */
    public void setBaseURI(URI baseURI);
    
    /**
     * 
     * @return the base URI of the WSDL
     */
    public URI getBaseURI();
}
