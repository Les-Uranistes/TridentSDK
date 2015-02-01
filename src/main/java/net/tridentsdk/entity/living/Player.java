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

package net.tridentsdk.entity.living;

import net.tridentsdk.GameMode;
import net.tridentsdk.Messagable;
import net.tridentsdk.entity.LivingEntity;
import net.tridentsdk.entity.decorate.InventoryHolder;
import net.tridentsdk.plugin.cmd.CommandIssuer;
import net.tridentsdk.window.inventory.Item;

import java.util.Locale;

/**
 * Represents a player entity after joining the server
 *
 * @author The TridentSDK Team
 */
public interface Player extends LivingEntity, Messagable, CommandIssuer, InventoryHolder {

    /**
     * Returns the flying speed of the Player
     *
     * @return float flying speed of the Player
     */
    float flyingSpeed();

    /**
     * Set the flying speed of the Player
     *
     * @param flyingSpeed float flying speed of the Player
     */
    void setFlyingSpeed(float flyingSpeed);

    // TODO: Use word settings?

    /**
     * Returns the Player's {@link Locale} settings
     *
     * @return Locale the Player's Locale settings
     */
    Locale locale();

    /**
     * Returns the ItemStack in the Player's hand
     *
     * @return ItemStack current ItemStack in the Player's hand
     */
    Item heldItem();

    /**
     * Returns the GameMode the Player is in
     *
     * @return GameMode current GameMode of the Player
     */
    GameMode gameMode();

    /**
     * Returns the move speed of the player
     *
     * @return float the Player's current move speed
     */
    float moveSpeed();

    /**
     * Sets the Player's move speed
     *
     * @param speed float Player's move speed
     */
    void setMoveSpeed(float speed);

    /**
     * Returns the sneak speed of the player
     *
     * @return float the Player's current sneak speed
     */
    float sneakSpeed();

    /**
     * Sets the Player's sneak speed
     *
     * @param speed float Player's sneak speed
     */
    void setSneakSpeed(float speed);

    /**
     * Returns the walk speed of the player
     *
     * @return float the Player's current walk speed
     */
    float walkSpeed();

    /**
     * Sets the Player's walk speed
     *
     * @param speed float Player's walk speed
     */
    void setWalkSpeed(float speed);

    /**
     * Sends the player a message
     *
     * @param message the message to display to the player
     */
    void sendMessage(String message);
}
