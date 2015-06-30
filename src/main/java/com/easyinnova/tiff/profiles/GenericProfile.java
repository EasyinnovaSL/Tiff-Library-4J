/**
 * <h1>GenericProfile.java</h1> 
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 17/6/2015
 *
 */
package main.java.com.easyinnova.tiff.profiles;

import main.java.com.easyinnova.tiff.model.TiffDocument;
import main.java.com.easyinnova.tiff.model.ValidationResult;

/**
 * The Generic class for Profile validation.
 */
public class GenericProfile {
  /** The image file descriptor to validate. */
  protected TiffDocument model;

  /** The result of the check. */
  protected ValidationResult validation;

  /**
   * Instantiates a new baseline profile.
   *
   * @param doc the tiff file model
   */
  public GenericProfile(TiffDocument doc) {
    validation = new ValidationResult();
    this.model = doc;
  }

  /**
   * Gets the validation result.
   *
   * @return the validation
   */
  public ValidationResult getValidation() {
    return validation;
  }
}

