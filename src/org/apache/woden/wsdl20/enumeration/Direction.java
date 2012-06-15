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
package org.apache.woden.wsdl20.enumeration;

/**
 * This class defines the values of the {direction} property of
 * InterfaceMessageReference and InterfaceFaultReference. This property 
 * indicates whether a message is coming "in" to the service or going "out" 
 * from the service.
 * <p>
 * The property is represented in XML by the message or fault 
 * reference element's tag name:
 * <ul>
 * <li>&lt;input&gt; and &lt;infault&gt; have the direction "in" - 
 *     represented by the constant IN
 * <li>&lt;output&gt; and &lt;outfault&gt; have the direction "out" - 
 *     represented by the constant OUT
 * </ul>
 * This class uses the typesafe enum pattern. Applications should use the
 * public static final constants defined in this class to specify or to 
 * evaluate direction.
 * <p>
 * Examples:
 * <pre>
 *     msgRef.setDirection(Direction.IN);
 *     if(msgRef.getDirection() == Direction.IN) ...
 *     if(msgRef.getDirection().equals(Direction.IN)) ...
 * 
 *     Note that == and .equals() are equivalent.
 * </pre>
 * TODO if extensibility is required, chg ctor to protected 
 * 
 * @author jkaputin@apache.org
 */
public class Direction 
{
    private final String fValue;
    private Direction(String value) { fValue = value; }
    public String toString() {return fValue;}
    
    public static final Direction IN = new Direction("in");
    public static final Direction OUT = new Direction("out");
}
