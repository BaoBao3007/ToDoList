import Dao.ReminderDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    double x,y = 0;
    private ReminderDao reminderDao;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Gui/login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        //move around here
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        sendDueReminders();

    }
    private void sendDueReminders() {
        if (reminderDao == null) {
            reminderDao = ReminderDao.getInstance();
        }
        reminderDao.sendDueReminders();
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (reminderDao != null) {
            reminderDao.stopReminderScheduler();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        reminderDao = ReminderDao.getInstance();
    }
}
