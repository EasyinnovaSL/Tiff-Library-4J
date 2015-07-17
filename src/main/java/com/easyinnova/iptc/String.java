/**
 * <h1>String.java</h1>
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
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 6/7/2015
 */
package com.easyinnova.iptc;

import com.easyinnova.tiff.model.types.Byte;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * The Class String.
 */
public class String extends abstractIptcType {

  /**
   * The value.
   */
  private java.lang.String value;

  /**
   * Instantiates a new String.
   */
  public String() {
    super();
    this.value = "";
    setType(2);
  }

    /**
     * Instantiates a new String.
     *
     * @param value the value represented in List<Byte>
     */
    public void read(List<Byte> value) {
        try {
            byte[] arrayContent = new byte[value.size()];
            for (int j = 0; j < value.size(); j++) {
                arrayContent[j] = value.get(j).toByte();
            }

            this.value = new java.lang.String(arrayContent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error encoding value");
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
    return value;
  }

}
