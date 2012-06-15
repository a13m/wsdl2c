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
package org.apache.woden.internal;

import java.util.Locale;

import org.apache.woden.ErrorHandler;
import org.apache.woden.ErrorInfo;
import org.apache.woden.ErrorLocator;
import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.internal.util.PropertyUtils;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;


/**
 * This class reports errors that occur while parsing, validating or
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
 * @author John Kaputin (jkaputin@apache.org)
 */
public class ErrorReporterImpl implements ErrorReporter {
    
    //TODO: add further behaviour as requirements emerge - e.g.
    //using a xml locator object to report line/col numbers.

    //the ISO-639 language code for the required locale
    protected static final String LOCALE_LANGUAGE = 
        "org.apache.woden.locale-language";

    //the class name of a custom error handler
    protected static final String ERROR_HANDLER_NAME = 
        "org.apache.woden.error-handler-name";

    //"true" or "false" to continue parsing after a fatal error
    protected static final String CONTINUE_AFTER_FATAL_ERROR = 
        "org.apache.woden.continue-after-fatal-error";

    /*
     * This property specifies the resource bundle containing the core Woden error messages.
     * 
     * TODO extract these errors to a new public (non-internal) bundle.
     * TODO define a public constants file with this type of info.
     */
      static final private String CORE_RESOURCE_BUNDLE = "org.apache.woden.internal.Messages";
      
    //Used for localization of error messages.
    private Locale fLocale;
    
    //Combines parameterized message text with message parameters 
    private MessageFormatter fMessageFormatter;
    
    //Used only if no custom error handler has been specified
    private ErrorHandler fDefaultErrorHandler;
    
    //Custom error handler to use instead of the default error handler
    private ErrorHandler fErrorHandler;
    
    //ExtensionRegistry contains names of user-registered error message ResourceBundles
    private ExtensionRegistry fExtensionRegistry;

    /*
     * The default constructor sets the instance variables. It uses default
     * implementations for <code>fMessageFormatter</code> and 
     * <code>fDefaultErrorHandler</code>. It checks for optional properties that
     * specify the settings for the remaining variables.
     * For <code>fLocale</code>, property org.apache.woden.locale can be used to
     * specify the ISO-639 language code for the required locale. If it is omitted 
     * it will default to "en" for English. There is also a setter method which will
     * override any value set by this constructor.
     * For <code>fErrorHandler</code>, property org.apache.woden.error-handler-name
     * can be used to specify the class name of the custom error handler or if
     * omitted, it is set to null.  There is also a setter method which will
     * override any value set by this constructor.
     * <p/> 
     * Their are several alternatives for specifying these properties, so the property lookup 
     * mechanism is encapsulated in a separate <code>PropertyUtils</code> class.
     * <p/>
     * TODO fContinueAfterFatalError and related code has been commented out for 
     * the time being. If we cannot identify a use case for it, it will be removed.
     * <p/>
     * For <code>fContinueAfterFatalError</code>, 
     * org.apache.woden.continue-after-fatal-error can be used to specify 
     * "true" or "false". If this property is omitted or contains any other value,
     * default to "true".

     * @throws WSDLException wraps exceptions that may occur while creating 
     * objects from Class names specified as properties (e.g. 
     * ClassNotFoundException, InstantiationException and IllegalAccessException).
     * 
     */
    public ErrorReporterImpl() throws WSDLException
    {
        fMessageFormatter = new MessageFormatter();
        fDefaultErrorHandler = new ErrorHandlerImpl();
        
        //Set the locale using the language code property if specified.
        String localeLanguage = PropertyUtils.findProperty(LOCALE_LANGUAGE);

        if (localeLanguage != null) {
            fLocale = new Locale(localeLanguage);;
        }
        
        //TODO handle an unsupported locale lang code 
        //(e.g. use system locale and log a warning message)
        
        //Set the custom error handler from the error handler property if specified.
        String errorHandlerName = PropertyUtils.findProperty(ERROR_HANDLER_NAME);
        
        if (errorHandlerName != null)
        {
            try
            {
                Class cl = Class.forName(errorHandlerName);
                fErrorHandler = (ErrorHandler)cl.newInstance();
            }
            catch (Exception e)
            {
                /*
                 ClassNotFoundException
                 InstantiationException
                 IllegalAccessException
                 */
                throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                        "Problem instantiating the customer error handler.",
                        e);
            }
        } 
        else 
        {
            fErrorHandler = null;
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#reportError(org.apache.woden.ErrorLocator, java.lang.String, java.lang.Object[], short)
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            Object[] arguments, 
                            short severity)
    throws WSDLException
    {
        reportError(errLoc, errorId, arguments, severity, null);
    }
        
    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#reportError(org.apache.woden.ErrorLocator, java.lang.String, java.lang.Object[], short, java.lang.Exception)
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            Object[] arguments, 
                            short severity,
                            Exception exception)
    throws WSDLException
    {
        String[] names = getResourceBundleNames();
        String message = fMessageFormatter.formatMessage(fLocale, errorId, arguments, names);
        reportError(errLoc, errorId, message, severity, exception);
    }
    
    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#reportError(org.apache.woden.ErrorLocator, java.lang.String, java.lang.String, short)
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            String message, 
                            short severity)
    throws WSDLException
    {
        reportError(errLoc, errorId, message, severity, null);
    }
    
    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#reportError(org.apache.woden.ErrorLocator, java.lang.String, java.lang.String, short, java.lang.Exception)
     */
    public void reportError(ErrorLocator errLoc, 
                            String errorId, 
                            String message, 
                            short severity,
                            Exception exception)
    throws WSDLException
    {
        ErrorInfo errorInfo = 
            new ErrorInfoImpl(errLoc, errorId, message, exception);

        ErrorHandler eh = 
            (fErrorHandler != null) ? fErrorHandler : fDefaultErrorHandler;
        
        if(severity == SEVERITY_WARNING) {
            eh.warning(errorInfo);
        }
        else if(severity == SEVERITY_ERROR) {
            eh.error(errorInfo);
        }
        else if(severity == SEVERITY_FATAL_ERROR) {
            eh.fatalError(errorInfo);
            
            // Fatal error strategy is to terminate with a WSDLException.
            
            if(exception == null) {
                throw new WSDLException(WSDLException.INVALID_WSDL,
                        "Fatal WSDL error:\n" + errorInfo.toString());
            }
            else if(exception instanceof WSDLException) {
                throw (WSDLException)exception;
            }
            else {
                throw new WSDLException(WSDLException.OTHER_ERROR,
                                        "Fatal error.",
                                        exception);
            }
        }
        else 
        {
            //TODO externalize these messages for localization
            throw new IllegalArgumentException("Invalid severity: " + severity);
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#setErrorHandler(org.apache.woden.ErrorHandler)
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        fErrorHandler = errorHandler;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#getErrorHandler()
     */
    public ErrorHandler getErrorHandler() {
        return (fErrorHandler != null) ? fErrorHandler : fDefaultErrorHandler;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#setLocale(java.util.Locale)
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#getLocale()
     */
    public Locale getLocale() {
        return fLocale;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.woden.ErrorReporter#getFormattedMessage(java.lang.String, java.lang.Object[])
     */
    public String getFormattedMessage(String key, Object[] arguments)
    {
        String[] names = getResourceBundleNames();
        String message = fMessageFormatter.formatMessage(fLocale, key, arguments, names);
        return message;
    }
    
    //Package private as this is only 
    void setExtensionRegistry(ExtensionRegistry extensionRegistry) {
        fExtensionRegistry = extensionRegistry;
    }
    
    private String[] getResourceBundleNames() {
        if(fExtensionRegistry != null) {
            return fExtensionRegistry.queryResourceBundleNames();
        } else {
            return new String[] {CORE_RESOURCE_BUNDLE};
        }
    }
}
