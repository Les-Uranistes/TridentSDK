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

package net.tridentsdk.base;

/**
 * Represents the instruments that can be played on a note block
 *
 * @author The TridentSDK Team
 */
public enum Instrument {
    /**
     * Piano note
     */
    PIANO((byte) 0x0),
    /**
     * Bass drum note
     */
    BASS_DRUM((byte) 0x1),
    /**
     * Snare drum note
     */
    SNARE_DRUM((byte) 0x2),
    /**
     * Stick note
     */
    STICKS((byte) 0x3),
    /**
     * Bass guitar note
     */
    BASS_GUITAR((byte) 0x4);

    final byte id;

    Instrument(byte i) {
        this.id = i;
    }

    /**
     * Resolves the Instrument from its respective Byte value
     *
     * @param b Byte representing the Instrument
     * @return Instrument from the supplied Byte
     */
    public static Instrument fromByte(byte b) {
        switch ((int) b) {
            case 0x0:
                return PIANO;
            case 0x1:
                return BASS_DRUM;
            case 0x2:
                return SNARE_DRUM;
            case 0x3:
                return STICKS;
            case 0x4:
                return BASS_GUITAR;
            default:
                return null;
        }
    }

    /**
     * Returns the {@code byte} value of the Instrument
     *
     * @return Byte value of the Instrument
     */
    public byte asByte() {
        return this.id;
    }
}
