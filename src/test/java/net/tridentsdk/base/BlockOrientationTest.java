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

package net.tridentsdk.base;

import net.tridentsdk.Coordinates;
import net.tridentsdk.util.Vector;
import org.junit.Assert;
import org.junit.Test;

public class BlockOrientationTest {

    @Test
    public void testGetDifference() throws Exception {
        Assert.assertEquals(BlockOrientation.NORTH.difference(), new Vector(0, 0, -1));
        Assert.assertEquals(BlockOrientation.SOUTH.difference(), new Vector(0, 0, 1));
        Assert.assertEquals(BlockOrientation.EAST.difference(), new Vector(1, 0, 0));
        Assert.assertEquals(BlockOrientation.WEST.difference(), new Vector(-1, 0, 0));
        Assert.assertEquals(BlockOrientation.NORTH_EAST.difference(), new Vector(1, 0, -1));
        Assert.assertEquals(BlockOrientation.NORTH_WEST.difference(), new Vector(-1, 0, -1));
        Assert.assertEquals(BlockOrientation.SOUTH_EAST.difference(), new Vector(1, 0, 1));
        Assert.assertEquals(BlockOrientation.SOUTH_WEST.difference(), new Vector(-1, 0, 1));
        Assert.assertEquals(BlockOrientation.TOP.difference(), new Vector(0, 1, 0));
        Assert.assertEquals(BlockOrientation.BOTTOM.difference(), new Vector(0, -1, 0));
        Assert.assertEquals(BlockOrientation.SELF.difference(), new Vector(0, 0, 0));
    }

    @Test
    public void testApply() throws Exception {
        Assert.assertEquals(BlockOrientation.NORTH.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 0, 0, -1));
        Assert.assertEquals(BlockOrientation.SOUTH.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 0, 0, 1));
        Assert.assertEquals(BlockOrientation.EAST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 1, 0, 0));
        Assert.assertEquals(BlockOrientation.WEST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, -1, 0, 0));
        Assert.assertEquals(BlockOrientation.NORTH_EAST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 1, 0, -1));
        Assert.assertEquals(BlockOrientation.NORTH_WEST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, -1, 0, -1));
        Assert.assertEquals(BlockOrientation.SOUTH_EAST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 1, 0, 1));
        Assert.assertEquals(BlockOrientation.SOUTH_WEST.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, -1, 0, 1));
        Assert.assertEquals(BlockOrientation.TOP.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 0, 1, 0));
        Assert.assertEquals(BlockOrientation.BOTTOM.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 0, -1, 0));
        Assert.assertEquals(BlockOrientation.SELF.apply(Coordinates.create(null, 0d, 0d, 0d)),
                Coordinates.create(null, 0, 0, 0));
    }
}