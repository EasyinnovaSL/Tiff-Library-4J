/**
 * <h1>Short.java</h1>
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 27/5/2015
 */
package com.easyinnova.iptc;

import com.easyinnova.tiff.model.types.Byte;

import java.util.List;

/**
 * The Class Short.
 */
public class Time extends abstractIptcType {

  /**
   * The value.
   */
  private java.lang.String value;

  /**
   * Instantiates a new Time.
   */
  public Time() {
    super();
    this.value = "";
    setType(4);
  }

  /**
   * Instantiates a new Time.
   *
   * @param value the value represented in List<Byte>
   */
  public void read(List<Byte> value) {
    this.value = "";
    for (int j = 0; j < value.size(); j++) {
      this.value += (char) value.get(j).toByte();
    }

  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public java.lang.String getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(java.lang.String value) {
    this.value = value;
  }

  @Override
  public java.lang.String toString() {
    java.lang.String svalue = value;
    try {
      svalue =
          value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6) + " "
              + value.substring(7, 9) + ":" + value.substring(9, 11);
    } catch (Exception e) {

    }
    return svalue;
  }

}
