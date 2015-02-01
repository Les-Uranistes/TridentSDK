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

package net.tridentsdk.event.weather;

import org.junit.Assert;
import org.junit.Test;

public class WeatherEventTest {

    @Test
    public void testGetWorld() throws Exception {
        WeatherEvent event = new WeatherEvent(null);
        Assert.assertEquals(event.world(), null);
    }

    @Test
    public void testIsIgnored() throws Exception {
        WeatherEvent event = new WeatherEvent(null);
        Assert.assertEquals(event.isIgnored(), false);

        event.cancel(true);
        Assert.assertEquals(event.isIgnored(), true);
    }

    @Test
    public void testCancel() throws Exception {
        new WeatherEvent(null).cancel(true);
        new WeatherEvent(null).cancel(false);
    }
}