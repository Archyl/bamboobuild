package utils.timeouts;

public class TimeoutUtil {

    private TimeoutUtil() {

    }

    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}
