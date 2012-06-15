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

import org.apache.woden.ErrorInfo;
import org.apache.woden.ErrorLocator;

/**
 * This class is a data object containing the information needed
 * for reporting warnings, errors and fatal errors. It overrides
 * the toString() method to concatenate this information into
 * a single string for reporting purposes.
 * 
 * @author jkaputin@apache.org
 */
public class ErrorInfoImpl implements ErrorInfo {
    
    //TODO decide if a data container object like this is suitable,
    //rather than an exception like XMLParserException used in Xerces.
    
    //TODO decide if properties captured here are sufficient for reporting needs.
    
    private ErrorLocator fErrLoc;
    private String fKey;
    private String fMessage;
    private Exception fException;

    public ErrorInfoImpl(ErrorLocator errorLocator,
                         String key, 
                         String message,
                         Exception exception) 
    {
        fErrLoc = errorLocator;
        fKey = key;
        fMessage = message;
        fException = exception;
    }
    
    public ErrorLocator getErrorLocator() {
        return fErrLoc;
    }

    public String getKey() {
        return fKey;
    }

    public String getMessage() {
        return fMessage;
    }
    
    public Exception getException() {
        return fException;
    }
    
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        if(fErrLoc != null)
        {
            sb.append(fErrLoc.getLineNumber() + ":" + 
                      fErrLoc.getColumnNumber() + ",");
        }
        
        sb.append(fKey + ",");
        sb.append(fMessage);
        
        if(fException != null) {
            sb.append("," + fException.getClass().getName() + ":" +
                      fException.getMessage());
        }
        
        return sb.toString();
        
    }

}
