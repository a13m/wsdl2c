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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.wsdl20.extensions.ElementExtensible;
import org.apache.woden.wsdl20.extensions.ExtensionElement;

/**
 * Common code for handling extension elements. 
 * Can be reused by inheritance or by delegation.
 * 
 * @author jkaputin@ws.apache.org
 */
public class ElementExtensibleImpl implements ElementExtensible 
{
    private List fExtElements = new Vector();

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#addExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void addExtensionElement(ExtensionElement extEl) 
    {
        if(extEl != null) {
            fExtElements.add(extEl);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#removeExtensionElement(org.apache.woden.wsdl20.extensions.ExtensionElement)
     */
    public void removeExtensionElement(ExtensionElement extEl) 
    {
        fExtElements.remove(extEl);
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElements()
     */
    public ExtensionElement[] getExtensionElements() 
    {
        ExtensionElement[] array = new ExtensionElement[fExtElements.size()];
        fExtElements.toArray(array);
        return array;
    }

    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#getExtensionElementsOfType(javax.xml.namespace.QName)
     */
    public ExtensionElement[] getExtensionElementsOfType(QName extType) 
    {
        List elements = new Vector();
        
        if(extType != null)
        {
            for(Iterator i=fExtElements.iterator(); i.hasNext();)
            {
                ExtensionElement extElem = (ExtensionElement)i.next();
                if(extElem.getExtensionType().equals(extType))
                {
                    elements.add(extElem);
                }
            }
        }
        
        ExtensionElement[] array = new ExtensionElement[elements.size()];
        elements.toArray(array);
        return array;
    }
    
    /* (non-Javadoc)
     * @see org.apache.woden.wsdl20.extensions.ElementExtensible#hasExtensionElementsForNamespace(javax.net.URI)
     */
    public boolean hasExtensionElementsForNamespace(URI namespace)
    {
        boolean result = false;
        if(namespace != null)
        {
            String extensionNS = namespace.toString();
            for(Iterator i=fExtElements.iterator(); i.hasNext();)
            {
                ExtensionElement extElem = (ExtensionElement)i.next();
                if(extensionNS.equals(extElem.getExtensionType().getNamespaceURI()))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


}
