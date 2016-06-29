/**
 * <h1>XMP.java</h1> 
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at
 * <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 9/6/2015
 *
 */
package com.easyinnova.tiff.model.types;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Class XMP.
 */
public class XMP extends XmlType {

  /** The metadata. */
  private Metadata metadata;

  /** The history */
  private List<Hashtable<String, String>> history;

  /**
   * Default constructor.
   */
  public XMP() {
    metadata = null;
  }

  /**
   * Creates the metadata.
   *
   * @return the hash map
   * @throws Exception parsing exception
   */
  @Override
  public Metadata createMetadata() throws Exception {
    if (metadata == null) {
      metadata = new Metadata();

      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new InputSource(xmlReader));
      DocumentTraversal traversal = (DocumentTraversal) doc;

      NodeIterator iterator = traversal.createNodeIterator(
          doc.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

      for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
        Element elem = ((Element) n);
        String name = elem.getTagName();
        String content = elem.getTextContent();
        String nameWithoutPrefix = name;
        String prefix = "";
        if (name.contains(":")) {
          nameWithoutPrefix = name.substring(name.indexOf(":") + 1);
          prefix = name.substring(0, name.indexOf(":"));
        }
        if (prefix.equals("x") || prefix.equals("rdf") || prefix.toLowerCase().equals("stevt")) continue;
        //System.out.println(name + "=" + content);
        Text txt = new Text(content);
        txt.setContainer("XMP");
        metadata.add(nameWithoutPrefix, txt);
        if (name.toLowerCase().startsWith("dc")) {
          // Dublin Core
          metadata.getMetadataObject(nameWithoutPrefix).setIsDublinCore(true);
        }
      }

      // History
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String sXml = writer.getBuffer().toString().replaceAll("\n|\r", "");
      history = null;
      if (sXml.contains("xmpMM:History")) {
        history = new ArrayList<>();
        int index = sXml.indexOf("xmpMM:History");
        while (sXml.indexOf("<rdf:li", index) > 0) {
          index = sXml.indexOf("<rdf:li", index);
          String subs = sXml.substring(index);
          subs = subs.substring(0, subs.indexOf("</rdf:li"));
          Hashtable<String, String> action = new Hashtable<String, String>();
          int index2 = 0;
          while (subs.indexOf("<stEvt:", index2) > 0) {
            index2 = subs.indexOf("<stEvt:", index2);
            String stevt = subs.substring(index2);
            stevt = stevt.substring(0, stevt.indexOf("</stEvt")+2);
            String name = stevt.substring(stevt.indexOf(":")+1);
            name = name.substring(0, name.indexOf(">"));
            String value = stevt.substring(stevt.indexOf(">")+1);
            value = value.substring(0, value.indexOf("</"));
            action.put(name, value);
            index2 = subs.indexOf("</stEvt:", index2);
          }
          history.add(action);
          index = sXml.indexOf("</rdf:li", index);
        }
      }
    }
    return metadata;
  }

  @Override
  public boolean containsMetadata() {
    return true;
  }

  /**
   * Reads the XML.
   *
   * @param tv the TagValue containing the array of bytes of the ICCProfile
   * @throws Exception parse exception
   */
  @Override
  public void read(TagValue tv) throws Exception {
    super.read(tv);
    createMetadata();
  }

  public List<Hashtable<String, String>> getHistory() {
    return history;
  }
}
