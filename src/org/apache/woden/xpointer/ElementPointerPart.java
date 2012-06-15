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
package org.apache.woden.xpointer;

import org.apache.woden.types.NCName;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.StringBuffer;

import java.lang.NumberFormatException;

/**
 * ElementPointerPart is a class which represents the element() scheme for the XPointer Framework.
 * The specification is defined at <a href="http://www.w3.org/TR/xptr-element/">http://www.w3.org/TR/xptr-element/</a>
 * 
 * This class is immutable.
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 *
 */
public class ElementPointerPart implements PointerPart {
    private final NCName ncname;
    private final List childSequence;
    
    /**
     * Constructs an ElementPointerPart with only an elementID NCName.
     * 
     * @param elementID an NCName of the elementID to reference.
     * @throws NullPointerException is a null elementID is given.
     */
    public ElementPointerPart(NCName elementID) {
        if(elementID == null) {
            throw new NullPointerException("The elementID argument is null.");
        }
        this.ncname = elementID;
        this.childSequence = null;
    }
    
    /**
     * Constructs an ElementPointerPart with only a childSequence.
     * 
     * @param childSequence a List of Integers representing the child sequence.
     * @throws NullPointerException if childSequence is null.
     * @throws IllegalArgumentException if childSequence is empty or contains elements other than Integers.
     */
    public ElementPointerPart(List childSequence) {
        if(childSequence == null) {
            throw new NullPointerException("The childSequence argument is null.");
        }
        if(childSequence.isEmpty()) {
            throw new IllegalArgumentException("The childSequence list is empty.");
        }
        for(Iterator it = childSequence.iterator(); it.hasNext();) {
            if(!(it.next() instanceof Integer)){
                throw new IllegalArgumentException("The childSequence list must only contain Integers.");
            }
        }
        this.ncname = null;
        this.childSequence = childSequence;
    }
    
    /**
     * Constructs an ElementPointerPart with both an NCName and a childSequence.
     * 
     * @param elementID an NCName of the elementID to reference.
     * @param childSequence a List of Integers representing the child sequence.
     * @throws NullPointerException if elementID or childSequence are null.
     * @throws IllegalArgumentException if childSequence is empty or contains elements other than Integers.
     */
    public ElementPointerPart(NCName elementID, List childSequence) {
        if(elementID == null) {
            throw new NullPointerException("The elementID argument is null.");
        }
        if(childSequence == null) {
            throw new NullPointerException("The childSequence argument is null.");
        }
        if(childSequence.isEmpty()) {
            throw new IllegalArgumentException("The childSequence list is empty.");
        }
        for(Iterator it = childSequence.iterator(); it.hasNext();) {
            Object integer = it.next();
            if(!(integer instanceof Integer)){
                throw new IllegalArgumentException("the childSequence list must only contain Integers.");
            } else if (((Integer)integer).intValue() == 0){
                throw new IllegalArgumentException("the childSequence list must only contain Integers bigger than 0.");
            }
        }
        this.ncname = elementID;
        this.childSequence = childSequence;
    }
    
    /**
     * Returns the NCName for this Element PointerPart.
     * 
     * @return an NCName if it exists in this Element PointerPart, otherwise null.
     */
    public NCName getNCName() {
        return ncname;
    }
    
    /**
     * Returns the child sequence of this Element PointerPart.
     * 
     * @return a Integer[] of the child sequence for this element pointer part, or an empty array if none exists. 
     */
    public Integer[] getChildSequence() {
        int size = childSequence.size();
        Integer[] sequence = new Integer[size];
        for(int i=0; i<size; i++) {
            sequence[i] = (Integer)childSequence.get(i);
        }
        return sequence;
    }
    
    /**
     * Checks if this Element PointerPart has a NCName or not.
     * 
     * @return a boolean, true if it has a NCName or false if not.
     */
    public boolean hasNCName() {
        return ncname != null;
    }
    
    /**
     * Checks if this Element PointerPart has a childSequence or not.
     * 
     * @return a boolean, true if this has a childSequence or false if not.
     */
    public boolean hasChildSequence() {
        return childSequence != null;
    }
    
    /*
     *(non-Javadoc)
     * @see org.apache.woden.xpointer.PointerPart#toString()
     */
    public String toString() {
        String schemeData;
        if (childSequence == null) {
            schemeData = ncname.toString();
        } else if (ncname == null) {
            schemeData = serialiseChildSequence();
        } else {
            schemeData = ncname.toString() + serialiseChildSequence();
        }
        return "element(" + schemeData + ")";
    }
    
    /**
     * Serialises the child sequence and returns it as a string.
     * 
     * @return a String of the serialised child sequence.
     */
    private String serialiseChildSequence() {
        StringBuffer buffer = new StringBuffer();
        for(Iterator it = childSequence.iterator(); it.hasNext();) {
            Integer child = (Integer)it.next();
            buffer.append("/" + child.toString());
        }
        return buffer.toString();
    }
    
    /**
     * Deserialises the schemaData for an ElementPointerPart and constructs a new ElementPointerPart from it.
     * 
     * @param schemeData a String of the schemeaData parsed from the string XPointer.
     * @return an ElementPointerPart representing the parsed schemaData.
     * @throws IllegalArgumentException if the schemeData has invalid scheme syntax.
     */
    public static ElementPointerPart parseFromString(final String schemeData) throws InvalidXPointerException {
        List childSequence = null;
        NCName elementID = null;
        int startChar;
        int endChar;
        
        //Find an NCName if it exists?
        startChar = schemeData.indexOf("/");
        // -1 Only an NCName. 0 No NCName. > 1 An NCName.
        
        switch (startChar) {
            case -1: //Only an NCName.
                try {
                    elementID = new NCName(schemeData);
                }
                catch (IllegalArgumentException e) {
                    throw new InvalidXPointerException("Invalid NCName in the XPointer", schemeData);
                }
                return new ElementPointerPart(elementID);
            case 0: //No NCName.
                break;
            default: //An NCName.
                try {
                    elementID = new NCName(schemeData.substring(0, startChar));
                } catch (IllegalArgumentException e) {
                    throw new InvalidXPointerException("Invalid NCName in the XPointer", schemeData, 0, startChar);
                }
                break;
        }
        
        //Find remaining child sequence.
        childSequence = new ArrayList();
        
        endChar = schemeData.indexOf("/", startChar+1);
        // -1 Only single child sequence element. > 0 A childSequence.
        
        if (endChar < 0) { //Only single child sequence element.
            childSequence.add(parseIntegerFromChildSequence(schemeData, startChar+1, schemeData.length()));
        } else { //Multiple child sequence elements.
            while(true) {
                if (endChar < 0) {//Last integer.
                    childSequence.add(parseIntegerFromChildSequence(schemeData, startChar+1, schemeData.length()));
                    break;
                } else { //Inner sequence integer.
                    childSequence.add(parseIntegerFromChildSequence(schemeData, startChar+1, endChar));
                    startChar = endChar;
                    endChar = schemeData.indexOf("/", startChar+1);
                }
            }
        }

        if (elementID == null) { //Only a childSequence
            return new ElementPointerPart(childSequence);
        } else { //Both NCName and childSequence
            return new ElementPointerPart(elementID, childSequence);
        }
    }

    /**
     * Parses a String for an integer between two indices and returns this as an Integer.
     * 
     * @param string a String to parse.
     * @param start an int char index to the start of the Integer.
     * @param end an int char index to the end of the Integer.
     * @return an Integer resulting from parsing the given String in the index range.
     * @throws IllegalArgumentException if the given char range does not contain an integer.
     */
    private static Integer parseIntegerFromChildSequence(String string, int start, int end) throws InvalidXPointerException{
        if (start < end) { //Make sure sub string is not of zero length.
            try { //Make sure the integer is valid.
                return new Integer(string.substring(start, end));
            } catch (NumberFormatException e) {
                throw new InvalidXPointerException("The child sequence part contained a invalid integer.", string,  start, end);
            }
        } else {
            throw new InvalidXPointerException("The child sequence part contained a empty item at " + String.valueOf(start), string, start, end);
        }
    }
}
