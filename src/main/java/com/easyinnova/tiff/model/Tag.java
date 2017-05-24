/**
 * <h1>Tag.java</h1>
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
 * @since 18/5/2015
 *
 */

package com.easyinnova.tiff.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Tag definition.
 */
public class Tag {
  
  /** The id. */
  private Integer id;

  /** The name. */
  private String name = "";

  /** The type. */
  private List<String> type;

  /** The cardinality. */
  private String cardinality = "";

  /** The default value. */
  private String defaultValue = "";
  
  /** The ifd. */
  private String ifd = "";
  
  /** The description. */
  private String description = "";
  
  /** The source. */
  private String source = "";
  
  /** The typedef. */
  private String typedef = null;

  /** The forced description value. */
  private String forceDescription = null;

  /** The created. */
  private Date created;
  
  /** The modified. */
  private Date modified;
  
  /** The value codes. */
  private List<Integer> valueCodes;

  /** The value descriptions. */
  private List<String> valueDescriptions;

  /** The tag value descriptions. */
  private HashMap<String, String> tagValueDescriptions = new HashMap<String, String>();

  /**
   * Gets the id.
   *
   * @return the id
   */
  public Integer getId() {
    return id;
  }
  
  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Creates the values dictionary.
   */
  public void createValuesDictionary() {
    tagValueDescriptions = new HashMap<String, String>();
    if (valueCodes != null && valueDescriptions != null && valueCodes.size() > 0
        && valueCodes.size() == valueDescriptions.size()) {
      for (int i = 0; i < valueCodes.size(); i++) {
        tagValueDescriptions.put(valueCodes.get(i) + "", valueDescriptions.get(i));
      }
    }
  }

  /**
   * Gets the tag value description.
   *
   * @param encodedValue the encoded value
   * @return the tag value description
   */
  public String getTextDescription(String encodedValue) {
    if (forceDescription != null) {
      return forceDescription;
    }
    String desc = null;
    if (tagValueDescriptions.containsKey(encodedValue)) {
      desc = tagValueDescriptions.get(encodedValue);
    }
    return desc;
  }

  public boolean hasReadableDescription(){
    return typedef == null;
  }

  public HashMap<String, String> getValues() {
    return tagValueDescriptions;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public List<String> getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(List<String> type) {
    this.type = type;
  }

  /**
   * Gets the cardinality.
   *
   * @return the cardinality
   */
  public String getCardinality() {
    return cardinality;
  }

  /**
   * Sets the cardinality.
   *
   * @param cardinality the new cardinality
   */
  public void setCardinality(String cardinality) {
    this.cardinality = cardinality;
  }

  /**
   * Gets the default value.
   *
   * @return the default value
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Sets the default value.
   *
   * @param defaultValue the new default value
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * Gets the ifd.
   *
   * @return the ifd
   */
  public String getIfd() {
    return ifd;
  }

  /**
   * Sets the ifd.
   *
   * @param ifd the new ifd
   */
  public void setIfd(String ifd) {
    this.ifd = ifd;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the source.
   *
   * @return the source
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the source.
   *
   * @param source the new source
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Gets the created.
   *
   * @return the created
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the created.
   *
   * @param created the new created
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Gets the modified.
   *
   * @return the modified
   */
  public Date getModified() {
    return modified;
  }

  /**
   * Sets the modified.
   *
   * @param modified the new modified
   */
  public void setModified(Date modified) {
    this.modified = modified;
  }

  /**
   * Gets the typedef.
   *
   * @return the typedef
   */
  public String getTypedef() {
    return typedef;
  }

  /**
   * Instantiates a new tag.
   *
   * @param id the id
   * @param name the name
   * @param type the type
   * @param cardinality the cardinality
   * @param defaultValue the default value
   * @param typedef the typedef
   */
  public Tag(int id, String name, List<String>  type, String cardinality, String defaultValue, String typedef, String forceDescription) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.cardinality = cardinality;
    this.defaultValue = defaultValue;
    this.ifd = ""; 
    this.description = "";
    this.source = "";
    // DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    this.created = date;
    this.modified= date;
    this.typedef = typedef;
    this.forceDescription = forceDescription;
  }

  public void setValues(HashMap<String, String> values) {
    this.tagValueDescriptions = values;
  }

  /**
   * Checks for typedef.
   *
   * @return true, if successful
   */
  public boolean hasTypedef() {
    return typedef != null;
  }
}
