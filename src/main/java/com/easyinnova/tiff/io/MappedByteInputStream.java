package com.easyinnova.tiff.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by easy on 09/08/2016.
 */
public class MappedByteInputStream extends InputStream {

  private MappedByteBuffer mb;
  private long mbsize;

  /**
   * Instantiates a new tiff file input stream.
   *
   * @param file the file
   * @throws FileNotFoundException the file not found exception
   */
  public MappedByteInputStream(File file) throws FileNotFoundException {
    FileInputStream f = new FileInputStream(file);
    FileChannel ch = f.getChannel();
    try {
      mbsize = ch.size();
      mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, mbsize);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
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
