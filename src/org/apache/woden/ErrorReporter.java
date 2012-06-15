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

import java.util.Locale;


/**
 * Defines the approach for reporting errors that occur while parsing, 
 * validating or
 * manipulating WSDL descriptions, such as XML parser errors or violations
 * of the rules defined in the WSDL specification. That is, errors that
 * relate specifically to the WSDL. It does not report system runtime 
 * or configuration errors, which are instead treated as exceptions.
 * <p>
 * There are four ways to report an error:
 * <p>
 * An error id and an array of message arguments are used to produce 
 * a formatted error message from some parameterized message text.
 * The error may be reported with an target exception or without one.
 * <p>
 * An error id is specified with some ready-formatted message text.
 * The error may be reported with an target exception or without one.
 * <p>
 * The error is handled according to the severity level 
 * (warning, error or fatal error) reported with the error.
 * <p/>
 * The error reporter supports the 'en' (English) locale by default 
 * and has a default error handler (i.e. a default implementation of 
 * ErrorHandler). However, a different locale may be configured 
 * via <code>setLocale</code> and a custom error handler implementation 
 * may be configured as a system property.
 * 
 * @author jkaputin@apache.org
 */
public interface ErrorReporter {
    
    public static final short SEVERITY_WARNING = 1;

    public static final short SEVERITY_ERROR = 2;

    public static final short SEVERITY_FATAL_ERROR = 3;

    /**
     * Insert the message arguments into a parameterized message identified 
     * by errorId to produce a formatted error message, then report the 
     * message and the error location in the document according to the 
     * severity. 
     * 
     * @param errLoc ErrorLocator showing the location of the error in the document. 
     * @param errorId String that identifies the message for this error.
     * @param arguments Object[] with values to be inserted into the message text.
     * @param severity a short indicating warning, error or fatal error.
     * 
     * @throws WSDLException if the severity is fatal error
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            Object[] arguments, 
                            short severity)
    throws WSDLException;

    /**
     * Insert the message arguments into a parameterized message identified 
     * by errorId to produce a formatted error message, then report the 
     * message and the error location in the document according to the 
     * severity. Also report the exception that caused this error.
     * 
     * @param errLoc ErrorLocator showing the location of the error in the document. 
     * @param errorId String that identifies the message for this error.
     * @param arguments Object[] with values to be inserted into the message text.
     * @param severity a short indicating warning, error or fatal error.
     * @param exception the Exception that caused this error 
     * 
     * @throws WSDLException if the severity is fatal error
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            Object[] arguments, 
                            short severity,
                            Exception exception) 
    throws WSDLException;

    /**
     * Report the message and the error location in the document according 
     * to the severity.
     * 
     * @param errLoc ErrorLocator showing the location of the error in the document. 
     * @param errorId String that identifies the message for this error.
     * @param message message text.
     * @param severity a short indicating warning, error or fatal error.
     * 
     * @throws WSDLException if the severity is fatal error
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            String message, 
                            short severity)
    throws WSDLException;

    /**
     * Report the message and the error location in the document according 
     * to the severity. Also report the exception that caused this error.
     * 
     * @param errLoc ErrorLocator showing the location of the error in the document. 
     * @param errorId String that identifies the message for this error.
     * @param message message text.
     * @param severity a short indicating warning, error or fatal error.
     * @param exception the Exception that caused this error 
     * 
     * @throws WSDLException if the severity is fatal error
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            String message, 
                            short severity,
                            Exception exception)
    throws WSDLException;
    
    /**
     * Set a custom error handler on this error reporter to replace the
     * default error handler.
     * 
     * TODO: identify all use cases for setting the error handler and determine how best to
     * expose this on the API. Currently, just via WSDLReader.setLocale which supports 
     * the 'read wsdl' use case, but others may include 'modify wsdl', 
     * 'create wsdl programmatically' and maybe 'write wsdl'.
     * 
     * @param errorHandler the custom error handler
     * 
     */
    public void setErrorHandler(ErrorHandler errorHandler);

    /**
     * Return the custom error handler if one has been set,  
     * otherwise return the default error handler.
     * 
     * @return the ErrorHandler used by this error reporter
     */
    public ErrorHandler getErrorHandler();

    /**
     * Set the Locale used for localization of error messages.
     * 
     * TODO: identify all use cases for setting the locale and determine how best to
     * expose this on the API. Currently, just via WSDLReader.setLocale which supports 
     * the 'read wsdl' use case, but others may include 'modify wsdl', 
     * 'create wsdl programmatically' and maybe 'write wsdl'.
     * 
     * @param locale the required locale
     * 
     */
    public void setLocale(Locale locale);

    /**
     * @return the Locale used for localization of error messages.
     */
    public Locale getLocale();

    /**
     * Returns a formatted message string for the specified message key and arguments.
     * Used for formatting messages that will not be reported by the error reporter as
     * parsing errors (via the reportError method). Typically these types of messages 
     * will be for configuration or runtime errors that will be thrown as exceptions
     * by the caller.
     * If there are no message arguments, a null value may be specified for the 
     * <code>arguments</code> parameter instead of an empty array. 
     * 
     * @param errorId a String representing the message key
     * @param arguments an Object array of message parameters
     * @return the formatted message string
     */
    public String getFormattedMessage(String errorId, Object[] arguments);
}