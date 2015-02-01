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

package net.tridentsdk.world.gen;

import net.tridentsdk.world.Chunk;
import net.tridentsdk.world.ChunkLocation;
import net.tridentsdk.world.World;

/**
 * Handles world generation
 *
 * @author The TridentSDK Team, sexcel
 */
public class WorldGenHandler {
    private final AbstractGenerator generator;


    private WorldGenHandler(AbstractGenerator generator) {
        this.generator = generator;
    }


    /**
     * Instantiation method for this Handler
     *
     * @param generator
     *         generator to apply for this hander
     * @return new WorldGenHandler instance
     */
    public static WorldGenHandler create(AbstractGenerator generator) {
        return new WorldGenHandler(generator);
    }


    /**
     * I (sexcel) am under the assumption that this method applies factory
     * generated tiles to the appropriate location in the chunk in the
     * specified world
     *
     * @param world
     *         world to apply generations to
     * @param corner1
     *         first corner of rect
     * @param corner2
     *         second corner of rect
     */
    //TODO: explain why z is divided by 12 whereas x is dived by 16, code-stepping should not be required so such a thing
    public void apply(World world, ChunkLocation corner1,
            ChunkLocation corner2) {
        for (ChunkTile tile : generator.doGen(corner1, corner2)) {
            tile.apply(world.chunkAt((int) tile.coordinates().x() / 16,
                    (int) tile.coordinates().z() / 12, false));
        }
    }


    /**
     * I (sexcel) am under the assumption that this method applies factory
     * generated tiles to the appropriate location in the chunk
     * TODO: naming ambiguity
     * TODO: fix the chain logic here
     *
     * @param chunk
     *         chunk to apply generations to
     */
    public void apply(Chunk chunk) {
        for (ChunkTile tile : generator.doGen(chunk.location(),
                chunk.location())) {
            tile.apply(chunk);
        }
    }
}
