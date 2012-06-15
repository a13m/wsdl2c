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
package org.apache.woden.internal.wsdl20.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.woden.WSDLException;
import org.apache.woden.internal.WSDLContext;
import org.apache.woden.internal.wsdl20.assertions.Description1001;
import org.apache.woden.internal.wsdl20.assertions.Description1002;
import org.apache.woden.internal.wsdl20.assertions.Interface1009;
import org.apache.woden.internal.wsdl20.assertions.Interface1010;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.validation.Assertion;
import org.apache.woden.wsdl20.validation.WodenContext;
import org.apache.woden.wsdl20.validation.AssertionInfo;
import org.apache.woden.wsdl20.xml.DescriptionElement;

/**
 * Validates a WSDL 2.0 Description component by walking the component model
 * <i>tree</i> and checking the relevant assertions for each component.
 * <p>
 * TODO add dependency checking for prereq assertions<br>
 * TODO decide if a return value is needed indicating the result (boolean or err/warn/OK)
 * 
 * @author John Kaputin (jkaputin@apache.org)
 *
 */
public class WSDLValidator {
    
    private WSDLContext fWsdlCtx;
    private WodenContext fWodenCtx;
    
    //not needed? ... private Map fAssertions;    //map of assertion id string -> AssertionInfo
    
    private Map fWsdlAsserts;   //map of target Class -> list of WSDL 2.0 Assertions
    private Map fExtAsserts;    //map of target Class -> list of extension Assertions
    
    public void validate(Description description, WSDLContext wsdlContext) throws WSDLException {
        int len = 0;
        this.fWsdlCtx = wsdlContext;
        this.fWodenCtx = new WodenContextImpl(fWsdlCtx.errorReporter, fWsdlCtx.uriResolver);
               
        //setup the WSDL 2.0 assertions
        //TODO do this once per wsdl reader object, not per document
        setupWSDLAssertions();
        
        //setup the extension assertions. 
        //TODO check - must be done per document in case ext reg has changed
        setupExtensionAssertions();
        
        //walk the parts of the xml tree not represented in the component model, calling checkAssertions
        //method for each element.
        // - i.e. description(s), import, include, types, documentation?
        
        DescriptionElement descElem = description.toElement();
        
        checkAssertions(DescriptionElement.class, descElem);
        
        //check assertions for the Description component
        
        checkAssertions(Description.class, description);
        
        //walk the top-level component trees, calling checkAssertions for each component and for each 
        //component.toElement().
        
        Interface[] intfaces = description.getInterfaces();
        len = intfaces.length;
        for(int i=0; i<len; i++) {
            checkAssertions(Interface.class, intfaces[i]);
        }
        
    }
    
    /*
     * Invoke the validate() method on each assertion mapped to the target WSDL 2.0 object.
     * This object will be a WSDLComponent or a WSDLElement. 
     * Note: with the outstanding API review issue about merging the two WSDL models, might be
     * able to change the Object paramater to a Woden-specific type.
     */
    private void checkAssertions(Class targetClass, Object target) throws WSDLException {
        
        Assertion a = null;
        
        //Check WSDL 2.0 assertions
        List wsdlAsserts = (List)fWsdlAsserts.get(targetClass);
        if (wsdlAsserts != null) {
            for (Iterator i = wsdlAsserts.iterator(); i.hasNext();) {
                a = (Assertion) i.next();
                a.validate(target, fWodenCtx);
            }
        }
        //Check extension assertions (get them from ExtensionRegistry)
        List extAsserts = (List)fExtAsserts.get(targetClass);
        if (extAsserts != null) {
            for (Iterator i = extAsserts.iterator(); i.hasNext();) {
                a = (Assertion) i.next();
                a.validate(target, fWodenCtx);
            }
        }
        
    }
    
    private void setupWSDLAssertions() {
        
        /* This map of WSDL 2.0 assertions might not be needed
         * 
        fAssertions.put("Description-1001".intern(), 
                new AssertionInfo(new Description1001(), DescriptionElement.class));
        fAssertions.put("Description-1002".intern(), 
                new AssertionInfo(new Description1002(), DescriptionElement.class));
        fAssertions.put("Description-1003".intern(), 
                new AssertionInfo(new Description1003(), DescriptionElement.class));
        fAssertions.put(Interface1009.ID, 
                new AssertionInfo(new Interface1009(), Interface.class));
        fAssertions.put(Interface1010.ID, 
                new AssertionInfo(new Interface1010(), Description.class));
        //TODO rest of WSDL 2.0 assertions defined in the spec
         * 
         */
        
        //Populate the Map of targetClass->List of WSDL 2.0 Assertions
        
        fWsdlAsserts = new HashMap();
        
        List descElem = new Vector();
        descElem.add(new Description1001());
        descElem.add(new Description1002());
        descElem.add(new Description1002());
        fWsdlAsserts.put(DescriptionElement.class, descElem);
        
        List desc = new Vector();
        desc.add(new Interface1010());
        fWsdlAsserts.put(Description.class, desc);
        
        List intf = new Vector();
        intf.add(new Interface1009());
        fWsdlAsserts.put(Interface.class, intf);
        
    }
    
    private void setupExtensionAssertions() {
        
        fExtAsserts = new HashMap();

        AssertionInfo[] infos = this.fWsdlCtx.extensionRegistry.queryAssertions();
        List asserts;
        int len = infos.length;
        for(int i=0; i<len; i++) {
            Class target = infos[i].targetClass;
            asserts = (List) fExtAsserts.get(target);
            if(asserts == null) {
                asserts = new Vector();
            }
            asserts.add(infos[i].assertion);
            fExtAsserts.put(target, asserts);
        }
    }

}
