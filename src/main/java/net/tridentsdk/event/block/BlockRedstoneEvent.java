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
import net.tridentsdk.event.Cancellable;

/**
 * Called when a block's redstone state is updated, called on each individual section of wire when they change, etc.
 *
 * @author The TridentSDK Team
 */
public class BlockRedstoneEvent extends BlockEvent implements Cancellable {
    private final int strength;
    private final Block causer;
    private final Cause cause;
    private volatile boolean cancelled;

    /**
     * @param block    Block which redstone state was updated
     * @param strength Integer representing the strength (power level) of the redstone
     * @param causer   Block which caused the redstone update
     * @param cause    Cause for the redstone update
     */
    public BlockRedstoneEvent(Block block, int strength, Block causer, Cause cause) {
        super(block);
        this.strength = strength;
        this.causer = causer;
        this.cause = cause;
        this.cancelled = false;
    }

    /**
     * Returns the block which caused the redstone update
     *
     * @return Block which caused the redstone updaye
     */
    public Block causer() {
        return this.causer;
    }

    /**
     * Returns the cause of the redstone update
     *
     * @return Cause of the redstone update
     */
    public Cause cause() {
        return this.cause;
    }

    /**
     * Returns the strength (power level) of the redstone
     *
     * @return Integer representing the power level of the redstone
     */
    public int strength() {
        return this.strength;
    }

    @Override
    public boolean isIgnored() {
        return cancelled;
    }

    @Override
    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Representing the cause of a redstone update
     */
    public enum Cause {
        LEVER,
        BUTTON,
        WIRE,
        TORCH,
        PRESSURE_PLATE,
        HOOK,
        TRAP_CHEST,
        SENSOR,
        REPEATER,
        COMPARATOR
    }
}
