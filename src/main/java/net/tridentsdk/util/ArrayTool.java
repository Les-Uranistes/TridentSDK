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

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;

import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Tools for modifying elements of an array
 *
 * WARNING: Potentially slow blocking method calls
 *
 * @param <T> the type held by the array to be converted
 */
@ThreadSafe
public final class ArrayTool<T> {
    private final T[] array;
    private final TypeToken<T> typeToken = new TypeToken<T>() {
    };

    private ArrayTool(T[] array) {
        this.array = array;
    }

    /**
     * Creates a new array tool
     *
     * @param array the underlying array
     * @param <T>   the type of the array
     * @return the new array tool
     */
    public static <T> ArrayTool<T> using(T[] array) {
        return new ArrayTool<>(array);
    }

    /**
     * Converts the underlying array to an array of the type specified
     *
     * @param clazz class type of array elements, implicitly sets generic method type parameter C
     * @param <C>   generic type of array elements
     * @return the new array converted from the underlying array
     * @throws java.lang.ClassCastException if the type of the underlying array cannot be cast to the new type
     */
    public <C> C[] convertTo(Class<C> clazz) {
        //noinspection unchecked
        C[] cs = (C[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < array.length; i++) {
            cs[i] = (C) array[i];
        }

        return cs;
    }

    /**
     * Creates a deep copy of an array, assuming that its {@link Object#clone()} method returns a deep copy
     *
     * @return the deep copy of the underlying array
     */
    public T[] cloneArray(Function<T, T> cloner) {
        //noinspection unchecked
        T[] ts = (T[]) Array.newInstance(typeToken.getRawType(), array.length);
        for (int i = 0; i < array.length; i++) {
            ts[i] = cloner.apply(array[i]);
        }

        return ts;
    }

    /**
     * Gets the array of this tool
     *
     * @return the array passed in during instantiation
     */
    public T[] underlyingArray() {
        return this.array;
    }

    @Override
    public String toString() {
        return "ArrayTool{" +
                "array=" + Arrays.toString(array) +
                ", typeToken=" + typeToken +
                '}';
    }
}
