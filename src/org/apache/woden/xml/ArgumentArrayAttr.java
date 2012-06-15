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

import org.apache.woden.wsdl20.extensions.rpc.Argument;

/**
 * This interface represents an XML attribute information items whose type
 * is a list of pairs (xs:QName, xs:token) that obey the contraints of
 * wrpc:signature as defined in Part 2 of the WSDL 2.0 spec.
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com, arthur.ryman@gmail.com)
 *
 */
public interface ArgumentArrayAttr extends XMLAttr {
	
	public Argument[] getArgumentArray();

}
