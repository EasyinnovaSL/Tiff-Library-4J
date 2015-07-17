/**
 * <h1>abstractTiffTag.java</h1>
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
 * @since 26/5/2015
 */
package com.easyinnova.iptc;

import com.easyinnova.tiff.model.types.Byte;

import java.util.List;

/**
 * The generic class abstractTiffType.
 */
public class abstractIptcType {

  /**
   * The tag size in bytes.
   */
  private int type;

  /**
   * Instantiates a new abstract tiff type.
   */
  public abstractIptcType() {
    type = 0; // Undefined
  }

  /**
   * Instantiates a new abstract tiff type.
   *
   * @param tv the tv
   */
  public abstractIptcType(List<Byte> tv) {
    type = 0; // Undefined
  }

  /**
   * Sets the type size in bytes.
   *
   * @param size the new type size
   */
  public void setType(int size) {
    type = size;
  }

  /**
   * Gets the type size in bytes.
   *
   * @return the type size
   */
  public int getTypeSize() {
    return type;
  }

  /**
   * Read.
   *
   * @param tv the tv
   */
  public void read(List<Byte> tv) {}

}
