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

import javax.xml.namespace.QName;

/**
 * This interface represents WSDL 2.0 extension elements. That is, any XML element
 * information items that appear as [children] of a WSDL 2.0 element and are not in 
 * the WSDL 2.0 namespace (http://www.w3.org/ns/wsdl).
 * <p>
 * Based on a similar interface from WSDL4J element extensibility.
 * 
 * TODO change this URL if the WSDL 2.0 namespace changes before spec is finalized.
 * 
 * @author jkaputin@apache.org
 */
public interface ExtensionElement 
{
    public void setExtensionType(QName qname);
    
    public QName getExtensionType();
    
    public void setRequired(Boolean required);
    
    public Boolean isRequired();
    
}
