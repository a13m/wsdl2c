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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.internal.util.StringUtils;
import org.apache.woden.xml.QNameListOrTokenAttr;

/**
 * This class represents XML attribute information items of type
 * <code>Union of list of QName or xs:token #any</code>.
 * For example, the wsoap:subcodes extension attribute of binding fault.
 * 
 * @author jkaputin@apache.org
 */
public class QNameListOrTokenAnyAttrImpl extends XMLAttrImpl 
                                         implements QNameListOrTokenAttr 
{
    private static final String emptyString = "".intern();
    public QNameListOrTokenAnyAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  QNameListOrTokenAttr interface declared methods 
     * ************************************************************/

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameListOrTokenAttr#isQNameList()
     */
    public boolean isQNameList() 
    {
        return getContent() instanceof QName[];
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameListOrTokenAttr#isToken()
     */
    public boolean isToken() 
    {
        return !isQNameList() && isValid();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameListOrTokenAttr#getQNames()
     */
    public QName[] getQNames() 
    {
        if(isQNameList()) {
            return (QName[])getContent();
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.QNameListOrTokenAttr#getToken()
     */
    public String getToken() 
    {
        if(!isQNameList() && isValid()) {
            return (String)getContent();
        } else {
            return null;
        }
    }

    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.internal.xml.XMLAttrImpl#convert(org.w3c.dom.Element, java.lang.String)
     *
     * Convert a string of type "Union of list of xs:QName or xs:token #any" to a 
     * java.xml.namespace.QName array or a String.
     * A null argument will return a null value.
     * Any conversion error will be reported and a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
        //First, check if the attribute contains the xs:token '#any'.
        if("#any".equals(attrValue)) return attrValue;
        
        //Second, assume the attribute contains a list of xs:QName.
        if(attrValue == null || emptyString.equals(attrValue))
        {
            setValid(false);
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL509", 
                    new Object[] {attrValue}, 
                    ErrorReporter.SEVERITY_ERROR);
            return null;
        }
        
        List qnStrings = StringUtils.parseNMTokens(attrValue);
        Iterator i = qnStrings.iterator();
        String qnString;
        QName qname;
        List qnames = new Vector();
        
        while(i.hasNext())
        {
            qnString = (String)i.next();
            try
            {
                qname = ownerEl.getQName(qnString);
            } 
            catch (WSDLException e) 
            {
                setValid(false);
                getErrorReporter().reportError(
                        new ErrorLocatorImpl(),  //TODO line&col nos.
                        "WSDL510", 
                        new Object[] {qnString, attrValue}, 
                        ErrorReporter.SEVERITY_ERROR, 
                        e);
                continue;
            }
            qnames.add(qname);
        }
        QName[] qnArray = new QName[qnames.size()];
        qnames.toArray(qnArray);
        return qnArray;    
    }
    
}
