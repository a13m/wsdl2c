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

/**
 * This interface represents elements that may contain extension elements.
 * 
 * @author jkaputin@apache.org
 */
public interface ElementExtensible 
{
    public void addExtensionElement(ExtensionElement extEl);
    
    public void removeExtensionElement(ExtensionElement extEl);

    public ExtensionElement[] getExtensionElements();
    
    public ExtensionElement[] getExtensionElementsOfType(QName extType);
    
    /**
     * Return true if the implementor has extension elements belonging to this namespace,
     * otherwise false.
     */
    public boolean hasExtensionElementsForNamespace(URI namespace);

}
