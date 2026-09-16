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