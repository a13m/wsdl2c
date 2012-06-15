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
package org.apache.woden.internal.util.dom;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.woden.WSDLException;
import org.apache.woden.wsdl20.xml.WSDLElement;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class originated from WSDL4J.
 * 
 * @author jkaputin@apache.org (Woden changes)
 */
public class DOMUtils {
  /**
   * The namespaceURI represented by the prefix <code>xmlns</code>.
   */
  private static String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";

  private static final String ATTR_XMLNS = "xmlns";
  private static final String emptyString = "";
  
  /**
   * Returns a list of attributes of an element. Returns an 
   * empty list if the element has no attributes. Does not
   * include namespace declarations.
   *
   * @param el       Element whose attributes are returned
   * @return the List of Attr
   */
  static public List getAttributes (Element el) {
    String nodename, prefix = null;
    List attrs = new Vector();
    NamedNodeMap attrMap = el.getAttributes();
    for(int i = 0; i < attrMap.getLength(); i++)
    {
      nodename = attrMap.item(i).getNodeName();
      prefix = attrMap.item(i).getPrefix();
      
      if (ATTR_XMLNS.equals(nodename) || ATTR_XMLNS.equals(prefix))
      {
        //ignore namespace declarations
        continue;
      }
      else
      {
        attrs.add(attrMap.item(i));  
      }
    }
      
    return attrs;
  }

  /**
   * Returns the value of an attribute of an element. Returns null
   * if the attribute is not found (whereas Element.getAttribute
   * returns "" if an attrib is not found). This method should be
   * used for elements that support extension attributes because it
   * does not track unexpected attributes.
   *
   * @param el       Element whose attrib is looked for
   * @param attrName name of attribute to look for
   * @return the attribute value including prefix if present
   */
  static public String getAttribute (Element el, String attrName) {
    String sRet = null;
    Attr   attr = el.getAttributeNode(attrName);

    if (attr != null) {
      sRet = attr.getValue();
    }
    return sRet;
  }

  /**
   * Returns the value of an attribute of an element. Returns null
   * if the attribute is not found (whereas Element.getAttribute
   * returns "" if an attrib is not found). This method should be
   * used for elements that do not support extension attributes
   * because it tracks the element's remaining attributes so that
   * eventually any unexpected attributes can be identified.
   *
   * @param el       Element whose attrib is looked for
   * @param attrName name of attribute to look for
   * @param remainingAttrs List of remaining attributes 
   * @return the attribute value
   */
  static public String getAttribute (Element el, String attrName, List remainingAttrs) {
    String sRet = null;
    Attr   attr = el.getAttributeNode(attrName);
    
    if (attr != null) {
      sRet = attr.getValue();
      remainingAttrs.remove(attr);
    }
    return sRet;
  }

  /**
   * Returns the value of an attribute of an element. Returns null
   * if the attribute is not found (whereas Element.getAttributeNS
   * returns "" if an attrib is not found).
   *
   * @param el       Element whose attrib is looked for
   * @param namespaceURI namespace URI of attribute to look for
   * @param localPart local part of attribute to look for
   * @return the attribute value
   */
  static public String getAttributeNS (Element el,
                                       String namespaceURI,
                                       String localPart) {
    String sRet = null;
    Attr   attr = el.getAttributeNodeNS (namespaceURI, localPart);

    if (attr != null) {
      sRet = attr.getValue ();
    }

    return sRet;
  }

  /**
   * Concat all the text and cdata node children of this elem and return
   * the resulting text.
   *
   * @param parentEl the element whose cdata/text node values are to
   *                 be combined.
   * @return the concatanated string.
   */
  static public String getChildCharacterData (Element parentEl) {
    if (parentEl == null) {
      return null;
    }
    Node          tempNode = parentEl.getFirstChild();
    StringBuffer  strBuf   = new StringBuffer();
    CharacterData charData;

    while (tempNode != null) {
      switch (tempNode.getNodeType()) {
        case Node.TEXT_NODE :
        case Node.CDATA_SECTION_NODE : charData = (CharacterData)tempNode;
                                       strBuf.append(charData.getData());
                                       break;
      }
      tempNode = tempNode.getNextSibling();
    }
    return strBuf.toString();
  }

  /**
   * Return the first child element of the given element. Null if no
   * children are found.
   *
   * @param elem Element whose child is to be returned
   * @return the first child element.
   */
  public static Element getFirstChildElement (Element elem) {
    for (Node n = elem.getFirstChild (); n != null; n = n.getNextSibling ()) {
      if (n.getNodeType () == Node.ELEMENT_NODE) {
        return (Element) n;
      }
    }
    return null;
  }

  /**
   * Return the next sibling element of the given element. Null if no
   * more sibling elements are found.
   *
   * @param elem Element whose sibling element is to be returned
   * @return the next sibling element.
   */
  public static Element getNextSiblingElement (Element elem) {
    for (Node n = elem.getNextSibling (); n != null; n = n.getNextSibling ()) {
      if (n.getNodeType () == Node.ELEMENT_NODE) {
        return (Element) n;
      }
    }
    return null;
  }

  /**
   * Return the first child element of the given element which has the
   * given attribute with the given value.
   *
   * @param elem      the element whose children are to be searched
   * @param attrName  the attrib that must be present
   * @param attrValue the desired value of the attribute
   *
   * @return the first matching child element.
   */
  public static Element findChildElementWithAttribute (Element elem,
                   String attrName,
                   String attrValue) {
    for (Node n = elem.getFirstChild (); n != null; n = n.getNextSibling ()) {
      if (n.getNodeType () == Node.ELEMENT_NODE) {
        if (attrValue.equals (DOMUtils.getAttribute ((Element) n, attrName))) {
          return (Element) n;
        }
      }
    }
    return  null;
  }

  /**
   * Count number of children of a certain type of the given element.
   *
   * @param elem the element whose kids are to be counted
   *
   * @return the number of matching kids.
   */
  public static int countKids (Element elem, short nodeType) {
    int nkids = 0;
    for (Node n = elem.getFirstChild (); n != null; n = n.getNextSibling ()) {
      if (n.getNodeType () == nodeType) {
        nkids++;
      }
    }
    return nkids;
  }

  public static void throwWSDLException(Element location) throws WSDLException
  {
    String elName = DOMQNameUtils.newQName(location).toString();

    WSDLException wsdlExc = new WSDLException(WSDLException.INVALID_WSDL,
                                              "Encountered unexpected element '" +
                                              elName + "'.");

    wsdlExc.setLocation(XPathUtils.getXPathExprFromNode(location));

    throw wsdlExc;
  }

  public static void throwWSDLException(Element location, List remainingAttrs) throws WSDLException
  {
    String elName = DOMQNameUtils.newQName(location).toString();
    
    StringBuffer sb = new StringBuffer();
    ListIterator i = remainingAttrs.listIterator();
    while (i.hasNext())
    {
      String attrName = DOMQNameUtils.newQName((Attr)i.next()).toString();
      sb.append(attrName);
      sb.append( i.hasNext() ? " " : "");
    }

    WSDLException wsdlExc = new WSDLException(WSDLException.INVALID_WSDL,
                                              "Element '" +
                                              elName +
                                              "' contained unexpected attributes: '" +
                                              sb.toString() +
                                              "'");

    wsdlExc.setLocation(XPathUtils.getXPathExprFromNode(location));

    throw wsdlExc;
  }

  public static void printAttribute(String name,
                                    String value,
                                    PrintWriter pw)
  {
    if (value != null)
    {
      pw.print(' ' + name + "=\"" + cleanString(value) + '\"');
    }
  }

  /**
   * Prints attributes with qualified names.
   */
  /*
  public static void printQualifiedAttribute(QName name,
                                             String value,
                                             Definition def,
                                             PrintWriter pw)
                                               throws WSDLException
  {
    if (name != null)
    {
      printAttribute(getQualifiedValue(name.getNamespaceURI(),
                                       name.getLocalPart(),
                                       def),
                     value,
                     pw);
    }
  }

  public static void printQualifiedAttribute(QName name,
                                             QName value,
                                             Definition def,
                                             PrintWriter pw)
                                               throws WSDLException
  {
    if (value != null)
    {
      printAttribute(getQualifiedValue(name.getNamespaceURI(),
                                       name.getLocalPart(),
                                       def),
                     getQualifiedValue(value.getNamespaceURI(),
                                       value.getLocalPart(),
                                       def),
                     pw);
    }
  }
  */

  public static void printQualifiedAttribute(String name,
                                             QName value,
                                             WSDLElement elem,
                                             PrintWriter pw)
                                               throws WSDLException
  {
    if (value != null)
    {
      printAttribute(name,
                     getQualifiedValue(value.getNamespaceURI(),
                                       value.getLocalPart(),
                                       elem),
                     pw);
    }
  }

  public static String getQualifiedValue(URI namespaceURI,
                                         String localPart,
                                         WSDLElement elem)
                                           throws WSDLException
  {
    String prefix = null;

    if (namespaceURI != null && !namespaceURI.toString().equals(""))
    {
      prefix = elem.getNamespacePrefix(namespaceURI);
    }
    
    String qv = ((prefix != null && !prefix.equals("")) ? prefix + ":" : "") + localPart;

    return qv;
  }

  public static String getQualifiedValue(String namespaceURI,
                                         String localPart,
                                         WSDLElement elem)
                                         throws WSDLException
  {
    URI nsUri = null;
    if(namespaceURI != null) {
        try {
            nsUri = new URI(namespaceURI);
        } catch (URISyntaxException e) {
            // TODO handle this correctly
            throw new RuntimeException(e);
        }
    }
    return getQualifiedValue(nsUri,localPart,elem);
  }

  /*
  public static String getPrefix(String namespaceURI,
                                 Definition def)
                                   throws WSDLException
  {
    String prefix = def.getPrefix(namespaceURI);

    if (prefix == null)
    {
      throw new WSDLException(WSDLException.OTHER_ERROR,
                              "Can't find prefix for '" + namespaceURI +
                              "'. Namespace prefixes must be set on the" +
                              " Definition object using the " +
                              "addNamespace(...) method.");
    }

    return prefix;
  }
  */

  public static String cleanString(String orig)
  {
    if (orig == null)
    {
      return "";
    }

    StringBuffer strBuf = new StringBuffer();
    char[] chars = orig.toCharArray();
    boolean inCDATA = false;

    for (int i = 0; i < chars.length; i++)
    {
      if (!inCDATA)
      {
        switch (chars[i])
        {
          case '&'  : strBuf.append("&amp;");
                      break;
          case '\"' : strBuf.append("&quot;");
                      break;
          case '\'' : strBuf.append("&apos;");
                      break;
          case '<'  :
                      {
                        if (chars.length >= i + 9)
                        {
                          String tempStr = new String(chars, i, 9);

                          if (tempStr.equals("<![CDATA["))
                          {
                            strBuf.append(tempStr);
                            i += 8;
                            inCDATA = true;
                          }
                          else
                          {
                            strBuf.append("&lt;");
                          }
                        }
                        else
                        {
                          strBuf.append("&lt;");
                        }
                      }
                      break;
          case '>'  : strBuf.append("&gt;");
                      break;
          default   : strBuf.append(chars[i]);
                      break;
        }
      }
      else
      {
        strBuf.append(chars[i]);

        if (chars[i] == '>'
            && chars[i - 1] == ']'
            && chars[i - 2] == ']')
        {
          inCDATA = false;
        }
      }
    }

    return strBuf.toString();
  }
  
}

