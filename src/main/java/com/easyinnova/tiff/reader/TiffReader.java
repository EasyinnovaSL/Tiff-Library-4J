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
import com.easyinnova.tiff.model.ByteOrder;
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
import com.easyinnova.tiff.profiles.TiffEPProfile;
import com.easyinnova.tiff.profiles.TiffITProfile;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
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
   * Gets the stream.
   *
   * @return the stream
   */
  public TiffInputStream getStream() {
    return data;
  }

  /**
   * Gets the result of the validation.
   *
   * @return the validation result
   */
  public ValidationResult getBaselineValidation() {
    return validation;
    // BaselineProfile bp = new BaselineProfile(tiffModel);
    // bp.validate();
    // return bp.getValidation();
  }

  /**
   * Gets the result of the validation.
   *
   * @return the validation result
   */
  public ValidationResult getTiffEPValidation() {
    TiffEPProfile bpep = new TiffEPProfile(tiffModel);
    bpep.validate();
    return bpep.getValidation();
  }

  /**
   * Gets the result of the validation.
   *
   * @param profile the TiffIT profile (0: default, 1: P1, 2: P2)
   * @return the validation result
   */
  public ValidationResult getTiffITValidation(int profile) {
    TiffITProfile bpit = new TiffITProfile(tiffModel, profile);
    bpit.validate();
    return bpit.getValidation();
  }

  public int readFile(String filename) {
    return readFile(filename, true);
  }

  /**
   * Parses a Tiff File and create an internal model representation.
   *
   * @param filename the Tiff filename
   * @return Error code (0: successful, -1: file not found, -2: IO exception)
   */
  public int readFile(String filename, boolean validate) {
    int result = 0;

    try {
      if (Files.exists(Paths.get(filename))) {
        data = new TiffInputStream(new File(filename));
        result = this.validate(validate);
      } else {
        // File not found
        result = -1;
        tiffModel.setFatalError(true, "File not found");
      }
    } catch (Exception ex) {
      // IO exception
      result = -2;
      tiffModel.setFatalError(true, "IO Exception");
    }

    return result;
  }

  /**
   * Parses a Tiff File and create an internal model representation.
   *
   * @param filename the Tiff filename
   * @return Error code (0: successful, -1: file not found, -2: IO exception)
   */
  public int readFile(RandomAccessFile raf, boolean validate) {
    int result = 0;

    try {
      data = new TiffInputStream(raf);
      result = this.validate(validate);
    } catch (Exception ex) {
      // IO exception
      result = -2;
      tiffModel.setFatalError(true, "IO Exception");
    }

    return result;
  }

  /**
   * Parses a Tiff File and create an internal model representation.
   *
   * @param filename the Tiff filename
   * @return Error code (0: successful, -1: file not found, -2: IO exception)
   */
  private int validate(boolean validate) {
    int result = 0;

    try {
        tiffModel = new TiffDocument();
        validation = new ValidationResult(validate);
        tiffModel.setSize(data.size());
        boolean correctHeader = readHeader();
        if (correctHeader) {
          if (tiffModel.getMagicNumber() < 42) {
            validation
                .addError("Incorrect tiff magic number", "Header", tiffModel.getMagicNumber());
          } else if (tiffModel.getMagicNumber() == 43) {
            validation.addErrorLoc("Big tiff file not yet supported", "Header");
          } else if (validation.isCorrect()) {
            readIFDs();

            if (validate) {
              BaselineProfile bp = new BaselineProfile(tiffModel);
              bp.validate();
              getBaselineValidation().add(bp.getValidation());
            }
          }
        }

        if (getBaselineValidation().getFatalError()) {
          tiffModel.setFatalError(true, getBaselineValidation().getFatalErrorMessage());
        }

        data.close();
    } catch (Exception ex) {
      // IO exception
      result = -2;
      tiffModel.setFatalError(true, "IO Exception");
    }

    return result;
  }

  /**
   * Reads the Tiff header.
   *
   * @return true, if successful
   */
  private boolean readHeader() {
    boolean correct = true;
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    int c1 = 0;
    int c2 = 0;
    try {
      c1 = data.readByte().toInt();
      c2 = data.readByte().toInt();
    } catch (Exception ex) {
      validation.addErrorLoc("Header IO Exception", "Header");
    }

    // read the first two bytes, in order to know the byte ordering
    if (c1 == 'I' && c2 == 'I') {
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } else if (c1 == 'M' && c2 == 'M') {
      byteOrder = ByteOrder.BIG_ENDIAN;
    }
    else if (byteOrderErrorTolerance > 0 && c1 == 'i' && c2 == 'i') {
      validation.addWarning("Byte Order in lower case", "" + c1 + c2, "Header");
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } else if (byteOrderErrorTolerance > 0 && c1 == 'm' && c2 == 'm') {
      validation.addWarning("Byte Order in lower case", "" + c1 + c2, "Header");
      byteOrder = ByteOrder.BIG_ENDIAN;
    } else if (byteOrderErrorTolerance > 1) {
      validation.addWarning("Non-sense Byte Order. Trying Little Endian.", "" + c1 + c2, "Header");
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } else {
      validation.addErrorLoc("Invalid Byte Order " + c1 + c2, "Header");
      correct = false;
    }
    if (correct) {
      tiffModel.setByteOrder(byteOrder);
      data.setByteOrder(byteOrder);

      try {
        // read magic number
        int magic = data.readShort().toInt();
        tiffModel.setMagicNumber(magic);
      } catch (Exception ex) {
        validation.addErrorLoc("Magic number parsing error", "Header");
        correct = false;
      }
    }
    return correct;
  }

  /**
   * Read the IFDs contained in the Tiff file.
   */
  private void readIFDs() {
    int offset0 = 0;
    try {
      // The pointer to the first IFD is located in bytes 4-7
      offset0 = data.readLong(4).toInt();
      tiffModel.setFirstIFDOffset(offset0);
      if (offset0 == 0)
        validation.addErrorLoc("There is no first IFD", "Header");
      else if (offset0 > data.size())
        validation.addErrorLoc("Incorrect offset", "Header");
    } catch (Exception ex) {
      validation.addErrorLoc("IO exception", "Header");
    }
      
    if (validation.isCorrect()) {
      int nifd = 1;
      try {
        IfdReader ifd0 = readIFD(offset0, true, 0);
        HashSet<Integer> usedOffsets = new HashSet<Integer>();
        usedOffsets.add(offset0);
        if (ifd0.getIfd() == null) {
          validation.addErrorLoc("Parsing error in first IFD", "IFD" + 0);
        } else {
          IFD iifd = ifd0.getIfd();
          iifd.setNextOffset(ifd0.getNextIfdOffset());
          tiffModel.addIfd0(iifd);

          IfdReader current_ifd = ifd0;

          // Read next IFDs
          boolean stop = false;
          while (current_ifd.getNextIfdOffset() > 0 && !stop) {
            if (usedOffsets.contains(current_ifd.getNextIfdOffset())) {
              // Circular reference
              validation.addErrorLoc("IFD offset already used", "IFD" + nifd);
              stop = true;
            } else if (current_ifd.getNextIfdOffset() > data.size()) {
              validation.addErrorLoc("Incorrect offset", "IFD" + nifd);
              stop = true;
            } else {
              usedOffsets.add(current_ifd.getNextIfdOffset());
              IfdReader next_ifd = readIFD(current_ifd.getNextIfdOffset(), true, nifd);
              if (next_ifd == null) {
                validation.addErrorLoc("Parsing error in IFD " + nifd, "IFD" + nifd);
                stop = true;
              } else {
                iifd = next_ifd.getIfd();
                iifd.setNextOffset(next_ifd.getNextIfdOffset());
                current_ifd.getIfd().setNextIFD(iifd);
                current_ifd = next_ifd;
              }
              nifd++;
            }
          }
        }
      } catch (Exception ex) {
        validation.addErrorLoc("IFD parsing error", "IFD" + nifd);
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
   * @param n the IFD number
   * @return the ifd reading result
   */
  private IfdReader readIFD(int offset, boolean isImage, int n) {
    IFD ifd = new IFD(isImage);
    ifd.setOffset(offset);
    IfdReader ir = new IfdReader();
    ir.setIfd(ifd);
    int nextIfdOffset = 0;
    try {
      if (offset % 2 != 0) {
        validation.addErrorLoc("Bad word alignment in the offset of the IFD", "IFD" + n);
      }
      int index = offset;
      int directoryEntries = data.readShort(offset).toInt();
      if (directoryEntries < 1) {
        validation.addError("Incorrect number of IFD entries", "IFD" + n,
            directoryEntries);
        validation.setFatalError(true, "Incorrect number of IFD entries");
      } else if (directoryEntries > 500) {
        if (n < 0) {
          validation.addError("Incorrect number of IFD entries", "SubIFD" + (-n), directoryEntries);
          validation.setFatalError(true, "Incorrect number of IFD entries");
        } else {
          validation.addError("Incorrect number of IFD entries", "IFD" + n, directoryEntries);
          validation.setFatalError(true, "Incorrect number of IFD entries");
        }
      } else {
        index += 2;

        // Reads the tags
        for (int i = 0; i < directoryEntries; i++) {
          int tagid = 0;
          int tagType = -1;
          int tagN = -1;
          try {
            tagid = data.readShort(index).toInt();
            tagType = data.readShort(index + 2).toInt();
            tagN = data.readLong(index + 4).toInt();
            boolean ok = checkType(tagid, tagType, n);
            if (!ok && tagN > 1000) tagN = 1000;
            TagValue tv = getValue(tagType, tagN, tagid, index + 8, ifd, n);
            if (ifd.containsTagId(tagid)) {
              if (duplicateTagTolerance > 0)
                validation.addWarning("Duplicate tag", "" + tagid, "IFD" + n);
              else
                validation.addError("Duplicate tag", "IFD" + n, tagid);
            }
            ifd.addTag(tv);
          } catch (Exception ex) {
            validation.addErrorLoc("Parse error in tag #" + i + " (" + tagid + ")", "IFD" + n);
            TagValue tv = new TagValue(tagid, tagType);
            tv.setReadOffset(index + 8);
            tv.setReadLength(tagN);
            ifd.addTag(tv);
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
            validation.addWarning("Unreadable next IFD offset", "", "IFD" + n);
          else
            validation.addErrorLoc("Unreadable next IFD offset", "IFD" + n);
        }
        if (nextIfdOffset > 0 && nextIfdOffset < 7) {
          validation.addError("Invalid next IFD offset", "IFD" + n, nextIfdOffset);
          nextIfdOffset = 0;
        }
        ir.setNextIfdOffset(nextIfdOffset);

        ir.readImage();
        if (isImage && !ifd.hasStrips() && !ifd.hasTiles()) {
          validation.setFatalError(true, "Incorrect image");
        }
      }
    } catch (Exception ex) {
      validation.addErrorLoc("IO Exception", "IFD" + n);
      return null;
    }
    return ir;
  }

  /**
   * Check tag type.
   *
   * @param tagid the tagid
   * @param tagType the tag type
   * @param n the n
   */
  private boolean checkType(int tagid, int tagType, int n) {
    if (TiffTags.hasTag(tagid) && !TiffTags.getTag(tagid).getName().equals("IPTC")) {
      boolean found = false;
      String stagType = TiffTags.getTagTypeName(tagType);
      if (stagType != null) {
        if (stagType.equals("SUBIFD"))
          stagType = "IFD";
        if (stagType.equals("UNDEFINED"))
          stagType = "BYTE";
        for (String vType : TiffTags.getTag(tagid).getType()) {
          String vType2 = vType;
          if (vType2.equals("UNDEFINED"))
            vType2 = "BYTE";
          if (vType2.equals(stagType)) {
            found = true;
          }
        }
      }
      if (!found) {
        validation.addError("Incorrect type for tag " + TiffTags.getTag(tagid).getName(),
            "IFD" + n, stagType);
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Gets the value of the given tag field.
   *
   * @param tagtype the tag type
   * @param n the cardinality
   * @param id the tag id
   * @param beginOffset the offset position of the tag value
   * @param parentIFD the parent ifd
   * @param nifd the ifd number
   * @return the tag value object
   */
  protected TagValue getValue(int tagtype, int n, int id, int beginOffset, IFD parentIFD, int nifd) {
    int type = tagtype;
    if (id == 330 && type != 13)
      type = 13;

    // Create TagValue object
    TagValue tv = new TagValue(id, type);
    tv.setTagOffset(beginOffset - 8);

    // Defined tags
    int offset = beginOffset;

    // Get type Size
    int typeSize = TiffTags.getTypeSize(type);

    boolean ok = true;

    // Check if the tag value fits in the directory entry value field, and get offset if not
    if (typeSize * n > tagValueSize) {
      try {
        offset = data.readLong(offset).toInt();
        if (offset % 2 != 0) {
          validation.addErrorLoc("Bad word alignment in the offset of tag " + id, "IFD" + n);
        }
      } catch (Exception ex) {
        validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + n);
        ok = false;
      }
    }

    tv.setReadOffset(offset);
    tv.setReadLength(n);
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
              if (ifdOffset % 2 != 0) {
                validation
                    .addErrorLoc("Bad word alignment in the offset of the sub IFD", "IFD" + n);
              }
              IfdReader ifd = readIFD(ifdOffset, true, -nifd);
              IFD subIfd = ifd.getIfd();
              subIfd.setParent(parentIFD);
              parentIFD.setsubIFD(subIfd);
              tv.add(subIfd);
              break;
          }
          offset += typeSize;
        }
      } catch (Exception ex) {
        validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        ok = false;
      }
    }
    if (type == 2) {
      tv.readString();
    }

    if (ok && TiffTags.hasTag(id)) {
      Tag t = TiffTags.getTag(id);
      if (t.hasTypedef() && !t.getTypedef().equals("SubIFD")) {
        String tagclass = t.getTypedef();

        try {
          abstractTiffType instanceOfMyClass =
              (abstractTiffType) Class.forName("com.easyinnova.tiff.model.types." + tagclass)
                  .getConstructor().newInstance();
          if (instanceOfMyClass.isIFD()) {
            long ifdOffset = tv.getFirstNumericValue();
            try {
              if (ifdOffset % 2 != 0) {
                validation.addErrorLoc("Bad word alignment in the offset of Exif", "IFD" + n);
              }
              IfdReader ifd = readIFD((int) ifdOffset, false, -1);
              IFD exifIfd = ifd.getIfd();
              exifIfd.setIsIFD(true);
              tv.clear();
              tv.add(exifIfd);
            } catch (Exception ex) {
              validation.addErrorLoc("Parse error in Exif", "IFD" + nifd);
            }
          } else {
            if (tv.getId() == 33723)
              instanceOfMyClass.read(tv, data.getFilePath());
            else
              instanceOfMyClass.read(tv);
          }
        } catch (ClassNotFoundException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (NoSuchMethodException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (SecurityException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (InstantiationException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (IllegalAccessException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (IllegalArgumentException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (InvocationTargetException e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        } catch (Exception e) {
          validation.addErrorLoc("Parse error getting tag " + id + " value", "IFD" + nifd);
        }
      }
    }

    if (ok) tv.setReadValue();
    return tv;
  }
}


