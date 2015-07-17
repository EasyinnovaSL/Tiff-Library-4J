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

import com.easyinnova.tiff.model.types.Ascii;
import com.easyinnova.tiff.model.types.Byte;
import com.easyinnova.tiff.model.types.Double;
import com.easyinnova.tiff.model.types.Float;
import com.easyinnova.tiff.model.types.Long;
import com.easyinnova.tiff.model.types.Rational;
import com.easyinnova.tiff.model.types.SByte;
import com.easyinnova.tiff.model.types.SLong;
import com.easyinnova.tiff.model.types.SRational;
import com.easyinnova.tiff.model.types.SShort;
import com.easyinnova.tiff.model.types.Short;
import com.easyinnova.tiff.model.types.Undefined;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * The Class TiffStreamIO.
 * 
 * @deprecated use {TiffInputStream} instead.
 */
@Deprecated
public class TiffStreamIO extends FilterInputStream implements TiffDataIntput {

  /**
   * Instantiates a new tiff stream io.
   *
   * @param in the inpu stream
   */
  public TiffStreamIO(InputStream in) {
    super(in);
  }

  /** The data. */
  MappedByteBuffer data;

  /** The a file. */
  RandomAccessFile aFile;

  /** The channel. */
  FileChannel channel;

  /** The filename. */
  String filename;

  /** The byte order. */
  ByteOrder byteOrder;

  /**
   * Read.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
  public void load(String filename) throws Exception {
    this.filename = filename;
    aFile = new RandomAccessFile(filename, "rw");
    channel = aFile.getChannel();
    data = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());

    // loads the entire file into memory
    data.load();
  }

  /**
   * Writes the header.
   */
  public void writeHeader() {
    if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
      data.put((byte) 'I');
      data.put((byte) 'I');
    } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
      data.put((byte) 'M');
      data.put((byte) 'M');
    }
    data.order(byteOrder);

    data.putShort((short) 42);

    data.putInt(8);
  }

  /**
   * Write.
   *
   * @param filename the filename
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void write(String filename) throws IOException {
    this.filename = filename;
    aFile = new RandomAccessFile(filename, "rw");
    channel = aFile.getChannel();
    data = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10000000);
  }

  /**
   * Close.
   */
  public void close() {
    try {
      channel.close();
      aFile.close();
    } catch (Exception ex) {
      /*everything is ok*/
    }
  }

  /**
   * Gets a byte.
   *
   * @param offset the file position offset
   * @return the int
   */
  public int get(int offset) {
    return data.get(offset);
  }

  /**
   * Puts a byte.
   *
   * @param val the val
   */
  public void put(byte val) {
    data.put(val);
  }

  /**
   * Puts a short (2 bytes).
   *
   * @param val the val
   */
  public void putShort(short val) {
    data.putShort(val);
  }

  /**
   * Puts a int (4 bytes).
   *
   * @param val the val
   */
  public void putInt(int val) {
    data.putInt(val);
  }

  /**
   * Define Byte Order.
   *
   * @param byteOrder the byte order
   */
  public void order(ByteOrder byteOrder) {
    data.order(byteOrder);
  }

  /**
   * Gets a short (2 bytes).
   *
   * @param offset the file position offset
   * @return the short
   */
  public int getShort(int offset) {
    return data.getShort(offset);
  }

  /**
   * Gets a int (4 bytes).
   *
   * @param offset the file position offset
   * @return the int
   */
  public int getInt(int offset) {
    return data.getInt(offset);
  }

  /**
   * Gets a int (4 bytes).
   *
   * @param offset the file position offset
   * @return the int
   */
  public long getUInt(int offset) {
    return data.getInt(offset) & 0xFFFFFFFFL;
  }

  /**
   * Gets a long (8 bytes).
   *
   * @param offset the file position offset
   * @return the long
   */
  public long getLong(int offset) {
    return data.getLong(offset);
  }

  /**
   * Gets a ushort (8 bytes).
   *
   * @param offset the file position offset
   * @return the ushort
   */
  public int getUshort(int offset) {
    return getShort(offset) & 0xFFFF;
  }

  /**
   * Gets a float (4 bytes).
   *
   * @param offset the file position offset
   * @return the int
   */
  public float getFloat(int offset) {
    return data.getFloat(offset);
  }

  /**
   * Gets a double (4 bytes).
   *
   * @param offset the file position offset
   * @return the int
   */
  public double getDouble(int offset) {
    return data.getDouble(offset);
  }

  /**
   * Position.
   *
   * @return the int
   */
  public int position() {
    return data.position();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readByte()
   */
  public Byte readByte() throws IOException {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readAscii()
   */
  public Ascii readAscii() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readShort()
   */
  public Short readShort() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readLong()
   */
  public Long readLong() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readRational()
   */
  public Rational readRational() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readSByte()
   */
  public SByte readSByte() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readUndefined()
   */
  public Undefined readUndefined() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readSShort()
   */
  public SShort readSShort() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readSLong()
   */
  public SLong readSLong() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readSRational()
   */
  public SRational readSRational() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readFloat()
   */
  public Float readFloat() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.easyinnova.tiff.io.TiffDataIntput#readDouble()
   */
  public Double readDouble() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }
}

