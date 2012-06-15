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
 * 'Union of list of xs:QName or xs:token', for example
 * the wsoap:subcodes extension attribute of binding &lt;fault&gt;.
 * <p>
 * The <code>isQNameList</code> method can be used to determine whether
 * to call the <code>getQNames</code> method or the <code>getToken</code>
 * method. If the implementor object is initialized with a list of QNames,
 * <code>isQNameList</code> will return 'true', <code>getQNames</code>
 * will return an array of QName objects and <code>getToken</code> will return
 * null. If it is initialized with a xs:token, <code>isQNameList</code> will 
 * return 'false', <code>getQNames</code> will return null and 
 * <code>getToken</code> will return the token string.
 * <p>
 * If the implementor object is initialized with a null value (i.e. because
 * of an attribute value conversion error or because the attribute value
 * was empty in the WSDL),  the <code>getContents</code>, 
 * <code>getQNameList</code> and <code>getToken</code> methods will return null 
 * and <code>isQNameList</code> and <code>isValid</code> will return false.
 * 
 * @author jkaputin@apache.org
 */
public interface QNameListOrTokenAttr extends XMLAttr 
{
    public boolean isQNameList();
    
    public boolean isToken();
    
    public QName[] getQNames();
    
    public String getToken();
}
