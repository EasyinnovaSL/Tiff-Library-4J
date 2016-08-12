/**
 * <h1>InputBufferTest.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 22/7/2015
 */
package com.easyinnova.tiff.io;

import static java.io.File.separator;

import java.io.File;

import junit.framework.TestCase;

/**
 * The type Random access file input stream test.
 */
public class MappedByteInputStreamTest extends TestCase {

  /**
   * Test read.
   *
   * @throws Exception the exception
   */
  public void testRead() throws Exception {
    MappedByteInputStream ascii = new MappedByteInputStream(new File("src" + separator + "test" + separator + "resources" + separator + "io" + separator + "asciiTest.hex"));
    assertEquals(65, ascii.read());
    ascii.close();
  }

  /**
   * Test read 1.
   *
   * @throws Exception the exception
   */
  public void testRead1() throws Exception {
    MappedByteInputStream ascii = new MappedByteInputStream(new File("src" + separator + "test" + separator + "resources" + separator + "io" + separator + "asciiTest.hex"));
    byte[] data = {(byte) 2, (byte) 1, (byte) 0};
    assertEquals(3, ascii.read(data));
    ascii.close();
  }

  /**
   * Test read 2.
   *
   * @throws Exception the exception
   */
  public void testRead2() throws Exception {
    MappedByteInputStream ascii = new MappedByteInputStream(new File("src" + separator + "test" + separator + "resources" + separator + "io" + separator + "asciiTest.hex"));
    byte[] data = {(byte) 2, (byte) 1, (byte) 0};

    assertEquals(1, ascii.read(data, 0, 1));
    assertEquals(2, ascii.read(data, 1, 2));
    ascii.close();
  }
}
