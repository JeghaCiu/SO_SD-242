import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {

    private JLabel timeDisplay;
    private JTextArea outputArea;
    private JButton startPauseButton, resetButton, lapButton;
    private Stopwatch logic;

    public UI() {
        setTitle("Секундомер");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logic = new Stopwatch(new Stopwatch.StopwatchListener() {
            @Override
            public void onTimeUpdate(int hundredths, int seconds, int minutes) {
                SwingUtilities.invokeLater(() -> {
                    timeDisplay.setText(String.format("%02d:%02d:%02d", minutes, seconds, hundredths));
                });
            }

            @Override
            public void onStatusMessage(String message) {
                addMessage(message);
            }
        });

        createInterface();
    }

    private void createInterface() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JLabel title = new JLabel("Секундомер", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        main.add(title, BorderLayout.NORTH);

        // Центральная панель
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Циферблат 
        timeDisplay = new JLabel("00:00:00", SwingConstants.CENTER);
        timeDisplay.setFont(new Font("Monospaced", Font.BOLD, 48));
        timeDisplay.setForeground(new Color(20, 20, 20));
        centerPanel.add(timeDisplay, BorderLayout.CENTER);

        // панель управления
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        startPauseButton = new JButton("Старт");
        resetButton = new JButton("Сброс");
        lapButton = new JButton("Круг");

        startPauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // start/stop
        startPauseButton.addActionListener(e -> {
            if (logic.isRunning()) {
                logic.pause();
                startPauseButton.setText("Старт");
            } else {
                logic.start();
                startPauseButton.setText("Пауза");
            }
        });

      
        resetButton.addActionListener(e -> {
            logic.reset();
            startPauseButton.setText("Старт");
        });

        lapButton.addActionListener(e -> logic.lap());

        controls.add(startPauseButton);
        controls.add(lapButton);
        controls.add(resetButton);

        centerPanel.add(controls, BorderLayout.SOUTH);
        main.add(centerPanel, BorderLayout.CENTER);

        // консоль
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

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