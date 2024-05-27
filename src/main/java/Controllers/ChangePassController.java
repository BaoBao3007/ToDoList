package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import Dao.MySQLDataAccess;
import Dao.UserDao;
import Model.User;

import javafx.scene.control.*;


import java.sql.SQLException;
import java.io.IOException;

public class ChangePassController {
    @FXML
    private Stage loginStage;
    @FXML
    private Parent loginRoot;
    @FXML
    private TextField txtuser;
    @FXML
    private PasswordField txtoldpass;
    @FXML
    private PasswordField txtnewpass;
    @FXML
    private PasswordField txtconfirmnewpass;

    private UserDao userDao = UserDao.getInstance();
    private MySQLDataAccess dbConnection;


    @FXML
    private void changepass(ActionEvent event) throws SQLException {
        String username = txtuser.getText();
        String oldPassword = txtoldpass.getText();
        String newPassword = txtnewpass.getText();
        String confirmNewPassword = txtconfirmnewpass.getText();

        // Kiểm tra các ràng buộc
        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Lỗi", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert("Lỗi", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return;
        }

        String oldPasswordFromDatabase = userDao.getPasswordByUsername(username);

        if (oldPasswordFromDatabase == null) {
            showAlert("Lỗi", "Tên đăng nhập không tồn tại.");
            return;
        }

        // So sánh mật khẩu cũ nhập vào với mật khẩu cũ trong cơ sở dữ liệu
        if (!oldPassword.equals(oldPasswordFromDatabase)) {
            showAlert("Lỗi", "Mật khẩu cũ không đúng.");
            return;
        }

        User user = new User(username, newPassword, "", "");

        // Gọi phương thức changePassword trong UserDao để cập nhật mật khẩu
        if (userDao.changePassword(user, oldPasswordFromDatabase)) { // Truyền thêm mật khẩu cũ
            showAlert("Thành công", "Đã đổi mật khẩu thành công.");
            backlogin(event);
        } else {
            showAlert("Lỗi", "Tên đăng nhập hoặc mật khẩu cũ không đúng.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    private void backlogin(ActionEvent event) {
        try {
            if (loginRoot == null) {
                // Load login.fxml nếu chưa được load
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gui/Login.fxml"));
                loginRoot = loader.load();
                loginStage = new Stage();
                loginStage.initStyle(StageStyle.UNDECORATED);
            }

            // Hiển thị lại stage đăng nhập
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();

            // Đóng stage đăng ký hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            // Xử lý khi có lỗi xảy ra (ví dụ: hiển thị thông báo lỗi)
            showAlert("Lỗi", "Không thể tải giao diện đăng nhập.");
            e.printStackTrace(); // In ra chi tiết lỗi để debug
        }
    }

}
