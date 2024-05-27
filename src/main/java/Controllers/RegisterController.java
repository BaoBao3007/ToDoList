package Controllers;

import Dao.DatabaseOperations;
import Model.User;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import java.sql.ResultSet;

public class RegisterController  {
    @FXML
    private TextField txtfullname;
    @FXML
    private TextField txtuser;
    @FXML
    private TextField txtemail;
    @FXML
    private PasswordField txtpass;
    @FXML
    private PasswordField txtenterpass;

    private DatabaseOperations db;

    public RegisterController() throws SQLException {
        db = new DatabaseOperations();
    }

    @FXML
    private void register(ActionEvent event) {
        String fullname = txtfullname.getText();
        String username = txtuser.getText();
        String email = txtemail.getText();
        String password = txtpass.getText();
        String confirmPassword = txtenterpass.getText();


        if (!isValidInput(fullname, username, email, password, confirmPassword)) {

            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin và đảm bảo mật khẩu trùng khớp.");
            return;
        }

        // Kiểm tra username đã tồn tại chưa (bạn cần tự triển khai)
        if (isUsernameExists(username)) {
            showAlert("Lỗi", "Tên người dùng đã tồn tại.");
            return;
        }


        User newUser = new User(username, password, email, fullname); // Mã hóa mật khẩu nếu cần


        try {
            String query = "INSERT INTO User (username, password, email, fullname) VALUES ('" +
                    newUser.getUsername() + "', '" + newUser.getPassword() + "', '" +
                    newUser.getEmail() + "', '" + newUser.getFullname() + "')";
            db.executeUpdate(query);

            // Hiển thị thông báo thành công
            showAlert("Thành công", "Đăng ký thành công!");
        } catch (SQLException e) {
            // Xử lý lỗi SQL (ví dụ: hiển thị thông báo lỗi)
            showAlert("Lỗi", "Đăng ký thất bại. Vui lòng thử lại.");
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Các phương thức hỗ trợ
    private boolean isValidInput(String fullname, String username, String email, String password, String confirmPassword) {
        if (fullname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return false; // Kiểm tra các trường khác không rỗng
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            showAlert("Lỗi", "Định dạng email không hợp lệ."); // Thông báo lỗi riêng cho email
            return false;
        }


        // Kiểm tra mật khẩu có ít nhất 6 ký tự và trùng khớp với mật khẩu xác nhận
        if (password.length() < 6 || !password.equals(confirmPassword)) {
            return false;
        }

        // Nếu tất cả các kiểm tra đều qua, dữ liệu hợp lệ
        return true;
    }
    private boolean isValidEmail(String email) {
        // Kiểm tra định dạng email bằng biểu thức chính quy
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    private boolean isUsernameExists(String username) {
        try {
            // 1. Tạo câu truy vấn SQL
            String query = "SELECT COUNT(*) FROM User WHERE username = '" + username + "'";

            // 2. Thực hiện truy vấn
            ResultSet resultSet = db.executeQuery(query);

            // 3. Kiểm tra kết quả
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Trả về true nếu tìm thấy username (count > 0)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi kiểm tra tên người dùng.");
        } finally {
            db.close(); // Đóng kết nối sau khi sử dụng
        }

        return false; // Trả về false nếu không tìm thấy hoặc có lỗi
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private Stage loginStage;
    @FXML
    private Parent loginRoot;


    @FXML
    private void backlogin(ActionEvent event) throws IOException {
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
    }

}
