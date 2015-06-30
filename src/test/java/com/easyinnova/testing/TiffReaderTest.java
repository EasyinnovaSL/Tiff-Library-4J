/**
 * <h1>TiffReaderTest.java</h1>
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
 * @since 2/6/2015
 *
 */
package test.java.com.easyinnova.testing;

import static org.junit.Assert.assertEquals;
import main.java.com.easyinnova.tiff.model.ImageStrips;
import main.java.com.easyinnova.tiff.model.TagValue;
import main.java.com.easyinnova.tiff.model.TiffDocument;
import main.java.com.easyinnova.tiff.model.Tile;
import main.java.com.easyinnova.tiff.model.types.IFD;
import main.java.com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for the class TiffReader.
 */
public class TiffReaderTest {

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
   * Test 1.
   */
  @Test
  public void Test1() {
    // Image 1
    result = tr.readFile("src\\test\\resources\\Small\\Grey_stripped.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdCount());
    ifd = (IFD) to.getFirstIFD();

    tv = ifd.getTag("ImageWidth");
    assertEquals(1, tv.getCardinality());
    assertEquals(999, tv.getFirstNumericValue());

    tv = ifd.getTag("ImageLength");
    assertEquals(1, tv.getCardinality());
    assertEquals(662, tv.getFirstNumericValue());

    tv = ifd.getTag("BitsPerSample");
    assertEquals(1, tv.getCardinality());
    assertEquals(8, tv.getFirstNumericValue());

    tv = ifd.getTag("PhotometricInterpretation");
    assertEquals(1, tv.getCardinality());
    assertEquals(1, tv.getFirstNumericValue());

    tv = ifd.getTag("PlanarConfiguration");
    assertEquals(1, tv.getCardinality());
    assertEquals(1, tv.getFirstNumericValue());

    assertEquals(true, ifd.hasStrips());
    assertEquals(false, ifd.hasTiles());
    ImageStrips ims = ifd.getImageStrips();
    long rowLength = ims.getStrips().get(0).getLength() / ims.getRowsPerStrip();
    int nrows = 0;
    for (int i = 0; i < ims.getStrips().size(); i++) {
      nrows += ims.getStrips().get(i).getLength() / rowLength;
    }
    assertEquals(nrows, ifd.getTag("ImageLength").getFirstNumericValue());
  }

  /**
   * Test2.
   */
  @Test
  public void Test2() {
    // Image 2
    result = tr.readFile("tests\\Organization\\Planar tile.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdCount());
    ifd = (IFD) to.getFirstIFD();

    tv = ifd.getTag("ImageWidth");
    assertEquals(1, tv.getCardinality());
    assertEquals(2000, tv.getFirstNumericValue());

    tv = ifd.getTag("ImageLength");
    assertEquals(1, tv.getCardinality());
    assertEquals(1500, tv.getFirstNumericValue());

    tv = ifd.getTag("BitsPerSample");
    assertEquals(3, tv.getCardinality());
    assertEquals(8, tv.getFirstNumericValue());

    tv = ifd.getTag("SamplesPerPixel");
    assertEquals(1, tv.getCardinality());
    assertEquals(3, tv.getFirstNumericValue());

    tv = ifd.getTag("Compression");
    assertEquals(1, tv.getCardinality());
    assertEquals(1, tv.getFirstNumericValue());

    tv = ifd.getTag("PhotometricInterpretation");
    assertEquals(1, tv.getCardinality());
    assertEquals(2, tv.getFirstNumericValue());

    tv = ifd.getTag("PlanarConfiguration");
    assertEquals(1, tv.getCardinality());
    assertEquals(2, tv.getFirstNumericValue());

    assertEquals(false, ifd.hasStrips());
    assertEquals(true, ifd.hasTiles());
    int tw = ifd.getImageTiles().getTileWidth();
    int th = ifd.getImageTiles().getTileHeight();
    assertEquals(256, tw);
    assertEquals(256, th);
    for (Tile t : ifd.getImageTiles().getTiles()) {
      assertEquals(256, t.getWidth());
      assertEquals(256, t.getHeight());
    }
    long ta = (ifd.getTag("ImageWidth").getFirstNumericValue() + tw - 1) / tw;
    long td = (ifd.getTag("ImageLength").getFirstNumericValue() + th - 1) / tw;
    assertEquals(ifd.getImageTiles().getTiles().size(), ta * td
        * ifd.getTag("SamplesPerPixel").getFirstNumericValue());
  }

  /**
   * Test3.
   */
  @Test
  public void Test3() {
    // Image 3
    result = tr.readFile("src\\test\\resources\\Small\\Bilevel.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdImagesCount());
    ifd = (IFD) to.getFirstIFD();

    assertEquals(0, to.getSubIfdCount());

    assertEquals(true, ifd.hasStrips());
    assertEquals(false, ifd.hasTiles());
    ImageStrips ims = ifd.getImageStrips();
    assertEquals(662, ims.getRowsPerStrip());
    long rowLength = ims.getStrips().get(0).getLength() / ims.getRowsPerStrip();
    int nrows = 0;
    for (int i = 0; i < ims.getStrips().size(); i++) {
      nrows += ims.getStrips().get(i).getLength() / rowLength;
    }
    assertEquals(nrows, ifd.getTag("ImageLength").getFirstNumericValue());
  }

  /**
   * Test4.
   */
  @Test
  public void Test4() {
    // Image 3
    result = tr.readFile("src\\test\\resources\\Small\\Indexed.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(1, to.getIfdImagesCount());
    ifd = (IFD) to.getFirstIFD();

    assertEquals(8, ifd.getTag("BitsPerSample").getFirstNumericValue());
    assertEquals(3 * (int) Math.pow(2, 8), ifd.getTag("ColorMap").getCardinality());

    assertEquals(1, to.getMetadataList("BitsPerSample").size());
    assertEquals("8", to.getMetadataSingleString("BitsPerSample"));
    assertEquals(999, Integer.parseInt(to.getMetadataSingleString("ImageWidth")));
    assertEquals("NIKON D7000", to.getMetadataSingleString("Model"));
  }
}

