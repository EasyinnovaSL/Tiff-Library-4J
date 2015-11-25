/**
 * <h1>TiffITProfileTest.java</h1>
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
 * @since 24/11/2015
 *
 */
package com.easyinnova.tiff.profiles;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class TiffITProfileTest.
 */
public class TiffITProfileTest {

  /** The tr. */
  TiffReader tr;

  /** The result. */
  int result;

  /** The to. */
  TiffDocument to;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  /**
   * Test.
   */
  @Test
  public void profileIT0() {
    TiffITProfile bp;

    // Valid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator
 + "IT Samples"
            + separator + "sample-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 0);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 0);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 0);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "IMG_0887_EP.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 0);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    // Invalid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
            + separator + "Bilevel.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 0);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);
  }

  /**
   * Test.
   */
  @Test
  public void profileIT1() {
    TiffITProfile bp;

    // Valid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 1);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1-c2.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    // Invalid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 1);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 1);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "IMG_0887_EP.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 1);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);
  }



  /**
   * Test.
   */
  @Test
  public void profileIT2() {
    TiffITProfile bp;

    // Valid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1-c2.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(true, bp.getValidation().correct);

    // Invalid
    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);

    result =
        tr.readFile("src" + separator + "test" + separator + "resources" + separator + "IT Samples"
            + separator + "sample-cmyk-IT1-c.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getBaselineValidation().correct);
    to = tr.getModel();
    bp = new TiffITProfile(to, 2);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);
  }
}

