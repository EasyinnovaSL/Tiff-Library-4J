/**
 * <h1>FixTags.java</h1> 
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
 * @since 9/10/2015
 *
 */
package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * The Class FixTags.
 */
public class FixTags {
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
   * Adds the tag test.
   */
  @Test
  public void addTagTest() {
    try {
      // Read Tiff
      result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "Bilevel.tif");
      TiffInputStream ti =
          new TiffInputStream(new File("src" + separator + "test" + separator + "resources"
              + separator + "Small" + separator + "Bilevel.tif"));
      to = tr.getModel();
      ifd = (IFD) to.getFirstIFD();

      // Add tag
      ifd.addTag("ImageDescription", "desc");

      // Write modified Tiff
      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(tr.getModel());
      tw.write("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel2.tif");


      // Read modified Tiff
      TiffReader trCopy = new TiffReader();
      trCopy.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel2.tif");
      TiffDocument tdCopy;
      tdCopy = trCopy.getModel();

      // Compare modified Tiff with original
      IFD ifdCopy;
      ifdCopy = tdCopy.getFirstIFD();

      // Re read original Tiff
      result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "Bilevel.tif");
      to = tr.getModel();
      ifd = (IFD) to.getFirstIFD();

      assertEquals(ifd.getTags().getTags().size() + 1, ifdCopy.getTags().getTags().size());
      assertEquals("desc", ifdCopy.getTag("ImageDescription").toString());
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  /**
   * Remove tag test.
   */
  @Test
  public void removeTagTest() {
    try {
      // Read Tiff
      result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "Bilevel.tif");
      TiffInputStream ti =
          new TiffInputStream(new File("src" + separator + "test" + separator + "resources"
              + separator + "Small" + separator + "Bilevel.tif"));
      to = tr.getModel();
      ifd = (IFD) to.getFirstIFD();

      // Remove tag
      ifd.removeTag("Copyright");

      // Write modified Tiff
      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(tr.getModel());
      tw.write("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel2.tif");


      // Read modified Tiff
      TiffReader trCopy = new TiffReader();
      trCopy.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel2.tif");
      TiffDocument tdCopy;
      tdCopy = trCopy.getModel();

      // Compare modified Tiff with original
      IFD ifdCopy;
      ifdCopy = tdCopy.getFirstIFD();

      // Re read original Tiff
      result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "Bilevel.tif");
      to = tr.getModel();
      ifd = (IFD) to.getFirstIFD();

      assertEquals(ifd.getTags().getTags().size() - 1, ifdCopy.getTags().getTags().size());
      assertEquals(true, ifd.getTag("Copyright") != null);
      assertEquals(false, ifdCopy.getTag("Copyright") != null);
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}

