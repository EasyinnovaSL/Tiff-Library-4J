/**
 * <h1>IfdReader.java</h1>
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
 * @since 2/6/2015
 *
 */
package main.java.com.easyinnova.tiff.reader;

import main.java.com.easyinnova.tiff.model.ImageStrips;
import main.java.com.easyinnova.tiff.model.ImageTiles;
import main.java.com.easyinnova.tiff.model.Strip;
import main.java.com.easyinnova.tiff.model.TiffTags;
import main.java.com.easyinnova.tiff.model.Tile;
import main.java.com.easyinnova.tiff.model.types.IFD;

import java.util.ArrayList;
import java.util.List;

/**
 * The object to store the result of an ifd parsing.
 */
public class IfdReader {

  /** The ifd. */
  IFD ifd;

  /** The next ifd offset. */
  int nextIfdOffset;

  /**
   * Gets the next ifd offset.
   *
   * @return the next ifd offset
   */
  public int getNextIfdOffset() {
    return nextIfdOffset;
  }

  /**
   * Gets the ifd.
   *
   * @return the ifd
   */
  public IFD getIfd() {
    return ifd;
  }

  /**
   * Sets the ifd.
   *
   * @param ifd the new ifd
   */
  public void setIfd(IFD ifd) {
    this.ifd = ifd;
  }

  /**
   * Sets the next ifd offset.
   *
   * @param offset the new next ifd offset
   */
  public void setNextIfdOffset(int offset) {
    nextIfdOffset = offset;
  }

  /**
   * Reads the image, which can be stored in strips and/or in tiles.
   */
  public void readImage() {
    if (ifd.containsTagId(TiffTags.getTagId("StripOffsets"))
        && ifd.containsTagId(TiffTags.getTagId("StripBYTECount"))) {
      readStrips();
    }
    if (ifd.containsTagId(TiffTags.getTagId("TileOffsets"))
        && ifd.containsTagId(TiffTags.getTagId("TileBYTECounts"))) {
      readTiles();
    }
  }

  /**
   * Read the tiles and store them (its offsets) in the imagetiles object.
   */
  private void readTiles() {
    ImageTiles imageTiles = new ImageTiles();
    List<Tile> tiles = new ArrayList<Tile>();
    long totalWidth = ifd.getTag("ImageWidth").getFirstNumericValue();
    long totalLength = ifd.getTag("ImageLength").getFirstNumericValue();
    long tilesWidth = ifd.getTag("TileWidth").getFirstNumericValue();
    long tilesHeight = ifd.getTag("TileLength").getFirstNumericValue();
    int actualWidth = 0;
    long actualHeight = tilesHeight;
    for (int i = 0; i < ifd.getTag("TileOffsets").getValue().size(); i++) {
      int to = ifd.getTag("TileOffsets").getValue().get(i).toInt();
      Tile tile = new Tile();
      tile.setOffset(to);
      tile.setWidth((int) tilesWidth);
      tile.setHeight((int) tilesHeight);
      long padX = 0;
      long padY = 0;
      boolean newLine = false;
      actualWidth += tilesWidth;
      if (actualWidth > totalWidth) {
        padX = tilesWidth - actualWidth % totalWidth;
        newLine = true;
      }
      if (actualHeight > totalLength) {
        padY = tilesHeight - actualHeight % totalLength;
      }
      tile.setPadding((int) padX, (int) padY);
      if (newLine) {
        actualHeight += tilesHeight;
        actualWidth = 0;
      }
      tiles.add(tile);
    }
    imageTiles.setTiles(tiles);

    imageTiles.setTileHeight((int) tilesHeight);
    imageTiles.setTileWidth((int) tilesWidth);
    ifd.setImageTiles(imageTiles);
  }

  /**
   * Read the strips and store them (its offsets) in the imagestrips object.
   */
  private void readStrips() {
    ImageStrips imageStrips = new ImageStrips();
    List<Strip> strips = new ArrayList<Strip>();
    long tsbc = ifd.getTag("StripBYTECount").getFirstNumericValue();
    long rps = 0;
    if (!ifd.containsTagId(TiffTags.getTagId("RowsPerStrip")))
      rps = 1;
    else rps = ifd.getTag("RowsPerStrip").getFirstNumericValue();
    long rowLength = tsbc / rps;
    if (rowLength == 0)
      rowLength = 1;
    for (int i = 0; i < ifd.getTag("StripOffsets").getValue().size(); i++) {
      int so = ifd.getTag("StripOffsets").getValue().get(i).toInt();
      int sbc = ifd.getTag("StripBYTECount").getValue().get(i).toInt();
      Strip strip = new Strip();
      strip.setOffset(so);
      strip.setLength(sbc);
      strip.setStripRows((int) (sbc / rowLength));
      strips.add(strip);
    }
    imageStrips.setStrips(strips);
    imageStrips.setRowsPerStrip(rps);

    ifd.setImageStrips(imageStrips);
  }
}

