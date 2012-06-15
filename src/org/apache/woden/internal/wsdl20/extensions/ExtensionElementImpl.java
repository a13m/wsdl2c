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
package org.apache.woden.internal.wsdl20.extensions;

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.extensions.ExtensionElement;

/**
 * Common code for extension elements. 
 * 
 * @author jkaputin@ws.apache.org
 */
public class ExtensionElementImpl implements ExtensionElement 
{
    private QName fExtElementType = null;
    private Boolean fRequired = null;

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setExtensionType(javax.xml.namespace.QName)
     */
    public void setExtensionType(QName qname) {
        fExtElementType = qname;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#getExtensionType()
     */
    public QName getExtensionType() {
        return fExtElementType;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#setRequired(java.lang.Boolean)
     */
    public void setRequired(Boolean required) {
        fRequired = required;

    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ExtensionElement#isRequired()
     */
    public Boolean isRequired() {
        return fRequired;
    }

}
