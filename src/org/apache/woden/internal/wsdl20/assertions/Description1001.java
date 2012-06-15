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
package org.apache.woden.internal.wsdl20.assertions;

import java.net.URI;
import java.net.URL;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.wsdl20.validation.Assertion;
import org.apache.woden.wsdl20.validation.WodenContext;
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * This class represents assertion Description-1001 from the WSDL 2.0 specification.
 * For details about this assertion see:
 * http://www.w3.org/TR/2007/REC-wsdl20-20070626/#Description-1001
 * 
 * @author John Kaputin (jkaputin@apache.org)
 * @author Lawrence Mandel (lmandel@apache.org)
 */
public class Description1001 implements Assertion {

	/**
	 * A list of URI schemes for which this assertion will attempt to check if
	 * the target namespace is dereferencable. 
	 */
	private static String searchableSchemes = "http,ftp,file";
	
    public String getId() {
        return "Description-1001".intern();
    }

    public void validate(Object target, WodenContext wodenCtx) throws WSDLException {
        DescriptionElement descElem = (DescriptionElement) target;
        URI tns = descElem.getTargetNamespace();
        
        try {
            URI resolvedUri = wodenCtx.getUriResolver().resolveURI(tns);
            URI uri = resolvedUri != null ? resolvedUri : tns;
            String scheme = uri.getScheme();
            
            // Only check if the scheme is a type that we can locate.
            // TODO: See if the searchable schemes should be extensible.
            Object o = null;
            if(searchableSchemes.indexOf(scheme)!=-1) {
            	URL url = uri.toURL();
            	o = url.getContent();
            }
            if(o == null) {
                throw new Exception();
            }
        } catch (WSDLException e2) {
        	// A WSDLException may be thrown due to a problem with the URI resolver so we should report this exception.
            throw e2;
        } catch (Exception e) {
        	// Any other exception including IOExceptoin, MalformedURLException, UnknownHostException, and 
        	// FileNotFoundException means that the namespace was not resolvable.
        	wodenCtx.getErrorReporter().reportError(
                    new ErrorLocatorImpl(), getId(), new Object[] {tns}, ErrorReporter.SEVERITY_WARNING);
        }
    }

}
