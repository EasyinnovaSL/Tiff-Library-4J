/**
 * <h1>TiffEPProfile.java</h1> 
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
 * @since 17/6/2015
 *
 */
package com.easyinnova.tiff.profiles;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;

/**
 * The Class TiffEPProfile.
 */
public class TiffEPProfile extends GenericProfile implements Profile {

  /**
   * Instantiates a new tiff/ep profile validation.
   *
   * @param doc the image to check
   */
  public TiffEPProfile(TiffDocument doc) {
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
    int spp = -1;
    if (checkRequiredTag(metadata, "SamplesPerPixel", 1)) {
      spp = (int) metadata.get("SamplesPerPixel").getFirstNumericValue();
      checkRequiredTag(metadata, "BitsPerSample", spp);
    }
    checkRequiredTag(metadata, "ImageDescription", -1);
    if (checkRequiredTag(metadata, "Compression", 1)) {
      int comp = (int) metadata.get("Compression").getFirstNumericValue();
      if (comp == 1)
        checkForbiddenTag(metadata, "CompressedBitsPerPixel");
    }
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    checkRequiredTag(metadata, "Make", -1);
    checkRequiredTag(metadata, "Model", -1);
    checkRequiredTag(metadata, "Software", -1);
    checkRequiredTag(metadata, "Copyright", -1);
    checkRequiredTag(metadata, "DateTimeOriginal", 20);
    checkRequiredTag(metadata, "DateTime", 20);
    checkRequiredTag(metadata, "TIFFEPStandardID", 4);
    if (checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {0, 1})) {
      int nst = (int) metadata.get("NewSubfileType").getFirstNumericValue();
      if (nst != 0) {
        checkRequiredTag(metadata, "SubIFDs", -1);
      }
    }
    if (checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {1, 2, 6, 32803,
        32767})) {
      int photo = (int) metadata.get("PhotometricInterpretation").getFirstNumericValue();
      if (photo == 6) {
        checkForbiddenTag(metadata, "YCbCrCoefficients");
        checkForbiddenTag(metadata, "YCbCrSubSampling");
        checkForbiddenTag(metadata, "YCbCrPositioning");
        checkForbiddenTag(metadata, "ReferenceBlackWhite");
      } else if (photo == 2 || photo == 3) {
        if (spp != 3) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", spp);
        }
      } else if (photo == 1 || photo == 32803) {
        if (spp != 1) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", spp);
        }
        if (photo == 32803) {
          checkRequiredTag(metadata, "CFARepeatPatternDim", 2);
          checkRequiredTag(metadata, "CFAPattern", -1);
        }
      }
    }
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[] {1, 2});
    checkRequiredTag(metadata, "ResolutionUnit", 1, new long[] {1, 2, 3});
    if (metadata.containsTagId(TiffTags.getTagId("Orientation")))
      checkRequiredTag(metadata, "Orientation", 1, new long[] {1, 3, 6, 8, 9});

    if (ifd.hasStrips()) {
      checkRequiredTag(metadata, "StripBYTECount", -1);
      checkRequiredTag(metadata, "StripOffsets", -1);
      checkRequiredTag(metadata, "RowsPerStrip", 1);
      if (ifd.hasTiles()) {
        validation.addError("Image in both strips and tiles");
      }
    } else if (ifd.hasTiles()) {
      checkRequiredTag(metadata, "TileLength", 1);
      checkRequiredTag(metadata, "TileOffsets", 1);
      checkRequiredTag(metadata, "TileWidth", -1);
    }

    int nycbcr = 0;
    if (metadata.containsTagId(TiffTags.getTagId("YCbCrCoefficients")))
      nycbcr++;
    if (metadata.containsTagId(TiffTags.getTagId("YCbCrSubSampling")))
      nycbcr++;
    if (metadata.containsTagId(TiffTags.getTagId("YCbCrPositioning")))
      nycbcr++;
    if (metadata.containsTagId(TiffTags.getTagId("ReferenceBlackWhite")))
      nycbcr++;
    if (nycbcr > 0 && nycbcr != 4) {
      checkRequiredTag(metadata, "YCbCrCoefficients", 3);
      checkRequiredTag(metadata, "YCbCrSubSampling", 2);
      checkRequiredTag(metadata, "YCbCrPositioning", 1);
      checkRequiredTag(metadata, "ReferenceBlackWhite", 6);
    }

    checkForbiddenTag(metadata, "PrimaryChromaticities");
    checkForbiddenTag(metadata, "WhitePoint");
    checkForbiddenTag(metadata, "TransferFunction");

    if (metadata.containsTagId(TiffTags.getTagId("FocalPlaneResolutionUnit"))) {
      int focal = (int) metadata.get("FocalPlaneResolutionUnit").getFirstNumericValue();
      if (focal < 1 || focal > 5)
        validation.addError("Invalid value fot TiffEP tag FocalPlaneResolutionUnit");
    }

    if (metadata.containsTagId(TiffTags.getTagId("SensingMethod"))) {
      int sensing = (int) metadata.get("SensingMethod").getFirstNumericValue();
      if (sensing < 0 || sensing > 8)
        validation.addError("Invalid value fot TiffEP tag SensingMethod");
    }
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

  /**
   * Check a forbidden tag is not present.
   *
   * @param metadata the metadata
   * @param tagName the tag name
   */
  private void checkForbiddenTag(IfdTags metadata, String tagName) {
    int tagid = TiffTags.getTagId(tagName);
    if (metadata.containsTagId(tagid)) {
      validation.addError("Forbidden tag for TiffEP found " + tagName);
    }
  }
}

