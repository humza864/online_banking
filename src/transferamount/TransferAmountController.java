
package transferamount;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.LoginScreenController;


public class TransferAmountController implements Initializable {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
            
    @FXML
    private Label lblAccountNo;
    @FXML
    private Label lblBalance;
    @FXML
    private TextField txtAccountNo;
    @FXML
    private TextField txtPIN;
    @FXML
    private TextField txtAmount;
    
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR);
    int minute = cal.get(Calendar.MINUTE);
    int second = cal.get(Calendar.SECOND);
    int dayORNight = cal.get(Calendar.AM_PM);
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String date = dateFormat.format(d);
    
    LocalTime localTime = LocalTime.now();
    DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time = localTime.format(dt);
    
    
    @FXML
    private void transferButton(MouseEvent event){
         if( !(txtAmount.getText().isEmpty()) ){   
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=? AND PIN=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    ps.setString(2, txtPIN.getText());
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                        if( !(String.valueOf(rs.getInt("AccountNo")).equals(txtAccountNo.getText())) ){
                            double transferAmount = Double.parseDouble(txtAmount.getText());
                            double tax = getTax(transferAmount);
                            double totalAmount = Double.parseDouble(lblBalance.getText());
                            if(transferAmount+tax>totalAmount){
                                    Alert a = new Alert(Alert.AlertType.ERROR);
                                    a.setTitle("Transfer Amount");
                                    a.setHeaderText("Error in Transfer Amount");
                                    a.setContentText("You don't have enough balance. Transfer Again!!");
                                    a.showAndWait();
                            }
                            else{
                                    double remAmount = (totalAmount - transferAmount)-tax;

                                    String query2 = "SELECT * FROM customer WHERE AccountNo=?";
                                    ps = con.prepareStatement(query2);

                                    ps.setInt(1, Integer.parseInt(txtAccountNo.getText()));
                                    rs = ps.executeQuery();
                                    if(rs.next()){
                                            double transferedAmount = Double.parseDouble(txtAmount.getText());
                                            double currentAmount = Double.parseDouble(rs.getString("Balance"));

                                            String query1 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                                            ps = con.prepareStatement(query1);
                                            ps.setDouble(1, remAmount);
                                            ps.setInt(2, LoginScreenController.accNo);
                                            int j =  ps.executeUpdate();
                                            if(j>0){
                                                   lblBalance.setText(String.valueOf(remAmount));
                                            }  

                                            double totAmount = currentAmount + transferedAmount;
                                            String query3 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                                            ps = con.prepareStatement(query1);
                                            ps.setDouble(1, totAmount);
                                            ps.setInt(2, Integer.parseInt(txtAccountNo.getText()));
                                            int i =  ps.executeUpdate();
                                            if(i>0){
                                                Alert a = new Alert(Alert.AlertType.INFORMATION);
                                                a.setTitle("Tranfer Amount");
                                                a.setHeaderText("Amount Transfered Successfully.");
                                                a.setContentText("Amount "+transferedAmount+" has been added to "+txtAccountNo.getText());
                                                a.showAndWait();
                                            }


                                            String sql2 = "INSERT INTO transferamount (AccountNo,Amount,SendTo,Date,Time,Tax) VALUES (?,?,?,?,?,?)";
                                            ps = con.prepareStatement(sql2);
                                            ps.setInt(1 , LoginScreenController.accNo);
                                            ps.setDouble(2 , transferAmount);
                                            ps.setInt(3 , Integer.parseInt(txtAccountNo.getText()));
                                            ps.setString(4 , date);
                                            ps.setString(5 , time);
                                            ps.setDouble(6,tax);

                                            int k = ps.executeUpdate();
                                                 if(k>0){
                                                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                                                        a.setTitle("Tranfer Amount");
                                                        a.setHeaderText("Amount Transfered Successfully.");
                                                        a.setContentText("Amount "+transferAmount+" has been successfully transfered.\nTax: "+tax+"\nCurrent Balance: "+remAmount);
                                                        a.showAndWait();
                                                        txtAccountNo.setText("");
                                                        txtAmount.setText("");
                                                        txtPIN.setText("");
                                                        
                                                        if(getNoOfTransactions()%10==0){
                                                            double newAmount=0.0;
                                                            String query4 = "SELECT Balance FROM customer WHERE AccountNo=?";
                                                            ps = con.prepareStatement(query4);
                                                            ps.setInt(1, LoginScreenController.accNo);
                                                            rs = ps.executeQuery();
                                                            if(rs.next()){
                                                                   newAmount = rs.getDouble("Balance") + 50;
                                                            }

                                                            String query5 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                                                            ps = con.prepareStatement(query5);
                                                            ps.setDouble(1, newAmount);
                                                            ps.setInt(2, LoginScreenController.accNo);
                                                            int bonus=ps.executeUpdate();
                                                            if(bonus>0){
                                                                lblBalance.setText(String.valueOf(newAmount));
                                                                
                                                                String query6 = "INSERT INTO bonus (AccountNo,Month,Year,Bonus) VALUES(?,?,?,?)";
                                                                ps = con.prepareStatement(query6);
                                                                ps.setInt(1 , LoginScreenController.accNo);
                                                                ps.setInt(2 , month+1);
                                                                ps.setInt(3 , year);
                                                                ps.setDouble(4 , 50.0);

                                                                int l = ps.executeUpdate();
                                                                if(l>0){
                                                                   Alert aa = new Alert(Alert.AlertType.INFORMATION);
                                                                   aa.setTitle("Bonus");
                                                                   aa.setHeaderText("Bonus");
                                                                   aa.setContentText("Congratulations! You have received a bonus of $50 due to large no. of transactions in current month.");
                                                                   aa.showAndWait();
                                                                }
                                                            }   
                                            
                                                        }
                                                 }
                                    }
                                    else{
                                            Alert a = new Alert(Alert.AlertType.ERROR);
                                            a.setTitle("Tranfer Amount");
                                            a.setHeaderText("Error in Transfer Amount");
                                            a.setContentText("Invalid Account No. Please!! Enter correct Account No to tranfer your amount.");
                                            a.showAndWait();
                                    }          
                            }
                    
                       }
                        else{
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Transfer Amount");
                            a.setHeaderText("Error in Transfer Amount");
                            a.setContentText("Amount can not be transfered to your own account.");
                            a.showAndWait();
                        }
                        
                    }   
                    else{
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Transfer Amount");
                        a.setHeaderText("Error in Transfer Amount");
                        a.setContentText("Invalid PIN. Please!! Enter correct PIN to transfer amount.");
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
         else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Transfer Amount");
                a.setHeaderText("Error in Transfer Amount");
                a.setContentText("Please!! Fill all the fields.");
                a.showAndWait();
         }
    }
    
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
                        lblAccountNo.setText(rs.getString("AccountNo"));
                        lblBalance.setText(String.valueOf(rs.getDouble("Balance")));
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
    private void searchButton(MouseEvent event){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, Integer.parseInt(txtAccountNo.getText()));
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                               Alert a = new Alert(Alert.AlertType.INFORMATION);
                               a.setTitle("Account Information");
                               a.setHeaderText("Below is the information of account.");
                               a.setContentText("Account No: "+rs.getString("AccountNo")+"\nName: "+rs.getString("Name")+"\nMobile No: "+rs.getString("MobileNo"));
                               a.showAndWait();
                           
                       
                           
                    }
                    else{
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Search");
                        a.setHeaderText("Search Account");
                        a.setContentText("Invalid Account No. Try Again!!");
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
    
    public int getNoOfTransactions(){
        int noOfTransactions=0;
        int accountNo = LoginScreenController.accNo;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    
                    String query1 = "SELECT COUNT(Date) FROM deposit WHERE AccountNo=? AND YEAR(CURDATE())=YEAR(Date) AND MONTH(CURDATE())=MONTH(Date)";        
                    ps = con.prepareStatement(query1);
                    ps.setInt(1, accountNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                            //System.out.println(rs.getString(1));
                            noOfTransactions+=rs.getInt(1);
                    }
                    
                    
                    String query2 = "SELECT COUNT(Date) FROM withdraw WHERE AccountNo=? AND YEAR(CURDATE())=YEAR(Date) AND MONTH(CURDATE())=MONTH(Date)";        
                    ps = con.prepareStatement(query2);
                    ps.setInt(1, accountNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                            //System.out.println(rs.getString(1));
                            noOfTransactions+=rs.getInt(1);
                    }
                    
                    
                    String query3 = "SELECT COUNT(Date) FROM transferamount WHERE AccountNo=? AND YEAR(CURDATE())=YEAR(Date) AND MONTH(CURDATE())=MONTH(Date)";        
                    ps = con.prepareStatement(query3);
                    ps.setInt(1, accountNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                            //System.out.println(rs.getString(1));
                            noOfTransactions+=rs.getInt(1);
                    }
                    
                    
        }
        catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error Message");
                    a.setContentText(String.valueOf(e));  
                    a.showAndWait();
                    
         }
         return noOfTransactions;       
    }
    
    public double getTax(double amount){
        double tax=0.0;
        if(amount>=20000){
            tax = (amount/100) * 2;
        }
        return tax;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            setInfo();
    }    
    
}
