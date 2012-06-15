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

import org.apache.woden.ErrorHandler;
import org.apache.woden.ErrorInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class implements the default error handling behaviour, which is simply
 * to report warnings, errors and fatal errors by printing the error info to 
 * the system output stream.
 * 
 * Implementations may replace this behaviour by providing their own implementation
 * of the ErrorHandler interface.
 *   
 * @author jkaputin@apache.org
 */
public class ErrorHandlerImpl implements ErrorHandler {
    
    /** SLF based logger. */
    private static final Log logger=LogFactory.getLog(ErrorHandlerImpl.class);

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorHandler#warning(org.apache.woden.ErrorInfo)
     */
    public void warning(ErrorInfo errorInfo) {
        
        logger.warn("Woden[Warning]," + errorInfo.toString());

    }

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorHandler#error(org.apache.woden.ErrorInfo)
     */
    public void error(ErrorInfo errorInfo) {

        logger.error("Woden[Error]," + errorInfo.toString());

    }

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorHandler#fatalError(org.apache.woden.ErrorInfo)
     */
    public void fatalError(ErrorInfo errorInfo) {

        logger.fatal("Woden[Fatal Error]," + errorInfo.toString());

    }

}
