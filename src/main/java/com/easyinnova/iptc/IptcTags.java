/**
 * <h1>IptcTags.java</h1> <p> This program is free software: you can redistribute it and/or modify
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
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 6/7/2015
 */

package com.easyinnova.iptc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.easyinnova.tiff.model.ReadTagsIOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.String;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Class IptcTags.
 */
public class IptcTags {

  /**
   * The singleton instance.
   */
  private static IptcTags instance = null;

  /**
   * The tag map.
   */
  public static HashMap<Integer, Tag> tagMap = new HashMap<Integer, Tag>();

  /**
   * The tag names.
   */
  protected static HashMap<java.lang.String, Tag> tagKeys = new HashMap<java.lang.String, Tag>();

  /**
   * The tag types.
   */
  public static HashMap<Integer, java.lang.String> tagTypes =
      new HashMap<Integer, java.lang.String>();

  /**
   * Instantiates a new iptc tags.
   */
  protected IptcTags() throws ReadTagsIOException {
    try {
      String folderPath = "./src/main/resources/iptc/";
      Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
      File folder = new File(folderPath);

      if (folder.exists() && folder.isDirectory()) {
        // Look in current dir
        for (final File fileEntry : folder.listFiles()) {
          try {
            BufferedReader br =
                new BufferedReader(new FileReader(fileEntry.toPath().toString()));

            Tag tag = gson.fromJson(br, Tag.class);

            tagMap.put(tag.getDecimal(), tag);
            tagKeys.put(tag.getKey(), tag);
          } catch (FileNotFoundException e) {
            throw new ReadTagsIOException();
          }
        }
      } else {
        // Look in JAR
        CodeSource src = IptcTags.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
          ZipInputStream zip = new ZipInputStream(jar.openStream());
          ZipEntry zipFile;
          while ((zipFile = zip.getNextEntry()) != null) {
            String name = zipFile.getName();
            if (name.startsWith("iptc/") && !name.equals("iptc/")) {
              try {
                BufferedReader in = new BufferedReader(new InputStreamReader(zip));

                Tag tag = gson.fromJson(in, Tag.class);
                tagMap.put(tag.getDecimal(), tag);
                tagKeys.put(tag.getKey(), tag);
              } catch (Exception ex) {
                throw new ReadTagsIOException();
              }
            }
          }
        } else {
          throw new ReadTagsIOException();
        }
      }


      tagTypes.put(1, "SHORT");
      tagTypes.put(2, "STRING");
      tagTypes.put(3, "DATE");
      tagTypes.put(4, "TIME");
      tagTypes.put(5, "UNDEFINED");
    } catch (Exception ex) {
      throw new ReadTagsIOException();
    }
  }

  /**
   * Gets the iptc tags.
   *
   * @return the singleton instance
   */
  public static synchronized IptcTags getIptcTags() throws ReadTagsIOException {
    if (instance == null) {
      instance = new IptcTags();
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
        getIptcTags();
    } catch (ReadTagsIOException e) {
            /*Nothing to be shown*/
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
  public static int getTagId(java.lang.String name) {
    int id = -1;
    try {
      if (instance == null)
        getIptcTags();
    } catch (ReadTagsIOException e) {
            /*Nothing to be shown*/
    }
    if (tagKeys.containsKey(name))
      id = tagKeys.get(name).getDecimal();
    return id;
  }

  /**
   * Checks for tag.
   *
   * @param id the id
   * @return true, if successful
   */
  public static boolean hasTag(int id) {
    try {
      if (instance == null)
        getIptcTags();
    } catch (ReadTagsIOException e) {
            /*Nothing to be shown*/
    }
    return tagMap.containsKey(id);
  }
}
