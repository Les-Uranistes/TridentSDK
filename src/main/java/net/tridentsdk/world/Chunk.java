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

import net.tridentsdk.base.Block;

/**
 * I (sexcel) am under the assumption that this class represents a Minecraft world chunk
 * TODO: add your fucking name to this
 *
 * @author sexcel
 */
public interface Chunk {
    /**
     * I'm under the assumption that this method is supposed to fill the
     * chunk with blocks
     *
     * TODO: rename this method to something less ambiguous OR fill JavaDoc with more pertinent information
     */
    void generate();

    /**
     * Returns a representation of a 2 dimensional vector POJO that is
     * cloneable and serializable
     * It purely contains the {@link #x}
     * and {@link #z} values
     *
     * @return location object
     */
    ChunkLocation location();

    /**
     * Returns the x coordinate of this chunk
     * I'm under the assumption that this is the upper left corner of the chunk
     *
     * @return x coordinate
     */
    int x();

    /**
     * Returns the z coordinate of this chunk
     * I'm under the assumption that this is the upper left corner of the chunk
     *
     * @return z coordinate
     */
    int z();

    /**
     * Returns the {@code World} that this {@code Chunk} belongs to
     *
     * @return world object
     */
    World world();

    /**
     * Retrieves a {@code Block} object at the specified coordinate (x, y, z)
     *
     * @param relX
     *         x value
     * @param y
     *         y value
     * @param relZ
     *         z value
     * @return relative Block object at specified coordinate values
     */
    Block tileAt(int relX, int y, int relZ);

    /**
     * Returns the {@code ChunkSnapshot} object linked to this chunk
     *
     * @return snapshot object
     */
    ChunkSnapshot snapshot();
}
