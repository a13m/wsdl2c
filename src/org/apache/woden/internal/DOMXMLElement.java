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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.XMLElement;
import org.apache.woden.internal.util.dom.DOMQNameUtils;
import org.apache.woden.internal.util.dom.XPathUtils;
import org.apache.woden.internal.wsdl20.Constants;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMXMLElement extends BaseXMLElement{

    private static final String emptyString = "".intern();
    
    public DOMXMLElement(ErrorReporter errorReporter) {
        super(errorReporter);
    }

    /*
     * @see org.apache.woden.XMLElement#setSource(java.lang.Object)
     */
    public void setSource(Object elem) {

        if(elem instanceof Element) {
            fSource = elem;
        }
        else {
            String elemClass = (elem != null 
                                    ? elem.getClass().getName()
                                    : null);
            String xmlElementClass = this.getClass().getName();
            String msg = fErrorReporter.getFormattedMessage(
                    "WSDL019", new Object[] {elemClass, xmlElementClass});
            throw new IllegalArgumentException(msg);
        }

    }

    /*TODO complete this method if it's added to XMLElement.
     * 
    public XMLAttribute[] getAttributes() {

        String nodename, prefix;
        Element el = (Element)fSource;
        List attrs = new Vector();
        NamedNodeMap attrMap = el.getAttributes();
        for(int i = 0; i < attrMap.getLength(); i++){
            nodename = attrMap.item(i).getNodeName();
            prefix = attrMap.item(i).getPrefix();
            if ( !(Constants.ATTR_XMLNS.equals(nodename) || 
                    Constants.ATTR_XMLNS.equals(prefix)) ) {
                //TODO create an XMLAttribute from attrMap.item(i)
                //attrs.add(xmlAttribute);
            }
        }

        XMLElement[] array = new XMLElement[attrs.size()];
        attrs.toArray(array);
        return array;
    }
    */


    protected String doGetAttributeValue(String attrName) {
    	
    	Element el = (Element)fSource;
    	return getAttribute(el, attrName);
    }

    protected URI doGetNamespaceURI() throws WSDLException {

    	Element el = (Element)fSource;
    	String nsStr =  el.getNamespaceURI();
    	URI uri = null;
    	
    	if (nsStr != null) {
            try {
                uri = new URI(nsStr);
            } catch (URISyntaxException e) {
                String msg = fErrorReporter.getFormattedMessage("WSDL506", new Object[] { nsStr });
                throw new WSDLException(WSDLException.INVALID_WSDL, msg, e);
            }
        }
        return uri;
    }

    protected String doGetLocalName() {

    	Element el = (Element)fSource;
    	return el.getLocalName();
    }
    
    protected QName doGetQName() {
    	
    	Element el = (Element)fSource;
    	return new QName(el.getNamespaceURI(), el.getLocalName());
    }

    protected QName doGetQName(String prefixedValue) throws WSDLException {

    	Element el = (Element)fSource;
    	int    index        = prefixedValue.indexOf(':');
    	String prefix       = (index != -1) 
    	                      ? prefixedValue.substring(0, index)
    			              : null;
    	String localPart    = prefixedValue.substring(index + 1);
    	String namespaceURI = getNamespaceFromPrefix(el, prefix);

        if(prefix != null && namespaceURI == null) {
            String faultCode = WSDLException.UNBOUND_PREFIX;
            String msg = fErrorReporter.getFormattedMessage(
                    "WSDL513", 
                    new Object[] {prefixedValue, DOMQNameUtils.newQName(el)});
            WSDLException wsdlExc = new WSDLException(
                    faultCode,
                    msg);
            wsdlExc.setLocation(XPathUtils.getXPathExprFromNode(el));
            throw wsdlExc;
        }
        
        return new QName(namespaceURI, localPart, (prefix != null ? prefix : emptyString));
    }

    protected XMLElement doGetFirstChildElement() {
    	
        XMLElement xmlElement = new DOMXMLElement(fErrorReporter);
        Element el = (Element)fSource;
        for (Node node = el.getFirstChild(); node!=null; node=node.getNextSibling()){
        	if (node.getNodeType() == Node.ELEMENT_NODE){
        		xmlElement.setSource(node);
        		return xmlElement;
        	}
        }
        return null;  //no child element found
    }

    protected XMLElement doGetNextSiblingElement() {
    	
        XMLElement xmlElement = new DOMXMLElement(fErrorReporter);
        Element el = (Element)fSource;
        for (Node node = el.getNextSibling (); node != null; node = node.getNextSibling ()) {
        	if (node.getNodeType() == Node.ELEMENT_NODE){
        		xmlElement.setSource(node);
        		return xmlElement;
        	}
        }
        return null;  //no sibling element found
    }
    
    protected XMLElement[] doGetChildElements() {
        
        List children = new Vector();
        XMLElement temp = doGetFirstChildElement();
        while(temp != null)
        {
            children.add(temp);
            temp = temp.getNextSiblingElement();
        }
        XMLElement[] array = new XMLElement[children.size()];
        children.toArray(array);
        return array;
    }
    
    /* ************************************************************************
     * Private helper methods
     * ************************************************************************/
    
    private String getAttribute(Element el, String attrName) {
    	
    	String sRet = null;
    	Attr   attr = el.getAttributeNode(attrName);
    	
    	if (attr != null) {
    		sRet = attr.getValue();
    	}
    	return sRet;
    }

    private String getAttributeNS (Element el,
    		                       String namespaceURI,
    		                       String localPart) {
    	String sRet = null;
    	Attr   attr = el.getAttributeNodeNS (namespaceURI, localPart);

    	if (attr != null) {
    		sRet = attr.getValue ();
    	}

    	return sRet;
    }
    
    private String getNamespaceFromPrefix(Node context, String prefix) {

    	short nodeType = context.getNodeType ();
    	Node tempNode = null;

    	switch (nodeType)
    	{
	    	case Node.ATTRIBUTE_NODE :
	    	{
	    		tempNode = ((Attr) context).getOwnerElement ();
	    		break;
	    	}
	    	case Node.ELEMENT_NODE :
	    	{
	    		tempNode = context;
	    		break;
	    	}
	    	default :
	    	{
	    		tempNode = context.getParentNode ();
	    		break;
	    	}
    	}

    	while (tempNode != null && tempNode.getNodeType () == Node.ELEMENT_NODE)
    	{
    		Element tempEl = (Element) tempNode;

    		String namespaceURI = (prefix == null)
    		        ? getAttribute (tempEl, Constants.ATTR_XMLNS)
    				: getAttributeNS (tempEl, Constants.NS_STRING_XMLNS, prefix);

    		if (namespaceURI != null)
    		{
    			return namespaceURI;
    		} 
    		else 
    		{
    			tempNode = tempEl.getParentNode ();
    		}
    	}

    	return null; //no namespace found for specified prefix
    }
}
