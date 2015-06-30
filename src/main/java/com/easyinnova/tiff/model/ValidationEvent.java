/**
 * <h1>ValidationError.java</h1>
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
 * @since 18/5/2015
 *
 */
package main.java.com.easyinnova.tiff.model;

/**
 * The object that contains an error in the validation of a Tiff file.
 */
public class ValidationEvent {

  /** The error description. */
  private String description;

  /** The value that has thrown the error. */
  private String value;

  /** The level. */
  private int level;

  /**
   * Default constructor.
   */
  public ValidationEvent() {
    description = "";
    value = null;
    level = 1;
  }

  /**
   * Parameterized constructor.
   *
   * @param desc error description
   * @param value the value that caused the error
   */
  public ValidationEvent(String desc, String value) {
    this.description = desc;
    this.value = value;
  }

  /**
   * Prints the error in the console.
   */
  public void printError() {
    System.out.print(description);
    if (value != null)
      System.out.print(" (" + value + ")");
    System.out.println();
  }

  /**
   * Prints the warning in the console.
   */
  public void printWarning() {
    System.out.print("Warning: ");
    System.out.print(description);
    if (value != null)
      System.out.print(" (" + value + ")");
    System.out.println();
  }

  /**
   * Gets the level.
   *
   * @return the level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the erroneous value.
   *
   * @return the erroneous value
   */
  public String getInvalidValue() {
    return value;
  }
}

