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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 26/5/2015
 *
 */
package main.java.com.easyinnova.tiff.model.types;

import main.java.com.easyinnova.tiff.model.Metadata;
import main.java.com.easyinnova.tiff.model.TagValue;
import main.java.com.easyinnova.tiff.model.TiffObject;

/**
 * The generic class abstractTiffType.
 */
public class abstractTiffType extends TiffObject {

  /** The tag size in bytes. */
  private int typeSize;

  /** The is ifd. */
  private boolean isIFD;

  /**
   * Instantiates a new abstract tiff type.
   */
  public abstractTiffType() {
    typeSize = 0; // Undefined
    isIFD = false;
  }

  /**
   * Sets the type size in bytes.
   *
   * @param size the new type size
   */
  public void setTypeSize(int size) {
    typeSize = size;
  }

  /**
   * Gets the type size in bytes.
   *
   * @return the type size
   */
  public int getTypeSize() {
    return typeSize;
  }

  /**
   * Convert the value to an integer.
   *
   * @return the int
   * @throws NumberFormatException Number format exception
   */
  public int toInt() throws NumberFormatException {
    return Integer.parseInt(this.toString());
  }

  /**
   * Read.
   *
   * @param tv the tv
   */
  public void read(TagValue tv) {
  }

  /**
   * Checks if is ifd.
   *
   * @return true, if is ifd
   */
  public boolean isIFD() {
    return isIFD;
  }

  /**
   * To uint.
   *
   * @return the char
   */
  public int toUint() {
    int res = toInt();
    if (res < 0)
      res += 256;
    return res;
  }

  /**
   * Sets the checks if is ifd.
   *
   * @param b the new checks if is ifd
   */
  protected void setIsIFD(boolean b) {
    isIFD = b;
  }

  /**
   * Contains metadata.
   *
   * @return true, if successful
   */
  public boolean containsMetadata() {
    return false;
  }

  /**
   * Creates the metadata.
   *
   * @return the hash map
   */
  public Metadata createMetadata() {
    return new Metadata();
  }
}
