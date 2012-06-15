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
import org.apache.woden.xml.BooleanAttr;

/**
 * This class represents XML attribute information items of type xs:boolean.
 * If the attribute value is not "true" or "false" the Boolean content will
 * be initialized to "false" by default, but the isValid() method will
 * return "false".
 * 
 * @author jkaputin@apache.org
 */
public class BooleanAttrImpl extends XMLAttrImpl implements BooleanAttr 
{
    public BooleanAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  BooleanAttr interface declared methods 
     * ************************************************************/
    
    public Boolean getBoolean() {
        return (Boolean)getContent();
    }
    
    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/

    /*
     * Convert a string of type xs:boolean to a java.lang.Boolean.
     * An empty string or a null argument will initialize the Boolean to false.
     * Any conversion error will be reported and will initialize the Boolean to false.
     * If the attrValue does not match the Boolean value the Attr is marked invalid.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {
        Boolean bool = Boolean.valueOf(attrValue);
        
        if(attrValue == null || !attrValue.equals(bool.toString()) )
        {
            setValid(false);
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL511", 
                    new Object[] {attrValue}, 
                    ErrorReporter.SEVERITY_ERROR);
        }
        
        return bool;
    }

}
