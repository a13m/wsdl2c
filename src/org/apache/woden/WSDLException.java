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
package org.apache.woden;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WSDLException extends Exception
{
  public static final long serialVersionUID = 1;

  public static final String INVALID_WSDL = "INVALID_WSDL";
  public static final String PARSER_ERROR = "PARSER_ERROR";
  public static final String OTHER_ERROR = "OTHER_ERROR";
  public static final String CONFIGURATION_ERROR = "CONFIGURATION_ERROR";
  public static final String UNBOUND_PREFIX = "UNBOUND_PREFIX";
  public static final String NO_PREFIX_SPECIFIED = "NO_PREFIX_SPECIFIED";

  private String fFaultCode = null;
  private Throwable fTargetThrowable = null;
  private String fLocation = null;

  public WSDLException(String faultCode, String msg, Throwable t)
  {
    super(msg);
    setFaultCode(faultCode);
    setTargetException(t);
  }

  public WSDLException(String faultCode, String msg)
  {
    this(faultCode, msg, null);
  }

  public void setFaultCode(String faultCode)
  {
    fFaultCode = faultCode;
  }

  public String getFaultCode()
  {
    return fFaultCode;
  }

  public void setTargetException(Throwable targetThrowable)
  {
    fTargetThrowable = targetThrowable;
  }

  public Throwable getTargetException()
  {
    return fTargetThrowable;
  }

  /**
   * Set the location using an XPath expression. Used for error messages.
   *
   * @param location an XPath expression describing the location where
   * the exception occurred.
   */
  public void setLocation(String location)
  {
    fLocation = location;
  }

  /**
   * Get the location, if one was set. Should be an XPath expression which
   * is used for error messages.
   */
  public String getLocation()
  {
    return fLocation;
  }

  public String getMessage()
  {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append("WSDLException");

    if (fLocation != null)
    {
        strBuf.append(" (at " + fLocation + ")");
    }

    if (fFaultCode != null)
    {
      strBuf.append(": faultCode=" + fFaultCode);
    }

    String thisMsg = super.getMessage();
    String targetMsg = (fTargetThrowable != null)
                       ? fTargetThrowable.getMessage()
                       : null;

    if (thisMsg != null
        && (targetMsg == null || !thisMsg.equals(targetMsg)))
    {
      strBuf.append(": " + thisMsg);
    }

    if (targetMsg != null)
    {
      strBuf.append(": " + targetMsg);
    }

    return strBuf.toString();
  }

  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.print(getMessage() + ": ");

    if (fTargetThrowable != null)
    {
      fTargetThrowable.printStackTrace(pw);
    }

    return sw.toString();
  }
}