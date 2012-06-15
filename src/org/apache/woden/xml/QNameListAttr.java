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
 * This interface represents XML attribute information items of type 
 * xs:list of QNames. The string is converted into a collection of QNames,
 * one for each valid name in the string. The QNames can be retrieved as an
 * array of QName. If an error occurs converting a QName it will not be included
 * in the array and the isValid() method will return false (even if there are 
 * some valid QNames in the list). If no qnames can be converted from the string,
 * getContent() and getQNames() will return null and isValid() will return false.
 * 
 * @author jkaputin@apache.org
 */
public interface QNameListAttr extends XMLAttr 
{
    public QName[] getQNames();
}
