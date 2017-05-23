package com.easyinnova.tiff;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Test;

import java.io.File;
import com.easyinnova.tiff.model.ByteOrder;

/**
 * Created by easy on 05/10/2016.
 */
public class XMPTest {
  /**
   * Valid examples set.
   */
  @Test
  public void XmpRead() {
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
      XMP xmp = (XMP)td.getFirstIFD().getTag("XMP").getValue().get(0);
      Metadata meta = xmp.createMetadata();

      assertEquals(18, meta.keySet().size());
      assertEquals(10, xmp.getHistory().size());
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  /**
   * Valid examples set.
   */
  @Test
  public void XmpWrite() {
    TiffReader tr;

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
      XMP xmp = (XMP)td.getFirstIFD().getTag("XMP").getValue().get(0);
      int nXmp = xmp.createMetadata().keySet().size();
      int nHist = xmp.getHistory().size();

      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(td);
      tw.setByteOrder(ByteOrder.LITTLE_ENDIAN);
      tw.write(filename.replace(".tif", "2.tif"));

      trCopy.readFile(filename.replace(".tif", "2.tif"));
      TiffDocument td2 = trCopy.getModel();
      XMP xmp2 = (XMP)td2.getFirstIFD().getTag("XMP").getValue().get(0);
      int nXmp2 = xmp2.createMetadata().keySet().size();
      int nHist2 = xmp2.getHistory().size();
      assertEquals(nXmp, nXmp2);
      assertEquals(nHist, nHist2);

      assertEquals(tr.getBaselineValidation().errors.size(), 0);
      assertEquals(trCopy.getBaselineValidation().errors.size(), 0);

      new File(filename.replace(".tif", "2.tif")).delete();
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }

  @Test
  public void XmpEdit() {
    TiffReader tr;
    int result;

    try {
      tr = new TiffReader();

      String filename = "src" + separator + "test" + separator + "resources" + separator + "IPTC"
          + separator + "IPTC2.tif";
      result =
          tr.readFile(filename);
      TiffInputStream ti = new TiffInputStream(new File(filename));
      assertEquals(0, result);
      assertEquals(true, tr.getBaselineValidation().isCorrect());

      TiffDocument td = tr.getModel();
      XMP xmp = (XMP)td.getFirstIFD().getTag("XMP").getValue().get(0);

      String copyRight = xmp.getCopyright();
      String creator = xmp.getCreator();
      String description = xmp.getDescription();
      String date = xmp.getDatetime();

      assertEquals("copyleft", copyRight);
      assertEquals("Antonio Lopez", creator);
      assertEquals("This is the description of a sample image", description);
      assertEquals("2015-06-04T08:09:12", date);

      xmp.editCopyright("Copyright Test");
      xmp.editCreator("Creator Test");
      xmp.editDescription("Description Test");
      xmp.editDatetime("Date Test");

      TiffWriter tw = new TiffWriter(ti);
      tw.SetModel(td);
      tw.setByteOrder(ByteOrder.LITTLE_ENDIAN);
      tw.write(filename.replace(".tif", "2.tif"));

      TiffReader trCopy = new TiffReader();
      trCopy.readFile(filename.replace(".tif", "2.tif"));
      TiffDocument td2 = trCopy.getModel();
      XMP xmp2 = (XMP)td2.getFirstIFD().getTag("XMP").getValue().get(0);

      String copyRight2 = xmp2.getCopyright();
      String creator2 = xmp2.getCreator();
      String description2 = xmp2.getDescription();
      String date2 = xmp2.getDatetime();

      assertEquals("Copyright Test", copyRight2);
      assertEquals("Creator Test", creator2);
      assertEquals("Description Test", description2);
      assertEquals("Date Test", date2);

      new File(filename.replace(".tif", "2.tif")).delete();

    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}
