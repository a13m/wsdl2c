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
package org.apache.woden.internal.xml;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
import org.apache.woden.xml.XMLAttr;

/**
 * This is an abstract superclass for all classes representing different
 * types of XML attribute information items.
 * 
 * @author jkaputin@apache.org
 */
public abstract class XMLAttrImpl implements XMLAttr
{
    private QName fAttrType = null;
    private Object fContent = null;
    private String fExternalForm = null;
    private boolean fValid = true;
    private ErrorReporter fErrorReporter = null;
    
    protected XMLAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        fErrorReporter = errRpt;
        fAttrType = attrType;
        fExternalForm = attrValue;
        fContent = convert(ownerEl, attrValue);
        if(fContent == null) {
            fValid = false;
        }
    }
    
    /* ************************************************************
     *  XMLAttr interface declared methods 
     * ************************************************************/

    public QName getAttributeType() {
        return fAttrType;
    }
    
    public Object getContent() {
        return fContent;
    }
    
    public String toExternalForm() {
        return fExternalForm;
    }
    
    public boolean isValid() {
        return fValid;
    }
    
    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/

    abstract protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException;
    
    protected ErrorReporter getErrorReporter()
    {
        return fErrorReporter;
    }
    
    /*
     * Validity is initialized to true, but set to false if the attribute's
     * value is null or if it cannot be converted to an object of the 
     * appropriate type). This method may be used to change the validity later
     * (e.g. if Validation determines an error).
     * 
     * TODO confirm this method is needed, otherwise remove it.
     */
    protected void setValid(boolean validity) {
        fValid = validity;
    }
}
