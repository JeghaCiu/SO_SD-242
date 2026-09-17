import javax.swing.*;
import java.awt.*;

public class TimerApp extends JFrame {

    private JTextArea outputArea;
    private JTextField delayField, durationField, periodField;
    private TimerLogic logic;

    public TimerApp() {
        setTitle("Планировщик задач - Timer Laboratory");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logic = new TimerLogic(this::addMessage);
        createInterface();
    }

    private void createInterface() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(
                "Планировщик задач с использованием Timer",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));
        main.add(title, BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(4, 1, 5, 5));

        delayField = new JTextField("5", 5);
        durationField = new JTextField("10", 5);
        periodField = new JTextField("2", 5);

        controls.add(createRow("1. Выполнить через:", delayField, e -> start(delayField, 1)));

        controls.add(createRow("2. Работать в течение:", durationField, e -> start(durationField, 2)));

        controls.add(createRow("3. Период:", periodField, e -> start(periodField, 3)));

        JButton stop = new JButton("Остановить все таймеры");
        stop.addActionListener(e -> logic.stopAllTimers());

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

    private JPanel createRow(String text, JTextField field, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton button = new JButton("Запустить");

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
            if (timer == 1) logic.startDelayTimer(seconds);
            if (timer == 2) logic.startDurationTimer(seconds);
            if (timer == 3) logic.startPeriodicTimer(seconds);
        } catch (NumberFormatException ex) {
            addMessage("Ошибка: введите целое число");
        }
    }

    private void addMessage(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(
                    outputArea.getDocument().getLength()
            );
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new TimerApp().setVisible(true)
        );
    }
}