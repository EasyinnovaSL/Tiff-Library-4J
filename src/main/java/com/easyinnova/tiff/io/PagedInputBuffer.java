/**
 * <h1>PagedInputBuffer.java</h1> 
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
 * @since 14/7/2015
 *
 */
package com.easyinnova.tiff.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Paged Buffered Tiff File Stream.<br>
 * Incorporates a set of pages (buffers) for minimizing the file reads all over the file.
 */
public class PagedInputBuffer {

  /** The buffer pages. */
  private List<InputBuffer> pages;

  /** The Max number of pages. */
  private int MaxPages = 5;

  /** The current buffer. */
  private InputBuffer currentBuffer;

  /** The input stream. */
  private TiffInputStream input;

  /**
   * Instantiates a new paged input buffer.
   *
   * @param input the input
   */
  public PagedInputBuffer(TiffInputStream input) {
    pages = new ArrayList<InputBuffer>();
    currentBuffer = null;
    this.input = input;
  }

  /**
   * Select buffer.
   *
   * @param offset the offset
   */
  private void selectPage(long offset) {
    currentBuffer = null;
    int i = 0;

    // Look if the given offset is already stored in a page
    while (i < pages.size() && currentBuffer == null) {
      if (pages.get(i).seekSuccessful(offset))
        currentBuffer = pages.get(i);
      i++;
    }

    if (currentBuffer == null) {
      // If the offset is not contained in any of the loaded pages, create a new one

      // FIFO
      if (pages.size() >= MaxPages)
        pages.remove(0);
      pages.add(new InputBuffer(input));

      currentBuffer = pages.get(pages.size() - 1);
    }
  }

  /**
   * Seek an offset.
   *
   * @param offset the offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(long offset) throws IOException {
    selectPage(offset);
    currentBuffer.seek(offset);
  }

  /**
   * Reads a byte.
   *
   * @param offset the offset
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int read(long offset) throws IOException {
    selectPage(offset);
    return currentBuffer.read(offset);
  }
}
