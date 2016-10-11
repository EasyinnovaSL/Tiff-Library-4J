package com.easyinnova.tiff.io;

import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by easy on 09/08/2016.
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
