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
import org.apache.woden.xml.IntOrTokenAttr;

/**
 * This class represents XML attribute information items of type
 * 'Union of xs:int, xs:token #any', for example the 
 * whttp:code extension attribute of binding &lt;fault&gt;.
 * 
 * @author jkaputin@apache.org
 */
public class IntOrTokenAnyAttrImpl extends XMLAttrImpl implements IntOrTokenAttr 
{

    public IntOrTokenAnyAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  QNameOrTokenAttr interface declared methods 
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.xml.IntOrTokenAttr#isInt()
     */
    public boolean isInt() 
    {
        return getContent() instanceof Integer;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.IntOrTokenAttr#isToken()
     */
    public boolean isToken() 
    {
        return !isInt() && isValid();
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.IntOrTokenAttr#getInt()
     */
    public Integer getInt() 
    {
        if(isInt()) {
            return (Integer)getContent();
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.xml.IntOrTokenAttr#getToken()
     */
    public String getToken() 
    {
        if(!isInt() && isValid()) {
            return (String)getContent();
        } else {
            return null;
        }
    }

    /* ************************************************************
     *  Implementation of abstract method inherited from XmlAttrImpl 
     * ************************************************************/
    
    /* (non-Javadoc)
     * @see org.apache.woden.internal.xml.XMLAttrImpl#convert(org.w3c.dom.Element, java.lang.String)
     * 
     * Convert a string of type "Union of xs:int, xs:token #any" to a 
     * java.lang.Integer or a String.
     * A null argument will return a null value.
     * Any conversion error will be reported and a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
        //First, check if the attribute contains the xs:token '#any'.
        if("#any".equals(attrValue)) return attrValue;
        
        //Second, assume the attribute contains a xs:int value.
        Integer intVal = null;
        try 
        {
            intVal = new Integer(attrValue);
        } 
        catch (NumberFormatException e) 
        {
            setValid(false);
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL512",
                    new Object[] {attrValue}, 
                    ErrorReporter.SEVERITY_ERROR, 
                    e);
        }
        return intVal;
    }

}
