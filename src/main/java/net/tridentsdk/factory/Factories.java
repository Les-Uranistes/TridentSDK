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

import net.tridentsdk.concurrent.HeldValueLatch;
import net.tridentsdk.docs.InternalUseOnly;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Available creational factories for basic objects
 * <p/>
 * <p>If the factories are accessed before initialization, the caller thread will block</p>
 * <p/>
 * <p>This is managed by using a {@link net.tridentsdk.concurrent.HeldValueLatch}, instead of a single latch. If a
 * class
 * needs to be initialized that uses another factory, it will never occur because the thread is setting a value after
 * the previous initialization is blocked because it was not fully initialized.</p>
 *
 * @author The TridentSDK Team
 */
@ThreadSafe
public final class Factories {

    private static final HeldValueLatch<TaskFactory> taskFactory = HeldValueLatch.create();
    private static final HeldValueLatch<ThreadFactory> threadFactory = HeldValueLatch.create();
    private static final HeldValueLatch<CollectFactory> collectFactory = HeldValueLatch.create();
    private static final HeldValueLatch<GenFactory> genFactory = HeldValueLatch.create();

    private static final ConfigFactory configFactory = new ConfigFactory();
    private static final ReflectFactory reflectionFactory = new ReflectFactory();

    private Factories() {
    }

    @InternalUseOnly
    public static void init(TaskFactory factory) {
        taskFactory.countDown(factory);
    }

    @InternalUseOnly
    public static void init(ThreadFactory factory) {
        threadFactory.countDown(factory);
    }

    @InternalUseOnly
    public static void init(CollectFactory factory) {
        collectFactory.countDown(factory);
    }

    @InternalUseOnly
    public static void init(GenFactory factory) {
        genFactory.countDown(factory);
    }

    /**
     * Schedulers and task executors
     *
     * @return the task factory
     */
    public static TaskFactory tasks() {
        TaskFactory retournais;
        try {
            retournais = taskFactory.await();
        } catch (InterruptedException e) {
            // Release up the stack
            Thread.currentThread().interrupt();
            retournais = null;
        }
        return retournais;
    }

    /**
     * Thread management, thread creators/assignment
     *
     * @return the threads factory
     */
    public static ThreadFactory threads() {
        ThreadFactory retournais;
        try {
            retournais = threadFactory.await();
        } catch (InterruptedException e) {
            // Release up the stack
            Thread.currentThread().interrupt();
            retournais = null;
        }
        return retournais;
    }

    /**
     * Reflection shortcuts and fast wrappers
     *
     * @return the reflection factory
     */
    public static ReflectFactory reflect() {
        return reflectionFactory;
    }

    /**
     * Reimplemented or new backed collections
     *
     * @return the collection factory
     */
    public static CollectFactory collect() {
        CollectFactory retournais;
        try {
            retournais = collectFactory.await();
        } catch (InterruptedException e) {
            // Release up the stack
            Thread.currentThread().interrupt();
            retournais = null;
        }
        return retournais;
    }

    /**
     * Obtains world generation utilities
     *
     * @return the world generation factory
     */
    public static GenFactory gen() {
        GenFactory retournais;
        try {
            retournais = genFactory.await();
        } catch (InterruptedException e) {
            // Release up the stack
            Thread.currentThread().interrupt();
            retournais = null;
        }
        return retournais;
    }

    /**
     * Deals with configurations and files
     *
     * @return the configuration factory
     */
    public static ConfigFactory configs() {
        return configFactory;
    }
}
