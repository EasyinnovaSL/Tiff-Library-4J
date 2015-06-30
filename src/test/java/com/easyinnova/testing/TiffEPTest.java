/**
 * <h1>TiffEPTest.java</h1>
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
 * @since 18/6/2015
 *
 */
package test.java.com.easyinnova.testing;

import static org.junit.Assert.assertEquals;
import main.java.com.easyinnova.tiff.model.TagValue;
import main.java.com.easyinnova.tiff.model.TiffDocument;
import main.java.com.easyinnova.tiff.model.types.IFD;
import main.java.com.easyinnova.tiff.profiles.TiffEPProfile;
import main.java.com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class TiffEPTest.
 */
public class TiffEPTest {

  /** The tr. */
  TiffReader tr;

  /** The to. */
  TiffDocument to;

  /** The tv. */
  TagValue tv;

  /** The ifd. */
  IFD ifd;

  /** The result. */
  int result;

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
  public void invalidTest() {
    // Image 1
    result = tr.readFile("src\\test\\resources\\Small\\Grey_stripped.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();

    TiffEPProfile bp = new TiffEPProfile(to);
    bp.validate();
    assertEquals(false, bp.getValidation().correct);
    assertEquals(12, bp.getValidation().errors.size());
    assertEquals(0, bp.getValidation().warnings.size());
  }
}

