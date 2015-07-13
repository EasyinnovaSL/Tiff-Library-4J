/**
 * <h1>IPTC.java</h1>
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
 * @since 9/6/2015
 */
package com.easyinnova.tiff.model.types;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.easyinnova.iptc.IptcTags;
import com.easyinnova.iptc.Tag;
import com.easyinnova.iptc.abstractIptcType;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;

/**
 * The Class IPTC.
 */
public class IPTC extends abstractTiffType {

    public static final int[] SEGMENT_MARKER = {28, 2};
    /**
     * The xml.
     */
    private HashMap<Byte, List<abstractIptcType>> content;


    /**
     * Creates the metadata.
     *
     * @return the hash map
     */
    @Override public Metadata createMetadata() {
        Metadata metadata = new Metadata();
        try {
            Iterator<Byte> it = content.keySet().iterator();
            while (it.hasNext()) {
                Byte b = it.next();
                List<abstractIptcType> valueList = content.get(b);
                for (int i = 0; i < valueList.size(); i++) {
                    Text txt = new Text(valueList.get(i).toString());
                    metadata.add(b.toString(), txt);
                }
            }
        } catch (Exception ex) {
        }

        return metadata;
    }

    @Override public String toString() {
        HashMap<String, List<abstractIptcType>> result =
            new HashMap<String, List<abstractIptcType>>();
        try {
            Iterator<Byte> it = content.keySet().iterator();
            while (it.hasNext()) {
                Byte b = it.next();
                List<abstractIptcType> valueList = content.get(b);
                for (int i = 0; i < valueList.size(); i++) {
                    List<abstractIptcType> list = result.get(b.toString());
                    if (list == null) {
                        list = new ArrayList<abstractIptcType>();
                    }
                    list.add(valueList.get(i));
                    result.put(b.toString(), list);
                }
            }
        } catch (Exception ex) {
        }
        return result.toString();
    }


    /**
     * Reads the IPTC.
     *
     * @param tv the TagValue containing the array of bytes of the IPTC
     */
    @Override public void read(TagValue tv) {
        content = new HashMap<Byte, List<abstractIptcType>>();

        for (int i = 0; i < tv.getCardinality(); i++) {
            if (tv.getValue().get(i).toInt() == SEGMENT_MARKER[0]) {
                /*check if segment contains type, size and content*/
                if ((i + 5) < tv.getCardinality()
                    && tv.getValue().get(i + 1).toInt() == SEGMENT_MARKER[1]) {
                    Byte type = new Byte(0);
                    type.setValue(tv.getValue().get(i + 2).toByte());
                    int size = ((tv.getValue().get(i + 3).toByte() & 0xff) << 8) | (
                        tv.getValue().get(i + 4).toByte() & 0xff);
                    if ((i + 4 + size) < tv.getCardinality()) {
                        List<Byte> value = new ArrayList<Byte>();
                        /*read the value of the tag*/
                        for (int j = (i + 5); j <= (i + 4 + size); j++) {
                            Byte current = new Byte(0);
                            current.setValue(tv.getValue().get(j).toByte());
                            value.add(current);
                        }

                        abstractIptcType object = null;
                        if (IptcTags.hasTag(type.getValue())) {
                            Tag t = IptcTags.getTag(type.getValue());
                            if (t.hasType()) {
                                String tagclass = t.getType();

                                try {
                                    object = (abstractIptcType) Class
                                        .forName("com.easyinnova.iptc." + tagclass).getConstructor()
                                        .newInstance();
                                    object.read(value);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                    //validation.addError("Parse error getting tag " + id + " value");
                                } catch (NoSuchMethodException | SecurityException e) {
                                    e.printStackTrace();
                                    //validation.addError("Parse error getting tag " + id + " value");
                                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                                    | InvocationTargetException e) {
                                    e.printStackTrace();
                                    //validation.addError("Parse error getting tag " + id + " value");
                                }
                            }
                        }
                        if (object != null) {
                            List<abstractIptcType> list = content.get(type);
                            if (list == null) {
                                list = new ArrayList<abstractIptcType>();
                            }
                            list.add(object);
                            content.put(type, list);
                        }
            /*jump to the end of readed tag*/
                        i = i + 4 + size;
                    }
                }
            }
        }



        tv.clear();
        tv.add(this);
    }

    @Override public boolean containsMetadata() {
        return true;
    }

}
