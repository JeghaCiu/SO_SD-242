import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {

    private Timer delayTimer;
    private Timer durationTimer;
    private Timer periodicTimer;

    private StopwatchListener listener;

    public interface StopwatchListener {
        void onStatusUpdate(String status);
        void onLogMessage(String message);
    }

    public Stopwatch(StopwatchListener listener) {
        this.listener = listener;
    }

    private void log(String text) {
        if (listener != null) {
            listener.onLogMessage(text);
        }
    }

    private void updateStatus(String status) {
        if (listener != null) {
            listener.onStatusUpdate(status);
        }
    }

    public synchronized void startDelayTimer(double seconds) {
        if (delayTimer != null) {
            delayTimer.cancel();
        }

        long delayMs = (long) (seconds * 1000);
        delayTimer = new Timer();
        log(String.format("[Режим 1] Таймер запущен. Сработает через %.1f сек...", seconds));
        updateStatus("Ожидание задержки...");

        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log(String.format("[Режим 1] СРАБОТАЛ! Прошло %.1f сек.", seconds));
                updateStatus("Задержка завершена!");
            }
        }, delayMs);
    }

    public synchronized void startDurationTimer(double seconds) {
        if (durationTimer != null) {
            durationTimer.cancel();
        }

        long durationMs = (long) (seconds * 1000);
        long startTime = System.currentTimeMillis();

        durationTimer = new Timer();
        log(String.format("[Режим 2] Запущен таймер работы на %.1f сек...", seconds));

        durationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= durationMs) {
                    log("[Режим 2] Время работы истекло. Остановка!");
                    updateStatus("Таймер времени остановлен.");
                    durationTimer.cancel();
                } else {
                    double remaining = (durationMs - elapsed) / 1000.0;
                    log(String.format("[Режим 2] Работает... Осталось: %.1f сек", remaining));
                    updateStatus(String.format("Активен (осталось %.1f с)", remaining));
                }
            }
        }, 0, 1000);
    }

    public synchronized void startPeriodicTimer(double periodSeconds) {
        if (periodicTimer != null) {
            periodicTimer.cancel();
        }

        long periodMs = (long) (periodSeconds * 1000);
        periodicTimer = new Timer();
        log(String.format("[Режим 3] Запущен периодический таймер (интервал %.1f сек)", periodSeconds));

        periodicTimer.scheduleAtFixedRate(new TimerTask() {
            private int tickCount = 0;

            @Override
            public void run() {
                tickCount++;
                log(String.format("[Режим 3] Тик #%d (интервал %.1f сек)", tickCount, periodSeconds));
                updateStatus(String.format("Периодический: Тик #%d", tickCount));
            }
        }, 0, periodMs);
    }


    public synchronized void stopAll() {
        if (delayTimer != null) {
            delayTimer.cancel();
            delayTimer = null;
        }
        if (durationTimer != null) {
            durationTimer.cancel();
            durationTimer = null;
        }
        if (periodicTimer != null) {
            periodicTimer.cancel();
            periodicTimer = null;
        }
        log("Все активные таймеры остановлены.");
        updateStatus("Все таймеры остановлены");
    }
}