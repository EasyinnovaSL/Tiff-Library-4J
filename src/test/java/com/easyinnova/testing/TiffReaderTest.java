/**
 * <h1>TiffReaderTest.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 2/6/2015
 */
package com.easyinnova.testing;

import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for the class TiffReader.
 */
public class TiffReaderTest {

  /**
   * The tr.
   */
  TiffReader tr;

  /**
   * The to.
   */
  TiffDocument to;

  /**
   * The tv.
   */
  TagValue tv;

  /**
   * The ifd.
   */
  IFD ifd;

  /**
   * The result.
   */
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
   * Test 1.
   */
  @Test
  public void Test1() {
    // Image 1
  }

  /**
   * Test2.
   */
  @Test
  public void Test2() {
    // Image 2
  }

  /**
   * Test3.
   */
  @Test
  public void Test3() {
    // Image 3
  }

  /**
   * Test4.
   */
  @Test
  public void Test4() {
    // Image 3
  }
}

