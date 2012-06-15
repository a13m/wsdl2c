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
package org.apache.woden.xml;

import javax.xml.namespace.QName;



/**
 * This interface represents an XML attribute information item. It can be initialized
 * with the string value of an attribute and the implementation must convert the string into
 * an object of the appropriate type. The getContent() method will return the converted 
 * Object and the caller must cast this to the appropriate type. 
 * If a conversion error occured because the string was not in the correct form, 
 * the isValid() method will return false. The toExternalForm() method will return the
 * attribute's original string value.
 * 
 * @author jkaputin@apache.org
 */
public interface XMLAttr 
{
    public QName getAttributeType();
    
    public Object getContent();
    
    public String toExternalForm();
    
    public boolean isValid();
}
