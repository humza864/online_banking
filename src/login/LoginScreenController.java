
package login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class LoginScreenController implements Initializable {
    public static Stage stage=null;
    public static int accNo;
    private boolean locked=false;

    @FXML
    private Pane loginPane;
    @FXML
    private TextField txtAccountNo;
    @FXML
    private TextField txtPIN;
    
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR);
    int minute = cal.get(Calendar.MINUTE);
    int second = cal.get(Calendar.SECOND);
    int dayORNight = cal.get(Calendar.AM_PM);
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String date = dateFormat.format(d);
    
    LocalTime localTime = LocalTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time = localTime.format(dtf);
    
    
    @FXML
    private void closeApp(MouseEvent event) {
            Platform.exit();
            System.exit(0);
    }
     @FXML
    private void recoverPassword(MouseEvent event) throws IOException{
            Parent fxml = FXMLLoader.load(getClass().getResource("/forgotpass/ForgotPassword.fxml"));
            loginPane.getChildren().removeAll();
            loginPane.getChildren().addAll(fxml);
    }
    @FXML
    private void createAccount(MouseEvent event) throws IOException{
            Parent fxml = FXMLLoader.load(getClass().getResource("/createaccount/CreateAccount.fxml"));
            loginPane.getChildren().removeAll();
            loginPane.getChildren().addAll(fxml);
    }

    @FXML
    public void loginAccount(MouseEvent event){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=? AND PIN=?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, Integer.parseInt(txtAccountNo.getText()));
                    ps.setString(2, txtPIN.getText());
                    accNo = Integer.parseInt(txtAccountNo.getText());
                    rs = ps.executeQuery();
                    if(rs.next()){
                        locked=checkLockAccount();
                        if(!locked){
                            ( (Node) event.getSource() ).getScene().getWindow().hide();   //The dashboard will hide
                            Parent root = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
                            Scene scene = new Scene(root);
                            scene.getStylesheets().add(getClass().getResource("/design/design.css").toExternalForm());
                            Stage stage = new Stage();
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.setScene(scene);
                            stage.show();
                            this.stage=stage;
                            
                        }
                        else{
                            String amount = JOptionPane.showInputDialog(null, "Your account has been locked.\nDeposit some amount to unlock.\n"+
                                   "Amount must be >=500", "0.0");
                            if(amount!=null){
                                double deposit = Double.parseDouble(amount);
                                if(deposit>=500){
                                    String query2 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                                    ps=con.prepareStatement(query2);
                                    ps.setDouble(1, deposit);
                                    ps.setInt(2,accNo);
                                    int i = ps.executeUpdate();
                                    if(i>0){
                                        String query3="INSERT INTO deposit (AccountNo,DepositAmount,NewAmount,Date,Time) VALUES (?,?,?,?,?)";
                                        ps = con.prepareStatement(query3);
                                        ps.setInt(1 , accNo);
                                        ps.setDouble(2 , deposit);
                                        ps.setDouble(3 , deposit);
                                        ps.setString(4 , date);
                                        ps.setString(5 , time);

                                        int j = ps.executeUpdate();
                                        if(j>0){
                                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                                            a.setTitle("Unlock Account");
                                            a.setHeaderText("Deposit Successfull");
                                            a.setContentText("You can now login to your account");
                                            a.showAndWait();
                                        }
                                    }
                                }
                                else{
                                    Alert a = new Alert(Alert.AlertType.ERROR);
                                    a.setTitle("Unlock Account");
                                    a.setHeaderText("Error in deposit");
                                    a.setContentText("Amount must be >=500");
                                    a.showAndWait();
                                }
                            }
                        }
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
                    a.setHeaderText("Error in Login");
                    a.setContentText("There is some technical issue." + e.toString());
                    a.showAndWait();
                    
            }
    }
    
    public boolean checkLockAccount(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String date1 = null;
            String date2 = null;
            int dayDiff=0;
            double balance=0.0;
            boolean lock = false;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT max(Date) FROM withdraw WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, accNo);
                    rs=ps.executeQuery();
                    if(rs.next()){
                        date1 = rs.getString(1);
                        //System.out.println(date1);
                    }
                    
                    String query2="SELECT max(Date) FROM transferamount WHERE AccountNo=?";
                    ps = con.prepareStatement(query2);
                    ps.setInt(1, accNo);
                    rs=ps.executeQuery();
                    if(rs.next()){
                        date2 = rs.getString(1);
                        //System.out.println(date2);
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    if(date1!=null && date2!=null){
                        Date withdrawDate = sdf.parse(date1);
                        Date transferamountDate = sdf.parse(date2);

                        if(withdrawDate.compareTo(transferamountDate)>=0){
                            String query3="SELECT DATEDIFF(CURDATE(),max(Date)) FROM withdraw WHERE AccountNo=?";
                            ps = con.prepareStatement(query3);
                            ps.setInt(1, accNo);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                dayDiff = rs.getInt(1);
                                //System.out.println(dayDiff);
                            }

                        }
                        if(transferamountDate.compareTo(withdrawDate)>=0){                         
                            String query4="SELECT DATEDIFF(CURDATE(),max(Date)) FROM transferamount WHERE AccountNo=?";
                            ps = con.prepareStatement(query4);
                            ps.setInt(1, accNo);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                dayDiff = rs.getInt(1);
                                //System.out.println(dayDiff);
                            }
                        }
  
                    }
                    else if(date1!=null && date2==null){ 
                        String query1="SELECT DATEDIFF(CURDATE(),max(Date)) FROM withdraw WHERE AccountNo=?";
                        ps = con.prepareStatement(query1);
                        ps.setInt(1, accNo);
                        rs = ps.executeQuery();
                        if(rs.next()){
                               dayDiff = rs.getInt(1);
                               //System.out.println(dayDiff);
                        }
                    }
                    else if(date2!=null && date1==null){
                        String query4="SELECT DATEDIFF(CURDATE(),max(Date)) FROM transferamount WHERE AccountNo=?";
                        ps = con.prepareStatement(query4);
                        ps.setInt(1, accNo);
                        rs = ps.executeQuery();
                        if(rs.next()){
                              dayDiff = rs.getInt(1);
                              //System.out.println(dayDiff);
                        }
                    }
                    
                    String query1="SELECT Balance FROM customer WHERE AccountNo=?";
                    ps = con.prepareStatement(query1);
                    ps.setInt(1, accNo);
                    rs = ps.executeQuery();
                    if(rs.next()){
                            balance = rs.getDouble(1);
                            //System.out.println(balance);
                    }
                    
                    if(dayDiff>=90 && balance==0.0){
                           lock=true;
                    }
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Login");
                    a.setContentText("There is some technical issue." + e.toString());
                    a.showAndWait();
                    
            }
            
            return lock;
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          //Todo here
    }    
    
}
