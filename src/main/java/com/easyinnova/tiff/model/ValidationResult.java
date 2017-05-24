/**
 * <h1>ValidationResult.java</h1>
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
package com.easyinnova.tiff.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Object that stores the results of the validation, errors and warnings.
 */
public class ValidationResult implements Serializable {

  /** Errors List. */
  public ArrayList<ValidationEvent> errors;

  /** Warnings List. */
  public ArrayList<ValidationEvent> warnings;

  /** Global correctness of the Tiff file. */
  public boolean correct;

  private boolean validate;

  private boolean fatalError;

  private String fatalErrorMessage;

  /**
   * Default constructor.
   */
  public ValidationResult(boolean validate) {
    errors = new ArrayList<ValidationEvent>();
    warnings = new ArrayList<ValidationEvent>();
    correct = true;
    this.validate = validate;
    fatalError = false;
    fatalErrorMessage = "";
  }

  /**
   * Adds an error.
   *
   * @param desc error description
   * @param value the value that caused the error
   * @param loc the error location
   */
  private void iaddError(String desc, String value, String loc) {
    if (!validate) return;
    ValidationEvent ve = new ValidationEvent(desc, value, loc);
    errors.add(ve);
    correct = false;
  }

  /**
   * Adds a warning.
   *
   * @param desc warning description
   * @param value the value that caused the warning
   * @param loc the location
   */
  private void iaddWarning(String desc, String value, String loc) {
    if (!validate) return;
    ValidationEvent ve = new ValidationEvent(desc, value, loc);
    warnings.add(ve);
  }

  /**
   * Adds an error.
   *
   * @param desc Error description
   * @param loc Error Location
   * @param value the integer value that caused the error
   */
  public void addError(String desc, String loc, long value) {
    iaddError(desc, "" + value, loc);
  }


  /**
   * Adds an error.
   *
   * @param desc Error description
   * @param loc Error Location
   * @param value the value that caused the error
   */
  public void addError(String desc, String loc, String value) {
    iaddError(desc, "" + value, loc);
  }

  /**
   * Adds an error.
   *
   * @param desc Error description
   * @param loc Error Location
   * @param value the float value that caused the error
   */
  public void addError(String desc, String loc, float value) {
    iaddError(desc, "" + value, loc);
  }

  /**
   * Adds an error.
   *
   * @param desc Error description
   * @param loc the location
   */
  public void addErrorLoc(String desc, String loc) {
    iaddError(desc, null, loc);
  }

  public void setFatalError(boolean value, String message) {
    fatalError = value;
    if (fatalErrorMessage.length() > 0) fatalErrorMessage += "\n";
    fatalErrorMessage += message;
  }

  public boolean getFatalError() {
    return fatalError;
  }

  public String getFatalErrorMessage() { return fatalErrorMessage; }

  /**
   * Adds an warning.
   *
   * @param desc Warning description
   * @param value the String that caused the warning
   * @param loc the location
   */
  public void addWarning(String desc, String value, String loc) {
    iaddWarning(desc, value, loc);
  }

  /**
   * Adds a validation result to this.
   *
   * @param validation the validation to add
   */
  public void add(ValidationResult validation) {
    correct &= validation.correct;
    if (!validate) return;
    errors.addAll(validation.errors);
    warnings.addAll(validation.warnings);
  }

  /**
   * Prints the errors to the console.
   */
  public void printErrors() {
    for (ValidationEvent ve : errors) {
      ve.printError();
    }
  }

  /**
   * Prints the warnings to the console.
   */
  public void printWarnings() {
    for (ValidationEvent ve : warnings) {
      ve.printWarning();
    }
  }

  /**
   * Checks if is correct.
   *
   * @return true, if is correct
   */
  public boolean isCorrect() {
    return correct;
  }

  /**
   * Gets the errors.
   *
   * @return the errors
   */
  public ArrayList<ValidationEvent> getErrors() {
    return errors;
  }

  /**
   * Gets the warnings.
   *
   * @return the warnings
   */
  public ArrayList<ValidationEvent> getWarnings() {
    return warnings;
  }
}
