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
package org.apache.woden.internal.wsdl20.validation;

import org.apache.woden.ErrorReporter;
import org.apache.woden.resolver.URIResolver;
import org.apache.woden.wsdl20.validation.WodenContext;

/**
 * @author John Kaputin (jkaputin@apache.org)
 */
public class WodenContextImpl implements WodenContext {
    
    private ErrorReporter errReporter;
    private URIResolver uriResolver;
    
    //package private ctor
    WodenContextImpl(ErrorReporter errReporter, URIResolver uriResolver) {
        this.errReporter = errReporter;
        this.uriResolver = uriResolver;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.validation.WodenContext#getErrorReporter()
     */
    public ErrorReporter getErrorReporter() {
        return this.errReporter;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.validation.WodenContext#getUriResolver()
     */
    public URIResolver getUriResolver() {
        return this.uriResolver;
    }

}
