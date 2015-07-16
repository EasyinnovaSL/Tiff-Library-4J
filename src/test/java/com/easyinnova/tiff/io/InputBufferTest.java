/**
 * <h1>InputBufferTest.java</h1>
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
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 17/7/2015
 */
package com.easyinnova.tiff.io;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.reader.TiffReader;
import junit.framework.TestCase;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * JUnit Tests for the class InputBufferTest.
 */
public class InputBufferTest extends TestCase {

  public void testSeek() throws Exception {

    TiffReader tr = new TiffReader();
    int result = tr.readFile("src/test/resources/io/io.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    TiffDocument to = tr.getModel();
/*
    IFD ifd = (IFD) to.getFirstIFD();
    IPTC iptc = (IPTC) (ifd.getTag("IPTC").getValue().get(0));
    Metadata mtIPTC = (iptc).createMetadata();
    iptc.toString();
    assertEquals(33723, ifd.getTag("IPTC").getId());
    assertEquals(true, ifd.getTag("IPTC").getValue().size() > 0);
    */
  }

  public void testRead() throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile("src/test/resources/io/io.tif");
    assertEquals(0, result);
    assertEquals(true, tr.getValidation().correct);
    TiffDocument to = tr.getModel();
/*
    IFD ifd = (IFD) to.getFirstIFD();
    IPTC iptc = (IPTC) ifd.getTag("IPTC").getValue().get(0);
    Metadata mtIPTC = (iptc).createMetadata();
    iptc.toString();
    assertEquals(33723, ifd.getTag("IPTC").getId());
    assertEquals(true, ifd.getTag("IPTC").getValue().size() > 0);
    */
  }
}
