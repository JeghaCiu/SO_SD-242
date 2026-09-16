import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Планировщик процессов: три режима java.util.Timer / TimerTask.
 *
 * 1) schedule(task, delay) — один раз через заданную задержку
 * 2) schedule(task, date) — один раз в указанное время
 * 3) scheduleAtFixedRate(task, delay, period) — повторять с периодом
 */
public class SchedulerApp extends JFrame {
    private final JTextArea logArea = new JTextArea();
    private final JSlider progressSlider = new JSlider(0, 100, 0);
    private final JSpinner delaySeconds = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
    private final JSpinner periodSeconds = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
    private final JTextField alarmTimeField = new JTextField(currentTimePlusMinutes(2));

    private Timer periodicTimer;
    private Timer delayTimer;
    private Timer alarmTimer;
    private Timer progressTimer;

    public SchedulerApp() {
        super("Планировщик процессов — Лабораторная 1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(680, 520);
        setLocationRelativeTo(null);

        logArea.setEditable(false);
        progressSlider.setPaintTicks(true);
        progressSlider.setMajorTickSpacing(20);
        progressSlider.setPaintLabels(true);

        add(buildControls(), BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(buildProgressPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildControls() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton startPeriodic = new JButton("Старт периодического");
        JButton stopPeriodic = new JButton("Стоп периодического");
        JButton startDelay = new JButton("Запустить с задержкой");
        JButton startAlarm = new JButton("Будильник на время");
        JButton startProgress = new JButton("Прогресс за 10 секунд");

        panel.add(new JLabel("Период, сек:"));
        panel.add(periodSeconds);
        panel.add(startPeriodic);
        panel.add(stopPeriodic);
        panel.add(new JLabel("Задержка, сек:"));
        panel.add(delaySeconds);
        panel.add(startDelay);
        panel.add(new JLabel());
        panel.add(new JLabel("Время будильника (ЧЧ:ММ):"));
        panel.add(alarmTimeField);
        panel.add(startAlarm);
        panel.add(startProgress);

        startPeriodic.addActionListener(e -> startPeriodic());
        stopPeriodic.addActionListener(e -> stopPeriodic());
        startDelay.addActionListener(e -> startDelayed());
        startAlarm.addActionListener(e -> startAlarm());
        startProgress.addActionListener(e -> startProgress());
        return panel;
    }

    private JPanel buildProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        panel.add(new JLabel("Индикатор процесса:"), BorderLayout.WEST);
        panel.add(progressSlider, BorderLayout.CENTER);
        return panel;
    }

    private void uiLog(String text) {
        SwingUtilities.invokeLater(() -> {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            logArea.append("[" + time + "] " + text + System.lineSeparator());
        });
    }

    /** Режим 3: повторять с фиксированным периодом. */
    private void startPeriodic() {
        stopPeriodic();
        int periodMs = (Integer) periodSeconds.getValue() * 1000;
        periodicTimer = new Timer();
        periodicTimer.scheduleAtFixedRate(new SoundTask(this::uiLog), 0, periodMs);
        uiLog("Запущен периодический таймер, период " + periodMs + " мс");
    }

    private void stopPeriodic() {
        if (periodicTimer != null) {
            periodicTimer.cancel();
            periodicTimer = null;
            uiLog("Периодический таймер остановлен (cancel)");
        }
    }

    /** Режим 1: один раз через delay. */
    private void startDelayed() {
        if (delayTimer != null) {
            delayTimer.cancel();
        }
        int delayMs = (Integer) delaySeconds.getValue() * 1000;
        delayTimer = new Timer();
        delayTimer.schedule(
                new MessageTask("Прошло " + delaySeconds.getValue() + " сек — отложенный процесс выполнен", this::uiLog),
                delayMs
        );
        uiLog("Отложенный процесс запланирован через " + delaySeconds.getValue() + " сек");
    }

    /** Режим 2: один раз в указанное время суток. */
    private void startAlarm() {
        Date alarmDate;
        try {
            alarmDate = parseAlarmDate(alarmTimeField.getText().trim());
        } catch (DateTimeParseException ex) {
            uiLog("Ошибка: время должно быть в формате ЧЧ:ММ, например 18:30");
            return;
        }

        if (alarmTimer != null) {
            alarmTimer.cancel();
        }
        alarmTimer = new Timer();
        alarmTimer.schedule(
                new MessageTask("Будильник: время процесса наступило!", this::uiLog),
                alarmDate
        );
        uiLog("Будильник поставлен на " + alarmTimeField.getText().trim());
    }

    /** Дополнительно: слайдер двигается таймером 10 секунд, шаг 10%. */
    private void startProgress() {
        if (progressTimer != null) {
            progressTimer.cancel();
        }
        progressSlider.setValue(0);
        progressTimer = new Timer();
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            int value = 0;

            @Override
            public void run() {
                value += 10;
                final int current = Math.min(value, 100);
                SwingUtilities.invokeLater(() -> progressSlider.setValue(current));
                uiLog("Прогресс процесса: " + current + "%");
                if (current >= 100) {
                    cancel();
                    uiLog("Процесс на слайдере завершён");
                }
            }
        }, 0, 1000);
    }

    private Date parseAlarmDate(String text) {
        LocalTime time = LocalTime.parse(text, DateTimeFormatter.ofPattern("H:mm"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTime().before(new Date())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

    private static String currentTimePlusMinutes(int minutes) {
        return LocalTime.now().plusMinutes(minutes).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulerApp().setVisible(true));
    }
}
