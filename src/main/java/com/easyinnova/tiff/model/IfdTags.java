/**
 * <h1>IfdTags.java</h1>
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 20/5/2015
 *
 */
package com.easyinnova.tiff.model;

import com.easyinnova.tiff.model.types.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Set of tags from an IFD.
 */
public class IfdTags {
  /** Tag list. */
  private ArrayList<TagValue> tags;

  /** The Hash tags id. */
  private HashMap<Integer, TagValue> hashTagsId;

  /** The Hash tags name. */
  private HashMap<String, TagValue> hashTagsName;

  /**
   * Instantiates a new set of tags.
   */
  public IfdTags() {
    tags = new ArrayList<TagValue>();
    hashTagsId = new HashMap<Integer, TagValue>();
    hashTagsName = new HashMap<String, TagValue>();
  }

  /**
   * Adds a tag to the set.
   *
   * @param tag the tag to add
   */
  public void addTag(TagValue tag) {
    //int pos = 0;
    //while (pos < tags.size() && tags.get(pos).getId() < tag.getId()) pos++;
    //tags.add(pos, tag);
    tags.add(tag);
    if (!hashTagsId.containsKey(tag.getId())) {
      hashTagsId.put(tag.getId(), tag);
    }
    Tag t = TiffTags.getTag(tag.getId());
    if (t != null) {
      if (hashTagsName.containsKey(t.getName())) {
        hashTagsName.put(t.getName(), tag);
      }
    }
  }

  /**
   * Adds the tag.
   *
   * @param tagName the tag name
   * @param tagValue the tag value
   */
  public void addTag(String tagName, int[] tagValue) {
    int id = TiffTags.getTagId(tagName);
    TagValue tag = new TagValue(id, 3);
    for (int i = 0; i < tagValue.length; i++) {
      com.easyinnova.tiff.model.types.Short val = new com.easyinnova.tiff.model.types.Short(tagValue[i]);
      tag.add(val);
    }
    addTag(tag);
  }

  /**
   * Adds the tag.
   *
   * @param tagName the tag name
   * @param tagValue the tag value
   */
  public void addTag(String tagName, String tagValue) {
    int id = TiffTags.getTagId(tagName);
    TagValue tag = new TagValue(id, 2);
    for (int i = 0; i < tagValue.length(); i++) {
      int val = tagValue.charAt(i);
      if (val > 127) val = 0;
      Ascii cha = new Ascii(val);
      tag.add(cha);
    }
    Ascii chaf = new Ascii(0);
    tag.add(chaf);
    addTag(tag);
  }

  /**
   * Removes the tag.
   *
   * @param tagName the tag name
   */
  public void removeTag(String tagName) {
    for (int i = 0; i < tags.size(); i++) {
      if (tags.get(i).getName().equals(tagName)) {
        tags.remove(i);
        if (hashTagsName.containsKey(tagName))
          hashTagsName.remove(tagName);
        if (hashTagsId.containsKey(TiffTags.getTagId(tagName)))
          hashTagsId.remove(TiffTags.getTagId(tagName));
        i--;
      }
    }
  }

  /**
   * Returns true if the id is contained.
   *
   * @param id the id to check
   * @return true if the id is contained
   */
  public boolean containsTagId(int id) {
    return hashTagsId.containsKey(id);
  }

  /**
   * Gets the Tag identified by its id.
   *
   * @param id tag id
   * @return the tag object
   */
  public TagValue get(int id) {
    return hashTagsId.get(id);
  }

  /**
   * Gets the Tag identified by its id.
   *
   * @param tagName the tag name
   * @return the tag object
   */
  public TagValue get(String tagName) {
    return hashTagsId.get(TiffTags.getTagId(tagName));
  }

  /**
   * Returns the tag list.
   *
   * @return list of tags
   */
  public ArrayList<TagValue> getTags() {
    return tags;
  }

  @Override
  public String toString() {
    String s = "";
    for (TagValue t : tags) {
      s += "[" + t.getName() + ":";
      String st = t.toString();
      if (st.length() < 100)
        s += st;
      else
        s += st.substring(0, 50) + "...";
      s += "]\r\n";
    }
    return s;
  }
}

