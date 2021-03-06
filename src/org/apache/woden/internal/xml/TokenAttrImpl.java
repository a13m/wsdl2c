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
import org.apache.woden.xml.TokenAttr;

/**
 * This class represents XML attribute information items of type xs:token.
 * 
 * TODO when the class org.apache.woden.types.Token has been created, convert the
 * xs:token to a Token instead of a String and modify the getToken method to return
 * a Token instead of a String.
 * 
 * @author jkaputin@apache.org
 */
public class TokenAttrImpl extends XMLAttrImpl implements TokenAttr 
{
    public TokenAttrImpl(XMLElement ownerEl, QName attrType, 
            String attrValue, ErrorReporter errRpt) throws WSDLException
    {
        super(ownerEl, attrType, attrValue, errRpt);
    }
    
    /* ************************************************************
     *  TokenAttr interface declared methods 
     * ************************************************************/
    
    public String getToken() {
        return (String)getContent();
    }
    
    /* ************************************************************
     *  Non-API implementation methods 
     * ************************************************************/

    /*
     * Convert a string of type xs:token to a java.lang.String (for now, but see 'todo' above).
     * A null argument will return a null value.
     * Any conversion error will be reported and a null value will be returned.
     */
    protected Object convert(XMLElement ownerEl, String attrValue) throws WSDLException
    {

        if(attrValue == null)
        {
            setValid(false);
            getErrorReporter().reportError(
                    new ErrorLocatorImpl(),  //TODO line&col nos.
                    "WSDL508",
                    new Object[] {attrValue},
                    ErrorReporter.SEVERITY_ERROR);
        }
        return attrValue;
    }

}
