
package deposit;

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


public class DepositAmountController implements Initializable {

    //public static String accountNo = LoginScreenController.accNo;

    @FXML
    private Label lblAccountNo;
    @FXML
    private Label lblBalance;
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
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String date = dateFormat.format(d);
    
    LocalTime localTime = LocalTime.now();
    DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time = localTime.format(dt);
    
    
    @FXML
    private void depositButton(MouseEvent event){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM customer WHERE AccountNo=? AND PIN=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    ps.setString(2, txtPIN.getText());
                    
                    rs = ps.executeQuery();
                    if(rs.next()){
                        if( !(txtAmount.getText().isEmpty()) ){ 
                            double dpAmount = Double.parseDouble(txtAmount.getText());
                            double tax = getTax(dpAmount);
                            double currentAmount = Double.parseDouble(lblBalance.getText());

                            double totalAmount = (currentAmount + dpAmount)-tax;
                            String query1 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                            ps = con.prepareStatement(query1);
                            ps.setDouble(1, totalAmount);
                            ps.setInt(2, LoginScreenController.accNo);
                            int j =  ps.executeUpdate();
                            if(j>0){
                                  lblBalance.setText(String.valueOf(totalAmount));
                            }               

                            String sql2 = "INSERT INTO deposit (AccountNo,DepositAmount,NewAmount,Date,Time,Tax) VALUES (?,?,?,?,?,?)";
                            ps = con.prepareStatement(sql2);
                            ps.setInt(1 , LoginScreenController.accNo);
                            ps.setDouble(2 , dpAmount);
                            ps.setDouble(3 , totalAmount);
                            ps.setString(4 , date);
                            ps.setString(5 , time);
                            ps.setDouble(6, tax);
                        
                            int i = ps.executeUpdate();
                            if(i>0){
                                       Alert a = new Alert(Alert.AlertType.INFORMATION);
                                       a.setTitle("Deposit");
                                       a.setHeaderText("Deposit Amount");
                                       a.setContentText("Amount "+dpAmount+" has been successfully deposited.\nTax: "+tax+"\nCurrent Balance: "+totalAmount);
                                       a.showAndWait();
                                       txtAmount.setText("");
                                       txtPIN.setText("");
                                       
                                       if(getNoOfTransactions()%10==0){
                                            double newAmount=0.0;
                                            String query2 = "SELECT Balance FROM customer WHERE AccountNo=?";
                                            ps = con.prepareStatement(query2);
                                            ps.setInt(1, LoginScreenController.accNo);
                                            rs = ps.executeQuery();
                                            if(rs.next()){
                                                   newAmount = rs.getDouble("Balance") + 50;
                                            }

                                            String query3 = "UPDATE customer SET Balance=? WHERE AccountNo=?";
                                            ps = con.prepareStatement(query3);
                                            ps.setDouble(1, newAmount);
                                            ps.setInt(2, LoginScreenController.accNo);
                                            int bonus=ps.executeUpdate();
                                            if(bonus>0){
                                                lblBalance.setText(String.valueOf(newAmount));
                                                
                                                String query4 = "INSERT INTO bonus (AccountNo,Month,Year,Bonus) VALUES(?,?,?,?)";
                                                ps = con.prepareStatement(query4);
                                                ps.setInt(1 , LoginScreenController.accNo);
                                                ps.setInt(2 , month+1);
                                                ps.setInt(3 , year);
                                                ps.setDouble(4 , 50.0);
                                                
                                                int k = ps.executeUpdate();
                                                if(k>0){
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
                            a.setTitle("Deposit");
                            a.setHeaderText("Error in Deposit Amount");
                            a.setContentText("Please!! Enter some amount to deposit.");
                            a.showAndWait();
                        }
                        
                   }
                    else{
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Error");
                            a.setHeaderText("Error in Deposit");
                            a.setContentText("Invalid PIN. Please!! Enter correct PIN to deposit amount.");
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
                        lblAccountNo.setText(String.valueOf(rs.getInt("AccountNo")));
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
