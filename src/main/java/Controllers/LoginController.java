package Controllers;


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

    double x,y=0;

    @FXML
    void signup(ActionEvent event) {
        if(txtuser.getText().equals("admin") && txtemail.getText().equals("123") && txtpass.getText().equals("123")){
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();

                stage.close();

                Dash();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContentText("Tài khoản hoặc mật khẩu không đúng");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.showAndWait();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exit.setOnMouseClicked(e -> System.exit(0));
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



}
