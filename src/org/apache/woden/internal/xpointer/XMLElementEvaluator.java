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
package org.apache.woden.internal.xpointer;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Arrays;

import org.apache.woden.XMLElement;
import org.apache.woden.types.NCName;
import org.apache.woden.xpointer.PointerPart;
import org.apache.woden.xpointer.ElementPointerPart;
import org.apache.woden.xpointer.XPointer;

/**
 * This class Evaluates an XPointer on a XMLElement, using the XPointer model in Woden.
 * It currently supports shorthand pointer and element() scheme based pointer part.
 * 
 * @author Dan Harvey <danharvey42@gmail.com>
 *
 */
public abstract class XMLElementEvaluator {
    private final XPointer xpointer;
    private final XMLElement root;

    /**
     * Constructs a new XMLElement abstract class for a XPointer and XMLElement.
     *  
     * @param xpointer an XPointer which to evaluate.
     * @param root an XMLElement which to evaluate the XPointer against.
     */
    public XMLElementEvaluator(XPointer xpointer, XMLElement root) {
        this.xpointer = xpointer;
        this.root = root;
    }
    
    /**
     * Evaluates the XPointer on the root XMLElement and returns the resulting XMLElement or null.
     * 
     * @return an XMLElement from the resultant evaluation of the root XMLElement or null if evaluation fails.
     * 
     */
    public XMLElement evaluate() {
        if(xpointer.hasPointerParts()) { //Scheme based pointer.
            //Take each pointer part at a time and evaluate it against the root element. The first result found will be returned.
            XMLElement result = null;
            for(Iterator it = Arrays.asList(xpointer.getPointerParts()).iterator(); it.hasNext(); ) {
                PointerPart pointerPart = (PointerPart)it.next();
                //TODO Add extra pointer parts here once we support them.
                if (pointerPart instanceof ElementPointerPart) {
                    result = evaluateElementPointerPart((ElementPointerPart)pointerPart);
                }
                if (result != null) return result;
            }
        } else if(xpointer.hasShorthandPointer()) { //Shorthand pointer
            //Iterator for XMLElement from root in document order. See http://www.w3.org/TR/xpath#dt-document-order
            return evaluateShorthandPointer(xpointer.getShorthandPointer());
            
        }
        return null;
    }
    
    /**
     * Evaluates an element() XPointer scheme based pointer part to the specification at
     * <a href="http://www.w3.org/TR/xptr-element/">http://www.w3.org/TR/xptr-element/</a>
     * 
     * @param elementPointerPart an ElementPointerPart to evaluate.
     * @return an XMLElement pointed to by this Element pointer part, or null if none exists.
     */
    private XMLElement evaluateElementPointerPart(ElementPointerPart elementPointerPart) {
        if (elementPointerPart.hasChildSequence() && elementPointerPart.hasNCName()) { //Both NCName and childSequence.
            //Find NCName.
            XMLElement element = evaluateShorthandPointer(elementPointerPart.getNCName());
            if (element == null) return null;
            //Walk through children.
            return evaluateChildSequence(element, elementPointerPart.getChildSequence());
        } else if(elementPointerPart.hasNCName()) { //Only NCName
            return evaluateShorthandPointer(elementPointerPart.getNCName());
        } else { //Only a childSequence
            //XML must only have 1 root element so we can't evaluate it if its > 1
            Integer[] childSequence = elementPointerPart.getChildSequence();
            if (childSequence[0].intValue() > 1) return null;
            Integer[] nChildSequence = new Integer[childSequence.length-1];
            for (int i=1; i<childSequence.length; i++) {
                nChildSequence[i-1] = childSequence[i];
            }
            return evaluateChildSequence(root, nChildSequence);
        }
    }
    
    /**
     * Evaluates an shorthand pointer in an XPointer based on the specification at
     * <a href="http://www.w3.org/TR/xptr-framework/#shorthand">http://www.w3.org/TR/xptr-framework/#shorthand</a>
     * 
     * @param ncname an NCName to evaluate.
     * @return an XMLElement pointed to by this shorthand name, or null if none exists.
     */
    private XMLElement evaluateShorthandPointer(NCName ncname) {
        //Iterator for XMLElement from root in document order. See http://www.w3.org/TR/xpath#dt-document-order
        String shorthand = ncname.toString();
        for(Iterator it = new DocumentOrderIterator(root); it.hasNext(); ){
            XMLElement element = (XMLElement)it.next();
            if (testElementShorthand(element, shorthand)) return element;
        }
        return null;
    }
    
    /**
     * Evaluates a child sequence array of Integers to an XMLElement following XML Document Order.
     * This is a helper method used by other evaluation methods in this class.
     * 
     * @param element an XMLElement to start from.
     * @param childSequence an Integer[] to evaluate from the start XMLElement.
     * @return an XMLElement pointed to by this childSequence, or null if none exists.
     */
    private XMLElement evaluateChildSequence(XMLElement element, Integer[] childSequence) {
        for(int i=0; i<childSequence.length; i++) {
            //does the iTh child exist?
            XMLElement[] children = element.getChildElements();
            children = filterNoneElementNodes(children);
            if (childSequence[i].intValue() > children.length) { //childSequence int out of bounds of child array so does not exist.
                return null;
            } else {
                element = element.getChildElements()[childSequence[i].intValue()-1];
            }
        }
        return element;
    }
    
    //Utility classes
    
    /**
     * Filters an XMLElement[] for nodes which are not xml tag elements.
     * 
     * @param nodes an XMLElement[] of the nodes to filter.
     * @return an XMLElement[] of the remaining nodes.
     */
    private static XMLElement[] filterNoneElementNodes(XMLElement[] nodes) {
        List nodeList = Arrays.asList(nodes);
        for(Iterator it = nodeList.iterator(); it.hasNext(); ) {            
            XMLElement node = (XMLElement)it.next();
            if(node.getLocalName().indexOf('#') > -1) {
                it.remove();
            }
        }
        XMLElement[] nNodes = new XMLElement[nodeList.size()];
        nodeList.toArray(nNodes);
        return nNodes;
    }
    
    //Abstract Methods
    /**
     * Tests the element for an id according to the specification at
     * <a href="http://www.w3.org/TR/xptr-framework/#term-sdi">http://www.w3.org/TR/xptr-framework/#term-sdi</a> and returns a boolean answer.
     * 
     * @param element An XMLElement to test for an id.
     * @param id A String of the id to test for.
     * @return boolean value of whether the id matches or not.
     */
    public abstract boolean testElementShorthand(XMLElement element, String id);
    
    //Internal classes
    /**
     * DocumentOrderIterator is a implementation of Iterator which iterates in Document Order from a root XMLElement object.
     * 
     */
    private class DocumentOrderIterator implements Iterator {
        private final Stack stack;
        
        public DocumentOrderIterator(XMLElement root) {
            stack = new Stack();
            stack.add(root);
        }
        
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        public Object next() {
            //Get next element.
            XMLElement element;
            try {
                element = (XMLElement)stack.pop(); 
            } catch (EmptyStackException e) {
                throw new NoSuchElementException();
            }
            //Add children to top of stack in reverse order.
            List children = Arrays.asList(element.getChildElements());
            Collections.reverse(children);
            stack.addAll(children);
            
            return element;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
