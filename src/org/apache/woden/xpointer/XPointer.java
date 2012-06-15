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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;
import java.lang.StringBuffer;

import org.apache.woden.types.NCName;

/**
 * XPointer is a class which represents an XPointer defined in the XPointer Framework.
 * This is specified at <a href="http://www.w3.org/TR/xptr-framework/">http://www.w3.org/TR/xptr-framework/</a>
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 * 
 */
public class XPointer {
    private static final String emptyString = "".intern();
    private final Map prefixBindingContex;
    private final Map namespaceBindingContex;
    private NCName shorthandPointer;
    private final List pointerParts;
    
    private static final NCName emptyNCName = new NCName(emptyString);
    
    private static final String NS_URI_XML = "http://www.w3.org/XML/1998/namespace";
    private static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
    private static final NCName NS_PREFIX_XMLNS = new NCName("xmlns");

    /**
     * Constructs a new XPointer.
     * 
     */
    public XPointer() {
        pointerParts = new ArrayList();
        shorthandPointer = emptyNCName;
        
        //Setup prefix/namespace binding context.
        prefixBindingContex = new HashMap();
        namespaceBindingContex = new HashMap();
        addPrefixNamespaceBinding(new NCName("xml"), NS_URI_XML);
    }
    
    /**
     * Constructs a new XPointer from the serialised string.
     * 
     * @param xpointerString a String form of the XPointer to deserialise.
     */
    public XPointer(String xpointerString) throws InvalidXPointerException {
        this(); //Construct a new XPointer.
        if (xpointerString == null || xpointerString.equals(emptyString))
            throw new InvalidXPointerException("The XPointer string is either null or empty", emptyString);
        XPointerParser.parseXPointer(xpointerString, this); //Parse the string and add the Pointers to the new XPointer.
    }
    
    /**
     * Appends a pointer part to the end of this XPointer.
     * 
     * @param pointerPart the Pointer Part to append.
     * @throws UnsupportedOperationException() if a Shorthand Pointer is already set.
     */
    public void addPointerPart(PointerPart pointerPart) {
        if(!shorthandPointer.equals(emptyNCName)) {
            throw new UnsupportedOperationException("A Shortname Pointer already exists for this XPointer.");
        } else {
            pointerParts.add(pointerPart); 
        }
    }
    
    /**
     * Inserts a pointer part at index of this XPointer.
     * 
     * @param pointerPart the Pointer Part to inserted.
     * @param index an integer specifying the point to insert the pointer part.
     * @throws UnsupportedOperationException() if a Shorthand Pointer is already set.
     */
    public void addPointerPart(int index, PointerPart pointerPart) {
        if(hasShorthandPointer()) {
            throw new UnsupportedOperationException("A Shortname Pointer already exists for this XPointer.");
        }
        if(index < 0 || index > pointerParts.size()) {
            throw new IndexOutOfBoundsException("The index you gave is out of the bounds of the list of XPointers");
        }
        pointerParts.add(index, pointerPart); 
    }
    
    /**
     * Returns the pointer parts in this XPointer.
     * 
     * @return a PointerPart[] of type Object[] containing the pointer parts in this XPointer.
     * @throws IllegalStateException if this XPointer has a shorthand pointer.
     */
    public Object[] getPointerParts() {
        if (hasPointerParts()) {
            int size = pointerParts.size();
            Object[] parts = new Object[size]; //Ugly but it will have to do for Java 1.4
            for(int i=0; i<size; i++) {
                parts[i] = pointerParts.get(i);
            }
            return parts;
        } else {
            throw new IllegalStateException("This XPointer has a shorthand pointer.");
        }
    }
    
    /**
     * Sets the Shorthand Pointer of this XPointer to the NCName given as an argument.
     * 
     * @param shorthandPointer an NCName of the Shorthand Pointer to set.
     * @throws UnsupportedOperationException() is a PointerPart Pointer is already set.
     */
    public void setShorthandPointer(NCName shorthandPointer) {
        if (hasPointerParts()) {
            throw new UnsupportedOperationException("A PointerPart Pointer already exists for this XPointer");
        }
        if (shorthandPointer.equals(null)) {
            throw new NullPointerException("The shorthandPointer argument is null");
        }
        
        this.shorthandPointer = shorthandPointer;
    }
    
    /**
     * Returns the shorthandPointer in this XPointer.
     * 
     * @return an NCName containing the shorthand pointer for this XPointer.
     * @throws IllegalStateException if this XPointer has a shorthand pointer.
     */
    public NCName getShorthandPointer() {
        if (hasShorthandPointer()) {
            return shorthandPointer;
        } else {
            throw new IllegalStateException("This XPointer has scheme based pointers.");
        }
    }
    
    /**
     * Adds a Prefix/Namespace binding to this XPointers contex.
     * 
     * @param prefix a NCName of the prefix too bind to the namespace.
     * @param namespace a String of the namespace to bind to the prefix.
     * @throws NullPointerException if the prefix or namespace arguments are null.
     * @throws IllegalArgumentException if the prefix or namespace are invalid as specified at <a href="http://www.w3.org/TR/xptr-framework/#nsContext">http://www.w3.org/TR/xptr-framework/#nsContext</a>
     */
    public void addPrefixNamespaceBinding(NCName prefix, String namespace) {
        if (prefix == null)
            throw new NullPointerException("The prefix argument provided has a null pointer.");
        if (namespace == null)
            throw new NullPointerException("The namespace argument provided has a null pointer.");
        if (prefix.equals(NS_PREFIX_XMLNS))
            throw new IllegalArgumentException("The xmlns prefix must not be bound to any namespace.");
        if (namespace.equals(NS_URI_XMLNS))
            throw new IllegalArgumentException("The " + NS_URI_XMLNS + " namespace must not be bound to any prefix.");
        //Its a valid binding so add it to the binding contex.
        prefixBindingContex.put(prefix, namespace);
        namespaceBindingContex.put(namespace, prefix);
    }
    
    /**
     * Gets the Namespace the Prefix is bound to if the binding exists,
     * otherwise it will return null.
     * 
     * @param prefix a NCName of the prefix bound to the namespace.
     * @return A String of the namespace bound to this prefix or null if none exists.
     */
    public String getPrefixBinding(NCName prefix) {
        return (String)prefixBindingContex.get(prefix);
    }
    
    /**
     * Gets Prefix the Namespace is bound to if the binding exists,
     * otherwise it will return null.
     * 
     * @param namespace a String of the prefix bound to the prefix.
     * @return A NCName of the prefix bound to this namespace or null if none exists.
     */
    public NCName getNamespaceBinding(String namespace) {
        return (NCName)namespaceBindingContex.get(namespace);
    }
    
    /**
     * Checks whether a prefix is bound or not.
     * 
     * @param prefix A NCName of the prefix to check.
     * @return a boolean value that is true if the binding exists, or false otherwise.
     */
    public boolean hasPrefixBinding(NCName prefix) {
        return prefixBindingContex.containsKey(prefix);
    }
    
    /**
     * Checks whether a namespace is bound or not.
     * 
     * @param namespace A String of the namespace to check.
     * @return a boolean value that is true if the binding exists, or false otherwise.
     */
    public boolean hasNamespaceBinding(String namespace) {
        return namespaceBindingContex.containsKey(namespace);
    }

    /**
     * Tests whether this XPointer has a shorthand pointer or not.
     * 
     * @return a boolean which is true if this XPointer contains an shorthand pointer, false otherwise.
     */
    public boolean hasShorthandPointer() {
        return !shorthandPointer.equals(emptyNCName);
    }
    
    /**
     * Tests whether this XPointer has scheme based pointers or not.
     * 
     * @return a boolean which is true if this XPointer contains scheme based pointers, false otherwise.
     */
    public boolean hasPointerParts() {
        return !pointerParts.isEmpty();
    }
    
    /**
     * Returns a String serialisation of this XPointer.
     * 
     * @return a String containing the serialisation of this XPointer
     */
    public String toString() {
        if (shorthandPointer.equals(emptyNCName)) {
            StringBuffer buffer = new StringBuffer();
            Iterator parts = pointerParts.iterator();
            while (parts.hasNext()) {
                buffer.append(parts.next());
            }
            return buffer.toString();
        } else {
            return shorthandPointer.toString();
        }
    }
    
}