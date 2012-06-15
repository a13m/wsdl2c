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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.woden.WSDLException;
import org.apache.woden.internal.util.PropertyUtils;
import org.apache.woden.resolver.URIResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * A Simple URI Resolver.
 * 
 * Locating the catalog file:
 * These alternatives, listed in search order, will be:
 * 1. JVM system properties (e.g. java -D arguments)
 * 		use -Dorg.apache.woden.resolver.simpleresolver.catalog="<filespec>" on the command line
 * 		where <filespec> is the location of the simple resolver catalog on the local file system.
 * 2. application properties defined in /META-INF/services (e.g. in a jar file)
 *      (when implemented in PropertyUtils.findProperty(String p)...)
 * 3. properties defined in a wsdl properties file located in JAVAHOME/lib directory
 * 		For (2) and (3) use regular property assignment notation:
 *      org.apache.woden.resolver.simpleresolver.catalog="<filespec>"
 *      
 *  Catalog file format:
 *  This is as for Java Properties file format, with one entry for each resolution mapping:
 *  <listed document URI>=<physical location URI>.
 *  
 *  Example catalog file contents (fictitious):
 *  
 *  	# {referenced URI}={actual location}
 *  	#
 *  	http\://www.apache.org/schema/remoteSchema.xsd=file:///c:/schema/localSchema.xsd
 *  	http\://www.apache.org/schema/myDoc=file:///c:/schema/XMLSchema.xsd
 *  	http\://www.w3.org/ns/wsdl-extensions.xsd=file:///c:/schema/wsdl-extensions.xsd
 *  	# end
 *  
 *  Note the use of backslash on the first colon of each line. This is necessary as catalog files are 
 *  read as Java Properties files.
 *  
 *  Currently, any relative URI references to other documents (such as with the WSDL 2.0 "location" 
 *  attribute of the <wsdl:import> and <wsdl:include> elements) will be automatically resolved 
 *  by virtue of the parent document.
 *  That is, if document a.wsdl references b.wsdl with a relative path and a.wsdl has an entry in the catalog 
 *  file, then the reference to b.wsdl will be deemed relative to the resolved location of a.wsdl (not the
 *  original location). This is under review and it is expected that future milestone releases will offer the
 *  choice of either option.
 *   
 */

public class SimpleURIResolver implements URIResolver {
		
	private static final String RESOLVER_PROPERTIES_PROPERTY = "org.apache.woden.resolver.simpleresolver.catalog";
	private static final String RESOLVER_BASE_PROPERTY = "org.apache.woden.resolver.simpleresolver.baseURIs";

	private final String userCatalogFile = PropertyUtils.findProperty(RESOLVER_PROPERTIES_PROPERTY);
	private final String rootURLsList = PropertyUtils.findProperty(RESOLVER_BASE_PROPERTY);

	private final String schemaCatalogFile = "schema.catalog";
	private final String schemaCatalogLocation = "META-INF/"; // eg. under woden.jar root
	private Hashtable uriTable = null;
	
	// logging
	private static final String RESOLVER_LOGGING_PROPERTY = "org.apache.woden.resolver.logging";
	private static final String LOGGING_ON = "on";
	private final String loggingRequest = PropertyUtils.findProperty(RESOLVER_LOGGING_PROPERTY);	
	private boolean logging;
	
	/** SLF based logger. */
    private static final Log logger=LogFactory.getLog(SimpleURIResolver.class);
	
	public SimpleURIResolver() throws WSDLException {
		
		Properties schemaCatalog = null;
		/* Unlike the user catalog case, the schema catalog does not refer to RESOLVER_PROPERTIES_PROPERTY
		 * for its catalog location (it's hard coded) or to RESOLVER_BASE_PROPERTY (it uses only the unmodified
		 * system classloader to yield absolute URLs from any relative "resolve-to" URLs in the catalog).
		 * The principle is that the schema catalog behaviour should be unaffected by the use of the public user catalog.
		 */
		
		Properties userCatalog = null;		
		
		// check if resolver logging required
		logging = (LOGGING_ON.equalsIgnoreCase(loggingRequest));
		
		// find location of schema catalog on the classpath. 
		// This catalog has a hardcoded name and relative location.
		ClassLoader systemLoader = this.getClass().getClassLoader();
		URL schemaCatalogURL = systemLoader.getResource(schemaCatalogLocation + schemaCatalogFile);	
		
		if (schemaCatalogURL == null) {
			// schema catalog is not in expected location on classpath, so look for an immediate
			// entry under the classpath.
			schemaCatalogURL = systemLoader.getResource(schemaCatalogFile);
		}

		// read & retain the system resolver references 
		schemaCatalog = loadCatalog(schemaCatalogURL);			
	
		if (userCatalogFile != null) { // user catalog file specified, so use it...
			// read & retain the user resolver references from user catalog properties file
			try 
			{
				userCatalog = loadCatalog(new URL(userCatalogFile));
			} 
			catch (MalformedURLException e) 
			{
				throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
	                    "Problem locating the URI resolver user catalog: " + userCatalogFile,
	                    e);
			}
		}
			    
	    // build a URL[] from a (possibly empty) list of search URL directories/jars
	    URL[] rootURLs = urlPathList(rootURLsList);
		URLClassLoader userLoader = new URLClassLoader(rootURLs, systemLoader);
		
	    // convert provided keys and values to URIs
	    try 
	    {	    	
	    	// process the schema catalog contents
	    	Hashtable interrimUriTable = toURI(schemaCatalog, systemLoader);
	    	// append the processed user catalog to the schema catalog
	    	uriTable = toURI(userCatalog, userLoader, interrimUriTable);
	    } 
	    catch (URISyntaxException e) 
	    {
			throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                    "Problem instantiating the URI resolution table.",
                    e);
		}
	}

	/**
	 * Load properties from specified catalog location
	 * 
	 * @param catalogLocation - location of catalog file to load
	 * @return resulting catalog
	 */
	private Properties loadCatalog(URL catalogLocation) {
		return loadCatalog(catalogLocation, new Properties());
	}
	
	/**
	 * Load properties from specified catalog location and append to exising catalog.
	 * 
	 * @param catalogLocation - location of catalog file to load
	 * @param catalog - exisiting catalog data to which to append
	 * @return appended catalog
	 */
	private Properties loadCatalog(URL catalogLocation, Properties catalog) {
	    if (catalogLocation != null) {
	        try {
		        catalog.load(catalogLocation.openStream());
		    } 
		    catch (IllegalArgumentException e) {
		    	e.printStackTrace();
		    	// treat missing properties file as empty file, ie no resolves specified.
		    	// TODO log this
		    }
		    catch (MalformedURLException e) {
		    	e.printStackTrace();
		    	// load() failed, continue with no resolves
		    	// TODO log this
		    }
		    catch (IOException e) {
		    	e.printStackTrace();
		    	// load() failed, continue with no resolves
		    	// TODO log this
		    }
	    }
		return catalog;
	}	
	/**
	 * Construct a list of URLs from a semi-colon seperated String list of URLs
	 * 
	 * @param rootURLs
	 * @return List of URLs or empty list
	 */
	private URL[] urlPathList(String rootURLs) throws WSDLException {
		if (rootURLs == null || rootURLs.length() == 0) {
			return new URL[0]; 
		}		
		String[] urlStrings = rootURLs.split(";");
		ArrayList urls = new ArrayList();
		
		for (int i=0; i<urlStrings.length; i++) {
			try {
				URL url = new URL(urlStrings[i]);
				urls.add(url);
			} catch (MalformedURLException e) {
				// Bad URLs are discarded noisily
				// TODO should log a woden warning, ignore entry, and continue thru list instead of following?
				throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
						"Invalid URL '" + urlStrings[i] + "' in base URL list '" + rootURLs + "'",
	                    e);
				// TODO or logging like :
				//logger.error("Invalid URL "+urlStrings[i]+": "+e.getMessage());
			}
		}		
		// convert to array of URLs, for later consumption
		return (URL[])urls.toArray(new URL[0]);
	}

	public URI resolveURI(URI uri) throws WSDLException {
		URI resolvedURI = null;
		if (uriTable.containsKey(uri)) 
		{ // TODO is this sufficient?
			resolvedURI = (URI)uriTable.get(uri); 
			logResolve(uri, resolvedURI);
		}
		else
		{
			logNoResolve(uri);
		}
		return resolvedURI;
	}
	
	private void logNoResolve(URI uri) {
		if (logging) {
		    logger.info("resolver:NO:  URI " + uri + " did not resolve");
		}	
	}

	private void logResolve(URI uri, URI resolvedURI) {
		if (logging) {
		    logger.info("resolver:YES: URI " + uri + " successfully resolved to URI " + resolvedURI);
		}	
	}

	/**
	 * Convert resolver Properties (Hashtable of Strings) to Hashtable of URIs
	 * 
	 * @param p - set of properties representing initial catalog
	 * @param loader - Classloader describing search locations for resolved-to, relative catalog entries
	 * @return
	 * @throws URISyntaxException
	 */
	private Hashtable toURI(Properties p, ClassLoader loader) throws URISyntaxException {
		// TODO change to throw a WSDLException

		return toURI(p, loader, new Hashtable());
	}
	
	/**
	 * Takes a set of properties representing {resolved-from, resolved-to} URI pairs
	 * and converts to a Hashtable. 
	 * Any relative resolved-to URIs are "converted ("resolved"!) to absolute location
	 * using the supplied classloader. Any entries for which such a convesion fails are ignored noisily.
	 * 
	 * @param p
	 * @param loader - class
	 * @param h
	 * @return HashTable mapping resolved-from URIs to absolute resolved-to URIs 
	 * @throws URISyntaxException
	 */
	private Hashtable toURI(Properties p, ClassLoader loader, Hashtable h) throws URISyntaxException {
		// TODO change to throw a WSDLException

		if (p != null)
		{
			Enumeration keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = p.getProperty(key);
				try 
				{
					// if value represents an absolute URL (ie has a scheme) keep as-is
					// otherwise treat as relative to the URLClassloader search locations.
					// If the relative resource cannot be located via the class loader, 
					// ignore the entry noisily.
					URI valueURI = new URI(value);
					if (!valueURI.isAbsolute()) 
					{
						// Relative URI so use class loader lookup...
						URL valueURL = loader.getResource(value); 
						if (valueURL != null) 
						{
							// lookup successful
							valueURI = new URI(valueURL.toString());
							h.put(new URI(key), valueURI); //TODO - can we replace with java.net.URL's?
						}
						else
						{
							// Report failed lookup on relative URL on RHS of catalog entry
							// TODO ignore noisily
							logger.error("Lookup failed for URL "+value);
						}
					}
					else
					{
						// Absolute URI - preserve as-is
						h.put(new URI(key), valueURI);
					}						
				}
				catch (URISyntaxException e) {
					// TODO ignore noisily
					logger.error("Invalid URL "+value+": "+e.getMessage());
				}		
			}
		}
		return h;
	}
}
