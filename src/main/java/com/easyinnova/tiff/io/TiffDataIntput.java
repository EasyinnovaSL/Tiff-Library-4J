/**
 * <h1>TiffDataIntput.java</h1> 
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
 * @author Xavier Tarrés Bonet
 * @version 1.0
 * @since 26/5/2015
 *
 */
package main.java.com.easyinnova.tiff.io;

import main.java.com.easyinnova.tiff.model.types.Ascii;
import main.java.com.easyinnova.tiff.model.types.Byte;
import main.java.com.easyinnova.tiff.model.types.Double;
import main.java.com.easyinnova.tiff.model.types.Float;
import main.java.com.easyinnova.tiff.model.types.Long;
import main.java.com.easyinnova.tiff.model.types.Rational;
import main.java.com.easyinnova.tiff.model.types.SByte;
import main.java.com.easyinnova.tiff.model.types.SLong;
import main.java.com.easyinnova.tiff.model.types.SRational;
import main.java.com.easyinnova.tiff.model.types.SShort;
import main.java.com.easyinnova.tiff.model.types.Short;
import main.java.com.easyinnova.tiff.model.types.Undefined;

import java.io.IOException;

/**
 * The Interface TiffDataIntput.
 */
public interface TiffDataIntput {
  
  /**
   * Read byte.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  Byte readByte() throws IOException;
  
  /**
   * Read byte.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
 
  Ascii readAscii() throws IOException;
  
  /**
   * Read ascii.
   *
   * @return the ascii
   * @throws IOException Signals that an I/O exception has occurred.
   */
 
  Short readShort() throws IOException;
  
  /**
   * Read long.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  Long readLong() throws IOException;
  
  /**
   * Read rational.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  Rational readRational() throws IOException;
  
  /**
   * Read s byte.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  SByte readSByte() throws IOException;
  
  /**
   * Read undefined.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  Undefined readUndefined() throws IOException;
  
  /**
   * Read s short.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  SShort readSShort() throws IOException;
  
  /**
   * Read s long.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  SLong readSLong() throws IOException;
  
  /**
   * Read s rational.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  SRational readSRational() throws IOException;
  
  /**
   * Read float.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
  Float readFloat() throws IOException;
  
  /**
   * Read double.
   *
   * @return the byte
   * @throws IOException Signals that an I/O exception has occurred.
   */
   Double readDouble() throws IOException;
   
}
