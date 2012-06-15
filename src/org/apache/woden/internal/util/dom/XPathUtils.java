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

import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * This class copied from WSDL4J.
 * 
 * A <code>XPathUtils</code> ...
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 * @author Sanjiva Weerawarana (sanjiva@watson.ibm.com)
 */
public class XPathUtils
{
  private static Node getPreviousTypedNode(Node node, short nodeType)
  {
    node = node.getPreviousSibling();

    while (node != null && node.getNodeType() != nodeType)
    {
      node = node.getPreviousSibling();
    }

    return node;
  }

  private static Node getNextTypedNode(Node node, short nodeType)
  {
    node = node.getNextSibling();

    while (node != null && node.getNodeType() != nodeType)
    {
      node = node.getNextSibling();
    }

    return node;
  }

  private static String getValue(Node node, short nodeType)
  {
    switch (nodeType)
    {
      case Node.ELEMENT_NODE :
        return ((Element)node).getTagName();

      case Node.TEXT_NODE :
        return ((Text)node).getData();

      case Node.PROCESSING_INSTRUCTION_NODE :
        return ((ProcessingInstruction)node).getData();

      default :
        return "";
    }
  }

  private static short getNodeType(Node node)
  {
    return (node != null ? node.getNodeType() : -1);
  }

  private static String getXPathFromVector(Vector path)
  {
    StringBuffer strBuf = new StringBuffer();
    int          length = path.size();

    for (int i = 0; i < length; i++)
    {
      Node   tempNode    = (Node)path.elementAt(i);
      short  nodeType    = getNodeType(tempNode);
      String targetValue = getValue(tempNode, nodeType);
      int    position    = 1;

      tempNode = getPreviousTypedNode(tempNode, nodeType);

      while (tempNode != null)
      {
        if (nodeType == Node.ELEMENT_NODE)
        {
          if (getValue(tempNode, nodeType).equals(targetValue))
          {
            position++;
          }
        }
        else
        {
          position++;
        }

        tempNode = getPreviousTypedNode(tempNode, nodeType);
      }

      boolean hasMatchingSiblings = (position > 1);

      if (!hasMatchingSiblings)
      {
        tempNode = (Node)path.elementAt(i);
        tempNode = getNextTypedNode(tempNode, nodeType);

        while (!hasMatchingSiblings && tempNode != null)
        {
          if (nodeType == Node.ELEMENT_NODE)
          {
            if (getValue(tempNode, nodeType).equals(targetValue))
            {
              hasMatchingSiblings = true;
            }
            else
            {
              tempNode = getNextTypedNode(tempNode, nodeType);
            }
          }
          else
          {
            hasMatchingSiblings = true;
          }
        }
      }

      String step;

      switch (nodeType)
      {
        case Node.TEXT_NODE :
          step = "text()";
          break;
        case Node.PROCESSING_INSTRUCTION_NODE :
          step = "processing-instruction()";
          break;
        default :
          step = targetValue;
          break;
      }

      if (step != null && step.length() > 0)
      {
        strBuf.append('/' + step);
      }

      if (hasMatchingSiblings)
      {
        strBuf.append("[" + position + "]");
      }
    }

    return strBuf.toString();
  }

  private static Vector getVectorPathFromNode(Node node)
  {
    Vector path = new Vector();

    while (node != null)
    {
      path.insertElementAt(node, 0);
      node = node.getParentNode();
    }

    return path;
  }

  /**
   * Generates an XPath expression that will return only the given node as its
   * result. This method only works for element, text, document and PI nodes.
   *
   * @param node the node to generate an XPath expression for. This node must
   * be an element node, a text node, a document node, or a processing
   * instruction node.
   * @return an XPath expression that will return only the given node as its
   * result.
   * @exception IllegalArgumentException if the given node is not an element,
   * text, document or PI node.
   */
  public static String getXPathExprFromNode(Node node)
                                                throws IllegalArgumentException
  {
    short nodeType = getNodeType(node);

    switch (nodeType)
    {
      case Node.ELEMENT_NODE :
      case Node.TEXT_NODE :
      case Node.PROCESSING_INSTRUCTION_NODE :
        return getXPathFromVector(getVectorPathFromNode(node));

      case Node.DOCUMENT_NODE :
        return "/";

      default :
        throw new IllegalArgumentException("Only works for element, text, " +
                                           "document, and PI nodes.");
    }
  }
}