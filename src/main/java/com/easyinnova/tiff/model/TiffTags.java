/**
 * <h1>TiffTags.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 18/5/2015
 */

package com.easyinnova.tiff.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Class TiffTags.
 */
public class TiffTags {

  /**
   * The tag map.
   */
  public static HashMap<Integer, Tag> tagMap = new HashMap<Integer, Tag>();
  /**
   * The tag types.
   */
  public static HashMap<Integer, String> tagTypes = new HashMap<Integer, String>();
  /**
   * The tag names.
   */
  protected static HashMap<String, Tag> tagNames = new HashMap<String, Tag>();
  /**
   * The singleton instance.
   */
  private static TiffTags instance = null;

  /**
   * Read tag from buffer.
   *
   * @param br the br
   * @return the tag
   */
  private static Tag readTagFromBuffer(BufferedReader br, boolean close) {
    int id = 0;
    String name = "", forceDescription = null;
    ArrayList<String> types = new ArrayList<>();
    String cardinality = "";
    String defaultValue = "";
    String typedef = null;
    String description = null;
    String[] valueCodes = null;
    String[] valueDescriptions = null;

    try {
      String sCurrentLine;
      boolean readingTypes = false;
      while ((sCurrentLine = br.readLine()) != null) {
        if (sCurrentLine.contains("\"id\"")) {
          String sid = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace(",", "").trim();
          id = Integer.parseInt(sid);
        } else if (sCurrentLine.contains("\"name\"")) {
          String sval = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
          name = sval;
        } else if (sCurrentLine.contains("\"forceDescription\"")) {
          String sval = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
          forceDescription = sval;
        } else if (sCurrentLine.contains("\"cardinality\"")) {
          String sval = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
          cardinality = sval;
        } else if (sCurrentLine.contains("\"defaultValue\"")) {
          String sval = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
          defaultValue = sval;
        } else if (sCurrentLine.contains("\"valueCodes\"")) {
          sCurrentLine = br.readLine();
          valueCodes = sCurrentLine.split(",");
        } else if (sCurrentLine.contains("\"description\"")) {
          sCurrentLine = br.readLine();
          description = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
        } else if (sCurrentLine.contains("\"valueDescriptions\"")) {
          sCurrentLine = br.readLine();
          valueDescriptions = sCurrentLine.split(",");
        } else if (sCurrentLine.contains("\"typedef\"")) {
          String sval = sCurrentLine.substring(sCurrentLine.indexOf(":") + 1).replace("\"", "").replace(",", "").trim();
          typedef = sval;
        } else if (sCurrentLine.contains("\"type\"")) {
          readingTypes = true;
        } else if (sCurrentLine.contains("],")) {
          readingTypes = false;
        } else if (readingTypes) {
          String sval = sCurrentLine.replace("\"", "").replace(",", "").trim();
          types.add(sval);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        //br.reset();
        if (br != null && close) br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    Tag tag = new Tag(id, name, types, cardinality, defaultValue, typedef, forceDescription);
    tag.setDescription(description);
    tag.createValuesDictionary();
    if (valueCodes != null && valueDescriptions != null && valueCodes.length == valueDescriptions.length) {
      HashMap<String, String> values = new HashMap<String, String>();
      for (int i=0;i<valueCodes.length;i++) {
        values.put(valueCodes[i].trim(), valueDescriptions[i].trim().replace("\"", ""));
      }
      tag.setValues(values);
    }

    tagMap.put(tag.getId(), tag);
    tagNames.put(tag.getName(), tag);
    return tag;
  }

  /**
   * Instantiates a new tiff tags.
   *
   * @throws ReadTagsIOException the read tags io exception
   */
  protected TiffTags() throws ReadTagsIOException {
    try {
      Path path = Paths.get("./src/main/resources/tifftags");
      if (Files.exists(path)) {
        // Look in current dir
        File folder = new File("./src/main/resources/tifftags/");
        if (folder.exists() && folder.isDirectory()) {
          for (final File fileEntry : folder.listFiles()) {
            try {
              FileReader fr = new FileReader(fileEntry.toPath().toString());
              BufferedReader br = new BufferedReader(fr);
              readTagFromBuffer(br, true);
            } catch (FileNotFoundException e) {
              throw new ReadTagsIOException();
            }
          }
        }
      } else {
        // Look in JAR
        CodeSource src = TiffTags.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
          ZipInputStream zip = new ZipInputStream(jar.openStream());
          ZipEntry zipFile;
          while ((zipFile = zip.getNextEntry()) != null) {
            String name = zipFile.getName();
            if (name.startsWith("tifftags/") && !name.equals("tifftags/")) {
              try {
                BufferedReader in = new BufferedReader(new InputStreamReader(zip));
                readTagFromBuffer(in, false);
              } catch (Exception ex) {
                throw new ReadTagsIOException();
              }
            }
          }
        } else {
          throw new ReadTagsIOException();
        }
      }
      // generateTagRules();

      tagTypes.put(1, "BYTE");
      tagTypes.put(2, "ASCII");
      tagTypes.put(3, "SHORT");
      tagTypes.put(4, "LONG");
      tagTypes.put(5, "RATIONAL");
      tagTypes.put(6, "SBYTE");
      tagTypes.put(7, "UNDEFINED");
      tagTypes.put(8, "SSHORT");
      tagTypes.put(9, "SSHORT");
      tagTypes.put(10, "SRATIONAL");
      tagTypes.put(11, "FLOAT");
      tagTypes.put(12, "DOUBLE");
      tagTypes.put(13, "SUBIFD");
    } catch (Exception ex) {
      throw new ReadTagsIOException();
    }
  }

  /**
   * Generate tag rules.
   *
   * @throws ReadTagsIOException the read tags io exception
   */
  protected void generateTagRules() throws ReadTagsIOException {
    try {
      PrintWriter writer = new PrintWriter("typecheck.xml", "UTF-8");
      for (int tagId : tagMap.keySet()) {
        Tag tag = tagMap.get(tagId);
        writer.println("  <rule context=\"tag[id=" + tag.getId() + "]\">");
        String typeRule = "";
        for (String tagType : tag.getType()) {
          if (typeRule.length() > 0)
            typeRule += " || ";
          typeRule += "{type=='" + tagType + "'}";
        }
        writer.println("   <assert test=\"" + typeRule + "\">Tag type does not match</assert>");
        writer.println("  </rule>");
      }
      writer.close();

      writer = new PrintWriter("cardinalitycheck.xml", "UTF-8");
      for (int tagId : tagMap.keySet()) {
        Tag tag = tagMap.get(tagId);
        if (tag.getCardinality().length() > 0 && !tag.getCardinality().equals("N")) {
          try {
            int card = Integer.parseInt(tag.getCardinality());
            writer.println("  <rule context=\"tag[id=" + tag.getId() + "]\">");
            String typeRule = "{cardinality==" + card + "}";
            writer.println("   <assert test=\"" + typeRule
                + "\">Tag cardinality does not match</assert>");
            writer.println("  </rule>");
          } catch (Exception ex) {
            // TODO: Deal with formulas
            System.err.println("Formula in tag " + tag.getName() + ": " + tag.getCardinality());
          }
        }
      }
      writer.close();
    } catch (Exception ex) {
      throw new ReadTagsIOException();
    }
  }

  /**
   * Gets the tiff tags.
   *
   * @return the singleton instance
   * @throws ReadTagsIOException the read tags io exception
   */
  public static synchronized TiffTags getTiffTags() throws ReadTagsIOException {
    if (instance == null) {
      instance = new TiffTags();
    }
    return instance;
  }

  /**
   * Gets tag information.
   *
   * @param identifier Tag id
   * @return the tag or null if the identifier does not exist
   */
  public static Tag getTag(int identifier) {
    Tag t = null;
    try {
      if (instance == null)
        getTiffTags();
    } catch (ReadTagsIOException e) {
      /*Nothing to be shown*/
    }
    if (tagMap.containsKey(identifier))
      t = tagMap.get(identifier);
    return t;
  }

  /**
   * Gets the type size.
   *
   * @param type the type
   * @return the type size
   */
  public static int getTypeSize(int type) {
    int typeSize = 1;
    switch (type) {
      case 3:
      case 8:
        typeSize = 2;
        break;
      case 4:
      case 9:
      case 11:
      case 13:
        typeSize = 4;
        break;
      case 5:
      case 10:
      case 12:
        typeSize = 8;
        break;
      default:
        typeSize = 1;
        break;
    }
    return typeSize;
  }

  /**
   * Gets the tag id.
   *
   * @param name the name
   * @return the tag id
   */
  public static int getTagId(String name) {
    int id = -1;
    try {
      if (instance == null)
        getTiffTags();
    } catch (ReadTagsIOException e) {
      /*Nothing to be shown*/
    }
    if (tagNames.containsKey(name))
      id = tagNames.get(name).getId();
    return id;
  }

  /**
   * Checks for tag.
   *
   * @param id the id
   * @return true, if successful
   */
  public static boolean hasTag(int id) {
    return tagMap.containsKey(id);
  }

  /**
   * Gets the tag type name.
   *
   * @param id the id
   * @return the tag type name
   */
  public static String getTagTypeName(int id) {
    return tagTypes.get(id);
  }
}
