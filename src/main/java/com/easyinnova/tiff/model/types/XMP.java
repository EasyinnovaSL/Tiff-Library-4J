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

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.SerializeOptions;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.easyinnova.tiff.io.TiffOutputStream;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.MetadataObject;
import com.easyinnova.tiff.model.TagValue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Class XMP.
 */
public class XMP extends XmlType {
  private transient XMPMeta xmpMeta;

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
      reCreateMetadata();
    }
    return metadata;
  }

  void reCreateMetadata() throws ParserConfigurationException, TransformerException, SAXException, IOException, XMPException {
    metadata = new Metadata();

    if (xmpMeta != null) {
      String parentDNS = null;
      for (XMPIterator iterator = xmpMeta.iterator(); iterator.hasNext(); ) {
        XMPPropertyInfo propInfo = (XMPPropertyInfo) iterator.next();
        String path = propInfo.getPath();
        String value = propInfo.getValue();
        if (path != null && value != null && path.length() > 0 && value.length() > 0) {
          if (path.contains("xmpMM:History") && path.contains("stEvt")) {
            if (history == null) history = new ArrayList<>();
            Hashtable<String, String> action = new Hashtable<String, String>();
            String name = path;
            if (path.contains(":")) name = path.substring(path.lastIndexOf(":") + 1);
            action.put(name, value);
            history.add(action);
          } else {
            String name = path;
            if (path.contains(":")) name = path.substring(path.indexOf(":") + 1);
            if (name.contains("[")) name = name.substring(0, name.indexOf("[")).trim();
            Text txt = new Text(value);
            txt.setContainer("XMP");
            metadata.add(name, txt, path, propInfo.getNamespace());
            if (path.toLowerCase().startsWith("dc")) {
              // Dublin Core
              metadata.getMetadataObject(name).setIsDublinCore(true);
              metadata.getMetadataObject(name).setSchema(parentDNS);
            }
          }
        } else {
          parentDNS = propInfo.getNamespace();
        }
      }
    } else {
      oldSchool();
    }
  }

  public String getCreator() {
    return getTag("creator");
  }

  public String getCopyright() {
    return getTag("rights");
  }

  public String getDescription() {
    return getTag("description");
  }

  public String getDatetime() {
    return getTag("CreateDate");
  }

  public String getTag(String name) {
    if (metadata == null) try {
      createMetadata();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    if (metadata.contains(name)) {
      return metadata.get(name).toString();
    }
    return null;
  }

  public void editCopyright(String value) {
    try {
      editTag("rights", value);
    } catch (XMPException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void editCreator(String value) {
    try {
      editTag("creator", value);
    } catch (XMPException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void editDescription(String value) {
    try {
      editTag("description", value);
    } catch (XMPException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void editDatetime(String value) {
    try {
      editTag("CreateDate", value);
    } catch (XMPException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void editTag(String tagName, String tagValue) throws Exception {
    MetadataObject mdo = metadata.getMetadataObject(tagName);
    String schema = mdo.getSchema();
    String name = tagName;
    if (mdo.isDublinCore()) name = "dc:" + name + "[1]";
    xmpMeta.setProperty(schema, name, tagValue);
    reCreateMetadata();
  }

  @Deprecated
  void oldSchool() throws ParserConfigurationException, IOException, SAXException, TransformerException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    builder.setErrorHandler(null);
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
      metadata.add(nameWithoutPrefix, txt, null, null);
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
    try {
      if (sXml.contains("xmpMM:History")) {
        String sHistory = sXml.substring(sXml.indexOf("xmpMM:History"));
        sHistory = sHistory.substring(0, sHistory.indexOf("</xmpMM:History>"));
        history = new ArrayList<>();
        int index = sHistory.indexOf("xmpMM:History");
        while (sHistory.indexOf("<rdf:li", index) > 0) {
          index = sHistory.indexOf("<rdf:li", index);
          String subs = sHistory.substring(index);
          int fi = subs.indexOf("</rdf:li");
          if (subs.indexOf("/>") != -1 && (fi == -1 || subs.indexOf("/>") < fi))
            fi = subs.indexOf("/>");
          subs = subs.substring(0, fi);
          Hashtable<String, String> action = new Hashtable<String, String>();
          int index2 = 0;
          while (subs.indexOf("stEvt:", index2) > 0) {
            if (subs.indexOf("<stEvt:", index2) > -1) {
              index2 = subs.indexOf("stEvt:", index2);
              String stevt = subs.substring(index2);
              stevt = stevt.substring(0, stevt.indexOf("</stEvt") + 2);
              String name = stevt.substring(stevt.indexOf(":") + 1);
              name = name.substring(0, name.indexOf(">"));
              String value = stevt.substring(stevt.indexOf(">") + 1);
              value = value.substring(0, value.indexOf("</"));
              action.put(name, value);
              index2 = subs.indexOf("</stEvt:", index2) + 9;
            } else {
              index2 = subs.indexOf("stEvt:", index2);
              String stevt = subs.substring(index2);
              int fin = stevt.indexOf("\"", stevt.indexOf("\"") + 1);
              stevt = stevt.substring(0, fin);
              String name = stevt.substring(stevt.indexOf(":") + 1);
              name = name.substring(0, name.indexOf("="));
              String value = stevt.substring(stevt.indexOf("\"") + 1);
              action.put(name, value);
              index2 += fin;
            }
          }
          history.add(action);
          index += fi;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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

    xmpMeta = XMPMetaFactory.parseFromBuffer(getBytes());

    createMetadata();
  }

  public void write(TiffOutputStream data) throws IOException, XMPException {
    SerializeOptions options = new SerializeOptions();
    byte[] buffer = XMPMetaFactory.serializeToBuffer(xmpMeta, options);
    for (byte b : buffer) {
      data.put(b);
    }
  }

  public int getLength() {
    SerializeOptions options = new SerializeOptions();
    try {
      byte[] buffer = XMPMetaFactory.serializeToBuffer(xmpMeta, options);
      return buffer.length;
    } catch (XMPException e) {
      e.printStackTrace();
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public List<Hashtable<String, String>> getHistory() {
    return history;
  }
}
