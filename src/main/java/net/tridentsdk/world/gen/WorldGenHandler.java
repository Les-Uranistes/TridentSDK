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
 * @author The TridentSDK Team
 */
public class WorldGenHandler {
    private final AbstractGenerator generator;

    private WorldGenHandler(AbstractGenerator generator) {
        this.generator = generator;
    }

    public static WorldGenHandler create(AbstractGenerator generator) {
        return new WorldGenHandler(generator);
    }

    public void apply(World world, ChunkLocation corner1, ChunkLocation corner2) {
        for (ChunkTile tile : generator.doGen(corner1, corner2)) {
            tile.apply(world.chunkAt((int) tile.coordinates().x() / 16, (int) tile.coordinates().z() / 12, false));
        }
    }

    public void apply(Chunk chunk) {
        for (ChunkTile tile : generator.doGen(chunk.location(), chunk.location())) {
            tile.apply(chunk);
        }
    }
}
