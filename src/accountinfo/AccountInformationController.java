
package accountinfo;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import static javafx.scene.AccessibleAttribute.TEXT;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import login.LoginScreenController;


public class AccountInformationController implements Initializable {

    //public static String accountNo = LoginScreenController.accNo;
    
    @FXML
    private Text txtAccNo;
    @FXML
    private Text txtGender;
    @FXML
    private Text txtAccType;
    @FXML
    private Text txtReligion;
    @FXML
    private Label lblBalance;
    @FXML
    private AnchorPane dashboardMain;
    @FXML
    private Label lblBonus;
    
    private int noOfTransactions=0;
    
    public void setInfo(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                        lblBalance.setText(String.valueOf(rs.getDouble("Balance")));
                        txtAccNo.setText(String.valueOf(rs.getInt("AccountNo")));
                        txtGender.setText(rs.getString("Gender"));
                        txtAccType.setText(rs.getString("AccountType"));
                        txtReligion.setText(rs.getString("Religion"));
                    }
                    else{
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Login Account");
                        a.setHeaderText("Error in login");
                        a.setContentText("Your AccountNo or PIN is wrong. Try Again!!");
                        a.showAndWait();
                    } 
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error Message");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
                    
            }
    }
    @FXML
    private void withdraw(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/WithdrawAmount.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
    }
    @FXML
    private void deposit(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/deposit/DepositAmount.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            setInfo();
    }    
    
}
