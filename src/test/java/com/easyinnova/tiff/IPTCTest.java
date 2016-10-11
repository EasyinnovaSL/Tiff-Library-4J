package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Test;

import java.io.File;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by easy on 10/10/2016.
 */
public class IPTCTest {
  /**
   * Valid examples set.
   */
  @Test
  public void IPTCRead() {
    TiffReader tr;
    int result;

    try {
      tr = new TiffReader();

      result =
          tr.readFile("src" + separator + "test" + separator + "resources" + separator + "Small"
              + separator + "RGB.tif");
      assertEquals(0, result);
      assertEquals(true, tr.getBaselineValidation().isCorrect());

      TiffDocument td = tr.getModel();
      IPTC iptc = (IPTC)td.getFirstIFD().getTag("IPTC").getValue().get(0);
      Metadata meta = iptc.createMetadata();

      assertEquals(6, meta.keySet().size());
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  /**
   * Valid examples set.
   */
  @Test
  public void IPTCWrite() {
    TiffReader tr;
    int result;

    try {
      tr = new TiffReader();
      TiffReader trCopy = new TiffReader();

      // Read the File to copy
      String filename =
          "src" + separator + "test" + separator + "resources" + separator + "Small" + separator
              + "RGB.tif";
      tr.readFile(filename);
      TiffInputStream ti = new TiffInputStream(new File(filename));
      TiffDocument td = tr.getModel();
      IPTC iptc = (IPTC)td.getFirstIFD().getTag("IPTC").getValue().get(0);
      int nIptc = iptc.createMetadata().keySet().size();

      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(td);
      tw.setByteOrder(ByteOrder.LITTLE_ENDIAN);
      tw.write(filename.replace(".tif", "2.tif"));

      trCopy.readFile(filename.replace(".tif", "2.tif"));
      TiffDocument td2 = trCopy.getModel();
      IPTC iptc2 = (IPTC)td2.getFirstIFD().getTag("IPTC").getValue().get(0);
      int nIptc2 = iptc2.createMetadata().keySet().size();

      assertEquals(nIptc, nIptc2);

      assertEquals(tr.getBaselineValidation().errors.size(), 0);
      assertEquals(trCopy.getBaselineValidation().errors.size(), 0);
      trCopy.getStream().close();

      new File(filename.replace(".tif", "2.tif")).delete();
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  @Test
  public void editTest() {
    TiffReader tr;

    try {
      tr = new TiffReader();
      TiffReader trCopy = new TiffReader();

      // Read the File to copy
      String filename =
          "src" + separator + "test" + separator + "resources" + separator + "IPTC" + separator
              + "IPTC2.tif";
      tr.readFile(filename);
      TiffInputStream ti = new TiffInputStream(new File(filename));
      TiffDocument td = tr.getModel();
      IPTC iptc = (IPTC)td.getFirstIFD().getTag("IPTC").getValue().get(0);
      int nIptc = iptc.createMetadata().keySet().size();

      String oCopyright = iptc.getCopyright();
      String oCreator = iptc.getCreator();
      String oDescription = iptc.getDescription();
      Date oDatetime = iptc.getDatetime();

      iptc.editCopyright("Test Copyright");
      iptc.editCreator("Test Creator");
      iptc.editDescription("Test Description");

      SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
      String dateInString = "31-08-1982 10:20:56";
      Date date = sdf.parse(dateInString);
      iptc.editDatetime(date);

      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(td);
      tw.setByteOrder(ByteOrder.LITTLE_ENDIAN);
      tw.write(filename.replace(".tif", "2.tif"));

      trCopy.readFile(filename.replace(".tif", "2.tif"));
      TiffDocument td2 = trCopy.getModel();
      IPTC iptc2 = (IPTC)td2.getFirstIFD().getTag("IPTC").getValue().get(0);
      int nIptc2 = iptc2.createMetadata().keySet().size();

      assertEquals(nIptc, nIptc2);

      assertEquals("Antonio Lopez", oCreator);
      assertEquals("copyleft", oCopyright);
      assertEquals("This is the description of a sample image", oDescription);
      System.out.println(oDatetime.toString());
      assertEquals("Thu Jun 04 00:00:00 CEST 2015", oDatetime.toString());

      assertEquals("Test Creator", iptc2.getCreator());
      assertEquals("Test Copyright", iptc2.getCopyright());
      assertEquals("Test Description", iptc2.getDescription());
      assertEquals(date.toString(), iptc2.getDatetime().toString());

      new File(filename.replace(".tif", "2.tif")).delete();

    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}
