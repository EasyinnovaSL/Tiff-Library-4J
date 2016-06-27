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
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 * The Class XMP.
 */
public class XMP extends XmlType {

  /** The metadata. */
  private Metadata metadata;

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

      /*DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new InputSource(xmlReader));
      DocumentTraversal traversal = (DocumentTraversal) doc;

      NodeIterator iterator = traversal.createNodeIterator(
          doc.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

      for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
        String tagname = ((Element) n).getTagName();
        if(tagname.equals("title")) {
          System.out.println("text=" + ((Element)n).getAttribute("text"));
        }
      }*/

      try {
        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlModel = inputFactory.createXMLStreamReader(xmlReader);
        while (xmlModel.hasNext()) {
          int eventType = xmlModel.next();
          switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
              String elementName = xmlModel.getLocalName();
              xmlModel.next();
              String elementData = xmlModel.getText();
              if (elementName.trim().length() > 0 && elementData.trim().length() > 0) {
                Text txt = new Text(elementData);
                txt.setContainer("XMP");
                metadata.add(elementName, txt);
                if (xmlModel.getPrefix() != null && xmlModel.getPrefix().equalsIgnoreCase("dc")) {
                  // Dublin Core
                  metadata.getMetadataObject(elementName).setIsDublinCore(true);
                }
              }
              break;
            default:
              break;
          }
        }
      } catch (Exception ex) {
        // throw new Exception("Parse format");
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
}
