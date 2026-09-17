import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SchedulerApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scheduler.fxml"));
        stage.setTitle("Планировщик процессов — Лабораторная 1");
        stage.setScene(new Scene(loader.load(), 640, 480));
        SchedulerController controller = loader.getController();
        stage.setOnCloseRequest(event -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
