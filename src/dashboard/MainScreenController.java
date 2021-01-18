
package dashboard;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import login.LoginScreenController;


public class MainScreenController implements Initializable {

    
    @FXML
    private Label lblName;
    @FXML
    private Label lblBody;
    @FXML
    private AnchorPane dashboardMain;
    
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
                        lblName.setText(rs.getString("Name"));
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
    private void transferAmount(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/transferamount/TransferAmount.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
    }
    @FXML
    private void transactionHistory(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/transactionhistory/Transaction.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            lblBody.setText("Habib Bank Limited is a Pakistani multinational bank based\nin Habib Bank Plaza, Karachi, Pakistan. It is owned\nby Aga Khan Fund for Economic Development and\nis the largest bank by assets in Pakistan. Founded\nin 1941, HBL became Pakistan's first commercial bank.");
            setInfo();
    }    
    
}
