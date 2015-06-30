/**
 * <h1>XmlType.java</h1> 
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
package main.java.com.easyinnova.tiff.model.types;

import main.java.com.easyinnova.tiff.model.TagValue;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * The Class XmlType.
 */
public class XmlType extends abstractTiffType {
  /** The xml. */
  private String xml;

  /** The xml model. */
  protected XMLStreamReader xmlModel;

  /**
   * Default constructor.
   */
  public XmlType() {
    xml = null;
    xmlModel = null;
  }

  /**
   * Load xml.
   *
   * @throws XMLStreamException the XML stream exception
   */
  private void loadXml() throws XMLStreamException {
    final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    final StringReader reader = new StringReader(xml);
    xmlModel = inputFactory.createXMLStreamReader(reader);
  }

  /**
   * Gets the xml model.
   *
   * @return the xml model
   */
  public XMLStreamReader getXmlModel() {
    return xmlModel;
  }

  @Override
  public String toString() {
    return xml.replace('\n', ' ');
  }

  /**
   * Reads the XML.
   * 
   * @param tv the TagValue containing the array of bytes of the ICCProfile
   */
  @Override
  public void read(TagValue tv) {
    xml = "";

    for (int i = 0; i < tv.getCardinality(); i++) {
      xml += (char) tv.getValue().get(i).toUint();
    }

    try {
      loadXml();
    } catch (Exception ex) {
      xmlModel = null;
    }

    tv.clear();
    tv.add(this);
  }
}

