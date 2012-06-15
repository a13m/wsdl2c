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

import org.apache.woden.ErrorLocator;

/**
 * Represents the location of parsing error within a XML document.
 *
 * @author kaputin
 */
public class ErrorLocatorImpl implements ErrorLocator {
    
    private int fLineNumber;
    private int fColumnNumber;

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorLocator#getDocumentBaseURI()
     */
    public String getDocumentBaseURI() {
        // TODO required?
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorLocator#getLocationURI()
     */
    public String getLocationURI() {
        // TODO required?
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.ErrorLocator#getLineNumber()
     */
    public int getLineNumber() {
        return fLineNumber;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.ErrorLocator#getColumnNumber()
     */
    public int getColumnNumber() {
        return fColumnNumber;
    }

    /**
     * Set the base URI of this locator.
     * 
     * @param uri The URI of the base location to set.
     */
    public void setDocumentBaseURI(String uri) {
        // TODO required?
    }

    /**
     * Set the location URI of this locator.
     * 
     * @param uri The URI of the location to set.
     */
    public void setLocationURI(String uri) {
        // TODO required?
    }

    /**
     * Set the line number of this locator.
     * 
     * @param line The line number to set.
     */
    public void setLineNumber(int line) {
        fLineNumber = line;
    }

    /**
     * Set the column number of this locator.
     * 
     * @param col The column number to set.
     */
    public void setColumnNumber(int col) {
        fColumnNumber = col;
    }
    
}
