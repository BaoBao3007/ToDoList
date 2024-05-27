package Controllers;

import Dao.DatabaseOperations;
import Model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.scene.control.Alert;

public class LoginController implements Initializable {

    @FXML
    private Pane Proot;

    @FXML
    private TextField txtuser;

    @FXML
    private TextField txtemail;

    @FXML
    private PasswordField txtpass;

    @FXML
    private ImageView exit;
    private Stage registerStage; // Biến lưu trữ stage cho màn hình đăng ký
    private Parent registerRoot; // Biến lưu trữ root node của màn hình đăng ký

    private DatabaseOperations db;

    double x,y=0;

    @FXML
    void signup(ActionEvent event) {
        String username = txtuser.getText();
        String password = txtpass.getText();
        String email = txtemail.getText();

        if (authenticateUser(username, password, email)) { // Sử dụng hàm authenticateUser đã có
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                GlobalData.currentUsername = username; // Lưu username vào biến toàn cục
                Dash();

            } catch (Exception e) {
                e.printStackTrace(); // Xử lý exception (nếu có) khi chuyển sang Dashboard
            }
        } else {
            // Xử lý khi đăng nhập thất bại
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContentText("Tài khoản hoặc mật khẩu không đúng");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK); // Chỉ cần nút OK
            dialog.showAndWait();
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exit.setOnMouseClicked(e -> System.exit(0));
        try {
            db = new DatabaseOperations();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi kết nối đến cơ sở dữ liệu."); // Hoặc xử lý lỗi theo cách khác
        }
    }
    private boolean authenticateUser(String username, String password, String email) {
        try {
            String query = "SELECT * FROM User WHERE username = '" + username + "' AND password = '" + password + "' AND email = '" + email + "'";
            ResultSet resultSet = db.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi kết nối đến cơ sở dữ liệu.");
            return false;
        } finally {
            db.close();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Hoặc AlertType.INFORMATION tùy thuộc vào loại thông báo
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void Dash()throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/Gui/Dashboard.fxml"));

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        //move around here
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void register(ActionEvent event) throws Exception {
        if (registerRoot == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gui/Register.fxml"));
            registerRoot = loader.load();
            registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
        }
        registerStage.setScene(new Scene(registerRoot));
        registerStage.show();
         ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
