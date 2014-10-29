package org.myLazyClock.travelApi;

import org.junit.Assert;
import org.junit.Test;

public class TravelManualStrategyTest {

    @Test
    public void testFactory() throws Exception {

        TravelFactory factory = TravelFactory.getInstance();

        TravelStrategy strategy = factory.get(TravelManualStrategy.ID);

        Assert.assertNotNull(strategy);

        Assert.assertTrue(strategy instanceof TravelManualStrategy);

    }
}