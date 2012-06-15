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
package org.apache.woden.internal.schema;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;

/**
 * Constants for XML Schema elements, attributes and URIs.
 * 
 * @author jkaputin@apache.org
 */
public class SchemaConstants {

    //Schema attribute names
    public static final String ATTR_ID = "id";
    public static final String ATTR_TARGET_NAMESPACE = "targetNamespace";
    public static final String ATTR_NAMESPACE = "namespace";
    public static final String ATTR_SCHEMA_LOCATION = "schemaLocation";
    
    //Schema element names
    public static final String ELEM_SCHEMA = "schema";
    public static final String ELEM_IMPORT = "import";

    //Schema namespace string
    public static final String NS_STRING_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    
    //Schema namespace uri
    public static final URI NS_URI_SCHEMA = URI.create(NS_STRING_SCHEMA);
    
    //Schema attribute qnames
    public static final QName Q_ATTR_ID = new QName(ATTR_ID);
    
    //Schema element qnames
    public static final QName Q_ELEM_SCHEMA =
        new QName(NS_STRING_SCHEMA, ELEM_SCHEMA);
    public static final QName Q_ELEM_SCHEMA_IMPORT = 
        new QName(NS_STRING_SCHEMA, ELEM_IMPORT);

    //Built-in XML Schema types. 19 primitive and 25 derived.
    public static final List LIST_Q_BUILT_IN_TYPES = Arrays.asList(new QName[]
        { new QName(NS_STRING_SCHEMA, "string"),
          new QName(NS_STRING_SCHEMA, "boolean"),
          new QName(NS_STRING_SCHEMA, "decimal"),
          new QName(NS_STRING_SCHEMA, "float"),
          new QName(NS_STRING_SCHEMA, "double"),
          new QName(NS_STRING_SCHEMA, "duration"),
          new QName(NS_STRING_SCHEMA, "dateTime"),
          new QName(NS_STRING_SCHEMA, "time"),
          new QName(NS_STRING_SCHEMA, "date"),
          new QName(NS_STRING_SCHEMA, "gYearMonth"),
          new QName(NS_STRING_SCHEMA, "gYear"),
          new QName(NS_STRING_SCHEMA, "gMonthDay"),
          new QName(NS_STRING_SCHEMA, "gDay"),
          new QName(NS_STRING_SCHEMA, "gMonth"),
          new QName(NS_STRING_SCHEMA, "hexBinary"),
          new QName(NS_STRING_SCHEMA, "base64Binary"),
          new QName(NS_STRING_SCHEMA, "anyURI"),
          new QName(NS_STRING_SCHEMA, "QName"),
          new QName(NS_STRING_SCHEMA, "NOTATION"),
          new QName(NS_STRING_SCHEMA, "normalizedString"),
          new QName(NS_STRING_SCHEMA, "token"),
          new QName(NS_STRING_SCHEMA, "language"),
          new QName(NS_STRING_SCHEMA, "NMTOKEN"),
          new QName(NS_STRING_SCHEMA, "NMTOKENS"),
          new QName(NS_STRING_SCHEMA, "Name"),
          new QName(NS_STRING_SCHEMA, "NCName"),
          new QName(NS_STRING_SCHEMA, "ID"),
          new QName(NS_STRING_SCHEMA, "IDREF"),
          new QName(NS_STRING_SCHEMA, "IDREFS"),
          new QName(NS_STRING_SCHEMA, "ENTITY"),
          new QName(NS_STRING_SCHEMA, "ENTITIES"),
          new QName(NS_STRING_SCHEMA, "integer"),
          new QName(NS_STRING_SCHEMA, "nonPositiveInteger"),
          new QName(NS_STRING_SCHEMA, "negativeInteger"),
          new QName(NS_STRING_SCHEMA, "long"),
          new QName(NS_STRING_SCHEMA, "int"),
          new QName(NS_STRING_SCHEMA, "short"),
          new QName(NS_STRING_SCHEMA, "byte"),
          new QName(NS_STRING_SCHEMA, "nonNegativeInteger"),
          new QName(NS_STRING_SCHEMA, "unsignedLong"),
          new QName(NS_STRING_SCHEMA, "unsignedInt"),
          new QName(NS_STRING_SCHEMA, "unsignedShort"),
          new QName(NS_STRING_SCHEMA, "unsignedByte"),
          new QName(NS_STRING_SCHEMA, "positiveInteger") });
}
