import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {

    private JLabel statusDisplay;
    private JTextArea outputArea;
    private JButton delayButton, durationButton, periodicButton, stopAllButton;
    private Stopwatch logic; // Наш класс с логикой Timer/TimerTask

    public UI() {
        setTitle("Мульти-Таймер (Timer & TimerTask)");
        setSize(550, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация логики с колбэками
        logic = new Stopwatch(new Stopwatch.StopwatchListener() {
            @Override
            public void onStatusUpdate(String status) {
                SwingUtilities.invokeLater(() -> statusDisplay.setText(status));
            }

            @Override
            public void onLogMessage(String message) {
                addMessage(message);
            }
        });

        createInterface();
    }

    private void createInterface() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Приложение с несколькими таймерами", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        main.add(title, BorderLayout.NORTH);

        // Центральная панель
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Дисплей статуса
        statusDisplay = new JLabel("Готов к работе", SwingConstants.CENTER);
        statusDisplay.setFont(new Font("Arial", Font.BOLD, 22));
        statusDisplay.setForeground(new Color(20, 20, 20));
        centerPanel.add(statusDisplay, BorderLayout.CENTER);

        // Панель управления кнопками
        JPanel controls = new JPanel(new GridLayout(2, 2, 10, 10));
        
        delayButton = new JButton("1. Через 3 сек");
        durationButton = new JButton("2. Работает 5 сек");
        periodicButton = new JButton("3. Каждые 1.5 сек");
        stopAllButton = new JButton("Остановить всё");

        stopAllButton.setForeground(Color.RED);

        // Слушатели кнопок (вызов методов из Stopwatch)
        delayButton.addActionListener(e -> logic.startDelayTimer(3));
        durationButton.addActionListener(e -> logic.startDurationTimer(5));
        periodicButton.addActionListener(e -> logic.startPeriodicTimer(1.5));
        stopAllButton.addActionListener(e -> logic.stopAll());

        controls.add(delayButton);
        controls.add(durationButton);
        controls.add(periodicButton);
        controls.add(stopAllButton);

        centerPanel.add(controls, BorderLayout.SOUTH);
        main.add(centerPanel, BorderLayout.CENTER);

        // Консоль логов
        outputArea = new JTextArea(9, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        main.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        setContentPane(main);
    }

    private void addMessage(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
}