import java.util.Timer;
import java.util.TimerTask;

public class TimerApp {

    public static void main(String[] args) {

        // ================================================
        // PART: ALEX
        // Timer 1: reacts once, after a fixed delay
        // ================================================
        Timer delayedTimer = new Timer();
        delayedTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[Delay] 3 seconds passed - one-time action executed");
            }
        }, 3000);

        System.out.println("Start: timers launched...");
    }
}