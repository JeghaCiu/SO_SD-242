import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerApp extends JFrame {

    private JTextArea outputArea;
    private JTextField delayField, durationField, periodField;
    private TimerLogic logic;

    public TimerApp() {
        setTitle("Таймер");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logic = new TimerLogic(new TimerLogic.MessageListener() {
            public void onMessage(String text) { addMessage(text); } });
        createInterface();
    }

    private void createInterface() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Таймеры крутые", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 20));
        main.add(title, BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(4, 1, 5, 5));

        delayField = new JTextField("1", 5);
        durationField = new JTextField("15", 5);
        periodField = new JTextField("5", 5);

        controls.add(createRow("1)Выполнить через:", delayField, new DelayListener()));
        controls.add(createRow("2)Работать в течение:", durationField, new DurationListener()));
        controls.add(createRow("3)Период:", periodField, new PeriodListener()));

        JButton stop = new JButton("Остановить все таймеры");
        stop.addActionListener(new StopListener());

        JPanel stopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stopPanel.add(stop);
        controls.add(stopPanel);

        main.add(controls, BorderLayout.CENTER);

        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        main.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JPanel createRow(String text, JTextField field, ActionListener action) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton button = new JButton("Включить таймер");

        panel.add(new JLabel(text));
        panel.add(field);
        panel.add(new JLabel("сек."));
        panel.add(button);

        button.addActionListener(action);

        return panel;
    }

    private void start(JTextField field, int timer) {
        try {
            int seconds = Integer.parseInt(field.getText().trim());
            if (seconds <= 0) return;
            if (timer == 1) { logic.startDelayTimer(seconds); }
            if (timer == 2) { logic.startDurationTimer(seconds); }
            if (timer == 3) { logic.startPeriodicTimer(seconds);} }
        catch (NumberFormatException ex) {addMessage("Ошибка: введите целое число");}
    }

    private void addMessage(String text) {
        SwingUtilities.invokeLater(new MessageRunnable(text));
    }

    private class MessageListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            addMessage(e.getActionCommand());
        }
    }

    private class DelayListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            start(delayField, 1);
        }
    }

    private class DurationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            start(durationField, 2);
        }
    }

    private class PeriodListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            start(periodField, 3);
        }
    }

    private class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            logic.stopAllTimers();
        }
    }

    private class MessageRunnable implements Runnable {
        private String text;

        public MessageRunnable(String text) {
            this.text = text;
        }

        public void run() {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TimerApp().setVisible(true);
            }
        });
    }
}