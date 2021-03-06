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

package net.tridentsdk.event.block;

import net.tridentsdk.base.Block;
import net.tridentsdk.event.Event;

/**
 * Represents any event that involves a block modification
 * <p/>
 * <p>This is an umbrella event, do not listen to it</p>
 *
 * @author The TridentSDK Team
 */
public abstract class BlockEvent extends Event {

    private final Block block;

    /**
     * @param block the block associated with the event
     */
    public BlockEvent(Block block) {
        this.block = block;
    }

    /**
     * @return return the block associated with the event
     */
    public Block block() {
        return this.block;
    }
}
