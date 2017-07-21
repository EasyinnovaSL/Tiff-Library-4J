/**
 * <h1>IPTC.java</h1>
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
 * @since 6/7/2015
 */
package com.easyinnova.tiff.model.types;

import com.easyinnova.tiff.io.TiffOutputStream;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.writer.TiffWriter;
import com.nmote.iim4j.IIMDataSetInfoFactory;
import com.nmote.iim4j.IIMFile;
import com.nmote.iim4j.IIMReader;
import com.nmote.iim4j.IIMWriter;
import com.nmote.iim4j.dataset.DataSet;
import com.nmote.iim4j.dataset.DataSetInfo;
import com.nmote.iim4j.dataset.DefaultDataSet;
import com.nmote.iim4j.dataset.InvalidDataSetException;
import com.nmote.iim4j.serialize.SerializationException;
import com.nmote.iim4j.serialize.Serializer;
import com.nmote.iim4j.stream.DefaultIIMOutputStream;
import com.nmote.iim4j.stream.FileIIMInputStream;
import com.nmote.iim4j.stream.IIMOutputStream;
import com.nmote.iim4j.stream.JPEGIIMInputStream;
import com.nmote.iim4j.stream.SubIIMInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class IPTC.
 */
public class IPTC extends abstractTiffType {

  /**
   * The Constant SEGMENT_MARKER.
   */
  public static final int[] SEGMENT_MARKER = {28, 2};

  /** The original value. */
  private List<abstractTiffType> originalValue;

  private transient IIMFile iimFile;

  /**
   * Instantiates a new IPTC.
   */
  public IPTC() {
  }

  /**
   * Sets the original.
   *
   */
  public void setOriginal(List<abstractTiffType> value) {
    originalValue = value;
  }

  /**
   * Gets the original.
   *
   * @return the original
   */
  public List<abstractTiffType> getOriginal() {
    return originalValue;
  }

  /**
   * Creates the metadata.
   *
   * @return the hash map
   */
  @Override
  public Metadata createMetadata() {
    Metadata metadata = new Metadata();
    try {
      for (DataSet ds : iimFile.getDataSets()) {
        Object value = "";
        try {
          value = ds.getValue();
        } catch (Exception ex) {

        }
        DataSetInfo info = ds.getInfo();
        //System.out.println(info.toString() + " " + info.getName() + ": " + value);

        metadata.add(info.getName(), new Text(value.toString()));
      }
    } catch (Exception ex) {
      /*Nothing to be shown*/
    }

    return metadata;
  }

  @Override
  public String toString() {
    String s = "";
    for (DataSet ds : iimFile.getDataSets()) {
      Object value = "";
      try {
        value = ds.getValue();
      } catch (Exception ex) {

      }
      DataSetInfo info = ds.getInfo();
      s += info.toString() + " " + info.getName() + ": " + value + "\n";
    }
    return s;
  }

  /**
   * Removes the tag.
   *
   * @param tagName the tag name
   */
  public void removeTag(String tagName) {
    int ids = -1;
    for (DataSet ds : iimFile.getDataSets()) {
      if (ds.getInfo().getName().equals(tagName))
        ids = ds.getInfo().getDataSetNumber();
    }
    if (ids != -1)
      iimFile.remove(ids);
  }

  public void editCopyright(String value) {
    editTag("Copyright Notice", value);
  }

  public void editCreator(String value) {
    editTag("Writer/Editor", value);
  }

  public void editDescription(String value) {
    editTag("Caption/Abstract", value);
  }

  String zeros(int value, int digits) {
    String s = value+"";
    while (s.length() < digits)
      s = "0" + s;
    return s;
  }

  public void editDatetime(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    int second = cal.get(Calendar.SECOND);

    editTag("Date Created", zeros(year, 4) + "" + zeros(month, 2) + zeros(day, 2));
    editTag("Time Created", zeros(hour, 2) + "" + zeros(minute, 2) + zeros(second, 2) + "+000000");
  }

  public String getCopyright() {
    return getTag("Copyright Notice");
  }

  public String getCreator() {
    return getTag("Writer/Editor");
  }

  public String getDescription() {
    return getTag("Caption/Abstract");
  }

  public Date getDatetime() {
    String dateField = getTag("Date Created");
    String timeField = getTag("Time Created");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss+SSSSSS");
    Date date = null;
    try {
      date = sdf.parse(dateField + " " + timeField);
    } catch (ParseException e) {
      //e.printStackTrace();
      return null;
    }
    return date;
  }

  public String getTag(String name) {
    for (DataSet ds : iimFile.getDataSets()) {
      if (ds.getInfo().getName().equals(name)) {
        try {
          return ds.getValue().toString();
        } catch (SerializationException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public void editTag(String name, String value) {
    int ids = -1;
    DataSet dataSet = null;
    for (DataSet ds : iimFile.getDataSets()) {
      if (ds.getInfo().getName().equals(name)) {
        ids = ds.getInfo().getDataSetNumber();
        dataSet = ds;
      }
    }
    if (ids != -1) {
      iimFile.remove(ids);
      DefaultDataSet ds = new DefaultDataSet(dataSet.getInfo(), value.getBytes());
      iimFile.add(ds);
    }
  }

  /**
   * Reads the IPTC.
   *
   * @param tv the TagValue containing the array of bytes of the IPTC
   */
  public void read(TagValue tv, String filename) {
    originalValue = tv.getValue();
    File file = new File(filename);
    IIMReader reader = null;
    SubIIMInputStream subStream = null;
    try {
      int offset = tv.getReadOffset();
      int length = tv.getReadlength();
      subStream = new SubIIMInputStream(new FileIIMInputStream(file), offset, length);

      reader = new IIMReader(subStream, new IIMDataSetInfoFactory());

      IIMFile iimFileReader = new IIMFile();
      iimFileReader.readFrom(reader, 0);
      List<DataSet> lds = new ArrayList<DataSet>();
      for (DataSet ds : iimFileReader.getDataSets()) {
        ds.getData();
        lds.add(ds);
      }

      iimFile = new IIMFile();
      iimFile.setDataSets(lds);

      tv.reset();
      tv.add(this);
      reader.close();

      subStream.close();
    } catch (IOException e) {
      //e.printStackTrace();
      try {
        reader.close();
        subStream.close();
      } catch (Exception ex) {

      }
    } catch (InvalidDataSetException e) {
      //e.printStackTrace();
      try {
        reader.close();
        subStream.close();
      } catch (Exception ex) {

      }
    }
  }

  public void write(TiffOutputStream data) throws IOException {
    try {
      ByteArrayOutputStream sw = new ByteArrayOutputStream();
      IIMWriter writer = new IIMWriter(new DefaultIIMOutputStream(sw));
      iimFile.writeTo(writer);
      writer.close();

      byte[] bytes = sw.toByteArray();
      for (byte b : bytes) {
        data.put(b);
      }
      data.put((byte) 0);

      return;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public int getLength() {
    try {
      ByteArrayOutputStream sw = new ByteArrayOutputStream();
      IIMWriter writer = new IIMWriter(new DefaultIIMOutputStream(sw));
      iimFile.writeTo(writer);
      writer.close();
      return sw.size();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //return iimFile.toString().length();
    return getOriginal().size();
  }

  @Override
  public boolean containsMetadata() {
    return true;
  }

}

