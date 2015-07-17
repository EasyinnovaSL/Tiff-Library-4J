/**
 * <h1>Tag.java</h1> <p> This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version; or, at your choice, under
 * the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p> <p> This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License and the Mozilla Public License for more details. </p> <p> You should have received
 * a copy of the GNU General Public License and the Mozilla Public License along with this program.
 * If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the ©
 * statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> © 2015
 * Easy Innova, SL </p>
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

    /**
     * Instantiates a new tag.
     *
     * @param hexadecimal the hexadecimal
     * @param decimal     the decimal
     * @param key         the key
     * @param type        the type
     * @param mandatory   the mandatory
     * @param repeatable  the repeatable
     * @param minimum     the minimum
     * @param maximum     the maximum
     * @param description the description
     */
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
     * Gets the hexadecimal.
     *
     * @return the hexadecimal
     */
    public java.lang.String getHexadecimal() {
        return hexadecimal;
    }

    /**
     * Sets the hexadecimal.
     *
     * @param hexadecimal the new hexadecimal
     */
    public void setHexadecimal(java.lang.String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    /**
     * Gets the decimal.
     *
     * @return the decimal
     */
    public Integer getDecimal() {
        return decimal;
    }

    /**
     * Sets the decimal.
     *
     * @param decimal the new decimal
     */
    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public java.lang.String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key the new key
     */
    public void setKey(java.lang.String key) {
        this.key = key;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Gets the mandatory.
     *
     * @return the mandatory
     */
    public Boolean getMandatory() {
        return mandatory;
    }

    /**
     * Sets the mandatory.
     *
     * @param mandatory the new mandatory
     */
    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Gets the repeatable.
     *
     * @return the repeatable
     */
    public Boolean getRepeatable() {
        return repeatable;
    }

    /**
     * Sets the repeatable.
     *
     * @param repeatable the new repeatable
     */
    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    /**
     * Gets the minimum.
     *
     * @return the minimum
     */
    public Integer getMinimum() {
        return minimum;
    }

    /**
     * Sets the minimum.
     *
     * @param minimum the new minimum
     */
    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    /**
     * Gets the maximum.
     *
     * @return the maximum
     */
    public Integer getMaximum() {
        return maximum;
    }

    /**
     * Sets the maximum.
     *
     * @param maximum the new maximum
     */
    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the created.
     *
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the created.
     *
     * @param created the new created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets the modified.
     *
     * @return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Sets the modified.
     *
     * @param modified the new modified
     */
    public void setModified(Date modified) {
        this.modified = modified;
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
