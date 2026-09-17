import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI app = new UI();
            app.setVisible(true);
        });
    }
}
