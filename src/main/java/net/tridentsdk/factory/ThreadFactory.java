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

package net.tridentsdk.factory;

import net.tridentsdk.entity.Entity;
import net.tridentsdk.entity.living.Player;
import net.tridentsdk.world.World;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

/**
 * Creates threads and managers for those threads
 *
 * <p>These methods are primarily only supposed to be used by the server only. Because they are not volatile, they are
 * not marked with {@link net.tridentsdk.docs.InternalUseOnly}. However, you must be careful - data will not be wiped
 * unless you remove it yourself, which holds true until the server exits and shutsdown. Even calling the method
 * without
 * using the returned {@link net.tridentsdk.concurrent.TaskExecutor} will place an additional assignment inside the
 * backing executor.</p>
 *
 * @author The TridentSDK Team
 */
@ThreadSafe
public interface ThreadFactory {
    /**
     * Get all of the thread entity wrappers
     *
     * @return the values of the entity cache
     */
    Collection<Entity> entities();

    /**
     * Gets all of the thread player wrappers
     *
     * @return the values of the concurrent cache
     */
    Collection<Player> players();

    /**
     * Get all of the wrapped world threads
     *
     * @return the worlds being managed by the task executors
     */
    Collection<World> worlds();

    /**
     * A new concurren task executor reimplemented in the server
     *
     * @param threads the threads available in the pool
     * @param name    the names appended to the end of the thread name
     * @param <T>     the assignment type for each thread
     * @return the execution factory
     */
    <T> ExecutorFactory<T> executor(int threads, String name);
}
