package org.myLazyClock.travelApi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TravelFactoryTest {

    private TravelFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = TravelFactory.getInstance();

    }

    @Test(expected = NullPointerException.class)
    public void testGetTravelStrategy() {
        TravelStrategy strategy = factory.get(-1);
        Assert.assertNull(strategy);
    }
}