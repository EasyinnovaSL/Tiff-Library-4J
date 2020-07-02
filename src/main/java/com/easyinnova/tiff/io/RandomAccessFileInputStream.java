/**
 * <h1>RandomAccessFileInputStream.java</h1>
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 26/5/2015
 */
package com.easyinnova.tiff.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * The Class RandomAccessFileInputStream.
 */
public class RandomAccessFileInputStream extends InputStream {

  /**
   * The random access file.
   */
  private final RandomAccessFile randomAccessFile;
  /**
   * Absolute position in file to finish reading on (exclusive)
   */
  private long limit = -1;

  private String path;

  /**
   * Instantiates a new tiff file input stream.
   *
   * @param file the file
   * @throws FileNotFoundException the file not found exception
   */
  public RandomAccessFileInputStream(File file) throws FileNotFoundException {
    path = file.getPath();
    randomAccessFile = new RandomAccessFile(file, "r");
  }

  public RandomAccessFileInputStream(final RandomAccessFile raf) throws FileNotFoundException {
    this(raf, "");
  }

  public RandomAccessFileInputStream(final RandomAccessFile raf, final String path) throws FileNotFoundException {
    this.path = path;
	this.randomAccessFile = raf;
  }

  public String getPath() {
    return path;
  }

  @Override public int read() throws IOException {
    return randomAccessFile.read();
  }

  @Override public int read(byte[] b) throws IOException {
    return randomAccessFile.read(b);
  }

  @Override public int read(byte[] b, int off, int len) throws IOException {
    return randomAccessFile.read(b, off, len);
  }

  @Override public int available() throws IOException {
    long a = (limit >= 0 ? Math.min(limit, randomAccessFile.length()) : randomAccessFile.length())
        - randomAccessFile.getFilePointer();
    return (int) Math.min(Integer.MAX_VALUE, Math.max(a, 0));
  }

  @Override public synchronized void reset() throws IOException {
    limit = -1;
    randomAccessFile.seek(0);
  }

  @Override public void close() throws IOException {
    randomAccessFile.close();
  }

  /**
   * Seek.
   *
   * @param pos the pos
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(long pos) throws IOException {
    randomAccessFile.seek(pos);
  }

  /**
   * Set Limit.
   *
   * @param limit the limit
   */
  public void limit(long limit) {
    this.limit = limit;
  }

  /**
   * Size.
   *
   * @return the int
   */
  public long size() {
    try {
      return randomAccessFile.length();
    } catch (Exception ex) {
      return 0;
    }
  }

}
