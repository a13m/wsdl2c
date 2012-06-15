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
package org.apache.woden.types;

/**
 * This class represents the data type NCName use for XML non-colonized names.
 * It is based on the class of the same name in Apache Axis.
 * 
 * @author jkaputin@apache.org
 */
public class NCName 
{
    
    static public boolean isValid(String stValue)
    {
        int scan;
        boolean bValid = true;

        for (scan=0; scan < stValue.length(); scan++) {
            if (scan == 0)
              bValid = XMLChar.isNCNameStart(stValue.charAt(scan));
            else
              bValid = XMLChar.isNCName(stValue.charAt(scan));
            if (bValid == false)
              break;
        }
        return bValid;
    }
    
    private String fValue = null;

    /**
     * 
     * @param value String representing an NCName
     * @throws IllegalArgumentException if value is not a valid NCName
     */
    public NCName(String value)
    {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        setValue(value);
    }
    
    /**
     * 
     * @param value String representing an NCName
     * @throws IllegalArgumentException if value is not a valid NCName
     */
    public void setValue(String value)
    {
        if(!NCName.isValid(value))
        {
            //TODO use ErrorReporter.getFormattedMessage() here instead of hardcoded message.
            //Need to implement a suitable accessor for ErrorReporter first (e.g. factory or singleton).
            throw new IllegalArgumentException("The string \"" +
                                               value +
                                               "\" does not represent a valid NCName.");
        }
        fValue = value;
    }
    
    public String toString()
    {
        return fValue;
    }
    
    public boolean equals(Object that) {
        // If the two object ids are the same then comparing with itself
        if (this==that) return true;
        
        // if 'that' isn't an instance of NCName then not equal.
        // implicitly tests for 'that' being null
        if (!(that instanceof NCName)) return false;
        
        NCName thatN = (NCName)that;
        
        // NCName's value can be null so if both fValues are null they are equal
        if (this.fValue == null && thatN.fValue == null) return true;
        
        // if either the fValues are null then the other one isn't so return false
        if (this.fValue == null || thatN.fValue == null) return false;

        // Now that neither fValues are null, compare them
        if (this.fValue.equals(thatN.fValue)) return true;
        
        return false;
        
    }
    
    public int hashCode() {
        return fValue.hashCode();
    }
}
