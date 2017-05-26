/**
 * <h1>IccTags.java</h1>
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 25/5/2015
 *
 */
package com.easyinnova.tiff.model.types;

import com.easyinnova.tiff.model.Tag;
import com.easyinnova.tiff.model.TiffTags;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class IccTags.
 */
public class IccTags implements Serializable {
  /** Do not modify! */
  private static final long serialVersionUID = 2946L;

  /** Tag list. */
  public ArrayList<IccTag> tags;

  /** The Hash tags id. */
  public HashMap<Integer, IccTag> hashTagsId;

  /** The Hash tags name. */
  public HashMap<String, IccTag> hashTagsName;

  /**
   * Instantiates a new icc tags.
   */
  public IccTags() {
    tags = new ArrayList<IccTag>();
    hashTagsId = new HashMap<Integer, IccTag>();
    hashTagsName = new HashMap<String, IccTag>();
  }

  /**
   * Adds a tag.
   *
   * @param tag the tag
   */
  public void addTag(IccTag tag) {
    tags.add(tag);
    hashTagsId.put(tag.signature, tag);
    Tag t = TiffTags.getTag(tag.signature);
    if (t != null)
      hashTagsName.put(t.getName(), tag);
  }
}

