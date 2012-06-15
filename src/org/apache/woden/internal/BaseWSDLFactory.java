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
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.internal.wsdl20.DescriptionImpl;
import org.apache.woden.internal.wsdl20.extensions.PopulatedExtensionRegistry;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * This abstract class contains properties and methods common 
 * to WSDLFactory implementations.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public abstract class BaseWSDLFactory extends WSDLFactory {

    protected WSDLContext fWsdlContext;
    
    protected BaseWSDLFactory() throws WSDLException {
        ErrorReporter errRpt = new ErrorReporterImpl();
        ExtensionRegistry extReg = new PopulatedExtensionRegistry(errRpt);
        ((ErrorReporterImpl)errRpt).setExtensionRegistry(extReg);
        fWsdlContext = new WSDLContext(this, errRpt, extReg, null);
    }
    
    abstract public WSDLReader newWSDLReader() throws WSDLException;

    public ExtensionRegistry newPopulatedExtensionRegistry() throws WSDLException {
        return new PopulatedExtensionRegistry(fWsdlContext.errorReporter);
    }

    //TODO change the name of this API method to newDescriptionElement()
    public DescriptionElement newDescription() {
        return new DescriptionImpl(fWsdlContext);
    }

    /*
     * Package private method used by the Woden implementation to create a description
     * element object with a wsdl context different to the default context provided
     * provided by this wsdl factory. For example, a wsdl reader or writer may pass
     * its own context object to this method.
     */
    DescriptionElement newDescriptionElement(WSDLContext wsdlContext) {
        return new DescriptionImpl(wsdlContext);
    }

}
