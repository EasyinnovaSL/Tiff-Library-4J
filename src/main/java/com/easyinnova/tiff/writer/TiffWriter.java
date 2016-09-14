/**
 * <h1>TiffWriter.java</h1>
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
package com.easyinnova.tiff.writer;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.io.TiffOutputStream;
import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.Double;
import com.easyinnova.tiff.model.types.Float;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.Long;
import com.easyinnova.tiff.model.types.Rational;
import com.easyinnova.tiff.model.types.SLong;
import com.easyinnova.tiff.model.types.SRational;
import com.easyinnova.tiff.model.types.SShort;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * The Class TiffWriter.
 */
public class TiffWriter {

  /** The model. */
  TiffDocument model;

  /** The odata. */
  TiffOutputStream data;

  /** The byte order. */
  ByteOrder byteOrder;

  /** The input. */
  TiffInputStream input;

  /**
   * Instantiates a new tiff writer.
   *
   * @param in the in
   */
  public TiffWriter(TiffInputStream in) {
    model = new TiffDocument();
    byteOrder = ByteOrder.BIG_ENDIAN;
    input = in;
  }

  /**
   * Sets the model.
   *
   * @param model the model
   */
  public void SetModel(TiffDocument model) {
    this.model = model;
  }

  /**
   * Sets the byte order.
   *
   * @param byteOrder the new byte order
   */
  public void setByteOrder(ByteOrder byteOrder) {
    this.byteOrder = byteOrder;
  }

  /**
   * Write.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
  public void write(String filename) throws Exception {
    data = new TiffOutputStream(input);
    data.setByteOrder(byteOrder);
    try {
      data.create(filename);
      writeHeader();
      writeIfds();
      data.close();
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * Writes the header.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeHeader() throws IOException {
    if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
      data.put((byte) 'I');
      data.put((byte) 'I');
    } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
      data.put((byte) 'M');
      data.put((byte) 'M');
    }

    data.putShort((short) 42);
  }

  /**
   * Write.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeIfds() throws IOException {
    IFD first = model.getFirstIFD();
    IFD current = first;

    if (current != null) {
      // First IFD offset
      data.putInt((int) data.position() + 4);
    }

    while (current != null) {
      writeIFD(current);
      current = current.getNextIFD();
    }
  }

  /**
   * Gets the oversized tags.
   *
   * @param ifd the ifd
   * @param oversized the oversized
   * @param undersized the undersized
   * @return the number of tags
   */
  private int classifyTags(IFD ifd, ArrayList<TagValue> oversized, ArrayList<TagValue> undersized) {
    int tagValueSize = 4;
    int n = 0;
    for (TagValue tag : ifd.getMetadata().getTags()) {
      int tagsize = getTagSize(tag);
      if (tagsize > tagValueSize) {
        oversized.add(tag);
      } else {
        undersized.add(tag);
      }
      n++;
    }
    return n;
  }

  /**
   * Write IFD data.
   *
   * @param ifd the ifd
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeIFD(IFD ifd) throws IOException {
    ArrayList<TagValue> oversizedTags = new ArrayList<TagValue>();
    ArrayList<TagValue> undersizedTags = new ArrayList<TagValue>();
    int ntags = classifyTags(ifd, oversizedTags, undersizedTags);

    HashMap<Integer, Integer> pointers = new HashMap<Integer, Integer>();

    // Write IFD entries
    data.putShort((short) ntags);
    ArrayList<TagValue> ltags = ifd.getTags().getTags();
    Collections.sort(ltags, new Comparator<TagValue>() {
      @Override
      public int compare(TagValue a1, TagValue a2) {
        return a1.getId()-a2.getId();
      }
    });
    for (TagValue tv : ltags) {
      int n = tv.getCardinality();
      int id = tv.getId();
      int tagtype = tv.getType();
      data.putShort((short) id);
      data.putShort((short) tagtype);
      if (id == 700)
        n = ((XMP)tv.getValue().get(0)).getLength();
      if (id == 34675)
        n = tv.getReadlength();
      if (id == 33723)
        n = ((IPTC) tv.getValue().get(0)).getLength();
      data.putInt(n);

      pointers.put(id, (int) data.position());
      int startpos = (int) data.position();
      if (oversizedTags.contains(tv)) {
        data.putInt(1); // Any number, later we will update the pointer
      } else {
        writeTagValue(tv);
        while ((int) data.position() - startpos < 4)
          data.put((byte) 0);
      }
    }

    long positionNextIfdOffset = data.position();
    data.putInt(0); // No next IFD (later we will update this value if there is a next IFD)

    // Update pointers and write tag values
    for (TagValue tv : oversizedTags) {
        // Update pointer of the tag entry
        int currentPosition = (int) data.position();
        if (currentPosition % 2 != 0)
          currentPosition++; // Word alignment check
        data.seek(pointers.get(tv.getId()));
        data.putInt(currentPosition);
        data.seek(currentPosition);

        writeTagValue(tv);
    }

    if (ifd.hasStrips()) {
      long stripOffsetsPointer = data.position();
      if (stripOffsetsPointer % 2 != 0) {
        // Correct word alignment
        data.put((byte) 0);
        stripOffsetsPointer = (int) data.position();
      }

      // Write strips and return its offsets
      ArrayList<Integer> offsets = writeStripData(ifd);

      if (offsets.size() > 1) {
        // Write offsets
        stripOffsetsPointer = data.position();
        for (int off : offsets) {
          data.putInt(off);
        }
      }

      // Update pointer of the strip offets
      int currentPosition = (int) data.position();
      data.seek(pointers.get(273));
      data.putInt((int) stripOffsetsPointer);
      data.seek(currentPosition);
    } else if (ifd.hasTiles()) {
      long tilesOffsetsPointer = data.position();
      if (tilesOffsetsPointer % 2 != 0) {
        // Correct word alignment
        data.put((byte) 0);
        tilesOffsetsPointer = (int) data.position();
      }
      ArrayList<Integer> offsets = writeTileData(ifd);

      if (offsets.size() > 1) {
        // Write offsets
        tilesOffsetsPointer = data.position();
        for (int off : offsets) {
          data.putInt(off);
        }
      }

      // Update pointer of the tag entry
      int currentPosition = (int) data.position();
      data.seek(pointers.get(324));
      data.putInt((int) tilesOffsetsPointer);
      data.seek(currentPosition);
    }

    if (ifd.hasNextIFD()) {
      // Update pointer of the next IFD offset
      int currentPosition = (int) data.position();
      if (currentPosition % 2 != 0)
        currentPosition++; // Word alignment check
      data.seek((int)positionNextIfdOffset);
      data.putInt(currentPosition);
      data.seek(currentPosition);
    }
  }

  /**
   * Gets the tag size.
   *
   * @param tag the tag
   * @return the tag size
   */
  private int getTagSize(TagValue tag) {
    int n = tag.getCardinality();
    int id = tag.getId();

    // Calculate tag size
    int type = tag.getType();

    if (id == 330) {
      // SubIFD
      n = 1000;
    }
    if (id == 700) {
      // XMP
      n = tag.getValue().get(0).toString().length();
    }
    if (id == 33723) {
      // IPTC
      n = tag.getReadlength();
    }
    if (id == 34665) {
      // EXIF
      n = 1000;
    }
    if (id == 34675) {
      // ICC
      n = tag.getReadlength();
    }

    int typeSize = TiffTags.getTypeSize(type);
    int tagSize = typeSize * n;
    return tagSize;
  }

  /**
   * Write tag.
   *
   * @param tag the tag
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void writeTagValue(TagValue tag) throws IOException {
    int id = tag.getId();

    // Write tag value
    for (abstractTiffType tt : tag.getValue()) {
      if (id == 700) {
        // XMP
        XMP xmp = (XMP)tt;
        xmp.write(data);
      } else if (id == 33723) {
        // IPTC
        IPTC iptc = (IPTC) tag.getValue().get(0);
        iptc.write(data);
      } else if (id == 330) {
        // SubIFD
        IFD subifd = ((IFD) tag.getValue().get(0));
        writeIFD(subifd);
      } else if (id == 34665) {
        // EXIF
        IFD exif = (IFD) tag.getValue().get(0);
        writeIFD(exif);
      } else if (id == 34675) {
        // ICC
        for (int off = tag.getReadOffset(); off < tag.getReadOffset() + tag.getReadlength(); off++) {
          data.put(input.readByte(off).toByte());
        }
        data.put((byte) 0);
      } else {
        switch (tag.getType()) {
          case 3:
            data.putShort((short) tt.toInt());
            break;
          case 8:
            data.putSShort((SShort) tt);
            break;
          case 4:
            data.putLong((Long) tt);
            break;
          case 9:
            data.putSLong((SLong) tt);
            break;
          case 11:
            data.putFloat((Float) tt);
            break;
          case 13:
            data.putInt(tt.toInt());
            break;
          case 5:
            data.putRational((Rational) tt);
            break;
          case 10:
            data.putSRational((SRational) tt);
            break;
          case 12:
            data.putDouble((Double) tt);
            break;
          case 2:
            data.put(tt.toByte());
            break;
          default:
            data.put(tt.toByte());
            break;
        }
      }
    }
  }

  /**
   * Write strip data.
   *
   * @param ifd the ifd
   * @return the array list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private ArrayList<Integer> writeStripData(IFD ifd) throws IOException {
    ArrayList<Integer> newStripOffsets = new ArrayList<Integer>();
    IfdTags metadata = ifd.getMetadata();
    TagValue stripOffsets = metadata.get(273);
    TagValue stripSizes = metadata.get(279);
    for (int i = 0; i < stripOffsets.getCardinality(); i++) {
      int pos = (int) data.position();
      newStripOffsets.add(pos);
      int start = stripOffsets.getValue().get(i).toInt();
      int size = stripSizes.getValue().get(i).toInt();
      this.input.seekOffset(start);
      for (int off = start; off < start + size; off++) {
        byte v = this.input.readDirectByte();
        data.put(v);
      }
      if (data.position() % 2 != 0) {
        // Correct word alignment
        data.put((byte) 0);
      }
    }
    return newStripOffsets;
  }

  /**
   * Write tile data.
   *
   * @param ifd the ifd
   * @return the array list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private ArrayList<Integer> writeTileData(IFD ifd) throws IOException {
    ArrayList<Integer> newTileOffsets = new ArrayList<Integer>();
    IfdTags metadata = ifd.getMetadata();
    TagValue tileOffsets = metadata.get(324);
    TagValue tileSizes = metadata.get(325);
    for (int i = 0; i < tileOffsets.getCardinality(); i++) {
      int pos = (int) data.position();
      if (pos % 2 != 0) {
        // Correct word alignment
        data.put((byte) 0);
        pos = (int) data.position();
      }
      newTileOffsets.add(pos);
      this.input.seekOffset(tileOffsets.getValue().get(i).toInt());
      for (int j = 0; j < tileSizes.getValue().get(i).toInt(); j++) {
        byte v = this.input.readDirectByte();
        data.put(v);
      }
      if (data.position() % 2 != 0) {
        // Correct word alignment
        data.put((byte) 0);
      }
    }
    return newTileOffsets;
  }
}

