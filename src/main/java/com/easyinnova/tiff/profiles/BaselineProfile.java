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
    for (TiffObject o : model.getImageIfds()) {
      IFD ifd = (IFD) o;
      IfdTags metadata = ifd.getMetadata();
      validateMetadata(metadata);
      checkImage(ifd, n, metadata);
      n++;
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
        validation.addWarning("Ignoring undefined tag id " + ie.getId());
      } else if (!TiffTags.tagTypes.containsKey(ie.getType())) {
        validation.addWarning("Ignoring unknown tag type " + ie.getType());
      }
      else {
        Tag t = TiffTags.getTag(ie.getId());
        String stype = TiffTags.tagTypes.get(ie.getType());
        if (!t.validType(stype)) {
          String stypes = "";
          for (String tt : t.getType()) {
            if (stypes.length() > 0)
              stypes += ",";
            stypes += tt;
          }
          validation.addError("Invalid type for tag " + TiffTags.getTag(ie.getId()).getName() + "["
              + stypes + "]", stype);
        }
        try {
          int card = Integer.parseInt(t.getCardinality());
          if (card != ie.getCardinality())
            validation.addError("Cardinality for tag " + TiffTags.getTag(ie.getId()).getName()
                + " must be " + card,
                ie.getCardinality());
        } catch (Exception e) {
          // TODO: Deal with formulas?
        }
      }

      if (ie.getId() < prevTagId) {
        if (tagOrderTolerance > 0)
          validation.addWarning("Tags are not in ascending order");
        else
          validation.addError("Tags are not in ascending order");
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
      validation.addError("Missing Photometric Interpretation");
    } else if (metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getValue().size() != 1) {
      validation.addError("Invalid Photometric Interpretation");
    } else {
      photometric =
          (int) metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue();
      switch (photometric) {
        case 0:
        case 1:
          if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))
              || metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue() == 1) {
            type = ImageType.BILEVEL;
            CheckBilevelImage(metadata);
          } else {
            type = ImageType.GRAYSCALE;
            CheckGrayscaleImage(metadata);
          }
          break;
        case 2:
          type = ImageType.RGB;
          CheckRGBImage(metadata);
          break;
        case 3:
          type = ImageType.PALETTE;
          CheckPalleteImage(metadata);
          break;
        case 4:
          type = ImageType.TRANSPARENCY_MASK;
          CheckTransparencyMask(metadata);
          break;
        case 5:
          type = ImageType.CMYK;
          CheckCMYK(metadata);
          break;
        case 6:
          type = ImageType.YCbCr;
          CheckYCbCr(metadata);
          break;
        case 8:
        case 9:
        case 10:
          type = ImageType.CIELab;
          CheckCIELab(metadata);
          break;
        default:
          validation.addWarning("Unknown Photometric Interpretation", photometric);
          break;
      }
    }
  }

  /**
   * Check Bilevel Image.
   *
   * @param metadata the metadata
   */
  private void CheckBilevelImage(IfdTags metadata) {
    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 2 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", comp);
  }

  /**
   * Check Grayscale Image.
   *
   * @param metadata the metadata
   */
  private void CheckGrayscaleImage(IfdTags metadata) {
    // Bits per Sample
    long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
    // if (bps != 4 && bps != 8)
    if (bps < 1)
      validation.addError("Invalid Bits per Sample", bps);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", comp);
  }

  /**
   * Check Pallete Color Image.
   *
   * @param metadata the metadata
   */
  private void CheckPalleteImage(IfdTags metadata) {
    // Color Map
    if (!metadata.containsTagId(TiffTags.getTagId("ColorMap"))) {
      validation.addError("Missing Color Map");
    } else {
      int n = metadata.get(TiffTags.getTagId("ColorMap")).getCardinality();
      if (n != 3 * (int) Math.pow(2, metadata.get(TiffTags.getTagId("BitsPerSample"))
          .getFirstNumericValue()))
        validation.addError("Incorrect Color Map Cardinality", metadata.get(320).getCardinality());
    }

    // Bits per Sample
    long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
    if (bps != 4 && bps != 8)
      validation.addError("Invalid Bits per Sample", bps);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", comp);
  }

  /**
   * Check transparency mask.
   *
   * @param metadata the metadata
   */
  private void CheckTransparencyMask(IfdTags metadata) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addError("Missing Samples Per Pixel");
    } else {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      if (spp != 1) {
        validation.addError("Invalid Samples Per Pixel", spp);
      }
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addError("Missing BitsPerSample");
    } else {
      long bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
      if (bps != 1) {
        validation.addError("Invalid BitsPerSample", bps);
      }
    }
  }

  /**
   * Check CMYK.
   *
   * @param metadata the metadata
   */
  private void CheckCMYK(IfdTags metadata) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addError("Missing Samples Per Pixel");
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addError("Missing BitsPerSample");
    }
  }

  /**
   * Check YCbCr.
   *
   * @param metadata the metadata
   */
  private void CheckYCbCr(IfdTags metadata) {
    // Samples per pixel
    if (!metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      validation.addError("Missing Samples Per Pixel");
    } else {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      if (spp != 3) {
        validation.addError("Invalid Samples Per Pixel", spp);
      }
    }

    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addError("Missing BitsPerSample");
    } else {
      for (abstractTiffType vi : metadata.get(TiffTags.getTagId("BitsPerSample")).getValue()) {
        if (vi.toInt() != 8) {
          validation.addError("Invalid BitsPerSample", vi.toInt());
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
   */
  private void CheckCIELab(IfdTags metadata) {
    // BitsPerSample
    if (!metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
      validation.addError("Missing BitsPerSample");
    } else {
      for (abstractTiffType vi : metadata.get(TiffTags.getTagId("BitsPerSample")).getValue()) {
        if (vi.toInt() != 8) {
          validation.addError("Invalid BitsPerSample", vi.toInt());
          break;
        }
      }
    }
  }

  /**
   * Check RGB Image.
   *
   * @param metadata the metadata
   */
  private void CheckRGBImage(IfdTags metadata) {
    // Samples per Pixel
    long samples = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
    if (samples < 3)
      validation.addError("Invalid Samples per Pixel", samples);

    // Compression
    long comp = metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
    // if (comp != 1 && comp != 32773)
    if (comp < 1)
      validation.addError("Invalid Compression", comp);
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
      validation.addError("Missing required field", TiffTags.getTag(id).getName());
    else {
      long val = metadata.get(id).getFirstNumericValue();
      if (val <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), "IFD" + n,
            val);
    }

    // Height tag is mandatory
    id = TiffTags.getTagId("ImageLength");
    if (!metadata.containsTagId(id))
      validation.addError("Missing required field", TiffTags.getTag(id).getName());
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
      validation.addError("Missing image organization tags");
    else if (strips && tiles)
      validation.addError("Image in both strips and tiles");
    else if (strips) {
      CheckStrips(metadata);
    } else if (tiles) {
      CheckTiles(ifd, metadata);
    }

    // Check pixel samples bits
    if (metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))
        && metadata.containsTagId(TiffTags.getTagId("SampesPerPixel"))) {
      long spp = metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      int bps = metadata.get(TiffTags.getTagId("BitsPerSample")).getValue().size();
      if (spp != bps) {
        validation.addError("Sampes per Pixel and Bits per Sample count do not match");
        if (bps == 1) {
          // TODO: Tolerate and proceed as if the BitsPerSample tag had a count equal to the
          // SamplesPerPixel tag value, and with all values equal to the single value actually given
        }
      }

      if (metadata.containsTagId(TiffTags.getTagId("ExtraSamples"))) {
        int ext = metadata.get(TiffTags.getTagId("ExtraSamples")).getValue().size();
        if (ext + 3 != bps) {
          validation.addError("Incorrect Extra Samples Count", ext);
        } else if (ext > 0 && bps <= 3) {
          validation.addError("Unnecessary Extra Samples", ext);
        }
      }

      if (bps > 1) {
        TagValue lbps = metadata.get(TiffTags.getTagId("BitsPerSample"));
        if (lbps == null || lbps.getValue() == null) {
          validation.addError("Invalid Bits per Sample");
        } else {
          boolean distinct_bps_samples = false;
          for (int i = 1; i < lbps.getCardinality(); i++) {
            if (lbps.getValue().get(i).toInt() != lbps.getValue().get(i - 1).toInt())
              distinct_bps_samples = true;
          }
          if (distinct_bps_samples)
            validation.addError("Distinct Bits per Sample values");
        }
      }
    }
  }

  /**
   * Check that the strips containing the image are well-formed.
   *
   * @param metadata the metadata
   */
  private void CheckStrips(IfdTags metadata) {
    long offset;
    int id;

    // Strip offsets
    id = TiffTags.getTagId("StripOffsets");
    offset = metadata.get(id).getFirstNumericValue();
    int nso = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), offset);

    // Strip Byte Counts
    id = TiffTags.getTagId("StripBYTECount");
    offset = metadata.get(id).getFirstNumericValue();
    int nsc = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), offset);

    if (nso != nsc) {
      validation.addError("Inconsistent strip lengths");
    }

    // Rows per Strip
    id = TiffTags.getTagId("RowsPerStrip");
    if (!metadata.containsTagId(id)) {
      if (rowsPerStripTolerance > 0)
        validation.addWarning("Missing required field", TiffTags.getTag(id).getName());
      else
        validation.addError("Missing required field", TiffTags.getTag(id).getName());
    } else {
      offset = metadata.get(id).getFirstNumericValue();
      if (offset <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), offset);
    }
  }

  /**
   * Check that the tiles containing the image are well-formed.
   *
   * @param metadata the metadata
   * @param ifd the ifd
   */
  private void CheckTiles(IFD ifd, IfdTags metadata) {
    long offset;
    int id;

    // Check Tile Offsets
    id = TiffTags.getTagId("TileOffsets");
    offset = metadata.get(id).getFirstNumericValue();
    int no = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), offset);

    // Check Tile Byte Counts
    id = TiffTags.getTagId("TileBYTECounts");
    offset = metadata.get(id).getFirstNumericValue();
    int nc = metadata.get(id).getCardinality();
    if (offset <= 0)
      validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), offset);

    if (no != nc) {
      validation.addError("Inconsistent tile lengths");
    }

    // Check Tile Width
    long tileWidth = 0;
    id = TiffTags.getTagId("TileWidth");
    if (!metadata.containsTagId(id))
      validation.addError("Missing required field for tiles " + TiffTags.getTag(id).getName());
    else {
      tileWidth = metadata.get(id).getFirstNumericValue();
      if (tileWidth <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), tileWidth);
    }

    // Check Tile Length
    id = TiffTags.getTagId("TileLength");
    long tileLength = 0;
    if (!metadata.containsTagId(id))
      validation.addError("Missing required field for tiles " + TiffTags.getTag(id).getName());
    else {
      tileLength = metadata.get(id).getFirstNumericValue();
      if (tileLength <= 0)
        validation.addError("Invalid value for field " + TiffTags.getTag(id).getName(), tileLength);
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
          validation.addError("Insufficient tiles");
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

