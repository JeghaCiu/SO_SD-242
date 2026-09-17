public class MainApp {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            UI app = new UI();
            app.setVisible(true);
        });
    }
}
