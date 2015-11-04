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
import java.nio.ByteOrder;

/**
 * The Class TiffOutputStream.
 */
public class TiffOutputStream {

  /** The original file */
  TiffInputStream originalFile;

  /** The filename. */
  String filename;

  /** The byte order. */
  ByteOrder byteOrder;

  /** The output. */
  OutputBuffer output;

  /**
   * Instantiates a new tiff stream io.
   *
   * @param in the input stream
   */
  public TiffOutputStream(TiffInputStream in) {
    originalFile = in;
    byteOrder = ByteOrder.BIG_ENDIAN;
    output = new OutputBuffer(byteOrder);
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
    output.Create(filename);
  }

  /**
   * Close.
   */
  public void close() {
    output.close();
  }

  /**
   * Seek.
   *
   * @param offset the offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(int offset) throws IOException {
    output.seek(offset);
  }

  /**
   * Puts a byte.
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void put(byte val) throws IOException {
    writeByteCurrentPosition(val);
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
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      writeIntCurrentPosition((val >>> 8) & 0xFF);
      writeIntCurrentPosition((val >>> 0) & 0xFF);
    }
    else {
      writeIntCurrentPosition((val >>> 0) & 0xFF);
      writeIntCurrentPosition((val >>> 8) & 0xFF);
    }
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
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      writeIntCurrentPosition((val >>> 24) & 0xFF);
      writeIntCurrentPosition((val >>> 16) & 0xFF);
      writeIntCurrentPosition((val >>> 8) & 0xFF);
      writeIntCurrentPosition((val >>> 0) & 0xFF);
    }
    else {
      writeIntCurrentPosition((val >>> 0) & 0xFF);
      writeIntCurrentPosition((val >>> 8) & 0xFF);
      writeIntCurrentPosition((val >>> 16) & 0xFF);
      writeIntCurrentPosition((val >>> 24) & 0xFF);
    }
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
    putInt(java.lang.Float.floatToIntBits(val.getValue()));
  }

  /**
   * Puts a double (4 bytes).
   *
   * @param val the val
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void putDouble(Double val) throws IOException {
    long v = java.lang.Double.doubleToLongBits(val.getValue());
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      writeIntCurrentPosition((int) (v >>> 56) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 48) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 40) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 32) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 24) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 16) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 8) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 0) & 0xFF);
    } else {
      writeIntCurrentPosition((int) (v >>> 0) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 8) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 16) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 24) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 32) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 40) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 48) & 0xFF);
      writeIntCurrentPosition((int) (v >>> 56) & 0xFF);
    }
  }

  /**
   * Write byte.
   *
   * @param v the v
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeByteCurrentPosition(byte v) throws IOException {
    output.writeByteCurrentPosition(v);
  }

  /**
   * Write byte.
   *
   * @param v the v
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeIntCurrentPosition(int v) throws IOException {
    output.writeIntCurrentPosition(v);
  }

  /**
   * Position.
   *
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public long position() throws IOException {
    return output.position();
  }
}

