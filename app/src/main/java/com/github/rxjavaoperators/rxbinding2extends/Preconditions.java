package com.github.rxjavaoperators.rxbinding2extends;

/**
 * Date: 24.02.2017
 * Time: 19:45
 *
 * @author Aleks Sander
 *         Project RxJavaOperators
 */

public class Preconditions {
    public static void checkArgument(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    private Preconditions() {
        throw new AssertionError("No instances.");
    }
}
