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
 * 
 *  @author Graham Turrell
 */
package org.apache.woden.resolver;

import java.io.IOException;
import java.net.URI;

import org.apache.woden.WSDLException;

/** 
 *Implementations of this interface may be used to specify a custom URI resolver.
 *Such an implementation can then be used to override the default Woden URI Resolver.
 *To associate a URI resolver programmatically, the following should be called prior to parser invocation.
 *<p>Example:
 *<br>WSDLFactory factory = WSDLFactory.newInstance();
 *<br>WSDLReader reader = factory.newWSDLReader();
 *<br>// MyURIResolver implements this interface ...
 *<br>URIResolver resolver = new MyURIResolver(); 
 *<br>reader.setURIResolver(resolver);
 *<br>...
 *<br>// Then, can parse a document and the assigned resolver will be used internally...
 *<br>Description desc = reader.readWSDL("http://myplace/mydoc.wsdl");
 *@see org.apache.woden.WSDLFactory
 *@see org.apache.woden.WSDLReader
 */
public interface URIResolver {
	
	/** 
	 * Implementation should return null if there is no resolution for the uri.
	 * 
	 * @param uri the uri to be resolved
	 * @return    the resolved URI (or null if no resolution available)
	 * @throws    WSDLException
	 * @throws    IOException
	 */
	public URI resolveURI(URI uri) throws WSDLException, IOException;

}
