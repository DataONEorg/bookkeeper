package org.dataone.bookkeeper.helpers;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A delegate class with helper methods for all testing
 */
public class StoreHelper {

    /**
     * Generate a random Integer for use as test case identifiers
     * @return
     */
    public static Integer getRandomId() {
        int randomInt = ThreadLocalRandom.current().nextInt(1, 100000000);
        return new Integer(randomInt);
    }

}
