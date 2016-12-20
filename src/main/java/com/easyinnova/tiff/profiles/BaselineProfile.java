/**
 * <h1>BaselineProfile.java</h1>
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
 * @since 4/6/2015
 *
 */
package com.easyinnova.tiff.profiles;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.Tag;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.Rational;
import com.easyinnova.tiff.model.types.abstractTiffType;

import java.lang.reflect.Executable;

/**
 * Checks if the Tiff file complies with the Baseline 6.0.
 */
public class BaselineProfile extends GenericProfile implements Profile {

  /**
   * Known types of images.
   */
  public enum ImageType {
    /** Bilevel (black and white). */
    BILEVEL,
    /** Crayscale. */
    GRAYSCALE,
    /** Palette-color. */
    PALETTE,
    /** The transparency mask. */
    TRANSPARENCY_MASK,
    /** RGB. */
    RGB,
    /** CMYK. */
    CMYK,
    /** YCbCr. */
    YCbCr,
    /** CIELab. */
    CIELab,
    /** Undefined. */
    UNDEFINED
  };

  /** The image type. */
  private ImageType type;

  /** The photometric interpretation. */
  private int photometric;

  /** Is image stored in strips. */
  private boolean strips = false;

  /** Is image stored in tiles. */
  private boolean tiles = false;

  /**
   * The tag order tolerance.<br>
   * 0: No tolerance. 1: Full tolerance (no matter if tags are not in ascending order)
   * */
  private int tagOrderTolerance = 1;

  /**
   * The rows per strip tolerance.<br>
   * 0: No tolerance. 1: Full tolerance.
   * */
  private int rowsPerStripTolerance = 1;

  /**
   * Instantiates a new baseline profile.
   *
   * @param doc the tiff file
   */
  public BaselineProfile(TiffDocument doc) {
    super(doc);
    type = ImageType.UNDEFINED;
  }

  /**
   * Validates the IFD.
   */
  @Override
  public void validate() {
    int n = 0;
    try {
      for (TiffObject o : model.getImageIfds()) {
        IFD ifd = (IFD) o;
        IfdTags metadata = ifd.getMetadata();
        validateMetadata(metadata);
        checkImage(ifd, n, metadata);
        n++;
      }
    } catch (Exception ex) {

    }
  }

  /**
   * Validates that the ifd entries have correct types and cardinalities, as they are defined in the
   * JSONs tag configuration files.
   *
   * @param metadata the ifd metadata
   */
  public void validateMetadata(IfdTags metadata) {
    int prevTagId = 0;
    try {
      TiffTags.getTiffTags();
    } catch (ReadTagsIOException e) {
    }
    for (TagValue ie : metadata.getTags()) {
      if (!TiffTags.tagMap.containsKey(ie.getId())) {
        validation.addWarning("Ignoring undefined tag id " + ie.getId(), "", "Metadata");
      } else if (!TiffTags.tagTypes.containsKey(ie.getType())) {
        validation.addWarning("Ignoring unknown tag type " + ie.getType(), "", "Metadata");
      }
      else {
        Tag t = TiffTags.getTag(ie.getId());
        String stype = TiffTags.tagTypes.get(ie.getType());
        if (ie.getId() == 320) {
          // Colormap length check
          long bps = 0;
          if (metadata.containsTagId(258))
            bps = metadata.get(258).getFirstNumericValue();
          long calc = 3 * (long) Math.pow(2, bps);
          if (calc != ie.getCardinality()) {
            validation.addError("Invalid cardinality for tag "
                + TiffTags.getTag(ie.getId()).getName() + "[" + ie.getCardinality() + "]",
                "Metadata", stype);
          }
        }
        try {
          // Cardinality check
          int card = Integer.parseInt(t.getCardinality());
          if (card != ie.getCardinality())
            validation.addError("Cardinality for tag " + TiffTags.getTag(ie.getId()).getName()
                + " must be " + card, "Metadata",
                ie.getCardinality());
        } catch (Exception e) {
          // TODO: Deal with formulas?
        }
      }

      if (ie.getId() < prevTagId) {
        if (tagOrderTolerance > 0)
          validation.addWarning("Tags are not in ascending order", "", "Metadata");
        else
          validation.addErrorLoc("Tags are not in ascending order", "Metadata");
      }
      prevTagId = ie.getId();
    }
  }

  /**
   * Check if the tags that define the image are correct and consistent.
   *
   * @param ifd the ifd
   * @param n the ifd number
   * @param metadata the ifd metadata
   */
  public void checkImage(IFD ifd, int n, IfdTags metadata) {
    CheckCommonFields(ifd, n, metadata);

    if (!metadata.containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
      validation.addErrorLoc("Missing Photometric Interpretation", "IFD" + n);
    } else if (metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getValue().size() != 1) {
      validation.addErrorLoc("Invalid Photometric Interpretation", "IFD" + n);
    } else {
      photometric =
          (int) metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue();
      switch (photometric) {
        case 0:
        case 1:
          if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))
              || metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue() == 1) {
            type = ImageType.BILEVEL;
            CheckBilevelImage(metadata, n);
          } else {
            type = ImageType.GRAYSCALE;
            CheckGrayscaleImage(metadata, n);
          }
          break;
        case 2:
          type = ImageType.RGB;
          CheckRGBImage(metadata, n);
          break;
        case 3:
          type = ImageType.PALETTE;
          CheckPalleteImage(metadata, n);
          break;
        case 4:
          type = ImageType.TRANSPARENCY_MASK;
          CheckTransparencyMask(metadata, n);
          break;
        case 5:
          type = ImageType.CMYK;
          CheckCMYK(metadata, n);
          break;
        case 6:
          type = ImageType.YCbCr;
          CheckYCbCr(metadata, n);
          break;
        case 8:
        case 9:
        case 10:
          type = ImageType.CIELab;
          CheckCIELab(metadata, n);
          break;
        default:
          validation.addWarning("Unknown Photometric Interpretation", "" + photometric, "IFD" + n);
          break;
      }
    }
  }

  /**
   * Check Bilevel Image.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckBilevelImage(IfdTags metadata, int n) {
    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 2 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", "IFD" + n, comp);
  }

  /**
   * Check Grayscale Image.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckGrayscaleImage(IfdTags metadata, int n) {
    // Bits per Sample
    long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
    // if (bps != 4 && bps != 8)
    if (bps < 1)
      validation.addError("Invalid Bits per Sample", "IFD" + n, bps);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", "IFD" + n, comp);
  }

  /**
   * Check Pallete Color Image.
   *
   * @param metadata the metadata
   * @param nifd the IFD number
   */
  private void CheckPalleteImage(IfdTags metadata, int nifd) {
    // Color Map
    if (!metadata.containsTagId(TiffTags.getTagId("ColorMap"))) {
      validation.addErrorLoc("Missing Color Map", "IFD" + nifd);
    } else {
      int n = metadata.get(TiffTags.getTagId("ColorMap")).getCardinality();
      if (n != 3 * (int) Math.pow(2, metadata.get(TiffTags.getTagId("BitsPerSample"))
          .getFirstNumericValue()))
        validation.addError("Incorrect Color Map Cardinality", "IFD" + nifd, metadata.get(320)
            .getCardinality());
    }

    // Bits per Sample
    long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
    if (bps != 4 && bps != 8)
      validation.addError("Invalid Bits per Sample", "IFD" + nifd, bps);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", "IFD" + nifd, comp);
  }

  /**
   * Check transparency mask.
   *
   * @param metadata the metadata
   * @param n the ifd number
   */
  private void CheckTransparencyMask(IfdTags metadata, int n) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addErrorLoc("Missing Samples Per Pixel", "IFD" + n);
    } else {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      if (spp != 1) {
        validation.addError("Invalid Samples Per Pixel", "IFD" + n, spp);
      }
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addErrorLoc("Missing BitsPerSample", "IFD" + n);
    } else {
      long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
      if (bps != 1) {
        validation.addError("Invalid BitsPerSample", "IFD" + n, bps);
      }
    }
  }

  /**
   * Check CMYK.
   *
   * @param metadata the metadata
   * @param n the ifd number
   */
  private void CheckCMYK(IfdTags metadata, int n) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addErrorLoc("Missing Samples Per Pixel", "IFD" + n);
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addErrorLoc("Missing BitsPerSample", "IFD" + n);
    }
  }

  /**
   * Check YCbCr.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckYCbCr(IfdTags metadata, int n) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addErrorLoc("Missing Samples Per Pixel", "IFD" + n);
    } else {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      if (spp != 3) {
        validation.addError("Invalid Samples Per Pixel", "IFD" + n, spp);
      }
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addErrorLoc("Missing BitsPerSample", "IFD" + n);
    } else {
      for (abstractTiffType vi : metadata.get(TiffTags.getTagId("BitsPerSample")).getValue()) {
        if (vi.toInt() != 8) {
          validation.addError("Invalid BitsPerSample", "IFD" + n, vi.toInt());
          break;
        }
      }
    }

    // Compression
    // long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 5 && comp != 6)
    // validation.addError("Invalid Compression", comp);

    // if (!metadata.containsTagId(TiffTags.getTagId("ReferenceBlackWhite")))
    // validation.addError("Missing ReferenceBlackWhite");

    // if (!metadata.containsTagId(TiffTags.getTagId("YCbCrCoefficients")))
    // validation.addError("Missing YCbCr Coefficients");

    // if (!metadata.containsTagId(TiffTags.getTagId("YCbCrSubSampling")))
    // validation.addError("Missing YCbCr SubSampling");

    // if (!metadata.containsTagId(TiffTags.getTagId("YCbCrPositioning")))
    // validation.addError("Missing YCbCr Positioning");
  }

  /**
   * Check CIELab.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckCIELab(IfdTags metadata, int n) {
    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addErrorLoc("Missing BitsPerSample", "IFD" + n);
    } else {
      for (abstractTiffType vi : metadata.get(TiffTags.getTagId("BitsPerSample")).getValue()) {
        if (vi.toInt() != 8) {
          validation.addError("Invalid BitsPerSample", "IFD" + n, vi.toInt());
          break;
        }
      }
    }
  }

  /**
   * Check RGB Image.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckRGBImage(IfdTags metadata, int n) {
    // Samples per Pixel
    long samples = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
    if (samples < 3)
      validation.addError("Invalid Samples per Pixel", "IFD" + n, samples);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", "IFD" + n, comp);
  }

  /**
   * Check common fields.
   *
   * @param ifd the ifd
   * @param n the ifd number
   * @param metadata the ifd metadata
   */
  private void CheckCommonFields(IFD ifd, int n, IfdTags metadata) {
    int id;

    // Width tag is mandatory
    id = TiffTags.getTagId("ImageWidth");
    if (!metadata.containsTagId(id))
      validation.addError("Missing required field", "IFD" + n, TiffTags.getTag(id).getName());
    else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Height tag is mandatory
    id = TiffTags.getTagId("ImageLength");
    if (!metadata.containsTagId(id))
      validation.addError("Missing required field", "IFD" + n, TiffTags.getTag(id).getName());
    else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check Resolution Unit
    id = TiffTags.getTagId("ResolutionUnit");
    if (!metadata.containsTagId(id)) {
      // validation.addError("Missing required field", TiffTags.getTag(id).getName());
    } else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val != 1 && val != 2 && val != 3)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check XResolution
    id = TiffTags.getTagId("XResolution");
    if (!metadata.containsTagId(id)) {
      // validation.addError("Missing required field", TiffTags.getTag(id).name);
    } else {
      float val = ((Rational) metadata.get(id).getValue().get(0)).getFloatValue();
      if (val <= 0f)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check YResolution
    id = TiffTags.getTagId("YResolution");
    if (!metadata.containsTagId(id)) {
      // validation.addError("Missing required field", TiffTags.getTag(id).name);
    } else {
      float val = ((Rational) metadata.get(id).getValue().get(0)).getFloatValue();
      if (val <= 0f)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check Planar Configuration
    id = TiffTags.getTagId("PlanarConfiguration");
    if (!metadata.containsTagId(id)) {
      // validation.addError("Missing required field", TiffTags.getTag(id).name);
    } else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val != 1 && val != 2)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check Orientation
    id = TiffTags.getTagId("Orientation");
    if (!metadata.containsTagId(id)) {
      // validation.addError("Missing required field", TiffTags.getTag(id).name);
    } else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val <= 0 || val > 8)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Check whether the image is stored in tiles or strips
    strips = ifd.hasStrips();
    tiles = ifd.hasTiles();
    if (!strips && !tiles)
      validation.addErrorLoc("Missing image organization tags", "IFD" + n);
    else if (strips && tiles)
      validation.addErrorLoc("Image in both strips and tiles", "IFD" + n);
    else if (strips) {
      CheckStrips(metadata, n);
    } else if (tiles) {
      CheckTiles(ifd, metadata, n);
    }

    // Check pixel samples bits
    if (metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))
        && metadata.containsTagId(TiffTags.getTagId("SampesPerPixel"))) {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      int bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getValue().size();
      if (spp != bps) {
        validation
.addErrorLoc("Samples per Pixel and Bits per Sample count do not match", "IFD"
            + n);
        if (bps == 1) {
          // TODO: Tolerate and proceed as if the BitsPerSample tag had a count equal to the
          // SamplesPerPixel tag value, and with all values equal to the single value actually given
        }
      }

      if (metadata.containsTagId(TiffTags.getTagId("ExtraSamples"))) {
        int ext = metadata.get(TiffTags.getTagId("ExtraSamples")).getValue().size();
        if (ext + 3 != bps) {
          validation.addError("Incorrect Extra Samples Count", "IFD" + n, ext);
        } else if (ext > 0 && bps <= 3) {
          validation.addError("Unnecessary Extra Samples", "IFD" + n, ext);
        }
      }

      if (bps > 1) {
        TagValue lbps = metadata.get(TiffTags.getTagId("BitsPerSample"));
        if (lbps == null || lbps.getValue() == null) {
          validation.addErrorLoc("Invalid Bits per Sample", "IFD" + n);
        } else {
          boolean distinct_bps_samples = false;
          for (int i = 1; i < lbps.getCardinality(); i++) {
            if (lbps.getValue().get(i).toInt() != lbps.getValue().get(i - 1).toInt())
              distinct_bps_samples = true;
          }
          if (distinct_bps_samples)
            validation.addErrorLoc("Distinct Bits per Sample values", "IFD" + n);
        }
      }
    }
  }

  /**
   * Check that the strips containing the image are well-formed.
   *
   * @param metadata the metadata
   * @param n the IFD number
   */
  private void CheckStrips(IfdTags metadata, int n) {
    long offset;
    int id;

    // Strip offsets
    id = TiffTags.getTagId("StripOffsets");
    offset = metadata.get(id).getFirstNumericValue();
    int nso = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
          offset);

    // Strip Byte Counts
    id = TiffTags.getTagId("StripBYTECount");
    offset = metadata.get(id).getFirstNumericValue();
    int nsc = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
          offset);

    if (nso != nsc) {
      validation.addErrorLoc("Inconsistent strip lengths", "IFD" + n);
    }

    int pixelSize = 0;
    for (int i = 0; i < metadata.get("BitsPerSample").getCardinality(); i++) {
      pixelSize += metadata.get("BitsPerSample").getValue().get(i).toInt();
    }
    if (metadata.get("Compression").getFirstNumericValue() == 1
 && pixelSize >= 8) {
      int calculatedImageLength = 0;
      for (int i = 0; i < nsc; i++) {
        calculatedImageLength += metadata.get(id).getValue().get(i).toInt();
      }
      if (calculatedImageLength != metadata.get("ImageLength").getFirstNumericValue()
          * metadata.get("ImageWidth").getFirstNumericValue() * pixelSize / 8) {
        // validation.toString();
        validation.addErrorLoc("Calculated and declared image size do not match", "IFD" + n);
      }
    }

    // Rows per Strip
    id = TiffTags.getTagId("RowsPerStrip");
    if (!metadata.containsTagId(id)) {
      if (rowsPerStripTolerance > 0)
        validation.addWarning("Missing required field", TiffTags.getTag(id).getName(), "IFD" + n);
      else
        validation.addError("Missing required field", "IFD" + n, TiffTags.getTag(id).getName());
    } else {
      offset = metadata.get(id).getFirstNumericValue();
      if (offset <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            offset);
    }
  }

  /**
   * Check that the tiles containing the image are well-formed.
   *
   * @param ifd the ifd
   * @param metadata the metadata
   * @param n the ifd number
   */
  private void CheckTiles(IFD ifd, IfdTags metadata, int n) {
    long offset;
    int id;

    // Check Tile Offsets
    id = TiffTags.getTagId("TileOffsets");
    offset = metadata.get(id).getFirstNumericValue();
    int no = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
          offset);

    // Check Tile Byte Counts
    id = TiffTags.getTagId("TileBYTECounts");
    offset = metadata.get(id).getFirstNumericValue();
    int nc = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
          offset);

    if (no != nc) {
      validation.addErrorLoc("Inconsistent tile lengths", "IFD" + n);
    }

    // Check Tile Width
    long tileWidth = 0;
    id = TiffTags.getTagId("TileWidth");
    if (!metadata.containsTagId(id))
      validation.addErrorLoc("Missing required field for tiles " + TiffTags.getTag(id).getName(),
          "IFD" + n);
    else {
      tileWidth = metadata.get(id).getFirstNumericValue();
      if (tileWidth <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            tileWidth);
    }

    // Check Tile Length
    id = TiffTags.getTagId("TileLength");
    long tileLength = 0;
    if (!metadata.containsTagId(id))
      validation.addErrorLoc("Missing required field for tiles " + TiffTags.getTag(id).getName(),
          "IFD" + n);
    else {
      tileLength = metadata.get(id).getFirstNumericValue();
      if (tileLength <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            tileLength);
    }

    long tilesPerImage =
        ((metadata.get(TiffTags.getTagId("ImageWidth")).getFirstNumericValue() + tileWidth - 1) / tileWidth)
            * ((metadata.get(TiffTags.getTagId("ImageLength")).getFirstNumericValue() + tileLength - 1) / tileLength);

    // Check Plannar Configuration
    id = TiffTags.getTagId("PlanarConfiguration");
    int idspp = TiffTags.getTagId("SamplesPerPixel");
    if (metadata.containsTagId(id) && metadata.containsTagId(idspp)) {
      long planar = metadata.get(id).getFirstNumericValue();
      long spp = metadata.get(idspp).getFirstNumericValue();
      if (planar == 2) {
        long spp_tpi = spp * tilesPerImage;
        if (ifd.getImageTiles().getTiles().size() < spp_tpi) {
          validation.addErrorLoc("Insufficient tiles", "IFD" + n);
        }
      }
    }
  }

  /**
   * Gets the image type.
   *
   * @return the type
   */
  public ImageType getType() {
    return type;
  }
}

