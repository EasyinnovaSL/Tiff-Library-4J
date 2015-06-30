/**
 * <h1>InputBuffer.java</h1> 
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
 * @since 26/6/2015
 *
 */
package main.java.com.easyinnova.tiff.io;

import java.io.IOException;

/**
 * The Class InputBuffer.
 */
public class InputBuffer {
  /** The internal buffer. */
  private int[] buffer;

  /** The maximum internal buffer size. */
  private int maxBufferSize = 100;

  /** The current buffer size. */
  private int currentBufferSize;

  /** The buffer offset (file position of the 0th element). */
  private long bufferOffset;

  /** The input stream. */
  private TiffInputStream input;

  /**
   * Instantiates a new input buffer.
   *
   * @param input the input
   */
  public InputBuffer(TiffInputStream input) {
    this.input = input;
    bufferOffset = 0;
    currentBufferSize = 0;
    if (maxBufferSize >= 0)
      buffer = new int[maxBufferSize];
  }

  /**
   * Checks if the given offset is already contained in the internal buffer.<br>
   * If not, fills the buffer starting at the given offset position.
   *
   * @param offset the offset to check
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void checkBuffer(long offset) throws IOException {
    if (offset - bufferOffset < 0 || offset - bufferOffset >= currentBufferSize) {
      // the given offset is not contained in the buffer
      bufferOffset = offset;
      int index = 0;
      input.seek(offset);
      try {
        for (long pos = offset; pos < offset + maxBufferSize; pos++) {
          int ch = input.read();
          buffer[index] = ch;
          index++;
        }
      } catch (IOException ex) {
        // end of file reached -> do nothing
      }
      currentBufferSize = index;
    }
  }

  /**
   * Seek.
   *
   * @param offset the offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void seek(long offset) throws IOException {
    if (maxBufferSize <= 0) {
      // old-school (no buffer optimization)
      input.seek(offset);
    } else {
      checkBuffer(offset);
    }
  }

  /**
   * Reads a byte.
   *
   * @param offset the offset
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int read(long offset) throws IOException {
    int b;
    if (maxBufferSize <= 0) {
      // old-school (no buffer optimization)
      b = input.read();
    } else {
      checkBuffer(offset);
      b = buffer[(int) (offset - bufferOffset)];
    }
    return b;
  }
}

