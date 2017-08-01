/**
 * <h1>ByteOrder.java</h1>
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

  /** Do not modify! */
  private static final long serialVersionUID = 2946L;

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
