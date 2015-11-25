/**
 * <h1>TiffTagsTest.java</h1> 
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
 * @since 24/11/2015
 *
 */
package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.Tag;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Test;

/**
 * The Class TiffTagsTest.
 */
public class TiffTagsTest {

  /**
   * Test.
   */
  @Test
  public void test() {
    Tag tag = TiffTags.getTag(259);
    assertEquals(tag.getTagValueDescription("5"), "LZW");
    assertEquals(tag.getTagValueDescription("unexisting"), null);

    Tag tag2 = TiffTags.getTag(TiffTags.getTagId("PhotometricInterpretation"));
    assertEquals(tag2.getTagValueDescription("2"), "RGB");
    assertEquals(tag2.getTagValueDescription("6"), "YCbCr");
    assertEquals(tag2.getTagValueDescription("unexisting"), null);

    tag2 = TiffTags.getTag(TiffTags.getTagId("Orientation"));
    assertEquals(tag2.getTagValueDescription("1"), "TopLeft");
    assertEquals(tag2.getTagValueDescription("unexisting"), null);

    tag2 = TiffTags.getTag(TiffTags.getTagId("PlanarConfiguration"));
    assertEquals(tag2.getTagValueDescription("1"), "Chunky");
    assertEquals(tag2.getTagValueDescription("2"), "Planar");
    assertEquals(tag2.getTagValueDescription("unexisting"), null);

    TiffReader tr;
    try {
      tr = new TiffReader();
      int result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "Bilevel.tif");
      assertEquals(0, result);
      assertEquals(true, tr.getBaselineValidation().isCorrect());

      String value =
          tr.getModel().getFirstIFD().getTag("PhotometricInterpretation").getDescriptiveValue();
      assertEquals(value, "Bilevel");
    } catch (ReadTagsIOException e) {
      assertEquals(0, 1);
    } catch (ReadIccConfigIOException e) {
      assertEquals(0, 1);
    }
  }
}

