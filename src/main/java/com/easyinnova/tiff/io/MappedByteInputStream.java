/**
 * <h1>MappedByteInputStream.java</h1>
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
 */

package com.easyinnova.tiff.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Victor Munoz on 09/08/2016.
 */
public class MappedByteInputStream extends InputStream {

  private MappedByteBuffer mb;
  private long mbsize;
  private FileChannel ch;
  private FileInputStream f;
  private String path;

  /**
   * Instantiates a new tiff file input stream.
   *
   * @param file the file
   * @throws FileNotFoundException the file not found exception
   */
  public MappedByteInputStream(File file) throws FileNotFoundException {
    path = file.getPath();
    f = new FileInputStream(file);
    ch = f.getChannel();
    try {
      mbsize = ch.size();
      mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, mbsize);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public MappedByteInputStream(RandomAccessFile raf) throws FileNotFoundException {
    path = "";
    ch = raf.getChannel();
    try {
      mbsize = ch.size();
      mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, mbsize);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public String getPath() {
    return path;
  }

  @Override public int read() throws IOException {
    int val2;
    try {
      val2 = mb.get();
      if (val2 < 0) val2 = 256 + val2;
    } catch (BufferUnderflowException ex) {
      val2 = -1;
    }
    return val2;
  }

  @Override public void close() throws IOException {
    try { ch.close(); } catch (Exception ex) { }
    try { f.close(); } catch (Exception ex) { }
    closeDirectBuffer(mb);
  }

  private void closeDirectBuffer(MappedByteBuffer cb) {
    if (cb==null || !cb.isDirect()) return;

    // we could use this type cast and call functions without reflection code,
    // but static import from sun.* package is risky for non-SUN virtual machine.
    //try { ((sun.nio.ch.DirectBuffer)cb).cleaner().clean(); } catch (Exception ex) { }
    try {
      Method cleaner = cb.getClass().getMethod("cleaner");
      cleaner.setAccessible(true);
      Method clean = Class.forName("sun.misc.Cleaner").getMethod("clean");
      clean.setAccessible(true);
      clean.invoke(cleaner.invoke(cb));
    } catch(Exception ex) { }
    cb = null;
  }

  /**
   * Seek.
   *
   * @param pos the pos
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(long pos) throws IOException {
    mb.position((int)pos);
  }

  /**
   * Size.
   *
   * @return the int
   */
  public long size() {
    return mbsize;
  }
}
