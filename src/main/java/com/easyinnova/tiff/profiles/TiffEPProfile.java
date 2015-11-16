/**
 * <h1>TiffEPProfile.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 17/6/2015
 */
package com.easyinnova.tiff.profiles;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TiffDocument;
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
    IFD ifd = model.getFirstIFD();
    int n = 0;
    while (ifd != null) {
      validateIfd(ifd, n);
      if (ifd.hasSubIFD()) {
        validateSubIfd(ifd.getsubIFD(), n);
      }
      ifd = ifd.getNextIFD();
      n++;
    }
  }

  /**
   * Validate ifd.
   *
   * @param ifd the ifd
   * @param n the ifd number
   */
  private void validateIfd(IFD ifd, int n) {
    boolean thumbnail = ifd.hasSubIFD() && ifd.getsubIFD().getImageSize() > ifd.getImageSize();
    IfdTags metadata = ifd.getMetadata();

    checkRequiredTag(metadata, "ImageLength", 1, "IFD" + n);
    checkRequiredTag(metadata, "ImageWidth", 1, "IFD" + n);
    int spp = -1;
    if (checkRequiredTag(metadata, "SamplesPerPixel", 1, "IFD" + n)) {
      spp = (int) metadata.get("SamplesPerPixel").getFirstNumericValue();
      checkRequiredTag(metadata, "BitsPerSample", spp, "IFD" + n);
    }
    if (n == 0)
      checkRequiredTag(metadata, "ImageDescription", -1, "IFD" + n);
    if (checkRequiredTag(metadata, "Compression", 1, "IFD" + n)) {
      int comp = (int) metadata.get("Compression").getFirstNumericValue();
      if (comp == 1)
        checkForbiddenTag(metadata, "CompressedBitsPerPixel", "IFD" + n);
    }
    checkRequiredTag(metadata, "XResolution", 1, "IFD" + n);
    checkRequiredTag(metadata, "YResolution", 1, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "Make", -1, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "Model", -1, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "Software", -1, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "Copyright", -1, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "DateTimeOriginal", 20, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "DateTime", 20, "IFD" + n);
    if (n == 0)
      checkRequiredTag(metadata, "TIFFEPStandardID", 4, "IFD" + n);
    if (checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {0, 1}, "IFD" + n)) {
      if (thumbnail)
        checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {1}, "IFD" + n);
      else
        checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {0}, "IFD" + n);
      int nst = (int) metadata.get("NewSubfileType").getFirstNumericValue();
      if (nst != 0) {
        if (n == 0)
          checkRequiredTag(metadata, "SubIFDs", -1, "IFD" + n);
      }
    }
    if (checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[]{1, 2, 6, 32803,
        32767}, "IFD" + n)) {
      int photo = (int) metadata.get("PhotometricInterpretation").getFirstNumericValue();
      if (photo != 6) {
        checkForbiddenTag(metadata, "YCbCrCoefficients", "IFD" + n);
        checkForbiddenTag(metadata, "YCbCrSubSampling", "IFD" + n);
        checkForbiddenTag(metadata, "YCbCrPositioning", "IFD" + n);
        checkForbiddenTag(metadata, "ReferenceBlackWhite", "IFD" + n);
      }
      if (photo == 2 || photo == 3) {
        if (spp != 3) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", "IFD" + n, spp);
        }
      } else if (photo == 1 || photo == 32803) {
        if (spp != 1) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", "IFD" + n, spp);
        }
        if (photo == 32803) {
          checkRequiredTag(metadata, "CFARepeatPatternDim", 2, "IFD" + n);
          checkRequiredTag(metadata, "CFAPattern", -1, "IFD" + n);
        }
      }
    }
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[] {1, 2}, "IFD" + n);
    checkRequiredTag(metadata, "ResolutionUnit", 1, new long[] {1, 2, 3}, "IFD" + n);
    if (metadata.containsTagId(TiffTags.getTagId("Orientation")))
      checkRequiredTag(metadata, "Orientation", 1, new long[] {1, 3, 6, 8, 9}, "IFD" + n);

    if (!thumbnail) {
      if (ifd.hasStrips()) {
        checkRequiredTag(metadata, "StripBYTECount", -1, "IFD" + n);
        checkRequiredTag(metadata, "StripOffsets", -1, "IFD" + n);
        checkRequiredTag(metadata, "RowsPerStrip", 1, "IFD" + n);
        if (ifd.hasTiles()) {
          validation.addErrorLoc("Image in both strips and tiles", "IFD" + n);
        }
      } else if (ifd.hasTiles()) {
        checkRequiredTag(metadata, "TileLength", 1, "IFD" + n);
        checkRequiredTag(metadata, "TileOffsets", 1, "IFD" + n);
        checkRequiredTag(metadata, "TileWidth", -1, "IFD" + n);
      }
    } else {
      checkRequiredTag(metadata, "StripBYTECount", -1, "IFD" + n);
      checkRequiredTag(metadata, "StripOffsets", -1, "IFD" + n);
      checkRequiredTag(metadata, "RowsPerStrip", 1, "IFD" + n);
      checkForbiddenTag(metadata, "TileLength", "IFD" + n);
      checkForbiddenTag(metadata, "TileOffsets", "IFD" + n);
      checkForbiddenTag(metadata, "TileWidth", "IFD" + n);
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
      checkRequiredTag(metadata, "YCbCrCoefficients", 3, "IFD" + n);
      checkRequiredTag(metadata, "YCbCrSubSampling", 2, "IFD" + n);
      checkRequiredTag(metadata, "YCbCrPositioning", 1, "IFD" + n);
      checkRequiredTag(metadata, "ReferenceBlackWhite", 6, "IFD" + n);
    }
    if (thumbnail) {
      checkForbiddenTag(metadata, "YCbCrCoefficients", "IFD" + n);
      checkForbiddenTag(metadata, "YCbCrSubSampling", "IFD" + n);
      checkForbiddenTag(metadata, "YCbCrPositioning", "IFD" + n);
      checkForbiddenTag(metadata, "ReferenceBlackWhite", "IFD" + n);
    }

    checkForbiddenTag(metadata, "PrimaryChromaticities", "IFD" + n);
    checkForbiddenTag(metadata, "WhitePoint", "IFD" + n);
    checkForbiddenTag(metadata, "TransferFunction", "IFD" + n);

    if (metadata.containsTagId(TiffTags.getTagId("FocalPlaneResolutionUnit"))) {
      int focal = (int) metadata.get("FocalPlaneResolutionUnit").getFirstNumericValue();
      if (focal < 1 || focal > 5)
        validation.addErrorLoc("Invalid value fot TiffEP tag FocalPlaneResolutionUnit", "IFD" + n);
    }

    if (metadata.containsTagId(TiffTags.getTagId("SensingMethod"))) {
      int sensing = (int) metadata.get("SensingMethod").getFirstNumericValue();
      if (sensing < 0 || sensing > 8)
        validation.addErrorLoc("Invalid value fot TiffEP tag SensingMethod", "IFD" + n);
    }
  }

  /**
   * Validate sub ifd.
   *
   * @param ifd the subifd
   * @param n the ifd number
   */
  private void validateSubIfd(IFD ifd, int n) {
    boolean thumbnail = ifd.getParent().getImageSize() > ifd.getImageSize();
    IfdTags metadata = ifd.getMetadata();

    checkRequiredTag(metadata, "ImageLength", 1, "SubIFD" + n);
    checkRequiredTag(metadata, "ImageWidth", 1, "SubIFD" + n);
    int spp = -1;
    if (checkRequiredTag(metadata, "SamplesPerPixel", 1, "SubIFD" + n)) {
      spp = (int) metadata.get("SamplesPerPixel").getFirstNumericValue();
      checkRequiredTag(metadata, "BitsPerSample", spp, "SubIFD" + n);
    }
    if (checkRequiredTag(metadata, "Compression", 1, "SubIFD" + n)) {
      int comp = (int) metadata.get("Compression").getFirstNumericValue();
      if (comp == 1)
        checkForbiddenTag(metadata, "CompressedBitsPerPixel", "IFD" + n);
    }
    checkRequiredTag(metadata, "XResolution", 1, "SubIFD" + n);
    checkRequiredTag(metadata, "YResolution", 1, "SubIFD" + n);
    checkForbiddenTag(metadata, "SubIFDs", "IFD" + n);
    if (thumbnail)
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {1}, "SubIFD" + n);
    else
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[] {0}, "SubIFD" + n);
    if (checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {1, 2, 6, 32803,
        32767}, "SubIFD" + n)) {
      int photo = (int) metadata.get("PhotometricInterpretation").getFirstNumericValue();
      if (photo != 6) {
        checkForbiddenTag(metadata, "YCbCrCoefficients", "SubIFD" + n);
        checkForbiddenTag(metadata, "YCbCrSubSampling", "SubIFD" + n);
        checkForbiddenTag(metadata, "YCbCrPositioning", "SubIFD" + n);
        checkForbiddenTag(metadata, "ReferenceBlackWhite", "SubIFD" + n);
      }
      if (photo == 2 || photo == 3) {
        if (spp != 3) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", "SubIFD" + n, spp);
        }
      } else if (photo == 1 || photo == 32803) {
        if (spp != 1) {
          validation.addError("Invalid SampesPerPixel value fo TiffEP", "SubIFD" + n, spp);
        }
        if (photo == 32803) {
          checkRequiredTag(metadata, "CFARepeatPatternDim", 2, "SubIFD" + n);
          checkRequiredTag(metadata, "CFAPattern", -1, "SubIFD" + n);
        }
      }
    }
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[] {1, 2}, "SubIFD" + n);
    checkRequiredTag(metadata, "ResolutionUnit", 1, new long[] {1, 2, 3}, "SubIFD" + n);

    if (!thumbnail) {
      if (ifd.hasStrips()) {
        checkRequiredTag(metadata, "StripBYTECount", -1, "SubIFD" + n);
        checkRequiredTag(metadata, "StripOffsets", -1, "SubIFD" + n);
        checkRequiredTag(metadata, "RowsPerStrip", 1, "SubIFD" + n);
        if (ifd.hasTiles()) {
          validation.addErrorLoc("Image in both strips and tiles", "SubIFD");
        }
      } else if (ifd.hasTiles()) {
        checkRequiredTag(metadata, "TileLength", 1, "SubIFD" + n);
        checkRequiredTag(metadata, "TileOffsets", 1, "SubIFD" + n);
        checkRequiredTag(metadata, "TileWidth", -1, "SubIFD" + n);
      }
    } else {
      checkRequiredTag(metadata, "StripBYTECount", -1, "SubIFD" + n);
      checkRequiredTag(metadata, "StripOffsets", -1, "SubIFD" + n);
      checkRequiredTag(metadata, "RowsPerStrip", 1, "SubIFD" + n);
      checkForbiddenTag(metadata, "TileLength", "SubIFD" + n);
      checkForbiddenTag(metadata, "TileOffsets", "SubIFD" + n);
      checkForbiddenTag(metadata, "TileWidth", "SubIFD" + n);
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
      checkRequiredTag(metadata, "YCbCrCoefficients", 3, "SubIFD" + n);
      checkRequiredTag(metadata, "YCbCrSubSampling", 2, "SubIFD" + n);
      checkRequiredTag(metadata, "YCbCrPositioning", 1, "SubIFD" + n);
      checkRequiredTag(metadata, "ReferenceBlackWhite", 6, "SubIFD" + n);
    }
    if (thumbnail) {
      checkForbiddenTag(metadata, "YCbCrCoefficients", "IFD" + n);
      checkForbiddenTag(metadata, "YCbCrSubSampling", "IFD" + n);
      checkForbiddenTag(metadata, "YCbCrPositioning", "IFD" + n);
      checkForbiddenTag(metadata, "ReferenceBlackWhite", "IFD" + n);
    }

    checkForbiddenTag(metadata, "PrimaryChromaticities", "IFD" + n);
    checkForbiddenTag(metadata, "WhitePoint", "IFD" + n);
    checkForbiddenTag(metadata, "TransferFunction", "IFD" + n);
  }

  /**
   * Check required tag is present, and its cardinality and value is correct.
   *
   * @param metadata the metadata
   * @param tagName the name of the mandatory tag
   * @param cardinality the mandatory cardinality
   * @param possibleValues the possible tag values
   * @param ext string extension
   * @return true, if tag is found
   */
  private boolean checkRequiredTag(IfdTags metadata, String tagName, int cardinality,
      long[] possibleValues, String ext) {
    boolean ok = true;
    int tagid = TiffTags.getTagId(tagName);
    if (!metadata.containsTagId(tagid)) {
      validation.addErrorLoc("Missing required tag for TiffEP " + tagName, ext);
      ok = false;
    } else if (cardinality != -1 && metadata.get(tagid).getCardinality() != cardinality) {
      validation.addError("Invalid cardinality for TiffEP tag " + tagName, ext, metadata
          .get(tagid)
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
        validation.addError("Invalid value for TiffEP tag " + tagName, ext, val);
    }
    return ok;
  }

  /**
   * Check a required tag is present.
   *
   * @param metadata the metadata
   * @param tagName the name of the mandatory tag
   * @param cardinality the mandatory cardinality
   * @param ext string extension
   * @return true, if tag is present
   */
  private boolean checkRequiredTag(IfdTags metadata, String tagName, int cardinality, String ext) {
    return checkRequiredTag(metadata, tagName, cardinality, null, ext);
  }

  /**
   * Check a forbidden tag is not present.
   *
   * @param metadata the metadata
   * @param tagName the tag name
   * @param ext string extension
   */
  private void checkForbiddenTag(IfdTags metadata, String tagName, String ext) {
    int tagid = TiffTags.getTagId(tagName);
    if (metadata.containsTagId(tagid)) {
      validation.addErrorLoc("Forbidden tag for TiffEP found " + tagName, ext);
    }
  }
}

