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

import net.tridentsdk.Messagable;

/**
 * A server entity which can execute commands, which can be players or the console
 *
 * @author The TridentSDK Team
 */
public interface CommandIssuer extends Messagable {

    /**
     * Issues a command from this sender, forcing them to run it
     */
    void invokeCommand(String message);

    /**
     * Gets the last command executed by this CommandIssuer, without the preceding "/"
     *
     * @return the last command executed by commandissuer
     */
    String lastCommand();
}
