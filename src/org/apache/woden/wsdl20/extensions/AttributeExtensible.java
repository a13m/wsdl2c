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
package org.apache.woden.wsdl20.extensions;

import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.woden.xml.XMLAttr;

/**
 * This interface represents elements that may contain extension attributes.
 * 
 * @author jkaputin@apache.org
 */
public interface AttributeExtensible 
{
    /**
     * Store the extension attribute object identified by the QName.
     * If the attribute argument is null, remove the extension attribute identified
     * by the specified QName argument.
     * 
     * TODO ? @throws IllegalArgumentException if the QName is null
     */
    public void setExtensionAttribute(QName attrType, XMLAttr attr);
   
    /**
     * Return the extension attribute with this QName.
     */
    public XMLAttr getExtensionAttribute(QName attrType);
    
    /**
     * Return the extension attributes from this namespace.
     */
    public XMLAttr[] getExtensionAttributesForNamespace(URI namespace);
    
    /**
     * Return all extension attributes.
     */
    public XMLAttr[] getExtensionAttributes();
    
    /**
     * Return true if the implementor has extension attributes belonging to this namespace,
     * otherwise false.
     */
    public boolean hasExtensionAttributesForNamespace(URI namespace);

}
