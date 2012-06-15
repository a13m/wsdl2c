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




import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLWriter;
import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.internal.WSDLContext;


/**
 * This abstract class contains properties and methods common
 * to WSDLWriter implementations. This abstract class implements
 * methods of the WSDLWriter interface that are common across all
 * concrete Writer implementations such as setting and getting
 * features and properties, ExtensionRegistry.
 *
 *   @author Sagara Gunathunga (sagara.gunathunga@gmail.com)
 */

 public abstract  class BaseWSDLWriter implements WSDLWriter{

     private String fFactoryImplName = null; //TODO deprecate/remove?

     protected WSDLContext fWsdlContext;
     final protected WriterFeatures features;

     public BaseWSDLWriter(WSDLContext wsdlContext){
         fWsdlContext = wsdlContext;
         fFactoryImplName = fWsdlContext.wsdlFactory.getClass().getName();
         features = new WriterFeatures();

     }

     /**
      * @return Returns the fErrorReporter.
      */
     public ErrorReporter getErrorReporter()
     {
         return fWsdlContext.errorReporter;
     }

     /**
      * Get the cached WSDLFactory if there is one, otherwise
      * create and cache a new one.
      *
      * TODO see setFactoryImplName todo
      *
      * @return Returns a.
      */
     protected WSDLFactory getFactory() throws WSDLException
     {
         return fWsdlContext.wsdlFactory;
         /*
         if(fFactory == null)
         {
             fFactory = (fFactoryImplName != null)
                         ? WSDLFactory.newInstance(fFactoryImplName)
                         : WSDLFactory.newInstance();
         }
         return fFactory;
         */
     }

     public void setFactoryImplName(String factoryImplName) {



         fFactoryImplName = factoryImplName;
     }

     /**
      * @return the WSDLFactory implementation classname
      */
     public String getFactoryImplName() {
         return fFactoryImplName;
     }

     public void setExtensionRegistry(ExtensionRegistry extReg)
     {
         if(extReg == null) {
             String msg = fWsdlContext.errorReporter.getFormattedMessage(
                     "WSDL014", new Object[] {});
             throw new NullPointerException(msg);
         }

         fWsdlContext = new WSDLContext(
                 fWsdlContext.wsdlFactory,
                 fWsdlContext.errorReporter,
                 extReg,
                 fWsdlContext.uriResolver);
     }

     public ExtensionRegistry getExtensionRegistry()
     {
         return fWsdlContext.extensionRegistry;
     }

     public void setFeature(String name, boolean value)
     {
         if(name == null)
         {
             //name must not be null
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL005", null));
         }
         try
         {
            features.setValue(name, value);
         }
         catch(IllegalArgumentException e)
         {
            // Feature name is not recognized, so throw an exception.
             Object[] args = new Object[] {name};
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL006", args));
         }
     }

     /**
      * Returns the on/off setting of the named feature, represented as a boolean.
      *
      * @param name the name of the feature to get the value of
      * @return a boolean representing the on/off state of the named feature
      * @throws IllegalArgumentException if the feature name is not recognized.
      */
     public boolean getFeature(String name)
     {
         if(name == null)
         {
             //name must not be null
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL005", null));
         }

         try
         {
            return features.getValue(name);
         }
         catch(IllegalArgumentException e)
         {
            // Feature name is not recognized, so throw an exception.
             Object[] args = new Object[] {name};
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL006", args));
         }
     }

     /**
      * Set a named property to the specified object.
      * <p>
      * All property names should be fully-qualified, Java package style to
      * avoid name clashes. All names starting with org.apache.woden. are
      * reserved for properties defined by the Woden implementation.
      * Properties specific to other implementations should be fully-qualified
      * to match the package name structure of that implementation.
      * For example: com.abc.propertyName
      *
      * @param name the name of the property to be set
      * @param value an Object representing the value to set the property to
      * @throws IllegalArgumentException if the property name is not recognized.
      */

     public void setProperty(String name, Object value)
     {
         if(name == null)
         {
             //name must not be null
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL007", null));
         }
         else if(name.equals("xyz"))
         {
             //TODO determine the required properties and
             //create an if block for each one to set the value.
         }
         else
         {
             //property name is not recognized, so throw an exception
             Object[] args = new Object[] {name};
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL008", args));
         }
     }

     /**
      * Returns the value of the named property.
      *
      * @param name the name of the property to get the value of
      * @return an Object representing the property's value
      * @throws IllegalArgumentException if the property name is not recognized.
      */
     public Object getProperty(String name)
     {
         if(name == null)
         {
             //name must not be null
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL007", null));
         }

         //Return the property's value or throw an exception if the property
         //name is not recognized

         if(name.equals("xyz"))
         {
             //TODO determine the required properties and
             //create an if block for each one to get the value.
             return null;
         }
         else
         {
             //property name is not recognized, so throw an exception
             Object[] args = new Object[] {name};
             throw new IllegalArgumentException(
                     fWsdlContext.errorReporter.getFormattedMessage("WSDL008", args));
         }
     }
}
