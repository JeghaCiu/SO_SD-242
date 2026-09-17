import java.util.TimerTask;
import java.util.function.Consumer;

public class MessageTask extends TimerTask {
    private final String message;
    private final Consumer<String> logger;

    public MessageTask(String message, Consumer<String> logger) {
        this.message = message;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.accept(message);
    }
}
