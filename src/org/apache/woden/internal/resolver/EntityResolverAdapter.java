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
 * @author Graham Turrell
 */

package org.apache.woden.internal.resolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EntityResolverAdapter implements EntityResolver {

	private org.apache.woden.resolver.URIResolver fActualResolver;
	
	public EntityResolverAdapter(org.apache.woden.resolver.URIResolver actualResolver) {
		fActualResolver = actualResolver;
	}
	
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		
		/* build the URI from args
		 * 
		 */
		URI uri = null;
		try 
		{
			// dumb placeholder:
			uri = new URI(systemId);
		} 
		catch (URISyntaxException e) 
		{
			throw new RuntimeException(e);
		}		
		
		/* resolve with target resolver
		 * 
		 */		
		URI resolved = null;
		try 
		{
			resolved = fActualResolver.resolveURI(uri);
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		// dumb placeholder:
		return (resolved == null)? null : new InputSource(resolved.toString());
	}

}
