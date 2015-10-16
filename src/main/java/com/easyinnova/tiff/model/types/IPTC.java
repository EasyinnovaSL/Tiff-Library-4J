/**
 * <h1>IPTC.java</h1>
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
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 6/7/2015
 */
package com.easyinnova.tiff.model.types;

import com.easyinnova.iptc.IptcTags;
import com.easyinnova.iptc.Tag;
import com.easyinnova.iptc.abstractIptcType;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.ValidationResult;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class IPTC.
 */
public class IPTC extends abstractTiffType {

  /**
   * The Constant SEGMENT_MARKER.
   */
  public static final int[] SEGMENT_MARKER = {28, 2};
  /**
   * The validation result.
   */
  public ValidationResult validation;

  /** The content. */
  private HashMap<Byte, List<abstractIptcType>> content;

  /** The original value. */
  private List<abstractTiffType> originalValue;

  /**
   * Instantiates a new IPTC.
   */
  public IPTC() {
    validation = new ValidationResult();
  }

  /**
   * Gets the original.
   *
   * @return the original
   */
  public List<abstractTiffType> getOriginal() {
    return originalValue;
  }

  /**
   * Creates the metadata.
   *
   * @return the hash map
   */
  @Override
  public Metadata createMetadata() {
    Metadata metadata = new Metadata();
    try {
      for (Map.Entry<Byte, List<abstractIptcType>> entry : content.entrySet()) {
        Byte b = entry.getKey();
        List<abstractIptcType> valueList = entry.getValue();
        for (abstractIptcType element : valueList) {
          Text txt = new Text(element.toString());
          String tagName = IptcTags.tagMap.get(Integer.parseInt(b.toString())).getShortName();
          metadata.add(tagName, txt);
        }
      }
    } catch (Exception ex) {
      /*Nothing to be shown*/
    }

    return metadata;
  }

  @Override
  public String toString() {
    return content.toString();
  }

  /**
   * Removes the tag.
   *
   * @param tagName the tag name
   */
  public void removeTag(String tagName) {
    List<abstractTiffType> l = new ArrayList<abstractTiffType>();
    for (int i = 0; i < originalValue.size(); i++) {
      l.add(originalValue.get(i));
    }
    for (int i = 0; i < l.size(); i++) {
      if (l.get(i).toInt() == SEGMENT_MARKER[0]) {
        // Check if segment contains type, size and content
        if ((i + 5) < l.size() && l.get(i + 1).toInt() == SEGMENT_MARKER[1]) {
          // Get tag type and size
          Byte type = new Byte(0);
          type.setValue(l.get(i + 2).toByte());
          int size =
 ((l.get(i + 3).toByte() & 0xff) << 8) | (l.get(i + 4).toByte() & 0xff);
          if ((i + 4 + size) < l.size()) {
            List<Byte> value = new ArrayList<Byte>();
            // Read the value of the tag
            for (int j = (i + 5); j <= (i + 4 + size); j++) {
              Byte current = new Byte(0);
              current.setValue(l.get(j).toByte());
              value.add(current);
            }

            abstractIptcType object = null;
            if (IptcTags.hasTag(type.getValue())) {
              // Known tag
              Tag t = IptcTags.getTag(type.getValue());
              if (t.hasType()) {
                String tagClass = t.getType();

                try {
                  object =
                      (abstractIptcType) Class.forName("com.easyinnova.iptc." + tagClass)
                          .getConstructor().newInstance();
                  object.read(value);
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                    | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                  validation.addError("Parse error getting IPTC tag " + type.toString());
                }
              }
            }

            if (object != null) {
              // Add content
              List<abstractIptcType> list = content.get(type);
              if (list == null) {
                list = new ArrayList<abstractIptcType>();
              }
              list.add(object);

              String tagname =
                  IptcTags.tagMap.get(Integer.parseInt(type.toString())).getShortName();
              if (tagname.equals(tagName)) {
                int tagSize = size + 3;
                for (int ii = 0; ii < tagSize; ii++) {
                  l.remove(i + 2);
                }
                i -= tagSize;
              }
            }
            // Jump to the end of read tag
            i = i + 4 + size;
          }
        }
      }
    }
    originalValue = l;
    TagValue tv = new TagValue(TiffTags.getTagId("IPTC"), 7);
    tv.setValue(originalValue);
    read(tv);
  }

  /**
   * Reads the IPTC.
   *
   * @param tv the TagValue containing the array of bytes of the IPTC
   */
  @Override
  public void read(TagValue tv) {
    content = new HashMap<Byte, List<abstractIptcType>>();

    for (int i = 0; i < tv.getCardinality(); i++) {
      if (tv.getValue().get(i).toInt() == SEGMENT_MARKER[0]) {
        // Check if segment contains type, size and content
        if ((i + 5) < tv.getCardinality() && tv.getValue().get(i + 1).toInt() == SEGMENT_MARKER[1]) {
          // Get tag type and size
          Byte type = new Byte(0);
          type.setValue(tv.getValue().get(i + 2).toByte());
          int size =
              ((tv.getValue().get(i + 3).toByte() & 0xff) << 8)
                  | (tv.getValue().get(i + 4).toByte() & 0xff);
          if ((i + 4 + size) < tv.getCardinality()) {
            List<Byte> value = new ArrayList<Byte>();
            // Read the value of the tag
            for (int j = (i + 5); j <= (i + 4 + size); j++) {
              Byte current = new Byte(0);
              current.setValue(tv.getValue().get(j).toByte());
              value.add(current);
            }

            abstractIptcType object = null;
            if (IptcTags.hasTag(type.getValue())) {
              // Known tag
              Tag t = IptcTags.getTag(type.getValue());
              if (t.hasType()) {
                String tagClass = t.getType();

                try {
                  object = (abstractIptcType) Class.forName("com.easyinnova.iptc." + tagClass)
                      .getConstructor().newInstance();
                  object.read(value);
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                  validation.addError("Parse error getting IPTC tag " + type.toString());
                }
              }
            }

            if (object != null) {
              // Add content
              List<abstractIptcType> list = content.get(type);
              if (list == null) {
                list = new ArrayList<abstractIptcType>();
              }
              list.add(object);
              content.put(type, list);
            }
            // Jump to the end of read tag
            i = i + 4 + size;
          }
        }
      }
    }

    originalValue = tv.getValue();
    tv.reset();
    tv.add(this);
  }

  @Override
  public boolean containsMetadata() {
    return true;
  }

}

