import java.util.Timer;
import java.util.TimerTask;

public class SingleTimer {
    private Timer timer = new Timer();

    public void start(long delayMs) {
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("Одноразовый таймер. Время вышло");
                timer.cancel();
            }
        }, delayMs);
    }
}