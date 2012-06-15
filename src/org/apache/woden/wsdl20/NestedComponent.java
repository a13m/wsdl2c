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
package org.apache.woden.wsdl20;

/**
 * Represents a super-type of all WSDL Components which are 'nested' directly
 * or indirectly within the 'top-level' WSDL components; Interface, Binding 
 * and Service. 
 * This interface defines behaviour for retrieving the parent WSDL component.
 * <p>
 * The nested WSDL components are:
 * <ul>
 *   <li>InterfaceFault</li>
 *   <li>InterfaceOperation</li>
 *   <li>InterfaceFaultReference</li>
 *   <li>InterfaceMessageReference</li>
 *   <li>BindingFault</li>
 *   <li>BindingOperation</li>
 *   <li>BindingFaultReference</li>
 *   <li>BindingMessageReference</li>
 *   <li>Endpoint</li>
 * </ul>
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public interface NestedComponent extends WSDLComponent {

    /**
     * Returns a WSDLComponent representing the parent of this nested component.
     * 
     * @return the parent WSDLComponent
     */
    public WSDLComponent getParent();
    
}
