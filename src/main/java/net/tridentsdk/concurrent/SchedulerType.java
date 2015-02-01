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

package net.tridentsdk.concurrent;

/**
 * Type of task that will be run by the scheduler, how it will be run, when it will be scheduled
 *
 * @author The TridentSDK Team
 */
public enum SchedulerType {
    /**
     * Asynchronously runs the task the next tick
     */
    ASYNC_RUN,

    /**
     * Asynchronously runs the task later
     */
    ASYNC_LATER,

    /**
     * Asynchronously runs the task repeatedly, until stopped.
     */
    ASYNC_REPEAT,

    /**
     * Synchronously runs the task the next tick
     */
    SYNC_RUN,

    /**
     * Synchronously runs the task later
     */
    SYNC_LATER,

    /**
     * Synchronously runs the task repeatedly, until stopped.
     */
    SYNC_REPEAT
}
