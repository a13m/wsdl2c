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
package org.apache.woden;

/**
 * This interface declares operations for handling three types
 * of errors; warnings, errors and fatal errors. Implementations should define
 * behaviour for these types of errors appropriate to their requirements.
 * For example, errors may be reported immediately by printing to the  system 
 * output stream or they may be cached for further processing by the invoking 
 * application.
 * 
 * This is based on the Xerces error reporter.
 * 
 * @author jkaputin@apache.org
 */
public interface ErrorHandler {
    
    public void warning(ErrorInfo errorInfo);
    
    public void error(ErrorInfo errorInfo);
    
    public void fatalError(ErrorInfo errorInfo);

}
