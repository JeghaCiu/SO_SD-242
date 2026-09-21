import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerLabApp extends JFrame {

    // Элементы управления GUI
    private JComboBox<String> modeComboBox;
    private JTextField timeInputField;
    private JLabel timeInputLabel;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;

    // Объекты таймеров
    private Timer activeTimer;
    private Timer controllerTimer;
    private int counter = 0;

    public TimerLabApp() {
        // Настройка главного окна
        setTitle("Лабораторная работа №1 — Выбор режима таймера");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Верхняя панель: Настройки и выбор режима ---
        JPanel controlPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Параметры работы таймера"));

        // Выпадающий список с 3 режимами
        controlPanel.add(new JLabel("Выберите режим работы:"));
        String[] modes = {
            "1. Реагировать через время (Однократная задержка)",
            "2. Реагировать в течение времени (С ограничением)",
            "3. Реагировать с указанным периодом (Повторение)"
        };
        modeComboBox = new JComboBox<>(modes);
        controlPanel.add(modeComboBox);

        // Поле ввода времени (в миллисекундах)
        timeInputLabel = new JLabel("Задержка / Период (мс):");
        controlPanel.add(timeInputLabel);
        timeInputField = new JTextField("3000"); // По умолчанию 3000 мс (3 сек)
        controlPanel.add(timeInputField);

        // Кнопки управления
        startButton = new JButton("Запустить таймер");
        stopButton = new JButton("Остановить / Сброс");
        stopButton.setEnabled(false);

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.NORTH);

        // --- Центральная панель: Лог событий ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Консоль событий"));

        add(scrollPane, BorderLayout.CENTER);

        // --- Слушатели событий (Listeners) ---

        // Изменение текста подсказки при выборе режима
        modeComboBox.addActionListener(e -> {
            int selected = modeComboBox.getSelectedIndex();
            if (selected == 0) {
                timeInputLabel.setText("Пауза перед срабатыванием (мс):");
                timeInputField.setText("3000");
            } else if (selected == 1) {
                timeInputLabel.setText("Длительность работы (мс):");
                timeInputField.setText("6000");
            } else if (selected == 2) {
                timeInputLabel.setText("Интервал повторений (мс):");
                timeInputField.setText("1500");
            }
        });

        // Запуск выбранного режима
        startButton.addActionListener(e -> startSelectedMode());

        // Ручная остановка
        stopButton.addActionListener(e -> {
            stopAllTimers();
            appendLog("⚠️ Работа таймера принудительно остановлена пользователем.");
        });
    }

    private String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + getTime() + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void startSelectedMode() {
        stopAllTimers(); // Сброс предыдущих таймеров
        counter = 0;

        int selectedMode = modeComboBox.getSelectedIndex();
        int timeValue;

        try {
            timeValue = Integer.parseInt(timeInputField.getText().trim());
            if (timeValue <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, введите корректное положительное число в миллисекундах!", 
                "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return;
        }

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        activeTimer = new Timer("ActiveTimer");

        switch (selectedMode) {
            case 0: // РЕЖИМ 1: Через определённый промежуток времени
                appendLog("▶ [РЕЖИМ 1] Запущен: Срабатывание ровно через " + timeValue + " мс.");
                activeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Toolkit.getDefaultToolkit().beep();
                        appendLog("✅ [РЕЖИМ 1] Таймер сработал!");
                        finishWork();
                    }
                }, timeValue);
                break;

            case 1: // РЕЖИМ 2: В течение определённого времени
                appendLog("▶ [РЕЖИМ 2] Запущен: Работает в течение " + timeValue + " мс (период 1 сек).");
                
                // Периодическая задача раз в секунду
                activeTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        counter++;
                        appendLog("   ...Процесс активен (" + counter + " сек)");
                    }
                }, 0, 1000);

                // Ограничитель времени
                controllerTimer = new Timer("ControllerTimer");
                controllerTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        appendLog("🛑 [РЕЖИМ 2] Время истекло (" + timeValue + " мс). Процесс остановлен!");
                        finishWork();
                    }
                }, timeValue);
                break;

            case 2: // РЕЖИМ 3: С указанным периодом
                appendLog("▶ [РЕЖИМ 3] Запущен: Повторение каждые " + timeValue + " мс.");
                activeTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        counter++;
                        Toolkit.getDefaultToolkit().beep();
                        appendLog("🔔 [РЕЖИМ 3] Периодический сигнал №" + counter);
                    }
                }, 0, timeValue);
                break;
        }
    }

    private void finishWork() {
        stopAllTimers();
        appendLog("=== Выполнение режима завершено ===\n");
    }

    private void stopAllTimers() {
        if (activeTimer != null) {
            activeTimer.cancel();
            activeTimer = null;
        }
        if (controllerTimer != null) {
            controllerTimer.cancel();
            controllerTimer = null;
        }
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimerLabApp app = new TimerLabApp();
            app.setVisible(true);
        });
    }
}