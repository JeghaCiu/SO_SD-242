import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class SchedulerController {
    @FXML private TextArea log;
    @FXML private Spinner<Integer> delaySec;
    @FXML private Spinner<Integer> periodSec;
    @FXML private TextField alarmTime;

    private Timer periodicTimer;
    private Timer delayTimer;
    private Timer alarmTimer;

    @FXML
    private void initialize() {
        periodSec.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2));
        delaySec.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 5));
        alarmTime.setText(LocalTime.now().plusMinutes(2).format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @FXML
    private void startPeriodic() {
        stopPeriodic();
        int periodMs = periodSec.getValue() * 1000;
        periodicTimer = new Timer();
        periodicTimer.scheduleAtFixedRate(new SoundTask(this::uiLog), 0, periodMs);
        uiLog("Период " + periodMs + " мс");
    }

    @FXML
    private void stopPeriodic() {
        if (periodicTimer != null) {
            periodicTimer.cancel();
            periodicTimer = null;
            uiLog("Периодический таймер остановлен");
        }
    }

    @FXML
    private void startDelayed() {
        if (delayTimer != null) delayTimer.cancel();
        int sec = delaySec.getValue();
        delayTimer = new Timer();
        delayTimer.schedule(new MessageTask("Прошло " + sec + " сек", this::uiLog), sec * 1000L);
        uiLog("Задержка " + sec + " сек");
    }

    @FXML
    private void startAlarm() {
        try {
            LocalTime time = LocalTime.parse(alarmTime.getText().trim(), DateTimeFormatter.ofPattern("H:mm"));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
            calendar.set(Calendar.MINUTE, time.getMinute());
            calendar.set(Calendar.SECOND, 0);
            if (calendar.getTime().before(new Date())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (alarmTimer != null) alarmTimer.cancel();
            alarmTimer = new Timer();
            alarmTimer.schedule(new MessageTask("Будильник сработал", this::uiLog), calendar.getTime());
            uiLog("Будильник на " + alarmTime.getText().trim());
        } catch (Exception ex) {
            uiLog("Формат времени: ЧЧ:ММ");
        }
    }

    void shutdown() {
        if (periodicTimer != null) periodicTimer.cancel();
        if (delayTimer != null) delayTimer.cancel();
        if (alarmTimer != null) alarmTimer.cancel();
    }

    private void uiLog(String text) {
        Platform.runLater(() ->
                log.appendText("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        + "] " + text + "\n"));
    }
}
