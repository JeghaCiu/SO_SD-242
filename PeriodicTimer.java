import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class PeriodicTimer {
    private Timer timer = new Timer();

    public void start(int durationSec, long periodMs) {
        timer.scheduleAtFixedRate(new TimerTask() {
            private int left = durationSec;

            public void run() {
                if (left > 0) {
                    System.out.println("Осталось: " + left + " сек");
                    left--;
                } else {
                    System.out.println("Многоразовый таймер. Период завершен");
                    timer.cancel();
                }
            }
        }, 0, periodMs);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите время работы таймера (сек): ");
        int sec = sc.nextInt();

        SingleTimer single = new SingleTimer();
        single.start(3000);

        PeriodicTimer periodic = new PeriodicTimer();
        periodic.start(sec, 1000);

        sc.close();
    }
}