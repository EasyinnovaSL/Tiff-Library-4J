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
package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.easyinnova.tiff.model.ImageStrips;
import com.easyinnova.tiff.model.ImageTiles;
import com.easyinnova.tiff.model.Strip;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.Tile;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for the class TiffReader.
 */
public class TiffStripsTilesTest {

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
    result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Bilevel.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(2, to.getIfdCount());
    ifd = to.getFirstIFD();

    assertNull(ifd.getImageTiles());


    ImageStrips imgStrps = ifd.getImageStrips();
    long rowsStrip = imgStrps.getRowsPerStrip();

    assertEquals(662, rowsStrip);

    for (Strip str : imgStrps.getStrips()) {
      assertEquals(str.getStripRows(), rowsStrip);
    }

  }

  /**
   * Test 2.
   */
  @Test
  public void Test2() {
    result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "RGB_stripped.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdCount());
    ifd = to.getFirstIFD();

    assertNull(ifd.getImageTiles());


    ImageStrips imgStrps = ifd.getImageStrips();
    long rowsStrip = imgStrps.getRowsPerStrip();

    assertEquals(21, rowsStrip);

//    for (Strip str : imgStrps.getStrips()) {
//      assertEquals(str.getStripRows(), rowsStrip);
//    }

    for (int i = 0; i < imgStrps.getStrips().size() - 1; i++) {
      Strip str = imgStrps.getStrips().get(i);
      assertEquals(str.getStripRows(), rowsStrip);
    }
    Strip str = imgStrps.getStrips().get(imgStrps.getStrips().size() - 1);
    assertEquals(str.getStripRows(), 11);
  }

  /**
   * Test 3.
   */
  @Test
  public void Test3() {
    result = tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar tile.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdCount());
    ifd = to.getFirstIFD();

    assertNull(ifd.getImageStrips());

    ImageTiles imgTls = ifd.getImageTiles();
    long width = imgTls.getTileWidth();
    long height = imgTls.getTileHeight();

    assertEquals(256, width);
    assertEquals(256, height);

    for (Tile tle : imgTls.getTiles()) {
      assertEquals(width, tle.getWidth());
      assertEquals(height, tle.getHeight());
    }

  }
}

