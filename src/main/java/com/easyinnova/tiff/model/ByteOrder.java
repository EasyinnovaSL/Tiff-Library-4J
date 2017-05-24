package com.easyinnova.tiff.model;

import java.io.Serializable;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class ByteOrder implements Serializable {
  private String name;

  private ByteOrder(String name) {
    this.name = name;
  }

  /**
   * Constant denoting big-endian byte order.  In this order, the bytes of a
   * multibyte value are ordered from most significant to least significant.
   */
  public static final ByteOrder BIG_ENDIAN
      = new ByteOrder("BIG_ENDIAN");

  /**
   * Constant denoting little-endian byte order.  In this order, the bytes of
   * a multibyte value are ordered from least significant to most
   * significant.
   */
  public static final ByteOrder LITTLE_ENDIAN
      = new ByteOrder("LITTLE_ENDIAN");

  /**
   * Constructs a string describing this object.
   *
   * <p> This method returns the string <tt>"BIG_ENDIAN"</tt> for {@link
   * #BIG_ENDIAN} and <tt>"LITTLE_ENDIAN"</tt> for {@link #LITTLE_ENDIAN}.
   * </p>
   *
   * @return  The specified string
   */
  public String toString() {
    return name;
  }
}
