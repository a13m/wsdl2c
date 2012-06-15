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

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.validation.Assertion;
import org.apache.woden.wsdl20.validation.WodenContext;

/**
 * This class represents assertion Interface-1009 from the WSDL 2.0 specification.
 * For details about this assertion see:
 * http://www.w3.org/TR/2007/REC-wsdl20-20070626/#Interface-1009
 * 
 * @author Lawrence Mandel (lmandel@apache.org)
 */
public class Interface1009 implements Assertion {

	public final static String ID = "Interface-1009".intern();
	
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
		Interface interfac = (Interface)target;
		Interface[] extendedInterfaces = interfac.getExtendedInterfaces();
		if(containsInterface(interfac, extendedInterfaces)) {
			try {
				wodenCtx.getErrorReporter().reportError(new ErrorLocatorImpl(), ID , new Object[]{interfac.getName()}, ErrorReporter.SEVERITY_ERROR);
			}catch(WSDLException e) {
				//TODO: Log problem reporting error.
			}
		}
	}
	
	/**
	   * Check whether the specified interface is in the list of extended interfaces.
	   * 
	   * @param interfac The interface that should be checked to see if it is in the list of exteneded interfaces. 
	   * @param extendedInterfaces An array of interfaces representing the list of extended interfaces.
	   * @return true if the interface is in the list of extended interfaces, false otherwise.
	   */
	private boolean containsInterface(Interface interfac, Interface[] extendedInterfaces) {
		boolean foundInterface = false;
		
		int numExtInterfaces = extendedInterfaces.length;
		for(int i = 0; i < numExtInterfaces && !foundInterface; i++) {
		  if(interfac.isEquivalentTo(extendedInterfaces[i]))
		    foundInterface = true;
		  else if(containsInterface(interfac, extendedInterfaces[i].getExtendedInterfaces()))
			foundInterface = true;
		}
		return foundInterface;
	  }

}
