import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;


// Test Comment

public class TimerApp extends JFrame {
    private final JTextArea notes = new JTextArea();
    private Timer durationTimer;
    private Timer periodTimer;

    public TimerApp() {
        super("Timer App");

        JButton delayButton = new JButton("After 3 sec");
        JButton durationButton = new JButton("For 8 sec");
        JButton periodButton = new JButton("Every 2 sec");
        JButton stopButton = new JButton("Stop all");

        delayButton.addActionListener(e -> startDelayTimer());
        durationButton.addActionListener(e -> startDurationTimer());
        periodButton.addActionListener(e -> startPeriodTimer());
        stopButton.addActionListener(e -> stopAll());

        JPanel buttons = new JPanel(new GridLayout(2, 2, 8, 8));
        buttons.add(delayButton);
        buttons.add(durationButton);
        buttons.add(periodButton);
        buttons.add(stopButton);

        notes.setEditable(false);
        notes.setLineWrap(true);
        notes.setText("Notes:\n");

        add(buttons, BorderLayout.NORTH);
        add(new JScrollPane(notes), BorderLayout.CENTER);

        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void startDelayTimer() {
        Timer timer = new Timer();
        note("Delay timer: waits 3 sec.");

        timer.schedule(new TimerTask() {
            public void run() {
                note("Delay timer: done.");
                timer.cancel();
            }
        }, 3000);
    }

    private void startDurationTimer() {
        stopDurationTimer();
        durationTimer = new Timer();
        note("Duration timer: works 8 sec.");

        durationTimer.scheduleAtFixedRate(new TimerTask() {
            private int seconds = 0;

            public void run() {
                seconds++;
                note("Duration timer: " + seconds + " sec.");

                if (seconds == 8) {
                    note("Duration timer: stopped.");
                    stopDurationTimer();
                }
            }
        }, 0, 1000);
    }

    private void startPeriodTimer() {
        stopPeriodTimer();
        periodTimer = new Timer();
        note("Period timer: every 2 sec.");

        periodTimer.scheduleAtFixedRate(new TimerTask() {
            private int count = 0;

            public void run() {
                count++;
                note("Period timer: tick " + count + ".");
            }
        }, 0, 2000);
    }

    private void stopAll() {
        stopDurationTimer();
        stopPeriodTimer();
        note("All active timers stopped.");
    }

    private void stopDurationTimer() {
        if (durationTimer != null) {
            durationTimer.cancel();
            durationTimer = null;
        }
    }

    private void stopPeriodTimer() {
        if (periodTimer != null) {
            periodTimer.cancel();
            periodTimer = null;
        }
    }

    private void note(String text) {
        SwingUtilities.invokeLater(() -> notes.append(text + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimerApp().setVisible(true));
    }
}
