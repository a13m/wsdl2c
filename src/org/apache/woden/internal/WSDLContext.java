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

package org.apache.woden.internal;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLFactory;
import org.apache.woden.resolver.URIResolver;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;

/**
 * A container of objects shared internally across the Woden implementation.
 * Intially, within the context of a WSDLFactory, the properties of this object are
 * passed by the WSDLFactory implementation to the constructor, with a null value
 * for the <code>uriResolver</code> property (as this is set by the WSDLReader, not the
 * factory).
 * <p>
 * Within the finer grained context of a WSDLReader, the <code>uriResolver</code> property
 * will reference the resolver used by the reader and if the reader's extension registry 
 * reference changes, the <code>extensionRegistry</code> property will reflect that new reference.
 * <p>
 * TODO if this needs to be made public on the API, either keep it as an immutable data object as-is,
 * or create an API interface called WSDLContext with getters instead of public variables and create
 * an implementation class called WSDLContextImpl.<br>
 * TODO check for use cases that break the WSDLFactory context concept (ie WSDLReader.setFactoryImplName).<br>
 * TODO decide if anything else should be kept here (e.g. woden feats & props, Description factory)
 *  
 * @author John Kaputin (jkaputin@apache.org)
 */
public class WSDLContext {
    final public WSDLFactory wsdlFactory;
    final public ErrorReporter errorReporter;
    final public ExtensionRegistry extensionRegistry;
    final public URIResolver uriResolver;
    
    //package private ctor
    WSDLContext(WSDLFactory wsdlFactory,
            ErrorReporter errorReporter,
            ExtensionRegistry extensionRegistry,
            URIResolver uriResolver) {
        this.wsdlFactory = wsdlFactory;
        this.errorReporter = errorReporter;
        this.extensionRegistry = extensionRegistry;
        this.uriResolver = uriResolver;
    }
}
