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
package org.apache.woden.internal.wsdl20.assertions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.validation.Assertion;
import org.apache.woden.wsdl20.validation.WodenContext;

/**
 * This class represents assertion Interface-1010 from the WSDL 2.0 specification.
 * For details about this assertion see:
 * http://www.w3.org/TR/2007/REC-wsdl20-20070626/#Interface-1010
 * 
 * @author Lawrence Mandel (lmandel@apache.org)
 */
public class Interface1010 implements Assertion {

	public final static String ID = "Interface-1010".intern();
	
	/* (non-Javadoc)
	 * @see org.apache.woden.wsdl20.validation.Assertion#getAssertionID()
	 */
	public String getId() {
		return ID;
	}

	/* (non-Javadoc)
	 * @see org.apache.woden.wsdl20.validation.Assertion#validate(java.lang.Object, org.apache.woden.wsdl20.validation.WodenContext)
	 */
	public void validate(Object target, WodenContext wodenCtx) throws WSDLException {
		Description desc = (Description)target;
		Interface[] interfaces = desc.getInterfaces();
		
		List names = new ArrayList();
		int numInterfaces = interfaces.length;
		for(int i = 0; i < numInterfaces; i++) {
			QName name = interfaces[i].getName();
			if(name == null)
				continue;
			if(names.contains(name)) {
				try {
					wodenCtx.getErrorReporter().reportError(new ErrorLocatorImpl(), ID, new Object[]{name}, ErrorReporter.SEVERITY_ERROR);
				}catch(WSDLException e) {
					//TODO: Log problem reporting error.
				}
			}
			else {
				names.add(name);
			}
		}
	}
}
