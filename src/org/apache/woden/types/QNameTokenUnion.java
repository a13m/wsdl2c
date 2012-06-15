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
package org.apache.woden.types;

import javax.xml.namespace.QName;

/**
 * This class represents the data type "Union of xs:QName and xs:Token" 
 * where the token values are #any, #none, or #other.
 *
 * @author Dan Harvey, danharvey42@gmail.com
 *
 */
public class QNameTokenUnion {
    private final QName qname;
    private final String token;
    public static final QNameTokenUnion ANY = new QNameTokenUnion("#any");
    public static final QNameTokenUnion NONE = new QNameTokenUnion("#none");
    public static final QNameTokenUnion OTHER = new QNameTokenUnion("#other");

    /*
     * Constructs a QNameTokenUnion as a 'token' type with the specified token value.
     * This constructor has been declared private so that it cannot be used to specify
     * arbitrary tokens. It is used only to create an enumeration of static variables 
     * representing the token types #any, #none and #other.
     */
    private QNameTokenUnion(String token) {
        this.token = token.intern();
        this.qname = null;
    }
    
    /**
     * Constructs a QNameTokenUnion as a 'QName' type with the specified qname value.
     * The qname parameter must not be null.
     * 
     * @param qname the QName to set as its value.
     * @throws NullPointerException if qname is null
     */
    public QNameTokenUnion(QName qname) {
        if (qname != null) {
            this.qname = qname;
            this.token = null;
        } else {
            throw new NullPointerException("QName=null");
        }
    }
    
    /**
     * Returns the QName if it exists otherwise null.
     * 
     * @return a QName if it exists otherwise null.
     */
    public QName getQName() {
        return qname;
    }
    
    /**
     * Returns the token value if it exists otherwise null.
     * 
     * @return a String value of the token if it exists otherwise null.
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Returns True if a QName exists, otherwise it returns False;
     * 
     * @return a boolean representing if this has a QName value.
     */
    public boolean isQName() {
        return qname != null;
    }
    
    /**
     * Returns True is a token value exists, otherwise it returns False.
     * 
     * @return a boolean representing if this has a token value.
     */
    public boolean isToken() {
        return token != null;
    }
}
