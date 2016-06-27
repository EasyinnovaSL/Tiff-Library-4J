package com.easyinnova.tiff.io;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IccProfile;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Test;

import java.io.File;

/**
 * Created by easy on 10/06/2016.
 */
public class ICCProfileTest {
  @Test
  public void Creator() {
    TiffReader tr;
    TiffDocument td;

    try {
      tr = new TiffReader();
      String filename =
          "src" + separator + "test" + separator + "resources" + separator + "Small" + separator
              + "RGB.tif";
      tr.readFile(filename);
      td = tr.getModel();
      IccProfile icc = td.getIccProfile();
      assertEquals("sRGB IEC61966-2.1", icc.getDescription());
      assertEquals(null, icc.getCreator());
      assertEquals("2.1", icc.getVersion());
      assertEquals(false, icc.getEmbedded());
      assertEquals(IccProfile.ProfileClass.Display, icc.getProfileClass());
    } catch (Exception e) {
      assertEquals(0, 1);
    }
  }
}
