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

package net.tridentsdk;

import net.tridentsdk.factory.Factories;
import net.tridentsdk.util.TridentLogger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Information displayed to the client in the server list
 *
 * @author The TridentSDK Team
 */
public class DisplayInfo {

    /**
     * A string containing the current broadcast MOTD of the server
     *
     * @return a string containing the MOTD of the server, may be empty, never null
     */
    public String motd() {
        return Trident.config().getString("motd", Defaults.MOTD);
    }

    /**
     * Sets the string displayed below the server name in the multiplayer menu
     *
     * @param motd the string to set. Supports color codes.
     */
    public void setMotd(String motd) {
        Trident.config().setString("motd", motd);
    }

    /**
     * Returns the {@link java.io.File} that represents the picture displayed next to the server listing on clients
     *
     * @return the file that represents the picture sent to clients when they ping the server
     *
     * @see #motdPictureImage() for the representing the image sent to clients
     */
    public File motdPicture() {
        return null;
    }

    /**
     * Gets the {@link java.awt.image.BufferedImage} that represents the Motd picture sent to clients
     *
     * @return the image sent to clients
     *
     * @see #motdPicture() for the file itself
     */
    public BufferedImage motdPictureImage() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Trident.config().getString("image-location", Defaults.MOTD_IMAGE_LOCATION)));
        } catch (IOException ex) {
            TridentLogger.error(ex);
        }
        return img;
    }

    public File motdImage() {
        return new File(Trident.config().getString("image-location", Defaults.MOTD_IMAGE_LOCATION));
    }

    /**
     * Sets the MOTD image sent to clients, may or may not take a server restart to take effect
     *
     * @param image the image to set it to
     *
     * @return 0 for success, -1 if this feature is disabled in config, -2 for generic failure
     */
    public int setMotdImage(Image image) {
        // TODO: implement
        return -1;
    }

    /**
     * Gets the maximum number of players allowed on the server
     *
     * @return the maximum number of players the server will allow
     */
    public int maxPlayers() {
        return Trident.config().getInt("max-players", Defaults.MAX_PLAYERS);
    }

    /**
     * Returns the number of players currently on the server
     *
     * @return a number representing the number of players on the server
     */
    public int playerCount() {
        return Factories.threads().players().size();
    }
}
