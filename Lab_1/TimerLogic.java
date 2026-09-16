import java.util.Timer;
import java.util.TimerTask;

public class TimerLogic {

    private Timer delayTimer, durationTimer, periodicTimer;
    private MessageListener listener;

    public interface MessageListener {
        void onMessage(String message);
    }

    public TimerLogic(MessageListener listener) {
        this.listener = listener;
    }

    private void msg(String text) {
        listener.onMessage(text);
    }

    public void startDelayTimer(int seconds) {
        if (delayTimer != null) delayTimer.cancel();

        delayTimer = new Timer();
        msg("Таймер 1 запущен. Ожидание: " + seconds + " сек.");

        delayTimer.schedule(new TimerTask() {
            public void run() {
                msg("Таймер 1: задача выполнена!");
                delayTimer.cancel();
            }
        }, seconds * 1000L);
    }

    public void startDurationTimer(int seconds) {
        if (durationTimer != null) durationTimer.cancel();

        durationTimer = new Timer();
        msg("Таймер 2 запущен на " + seconds + " сек.");

        durationTimer.scheduleAtFixedRate(new TimerTask() {
            long start = System.currentTimeMillis();

            public void run() {
                long elapsed = (System.currentTimeMillis() - start) / 1000;
                msg("Таймер 2: прошло " + elapsed + " сек.");

                if (elapsed >= seconds) {
                    msg("Таймер 2 завершил работу.");
                    durationTimer.cancel();
                }
            }
        }, 0, 1000);
    }

    public void startPeriodicTimer(int seconds) {
        if (periodicTimer != null) periodicTimer.cancel();

        periodicTimer = new Timer();
        msg("Таймер 3 запущен. Период: " + seconds + " сек.");

        periodicTimer.scheduleAtFixedRate(new TimerTask() {
            int counter = 1;

            public void run() {
                msg("Таймер 3: срабатывание №" + counter++);
            }
        }, 0, seconds * 1000L);
    }

    public void stopAllTimers() {
        if (delayTimer != null) delayTimer.cancel();
        if (durationTimer != null) durationTimer.cancel();
        if (periodicTimer != null) periodicTimer.cancel();

        delayTimer = durationTimer = periodicTimer = null;
        msg("Все таймеры остановлены.");
    }
}