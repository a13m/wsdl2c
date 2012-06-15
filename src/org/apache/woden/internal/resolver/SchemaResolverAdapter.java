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
package org.apache.woden.internal.resolver;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.woden.XMLElement;
import org.apache.woden.internal.util.StringUtils;
import org.xml.sax.InputSource;

/**
 * Adapter from woden to ws.commons resolver, for use with XMLSchemaCollection
 * (Effectively chains ws.commons DefaultURIResolver...)
 * 
 * @author Graham Turrell
 *
 */
public abstract class SchemaResolverAdapter implements org.apache.ws.commons.schema.resolver.URIResolver {
	
	private org.apache.woden.resolver.URIResolver fActualResolver;
    protected XMLElement fContextElement;
	
	public SchemaResolverAdapter(org.apache.woden.resolver.URIResolver actualResolver, 
            XMLElement contextElement) {
        
		fActualResolver = actualResolver;
        fContextElement = contextElement;
	}
	
	/**
	 * returns resolved URI if one can be found, otherwise the URI constructed from
	 * the arguments. Conforms to the interface spec.
	 */
	public InputSource resolveEntity(String targetNamespace,
               String schemaLocation,
               String baseUri
               ) {
		
		/* build the URI from args
		 * 
		 */
		URI uri = null;
		try 
		{
    	    /* 
    	    * Its necessary to provide an absolute URI from the 
    	    * given schemaLocation. If SchemaLocation is relative
    	    * we first try to resolve the base uri (ie that of the enclosing
    	    * document), so that all schemaLocation references will be relative
    	    * to the resolved base uri. This behaviour is consistent 
    	    * with the EntityResolverAdapter callback used with wsdl2 parsing.
    	    * This removes the need to list relative URIs in the catalog.
    	    */
	    	   
    	   /*
    	    * TODO may want to have this behaviour switchable?
    	    * currently a convenience for Simple URI resolvers, where 
    	    * root URI's cannot be specified for groups of URLs.
    	    * It removes the need to specify relative URL from source documents
    	    * in the catalog. This is at the cost of flexibility, as relative
    	    * URLs in the source documents must now have the same relative location
    	    * to the resolved source document.
    	    *
    	    * OASIS XML Catalog resolvers do allow a flexible use of root URI's 
    	    * and here it may be better to *not* resolve the base URI here.
    	    * If so, EntityResolverAdapter should be changed to similar behaviour.
    	    */
    	   
			
			URI resolvedBaseUri = fActualResolver.resolveURI(new URI(baseUri));
			uri = buildUri(targetNamespace, schemaLocation, resolvedBaseUri == null ? baseUri : resolvedBaseUri.toString()); 
		} 
		catch (Exception e) 
		{
			/* IOException
			 * WSDLException
			 * URISyntaxException
			 * MalformedURLException
			 */
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
        
        InputSource iSource = new InputSource(resolved != null ? resolved.toString() : uri.toString());
        
        //Check if the uri ends in a fragid, if so include the schema element it 
        //identifies in the input source.
        String systemId = iSource.getSystemId();
        String fragId = null;
        int i = systemId.indexOf("#");
        if(i > -1) {
            fragId = systemId.substring(i);
            iSource.setByteStream(resolveFragId(fragId));
        }
		
		return iSource;
	}

    /*
     * Helper method to resolve fragid to a schema element, represented as a byte stream.
     * This will be XML parser-specific, so implementations should override this method.
     */
    protected abstract InputStream resolveFragId(String fradId);
    
	/* based on ws commons DefaultURIResolver.resolveEntity(...)
	 * 
	 */
	private URI buildUri(String targetNamespace,
               String schemaLocation,
               String baseUri) throws URISyntaxException, MalformedURLException {
		
       if (baseUri != null) 
        {
           URL ctxUrl = new URL(baseUri);
           URL schemaUrl = StringUtils.getURL(ctxUrl,schemaLocation);
           URI uri = new URI(schemaUrl.toString());
           return uri;
        }
        return new URI(schemaLocation);
		
	}
}
