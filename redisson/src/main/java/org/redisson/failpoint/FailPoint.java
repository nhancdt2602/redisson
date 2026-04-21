package org.redisson.failpoint;

public class FailPoint {
    private static volatile boolean enabled = false;
    public static void simulateDelay(long delayMillis) {
        if (!enabled) return;
        long start = System.currentTimeMillis();
        long now = System.currentTimeMillis();
        while (now - start < delayMillis) {
            now = System.currentTimeMillis();
        }
    }

    public static boolean enabled() {
        return enabled;
    }

    public static void setEnabled(boolean aenabled) {
        enabled = aenabled;
    }
}
