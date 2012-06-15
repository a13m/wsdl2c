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
import org.apache.woden.xml.QNameListAttr;


/**
 * This class represents XML attribute information items of type xs:list of QNames.
 * 
 * @author jkaputin@apache.org
 */
public class QNameListAttrImpl extends XMLAttrImpl implements QNameListAttr 
{
    private static final String emptyString = "".intern();
    public QNameListAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  QNameAttr interface declared methods 
     * ************************************************************/
    
    public QName[] getQNames() {
        return (QName[])getContent();
    }
    
    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/

    /*
     * Convert a string of type 'xs:list of QNames' to a java.xml.namespace.QName[].
     * A a null argument will return a null value.
     * If a QName string in the list causes a conversion error, it will be reported 
     * and that QName will not appear in the array. Valid QName strings will still
     * be converted, but the object will be marked invalid. If no QName strings can
     * be converted, a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
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
