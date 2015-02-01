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

import net.tridentsdk.util.TridentLogger;

import javax.annotation.concurrent.Immutable;

/**
 * Immutable value representing the pitch of a note played
 *
 * @author The TridentSDK Team
 */
@Immutable
public class Note {

    private final short id;

    // TODO convertit depuis 'int' a 'short' en initialiser publique est tres mal.
    // TODO considérer l'augmentation d'exception IllegalArgumentException
    public Note(int id) {
        if (id > 24) {
            TridentLogger.error(new IllegalArgumentException("Note is too high!"));
        } else if (id < 0) {
            TridentLogger.error(new IllegalArgumentException("Note is too low!"));
        }

        this.id = (short) id;
    }

    /**
     * Returns a note one step sharper than this
     */
    public final Note sharpen() {
        if ((this.id + 1) > 24) {
            TridentLogger.error(new IllegalArgumentException("Cannot sharpen this note, it is already the max"));
        }
        return new Note((int) this.id + 1);
    }

    /**
     * Returns a note flatter than this
     */
    public final Note flatten() {
        if ((this.id - 1) < 0) {
            TridentLogger.error(new IllegalArgumentException("Cannot flatten this note, it is already the min"));
        }
        return new Note(this.id - 1);
    }

    public final short id() {
        return id;
    }
}
