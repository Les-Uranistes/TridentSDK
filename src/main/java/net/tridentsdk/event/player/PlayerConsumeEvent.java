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

package net.tridentsdk.event.player;

import net.tridentsdk.entity.DroppedItem;
import net.tridentsdk.entity.living.Player;

/**
 * Called when a player consumes food, or potions
 *
 * @author The TridentSDK Team
 */
public class PlayerConsumeEvent extends PlayerHungerEvent {

    private final DroppedItem item;
    private volatile boolean cancelled;

    /**
     * @param player the player associated with this event
     * @param feed   the amount of hunger replenished
     * @param item   the item consumed
     */

    public PlayerConsumeEvent(Player player, DroppedItem item, double feed) {
        super(player, feed);
        this.setReplenishAmount(feed);
        this.item = item;
        this.cancelled = false;
    }

    /**
     * @param feed the amount of hunger replenished
     */
    public void setReplenishAmount(double feed) {
        super.setFeed(feed);
    }

    /**
     * @return return the amount of hunger replenished
     */
    public double replenishAmount() {
        return super.feed();
    }

    /**
     * @return return the item consumed
     */
    public DroppedItem food() {
        return this.item;
    }

    @Override
    public boolean isIgnored() {
        return cancelled;
    }

    @Override
    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
