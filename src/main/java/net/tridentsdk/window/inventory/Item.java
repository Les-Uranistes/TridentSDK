/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tridentsdk.window.inventory;

import net.tridentsdk.base.Substance;

/**
 * Inventory item, holding all properties of the item
 *
 * @author The TridentSDK Team, sexcel
 */
public class Item {

    private final int       id;
    private final Substance mat;

    private volatile short quantity;
    private volatile short damageValue;


    public Item(Substance mat) {
        this(mat, (short) 1);
    }


    public Item(Substance mat, short quantity) {
        this.id = mat.id();
        this.mat = mat;

        this.quantity = quantity;
        this.damageValue = (short) 100; // psudeo-value //Not a pseudo value, its a default according to the code atm
    }


    public int id() {
        return this.id;
    }


    public Substance type() {
        return this.mat;
    }


    public short quantity() {
        return this.quantity;
    }


    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }


    public short damageValue() {
        return this.damageValue;
    }


    public void setDamageValue(short damageValue) {
        this.damageValue = damageValue;
    }


    public boolean isSimilar(Item i) {
        return (id == i.id) &&
                (mat == i.mat) &&
                (quantity == i.quantity) &&
                (damageValue == i.damageValue);

    }
}
