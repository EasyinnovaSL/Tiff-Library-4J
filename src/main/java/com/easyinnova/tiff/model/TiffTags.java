/**
 * <h1>TiffTags.java</h1>
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 18/5/2015
 *
 */

package main.java.com.easyinnova.tiff.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Class TiffTags.
 */
public class TiffTags {

  /** The singleton instance. */
  private static TiffTags instance = null;

  /** The tag map. */
  public static HashMap<Integer, Tag> tagMap = new HashMap<Integer, Tag>();

  /** The tag names. */
  protected static HashMap<String, Tag> tagNames = new HashMap<String, Tag>();

  /** The tag types. */
  public static HashMap<Integer, String> tagTypes = new HashMap<Integer, String>();

  /**
   * Instantiates a new tiff tags.
   *
   * @throws ReadTagsIOException the read tags io exception
   */
  protected TiffTags() throws ReadTagsIOException {
    try {
      Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

      Path path = Paths.get("./src/main/resources");
      if (Files.exists(path)) {
        // Look in current dir
        File folder = new File("./src/main/resources/tifftags/");
        for (final File fileEntry : folder.listFiles()) {
          try {
            BufferedReader br = new BufferedReader(new FileReader(fileEntry.toPath().toString()));

            Tag tag = gson.fromJson(br, Tag.class);

            tagMap.put(tag.getId(), tag);
            tagNames.put(tag.getName(), tag);
          } catch (FileNotFoundException e) {
            throw new ReadTagsIOException();
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

                Tag tag = gson.fromJson(in, Tag.class);
                tagMap.put(tag.getId(), tag);
                tagNames.put(tag.getName(), tag);
              } catch (Exception ex) {
                throw new ReadTagsIOException();
              }
            }
          }
        } else {
          throw new ReadTagsIOException();
        }
      }

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
    }
    if (tagMap.containsKey(identifier))
      t = tagMap.get(identifier);
    return t;
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
}
