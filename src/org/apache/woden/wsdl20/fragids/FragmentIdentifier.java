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

package org.apache.woden.wsdl20.fragids;

import javax.xml.namespace.QName;

import org.apache.woden.types.NCName;
import org.apache.woden.wsdl20.fragids.ComponentPart;
import org.apache.woden.xpointer.XPointer;
import org.apache.woden.xpointer.XmlnsPointerPart;

/**
 * This Class extends the XPointer class to work with WSDL fragment identifiers which are WSDL XPointer in effect.
 * 
 * @author Dan Harvey (danharvey42@gmail.com)
 * 
 * TODO Add methods to find WSDL component from the FragmentIdentifier.
 *      Add deserialisation code in a constructor with a String argument. (Pass this onto XPointer to do the work?)
 *
 */

public class FragmentIdentifier {
    private static XPointer xpointer;
    
    /**
     * Constructs a new empty Fragment Identifier
     * 
     * @param wsdlPart The WSDL2.0 component pointer part for this fragment identifier.
     * 
     */
    public FragmentIdentifier(ComponentPart wsdlPart) {
        xpointer = new XPointer();
        wsdlPart = wsdlPart.prefixNamespaces(this); //Prefix namespaces if needed.
        xpointer.addPointerPart(wsdlPart);
    }

    /**
     * Returns a String serialisation of this fragment identifier.
     * 
     * @return a String fragment identifier
     */
    public String toString() {
        return xpointer.toString();
    }
    
    /** Namespace management code **/
    /**
     * Returns the prefix for the Xml namespace of the QName in the XPointer.
     * If the namespace does not have a prefix in the XPointer it will create a new prefix
     * with the prefix from the QName or one of the form nsXX and add a xmlns Pointer Part, then return that.
     * 
     * @param qname The QName containing the namespace and a prefix.
     * @return a NCName of the prefix for the namespace.
     */
    public NCName getXmlNamespacePrefix(QName qname) {
        return getXmlNamespacePrefix(qname.getNamespaceURI());
    }

    /**
     * Returns the prefix for the Xml namespace in the XPointer.
     * If the namespace does not have a prefix in the XPointer it will create a new prefix
     * of the form nsXX and add a xmlns Pointer Part, then return that.
     * 
     * @param namespace The namespace to get the prefix for.
     * @return a NCName of the prefix for the namespace.
     */
    public NCName getXmlNamespacePrefix(String namespace) {
        //Lookup prefix
        NCName prefix = (NCName)xpointer.getNamespaceBinding(namespace);
        if (prefix == null) {
            //The namespace does not have a prefix yet so lets add one.
            //Find next available nsXXX prefix
            int i = 1;
            do {
                prefix = new NCName("ns" + i);
                i++;
            } while (xpointer.hasPrefixBinding(prefix));
            
            //Add prefix pointer part.
            xpointer.addPointerPart(new XmlnsPointerPart(prefix, namespace));
            
            //Add to our binding contex. 
            xpointer.addPrefixNamespaceBinding(prefix, namespace);
        }
        return prefix;
    }
    
    /**
     * Returns a QName prefixed from the map of local namespaces and prefixes.
     * The namespace and localpart remain unchanged.
     * 
     * @param qname the QName used to lookup the namespace and copy.
     * @return a QName with the new prefix, but same namespace and localpart.
     */
    public QName prefixQNameNamespace(QName qname) {
        //Get prefix for the fault QName in the XPointer.
        NCName prefix = getXmlNamespacePrefix(qname);
        return new QName(qname.getNamespaceURI(), qname.getLocalPart(), prefix.toString());
    }
}
