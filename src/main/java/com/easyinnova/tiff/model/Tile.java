/**
 * <h1>Tile.java</h1> 
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
 * @since 5/6/2015
 *
 */
package com.easyinnova.tiff.model;

/**
 * The Class Tile.
 */
public class Tile {

  /** The offset. */
  private int offset;

  /** The width. */
  private int width;

  /** The height. */
  private int height;

  /** The padding x. */
  private int paddingX;

  /** The padding y. */
  private int paddingY;

  /**
   * Instantiates a new tile.
   */
  public Tile() {
    paddingX = 0;
    paddingY = 0;
  }

  /**
   * Sets the offset.
   *
   * @param offset the new offset
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /**
   * Sets the width.
   *
   * @param width the new width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Sets the height.
   *
   * @param height the new height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Gets the offset.
   *
   * @return the offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Gets the width.
   *
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height.
   *
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the padding.
   *
   * @param x the x
   * @param y the y
   */
  public void setPadding(int x, int y) {
    paddingX = x;
    paddingY = y;
  }

  /**
   * Gets the padding x.
   *
   * @return the padding x
   */
  public int getPaddingX() {
    return paddingX;
  }

  /**
   * Gets the padding y.
   *
   * @return the padding y
   */
  public int getPaddingY() {
    return paddingY;
  }
}

