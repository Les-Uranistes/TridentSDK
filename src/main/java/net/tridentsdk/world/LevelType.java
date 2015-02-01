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

package net.tridentsdk.world;

/**
 * I (sexcel) am under the assumption that this class represents the biome
 * type of the Minecraft world
 * TODO: add your fucking name to this
 *
 * @author sexcel
 */
public enum LevelType {

    DEFAULT("default"),
    FLAT("flat"),
    LARGE_BIOMES("largeBiomes"),
    //TODO: CamelCase:  I (sexcel) don't understand why you're doing this
    AMPLIFIED("amplified"),

    DEFAULT_1_1("default_1_1"); //TODO: SnakeCase: I (sexcel) don't understand why you're doing this.  Also wtf is
    // this supposed to mean.

    private final String typeName;


    /**
     * Sets the typeName to passed typeName
     *
     * @param typeName
     *         name of LevelType
     */
    LevelType(String typeName) {
        this.typeName = typeName;
    }


    /**
     * Retrieves the LevelType based on the input levelName
     * If not matched, will default to {@see DEFAULT}
     *
     * @param levelName
     *         name of level
     * @return LevelType value, else null
     */
    public static LevelType levelTypeOf(String levelName) {
        LevelType retVal = LevelType.DEFAULT;
        for (LevelType level : values()) {
            if (level.typeName.equalsIgnoreCase(levelName)) {
                retVal = level;
                break;
            }
        }

        return retVal;
    }


    /**
     * Returns the string name of the LevelType
     * TODO: I (sexcel) don't know why you wouldn't just use the name of the enum value
     *
     * @return String representation of this enum value
     */
    @Override
    public String toString() {
        return this.typeName;
    }
}
