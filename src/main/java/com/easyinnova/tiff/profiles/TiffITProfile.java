/**
 * <h1>TiffITProfile.java</h1> 
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
 * @since 28/7/2015
 *
 */
package com.easyinnova.tiff.profiles;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;

/**
 * The Class TiffITProfile.
 */
public class TiffITProfile extends GenericProfile implements Profile {
  /**
   * Instantiates a new tiff/ep profile validation.
   *
   * @param doc the image to check
   */
  public TiffITProfile(TiffDocument doc) {
    super(doc);
  }

  /**
   * Validates that the IFD conforms the Tiff/EP standard.
   */
  @Override
  public void validate() {
    for (TiffObject o : model.getImageIfds()) {
      IFD ifd = (IFD) o;
      validateIfd(ifd);
    }
  }

  /**
   * Validate ifd.
   *
   * @param ifd the ifd
   */
  private void validateIfd(IFD ifd) {
    IfdTags metadata = ifd.getMetadata();

    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "StripOffsets", 1);
    checkRequiredTag(metadata, "RowsPerStrip", 1);
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
  }

  /**
   * Check required tag is present, and its cardinality and value is correct.
   *
   * @param metadata the metadata
   * @param tagName the name of the mandatory tag
   * @param cardinality the mandatory cardinality
   * @param possibleValues the possible tag values
   * @return true, if tag is found
   */
  private boolean checkRequiredTag(IfdTags metadata, String tagName, int cardinality,
      long[] possibleValues) {
    boolean ok = true;
    int tagid = TiffTags.getTagId(tagName);
    if (!metadata.containsTagId(tagid)) {
      validation.addError("Missing required tag for TiffEP " + tagName);
      ok = false;
    } else if (cardinality != -1 && metadata.get(tagid).getCardinality() != cardinality) {
      validation.addError("Invalid cardinality for TiffEP tag " + tagName, metadata.get(tagid)
          .getCardinality());
    } else if (cardinality == 1 && possibleValues != null) {
      long val = metadata.get(tagid).getFirstNumericValue();
      boolean contained = false;
      int i = 0;
      while (i < possibleValues.length && !contained) {
        contained = possibleValues[i] == val;
        i++;
      }
      if (!contained)
        validation.addError("Invalid value for TiffEP tag " + tagName, val);
    }
    return ok;
  }

  /**
   * Check a required tag is present.
   *
   * @param metadata the metadata
   * @param tagName the name of the mandatory tag
   * @param cardinality the mandatory cardinality
   * @return true, if tag is present
   */
  private boolean checkRequiredTag(IfdTags metadata, String tagName, int cardinality) {
    return checkRequiredTag(metadata, tagName, cardinality, null);
  }
}
