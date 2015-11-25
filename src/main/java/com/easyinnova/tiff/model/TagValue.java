/**
 * <h1>TagValue.java</h1>
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 27/5/2015
 *
 */
package com.easyinnova.tiff.model;

import com.easyinnova.tiff.model.types.abstractTiffType;

import java.util.ArrayList;
import java.util.List;

/**
 * IFD tag object containing a list of values of a given tag type.
 */
public class TagValue extends TiffObject {

  /** The tag identifier. */
  private int id;

  /** The type of the tag. */
  private int type;

  /** The list of values. */
  private List<abstractTiffType> value;
  
  /** The offset where the tag has been written (only used in TiffWriter). */
  private int offset;

  /** The offset where the tag has been read. */
  private int readOffset;

  /** The tag length that has been read. */
  private int readLength;

  /**
   * Instantiates a new tag value.
   *
   * @param id tag id
   * @param type tag type id
   */
  public TagValue(int id, int type) {
    this.id = id;
    this.type = type;
    value = new ArrayList<abstractTiffType>();
  }
  
  /**
   * Sets the read offset.
   *
   * @param offset the new read offset
   */
  public void setReadOffset(int offset) {
    readOffset = offset;
  }

  /**
   * Gets the read offset.
   *
   * @return the read offset
   */
  public int getReadOffset() {
    return readOffset;
  }

  /**
   * Gets the descriptive value.
   *
   * @return the descriptive value
   */
  public String getDescriptiveValue() {
    String desc = this.toString();
    Tag tag = TiffTags.getTag(id);
    if (tag != null) {
      String tagDescription = tag.getTagValueDescription(toString());
      if (tagDescription != null)
        desc = tagDescription;
    }
    return desc;
  }

  /**
   * Sets the read length.
   *
   * @param length the new read length
   */
  public void setReadLength(int length) {
    readLength = length;
  }

  /**
   * Gets the readlength.
   *
   * @return the readlength
   */
  public int getReadlength() {
    return readLength;
  }

  /**
   * Gets the list of values.
   *
   * @return the list
   */
  public List<abstractTiffType> getValue() {
    return this.value; 
   }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(List<abstractTiffType> value) {
    this.value = value;
  }

  /**
   * Adds a value to the list.
   *
   * @param value the value
   */
  public void add(abstractTiffType value) {
    this.value.add(value);
  }

  /**
   * Sets the offset.
   *
   * @param off the new offset
   */
  public void setOffset(int off) {
    offset = off;
  }

  /**
   * Gets the offset.
   *
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Gets the tag id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the tag type.
   *
   * @return the type id
   */
  public int getType() {
    return type;
  }

  /**
   * Gets the number of values in the list.
   *
   * @return the cardinality
   */
  public int getCardinality() {
    return value.size();
  }

  /**
   * Gets the first value of the list parsed as a number.
   *
   * @return the first integer value
   */
  public long getFirstNumericValue() {
    return Long.parseLong(value.get(0).toString());
  }

  /**
   * Gets a string representing the value.
   *
   * @return the string
   */
  public String toString() {
    String s = "";
    int max = 200;
    boolean defined = false;
    try {
      defined = TiffTags.hasTag(id) && TiffTags.getTag(id).hasTypedef();
    } catch (Exception ex) {
    }
    if (defined) {
      s = value.get(0).toString();
    } else if (type != 1 || value.size() < 10) {
      int n = value.size();
      if (n > 1 && type != 2)
        s += "[";
      for (int i = 0; i < n; i++) {
        s += value.get(i).toString();
        if (n > 1 && i + 1 < n && type != 2)
          s += ",";
        if (s.length() > max)
          break;
      }
      if (n > 1 && type != 2)
        s += "]";
    }
    return s;
  }

  /**
   * Gets the name of the tag.
   *
   * @return the name
   */
  public String getName() {
    String name = "" + id;
    if (TiffTags.hasTag(id))
      name = TiffTags.getTag(id).getName();
    return name;
  }

  /**
   * Gets the bytes.
   *
   * @param i the i
   * @param j the j
   * @return the bytes
   */
  public int getBytes(int i, int j) {
    int result = 0;
    for (int k = i; k < i + j; k++) {
      result += value.get(k).toInt();
      if (k + 1 < i + j)
        result <<= 8;
    }
    return result;
  }

  /**
   * Clears the list of values.
   */
  public void clear() {
    value.clear();
  }

  /**
   * Reset.
   */
  public void reset() {
    value = new ArrayList<abstractTiffType>();
  }
}

