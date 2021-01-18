
package forgotpass;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.BankingApplication;


public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField txtAccountNo;
    @FXML
    private ComboBox<String> cboxSecurityQuestion;
    @FXML
    private TextField txtAnswer;
    
   ObservableList<String> list = FXCollections.observableArrayList("What is your nickname?","What is your Childhood town?","What is your lucky number?");
    
    @FXML
    private void backToLogin(MouseEvent evt) throws IOException{
            BankingApplication.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }
    @FXML
    private void closeApp(MouseEvent evt){
            Platform.exit();
            System.exit(0);
    }
    
    @FXML
    public void recoverAccount(MouseEvent evt){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=? AND SecurityQuestion=? AND Answer=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, Integer.parseInt(txtAccountNo.getText()));
                    ps.setString(2, cboxSecurityQuestion.getValue());
                    ps.setString(3, txtAnswer.getText());
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Account Recovery");
                        a.setHeaderText("Below is your password.");
                        a.setContentText("Account No:\t"+rs.getString("AccountNo")+"\nPIN:\t"+rs.getString("PIN"));
                        a.showAndWait();
                    }
                    else{
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Account Recovery");
                        a.setHeaderText("Error in Recovering Password.");
                        a.setContentText("Please!! Enter valid details to recover your account.");
                        a.showAndWait();
                    }
                    
                    
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Recovering Password.");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
                    
            }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            cboxSecurityQuestion.setItems(list);
    }    
    
}
