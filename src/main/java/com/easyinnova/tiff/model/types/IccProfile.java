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

import com.easyinnova.tiff.model.IccProfileCreator;
import com.easyinnova.tiff.model.IccProfileCreators;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.ValidationResult;

import java.io.IOException;

/**
 * The Class IccProfile.
 */
public class IccProfile extends abstractTiffType {

  public enum ProfileClass { Input, Display, Output, DeviceLink, ColorSpace, Abstract, NamedColor, Unknown }

  /** The tags. */
  public IccTags tags;

  /** The validation result. */
  public ValidationResult validation;

  /** The version. */
  private String version;

  /** The description. */
  private String description;

  /** The version. */
  private IccProfileCreator creator;

  /** Profile class */
  private ProfileClass profileClass;

  /**
   * Default Constructor.
   */
  public IccProfile() {
    version = null;
    creator = null;
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
    if (description != null) return description;
    String s = "";
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
    int maj = tv.getBytesBigEndian(8, 1); // Major version
    int min = (tv.getBytesBigEndian(9, 1) & 0xF0) >> 4; // Minor version (in the first 4 bits of the
                                                        // byte)
    version = maj + "." + min;
  }

  /**
   * Read class.
   *
   * @param tv the tv
   */
  private void readClass(TagValue tv) {
    int profileClass = tv.getBytesBigEndian(12, 4);
    String hex = Integer.toHexString(profileClass);
    if (hex.equals("73636e72")) {
      this.profileClass = ProfileClass.Input;
    } else if (hex.equals("6d6e7472")) {
      this.profileClass = ProfileClass.Display;
    } else if (hex.equals("70727472")) {
      this.profileClass = ProfileClass.Output;
    } else if (hex.equals("6C696e6b")) {
      this.profileClass = ProfileClass.DeviceLink;
    } else if (hex.equals("73706163")) {
      this.profileClass = ProfileClass.ColorSpace;
    } else if (hex.equals("61627374")) {
      this.profileClass = ProfileClass.Abstract;
    } else if (hex.equals("6e6d636c")) {
      this.profileClass = ProfileClass.NamedColor;
    } else {
      this.profileClass = ProfileClass.Unknown;
    }
  }

  public ProfileClass getProfileClass() {
    return profileClass;
  }

  /**
   * Read creator.
   *
   * @param tv the tv
   */
  private void readCreator(TagValue tv) {
    int creatorSignature = tv.getBytesBigEndian(4, 4);
    creator = IccProfileCreators.getIccProfile(creatorSignature);
  }

  /**
   * Read description.
   *
   * @param tv the tag value
   * @throws IOException exception
   * @throws NumberFormatException exception
   */
  private void readDescription(TagValue tv) throws NumberFormatException, IOException {
    int index = 128;
    int tagCount = tv.getBytesBigEndian(index, 4);
    index += 4;
    for (int i = 0; i < tagCount; i++) {
      int signature = tv.getBytesBigEndian(index, 4);
      int tagOffset = tv.getBytesBigEndian(index + 4, 4);
      if (Integer.toHexString(signature).equals("64657363")) {
        String typedesc = Integer.toHexString(tv.getBytesBigEndian(tagOffset, 4));
        if (typedesc.equals("64657363")) {
          int size = tv.getBytesBigEndian(tagOffset + 8, 4) - 1;
          String s = "";
          int j = 0;
          int begin = tagOffset + 12;
          while (begin + j < begin + size) {
            int unicode_char = tv.getBytesBigEndian(begin + j, 1);
            s += Character.toString((char) unicode_char);
            j++;
          }
          description = s;
        } else {
          description = "";
        }
      }
      index += 12;
    }
  }

  /**
   * Reads the desired values of the ICCProfile.
   * 
   * @param tv the TagValue containing the array of bytes of the ICCProfile
   */
  @Override
  public void read(TagValue tv) {
    readClass(tv);
    readVersion(tv);
    readCreator(tv);
    try {
      readDescription(tv);
    } catch (NumberFormatException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    tv.clear();
    tv.add(this);
  }
}

