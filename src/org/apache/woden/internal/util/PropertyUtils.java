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
package org.apache.woden.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class defines the property lookup mechanism for the three supported 
 * alternatives for specifying configuration properties. 
 * 
 * These alternatives, listed in search order, are:
 * 1. JVM system properties (e.g. java -D arguments)
 * 2. application properties defined in /META-INF/services (e.g. in a jar file)
 * 3. properties defined in a wsdl properties file located in JAVAHOME/lib directory
 * 
 * @author jkaputin@apache.org
 */
public class PropertyUtils {
    
    private static final String PROPERTY_FILE_NAME = "wsdl.properties";
    
    private static String fFullPropertyFileName = null;
    
    private static Properties fProperties;
    
    public static String findProperty(String propertyName) {
        
        String propertyValue = null;

        //First, check for a JVM-wide system property.
        
        try
        {
          propertyValue = System.getProperty(propertyName);

          if (propertyValue != null)
          {
            return propertyValue;
          }
        }
        catch (SecurityException e)
        {
            //TODO empty catch block copied from wsdl4j. Is this necessary?
            //Was possibly used here to ignore exc for applet invocation?
        }

        // Second, check for an application-specific /META-INF/services property
        
        //TODO put code here to check for factory impl name property in /META...
        
        // Third, check the system properties file.
        
        if (fProperties == null) {
            
            fProperties = new Properties();
            
            String propFileName = getFullPropertyFileName();

            if (propFileName != null)
            {
                try
                {
                    File propFile = new File(propFileName);
                    FileInputStream fis = new FileInputStream(propFile);

                    fProperties.load(fis);
                    fis.close();
                }
                catch (IOException e)
                {
                    //TODO empty catch block copied from wsdl4j. Necessary?
                }
            }
        }
        
        propertyValue = fProperties.getProperty(propertyName);

        if (propertyValue != null)
        {
            return propertyValue;
        }

        //Return null if we can't find the property
        return null;
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
                //TODO empty catch block copied from wsdl4j. Is this necessary?
                //Was possibly used here to ignore exc for applet invocation?
            }
        }
        
        return fFullPropertyFileName;
    }
    

}
