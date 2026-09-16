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

        // ================================================
        // PART: KIRILLOV
        // Timer 2: reacts repeatedly, at a fixed period
        // ================================================

        Timer periodicTimer = new Timer();
        periodicTimer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                System.out.println("[Period] Repeated action #" + counter);
            }
        }, 0, 2000);
    }
}