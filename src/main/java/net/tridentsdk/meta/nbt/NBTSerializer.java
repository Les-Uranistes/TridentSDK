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

package net.tridentsdk.meta.nbt;

import net.tridentsdk.perf.FastClass;
import net.tridentsdk.perf.FastField;
import net.tridentsdk.util.TridentLogger;

import java.lang.reflect.Field;

public final class NBTSerializer {

    private NBTSerializer() {}

    public static <T> T deserialize(Class<T> clzz, CompoundTag tag) {
        if (!(NBTSerializable.class.isAssignableFrom(clzz))) {
            TridentLogger.error(new IllegalArgumentException("Provided object is not serializable!"));
        }

        FastClass cls = FastClass.get(clzz);
        T instance = cls.constructor().newInstance();

        return deserialize(instance, tag);
    }

    // TODO fonction est plus compliqué (20+)
    public static <T> T deserialize(T instance, CompoundTag tag) {
        if (NBTSerializable.class.isAssignableFrom(instance.getClass())) {
            FastClass cls = FastClass.get(instance.getClass());

            for (FastField field : cls.fields()) {
                Field fld = field.toField();

                if (fld.isAnnotationPresent(NBTField.class)) {
                    String tagName = fld.getAnnotation(NBTField.class).name();
                    TagType type = fld.getAnnotation(NBTField.class).type();

                    NBTTag value = tag.containsTag(tagName) ? tag.getTag(tagName) : new NullTag(tagName);

                    if (value.type() == type) {
                        switch (value.type()) {
                            case BYTE:
                                field.set(instance, value.asType(ByteTag.class).value());
                                break;

                            case BYTE_ARRAY:
                                field.set(instance, value.asType(ByteArrayTag.class).value());
                                break;

                            case COMPOUND:
                                if (NBTSerializable.class.isAssignableFrom(field.toField().getType())) {
                                    field.set(instance, deserialize(field.toField().getType(), value.asType(CompoundTag.class)));
                                } else {
                                    field.set(instance, value);
                                }

                                break;

                            case DOUBLE:
                                field.set(instance, value.asType(DoubleTag.class).value());
                                break;

                            case FLOAT:
                                field.set(instance, value.asType(FloatTag.class).value());
                                break;

                            case INT:
                                field.set(instance, value.asType(IntTag.class).value());
                                break;

                            case INT_ARRAY:
                                field.set(instance, value.asType(IntArrayTag.class).value());
                                break;

                            case LONG:
                                field.set(instance, value.asType(LongTag.class).value());
                                break;

                            case SHORT:
                                field.set(instance, value.asType(ShortTag.class).value());
                                break;

                            case LIST:
                                field.set(instance, value.asType(ListTag.class));
                                break;

                            case STRING:
                                field.set(instance, value.asType(StringTag.class).value());
                                break;

                            case NULL:
                                field.set(instance, null);
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        } else {
            TridentLogger.error(new IllegalArgumentException("Provided object is not serializable!"));
        }

        return instance;
    }

    public static CompoundTag serialize(NBTSerializable serializable) {
        return serialize(serializable, serializable.getClass().getSimpleName());
    }

    // TODO fonction is plus compliqué (16+)
    public static CompoundTag serialize(NBTSerializable serializable, String name) {
        FastClass cls = FastClass.get(serializable.getClass());
        CompoundTagBuilder<NBTBuilder> builder = NBTBuilder.newBase(name);

        for (FastField field : cls.fields()) {
            Field fld = field.toField();

            if (fld.isAnnotationPresent(NBTField.class)) {
                String tagName = fld.getAnnotation(NBTField.class).name();
                TagType tagType = fld.getAnnotation(NBTField.class).type();
                Object value = field.get(serializable);

                switch (tagType) {
                    case BYTE:
                        builder.byteTag(tagName, (byte) value);
                        break;

                    case BYTE_ARRAY:
                        builder.byteArrayTag(tagName, (byte[]) value);
                        break;

                    case COMPOUND:
                        builder.compoundTag((CompoundTag) value);
                        break;

                    case DOUBLE:
                        builder.doubleTag(tagName, (double) value);
                        break;

                    case FLOAT:
                        builder.floatTag(tagName, (float) value);
                        break;

                    case INT:
                        builder.intTag(tagName, (int) value);
                        break;

                    case INT_ARRAY:
                        builder.intArrayTag(tagName, (int[]) value);
                        break;

                    case LONG:
                        builder.longTag(tagName, (long) value);
                        break;

                    case SHORT:
                        builder.shortTag(tagName, (short) value);
                        break;

                    case LIST:
                        builder.listTag((ListTag) value);
                        break;

                    case STRING:
                        builder.stringTag(tagName, (String) value);
                        break;

                    case NULL:
                        builder.nullTag(tagName);
                        break;

                    default:
                        break;
                }
            }

        }

        return builder.endCompoundTag().build();
    }
}
