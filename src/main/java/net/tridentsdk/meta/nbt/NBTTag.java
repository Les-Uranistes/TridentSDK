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

package net.tridentsdk.meta.nbt;

/**
 * @author The TridentSDK Team
 */
public abstract class NBTTag {

    String name;
    boolean hasName;

    public NBTTag(String name) {
        if (name != null) {
            if (name.equals("")) {
                // a blank name is a null name
                return;
            }
            this.name = name;
            this.hasName = true;
        }

    }

    public <T extends NBTTag> T asType(Class<T> type) {
        return (T) this;
    }

    public String name() {
        return this.name;
    }

    public abstract TagType type();

    public boolean hasName() {
        return this.hasName;
    }
}
