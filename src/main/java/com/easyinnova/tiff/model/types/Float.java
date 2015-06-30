/**
 * <h1>Float.java</h1> 
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
<<<<<<< HEAD
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
=======
 * NB: for the � statement, include Easy Innova SL or other company/Person contributing the code.
>>>>>>> refs/heads/tiffinputstream
 * </p>
 * <p>
<<<<<<< HEAD
 * © 2015 Easy Innova, SL
=======
 * � 2015 Easy Innova, SL
>>>>>>> refs/heads/tiffinputstream
 * </p>
 *
<<<<<<< HEAD
 * @author Víctor Muñoz Solà
=======
 * @author V�ctor Mu�oz Sol�
>>>>>>> refs/heads/tiffinputstream
 * @version 1.0
 * @since 29/5/2015
 *
 */
package main.java.com.easyinnova.tiff.model.types;

/**
 * The Class Float.
 */
public class Float extends abstractTiffType {

  /** The value. */
  private float value;

  /**
   * Instantiates a new short.
   *
   * @param value the value
   */
  public Float(float value) {
    super();
    this.value = value;
    setTypeSize(4);
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public float getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "" + value;
  }

}

