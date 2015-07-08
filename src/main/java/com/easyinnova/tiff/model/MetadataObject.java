/**
 * <h1>MetadataObject.java</h1> 
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
 * @since 7/7/2015
 *
 */
package main.java.com.easyinnova.tiff.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MetadataObject.
 */
public class MetadataObject {

  /** The list. */
  private List<TiffObject> list;

  /** The is dublin core. */
  private boolean isDublinCore;

  /**
   * Instantiates a new metadata object.
   *
   * @param objectList the object list
   */
  public MetadataObject(List<TiffObject> objectList) {
    list = objectList;
    isDublinCore = false;
  }

  /**
   * Instantiates a new metadata object.
   */
  public MetadataObject() {
    list = new ArrayList<TiffObject>();
    isDublinCore = false;
  }

  /**
   * Sets the checks if is dublin core.
   *
   * @param isDC the new checks if is dublin core
   */
  public void setIsDublinCore(boolean isDC) {
    isDublinCore = isDC;
  }

  /**
   * Checks if is dublin core.
   *
   * @return true, if is dublin core
   */
  public boolean isDublinCore() {
    return isDublinCore;
  }

  /**
   * Gets the object list.
   *
   * @return the object list
   */
  public List<TiffObject> getObjectList() {
    return list;
  }
}

