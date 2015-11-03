/**
 * <h1>TiffStreamIO.java</h1>
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
 * @since 22/5/2015
 *
 */
package com.easyinnova.tiff.io;

import com.easyinnova.tiff.model.types.Double;
import com.easyinnova.tiff.model.types.Float;
import com.easyinnova.tiff.model.types.Long;
import com.easyinnova.tiff.model.types.Rational;
import com.easyinnova.tiff.model.types.SLong;
import com.easyinnova.tiff.model.types.SRational;
import com.easyinnova.tiff.model.types.SShort;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

/**
 * The Class TiffOutputStream.
 */
public class TiffOutputStream {

  /** The original file */
  TiffInputStream originalFile;

  /** The a file. */
  RandomAccessFile aFile;

  /** The filename. */
  String filename;

  /** The byte order. */
  ByteOrder byteOrder;

  /** The position. */
  int position;

  /**
   * Instantiates a new tiff stream io.
   *
   * @param in the input stream
   */
  public TiffOutputStream(TiffInputStream in) {
    originalFile = in;
    position = 0;
    byteOrder = ByteOrder.BIG_ENDIAN;
  }

  /**
   * Gets the byte order.
   *
   * @return the byteorder
   */
  public ByteOrder getByteOrder() {
    return byteOrder;
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
   * Create stream.
   *
   * @param filename the filename
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void create(String filename) throws IOException {
    this.filename = filename;
    aFile = new RandomAccessFile(filename, "rw");
    // channel = aFile.getChannel();
    // data = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10000000);
  }

  /**
   * Close.
   */
  public void close() {
    try {
      aFile.close();
    } catch (Exception ex) {
      /*everything is ok*/
    }
  }

  /**
   * Seek.
   *
   * @param offset the offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(int offset) throws IOException {
    aFile.seek(offset);
    position = offset;
  }

  /**
   * Puts a byte.
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void put(byte val) throws IOException {
    aFile.write(val);
    position++;
  }

  /**
   * Reads a byte.
   *
   * @param offset the position
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public byte get(int offset) throws IOException {
    return originalFile.readByte(offset).toByte();
  }

  /**
   * Puts a short (2 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putShort(short val) throws IOException {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
      aFile.writeShort(val);
    else {
      aFile.write((val >>> 0) & 0xFF);
      aFile.write((val >>> 8) & 0xFF);
    }
    position += 2;
  }

  /**
   * Puts a sShort (2 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putSShort(SShort val) throws IOException {
    putShort(val.getValue());
  }

  /**
   * Puts a int (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putInt(int val) throws IOException {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
      aFile.writeInt(val);
    else {
      aFile.write((val >>> 0) & 0xFF);
      aFile.write((val >>> 8) & 0xFF);
      aFile.write((val >>> 16) & 0xFF);
      aFile.write((val >>> 24) & 0xFF);
    }
    position += 4;
  }

  /**
   * Puts a Long (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putLong(Long val) throws IOException {
    putInt(val.getInternalValue());
  }

  /**
   * Puts a SLong (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putSLong(SLong val) throws IOException {
    putInt(val.getValue());
  }

  /**
   * Puts a Rational (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putRational(Rational val) throws IOException {
    putInt(val.getNumerator());
    putInt(val.getDenominator());
  }

  /**
   * Puts a SRational (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putSRational(SRational val) throws IOException {
    putInt(val.getNumerator());
    putInt(val.getDenominator());
  }

  /**
   * Puts a float (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putFloat(Float val) throws IOException {
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      aFile.writeFloat(val.getValue());
      position += 4;
    } else {
      putInt(java.lang.Float.floatToIntBits(val.getValue()));
    }
  }

  /**
   * Puts a double (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putDouble(Double val) throws IOException {
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      aFile.writeDouble(val.getValue());
    } else {
      long v = java.lang.Double.doubleToLongBits(val.getValue());
      aFile.write((int) (v >>> 0) & 0xFF);
      aFile.write((int) (v >>> 8) & 0xFF);
      aFile.write((int) (v >>> 16) & 0xFF);
      aFile.write((int) (v >>> 24) & 0xFF);
      aFile.write((int) (v >>> 32) & 0xFF);
      aFile.write((int) (v >>> 40) & 0xFF);
      aFile.write((int) (v >>> 48) & 0xFF);
      aFile.write((int) (v >>> 56) & 0xFF);
    }
    position += 8;
  }

  /**
   * Position.
   *
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public long position() throws IOException {
    return position;
  }
}

