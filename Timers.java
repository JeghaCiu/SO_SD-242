import java.util.Timer;
import java.util.TimerTask;

public class Timers {

    public interface Listener {
        void onTick(int i, String msg, boolean count);
        void onStop(int i, String msg);
    }

    public static final int ONCE = 0, DURATION = 1, PERIOD = 2;

    private final int index, mode;
    private final Listener listener;
    private Timer timer;

    public Timers(int index, int mode, Listener listener) {
        this.index = index;
        this.mode = mode;
        this.listener = listener;
    }

    public synchronized boolean isRunning() {
        return timer != null;
    }

    public synchronized void start(final long v) {
        if (timer != null) return;
        timer = new Timer(true);

        if (mode == ONCE) {
            listener.onTick(index, "ожидание " + v + " мс", false);
            timer.schedule(new TimerTask() {
                public void run() {
                    listener.onTick(index, "время вышло", true);
                    stop("одноразовый завершён");
                }
            }, v);

        } else if (mode == DURATION) {
            listener.onTick(index, "работа " + v + " сек", false);
            timer.scheduleAtFixedRate(new TimerTask() {
                long left = v;
                public void run() {
                    if (left > 0) listener.onTick(index, "осталось " + left-- + " сек", true);
                    else stop("период " + v + " сек завершён");
                }
            }, 0, 1000);

        } else {
            listener.onTick(index, "период " + v + " мс", false);
            timer.scheduleAtFixedRate(new TimerTask() {
                int n;
                public void run() {
                    listener.onTick(index, "срабатывание " + ++n, true);
                }
            }, v, v);
        }
    }

    public synchronized void stop(String msg) {
        if (timer == null) return;
        timer.cancel();
        timer = null;
        listener.onStop(index, msg);
    }
}