/**
 * <h1>Tag.java</h1>
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
 * @author Antonio Manuel Lopez Arjona
 * @version 1.0
 * @since 6/7/2015
 */

package com.easyinnova.iptc;

import java.util.Date;

/**
 * Tag definition.
 */
public class Tag {

    /**
     * The hexadecimal value.
     */
    protected java.lang.String hexadecimal;

    /**
     * The decimal value.
     */
    protected Integer decimal;

    /**
     * The key.
     */
    protected java.lang.String key;

    /**
     * The type of the data.
     */
    protected java.lang.String type;

    /**
     * The mandatory flag.
     */
    protected Boolean mandatory;

    /**
     * The repeatable flag.
     */
    protected Boolean repeatable;

    /**
     * The minimum length.
     */
    protected Integer minimum;

    /**
     * The maximum length.
     */
    protected Integer maximum;

    /**
     * The description.
     */
    protected java.lang.String description;

    /**
     * The created date.
     */
    protected Date created;

    /**
     * The modified date.
     */
    protected Date modified;

    public java.lang.String getHexadecimal() {
        return hexadecimal;
    }

    public void setHexadecimal(java.lang.String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    public Integer getDecimal() {
        return decimal;
    }

    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }

    public java.lang.String getKey() {
        return key;
    }

    public void setKey(java.lang.String key) {
        this.key = key;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Tag(java.lang.String hexadecimal, Integer decimal, java.lang.String key,
        java.lang.String type, Boolean mandatory, Boolean repeatable, Integer minimum,
        Integer maximum, java.lang.String description) {
        super();
        this.hexadecimal = hexadecimal;
        this.decimal = decimal;
        this.key = key;
        this.type = type;
        this.mandatory = mandatory;
        this.repeatable = repeatable;
        this.minimum = minimum;
        this.maximum = maximum;
        this.description = description;
        Date date = new Date();
        this.created = date;
        this.modified = date;
    }


    /**
     * Valid type.
     *
     * @param type the type
     * @return true, if is valid
     */
    public boolean validType(java.lang.String type) {
        boolean valid = false;
        if (type.contains(type))
            valid = true;
        if (type.equals("LONG") && type.contains("UNDEFINED"))
            valid = true;
        return valid;
    }

    /**
     * Checks for typedef.
     *
     * @return true, if successful
     */
    public boolean hasType() {
        return type != null;
    }
}
