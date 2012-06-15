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
package org.apache.woden.internal.wsdl20.extensions;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.extensions.AttributeExtensible;
import org.apache.woden.xml.XMLAttr;

/**
 * Common code for handling extension attributes. 
 * Can be reused by inheritance or by delegation.
 * 
 * @author jkaputin@ws.apache.org
 */
public class AttributeExtensibleImpl implements AttributeExtensible 
{
    private Map fExtAttributes = new HashMap();

    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#setExtensionAttribute(javax.xml.namespace.QName, org.apache.woden.xml.XMLAttr)
     */
    public void setExtensionAttribute(QName attrType, XMLAttr attr) 
    {
        if(attrType != null)  //TODO throw IllegArgExc if it is null?
        {
            if(attr != null) {
                fExtAttributes.put(attrType, attr);
            } else {
                fExtAttributes.remove(attrType);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttribute(javax.xml.namespace.QName)
     */
    public XMLAttr getExtensionAttribute(QName attrType) 
    {
        //TODO throw IllegArgExc if it is null?
        if(attrType != null) {
            return (XMLAttr)fExtAttributes.get(attrType);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributesForNamespace(java.net.URI)
     */
    public XMLAttr[] getExtensionAttributesForNamespace(URI namespace) 
    {
        if(namespace != null)
        {
            String extensionNS = namespace.toString();
            List list = new Vector();
            Collection coll = fExtAttributes.keySet();
            for(Iterator i = coll.iterator(); i.hasNext();)
            {
                QName qn = (QName)i.next();
                if(qn.getNamespaceURI().equals(extensionNS))
                {
                    list.add(fExtAttributes.get(qn));
                }
            }
            XMLAttr[] array = new XMLAttr[list.size()];
            list.toArray(array);
            return array;
        }
        else
        {
            return new XMLAttr[0];
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#getExtensionAttributes()
     */
    public XMLAttr[] getExtensionAttributes() 
    {
        Collection coll = fExtAttributes.values();
        XMLAttr[] array = new XMLAttr[coll.size()];
        coll.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.AttributeExtensible#hasExtensionAttributesForNamespace(javax.net.URI)
     */
    public boolean hasExtensionAttributesForNamespace(URI namespace)
    {
        boolean result = false;
        if(namespace != null)
        {
            String extensionNS = namespace.toString();
            Collection coll = fExtAttributes.keySet();
            for(Iterator i = coll.iterator(); i.hasNext();)
            {
                QName qn = (QName)i.next();
                if(extensionNS.equals(qn.getNamespaceURI()))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
}
