import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerApp extends JFrame implements ActionListener, Timers.Listener {

    private static final String[] TITLE = {"1. Через промежуток (мс)", "2. В течение (сек)", "3. С периодом (мс)"};
    private static final String[] DEF = {"3000", "10", "500"};

    private final JButton[] b = new JButton[3], go = new JButton[3], off = new JButton[3];
    private final JTextField[] f = new JTextField[3];
    private final Timers[] t = new Timers[3];
    private final JTextArea log = new JTextArea(12, 55);

    public TimerApp() {
        super("Timer и TimerTask: три режима");
        JPanel top = new JPanel(new GridLayout(1, 3, 6, 6));

        for (int i = 0; i < 3; i++) {
            t[i] = new Timers(i, i, this);
            JPanel p = new JPanel(new GridLayout(3, 1, 4, 4));
            p.setBorder(BorderFactory.createTitledBorder(TITLE[i]));
            p.add(f[i] = new JTextField(DEF[i]));

            b[i] = new JButton("0");
            b[i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            b[i].addActionListener(this);
            p.add(b[i]);

            JPanel c = new JPanel(new GridLayout(1, 2, 4, 0));
            c.add(go[i] = new JButton("Старт"));
            c.add(off[i] = new JButton("Стоп"));
            go[i].addActionListener(this);
            off[i].addActionListener(this);
            off[i].setEnabled(false);
            p.add(c);
            top.add(p);
        }

        log.setEditable(false);
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(log), BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            if (e.getSource() == go[i]) { start(i); return; }
            if (e.getSource() == off[i]) { t[i].stop("остановлен вручную"); return; }
            if (e.getSource() == b[i]) { b[i].setText("0"); return; }
        }
    }

    private void start(int i) {
        if (t[i].isRunning()) return;
        long v;
        try { v = Long.parseLong(f[i].getText().trim()); } catch (NumberFormatException ex) { v = 0; }
        if (v <= 0) { print(i, "неверный параметр"); return; }
        b[i].setText("0");
        go[i].setEnabled(false);
        off[i].setEnabled(true);
        f[i].setEnabled(false);
        t[i].start(v);
    }

    public void onTick(final int i, final String m, final boolean count) {
        SwingUtilities.invokeLater(() -> {
            if (count) b[i].setText(Integer.toString(Integer.parseInt(b[i].getText()) + 1));
            print(i, m);
        });
    }

    public void onStop(final int i, final String m) {
        SwingUtilities.invokeLater(() -> {
            go[i].setEnabled(true);
            off[i].setEnabled(false);
            f[i].setEnabled(true);
            print(i, m);
        });
    }

    private void print(int i, String m) {
        log.append("Таймер " + i + ": " + m + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimerApp().setVisible(true));
    }
}