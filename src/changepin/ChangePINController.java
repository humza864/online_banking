
package changepin;

import dashboard.DashboardController;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.LoginScreenController;

public class ChangePINController implements Initializable {

    @FXML
    private TextField txtOldPIN;
    @FXML
    private TextField txtNewPIN;
    @FXML
    private TextField txtRetypeNewPIN;
    
    DashboardController d = new DashboardController();
    
    @FXML
    private void changePIN(MouseEvent event){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=? AND PIN=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    ps.setString(2, txtOldPIN.getText());
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                        if( !(txtNewPIN.getText().isEmpty()) ){
                            if(txtNewPIN.getText().equals(txtRetypeNewPIN.getText())){
                                    String query1 = "UPDATE customer SET PIN=? WHERE AccountNo=?";
                                    ps = con.prepareStatement(query1);
                                    ps.setString(1, txtNewPIN.getText());
                                    ps.setInt(2, LoginScreenController.accNo);
                                    int i =  ps.executeUpdate();
                                    if(i>0){
                                           Alert a = new Alert(Alert.AlertType.INFORMATION);
                                           a.setTitle("Change PIN");
                                           a.setHeaderText("PIN Changed Successfully.");
                                           a.setContentText("Your PIN has been successfully changed now you have to login again with new PIN");
                                           a.showAndWait();
                                           txtOldPIN.setText("");
                                           txtNewPIN.setText("");
                                           txtRetypeNewPIN.setText("");
                                           d.logout(event);
                                    }       
                            }
                            else{
                                    Alert a = new Alert(Alert.AlertType.ERROR);
                                    a.setTitle("Change PIN");
                                    a.setHeaderText("Error in Changing PIN.");
                                    a.setContentText("Enter the same PIN to confirm.");
                                    a.showAndWait();
                            }
                        }
                        else{
                           Alert a = new Alert(Alert.AlertType.ERROR);
                           a.setTitle("Change PIN");
                           a.setHeaderText("Error in Changing PIN.");
                           a.setContentText("Please!! Enter the new PIN.");
                           a.showAndWait();
                        } 
              
                   }
                   else{
                           Alert a = new Alert(Alert.AlertType.ERROR);
                           a.setTitle("Change PIN");
                           a.setHeaderText("Error in Changing PIN.");
                           a.setContentText("Invalid Old PIN.");
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
