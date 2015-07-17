package com.easyinnova.testing;

import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.io.TiffInputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * The Class TiffFileInputStreamTest.
 */
public class TiffFileInputStreamTest {

  /**
   * Sets the up before class.
   *
   * @throws Exception the exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  /**
   * Tear down after class.
   *
   * @throws Exception the exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  /**
   * Sets the up.
   *
   * @throws Exception the exception
   */
  @Before
  public void setUp() throws Exception {

  }

  /**
   * Tear down.
   *
   * @throws Exception the exception
   */
  @After
  public void tearDown() throws Exception {

  }

  /**
   * Test.
   * 
   * @throws IOException zxc
   */
  @Test
  public void ReadByte() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/ByteTest.hex"));

    assertEquals("127", stream.readByte().toString());
    assertEquals("128", stream.readByte().toString());
    assertEquals("0", stream.readByte().toString());
    assertEquals("255", stream.readByte().toString());
    stream.close();
  }

  /**
   * Read ascii.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void ReadAscii() throws IOException {
    TiffInputStream asci = new TiffInputStream(new File("src/test/resources/io/asciiTest.hex"));
    assertEquals("A", asci.readAscii().toString());
    assertEquals("s", asci.readAscii().toString());
    assertEquals("c", asci.readAscii().toString());
    assertEquals("i", asci.readAscii().toString());
    assertEquals("i", asci.readAscii().toString());
    assertEquals("T", asci.readAscii().toString());
    assertEquals("e", asci.readAscii().toString());
    assertEquals("s", asci.readAscii().toString());
    assertEquals("t", asci.readAscii().toString());
    assertEquals("F", asci.readAscii().toString());
    assertEquals("i", asci.readAscii().toString());
    assertEquals("l", asci.readAscii().toString());
    assertEquals("e", asci.readAscii().toString());
    asci.close();
  }

  /**
   * Read s byte.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readSByte() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/ByteTest.hex"));
    assertEquals("127", stream.readSByte().toString());
    assertEquals("-128", stream.readSByte().toString());
    assertEquals("0", stream.readSByte().toString());
    assertEquals("-1", stream.readSByte().toString());
    stream.close();
  }

  /**
   * Read short.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readShort() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/ShortTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("32767", stream.readShort().toString());
    assertEquals("32768", stream.readShort().toString());
    assertEquals("0", stream.readShort().toString());
    assertEquals("65535", stream.readShort().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("65407", stream.readShort().toString());
    assertEquals("128", stream.readShort().toString());
    assertEquals("0", stream.readShort().toString());
    assertEquals("65535", stream.readShort().toString());
    stream.close();
  }

  /**
   * Read sshort.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readSShort() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/ShortTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("32767", stream.readSShort().toString());
    assertEquals("-32768", stream.readSShort().toString());
    assertEquals("0", stream.readSShort().toString());
    assertEquals("-1", stream.readSShort().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("-129", stream.readSShort().toString());
    assertEquals("128", stream.readSShort().toString());
    assertEquals("0", stream.readSShort().toString());
    assertEquals("-1", stream.readSShort().toString());
    stream.close();
  }

  /**
   * Read long.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readLong() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/LongTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("2147483647", stream.readLong().toString());
    assertEquals("2147483648", stream.readLong().toString());
    assertEquals("0", stream.readLong().toString());
    assertEquals("4294967295", stream.readLong().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("4294967167", stream.readLong().toString());
    assertEquals("128", stream.readLong().toString());
    assertEquals("0", stream.readLong().toString());
    assertEquals("4294967295", stream.readLong().toString());
    stream.close();
  }

  /**
   * Read ss long.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readSSLong() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/LongTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("2147483647", stream.readSLong().toString());
    assertEquals("-2147483648", stream.readSLong().toString());
    assertEquals("0", stream.readSLong().toString());
    assertEquals("-1", stream.readSLong().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("-129", stream.readSLong().toString());
    assertEquals("128", stream.readSLong().toString());
    assertEquals("0", stream.readSLong().toString());
    assertEquals("-1", stream.readSLong().toString());
    stream.close();
  }

  /**
   * Read ss long.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readFloat() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/FloatTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("0.0", stream.readFloat().toString());
    assertEquals("-0.0", stream.readFloat().toString());
    assertEquals("Infinity", stream.readFloat().toString());
    assertEquals("-Infinity", stream.readFloat().toString());
    assertEquals("3.4028235E38", stream.readFloat().toString());
    assertEquals("-3.4028235E38", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("-1.1754942E-38", stream.readFloat().toString());
    assertEquals("1.1754942E-38", stream.readFloat().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("0.0", stream.readFloat().toString());
    assertEquals("1.794E-43", stream.readFloat().toString());
    assertEquals("4.6096E-41", stream.readFloat().toString());
    assertEquals("4.6275E-41", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());
    assertEquals("NaN", stream.readFloat().toString());

    stream.close();
  }

  /**
   * Read double.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readDouble() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/DoubleTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("0.0", stream.readDouble().toString());
    assertEquals("-0.0", stream.readDouble().toString());
    assertEquals("Infinity", stream.readDouble().toString());
    assertEquals("-Infinity", stream.readDouble().toString());
    assertEquals("1.7976931348623157E308", stream.readDouble().toString());
    assertEquals("-1.7976931348623157E308", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("-2.225073858507201E-308", stream.readDouble().toString());
    assertEquals("2.225073858507201E-308", stream.readDouble().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("0.0", stream.readDouble().toString());
    assertEquals("6.32E-322", stream.readDouble().toString());
    assertEquals("3.0418E-319", stream.readDouble().toString());
    assertEquals("3.04814E-319", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    stream.close();
  }

  /**
   * Read racional.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void readRacional() throws IOException {
    TiffInputStream stream = new TiffInputStream(new File("src/test/resources/io/DoubleTest.hex"));
    stream.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals("0.0", stream.readDouble().toString());
    assertEquals("-0.0", stream.readDouble().toString());
    assertEquals("Infinity", stream.readDouble().toString());
    assertEquals("-Infinity", stream.readDouble().toString());
    assertEquals("1.7976931348623157E308", stream.readDouble().toString());
    assertEquals("-1.7976931348623157E308", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("-2.225073858507201E-308", stream.readDouble().toString());
    assertEquals("2.225073858507201E-308", stream.readDouble().toString());
    stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    stream.seekOffset(0);
    assertEquals("0.0", stream.readDouble().toString());
    assertEquals("6.32E-322", stream.readDouble().toString());
    assertEquals("3.0418E-319", stream.readDouble().toString());
    assertEquals("3.04814E-319", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    assertEquals("NaN", stream.readDouble().toString());
    stream.close();
  }
}
