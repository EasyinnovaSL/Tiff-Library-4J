/**
 * <h1>Test1.java</h1> <p> This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version; or, at your choice, under
 * the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p> <p> This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License and the Mozilla Public License for more details. </p> <p> You should have received
 * a copy of the GNU General Public License and the Mozilla Public License along with this program.
 * If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the ©
 * statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> © 2015
 * Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 21/5/2015
 */
package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Test;

import java.io.File;

/**
 * Testing class.
 */
public class TiffValidInvalidTests {
  /**
   * Valid examples set.
   */
  @Test
  public void ValidExamples() {
    TiffReader tr;
    int result;

    try {
      tr = new TiffReader();

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Intel.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Motorola.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Colorspace" + separator + "F32.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Recommended list.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Old school E.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky multistrip.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky singlestrip.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky tile.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar multistrip.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar singlestrip.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar tile.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola nopred nocomp.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola pred nocomp.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel nopred nocomp.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel pred nocomp.TIF");
      assertEquals(0, result);
      assertEquals(true, tr.getValidation().isCorrect());

    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  /**
   * Invalid examples set.
   */
  @Test
  public void InvalidExamples() {
    TiffReader tr;
    int result;

    try {
      tr = new TiffReader();

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Nonsense byteorder E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Insane tag count E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Circular E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Circular Short E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Beyond EOF E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());

      result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Premature EOF E.TIF");
      assertEquals(0, result);
      assertEquals(false, tr.getValidation().isCorrect());
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}
