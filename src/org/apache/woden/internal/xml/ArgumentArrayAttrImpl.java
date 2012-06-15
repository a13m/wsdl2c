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

package org.apache.woden.internal.xml;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.ErrorLocatorImpl;
import org.apache.woden.internal.util.StringUtils;
import org.apache.woden.wsdl20.extensions.rpc.Argument;
import org.apache.woden.wsdl20.extensions.rpc.Direction;
import org.apache.woden.xml.ArgumentArrayAttr;

/**
 * This class represents an XML attribute information items whose type is a list
 * of pairs (xs:QName, xs:token) that obey the contraints of wrpc:signature as
 * defined in Part 2 of the WSDL 2.0 spec.
 * 
 * @author Arthur Ryman (ryman@ca.ibm.com)
 */

public class ArgumentArrayAttrImpl extends XMLAttrImpl implements
		ArgumentArrayAttr {
	
	public ArgumentArrayAttrImpl(XMLElement ownerEl, QName attrType,
			String attrValue, ErrorReporter errRpt) throws WSDLException {

		super(ownerEl, attrType, attrValue, errRpt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.woden.xml.ArgumentArrayAttr#getArgumentArray()
	 */
	public Argument[] getArgumentArray() {

		return (Argument[]) getContent();
	}

	protected Object convert(XMLElement ownerEl, String attrValue)
			throws WSDLException {

		setValid(false);

		if (attrValue != null) {

			List tokens = StringUtils.parseNMTokens(attrValue);
			int length = tokens.size();

			// list consists of (qname, token) pairs
			if (length % 2 == 0) {

				int argc = length / 2;
				Argument[] args = new Argument[argc];

				Iterator i = tokens.iterator();
				for (int j = 0; j < argc; j++) {

					String qnameStr = (String) i.next();
					QName qname = convertQName(ownerEl, qnameStr);
					if (qname == null)
						return null;

					String directionStr = (String) i.next();
					Direction direction = convertDirection(directionStr);
					if (direction == null)
						return null;

					args[j] = new Argument(qname, direction);
				}

				setValid(true);
				return args;
			}
		}

		// TODO: line&col nos.
		// TODO: use correct error number
		getErrorReporter().reportError(new ErrorLocatorImpl(), "WSDL510",
				new Object[] { attrValue }, ErrorReporter.SEVERITY_ERROR,
				new IllegalArgumentException());

		return null;
	}

	private QName convertQName(XMLElement ownerEl, String qnameStr)
			throws WSDLException {

		QName qname = null;

		try {
			qname = ownerEl.getQName(qnameStr);
		} catch (WSDLException e) {

			// TODO: line&col nos.
			// TODO: use correct error number
			getErrorReporter().reportError(new ErrorLocatorImpl(), "WSDL510",
					new Object[] { qnameStr }, ErrorReporter.SEVERITY_ERROR, e);
		}

		return qname;
	}

	private Direction convertDirection(String directionStr)
			throws WSDLException {

		Direction[] directions = new Direction[] { Direction.IN,
				Direction.INOUT, Direction.OUT, Direction.RETURN };

        for (int i = 0; i < directions.length; i++) {
            if (directionStr.equals(directions[i].toString())) {
                return directions[i];
            }
        }

		// TODO: line&col nos.
		// TODO: use correct error number
		getErrorReporter().reportError(new ErrorLocatorImpl(), "WSDL510",
				new Object[] { directionStr }, ErrorReporter.SEVERITY_ERROR,
				new IllegalArgumentException());

		return null;

	}

}
