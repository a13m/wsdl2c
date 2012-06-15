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


/**
 * This interface represents XML attribute information items of type 
 * 'Union of xs:int, xs:token', for example
 * the whttp:code extension attribute of binding &lt;fault&gt;.
 * <p>
 * The <code>isInt</code> and <code>isToken</code> methods determine whether 
 * to call the <code>getInt</code> or <code>getToken</code> methods. 
 * If the implementor object is initialized with an int,
 * <code>isInt</code> will return 'true', <code>isToken</code> will return
 * 'false', <code>getInt</code> will return the int value and <code>getToken</code> 
 * will return null. If it is initialized with an xs:token, <code>isInt</code> 
 * will return 'false', <code>isToken</code> will return 'true', <code>getInt</code> 
 * will return null and <code>getToken</code> will return the token string.
 * <p>
 * If the implementor object is initialized with a null value (i.e. because
 * of an attribute value conversion error or because the attribute value
 * was empty in the WSDL),  the <code>getContents</code>, <code>getInt</code> 
 * and <code>getToken</code> methods will return null and <code>isInt</code>, 
 * <code>isToken</code> and <code>isValid</code> will return false.
 * 
 * @author jkaputin@apache.org
 */
public interface IntOrTokenAttr extends XMLAttr 
{
    public boolean isInt();
    
    public boolean isToken();
    
    public Integer getInt();
    
    public String getToken();
}
