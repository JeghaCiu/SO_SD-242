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
