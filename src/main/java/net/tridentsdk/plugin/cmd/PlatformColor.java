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

package net.tridentsdk.plugin.cmd;

/**
 * ANSI codes for coloring the console
 *
 * <p>Don't use this class if you need console colors. Use {@link net.tridentsdk.plugin.cmd.ServerConsole}</p>
 *
 * @author The TridentSDK Team
 */
public final class PlatformColor {
    /**
     * An empty string
     */
    public static final String EMPTY = "";
    private static final String ESC = String.valueOf((char) 0x1B);

    private PlatformColor() {
    }

    /**
     * Obtains the colors needed by name
     *
     * <p>This is <strong>not</strong> case-sensitive</p>
     *
     * <p>The list of colors which can be retrieved:
     * <ul>
     * <li>reset</li>
     * <li>black</li>
     * <li>red</li>
     * <li>green</li>
     * <li>yellow</li>
     * <li>purple</li>
     * <li>cyan</li>
     * <li>white</li>
     * <li>cursoreol2 - ANSI escape which moves the insertion cursor 2 spaces to the right</li>
     * </ul></p>
     *
     * @param color the name of the color to retrieve the escape for
     * @return the proper escape code or {@link net.tridentsdk.plugin.cmd.PlatformColor#EMPTY} if it does not exist, or
     * if the platform is windows, which does not support color codes or ANSI escapes
     */
    public static String forColor(String color) {
        String retournais;
        if (isWindows()) {
            retournais = EMPTY;
        } else {
            switch (color.toLowerCase()) {
                case "reset":
                    retournais = "\u001B[0m";
                    break;
                case "black":
                    retournais = "\u001B[30m";
                    break;
                case "red":
                    retournais = "\u001B[31m";
                    break;
                case "green":
                    retournais = "\u001B[32m";
                    break;
                case "yellow":
                    retournais = "\u001B[33m";
                    break;
                case "blue":
                    retournais = "\u001B[34m";
                    break;
                case "purple":
                    retournais = "\u001B[35m";
                    break;
                case "cyan":
                    retournais = "\u001B[36m";
                    break;
                case "white":
                    retournais = "\u001B[37m";
                    break;
                case "cursoreol2":
                    retournais = ESC + "[2C";
                    break;
                default:
                    retournais = EMPTY;
            }
        }

        return retournais;
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }
}
