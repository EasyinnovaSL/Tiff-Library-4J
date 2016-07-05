/**
 * <h1>Metadata.java</h1> 
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
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at
 * <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 10/6/2015
 *
 */
package com.easyinnova.tiff.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A dictinary of all the metadata contained in the tiff file.
 */
public class Metadata {

  /** The metadata. */
  private HashMap<String, MetadataObject> metadata;

  /**
   * Instantiates a new metadata.
   */
  public Metadata() {
    metadata = new HashMap<String, MetadataObject>();
  }

  /**
   * Adds a metadata value to the dictionary.
   *
   * @param name the name
   * @param value the value
   */
  public void add(String name, TiffObject value) {
    if (!metadata.containsKey(name)) {
      metadata.put(name, new MetadataObject());
    }
    metadata.get(name).getObjectList().add(value);
  }

  /**
   * Adds a metadata value to the dictionary.
   *
   * @param name the name
   * @param value the value
   * @param isDC the is dublin core
   */
  public void add(String name, TiffObject value, boolean isDC) {
    if (!metadata.containsKey(name)) {
      metadata.put(name, new MetadataObject());
      metadata.get(name).setIsDublinCore(isDC);
    }
    metadata.get(name).getObjectList().add(value);
  }

  /**
   * Check if the dictionary contains the given metadata.
   *
   * @param name the name
   * @return true, if successful
   */
  public boolean contains(String name) {
    return metadata.containsKey(name);
  }

  /**
   * Gets a metadata value, returning the appropriate value when multiple are found.
   *
   * @param name the name of the metadata.
   * @return the tiff object with the value of the metadata.
   */
  public TiffObject get(String name) {
    TiffObject result = null;
    String container = null;

    ArrayList<TiffObject> found = new ArrayList<>();
    if (contains(name)) {
      // Find objects with this exact name
      if (metadata.get(name).getObjectList().size() == 1) {
        found.add(getFirst(name));
      } else {
        for (TiffObject to : metadata.get(name).getObjectList()) {
          found.add(to);
        }
      }
    } else {
      // Find objects with similar or equivalent name
      for (String key : metadata.keySet()) {
        boolean similar = key.toLowerCase().equals(name.toLowerCase());
        if (!similar) similar = name.toLowerCase().equals("date") && key.toLowerCase().equals("datetime");
        if (!similar) similar = name.toLowerCase().equals("date") && key.toLowerCase().equals("creatordate");
        if (!similar) similar = name.toLowerCase().equals("description") && key.toLowerCase().equals("imagedescription");
        if (!similar) similar = name.toLowerCase().equals("creator") && key.toLowerCase().equals("artist");
        if (!similar) similar = name.toLowerCase().equals("creator") && key.toLowerCase().equals("creatortool");
        if (similar) {
          for (TiffObject to : metadata.get(key).getObjectList()) {
            found.add(to);
          }
        }
      }
    }

    // Return the most prioritary result
    if (found.size()==1) {
      result = found.get(0);
    } else {
      for (TiffObject to : found) {
        if (result == null) {
          result = to;
          container = to.getContainer();
        } else if (to.getContainer() != null) {
          // Preferences in (descending) order: EXIF, XMP, IPTC, Tiff tag
          if (container == null || to.getContainer().equals("EXIF")
              || (to.getContainer().equals("XMP") && container.equals("IPTC"))) {
            result = to;
            container = to.getContainer();
          }
        }
      }
    }

    return result;
  }

  /**
   * Gets the first metadata object of the fiven metadata name.
   *
   * @param name the name
   * @return the first
   */
  private TiffObject getFirst(String name) {
    return metadata.get(name).getObjectList().get(0);
  }

  /**
   * Gets the list of objects of the given metadata name.
   *
   * @param name the name
   * @return the list
   */
  public List<TiffObject> getList(String name) {
    return metadata.get(name).getObjectList();
  }

  /**
   * Gets the metadata object.
   *
   * @param name the name
   * @return the metadata object
   */
  public MetadataObject getMetadataObject(String name) {
    return metadata.get(name);
  }

  /**
   * Get the set of metadata names.
   *
   * @return the sets the
   */
  public Set<String> keySet() {
    return metadata.keySet();
  }

  /**
   * Adds a complete dictionary to the current one.
   *
   * @param meta the metadata dictionary to add
   */
  public void addMetadata(Metadata meta) {
    for (String k : meta.keySet()) {
      for (TiffObject to : meta.getList(k)) {
        add(k, to, meta.getMetadataObject(k).isDublinCore());
      }
    }
  }
}

