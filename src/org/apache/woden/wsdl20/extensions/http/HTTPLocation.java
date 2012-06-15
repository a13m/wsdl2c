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
package org.apache.woden.wsdl20.extensions.http;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.woden.types.NCName;

/**
 * This class represents the {http location} extension property of the  
 * <code>BindingOperation</code> component which maps to the <code>whttp:location</code> 
 * extension attribute of the WSDL binding &lt;operation&gt; element, as defined by the
 * WSDL 2.0 HTTP binding extensions.
 * <p>
 * The value of the <code>whttp:location</code> attribute may contain templates in which elements 
 * from the instance data of the message to be serialized in the request IRI are cited by 
 * enclosing their local name within curly braces. 
 * A template can then be substituted by matching the local name against an element in the instance
 * data and replacing the template in the HTTP Location with the String value of that element.
 * <p>
 * For example, consider the HTTP Location "temperature/{town}" and the message data element
 * <code>&lt;town&gt;Sydney&lt;/town&gt;</code>.
 * After substitution, the formatted HTTP Location is "temperature/Sydney". Note, that the entire
 * template "{town}" is replaced by the matching element's value, "Sydney".
 * <p>
 * If a template is not matched against the instance data, it is replaced in
 * the formatted HTTP Location by the empty string (in other words, it is omitted).
 * <p>
 * This class has one constructor and this takes a String representing a
 * <code>whttp:location</code> value, which may contain the curly brace templates 
 * described above.
 * The class can perform template substitution and return the formatted HTTP Location 
 * resulting from such substitution. It can also return the original HTTP Location
 * value specified on the constructor, so that even after substitution it is possible to
 * see where any templates were used.
 * <p>
 * This class uses the EBNF grammar defined for {http location} by the 
 * WSDL 2.0 HTTP binding extensions to parse and validate the original HTTP Location string. 
 * It checks that any single left and right curly braces are correctly paired to enclose a String 
 * that is of the correct type to represent an element local name (that is, a String of type 
 * xs:NCName). 
 * <p>
 * It also supports the double curly brace syntax used to represent a literal single curly brace 
 * in the formatted HTTP Location. That is, a double curly brace escapes a literal single curly 
 * brace to avoid mistaking it for a template. For example, "abc{{def" is formatted as "abc{def" 
 * and this literal left brace is not interpreted as the beginning of a template.
 * 
 * @author John Kaputin (jkaputin@apache.org)
 */
public class HTTPLocation {
    
    private String fOriginalLocation;
    boolean fValid = true;
    
    private List fValidatedList = null; //used for validating the HTTP location string
    private List fConsolidatedList = null; //used for substitution and formatting
    
    private static final String emptyString = "".intern();
    private static final String questionMark = "?".intern();
    private static final String leftBrace = "{".intern();
    private static final String rightBrace = "}".intern();
    private static final String doubleLeftBraces = "{{".intern();
    private static final String doubleRightBraces = "}}".intern();
    private static final String exclamatedLeftBrace = "{!".intern();
    
    /**
     * Creates an HTTPLocation object to represent the specified HTTP Location String value.
     * This String is typically the value of the <code>whttp:location</code> attribute within 
     * a binding operation element.
     * <p>
     * The location template String argument must not be null.
     *  
     * @param location the String value of the http location
     */
    public HTTPLocation(String location) {
        fOriginalLocation = location;
        
        if(location == null) {
            //TODO throw NPE with suitable error message
            fValid = false;
        } else if(location.equals(emptyString)) {
            fValidatedList = new Vector();
            fValidatedList.add(emptyString);
            fConsolidatedList = new Vector();
            fConsolidatedList.add(emptyString);
        } else {
            List tokenizedList = tokenizeLocation();
            validateTokens(tokenizedList);
            if(fValid) {
                consolidateTokens();
            }
        }
    }
    
    /**
     * Indicates whether the original HTTP Location string used to create this object
     * is valid. That is, whether it can be parse according to the EBNF grammar specified
     * for the {http location} property by the WSDL 2.0 HTTP binding extensions.
     * 
     * @return true if the format of the original HTTP location string is valid, 
     *         otherwise false.
     */
    public boolean isLocationValid() {
        return fValid;
    }
    
    /**
     * Returns the original HTTP Location String specified when this object 
     * was created.
     * 
     * @return the original HTTP Location String
     */
    public String getOriginalLocation() {
        return fOriginalLocation;
    }
    
    /**
     * Returns a formatted String representing the original HTTP Location modified by any
     * template substitution that has taken place via the <code>substitute</code> methods.
     * Templates that have not been matched against any element from the instance data 
     * will be omitted from the formatted String. 
     * An unmatched template is indicated by an HTTPLocationTemplate object whose value is null.
     * <p>
     * If the original HTTP Location does not contain any templates then substitution 
     * is not applicable and this method will return the same String returned by the 
     * <code>getOriginalLocation()</code> method.
     * <p>
     * If the HTTP Locationis invalid this method will return null.
     * 
     * @return the formatted HTTP Location String, after any template substitution
     */
    public String getFormattedLocation() {
        if(!fValid) {
            return null;
        }
        
        StringBuffer buffer = new StringBuffer();
        Object currToken;
        HTTPLocationTemplate template;
        Iterator it = fConsolidatedList.iterator();
        
        while(it.hasNext()) {
            currToken = it.next();
            if(currToken instanceof HTTPLocationTemplate) {
                template = (HTTPLocationTemplate)currToken;
                String value = template.getValue();
                if(value != null) {
                    //the template is replaced in the HTTP Location by the matching element's value
                    buffer.append(value);
                }
            } else {
                buffer.append(currToken);
            }
        }
        
        return buffer.toString();
    }
    
    /**
     * Same behaviour as getFormattedLocation()
     * 
     * @return the formatted HTTP Location String, after any template substitution
     */
    public String toString() {
        return getFormattedLocation();
    }
    
    /**
     * Return the templates that appear in the HTTP Location string in
     * the order they appear.
     * If the HTTP Location contains no templates or if it is 
     * invalid this method will return an empty array.
     * 
     * @return an array of HTTPLocationTemplate objects
     */
    public HTTPLocationTemplate[] getTemplates() {
        List templates = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    templates.add(next);
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[templates.size()];
        templates.toArray(array);
        return array;
    }
    
    /**
     * Return the templates that appear in the URI Path portion of the HTTP Location
     * string in the order they appear.
     * This is the portion before the first occurrence of '?'.
     * If the HTTP Location contains no templates in the Path or if it is 
     * invalid this method will return an empty array.
     * 
     * @return an array of HTTPLocationTemplate objects
     */
    public HTTPLocationTemplate[] getTemplatesInPath() {
        List templates = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    HTTPLocationTemplate template = (HTTPLocationTemplate)next;
                    if(!template.isQuery()) {
                        templates.add(next);
                    }
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[templates.size()];
        templates.toArray(array);
        return array;
    }
    
    /**
     * Return templates that appear in the URI Query portion of the HTTP Location 
     * string in the order they appear. 
     * This is the portion after the first occurrence of '?'.
     * If the HTTP Location contains no templates in the Query or if it is 
     * invalid this method will return an empty array.
     *  
     * @return an array of HTTPLocationTemplate objects
     */
    public HTTPLocationTemplate[] getTemplatesInQuery() {
        List templates = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    HTTPLocationTemplate template = (HTTPLocationTemplate)next;
                    if(template.isQuery()) {
                        templates.add(next);
                    }
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[templates.size()];
        templates.toArray(array);
        return array;
    }
    
    /**
     * Return the names of the templates that appear in the HTTP Location string
     * in the order they appear. 
     * If the HTTP Location contains no templates or if it is 
     * invalid this method will return an empty array.
     * 
     * @return a String array of template names
     */
    public String[] getTemplateNames() {
        List names = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    HTTPLocationTemplate template = (HTTPLocationTemplate)next;
                    names.add(template.getName());
                }
            }
        }
        
        String[] array = new String[names.size()];
        names.toArray(array);
        return array;
    }

    /**
     * Return the names of the templates that appear in the URI Path portion of the
     * HTTP Location string in the order they appear.
     * This is the portion before the first occurrence of '?'.
     * If the HTTP Location contains no templates in the Path or if it is 
     * invalid this method will return an empty array.
     * 
     * @return a String array of template names
     */
    public String[] getTemplateNamesInPath() {
        List names = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    HTTPLocationTemplate template = (HTTPLocationTemplate)next;
                    if(!template.isQuery()) {
                        names.add(template.getName());
                    }
                }
            }
        }
        
        String[] array = new String[names.size()];
        names.toArray(array);
        return array;
    }
    
    /**
     * Return the names of the templates that appear in the URI Query portion of the 
     * HTTP Location string in the order they appear. 
     * This is the portion after the first occurrence of '?'.
     * If the HTTP Location contains no templates in the Query or if it is 
     * invalid this method will return an empty array.
     * 
     * @return a String array of template names
     */
    public String[] getTemplateNamesInQuery() {
        List names = new Vector();
        
        if(fValid) {
            Iterator it = fConsolidatedList.iterator();
            while(it.hasNext()) {
                Object next = it.next();
                if(next instanceof HTTPLocationTemplate) {
                    HTTPLocationTemplate template = (HTTPLocationTemplate)next;
                    if(template.isQuery()) {
                        names.add(template.getName());
                    }
                }
            }
        }
        
        String[] array = new String[names.size()];
        names.toArray(array);
        return array;
    }

    /**
     * Return the first template with the specified name from the HTTP Location string
     * or null if no such template is exists.
     * <p>
     * If the HTTP Location is invalid this method will return null.
     * 
     * @return an HTTPLocationTemplate with the specified name
     */
    public HTTPLocationTemplate getTemplate(String name) {
        if(!fValid) {
            return null;
        }
        
        HTTPLocationTemplate namedTemplate = null;
        if(name != null) {
            HTTPLocationTemplate[] templates = getTemplates();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplate = templates[i];
                    break;
                }
            }
        }
        return namedTemplate;
    }
    
    /**
     * Return the templates with the specified name from the HTTP Location string .
     * If the HTTP Location contains no such templates or if it is 
     * invalid this method will return an empty array.
     * 
     * @return an array of HTTPLocationTemplates with the specified name
     */
    public HTTPLocationTemplate[] getTemplates(String name) {
        List namedTemplates = new Vector();
        
        if(fValid && name != null) {
            HTTPLocationTemplate[] templates = getTemplates();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplates.add(templates[i]);
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[namedTemplates.size()];
        namedTemplates.toArray(array);
        return array;
    }
    
    /**
     * Return the first template with the specified name from the URI Path portion of
     * the HTTP Location string or null if no such template exists. 
     * The Path is the portion before the first occurrence of '?'.
     * <p>
     * If the HTTP Location is invalid this method will return null.
     * 
     * @return an HTTPLocationTemplate with the specified name
     */
    public HTTPLocationTemplate getTemplateInPath(String name) {
        if(!fValid) {
            return null;
        }
        
        HTTPLocationTemplate namedTemplate = null;
        if(name != null) {
            HTTPLocationTemplate[] templates = getTemplatesInPath();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplate = templates[i];
                    break;
                }
            }
        }
        return namedTemplate;
    }
    
    /**
     * Return the templates with the specified name from the URI Path portion of the
     * HTTP Location string. This is the portion before the first occurrence of '?'.
     * If the HTTP Location contains no such templates in the Path or if it is 
     * invalid this method will return an empty array.
     * 
     * @return an array of HTTPLocationTemplates with the specified name
     */
    public HTTPLocationTemplate[] getTemplatesInPath(String name) {
        List namedTemplates = new Vector();
        
        if(fValid && name != null) {
            HTTPLocationTemplate[] templates = getTemplatesInPath();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplates.add(templates[i]);
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[namedTemplates.size()];
        namedTemplates.toArray(array);
        return array;
    }
    
    /**
     * Return the first template with the specified name from the URI Query portion of
     * the HTTP Location string or null if no such template exists. 
     * The Query is the portion after the first occurrence of '?'.
     * <p>
     * If the HTTP Location is invalid this method will return null.
     * 
     * @return an HTTPLocationTemplate with the specified name
     */
    public HTTPLocationTemplate getTemplateInQuery(String name) {
        if(!fValid) {
            return null;
        }
        
        HTTPLocationTemplate namedTemplate = null;
        if(name != null) {
            HTTPLocationTemplate[] templates = getTemplatesInQuery();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplate = templates[i];
                    break;
                }
            }
        }
        return namedTemplate;
    }
    
    /**
     * Return the templates with the specified name from the URI Query portion of the
     * HTTP Location string. This is the portion after the first occurrence of '?'.
     * If the HTTP Location contains no such templates in the Query or if it is 
     * invalid this method will return an empty array.
     * 
     * @return an array of HTTPLocationTemplates with the specified name
     */
    public HTTPLocationTemplate[] getTemplatesInQuery(String name) {
        List namedTemplates = new Vector();
        
        if(fValid && name != null) {
            HTTPLocationTemplate[] templates = getTemplatesInQuery();
            for(int i=0; i<templates.length; i++) {
                if(templates[i].getName().equals(name)) {
                    namedTemplates.add(templates[i]);
                }
            }
        }
        
        HTTPLocationTemplate[] array = new HTTPLocationTemplate[namedTemplates.size()];
        namedTemplates.toArray(array);
        return array;
    }
    
    
    /*
     * Scan the original HTTP Location string char by char, left to right, looking for the
     * following string tokens:
     *   "{{" - literal open brace
     *   "}}" - literal close brace
     *   "{!" - start raw template
     *   "{"  - start encoded template
     *   "}"  - end template
     *   First occurrence of "?" - start of Query string
     *   Any other string
     *   
     * Add these tokens to a list to be used as input to the validation step.
     */
    private List tokenizeLocation() {
        
        StringBuffer buffer = new StringBuffer();
        int len = fOriginalLocation.length();
        char currChar;
        int lastPos = len-1;
        List tokens = new Vector();
        boolean questionMarkFound = false;
        
        for(int i=0; i<len; i++) {
            currChar = fOriginalLocation.charAt(i);
            if(currChar == '?' && !questionMarkFound) {
                if(buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer = new StringBuffer();
                }
                questionMarkFound = true;
                tokens.add(questionMark);
            } else if(currChar == '{') {
                if(buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer = new StringBuffer();
                }
                if(i < lastPos && fOriginalLocation.charAt(i+1) == '{') {
                    tokens.add(doubleLeftBraces);
                    i++;   //move scan position to the 2nd left curly brace
                } else if(i < lastPos && fOriginalLocation.charAt(i+1) == '!') {
                    tokens.add(exclamatedLeftBrace);
                    i++;   //move scan position to the exclamation mark
                } else {
                    tokens.add(leftBrace);
                }
            } else if(currChar == '}') {
                if(buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer = new StringBuffer();
                }
                if(i < lastPos && fOriginalLocation.charAt(i+1) == '}') {
                    tokens.add(doubleRightBraces);
                    i++;   //move scan position to the 2nd right curly brace
                } else {
                    tokens.add(rightBrace);
                }
                
            } else {
                buffer.append(currChar);
            }
        }

        if(buffer.length() > 0) {
            tokens.add(buffer.toString());
        }
        
        return tokens;
    }

    /*
     * The EBNF grammar defined for HTTP Location in WSDL 2.0 spec Part2 Section 6.8.1.1 is:
     * 
     * httpLocation ::= charData? (( openBrace | closeBrace | template ) charData?)*
     * charData ::= [^{}]*
     * openBrace ::= '{{'
     * closeBrace ::= '}}'
     * template ::= rawTemplate | encodedTemplate
     * rawTemplate ::= '{!' NCName '}'
     * encodedTemplate ::= '{' NCName '}' 
     * 
     * The input to this method is an ordered list consisting of the following tokens parsed 
     * from the original HTTP Location string: '{{', '}}', '{!', '{', '}', first occurrence of ?, 
     * any other string.
     * 
     * This method will validate these tokens according to the EBNF grammar above. That is,
     * it will do the following checks in order against each token, stopping and moving on 
     * to the next token if any check is satisfied:
     * 1) check for a '?' indicating the start of the URI Query string
     * 2) check for the openBrace '{{'
     * 3) check for the closeBrace '}}'
     * 4) check for a rawTemplate, based on the sequence of tokens '{!', NCName, '}' 
     * 5) check for an encodedTemplate, based on the sequence of tokens '{', NCName, '}'
     * 6) check that any remaining string is valid charData (has no curly braces).
     * 
     * Encoded templates have a matching pairs of left and right curly braces "{...}".
     * Raw templates have an exclamated left brace and a right brace "{!...}".
     * The NCName string in a raw or encoded template must be of type xs:NCName 
     * (that is, xs:NCName is the type defined for the local name of an XML element).
     * 
     * Valid tokens will be copied to a 'validated' list. The tokens comprising any raw or encoded
     * templates identified at steps 3) or 4) will be replaced in this new list by 
     * HTTPLocationTemplate objects. 
     * 
     * If an error is found, the HTTP Location will be flagged as invalid and no formatted location
     * or templates will be available.
     */
    private void validateTokens(List tokenizedList) {
        
        List tokens = new Vector();
        Object currToken;
        String nextToken;
        Object nextNextToken;
        int size = tokenizedList.size();
        ListIterator it = tokenizedList.listIterator();
        boolean isQuery = false;
        
        while(fValid && it.hasNext()) {
            currToken = it.next();
            int currIndex = it.previousIndex();
            
            if(currToken == questionMark) {
                // 1) check for a '?' indicating the start of the URI Query string
                isQuery = true;
                tokens.add(questionMark);
            } else if(currToken == doubleLeftBraces || currToken == doubleRightBraces) {
                // 2) check for the openBrace '{{'
                // 3) check for the closeBrace '}}'
                tokens.add(currToken);
            } else if(currToken == leftBrace || currToken == exclamatedLeftBrace) {
                // 4) check for a rawTemplate, based on the sequence of tokens '{!', NCName, '}' 
                // 5) check for an encodedTemplate, based on the sequence of tokens '{', NCName, '}'
                if(size-currIndex < 3) {
                    fValid = false; //this token is not followed by two further tokens 
                } else {
                    nextToken = (String)it.next();
                    nextNextToken = it.next();
                    if(NCName.isValid(nextToken) && nextNextToken == rightBrace) {
                        boolean isEncoded = (currToken == leftBrace);
                        HTTPLocationTemplate template = 
                            new HTTPLocationTemplate(nextToken, isEncoded, isQuery);
                        tokens.add(template);
                    } else {
                        fValid = false; //we don't have a valid template
                    }
                }
            } else {
                // 6) check that any other type of token is valid charData (has no curly braces).
                String s = (String)currToken;
                int index = s.indexOf(leftBrace);
                if(index < 0) {
                    index = s.indexOf(rightBrace);
                }
                if(index >= 0) {
                    fValid = false; //contains unmatched curly braces
                } else {
                    tokens.add(currToken);
                }
            }
        } //end while loop

        if(fValid) {
            fValidatedList = tokens;
        }
    }
    
    /*
     * Consolidate the validated tokens parsed from the HTTP Location string, into a list containing
     * only HTTPLocationTemplate objects, literal single braces or Strings representing any other 
     * HTTP Location content.  This consolidated list will be used for template substitution 
     * and for producing an external representation of the HTTP Location string, formatted with the 
     * substituted values. 
     * Note, any double curly braces will be replaced by a literal single brace. 
     */
    private void consolidateTokens() {
        
        StringBuffer buffer = new StringBuffer();
        Object currToken;
        List tokens = new Vector();
        Iterator it = fValidatedList.iterator();
        
        while(it.hasNext()) {
            currToken = it.next();
            if(currToken instanceof HTTPLocationTemplate) {
                //Output any previously concatentated string content then output this template's value.
                if(buffer.length() > 0) {
                    tokens.add(buffer.toString());
                    buffer = new StringBuffer();
                }
                tokens.add(currToken);
            } else if(currToken == doubleLeftBraces) {
                buffer.append(leftBrace);
            } else if(currToken == doubleRightBraces) {
                buffer.append(rightBrace);
            } else {
                //concatentate this token to previous string content
                buffer.append(currToken);
            }
        }
        
        if(buffer.length() > 0) {
            tokens.add(buffer.toString());
        }
        
        fConsolidatedList = tokens;
    }
    
}
