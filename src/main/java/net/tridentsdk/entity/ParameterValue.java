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

package net.tridentsdk.entity;

/**
 * Immutable parameter type and object value for dynamic constructor resolvation
 *
 * @param <T> the type for the parameter
 * @author The TridentSDK Team
 */
public class ParameterValue<T> {
    private final Class<T> clazz;
    private final T value;

    private ParameterValue(Class<T> clazz, T value) {
        this.clazz = clazz;
        this.value = value;
    }

    /**
     * Creates a new parameter value
     *
     * @param clazz the class type
     * @param value the value of the parameter
     * @param <T>   the type
     * @return the new parameter value
     */
    public static <T> ParameterValue from(Class<T> clazz, T value) {
        return new ParameterValue<>(clazz, value);
    }

    /**
     * The class type for this parameter
     *
     * @return the parameter class type
     */
    public Class<T> clazz() {
        return this.clazz;
    }

    /**
     * The argument to be passed in for the parameter
     *
     * @return the value passed for the parameter
     */
    public T value() {
        return this.value;
    }

    @Override
    public String toString() {
        return "ParameterValue{" +
                "class=" + clazz +
                ", value=" + value +
                '}';
    }
}
