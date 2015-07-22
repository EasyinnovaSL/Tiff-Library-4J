/**
 * <h1>Test1.java</h1>
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
 * @since 21/5/2015
 *
 */
package com.easyinnova.tiff;

import org.junit.Test;

/**
 * Testing class.
 */
public class Tests {

  /**
   * Valid examples set.
   */
  @Test
  public void ValidExamples() {
    /*
     * TiffReader tr; int result;
     * 
     * tr = new TiffReader();
     * 
     * 
     * result = tr.readFile("tests\\Header\\Classic Intel.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Header\\Classic Motorola.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Colorspace\\F32.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD tree\\Recommended list.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD tree\\Old school E.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Chunky multistrip.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Chunky singlestrip.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Chunky tile.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Planar multistrip.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Planar singlestrip.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Organization\\Planar tile.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * // Compression // tf = new TiffFile("tests\\Compression\\Motorola nopred comp.TIF"); //
     * assertEquals(0, result); // assertEquals(true, tf.validation.correct);
     * 
     * result = tr.readFile("tests\\Compression\\Motorola nopred nocomp.TIF"); assertEquals(0,
     * result); assertEquals(true, tr.validation.correct);
     * 
     * // tf = new TiffFile("tests\\Compression\\Motorola pred comp.TIF"); // assertEquals(0,
     * result); // assertEquals(true, tf.validation.correct);
     * 
     * result = tr.readFile("tests\\Compression\\Motorola pred nocomp.TIF"); assertEquals(0,
     * result); assertEquals(true, tr.validation.correct);
     * 
     * // tf = new TiffFile("tests\\Compression\\Intel nopred comp.TIF"); // assertEquals(0,
     * result); // assertEquals(true, tf.validation.correct);
     * 
     * result = tr.readFile("tests\\Compression\\Intel nopred nocomp.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     * 
     * // tf = new TiffFile("tests\\Compression\\Intel pred comp.TIF"); // assertEquals(0, result);
     * // assertEquals(true, tf.validation.correct);
     * 
     * result = tr.readFile("tests\\Compression\\Intel pred nocomp.TIF"); assertEquals(0, result);
     * assertEquals(true, tr.validation.correct);
     */
  }

  /**
   * Invalid examples set.
   */
  @Test
  public void InvalidExamples() {
    /*
     * TiffReader tr; int result;
     * 
     * tr = new TiffReader();
     * 
     * 
     * result = tr.readFile("tests\\Header\\Nonsense byteorder E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Header\\Incorrect version E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD Struct\\Insane tag count E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD Struct\\Circular E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD Struct\\Circular Short E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD Struct\\Beyond EOF E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\IFD Struct\\Premature EOF E.TIF"); assertEquals(0, result);
     * assertEquals(false, tr.validation.correct);
     * 
     * result = tr.readFile("tests\\Colorspace\\I8 bad BitsPerSample count E.TIF"); assertEquals(0,
     * result); assertEquals(false, tr.validation.correct);
     */
  }
}

