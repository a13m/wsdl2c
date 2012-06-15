/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.woden.wsdl20.fragids;


/**
 * <code>DescriptionPart</code> is a Description Pointer Part for the Description WSDL 2.0 component.
 * See the specification at <a href="http://www.w3.org/TR/wsdl20/#wsdl.description">http://www.w3.org/TR/wsdl20/#wsdl.description</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class DescriptionPart implements ComponentPart{

    /**
     * Returns a String of the serialised Description Pointer Part.
     * 
     * @return a String the serialised Description Pointer Part.
     */
    public String toString() {
        return "wsdl.description()";
    }
    
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier) {
        return this;
    }
    
}
