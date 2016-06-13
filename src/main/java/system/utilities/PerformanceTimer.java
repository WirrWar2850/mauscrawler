package system.utilities;

import java.time.Duration;
import java.time.Instant;
import java.util.Stack;

/**
 * simple nestable timer for measuring application performance
 *
 * Created by Fabi on 28.02.2015.
 */
public class PerformanceTimer {
    Stack<Instant> timers = new Stack<>();

    public Instant start() {
        return timers.push(Instant.now());
    }

    public Duration stop() {
        return Duration.between(timers.pop(), Instant.now());
    }
}
