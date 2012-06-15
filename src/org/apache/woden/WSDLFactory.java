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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.woden.wsdl20.extensions.ExtensionRegistry;
import org.apache.woden.wsdl20.xml.DescriptionElement;


/**
 * 
 * Based on wsdl4j factory.
 * 
 * @author jkaputin@apache.org
 */
public abstract class WSDLFactory {


    private static final String PROPERTY_NAME =
        "org.apache.woden.WSDLFactory";
    private static final String PROPERTY_FILE_NAME =
        "wsdl.properties";
    private static final String DEFAULT_FACTORY_IMPL_NAME =
        "org.apache.woden.internal.DOMWSDLFactory";

    private static String fFullPropertyFileName = null;
    

    public static WSDLFactory newInstance() throws WSDLException {
        
        String factoryImplName = findFactoryImplName();
        return newInstance(factoryImplName);
    }
    
    public static WSDLFactory newInstance(String factoryImplName) 
    throws WSDLException {
        
        if (factoryImplName != null)
        {
            try
            {
                Class cl = Class.forName(factoryImplName);
                
                return (WSDLFactory)cl.newInstance();
            }
            catch (Exception e)
            {
                /*
                 Catches and wraps:
                 ClassNotFoundException
                 InstantiationException
                 IllegalAccessException
                 */
                
                throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                        "Problem instantiating a WSDLFactory implementation " +
                        "using factory impl name '" + factoryImplName + "' ",
                        e);
            }
        }
        else
        {
            throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                    "Unable to find the name of a WSDLFactory implementation class.");
        }
    }
    
    private static String findFactoryImplName() throws WSDLException
    {
        String factoryImplName = null;

        // First, check the system property.
        try
        {
          factoryImplName = System.getProperty(PROPERTY_NAME);

          if (factoryImplName != null)
          {
            return factoryImplName;
          }
        }
        catch (SecurityException e)
        {
            //TODO empty catch block copied from wsdl4j. Decide if OK to ignore?
        }

        // Second, check /META-INF/services
        //TODO put code here to check for factory impl name property in /META...
        
        // Third, check for a properties file on the system path.
        String propFileName = getFullPropertyFileName();

        if (propFileName != null)
        {
          try
          {
            Properties properties = new Properties();
            File propFile = new File(propFileName);
            FileInputStream fis = new FileInputStream(propFile);

            properties.load(fis);
            fis.close();

            factoryImplName = properties.getProperty(PROPERTY_NAME);

            if (factoryImplName != null)
            {
              return factoryImplName;
            }
          } 
          catch (FileNotFoundException e1) 
          {
            // This just means no properties file was found. OK to ignore.
          }
          catch (IOException e)
          {
              /*
               * thrown by Properties.load if an error occurred when reading 
               * from the input stream.
               */
              throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                      "Problem loading the properties file '" +
                      propFileName + "' ",
                      e);
          }
          catch (IllegalArgumentException e)
          {
              /*
               * thrown by Properties.load if the input stream contains a 
               * malformed Unicode escape sequence. 
               */
              throw new WSDLException(WSDLException.CONFIGURATION_ERROR,
                      "Problem with the content of the properties file '" +
                      propFileName + "' ",
                      e);
          }
        }

        // Fourth, return the default.
        return DEFAULT_FACTORY_IMPL_NAME;
        
    }
    
    private static String getFullPropertyFileName()
    {
        if (fFullPropertyFileName == null)
        {
            try
            {
                String javaHome = System.getProperty("java.home");
                
                fFullPropertyFileName = javaHome + File.separator + "lib" +
                File.separator + PROPERTY_FILE_NAME;
            }
            catch (SecurityException e)
            {
                //TODO empty catch block copied from wsdl4j. Decide if OK to ignore?
            }
        }
        
        return fFullPropertyFileName;
    }
    public abstract WSDLWriter newWSDLWriter() throws WSDLException;
    
    public abstract WSDLReader newWSDLReader() throws WSDLException;
    
    public abstract DescriptionElement newDescription();
    
    public abstract ExtensionRegistry newPopulatedExtensionRegistry() throws WSDLException;
    

}
