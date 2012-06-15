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
package org.apache.woden.wsdl20.fragids;

import org.apache.woden.xpointer.PointerPart;

/**
 * <code>ComponentPart</code> is the abstract base class for all WSDL 2.0 component
 * fragment identifiers.
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public interface ComponentPart extends PointerPart {

    /**
     * Checks that the namespace prefixes used in this PointerPart are consistent with those in the WSDL Fragment Identifier.
     * It returns a identical copy of this object with the required changes.
     * This method is called by the add method on WSDL Fragment Identifier when PointerParts are added to it.
     * 
     * @param fragmentIdentifier a Fragment Identifier which the namespace prefixes are checked against.
     * @return a ComponentPart which has been checked with changed namespaces if needed.
     */
    public ComponentPart prefixNamespaces(FragmentIdentifier fragmentIdentifier);
    
}
