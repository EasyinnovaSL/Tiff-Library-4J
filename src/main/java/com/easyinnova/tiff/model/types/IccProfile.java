/**
 * <h1>IccProfile.java</h1> 
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
 * @since 25/5/2015
 *
 */
package com.easyinnova.tiff.model.types;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.IccProfileCreator;
import com.easyinnova.tiff.model.IccProfileCreators;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.ValidationResult;

/**
 * The Class IccProfile.
 */
public class IccProfile extends abstractTiffType {

  /** The tags. */
  public IccTags tags;

  /** The data. */
  TiffInputStream data;

  /** The validation result. */
  public ValidationResult validation;

  /** The version. */
  private String version;

  /** The version. */
  private IccProfileCreator creator;

  /**
   * Default Constructor.
   */
  public IccProfile() {
    version = null;
    creator = null;
  }

  /**
   * Instantiates a new icc profile.
   *
   * @param offset the offset in bytes to the beginning of the ICC Profile
   * @param size the size in bytes of the embedded ICC Profile
   * @param data the data
   */
  public IccProfile(int offset, int size, TiffInputStream data) {
    this.data = data;
    validation = new ValidationResult();
    setTypeSize(size);
    tags = new IccTags();
    readIccProfile(offset, size);
  }

  /**
   * Read icc profile.
   *
   * @param offset the offset
   * @param size the size
   */
  private void readIccProfile(int offset, int size) {
    try {
      int iccsize = data.readLong(offset).toInt();
      if (iccsize != size)
        validation.addErrorLoc("ICC Profile size does not match", "ICC");

      int index = offset + 128;
      int tagCount = data.readLong(index).toInt();

      for (int i = 0; i < tagCount; i++) {
        int signature = data.readLong(index).toInt();
        int tagOffset = data.readLong(index + 4).toInt();
        int tagSize = data.readLong(index + 8).toInt();
        IccTag tag = new IccTag(signature, tagOffset, tagSize);
        tags.addTag(tag);
        index += 12;
      }
    } catch (Exception ex) {
    }
  }

  /**
   * Gets the version.
   *
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Gets the creator.
   *
   * @return the creator
   */
  public IccProfileCreator getCreator() {
    return creator;
  }

  @Override
  public String toString() {
    String s = "";
    // for (IccTag tag : tags.tags)
    // s += "[" + tag.signature + "->" + tag.offset + "]";
    String scr = "Unknown creator";
    if (creator != null)
      scr = creator.getCreator();
    s = "[" + scr + ", " + version + "]";
    return s;
  }

  /**
   * Read version.
   *
   * @param tv the tv
   */
  private void readVersion(TagValue tv) {
    int maj = tv.getBytes(8, 1); // Major version
    int min = (tv.getBytes(9, 1) & 0xF0) >> 4; // Minor version (in the first 4 bits of the byte)
    // int z1 = tv.getBytes(10, 1);
    // int z2 = tv.getBytes(11, 1);
    version = maj + "." + min;
  }

  /**
   * Read creator.
   *
   * @param tv the tv
   */
  private void readCreator(TagValue tv) {
    int creatorSignature = tv.getBytes(4, 4);
    creator = IccProfileCreators.getIccProfile(creatorSignature);
  }

  /**
   * Reads the desired values of the ICCProfile.
   * 
   * @param tv the TagValue containing the array of bytes of the ICCProfile
   */
  @Override
  public void read(TagValue tv) {
    readVersion(tv);
    readCreator(tv);

    tv.clear();
    tv.add(this);
  }
}

