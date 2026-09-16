import java.awt.Toolkit;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Периодическое действие: звуковой сигнал и запись в журнал.
 */
public class SoundTask extends TimerTask {
    private final Consumer<String> logger;
    private int count;

    public SoundTask(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        count++;
        Toolkit.getDefaultToolkit().beep();
        logger.accept("Периодический процесс: сигнал #" + count);
    }
}
