/**
 * <h1>TiffFile.java</h1>
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
 * @since 14/5/2015
 *
 */
package com.easyinnova.tiff.model;

import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IccProfile;
import com.easyinnova.tiff.model.types.abstractTiffType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modeling of the TIFF file with methods to access its IFDs and metadata.
 */
public class TiffDocument implements Serializable {

  /** The magic number. */
  private int magicNumber;

  /** The list of Ifd. */
  private IFD firstIFD;

  /** The metadata. */
  private Metadata metadata;

  /** The byte order. */
  private ByteOrder byteOrder;

  /** The offset of the first IFD. */
  private int firstIFDOffset;

  /** The file size in bytes. */
  private long size;

  private boolean fatalError;
  private String fatalErrorMessage;

  /**
   * Instantiates a new tiff file.
   */
  public TiffDocument() {
    firstIFD = null;
    metadata = null;
    fatalError = false;
  }

  /**
   * Adds an IFD to the model.
   *
   * @param ifd the ifd
   */
  public void addIfd0(IFD ifd) {
    firstIFD = ifd;
  }

  /**
   * Sets the first ifd offset.
   *
   * @param offset the new first ifd offset
   */
  public void setFirstIFDOffset(int offset) {
    firstIFDOffset = offset;
  }

  /**
   * Gets the first ifd offset.
   *
   * @return the first ifd offset
   */
  public int getFirstIFDOffset() {
    return firstIFDOffset;
  }

  /**
   * Sets the size.
   *
   * @param size the new size
   */
  public void setSize(long size) {
    this.size = size;
  }

  public boolean getFatalError() {
    return fatalError;
  }

  public String getFatalErrorMEssage() {
    return fatalErrorMessage;
  }

  public void setFatalError(boolean error, String message) {
    fatalError = error;
    fatalErrorMessage = message;
  }

  public IccProfile getIccProfile() {
    if (getMetadata() != null) {
      if (getMetadata().contains("ICCProfile")) {
        List<TiffObject> lobj = getMetadata().getList("ICCProfile");
        for (TiffObject obj : lobj) {
          if (obj instanceof IccProfile) {
            return (IccProfile) obj;
          }
        }
      }
    }
    return null;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public long getSize() {
    return size;
  }

  /**
   * Gets the ifd count.
   *
   * @return the ifd count
   */
  public int getIfdCount() {
    int c = 0;
    if (metadata != null && metadata.contains("IFD"))
      c = getMetadataList("IFD").size();
    return c;
  }

  /**
   * Gets the images count.
   *
   * @return the ifd count
   */
  public int getIfdImagesCount() {
    int c = 0;
    if (metadata.contains("IFD")) {
      List<TiffObject> l = getMetadataList("IFD");
      int n = 0;
      for (TiffObject to : l) {
        IFD ifd = (IFD) to;
        if (ifd.isImage())
          n++;
      }
      c = n;
    }
    return c;
  }

  /**
   * Gets the Subifd count.
   *
   * @return the Subifd count
   */
  public int getSubIfdCount() {
    int c = 0;
    if (metadata != null && metadata.contains("SubIFDs"))
      c = getMetadataList("SubIFDs").size();
    return c;
  }

  /**
   * Gets the count of IFDs and SubIFDs.
   *
   * @return the ifd count
   */
  public int getIfdAndSubIfdCount() {
    return getMetadataList("IFD").size() + getMetadataList("SubIFDs").size();
  }

  /**
   * Returns a list of ifds including exifds.
   *
   * @return the ifds list
   */
  public List<TiffObject> getIfds() {
    List<TiffObject> l = new ArrayList<TiffObject>();
    if (metadata != null && metadata.contains("IFD"))
      l = getMetadataList("IFD");
    return l;
  }

  /**
   * Returns a list of ifds representing Images.
   *
   * @return the ifds list
   */
  public List<TiffObject> getImageIfds() {
    List<TiffObject> l = new ArrayList<TiffObject>();
    IFD oifd = this.firstIFD;
    while (oifd != null) {
      if (oifd.isImage()) {
        if (oifd.hasSubIFD()) {
          try {
            long length = oifd.getMetadata().get("ImageLength").getFirstNumericValue();
            long width = oifd.getMetadata().get("ImageWidth").getFirstNumericValue();
            long sublength =
                oifd.getsubIFD().getMetadata().get("ImageLength").getFirstNumericValue();
            long subwidth = oifd.getsubIFD().getMetadata().get("ImageWidth").getFirstNumericValue();
            if (sublength > length && subwidth > width) {
              l.add(oifd.getsubIFD());
            } else {
              l.add(oifd);
            }
          } catch (Exception ex) {
            l.add(oifd);
          }
        } else {
          l.add(oifd);
        }
      }
      oifd = oifd.getNextIFD();
    }
    return l;
  }

  /**
   * Returns a list of subifds.
   *
   * @return the subifds list
   */
  public List<TiffObject> getSubIfds() {
    List<TiffObject> l = new ArrayList<TiffObject>();
    if (metadata != null && metadata.contains("SubIFDs"))
      l = getMetadataList("SubIFDs");
    return l;
  }

  /**
   * Gets the endianess.
   *
   * @return the endianess
   */
  public ByteOrder getEndianess() {
    return byteOrder;
  }

  /**
   * Returns a list of subifds.
   *
   * @return the subifds list
   */
  public List<TiffObject> getIfdsAndSubIfds() {
    List<TiffObject> all = new ArrayList<TiffObject>();
    all.addAll(getMetadataList("IFD"));
    all.addAll(getMetadataList("SubIFDs"));
    return all;
  }

  /**
   * Returns the first image of the file.
   *
   * @return image file d
   */
  public IFD getFirstIFD() {
    return this.firstIFD;
  }

  /**
   * Gets the magic number of the Tiff file.
   *
   * @return the magic number
   */
  public int getMagicNumber() {
    return magicNumber;
  }

  /**
   * Sets the magic number of the Tiff file.
   *
   * @param magic the new magic number
   */
  public void setMagicNumber(int magic) {
    this.magicNumber = magic;
  }

  /**
   * Sets the magic number of the Tiff file.
   *
   * @param byteOrder the byte order
   */
  public void setByteOrder(ByteOrder byteOrder) {
    this.byteOrder = byteOrder;
  }

  /**
   * Gets an string with the value of the first tag matching the given tag name.<br>
   *
   * @param name the tag name
   * @return the metadata single string
   */
  public String getMetadataSingleString(String name) {
    String s = "";
    if (metadata == null)
      createMetadataDictionary();
    if (metadata.contains(name))
      s = metadata.get(name).toString();
    return s;
  }

  /**
   * Gets the metadata ok a given class name.
   *
   * @param name the class name
   * @return the list of metadata that matches with the class name
   */
  public List<TiffObject> getMetadataList(String name) {
    List<TiffObject> l = new ArrayList<TiffObject>();
    if (metadata == null)
      createMetadataDictionary();
    if (metadata.contains(name))
      l = metadata.getList(name);
    return l;
  }

  /**
   * Creates the metadata dictionary.
   */
  public void createMetadataDictionary() {
    metadata = new Metadata();
    if (firstIFD != null) {
      addMetadataFromIFD(firstIFD, "IFD", false);
    }
  }

  /**
   * Adds the metadata from ifd.
   *
   * @param ifd the ifd
   * @param key the key
   * @param exif the exif
   * @the tiff tags io exception
   */
  private void addMetadataFromIFD(IFD ifd, String key, boolean exif) {
    metadata.add(key, ifd);
    for (TagValue tag : ifd.getMetadata().getTags()) {
      if (tag.getCardinality() == 1) {
        abstractTiffType t = tag.getValue().get(0);
        if (t.isIFD()) {
          addMetadataFromIFD((IFD) t, key, true);
        } else if (t.containsMetadata()) {
          try {
            Metadata meta = t.createMetadata();
            metadata.addMetadata(meta);
          } catch (Exception ex) {
            // TODO: What?
          }
        } else {
          if (exif)
            t.setContainer("EXIF");
          metadata.add(tag.getName(), t);
        }
      } else {
        if (exif)
          tag.setContainer("EXIF");
        metadata.add(tag.getName(), tag);
      }
    }
    if (ifd.hasNextIFD()) {
      addMetadataFromIFD(ifd.getNextIFD(), key, false);
    }
  }

  /**
   * Prints the metadata.
   */
  public void printMetadata() {
    if (metadata == null)
      createMetadataDictionary();
    System.out.println("METADATA");
    // if (metadata.getCreator() != null) System.out.println("Creator:" + metadata.getCreator());
    for (String name : metadata.keySet()) {
      String mult = "";
      if (getMetadataList(name).size() > 1)
        mult = "(x" + getMetadataList(name).size() + ")";
      if (metadata.getMetadataObject(name).isDublinCore())
        System.out.println("[DC]");
      System.out.println(name + mult + ": "
          + getMetadataSingleString(name));
    }
  }

  /**
   * Gets the metadata.
   *
   * @return the metadata
   */
  public Metadata getMetadata() {
    return metadata;
  }

  /**
   * Removes the tag.
   *
   * @param tagName the tag name
   * @return true, if successful
   */
  public boolean removeTag(String tagName) {
    boolean result = false;
    IFD ifd = firstIFD;
    while (ifd != null) {
      if (ifd.containsTagId(TiffTags.getTagId(tagName))) {
        ifd.removeTag(tagName);
      }
      ifd = ifd.getNextIFD();
    }
    createMetadataDictionary();
    return result;
  }

  /**
   * Adds the tag.
   *
   * @param tagName the tag name
   * @param tagValue the tag value
   * @return true, if successful
   */
  public boolean addTag(String tagName, String tagValue) {
    boolean result = false;
    if (firstIFD != null) {
      if (firstIFD.containsTagId(TiffTags.getTagId(tagName))) {
        firstIFD.removeTag(tagName);
      }
      firstIFD.addTag(tagName, tagValue);
      createMetadataDictionary();
    }
    return result;
  }
}
