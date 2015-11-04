/**
 * <h1>OutputBuffer.java</h1> 
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
 * @since 4/11/2015
 *
 */
package com.easyinnova.tiff.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

/**
 * The Class OutputBuffer.
 */
public class OutputBuffer {
  /** The internal buffer. */
  private int[] buffer;

  /** The internal buffer flag to know if the data is byte or int. */
  private boolean[] isByte;

  /** The maximum internal buffer size. */
  private int maxBufferSize = 100;

  /** The current buffer size. */
  private int currentBufferSize;

  /** The buffer offset (file position of the 0th element). */
  private long bufferOffset;

  /** The byte order. */
  ByteOrder byteOrder;

  /** The a file. */
  RandomAccessFile aFile;

  /** The position. */
  int position;

  /**
   * Instantiates a new output buffer.
   *
   * @param byteOrder the byte order
   */
  public OutputBuffer(ByteOrder byteOrder) {
    this.byteOrder = byteOrder;
    bufferOffset = 0;
    currentBufferSize = 0;
    if (maxBufferSize >= 0) {
      newBuffer(0);
    }
  }

  /**
   * Creates the file.
   *
   * @param filename the filename
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void Create(String filename) throws IOException {
    aFile = new RandomAccessFile(filename, "rw");
  }

  /**
   * Close.
   */
  public void close() {
    try {
      writeBuffer();
      aFile.close();
    } catch (Exception ex) {
      /* everything is ok */
    }
  }

  /**
   * Seek.
   *
   * @param offset the offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(int offset) throws IOException {
    writeBuffer();
    newBuffer(offset);
    aFile.seek(offset);
    position = offset;
  }

  /**
   * New buffer.
   *
   * @param offset the offset
   */
  private void newBuffer(int offset) {
    if (maxBufferSize >= 0) {
      buffer = new int[maxBufferSize];
      isByte = new boolean[maxBufferSize];
      bufferOffset = offset;
      currentBufferSize = 0;
    }
  }

  /**
   * Write byte.
   *
   * @param v the v
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeByteCurrentPosition(byte v) throws IOException {
    if (position - bufferOffset < maxBufferSize) {
      int pos = position - (int) bufferOffset;
      buffer[pos] = v;
      isByte[pos] = true;
      currentBufferSize++;
      if (currentBufferSize == maxBufferSize) {
        writeBuffer();
        newBuffer(position + 1);
      }
    } else {
      aFile.write(v);
    }
    position++;
  }

  /**
   * Write byte.
   *
   * @param v the v
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeIntCurrentPosition(int v) throws IOException {
    if (position - bufferOffset < maxBufferSize) {
      int pos = position - (int) bufferOffset;
      buffer[pos] = v;
      isByte[pos] = false;
      currentBufferSize++;
      if (currentBufferSize == maxBufferSize) {
        writeBuffer();
        newBuffer(position + 1);
      }
    } else {
      aFile.write(v);
    }
    position++;
  }

  /**
   * Write buffer.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void writeBuffer() throws IOException {
    for (int pos = 0; pos < currentBufferSize; pos++) {
      if (isByte[pos]) {
        aFile.write((byte) buffer[pos]);
      } else {
        aFile.write(buffer[pos]);
      }
    }
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

