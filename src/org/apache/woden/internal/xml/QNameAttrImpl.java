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
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.xml.QNameAttr;

/**
 * This class represents XML attribute information items of type xs:QName.
 * 
 * @author jkaputin@apache.org
 */
public class QNameAttrImpl extends XMLAttrImpl implements QNameAttr 
{
    public QNameAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  QNameAttr interface declared methods 
     * ************************************************************/
    
    public QName getQName() {
        return (QName)getContent();
    }
    
    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/

    /*
     * Convert a string of type xs:QName to a java.xml.namespace.QName.
     * A a null argument will return a null value.
     * Any conversion error will be reported and a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
        Exception ex = null;
        QName qn = null;
        
        if(attrValue != null)
        {
            try
            {
                qn = ownerEl.getQName(attrValue);
            } 
            catch (WSDLException e) 
            {
                ex = e;
            }
        }
        
        if(qn == null)
        {
            setValid(false);
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL507",              //TODO chg to WSDL505 and test
                    new Object[] {attrValue}, 
                    ErrorReporter.SEVERITY_ERROR, 
                    ex);
        }
        
        return qn;
    }

}
