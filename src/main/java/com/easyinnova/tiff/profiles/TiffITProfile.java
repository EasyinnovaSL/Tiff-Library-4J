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

  /** The profile. */
  private int profile;

  /** The current ifd. */
  private int currentIfd;

  /**
   * Instantiates a new tiff/ep profile validation.
   *
   * @param doc the image to check
   * @param profile the TiffIT profile (0: default, 1: P1, 2: P2)
   */
  public TiffITProfile(TiffDocument doc, int profile) {
    super(doc);
    this.profile = profile;
  }

  /**
   * Validates that the IFD conforms the Tiff/IT standard.
   */
  @Override
  public void validate() {
    currentIfd = 0;
    for (TiffObject o : model.getImageIfds()) {
      currentIfd++;
      IFD ifd = (IFD) o;
      IfdTags metadata = ifd.getMetadata();
      int sft = -1;
      int photo = -1;
      int bps = -1;
      int planar = -1;
      int comp = -1;
      if (metadata.containsTagId(TiffTags.getTagId("SubfileType"))) {
        sft = (int)metadata.get(TiffTags.getTagId("SubfileType")).getFirstNumericValue();
      }
      if (metadata.containsTagId(TiffTags.getTagId("Compression"))) {
        comp = (int)metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue();
      }
      if (metadata.containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
        photo = (int)metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue();
      }
      if (metadata.containsTagId(TiffTags.getTagId("BitsPerSample"))) {
        bps = (int)metadata.get(TiffTags.getTagId("BitsPerSample")).getFirstNumericValue();
      }
      if (metadata.containsTagId(TiffTags.getTagId("PlanarConfiguration"))) {
        planar = (int)metadata.get(TiffTags.getTagId("PlanarConfiguration")).getFirstNumericValue();
      }

      int p = profile;

      // Determination of TIFF/IT file type
      if (sft == 1 || sft == -1) {
        if (comp == 1 || comp == 32895) {
          if (photo == 5) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              if (bps > 1) {
                validateIfdCT(ifd, p);
              } else if (bps == 1) {
                validateIfdSD(ifd, p);
              }
            }
          } else if (photo == 2) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 8) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 0 || photo == 1) {
            if (bps == 1) {
              validateIfdBP(ifd, p);
            } else if (bps > 1) {
              validateIfdMP(ifd, p);
            }
          }
        } else if (comp == 4) {
          if (photo == 0 || photo == 1) {
            validateIfdBP(ifd, p);
          } else if (photo == 5) {
            validateIfdSD(ifd, p);
          }
        } else if (comp == 7) {
          if (photo == 5) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 2) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 6) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 8) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 0 || photo == 1) {
            if (bps > 1) {
              validateIfdMP(ifd, p);
            }
          }
        } else if (comp == 8) {
          if (photo == 5) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              if (bps > 1) {
                validateIfdCT(ifd, p);
              } else if (bps == 1) {
                validateIfdSD(ifd, p);
              }
            }
          } else if (photo == 2) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 8) {
            if (planar == 1) {
              validateIfdCT(ifd, p);
            } else if (planar == 32768) {
              validateIfdCT(ifd, p);
            } else if (planar == 2) {
              validateIfdCT(ifd, p);
            }
          } else if (photo == 0 || photo == 1) {
            if (bps == 1) {
              validateIfdBP(ifd, p);
            } else if (bps > 1) {
              validateIfdMP(ifd, p);
            }
          }
        } else if (comp == 32896) {
          validateIfdLW(ifd, p);
        } else if (comp == 32897) {
          validateIfdHC(ifd, p);
        } else if (comp == 32898) {
          validateIfdBL(ifd, p);
        } else if (((sft >> 3) & 1) == 1) {
          validateIfdFP(ifd, p);
        }
      }
    }
  }

  /**
   * Validate Continuous Tone.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdCT(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    boolean rgb =
        metadata.containsTagId(TiffTags.getTagId("PhotometricInterpretation"))
            && metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue() == 2;
    boolean lab =
        metadata.containsTagId(TiffTags.getTagId("PhotometricInterpretation"))
            && metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue() == 8;
    int spp = -1;
    if (metadata.containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      spp = (int) metadata.get(TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
    }
    int planar = 1;
    if (metadata.containsTagId(TiffTags.getTagId("PlanarConfiguration"))) {
      planar = (int) metadata.get(TiffTags.getTagId("PlanarConfiguration")).getFirstNumericValue();
    }
    int rps = -1;
    if (metadata.containsTagId(TiffTags.getTagId("RowsPerStrip"))) {
      rps = (int) metadata.get(TiffTags.getTagId("RowsPerStrip")).getFirstNumericValue();
    }
    int length = -1;
    if (metadata.containsTagId(TiffTags.getTagId("ImageLength"))) {
      length = (int) metadata.get(TiffTags.getTagId("ImageLength")).getFirstNumericValue();
    }
    int spi = Math.floorDiv(length + rps - 1, rps);

    if (lab) {

    } else {
      if (p == 1 || p == 2) {
        checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
      }
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "BitsPerSample", spp);
    if (rgb || lab) {
      checkRequiredTag(metadata, "BitsPerSample", 3, new long[]{8, 16});
    } else {
      if (p == 0) {
        checkRequiredTag(metadata, "BitsPerSample", spp, new long[]{8, 16});
      } else {
        checkRequiredTag(metadata, "BitsPerSample", spp, new long[]{8});
      }
    }
    if (p == 2 || rgb || lab) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1,7,8});
    } else if (p == 1) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1});
    }
    if (p == 0) {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {2, 5, 6, 8});
      if (metadata.containsTagId(TiffTags.getTagId("PhotometricInterpretation"))
          && metadata.get(TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue() == 6
          &&
          metadata.get(TiffTags.getTagId("Compression")).getFirstNumericValue() != 7) {
        validation.addErrorLoc("YCbCr shall be used only when compression has the value 7", "IFD"
            + currentIfd);
      }
    } else if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {5});
    }
    if (planar == 1 || planar == 32768) {
      checkRequiredTag(metadata, "StripOffsets", spi);
    } else if (planar == 2) {
      checkRequiredTag(metadata, "StripOffsets", spp * spi);
    }
    if (rgb || lab) {
    } else if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    if (rgb || lab) {
      checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{3});
    } else {
      if (p == 0) {
        checkRequiredTag(metadata, "SamplesPerPixel", 1);
      } else if (p == 1 || p == 2) {
        checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{4});
      }
    }
    if (p == 1 || p == 2 || rgb || lab) {
      if (planar == 1 || planar == 32768) {
        checkRequiredTag(metadata, "StripBYTECount", spi);
      } else if (planar == 2) {
        checkRequiredTag(metadata, "StripBYTECount", spp * spi);
      }
      checkRequiredTag(metadata, "XResolution", 1);
      checkRequiredTag(metadata, "YResolution", 1);
    }
    if (p == 0 || rgb || lab) {
      checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[]{1,2,32768});
    } else if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[]{1});
    }
    if (rgb || lab) {

    } else {
      if (p == 1 || p == 2) {
        checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
        checkRequiredTag(metadata, "InkSet", 1, new long[]{1});
        checkRequiredTag(metadata, "NumberOfInks", 1, new long[]{4});
        checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
      }
    }
  }

  /**
   * Validate Line Work.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdLW(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    if (p == 1) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{1});
    if (p == 0) {
      checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{4});
    } else {
      checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{8});
    }
    checkRequiredTag(metadata, "Compression", 1, new long[]{32896});
    checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {5});
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "InkSet", 1, new long[]{1});
      checkRequiredTag(metadata, "NumberOfInks", 1, new long[]{4});
    }
    if (p == 1) {
      checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
    }
    checkRequiredTag(metadata, "ColorTable", -1);
  }

  /**
   * Validate High-Resolution Continuous-Tone.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdHC(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    int spp = -1;
    if (metadata.containsTagId(TiffTags.getTagId("SampesPerPixel"))) {
      spp = (int)metadata.get(TiffTags.getTagId("SampesPerPixel")).getFirstNumericValue();
    }
    if (p == 1) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "BitsPerSample", spp);
    if (p == 1) {
      checkRequiredTag(metadata, "BitsPerSample", 4, new long[]{8});
    }
    checkRequiredTag(metadata, "Compression", 1, new long[]{32897});
    checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {5});
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    if (p == 1) {
      checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{4});
    }
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[]{1});
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "InkSet", 1, new long[]{1});
      checkRequiredTag(metadata, "NumberOfInks", 1, new long[]{4});
      checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
    }
    checkRequiredTag(metadata, "TransparencyIndicator", 1, new long[]{0, 1});
  }

  /**
   * Validate Monochrome Picture.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdMP(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    if (p == 1) {
      checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{8, 16});
    } else {
      checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{8});
    }
    if (p == 0) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1,7,8,32895});
    } else if (p == 1) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1});
    } else {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1,7,8});
    }
    if (p == 0) {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1);
    } else {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {0});
    }
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 0) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1,4,5,8});
    } else {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    if (p == 1) {
      checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{1});
    }
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
      checkRequiredTag(metadata, "PixelIntensityRange", 2, new long[]{0, 255});
    }
    checkRequiredTag(metadata, "ImageColorIndicator", 1, new long[]{0, 1});
  }

  /**
   * Validate Binary Picture.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdBP(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{1});
    if (p == 0) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1,4,8});
    } else if (p == 1) {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1});
    } else {
      checkRequiredTag(metadata, "Compression", 1, new long[]{1,4,8});
    }
    if (p == 0) {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1);
    } else {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {0});
    }
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 0) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1,4,5,8});
    } else {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{1});
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
    }
    checkRequiredTag(metadata, "ImageColorIndicator", 1, new long[]{0, 1, 2});
    checkRequiredTag(metadata, "BackgroundColorIndicator", 1, new long[]{0, 1, 2});
  }

  /**
   * Validate Binary Lineart.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1)
   */
  private void validateIfdBL(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    if (p == 1) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{1});
    checkRequiredTag(metadata, "Compression", 1, new long[]{32898});
    if (p == 0) {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {0, 1});
    } else {
      checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {0});
    }
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 0) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1,4,5,8});
    } else {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{1});
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    if (p == 1) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "DotRange", 2, new long[]{0, 255});
    }
    checkRequiredTag(metadata, "ImageColorIndicator", 1, new long[]{0, 1, 2});
    checkRequiredTag(metadata, "BackgroundColorIndicator", 1, new long[]{0, 1, 2});
  }

  /**
   * Validate Screened Data image.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P2 = 2)
   */
  private void validateIfdSD(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    if (p == 2) {
      checkRequiredTag(metadata, "NewSubfileType", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "BitsPerSample", 1, new long[]{1});
    checkRequiredTag(metadata, "Compression", 1, new long[]{1,4,8});
    checkRequiredTag(metadata, "PhotometricInterpretation", 1, new long[] {5});
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 0) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1,4,5,8});
    } else {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    if (p == 2) {
      checkRequiredTag(metadata, "SamplesPerPixel", 1, new long[]{1,4});
    }
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[]{2});
    if (p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "NumberOfInks", 1, new long[]{4});
    }
    checkRequiredTag(metadata, "InkSet", 1, new long[]{1});
    checkRequiredTag(metadata, "BackgroundColorIndicator", 1, new long[]{0, 1, 2});
  }

  /**
   * Validate Final Page.
   *
   * @param ifd the ifd
   * @param p the profile (default = 0, P1 = 1, P2 = 2)
   */
  private void validateIfdFP(IFD ifd, int p) {
    IfdTags metadata = ifd.getMetadata();

    checkRequiredTag(metadata, "ImageDescription", 1);
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "StripOffsets", 1, new long[]{0});
    }
    checkRequiredTag(metadata, "NewSubfileType", 1);
    checkRequiredTag(metadata, "ImageLength", 1);
    checkRequiredTag(metadata, "ImageWidth", 1);
    checkRequiredTag(metadata, "StripOffsets", 1);
    if (p == 0) {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1,4,5,8});
    } else {
      checkRequiredTag(metadata, "Orientation", 1, new long[]{1});
    }
    checkRequiredTag(metadata, "StripBYTECount", 1);
    checkRequiredTag(metadata, "XResolution", 1);
    checkRequiredTag(metadata, "YResolution", 1);
    checkRequiredTag(metadata, "PlanarConfiguration", 1, new long[]{2});
    if (p == 1 || p == 2) {
      checkRequiredTag(metadata, "ResolutionUnit", 1, new long[]{2, 3});
      checkRequiredTag(metadata, "NumberOfInks", 1, new long[]{4});
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
      validation.addErrorLoc("Missing required tag for TiffIT" + profile + " " + tagName, "IFD"
          + currentIfd);
      ok = false;
    } else if (cardinality != -1 && metadata.get(tagid).getCardinality() != cardinality) {
      validation.addError("Invalid cardinality for TiffIT" + profile + " tag " + tagName, "IFD"
          + currentIfd,
          metadata.get(tagid)
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
        validation.addError("Invalid value for TiffIT" + profile + " tag " + tagName, "IFD"
            + currentIfd, val);
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
