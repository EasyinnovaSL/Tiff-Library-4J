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
package com.easyinnova.testing;

import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.ImageStrips;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Tests for the class TiffReader.
 */
public class TiffMetadataTest {

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
    result = tr.readFile("src\\test\\resources\\Small\\Bilevel.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    to = tr.getModel();
    assertEquals(2, to.getIfdCount());
    ifd = (IFD) to.getFirstIFD();

    tv = ifd.getTag("ImageWidth");
    assertEquals(1, tv.getCardinality());
    assertEquals(999, tv.getFirstNumericValue());

    tv = ifd.getTag("ImageLength");
    assertEquals(1, tv.getCardinality());
    assertEquals(662, tv.getFirstNumericValue());

    tv = ifd.getTag("BitsPerSample");
    assertEquals(1, tv.getCardinality());
    assertEquals(1, tv.getFirstNumericValue());

    tv = ifd.getTag("PhotometricInterpretation");
    assertEquals(1, tv.getCardinality());
    assertEquals(0, tv.getFirstNumericValue());

/*
	tv = ifd.getTag("PlanarConfiguration");
	assertEquals(1, tv.getCardinality());
	assertEquals(1, tv.getFirstNumericValue());
*/
    assertEquals(true, ifd.hasStrips());
    assertEquals(false, ifd.hasTiles());
    ImageStrips ims = ifd.getImageStrips();
    long rowLength = ims.getStrips().get(0).getLength() / ims.getRowsPerStrip();
    int nrows = 0;
    for (int i = 0; i < ims.getStrips().size(); i++) {
      nrows += ims.getStrips().get(i).getLength() / rowLength;
    }
    assertEquals(nrows, ifd.getTag("ImageLength").getFirstNumericValue());

    //Metadata metadata=to.getMetadata();


    assertEquals("1", to.getMetadataSingleString("Compression"));
    assertEquals("82750", to.getMetadataSingleString("StripBYTECount"));
    assertEquals("[48,49,48,48]", to.getMetadataSingleString("FlashPixVersion"));
    assertEquals("11/5", to.getMetadataSingleString("42240"));
    assertEquals("xmp.iid:D501C27D082068118C14F1C6AB6A9EEE", to.getMetadataSingleString("InstanceID"));
    assertEquals("3", to.getMetadataSingleString("ExposureProgram"));

    assertEquals("65535", to.getMetadataSingleString("ColorSpace"));
    assertEquals("0", to.getMetadataSingleString("ColorMode"));
    assertEquals("2010-05-24T13:57:15", to.getMetadataSingleString("CreateDate"));
    assertEquals("2015-05-20T23:20:53+02:00", to.getMetadataSingleString("when"));
    assertEquals("1", to.getMetadataSingleString("BitsPerSample"));
    assertEquals("2015:05:20 23:28:35", to.getMetadataSingleString("DateTime"));

    assertEquals("720000/10000", to.getMetadataSingleString("YResolution"));
    assertEquals("80", to.getMetadataSingleString("SubSecTimeOriginal"));
    assertEquals("2", to.getMetadataSingleString("34864"));
    assertEquals("0", to.getMetadataSingleString("ExposureMode"));
    assertEquals("999", to.getMetadataSingleString("ImageWidth"));
    assertEquals("1/320", to.getMetadataSingleString("ExposureTime"));

    assertEquals("saved", to.getMetadataSingleString("action"));
    assertEquals("0", to.getMetadataSingleString("NewSubfileType"));
    assertEquals("80", to.getMetadataSingleString("SubSecTime"));
    assertEquals("5/1", to.getMetadataSingleString("FNumber"));
    assertEquals("0/1", to.getMetadataSingleString("FlashCompensation"));
    assertEquals("999", to.getMetadataSingleString("PixelXDimension"));

    assertEquals("3", to.getMetadataSingleString("FileSource"));
    assertEquals("image/tiff", to.getMetadataSingleString("format"));
    assertEquals("xmp.did:F77F1174072068118C14F1C6AB6A9EEE", to.getMetadataSingleString("DocumentID"));
    assertEquals("123", to.getMetadataSingleString("FocalLengthIn35mmFilm"));
    assertEquals("NIKON CORPORATION", to.getMetadataSingleString("Make"));
    assertEquals("4/1", to.getMetadataSingleString("CompressedBitsPerPixel"));

    assertEquals("1", to.getMetadataSingleString("Orientation"));
    assertEquals("0", to.getMetadataSingleString("Contrast"));
    assertEquals("xmp.iid:F77F1174072068118C14F1C6AB6A9EEE", to.getMetadataSingleString("instanceID"));
    assertEquals("2010:05:24 13:57:15", to.getMetadataSingleString("DateTimeDigitized"));
    assertEquals("720000/10000", to.getMetadataSingleString("XResolution"));

    assertEquals("6073340", to.getMetadataSingleString("SerialNumber"));
    assertEquals("5", to.getMetadataSingleString("MeteringMode"));
    assertEquals("82/1", to.getMetadataSingleString("FocalLength"));
    assertEquals("AF-S DX VR Zoom-Nikkor 18-200mm f/3.5-5.6G IF-ED [II]", to.getMetadataSingleString("Lens"));
    assertEquals("0", to.getMetadataSingleString("GainControl"));
    assertEquals("0", to.getMetadataSingleString("SceneCaptureType"));

    assertEquals("6073340", to.getMetadataSingleString("SerialNumber"));
    assertEquals("5", to.getMetadataSingleString("MeteringMode"));
    assertEquals("82/1", to.getMetadataSingleString("FocalLength"));
    assertEquals("AF-S DX VR Zoom-Nikkor 18-200mm f/3.5-5.6G IF-ED [II]", to.getMetadataSingleString("Lens"));
    assertEquals("0", to.getMetadataSingleString("GainControl"));
    assertEquals("0", to.getMetadataSingleString("SceneCaptureType"));

    assertEquals("[65,83,67,73,73,0,0,0,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32]", to.getMetadataSingleString("UserComment"));
    //assertEquals("[28,1,90,0,3,27,37,71,28,1,90,0,3,27,37,71,28,2,0,0,2,0,0,28,2,80,0,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,28,2,55,0,8,50,48,49,48,48,53,50,52,28,2,60,0,11,49,51,53,55,49,53,43,48,48,48,48,28,2,116,0,54,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32]", to.getMetadataSingleString("IPTC"));
    assertEquals("[48,50,51,48]", to.getMetadataSingleString("36864"));
  }

  /**
   * Test 2.
   */
  @Test
  public void Test2() {
    // Image 1
    result = tr.readFile("src\\test\\resources\\Header\\Incorrect bigconst E.tif");
    assertEquals(0, result);
    assertEquals(false, tr.getValidation().correct);
    to = tr.getModel();


    assertEquals(null, to.getMetadata());
  }

  /**
   * Test 3.
   */
  @Test
  public void Test3() {
    // Image 1
    result = tr.readFile("src\\test\\resources\\Header\\Classic Motorola.tif");
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
    assertEquals(16, tv.getFirstNumericValue());

    tv = ifd.getTag("PhotometricInterpretation");
    assertEquals(1, tv.getCardinality());
    assertEquals(2, tv.getFirstNumericValue());


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

    //Metadata metadata=to.getMetadata();


    assertEquals("1", to.getMetadataSingleString("Compression"));
    assertEquals("[3072000,3072000,3072000,3072000,3072000,2640000]", to.getMetadataSingleString("StripBYTECount"));
    assertEquals("1", to.getMetadataSingleString("PlanarConfiguration"));
    assertEquals("2", to.getMetadataSingleString("PhotometricInterpretation"));
    assertEquals("3", to.getMetadataSingleString("SamplesPerPixel"));
    assertEquals("2000", to.getMetadataSingleString("ImageWidth"));

    assertEquals("[8,3072008,6144008,9216008,12288008,15360008]", to.getMetadataSingleString("StripOffsets"));
    assertEquals("1500", to.getMetadataSingleString("ImageLength"));
    assertEquals("[16,16,16]", to.getMetadataSingleString("BitsPerSample"));

  }

}

