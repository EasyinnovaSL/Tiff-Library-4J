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
package main.java.com.easyinnova.tiff.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A dictinary of all the metadata contained in the tiff file.
 */
public class Metadata {

  /** The metadata. */
  private HashMap<String, List<TiffObject>> metadata;

  /**
   * Instantiates a new metadata.
   */
  public Metadata() {
    metadata = new HashMap<String, List<TiffObject>>();
  }

  /**
   * Adds a metadata value to the dictionary.
   *
   * @param name the name
   * @param value the value
   */
  public void add(String name, TiffObject value) {
    if (!metadata.containsKey(name))
      metadata.put(name, new ArrayList<TiffObject>());
    metadata.get(name).add(value);
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
   * Gets the first metadata object of the fiven metadata name.
   *
   * @param name the name
   * @return the first
   */
  public TiffObject getFirst(String name) {
    return metadata.get(name).get(0);
  }

  /**
   * Gets the list of objects of the given metadata name.
   *
   * @param name the name
   * @return the list
   */
  public List<TiffObject> getList(String name) {
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
        add(k, to);
      }
    }
  }
}

