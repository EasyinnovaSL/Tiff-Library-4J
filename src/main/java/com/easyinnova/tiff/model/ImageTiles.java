/**
 * <h1>ImageTiles.java</h1> 
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

import java.util.List;

/**
 * The Class ImageTiles.
 */
public class ImageTiles {

  /** The tiles offsets. */
  private List<Tile> tiles;

  /** The tile width. */
  private int tileWidth;

  /** The tile height. */
  private int tileHeight;

  /**
   * Default constructor.
   */
  public ImageTiles() {}

  /**
   * Gets the tiles.
   *
   * @return the tiles
   */
  public List<Tile> getTiles() {
    return tiles;
  }

  /**
   * Sets the tiles.
   *
   * @param tiles the new tiles
   */
  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

  /**
   * Sets the tile width.
   *
   * @param width the new tile width
   */
  public void setTileWidth(int width) {
    tileWidth = width;
  }

  /**
   * Sets the tile height.
   *
   * @param height the new tile height
   */
  public void setTileHeight(int height) {
    tileHeight = height;
  }

  /**
   * Gets the tile width.
   *
   * @return the tile width
   */
  public int getTileWidth() {
    return tileWidth;
  }

  /**
   * Gets the tile height.
   *
   * @return the tile height
   */
  public int getTileHeight() {
    return tileHeight;
  }
}

