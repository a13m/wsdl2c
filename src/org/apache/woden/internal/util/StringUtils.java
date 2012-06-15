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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class originated from WSDL4J.
 * 
 * @author   Matthew J. Duftler
 * @author jkaputin@apache.org (Woden changes)
 */
public class StringUtils {

    public static final String lineSeparator =
        System.getProperty("line.separator", "\n");
      public static final String lineSeparatorStr = cleanString(lineSeparator);

      // Ensure that escape sequences are passed through properly.
      public static String cleanString(String str)
      {
        if (str == null)
          return null;
        else
        {
          char[]       charArray = str.toCharArray();
          StringBuffer sBuf      = new StringBuffer();
          
          for (int i = 0; i < charArray.length; i++)
            switch (charArray[i])
            {
              case '\"' : sBuf.append("\\\"");
                          break;
              case '\\' : sBuf.append("\\\\");
                          break;
              case '\n' : sBuf.append("\\n");
                          break;
              case '\r' : sBuf.append("\\r");
                          break;
              default   : sBuf.append(charArray[i]);
                          break;
            }
          
          return sBuf.toString();
        }
      }

      /*
        This method will return the correct name for a class object representing
        a primitive, a single instance of a class, as well as n-dimensional arrays
        of primitives or instances. This logic is needed to handle the string returned
        from Class.getName(). If the class object represents a single instance (or
        a primitive), Class.getName() returns the fully-qualified name of the class
        and no further work is needed. However, if the class object represents an
        array (of n dimensions), Class.getName() returns a Descriptor (the Descriptor
        grammar is defined in section 4.3 of the Java VM Spec). This method will
        parse the Descriptor if necessary.
      */
      public static String getClassName(Class targetClass)
      {
        String className = targetClass.getName();

        return targetClass.isArray() ? parseDescriptor(className) : className;
      }

      /*
        See the comment above for getClassName(targetClass)...
      */
      private static String parseDescriptor(String className)
      {
        char[] classNameChars = className.toCharArray();
        int    arrayDim       = 0;
        int    i              = 0;

        while (classNameChars[i] == '[')
        {
          arrayDim++;
          i++;
        }

        StringBuffer classNameBuf = new StringBuffer();

        switch (classNameChars[i++])
        {
          case 'B' : classNameBuf.append("byte");
                     break;
          case 'C' : classNameBuf.append("char");
                     break;
          case 'D' : classNameBuf.append("double");
                     break;
          case 'F' : classNameBuf.append("float");
                     break;
          case 'I' : classNameBuf.append("int");
                     break;
          case 'J' : classNameBuf.append("long");
                     break;
          case 'S' : classNameBuf.append("short");
                     break;
          case 'Z' : classNameBuf.append("boolean");
                     break;
          case 'L' : classNameBuf.append(classNameChars,
                                         i, classNameChars.length - i - 1);
                     break;
        }

        for (i = 0; i < arrayDim; i++)
          classNameBuf.append("[]");

        return classNameBuf.toString();
      }

    /*
     * Return a URL created from a context URL and a relative URI.
     * If a valid URL cannot be created the only other possibility
     * this method will consider is that an absolute file path has 
     * been passed in as the relative URI argument, and it will try
     * to create a 'file' URL from it.
     *
     * @param contextURL the document base URL
     * @param fileSpec a file URI relative to the contextURL or an
     * absolute file path
     * @return the URL created from contextURL and fileSpec
     */
    public static URL getURL(URL contextURL, String fileSpec)
      throws MalformedURLException {

        URL url = null;
        
        try {
            url = new URL(contextURL, fileSpec);
        }
        catch (MalformedURLException e) {
            
            File file = new File(fileSpec);
            if (file.isAbsolute()) {
                url = file.toURL();
            }
            else {
                throw e;
            }
        }
        
        return url;
    }
    
    public static InputStream getContentAsInputStream(URL url)
      throws IllegalArgumentException, IOException {
        
        if (url == null)
        {
            //TODO externalize the message
            throw new IllegalArgumentException("URL cannot be null.");
        }
        
        //TODO consider exception handling used in wsdl4j
        Object content = url.getContent();
        
        if (content == null)
        {
            //TODO externalize the message
            throw new IllegalArgumentException("No content.");
        }
        
        if (content instanceof InputStream)
        {
            return (InputStream)content;
        }
        else
        {
            //TODO externalize the message
            throw new IllegalArgumentException((content instanceof String)
                    ? (String)content
                            : "This URL points to a: " +
                            StringUtils.getClassName(content.getClass()));
        }
    }
    
    public static List parseNMTokens(String nmTokens)
    {
      return parseNMTokens(nmTokens, " ");
    }

    public static List parseNMTokens(String nmTokens, String delimiter)
    {
      StringTokenizer strTok = new StringTokenizer(nmTokens, delimiter);
      List tokens = new Vector();

      while (strTok.hasMoreTokens())
      {
        tokens.add(strTok.nextToken());
      }

      return tokens;
    }

    public static String getNMTokens(List list)
    {
      if (list != null)
      {
        StringBuffer strBuf = new StringBuffer();
        int size = list.size();

        for (int i = 0; i < size; i++)
        {
          String token = (String)list.get(i);

          strBuf.append((i > 0 ? " " : "") + token);
        }

        return strBuf.toString();
      }
      else
      {
        return null;
      }
    }
}
