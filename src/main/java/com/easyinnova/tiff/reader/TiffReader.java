/**
 * <h1>TiffReader.java</h1>
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
 * @since 28/5/2015
 *
 */
package com.easyinnova.tiff.reader;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.IccProfileCreators;
import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.Tag;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.abstractTiffType;
import com.easyinnova.tiff.profiles.BaselineProfile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Reads and parses a Tiff file, storing it in an internal model.
 */
public class TiffReader {

  /** The model containing the Tiff data. */
  TiffDocument tiffModel;

  /** The stream to get data from the file. */
  TiffInputStream data;

  /** The size in bytes of the tags' values field (=4 for Tiff, =8 for BigTiff). */
  // TODO: Use it
  int tagValueSize = 4;

  /** The result of the validation. */
  ValidationResult validation;

  /**
   * Tolerance to duplicate tags.<br>
   * 0: No tolerance. 1: Tolerate duplicates keeping first appearance only
   */
  int duplicateTagTolerance = 10;

  /**
   * Tolerance to errors in the next IFD pointer.<br>
   * 0: No tolerance. 1: Tolerate errors assuming there is no next Ifd (offset = 0)
   */
  int nextIFDTolerance = 0;

  /**
   * Tolerance to errors in the byte ordering.<br>
   * 0: No tolerance. 1: Lower case tolerance. 2: Full tolerance, assuming little endian.
   * */
  int byteOrderErrorTolerance = 0;

  /**
   * Default constructor.<br>
   * Instantiates a new empty tiff reader.
   *
   * @throws ReadTagsIOException the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  public TiffReader() throws ReadTagsIOException, ReadIccConfigIOException {
    tiffModel = null;
    TiffTags.getTiffTags();
    IccProfileCreators.getIccProfileCreators();
  }

  /**
   * Gets the internal model of the Tiff file.
   *
   * @return the model
   */
  public TiffDocument getModel() {
    return tiffModel;
  }

  /**
   * Gets the result of the validation.
   *
   * @return the validation result
   */
  public ValidationResult getBaselineValidation() {
    return validation;
  }

  /**
   * Parses a Tiff File and create an internal model representation.
   *
   * @param filename the Tiff filename
   * @return Error code (0: successful, -1: file not found, -2: IO exception)
   */
  public int readFile(String filename) {
    int result = 0;

    try {
      if (Files.exists(Paths.get(filename))) {
        data = new TiffInputStream(new File(filename));

        tiffModel = new TiffDocument();
        validation = new ValidationResult();
        readHeader();
        if (tiffModel.getMagicNumber() < 42) {
          validation.addError("Incorrect tiff magic number", tiffModel.getMagicNumber());
        } else if (tiffModel.getMagicNumber() == 43) {
          validation.addError("Big tiff file not yet supported");
        } else if (validation.isCorrect()) {
          readIFDs();

          BaselineProfile bp = new BaselineProfile(tiffModel);
          bp.validate();
          getBaselineValidation().add(bp.getValidation());
        }

        data.close();
      } else {
        // File not found
        result = -1;
      }
    } catch (Exception ex) {
      // IO exception
      result = -2;
    }

    return result;
  }

  /**
   * Reads the Tiff header.
   */
  private void readHeader() {
    boolean correct = true;
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    int c1 = 0;
    int c2 = 0;
    try {
      c1 = data.readByte().toInt();
      c2 = data.readByte().toInt();
    } catch (Exception ex) {
      validation.addError("Header IO Exception");
    }

    // read the first two bytes, in order to know the byte ordering
    if (c1 == 'I' && c2 == 'I')
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    else if (c1 == 'M' && c2 == 'M')
      byteOrder = ByteOrder.BIG_ENDIAN;
    else if (byteOrderErrorTolerance > 0 && c1 == 'i' && c2 == 'i') {
      validation.addWarning("Byte Order in lower case");
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } else if (byteOrderErrorTolerance > 0 && c1 == 'm' && c2 == 'm') {
      validation.addWarning("Byte Order in lower case");
      byteOrder = ByteOrder.BIG_ENDIAN;
    } else if (byteOrderErrorTolerance > 1) {
      validation.addWarning("Non-sense Byte Order. Trying Little Endian.");
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } else {
      validation.addError("Invalid Byte Order " + c1 + c2);
    }

    if (correct) {
      // set byte ordering to the stream
      data.setByteOrder(byteOrder);

      try {
        // read magic number
        int magic = data.readShort().toInt();
        tiffModel.setMagicNumber(magic);
      } catch (Exception ex) {
        validation.addError("Magic number parsing error");
      }
    }
  }

  /**
   * Read the IFDs contained in the Tiff file.
   */
  private void readIFDs() {
    int offset0 = 0;
    try {
      // The pointer to the first IFD is located in bytes 4-7
      offset0 = data.readLong(4).toInt();
      if (offset0 == 0)
        validation.addError("There is no first IFD");
      else if (offset0 > data.size())
        validation.addError("Incorrect offset");
    } catch (Exception ex) {
      validation.addError("IO exception");
    }
      
    if (validation.isCorrect()) {
      try {
        IfdReader ifd0 = readIFD(offset0, true);
        HashSet<Integer> usedOffsets = new HashSet<Integer>();
        usedOffsets.add(offset0);
        if (ifd0.getIfd() == null) {
          validation.addError("Parsing error in first IFD");
        } else {
          tiffModel.addIfd0(ifd0.getIfd());

          IfdReader current_ifd = ifd0;

          // Read next IFDs
          int nifd = 1;
          boolean stop = false;
          while (current_ifd.getNextIfdOffset() > 0 && !stop) {
            if (usedOffsets.contains(current_ifd.getNextIfdOffset())) {
              // Circular reference
              validation.addError("IFD offset already used");
              stop = true;
            } else if (current_ifd.getNextIfdOffset() > data.size()) {
              validation.addError("Incorrect offset");
              stop = true;
            } else {
              usedOffsets.add(current_ifd.getNextIfdOffset());
              IfdReader next_ifd = readIFD(current_ifd.getNextIfdOffset(), true);
              if (next_ifd == null) {
                validation.addError("Parsing error in IFD " + nifd);
                stop = true;
              } else {
                current_ifd.getIfd().setNextIFD(next_ifd.getIfd());
                current_ifd = next_ifd;
              }
              nifd++;
            }
          }
        }
      } catch (Exception ex) {
        validation.addError("IFD parsing error");
      }

      try {
        tiffModel.createMetadataDictionary();
      } catch (Exception ex) {
      }
    }
  }

  /**
   * Parses the image file descriptor data.
   *
   * @param offset the file offset (in bytes) pointing to the IFD
   * @param isImage the is image
   * @return the ifd reading result
   */
  private IfdReader readIFD(int offset, boolean isImage) {
    IFD ifd = new IFD(isImage);
    IfdReader ir = new IfdReader();
    ir.setIfd(ifd);
    int nextIfdOffset = 0;
    try {
      int index = offset;
      int directoryEntries = data.readShort(offset).toInt();
      if (directoryEntries < 1) {
        validation.addError("Incorrect number of IFD entries",
            directoryEntries);
      } else {
        index += 2;

        // Reads the tags
        for (int i = 0; i < directoryEntries; i++) {
          int tagid = 0;
          try {
            tagid = data.readShort(index).toInt();
            int tagType = data.readShort(index + 2).toInt();
            int tagN = data.readLong(index + 4).toInt();
            if (tagType == 13) {
              tagType = 13;
            }
            TagValue tv = getValue(tagType, tagN, tagid, index + 8, ifd);
            if (ifd.containsTagId(tagid)) {
              if (duplicateTagTolerance > 0)
                validation.addWarning("Duplicate tag", tagid);
              else
                validation.addError("Duplicate tag", tagid);
            } else {
              ifd.addTag(tv);
            }
          } catch (Exception ex) {
            validation.addError("Parse error in tag #" + i + " (" + tagid + ")");
          }

          index += 12;
        }

        // Reads the position of the next IFD
        nextIfdOffset = 0;
        try {
          nextIfdOffset = data.readLong(index).toInt();
        } catch (Exception ex) {
          nextIfdOffset = 0;
          if (nextIFDTolerance > 0)
            validation.addWarning("Unreadable next IFD offset");
          else
            validation.addError("Unreadable next IFD offset");
        }
        if (nextIfdOffset > 0 && nextIfdOffset < 7) {
          validation.addError("Invalid next IFD offset", nextIfdOffset);
          nextIfdOffset = 0;
        }
        ir.setNextIfdOffset(nextIfdOffset);

        ir.readImage();
      }
    } catch (Exception ex) {
      validation.addError("IO Exception");
      ir.setIfd(null);
    }
    return ir;
  }

  /**
   * Gets the value of the given tag field.
   *
   * @param tagtype the tag type
   * @param n the cardinality
   * @param id the tag id
   * @param beginOffset the offset position of the tag value
   * @param parentIFD the parent ifd
   * @return the tag value object
   */
  protected TagValue getValue(int tagtype, int n, int id, int beginOffset, IFD parentIFD) {
    int type = tagtype;
    if (id == 330 && type != 13)
      type = 13;

    // Create TagValue object
    TagValue tv = new TagValue(id, type);

    // Defined tags
    int offset = beginOffset;

    // Get type Size
    int typeSize = 1;
    switch (type) {
      case 3:
      case 8:
        typeSize = 2;
        break;
      case 4:
      case 9:
      case 11:
      case 13:
        typeSize = 4;
        break;
      case 5:
      case 10:
      case 12:
        typeSize = 8;
        break;
      default:
        typeSize = 1;
        break;
    }

    boolean ok = true;

    // Check if the tag value fits in the directory entry value field, and get offset if not
    if (typeSize * n > tagValueSize) {
      try {
      offset = data.readLong(offset).toInt();
      } catch (Exception ex) {
        validation.addError("Parse error getting tag " + id + " value");
        ok = false;
      }
    }

    if (ok) {
      try {
        for (int i = 0; i < n; i++) {
          // Get N tag values
          switch (type) {
            case 1:
              tv.add(data.readByte(offset));
              break;
            case 2:
              tv.add(data.readAscii(offset));
              break;
            case 6:
              tv.add(data.readSByte(offset));
              break;
            case 7:
              tv.add(data.readUndefined(offset));
              break;
            case 3:
              tv.add(data.readShort(offset));
              break;
            case 8:
              tv.add(data.readSShort(offset));
              break;
            case 4:
              tv.add(data.readLong(offset));
              break;
            case 9:
              tv.add(data.readSLong(offset));
              break;
            case 5:
              tv.add(data.readRational(offset));
              break;
            case 10:
              tv.add(data.readSRational(offset));
              break;
            case 11:
              tv.add(data.readFloat(offset));
              break;
            case 12:
              tv.add(data.readDouble(offset));
              break;
            case 13:
              int ifdOffset = data.readLong(offset).toInt();
              IfdReader ifd = readIFD(ifdOffset, true);
              IFD subIfd = ifd.getIfd();
              subIfd.setParent(parentIFD);
              parentIFD.setsubIFD(subIfd);
              tv.add(subIfd);
              break;
          }
          offset += typeSize;
        }
      } catch (Exception ex) {
        validation.addError("Parse error getting tag " + id + " value");
        ok = false;
      }
    }

    if (ok && TiffTags.hasTag(id)) {
      Tag t = TiffTags.getTag(id);
      if (t.hasTypedef()) {
        String tagclass = t.getTypedef();

        try {
          abstractTiffType instanceOfMyClass =
              (abstractTiffType) Class.forName("com.easyinnova.tiff.model.types." + tagclass)
                  .getConstructor().newInstance();
          if (instanceOfMyClass.isIFD()) {
            long ifdOffset = tv.getFirstNumericValue();
            try {
              IfdReader ifd = readIFD((int) ifdOffset, false);
              IFD exifIfd = ifd.getIfd();
              exifIfd.setIsIFD(true);
              tv.clear();
              tv.add(exifIfd);
            } catch (Exception ex) {
              validation.addError("Parse error in Exif");
            }
          } else {
            instanceOfMyClass.read(tv);
          }
        } catch (ClassNotFoundException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (NoSuchMethodException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (SecurityException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (InstantiationException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (IllegalAccessException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (IllegalArgumentException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (InvocationTargetException e) {
          validation.addError("Parse error getting tag " + id + " value");
        } catch (Exception e) {
          validation.addError("Parse error getting tag " + id + " value");
        }
      }
    }

    return tv;
  }
}


