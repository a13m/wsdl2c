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

import java.net.URI;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLSource;

/**
 * This abstract class implements methods of the WSDLSource interface that are
 * common across all concrete implementations. The only method on this interface
 * that is specific to each concrete implementation is the <code>setSource</code>
 * method and this method is declared abstract on this class.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public abstract class BaseWSDLSource implements WSDLSource {
    
    private URI fBaseURI = null;
    protected Object fSource = null;
    protected ErrorReporter fErrorReporter = null;
    
    protected BaseWSDLSource(ErrorReporter errorReporter) {
        fErrorReporter = errorReporter;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.WSDLSource#setSource(java.lang.Object)
     */
    abstract public void setSource(Object wsdlSource);

    /* (non-Javadoc)
     * @see org.apache.woden.WSDLSource#getSource()
     */
    public Object getSource() {
        return fSource;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.WSDLSource#setBaseURI(java.net.URI)
     */
    public void setBaseURI(URI baseURI) {
        fBaseURI = baseURI;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.WSDLSource#getBaseURI()
     */
    public URI getBaseURI() {
        return fBaseURI;
    }

}
