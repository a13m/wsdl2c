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
import org.apache.woden.xml.QNameOrTokenAttr;

/**
 * This class represents XML attribute information items of type
 * <code>Union of QName or xs:token #any</code>.
 * For example, the wsoap:code extension attribute of binding fault.
 * 
 * @author jkaputin@apache.org
 */
public class QNameOrTokenAnyAttrImpl extends XMLAttrImpl 
                                     implements QNameOrTokenAttr 
{
    public QNameOrTokenAnyAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  QNameOrTokenAttr interface declared methods 
     * ************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameOrTokenAttr#isQName()
     */
    public boolean isQName() 
    {
        return getContent() instanceof QName;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameOrTokenAttr#isToken()
     */
    public boolean isToken() 
    {
        if(!isQName() && isValid()) {
            return true;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameOrTokenAttr#getQName()
     */
    public QName getQName() 
    {
        if(isQName()) {
            return (QName)getContent();
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameOrTokenAttr#getToken()
     */
    public String getToken() 
    {
        if(!isQName() && isValid()) {
            return (String)getContent();
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.internal.xml.XMLAttrImpl#convert(org.w3c.dom.Element, java.lang.String)
     *
     * Convert a string of type "Union of xs:QName or xs:token #any" to a 
     * java.xml.namespace.QName or a String.
     * A null argument will return a null value.
     * Any conversion error will be reported and a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
        //First, check if the attribute contains the xs:token '#any'.
        if("#any".equals(attrValue)) return attrValue;
        
        //Second, assume the attribute contains a xs:QName value.
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
                    "WSDL507",
                    new Object[] {attrValue}, 
                    ErrorReporter.SEVERITY_ERROR, 
                    ex);
        }
        
        return qn;
    }
    
}
