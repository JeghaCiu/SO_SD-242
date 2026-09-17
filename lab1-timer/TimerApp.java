import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

// ================================================
// PART: ALEX - One-time delayed GO signal
// ================================================
class GoSignal extends TimerTask {
    private final JTextArea output;
    private final JLabel statusLabel;

    public GoSignal(JTextArea output, JLabel statusLabel) {
        this.output = output;
        this.statusLabel = statusLabel;
    }

    @Override
    public void run() {
        Toolkit.getDefaultToolkit().beep();
        SwingUtilities.invokeLater(() -> {
            output.append("[T-minus] Mission Control: GO for launch sequence!\n");
            statusLabel.setText("GO FOR LAUNCH");
        });
    }
}

// ================================================
// PART: KIRILLOV - Repeating countdown every second
// ================================================
class CountdownTick extends TimerTask {
    private int secondsElapsed = 0;
    private final JTextArea output;
    private final JLabel timerLabel;
    private final JLabel statusLabel;

    public CountdownTick(JTextArea output, JLabel timerLabel, JLabel statusLabel) {
        this.output = output;
        this.timerLabel = timerLabel;
        this.statusLabel = statusLabel;
    }

    @Override
    public void run() {
        secondsElapsed++;
        Toolkit.getDefaultToolkit().beep();
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText(String.format("T+%02d", secondsElapsed));
            statusLabel.setText("COUNTDOWN IN PROGRESS");
            output.append("[Countdown] T+" + secondsElapsed + "s ...\n");
        });
    }
}

// ================================================
// PART: ALEX - Stops timers and triggers liftoff
// ================================================
class Liftoff extends TimerTask {
    private final Timer countdownTimer, goTimer, liftoffTimer;
    private final JTextArea output;
    private final JLabel timerLabel, statusLabel;
    private final JButton startButton;

    public Liftoff(Timer countdownTimer, Timer goTimer, Timer liftoffTimer,
                   JTextArea output, JLabel timerLabel, JLabel statusLabel,
                   JButton startButton) {
        this.countdownTimer = countdownTimer;
        this.goTimer = goTimer;
        this.liftoffTimer = liftoffTimer;
        this.output = output;
        this.timerLabel = timerLabel;
        this.statusLabel = statusLabel;
        this.startButton = startButton;
    }

    @Override
    public void run() {
        countdownTimer.cancel();
        goTimer.cancel();
        liftoffTimer.cancel();
        Toolkit.getDefaultToolkit().beep();
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("LIFTOFF!");
            statusLabel.setText("ROCKET LAUNCHED SUCCESSFULLY");
            output.append("[LIFTOFF] 10 seconds of countdown complete - rocket has launched!\n");
            startButton.setEnabled(false);
        });
    }
}

public class TimerApp {
    private JFrame frame;
    private JLabel statusLabel, timerLabel;
    private JTextArea output;
    private JButton startButton, resetButton;
    private Timer goTimer, countdownTimer, liftoffTimer;

    public TimerApp() {
        frame = new JFrame("Rocket Launch Control");
        frame.setSize(750, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("ROCKET LAUNCH CONTROL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        statusLabel = new JLabel("SYSTEM READY", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel = new JLabel("T+00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 64));
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        output = new JTextArea();
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setEditable(false);
        output.setText("=== ROCKET LAUNCH SYSTEM ===\nSystem initialized.\nPress START LAUNCH to begin.\n\n");

        startButton = new JButton("START LAUNCH");
        resetButton = new JButton("RESET");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 15, 0));
        buttons.add(startButton);
        buttons.add(resetButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(titleLabel, BorderLayout.NORTH);
        top.add(statusLabel, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        center.add(timerLabel, BorderLayout.NORTH);
        center.add(new JScrollPane(output), BorderLayout.CENTER);
        center.add(buttons, BorderLayout.SOUTH);

        frame.add(top, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);

        startButton.addActionListener(e -> startLaunch());
        resetButton.addActionListener(e -> resetLaunch());
    }
}