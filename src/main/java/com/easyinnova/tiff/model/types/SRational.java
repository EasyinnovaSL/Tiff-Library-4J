/**
 * <h1>SRational.java</h1>
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 28/5/2015
 *
 */
package main.java.com.easyinnova.tiff.model.types;

/**
 * The Class SRational.
 */
public class SRational extends abstractTiffType {

  /** The numerator. */
  private SLong numerator;

  /** The denominator. */
  private SLong denominator;

  /**
   * Instantiates a new rational.
   *
   * @param numerator the SLONG numerator
   * @param denominator the SLONG denominator
   */
  public SRational(int numerator, int denominator) {
    super();
    this.numerator = new SLong(numerator);
    this.denominator = new SLong(denominator);
    setTypeSize(8);
  }

  /**
   * Gets the numerator.
   *
   * @return the numerator
   */
  public int getNumerator() {
    return numerator.toInt();
  }

  /**
   * Sets the numerator.
   *
   * @param numerator the new numerator
   */
  public void setNumerator(int numerator) {
    this.numerator = new SLong(numerator);
  }

  /**
   * Gets the denominator.
   *
   * @return the denominator
   */
  public int getDenominator() {
    return denominator.toInt();
  }

  /**
   * Sets the denominator.
   *
   * @param denominator the new denominator
   */
  public void setDenominator(int denominator) {
    this.denominator = new SLong(denominator);
  }

  @Override
  public String toString() {
    return numerator.toString() + "/" + denominator.toString();
  }
}

