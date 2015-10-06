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
package com.easyinnova.tiff.io;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Test;

import java.io.File;

/**
 * Testing class.
 */
public class TiffWriterTest {

  /**
   * Creator test
   */
  @Test
  public void Creator() {
    TiffReader tr;
    TiffReader trCopy;
    TiffWriter tw;
    TiffDocument td;
    TiffDocument tdCopy;
    IFD ifd;
    IFD ifdCopy;

    try {
      tr = new TiffReader();
      trCopy = new TiffReader();

      //Read the File to copy
      tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel.tif");
      TiffInputStream ti = new TiffInputStream(new File("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Bilevel.tif"));
      td=tr.getModel();

      tw = new TiffWriter(ti);
      tw.SetModel(td);
      tw.write("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Bilevel2.tif");


      trCopy.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
          + separator + "Bilevel2.tif");
      tdCopy=trCopy.getModel();

      ifd=td.getFirstIFD();
      ifdCopy=tdCopy.getFirstIFD();


      assertEquals(td.getIfdAndSubIfdCount(),tdCopy.getIfdAndSubIfdCount());
      assertEquals(td.getIfdCount(), tdCopy.getIfdCount());
      assertEquals(td.getIfdImagesCount(),tdCopy.getIfdImagesCount());
      assertEquals(td.getMagicNumber(),tdCopy.getMagicNumber());

      assertEquals(ifd.getTag("ImageWidth").getCardinality(), ifdCopy.getTag("ImageWidth").getCardinality());
      assertEquals(ifd.getTag("ImageWidth").getFirstNumericValue(),ifdCopy.getTag("ImageWidth").getFirstNumericValue());

      assertEquals(ifd.getTag("ImageLength").getCardinality(), ifdCopy.getTag("ImageLength").getCardinality());
      assertEquals(ifd.getTag("ImageLength").getFirstNumericValue(),ifdCopy.getTag("ImageLength").getFirstNumericValue());

      assertEquals(ifd.getTag("BitsPerSample").getCardinality(), ifdCopy.getTag("BitsPerSample").getCardinality());
      assertEquals(ifd.getTag("BitsPerSample").getFirstNumericValue(),ifdCopy.getTag("BitsPerSample").getFirstNumericValue());

      assertEquals(ifd.getTag("PhotometricInterpretation").getCardinality(), ifdCopy.getTag("PhotometricInterpretation").getCardinality());
      assertEquals(ifd.getTag("PhotometricInterpretation").getFirstNumericValue(),ifdCopy.getTag("PhotometricInterpretation").getFirstNumericValue());

      assertEquals(ifdCopy.hasStrips(), ifd.hasStrips());
      assertEquals(ifdCopy.hasTiles(), ifd.hasTiles());


      assertEquals(td.getMetadataSingleString("Compression"),tdCopy.getMetadataSingleString("Compression"));
      assertEquals(td.getMetadataSingleString("StripBYTECount"),tdCopy.getMetadataSingleString("StripBYTECount"));
      assertEquals(td.getMetadataSingleString("FlashPixVersion"),tdCopy.getMetadataSingleString("FlashPixVersion"));
      assertEquals(td.getMetadataSingleString("42240"),tdCopy.getMetadataSingleString("42240"));
      assertEquals(td.getMetadataSingleString("InstanceID"),tdCopy.getMetadataSingleString("InstanceID"));
      assertEquals(td.getMetadataSingleString("ExposureProgram"),tdCopy.getMetadataSingleString("ExposureProgram"));

      assertEquals(td.getMetadataSingleString("ColorSpace"),tdCopy.getMetadataSingleString("ColorSpace"));
      assertEquals(td.getMetadataSingleString("ColorMode"),tdCopy.getMetadataSingleString("ColorMode"));
      assertEquals(td.getMetadataSingleString("CreateDate"),tdCopy.getMetadataSingleString("CreateDate"));
      assertEquals(td.getMetadataSingleString("when"),tdCopy.getMetadataSingleString("when"));
      assertEquals(td.getMetadataSingleString("BitsPerSample"),tdCopy.getMetadataSingleString("BitsPerSample"));
      assertEquals(td.getMetadataSingleString("DateTime"),tdCopy.getMetadataSingleString("DateTime"));

      assertEquals(td.getMetadataSingleString("YResolution"),tdCopy.getMetadataSingleString("YResolution"));
      assertEquals(td.getMetadataSingleString("SubSecTimeOriginal"),tdCopy.getMetadataSingleString("SubSecTimeOriginal"));
      assertEquals(td.getMetadataSingleString("34864"),tdCopy.getMetadataSingleString("34864"));
      assertEquals(td.getMetadataSingleString("ExposureMode"),tdCopy.getMetadataSingleString("ExposureMode"));
      assertEquals(td.getMetadataSingleString("ImageWidth"),tdCopy.getMetadataSingleString("ImageWidth"));
      assertEquals(td.getMetadataSingleString("ExposureTime"),tdCopy.getMetadataSingleString("ExposureTime"));

      assertEquals(td.getMetadataSingleString("action"),tdCopy.getMetadataSingleString("action"));
      assertEquals(td.getMetadataSingleString("NewSubfileType"),tdCopy.getMetadataSingleString("NewSubfileType"));
      assertEquals(td.getMetadataSingleString("SubSecTime"),tdCopy.getMetadataSingleString("SubSecTime"));
      assertEquals(td.getMetadataSingleString("FNumber"),tdCopy.getMetadataSingleString("FNumber"));
      assertEquals(td.getMetadataSingleString("FlashCompensation"),tdCopy.getMetadataSingleString("FlashCompensation"));
      assertEquals(td.getMetadataSingleString("PixelXDimension"),tdCopy.getMetadataSingleString("PixelXDimension"));

      assertEquals(td.getMetadataSingleString("FileSource"),tdCopy.getMetadataSingleString("FileSource"));
      assertEquals(td.getMetadataSingleString("format"),tdCopy.getMetadataSingleString("format"));
      assertEquals(td.getMetadataSingleString("DocumentID"),tdCopy.getMetadataSingleString("DocumentID"));
      assertEquals(td.getMetadataSingleString("FocalLengthIn35mmFilm"),tdCopy.getMetadataSingleString("FocalLengthIn35mmFilm"));
      assertEquals(td.getMetadataSingleString("Make"),tdCopy.getMetadataSingleString("Make"));
      assertEquals(td.getMetadataSingleString("CompressedBitsPerPixel"),tdCopy.getMetadataSingleString("CompressedBitsPerPixel"));

      assertEquals(td.getMetadataSingleString("Orientation"),tdCopy.getMetadataSingleString("Orientation"));
      assertEquals(td.getMetadataSingleString("Contrast"),tdCopy.getMetadataSingleString("Contrast"));
      assertEquals(td.getMetadataSingleString("instanceID"),tdCopy.getMetadataSingleString("instanceID"));
      assertEquals(td.getMetadataSingleString("DateTimeDigitized"),tdCopy.getMetadataSingleString("DateTimeDigitized"));
      assertEquals(td.getMetadataSingleString("XResolution"),tdCopy.getMetadataSingleString("XResolution"));

      assertEquals(td.getMetadataSingleString("SerialNumber"),tdCopy.getMetadataSingleString("SerialNumber"));
      assertEquals(td.getMetadataSingleString("MeteringMode"),tdCopy.getMetadataSingleString("MeteringMode"));
      assertEquals(td.getMetadataSingleString("FocalLength"),tdCopy.getMetadataSingleString("FocalLength"));
      assertEquals(td.getMetadataSingleString("Lens"),tdCopy.getMetadataSingleString("Lens"));
      assertEquals(td.getMetadataSingleString("GainControl"),tdCopy.getMetadataSingleString("GainControl"));
      assertEquals(td.getMetadataSingleString("SceneCaptureType"),tdCopy.getMetadataSingleString("SceneCaptureType"));

      assertEquals(td.getMetadataSingleString("UserComment"),tdCopy.getMetadataSingleString("UserComment"));


    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}
