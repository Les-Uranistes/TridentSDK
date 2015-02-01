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

package net.tridentsdk.docs;

import java.lang.annotation.Documented;

/**
 * Marks a member of the API or implementation that is designed to be used internally only.
 * <p/>
 * <p>The behavior of callers on those members marked by this annotation is unspecified and left undocumented.</p>
 */
@Documented
public @interface InternalUseOnly {}
