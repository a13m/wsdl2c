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

//Woden
import org.apache.woden.XMLElement;
import org.apache.woden.internal.DOMXMLElement;
import org.apache.woden.ErrorReporter;

//XPointer
import org.apache.woden.xpointer.XPointer;

//DOM
import org.w3c.dom.Element;

/**
 * This class extends the XMLElementEvaluator to support the DOM implementation in XMLElement.
 * 
 * @author Dan Harvey <danharvey42@gmail.com>
 *
 */
public class DOMXMLElementEvaluator extends XMLElementEvaluator {

    /**
     * Constructs a new DOMXMLElementEvaluator to evaluate a XPointer on a DOM Element.
     * 
     * @param xpointer an XPointer to evaluate.
     * @param element an DOM Element to be evaluated. 
     * @param errorReporter the Woden Error Reporter context object.
     */
    public DOMXMLElementEvaluator(XPointer xpointer, Element element, ErrorReporter errorReporter) {
        super(xpointer, createXMLElement(element, errorReporter));
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.woden.internal.xpointer.XMLElementEvaluator#testElementShorthand(org.apache.woden.XMLElement, java.lang.String)
     */
    public boolean testElementShorthand(XMLElement element, String shorthand) {
        //Simple http://www.w3.org/TR/xml-id/ support for now until we support full scheme based ID's.
        Element domElement = (Element)element.getSource();
        String attr = domElement.getAttributeNS("http://www.w3.org/XML/1998/namespace", "id");
        return attr != null && attr.equals(shorthand);
    }
    
    /**
     * Evaluates the XPointer on the root Element and returns the resulting Element or null.
     * 
     * @return an Element from the resultant evaluation of the root Element or null if evaluation fails.
     */
    public Element evaluateElement(){
        XMLElement element = evaluate();
        if (element != null) return (Element)element.getSource();
        return null;
    }
    
    //Private methods
    private static XMLElement createXMLElement(Element element, ErrorReporter errorReporter) {
        DOMXMLElement domXMLElement = new DOMXMLElement(errorReporter);
        domXMLElement.setSource(element);
        return domXMLElement;
    }
}
