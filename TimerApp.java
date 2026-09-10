import java.util.Timer;
import java.util.TimerTask;

/**
 * Лабораторная работа №1: Разработка механизма планирования процессов
 * с использованием таймера.
 *
 * Демонстрируются 3 обязательных режима работы таймера:
 *   1) Реакция через определённый промежуток времени (delay)
 *   2) Реакция в течение определённого времени (работает N секунд, потом сам себя останавливает)
 *   3) Реакция с указанным периодом (повторяется каждые period мс)
 * javac TimerApp.java && java TimerApp
 */
public class TimerApp {

    // ---------- Режим 1: реакция через определённый промежуток времени ----------
    static class DelayedTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("[Timer 1] Worked after 3 seconds from launch");
        }
    }

    // ---------- Режим 2: реакция в течение определённого времени ----------
    // Тикает каждую секунду, но сам себя останавливает через 8 секунд работы.
    static class DurationTask extends TimerTask {
        private final long startTime = System.currentTimeMillis();
        private final long durationMs;
        private final Timer ownerTimer;

        public DurationTask(Timer ownerTimer, long durationMs) {
            this.ownerTimer = ownerTimer;
            this.durationMs = durationMs;
        }

        @Override
        public void run() {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed >= durationMs) {
                System.out.println("[Timer 2] Time out (" + (durationMs / 1000) + " sec) — stopping.");
                ownerTimer.cancel();
                return;
            }
            System.out.println("[Timer 2] Working... done " + (elapsed / 1000) + " sec.");
        }
    }

    // ---------- Режим 3: реакция с указанным периодом ----------
    static class PeriodicTask extends TimerTask {
        private int counter = 0;

        @Override
        public void run() {
            counter++;
            System.out.println("[Timer 3] Periodic tic #" + counter + " (every 2 sec).");
        }
    }

    public static void main(String[] args) {
        System.out.println("App Start. Launching 3 timers...\n");

        // Таймер 1: разовая реакция через 3 секунды
        Timer timer1 = new Timer();
        timer1.schedule(new DelayedTask(), 3000);

        // Таймер 2: тикает каждую секунду, но живёт только 8 секунд
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new DurationTask(timer2, 8000), 0, 1000);

        // Таймер 3: строго периодическая задача каждые 2 секунды, без остановки вручную
        Timer timer3 = new Timer();
        timer3.scheduleAtFixedRate(new PeriodicTask(), 0, 2000);

        // Останавливаем всё приложение через 12 секунд, чтобы демонстрация не висела вечно
        Timer shutdownTimer = new Timer();
        shutdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n12 seconds passed - app stop.");
                timer1.cancel();
                timer2.cancel();
                timer3.cancel();
                shutdownTimer.cancel();
                System.exit(0);
            }
        }, 12000);
    }
}
