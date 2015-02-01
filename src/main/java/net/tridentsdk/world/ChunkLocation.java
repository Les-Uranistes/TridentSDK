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

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Stores the location of a Chunk
 *
 * @author The TridentSDK Team, sexcel
 */
//TODO: While this mutable object is a compliant solution to the partially initialized objects problem, it is still not deemed a thread-safe alternative as seen in the provided link below
//https://www.securecoding.cert.org/confluence/display/java/TSM03-J.+Do+not+publish+partially+initialized+objects
public class ChunkLocation implements Serializable, Cloneable {
    private static final long serialVersionUID = 9083698035337137603L;
    private final int x;
    private final int z;

    /**
     * Sets the coordinates(x,z) of this ChunkLocation
     *
     * @param x
     *         x value
     * @param z
     *         z value
     */
    private ChunkLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Creates a new ChunkLocation object with the give coordinates(x,z)
     *
     * @param x
     *         x value
     * @param z
     *         z value
     * @return new instantiated ChunkLocation
     */
    public static ChunkLocation create(int x, int z) {
        return new ChunkLocation(x, z);
    }

    /**
     * Returns the x coordinate
     *
     * @return x coordinate
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the z coordinate
     *
     * @return z coordinate
     */
    public int z() {
        return this.z;
    }

    /**
     * Compares the this with the passed Object and compares coordinates(x,z) if the obj is an instanceof ChunkLocation
     *
     * @param obj
     *         Object to compare
     * @return true if ChunkLocation and coordinates match(x,z), else false
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChunkLocation) &&
                ((ChunkLocation) obj).x == x && ((ChunkLocation) obj).z == z;
    }

    /**
     * Clones the ChunkLocation
     *
     * @return ChunkLocation clone of this object
     */
    //TODO: Add to throws clause to method signature
    @Override
    @Nullable
    public Object clone() {
        Object retVal = null;
        try {
            retVal = super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return retVal;
    }
}
