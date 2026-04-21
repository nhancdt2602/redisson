package org.redisson;


import org.junit.jupiter.api.Test;
import org.redisson.api.RRemoteService;
import org.redisson.api.RemoteInvocationOptions;
import org.redisson.failpoint.FailPoint;

import java.util.concurrent.TimeUnit;

public class RedissonRemoteServiceFailPointTest extends RedisDockerTest {
    @Test
    public void testRetryRaceCondition() {
        FailPoint.setEnabled(true);
        try {
            RRemoteService serverRemote = redisson.getRemoteService("demo");
            serverRemote.register(RemoteInterface.class, new RemoteImpl());

            RRemoteService clientRemote = redisson.getRemoteService("demo");

            RemoteInvocationOptions opts = RemoteInvocationOptions.defaults()
                    .expectAckWithin(200, TimeUnit.MILLISECONDS)
                    .expectResultWithin(5, TimeUnit.SECONDS);


            RemoteInterface svc = clientRemote.get(RemoteInterface.class, opts);
            svc.doSomething();
        } finally {
            FailPoint.setEnabled(false);
        }
    }

    private static interface RemoteInterface {
        boolean doSomething();
    }

    private static class RemoteImpl implements RemoteInterface {
        @Override
        public boolean doSomething() {
            FailPoint.simulateDelay(100);
            return false;
        }
    }
}
