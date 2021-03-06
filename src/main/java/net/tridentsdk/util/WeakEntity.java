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

package net.tridentsdk.util;

import com.google.common.collect.Sets;
import net.tridentsdk.Trident;
import net.tridentsdk.docs.InternalUseOnly;
import net.tridentsdk.entity.Entity;
import net.tridentsdk.entity.decorate.DecorationAdapter;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A reference to an entity
 * <p/>
 * <p>Three constructors are provided. In the case that the stored value comes from an <em>alien-source</em>, or any
 * general situation where the value to be held by the WeakEntity is unknown and unspecified, the recommended factory
 * method to use is {@link #orEmpty(net.tridentsdk.entity.Entity)}. This prevents the creation of unnecessary instances
 * of WeakEntity where the value is always going to {@code null}. Because this method always returns the same instance
 * of a {@code null} holding WeakEntity, registration does not need to be executed, where registration means to place
 * the reference into a queue which will be cleared once the entity has been removed. In this context, and alien method
 * is: <em>"A method you call for which you have no control over the code, and further don’t even know what the code
 * does, other than the method signature</em> [...] <em>it could refer to calls to third party libraries</em>."</p>
 * <p/>
 * <p>Because entities are loaded with all of their data and AI, the removal of them entirely depends on the references
 * left held to it. If the entity is technically removed by the server, but still held by a plugin, for example, then
 * a memory leak will occur because the entity will never actually be removed. It is not updated for players, but the
 * reference stays in memory, wasting computing power on a non-existent entity.</p>
 * <p/>
 * <p>Use this class to store a link or bridge to the reference of an entity. The storage of an instance of this class
 * will only hold this class only, and the reference to the entity in this class may become {@code null} at any time,
 * in which case is indicated by {@link #isNull()} returning {@code true}.</p>
 * <p/>
 * <p>Much thought was put into the design of this class, especially if it should be made thread-safe. Because this is
 * an internal change, it can be easily done without breaking the API. However, the decision was made to make this
 * class
 * thread-safe in order to preserve the fact that both the users of this API, including plugins and the server itself
 * is concurrent. It would decrease the burden to handle this safely using the internal implementation rather than
 * having the client code execute awkward constructs in order to keep consistency. This is OK for the implementation to
 * perform, but because plugin authors may not always think about the concurrent nature of this API, it was done at the
 * cost of performance to reduce bugs which are often difficult to find.</p>
 * <p/>
 * <p>This is the equivalent of {@link com.google.common.base.Optional} but for entities specifically, much like an
 * optional which stores a {@link java.lang.ref.WeakReference} of the specified property. While this class does use
 * WeakReference to manage the references, other methods to safeguard the reference is also implemented, such as
 * removal
 * listeners.</p>
 * <p/>
 * <p>Internally, this class manages a queue of references which, in turn, are polled by a thread called
 * {@code Trident - Reference Handler}. Every call to clear a specific reference also runs the mark/sweep collector,
 * although it is invoked directly rather than using {@link #runMarkSweep()}. The implementation of the collector may
 * be
 * found in the documentation for {@link  #runMarkSweep()}. Entities call
 * {@link #clearReferencesTo(net.tridentsdk.entity.Entity)} when they are removed to purge old references to that
 * entity
 * after it has finished despawning. This method also runs the mark/sweep collector. This is only implemented as a
 * safeguard behind {@link java.lang.ref.WeakReference}, which is used to store the actual instance of the entity to be
 * weakly referenced. The reference handling thread cleans up the entity after all the references have been cleared, as
 * well as collecting any other obsolete references. This was done because WeakReference may allow the reference to be
 * held longer than it has lived, in the case that the GC does not run, the reference handler will perform the
 * necessary actions to make sure the entity is correctly nulled.</p>
 * <p/>
 * <p>Here are examples on the usage of this class.</p>
 * <p/>
 * <p>WeakEntity can and should be used when storing entities, such as players inside a collection or reference.</p>
 * <pre><code>
 *     private final Map&lt;WeakEntity&lt;Player&gt;, Integer&gt; score =
 *         Maps.newHashMap();
 * <p/>
 *     // Add player
 *     Player player = ...
 *     int playerScore = ...
 *     score.put(WeakEntity.of(player), playerScore);
 * <p/>
 *     // Checking player
 *     for (WeakEntity&lt;Player&gt; weakEntity : score.keySet()) {
 *         // This is simply proof of concept, don't check if it is null
 *         // then try to obtain it
 *         // Or try to hide the player from himself/herself
 *         if (weakEntity.isNull())
 *             continue;
 *         weakEntity.obtain().hide(weakEntity);
 *     }
 * </code></pre>
 * <p/>
 * <p>WeakEntity acts like an optional which can be used to eliminate the need to check for {@code null}.</p>
 * <pre><code>
 *     // Finding the player
 *     // Don't do this in production-stage code, it doesn't work the way the name implies!
 *     public WeakEntity&lt;Bat&gt; findClosest(Player player, int range) {
 *         for (Entity entity : player.withinRange(range)) {
 *             if (entity instanceof Bat) {
 *                 // Unable to be null, so we use of(Entity)
 *                 // Instead of orEmpty(Entity)
 *                 return WeakEntity.of(entity);
 *             }
 *         }
 * <p/>
 *         // No bats near the player, it's empty
 *         return WeakEntity.empty();
 *     }
 * <p/>
 *     // Find a Bat closest to the player, or spawn a new one, and accelerate it upwards (just because)
 *     findClosest(player, 69)
 *         .or(EntityBuilder.create().build(TridentBat.class))
 *         .setVelocity(new Vector(0, 10, 0));
 * </code></pre>
 * <p/>
 * <p>WeakEntity does not need to be instantiated to find in a collection.</p>
 * <pre><code>
 *     Map&lt;WeakEntity&lt;Entity&gt;, Object&gt; map = Maps.newHashMap();
 *     ...
 *     Object o = map.get(WeakEntity.finderOf(entity));
 * </code></pre>
 * <p/>
 * <p>Finally, WeakEntity can be used to find and prevent bugs which would otherwise cause NullPointerExceptions.</p>
 * <pre><code>
 *     // Using it to find bugs
 *     // Oh, leave it here for a while until I implement a different method, then I'll come back to it
 *     WeakEntity&lt;Player&gt; weakEntity = WeakEntity.of(null);
 *     ...
 *     // I was smart and used obtain() instead of entity()
 *     weakEntity.obtain().hide();
 * <p/>
 *     // Code errors
 * <p/>
 *     // Go back to the line you found in the stacktrace
 *     // Looks like it was on the line that tried to obtain the entity
 *     // from a WeakEntity
 *     // We remember that we implemented a find player method later on
 * <p/>
 *     // Final code
 *     WeakEntity&lt;Player&gt; weakEntity = WeakEntity.of(player());
 * </code></pre>
 *
 * @author The TridentSDK Team
 */
@ThreadSafe
public final class WeakEntity<T extends Entity> {

    private static final WeakEntity<?> NULL_ENTITY = new WeakEntity<>(new SafeReference<Entity>(null));
    private static final CleaningRefCollection REFERENCE_QUEUE = CleaningRefCollection.newQueue();

    // Not automatically instantiated by the server, won't be started if it is not needed
    static {
        Thread thread = new Thread(REFERENCE_QUEUE, "Trident - Reference Handler");
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private final SafeReference<T> referencedEntity;
    private volatile Object finder;

    private WeakEntity(SafeReference<T> referencedEntity) {
        this.referencedEntity = referencedEntity;
    }

    /**
     * Obtains a WeakEntity which is either a {@link #empty()} reference or a reference
     * {@link #of(net.tridentsdk.entity.Entity)}, depending on if the specified entity is {@code null} or
     * non-{@code null}, respectively
     * <p/>
     * <p>If the returned WeakEntity holds {@code null}, and is equivalent to {@link #empty()}, then no instance of
     * WeakEntity was created.</p>
     * <p/>
     * <p>If the referenced entity is <em>known</em> to be {@code null}, then do not use this method. Although it is
     * still correct, if it is not possible for it to be {@code null}, use an {@link #empty()} WeakEntity.</p>
     *
     * @param referencedEntity the entity to obtain the reference for
     * @param <T>              the type of entity to hold
     *
     * @return a WeakEntity which holds the specified entity, or {@link #empty()} if the specified entity to reference
     * is {@code null}
     */
    public static <T extends Entity> WeakEntity<T> orEmpty(T referencedEntity) {
        WeakEntity<T> retournais;

        if (referencedEntity == null) {
            retournais = empty();
        } else {
            retournais = of(referencedEntity);
        }

        return retournais;
    }

    /**
     * Obtains a WeakEntity which holds a {@code null} referenced entity
     * <p/>
     * <p>This method does not produce a new object. The instance of an empty WeakEntity is held in a {@code static}
     * field inside this class.</p>
     *
     * @param <T> the type of entity to hold {@code null} for
     *
     * @return a WeakEntity which holds {@code null}
     */
    public static <T extends Entity> WeakEntity<T> empty() {
        return (WeakEntity<T>) NULL_ENTITY;
    }

    /**
     * Obtains a WeakEntity which holds the specified entity
     * <p/>
     * <p>The entity which is specified will become {@code null} when it is dereferenced from the server.</p>
     * <p/>
     * <p>This may be used in the place of {@link #orEmpty(net.tridentsdk.entity.Entity)} if creation of a new
     * WeakEntity for a {@code null} entity is OK. In other words, this is a fast-path (albeit the fact that the
     * difference in performance is negligible in comparison with creation overhead and memory) for the
     * {@link #orEmpty(net.tridentsdk.entity.Entity)}.</p>
     *
     * @param referencedEntity the entity to hold until it is removed
     * @param <T>              the type of entity to hold
     *
     * @return a WeakEntity which holds the entity until it becomes {@code null}
     */
    public static <T extends Entity> WeakEntity<T> of(T referencedEntity) {
        WeakEntity<T> weakEntity = new WeakEntity<>(new SafeReference<>(referencedEntity));
        REFERENCE_QUEUE.put(weakEntity);
        return weakEntity;
    }

    /**
     * Removes the references of the entity from any WeakEntity instances which are non-null
     * <p/>
     * <p>This method was meant to be used only for the implementation. The calling of this method by a non-Trident
     * class has no effect.</p>
     *
     * @param entity the entity to dereference and clear from WeakEntity
     *
     * @throws IllegalAccessException when the caller is not Trident
     */
    @InternalUseOnly
    public static void clearReferencesTo(Entity entity) throws IllegalAccessException {
        if (!Trident.isTrident()) {
            // TODO déplacer avec un réalisation du SecurityManager
            throw new IllegalAccessException("WeakEntities may only be cleared by a Trident class");
        }
        REFERENCE_QUEUE.clearReference(entity);
    }

    /**
     * Provides an object which possesses the matching properties for a WeakEntity
     * <p/>
     * <p>This is needed when obtaining a WeakEntity from a collection. The returned object overrides equals and
     * hashCode, as well as toString to mimic the actual referenced entity. If the reference is {@code null}, then no
     * new object is created and the method returns a cached version of the object. Because the WeakEntity's original
     * implementation also delegates the same identifying functions to the reference, this is the preferred way to find
     * a WeakEntity from a collection or to match with a stored version.</p>
     * <p/>
     * <p>This method current does not work.</p>
     *
     * @param entity the entity to get the finder for
     *
     * @return an object that possesses the properties of the entity so that they match up with an WeakEntity containing
     * the same entity
     */
    public static Object finderOf(Entity entity) {
        Object retournais;
        if (entity == null) {
            retournais = RefList.NULL;
        } else {
            retournais = REFERENCE_QUEUE.finderOf(entity);
        }
        return retournais;
    }

    /**
     * Forces the reference handler thread to run the {@code null} or garbage reference collection cycle and reclaim
     * memory lost by those references
     * <p/>
     * <p>This runs the mark sweeping strategy employed by the reference handler. Previous references are swept from
     * the session and the thread continues to mark the {@code null} references. They are placed into a collection
     * until the sweeping completes. When this step does complete, the collection is checked, then the references are
     * purged from the reference queue.</p>
     * <p/>
     * <p>Unlike Java's default GC implementation, this method is strongly bound. This always succeeds in running the
     * collection cycle.</p>
     */
    public static void runMarkSweep() {
        REFERENCE_QUEUE.beginSweep();
    }

    /**
     * Obtains the instance of the referenced entity, or, if it {@link #isNull()}, then this method will return the
     * specified fallback object, which must match the type of this object
     * <p/>
     * <p>The provided fallback should not be {@code null}. This is allowed, and is still correct, however, a more
     * effective strategy is to use {@link #entity()} instead.</p>
     *
     * @param fallback the entity to return in place of the reference in the case that the reference is {@code null}
     *
     * @return the entity if non-{@code null}, or the fallback if it is
     */
    public T or(T fallback) {
        T ref = this.referencedEntity.get();
        return (ref == null) ? fallback : ref;
    }    /**
     * Observes the reference to see if the entity is {@code null} or not accessible via the server anymore
     *
     * @return {@code true} to indicate the entity is removed and cannot be further referenced, in which case the
     * instance of this object should also become {@code null}
     */
    public boolean isNull() {
        return entity() == null;
    }

    /**
     * Obtains the referenced entity, but is fail-fast
     * <p/>
     * <p>This method will throw an {@link java.lang.IllegalStateException} if the referenced entity is {@code null}.
     * Usually, this should be used for debugging purposes, but can be used as a marker in the case that something
     * within the code goes wrong.</p>
     *
     * @return the referenced entity, can <strong>never</strong> be {@code null}
     */
    public T obtain() {
        T ref = this.referencedEntity.get();
        if (ref == null) {
            throw new IllegalStateException("Obtained entity is null");
        }
        return ref;
    }

    /**
     * This removes the reference to the entity in this particular instance of WeakEntity
     * <p/>
     * <p>If the referenced entity is {@code null} already, no changes will take effect. The call of this method also
     * has <em>happens-before</em> consistency to any subsequent inspection calls to this WeakEntity.</p>
     */
    public void clear() {
        this.referencedEntity.clear();
    }    /**
     * Obtains the instance of the referenced entity, or {@code null} if it has been dereferenced
     *
     * @return the entity which this reference points to, or {@code null} if the entity no longer exists on the server
     */
    public T entity() {
        return this.referencedEntity.get();
    }

    /**
     * Instance method of {@link #finderOf(net.tridentsdk.entity.Entity)}. See that method for the documentation and
     * implementation.
     * <p/>
     * <p>This method still internally polls the reference queue for the entity's cached finder.</p>
     *
     * @return an Object which can be used to find WeakEntities in a collection
     */
    public Object finder() {
        return finder;
    }

    // Thread-visible version of WeakReference
    private static class SafeReference<T> extends WeakReference<T> {

        private volatile int fence = 0;

        SafeReference(T referent) {
            super(referent);
        }

        @Override
        public T get() {
            return super.get();
        }

        @Override
        public void clear() {
            super.clear();
            fence = 0;
        }

        @Override
        public String toString() {
            return "SafeReference{" +
                    "fence=" + fence +
                    '}';
        }
    }

    // Stores references and collects/cleans up null references
    private static class CleaningRefCollection implements Runnable {

        // Locking mechanisms
        private final Lock lock = new ReentrantLock();
        private final Condition hasNodes = lock.newCondition();
        // INVARIANT: MODIFIED THREAD-LOCALLY ONLY
        private final Set<WeakEntity> marked = Sets.newHashSet();
        @GuardedBy("lock")
        private RefEntry[] entries = new RefEntry[16];
        @GuardedBy("lock")
        private int length = 0;

        public static CleaningRefCollection newQueue() {
            return new CleaningRefCollection();
        }

        public void put(WeakEntity<?> weakEntity) {
            RefList node = RefList.newNode(weakEntity, get(weakEntity.or(null)));
            add(node.finder(), node);
        }

        private RefList get(Object key) {
            lock.lock();
            try {
                int hash = hash(key);
                RefEntry node = this.loop(key, this.entries[hash]);
                return (node == null) ? null : node.val();
            } finally {
                lock.unlock();
            }
        }

        // Collection ops
        private void add(Object key, RefList value) {
            lock.lock();
            try {
                int hash = hash(key);
                RefEntry toSet = this.search(key, value, hash);
                toSet.setHash(hash);
                toSet.setVal(value);
            } finally {
                lock.unlock();
            }
        }

        private int hash(Object key) {
            long h = (long) key.hashCode();
            h = ((h >> 16) ^ h) * 0x33L;
            h = ((h >> 16) ^ h) * 0x33L;
            h = (h >> 16) ^ h;

            return (int) (h & ((long) entries.length - 1));
        }

        private RefEntry loop(Object key, RefEntry head) {
            RefEntry tail = head;
            RefEntry retournais = null;
            while (tail != null) {
                if (tail.key().equals(key)) {
                    retournais = tail;
                    break;
                }
                tail = tail.next();
            }

            return retournais;
        }

        private RefEntry search(Object k, RefList v, int hash) {
            RefEntry head = this.entries[hash];
            RefEntry tail = this.loop(k, head);
            return (tail != null) ? tail : this.create(k, null, v, hash);
        }

        private RefEntry create(Object k, RefEntry previous, RefList v, int hash) {
            RefEntry node = new RefEntry(k, v, hash);
            RefEntry retournais;

            if (previous == null) {
                this.entries[hash] = node;
                this.length++;
                this.resize();
                retournais = node;
            } else {
                previous.setNext(node);
                this.length++;
                this.resize();
                retournais = node;
            }

            return retournais;
        }

        private void resize() {
            lock.lock();
            try {
                if (this.length > (entries.length - 2)) {
                    int newLen = entries.length * 2;

                    RefEntry[] resize = new RefEntry[newLen];
                    System.arraycopy(entries, 0, resize, 0, length);
                    this.entries = resize;
                }
            } finally {
                lock.unlock();
            }
        }

        // Clears references to the entity, forcing the sweep to run
        // this leaves the empty node to be collected by the sweeper
        public void clearReference(Entity entity) {
            RefList entities = get(entity);

            if (entities != null) {
                for (WeakEntity<?> e : entities) {
                    e.clear();
                }

                // Clear unused reference
                beginSweep();
            }
        }

        public void beginSweep() {
            lock.lock();
            try {
                hasNodes.signal();
            } finally {
                lock.unlock();
            }
        }

        public Object finderOf(Entity entity) {
            RefList node = get(entity);
            return (node == null) ? RefList.NULL : node.finder();
        }

        @Override
        public void run() {
            //noinspection InfiniteLoopStatement
            while (true) {
                // Step 1: find nodes
                Set<RefEntry> localEntries = entries();

                // Try to make the collector run
                // This might help stored references be checked
                // In case the GC doesn't run, who cares anyways
                System.gc(); // TODO Tres mal!

                // Step 2: mark
                for (RefEntry entry : localEntries) {
                    Set<WeakEntity> weakRefs = entry.val().refs();

                    for (WeakEntity ref : weakRefs) {
                        if (ref.isNull()) {
                            marked.add(ref);
                        }
                    }
                }

                try {
                    lock.lockInterruptibly();
                    while (marked.isEmpty()) {
                        hasNodes.await();
                    }

                    // Step 3: sweep
                    for (RefEntry entry : localEntries) {
                        Set<WeakEntity> refs = entry.val().refs();
                        refs.removeAll(marked);

                        if (refs.isEmpty()) {
                            remove(entry.val());
                        }
                    }

                    // Remove marked entries
                    marked.clear();
                } catch (InterruptedException ignored) {
                    // Run by a daemon thread, doesn't matter if it was interrupted
                } finally {
                    lock.unlock();
                }
            }
        }

        private Set<RefEntry> entries() {
            Set<RefEntry> set = Sets.newHashSet();
            for (RefEntry entry : entries) {
                if (entry != null) {
                    set.add(entry);
                }
            }
            return set;
        }

        private void remove(Object key) {
            try {
                RefEntry head = this.entries[hash(key)];
                if (head != null) {
                    this.remove(key, head);
                }
            } finally {
                lock.unlock();
            }
        }

        private void remove(Object k, RefEntry head) {
            RefEntry leading = head;
            RefEntry tail = (leading == null) ? null : leading.next();

            while (tail != null) {
                if (tail.key().equals(k)) {
                    leading.setNext(tail.next());
                    this.length--;
                } else {
                    tail = tail.next();
                    leading = leading.next();
                }
            }

            if ((leading != null) && leading.key().equals(k)) {
                this.entries[leading.hash()] = null;
            }

            this.length--;
        }

        @Override
        public String toString() {
            return "CleaningRefCollection{" +
                    "entries=" + Arrays.toString(entries) +
                    ", length=" + length +
                    ", lock=" + lock +
                    ", hasNodes=" + hasNodes +
                    ", marked=" + marked +
                    '}';
        }
    }

    private static final class RefEntry {

        private final Object key;

        @GuardedBy("RefList.lock")
        private RefList list;
        @GuardedBy("RefList.lock")
        private int hash;
        @GuardedBy("RefList.lock")
        private RefEntry next = null;

        private RefEntry(Object key, RefList entity, int hash) {
            this.key = key;
            this.list = entity;
            this.hash = hash;
        }

        public static RefEntry newEntry(Object key, RefList entity, int hash) {
            return new RefEntry(key, entity, hash);
        }

        public Object key() {
            return this.key;
        }

        public RefList val() {
            return this.list;
        }

        public void setVal(RefList list) {
            this.list = list;
        }

        public int hash() {
            return this.hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public RefEntry next() {
            return this.next;
        }

        public void setNext(RefEntry next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "RefEntry{" +
                    "key=" + key +
                    ", list=" + list +
                    ", hash=" + hash +
                    ", next=" + next +
                    '}';
        }
    }    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return (obj == null) ? isNull() : obj.equals(referencedEntity.get());
    }

    // A set of references assigned to a particular entity
    private static final class RefList implements Iterable<WeakEntity> {

        private static final Entity NULL = new DecorationAdapter<Entity>(null) {
            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return obj == null;
            }
        };

        @GuardedBy("lock")
        private final Set<WeakEntity> weakEntities;
        private final Object finder;
        private final Object lock = new Object();

        private RefList(final WeakEntity entity, RefList node) {
            entity.finder = new Object() {
                @Override
                public int hashCode() {
                    return entity.hashCode();
                } // TODO CQD?

                @Override
                public boolean equals(Object obj) {
                    return entity.equals(obj);
                }
            };
            this.finder = entity.finder;

            Set<WeakEntity> original;
            if (node == null) {
                original = Sets.newHashSet();
            } else {
                original = node.weakEntities;
            }

            if (original == null) {
                original = Sets.newHashSet();
            }
            original.add(entity);

            weakEntities = original;
        }

        public static RefList newNode(WeakEntity entity, RefList node) {
            return new RefList(entity, node);
        }

        // Removes the references which are empty
        public void clean() throws InterruptedException {
            synchronized (lock) {
                for (WeakEntity entity : weakEntities) {
                    if (entity.isNull()) {
                        weakEntities.remove(entity);
                    }
                }
            }
        }

        // Removes a specified entity
        public void clear(WeakEntity entity) {
            synchronized (lock) {
                weakEntities.remove(entity);
            }
        }

        public Set<WeakEntity> refs() {
            synchronized (lock) {
                return weakEntities;
            }
        }

        @Override
        public Iterator<WeakEntity> iterator() {
            synchronized (lock) {
                return weakEntities.iterator();
            }
        }

        // Used to reference the entity without actually using it
        public Object finder() {
            return this.finder;
        }

        @Override
        public String toString() {
            return "RefList{" +
                    "weakEntities=" + weakEntities +
                    ", finder=" + finder +
                    ", lock=" + lock +
                    '}';
        }
    }    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return isNull() ? 0 : referencedEntity.get().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getName() + "{referencedEntity = " + referencedEntity.get() + "}@" + hashCode();
    }








}
