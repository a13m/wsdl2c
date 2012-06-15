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

import org.apache.woden.schema.ImportedSchema;

/**
 * This interface represents a schema import, &lt;xs:import&gt;. It extends the abstract
 * class SchemaImpl, adding support for the <code>schemaLocation</code> attribute.
 * 
 * @author jkaputin@apache.org
 */
public class ImportedSchemaImpl extends SchemaImpl implements ImportedSchema 
{
    private URI fSchemaLocation = null;

    /* (non-Javadoc)
     * @see org.apache.woden.schema.ImportedSchema#setSchemaLocation(java.net.URI)
     */
    public void setSchemaLocation(URI location) {
        fSchemaLocation = location;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.schema.ImportedSchema#getSchemaLocation()
     */
    public URI getSchemaLocation() {
        return fSchemaLocation;
    }
}
