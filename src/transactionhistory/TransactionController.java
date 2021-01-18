
package transactionhistory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import login.LoginScreenController;


public class TransactionController implements Initializable {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
            
    @FXML
    private TableView<History> withdrawTable;
    @FXML
    private TableColumn<History, String> withdrawAccountNo;
    @FXML
    private TableColumn<History, String> withdrawAmount;
    @FXML
    private TableColumn<History, String> withdrawRemAmount;
    @FXML
    private TableColumn<History, String> withdrawDate;
    @FXML
    private TableColumn<History, String> withdrawTime;
    @FXML
    private TableColumn<History, String> withdrawTax;
    
    
    @FXML
    private TableView<History> depositTable;
    @FXML
    private TableColumn<History, String> depositAccountNo;
    @FXML
    private TableColumn<History, String> depositAmount;
    @FXML
    private TableColumn<History, String> depositNewAmount;
    @FXML
    private TableColumn<History, String> depositDate;
    @FXML
    private TableColumn<History, String> depositTime;
    @FXML
    private TableColumn<History, String> depositTax;
    
    
    @FXML
    private TableView<History> transferTable;
    @FXML
    private TableColumn<History, String> transferAccountNo;
    @FXML
    private TableColumn<History, String> transferAmount;
    @FXML
    private TableColumn<History, String> sendTo;
    @FXML
    private TableColumn<History, String> transferDate;
    @FXML
    private TableColumn<History, String> transferTime;
    @FXML
    private TableColumn<History, String> transferTax;
    
    
    @FXML
    private TableView<History> receiveTable;
    @FXML
    private TableColumn<History, String> receiveAccountNo;  //Account no is user's receive is just for identifying the column is of receive table
    @FXML
    private TableColumn<History, String> receiveAmount;
    @FXML
    private TableColumn<History, String> receiveAmountFrom;
    @FXML
    private TableColumn<History, String> receiveDate;
    @FXML
    private TableColumn<History, String> receiveTime;
    
    public void withdraw(){
            withdrawAccountNo.setCellValueFactory(new PropertyValueFactory<History,String>("account"));
            withdrawAmount.setCellValueFactory(new PropertyValueFactory<History,String>("amount"));
            withdrawRemAmount.setCellValueFactory(new PropertyValueFactory<History,String>("generic"));
            withdrawDate.setCellValueFactory(new PropertyValueFactory<History,String>("date"));
            withdrawTime.setCellValueFactory(new PropertyValueFactory<History,String>("time"));
            withdrawTax.setCellValueFactory(new PropertyValueFactory<History,String>("tax"));
            
            
            ObservableList<History> list = FXCollections.observableArrayList();
            
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM withdraw WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                          list.add( new History( String.valueOf(rs.getInt("AccountNo")), String.valueOf(rs.getDouble("WithdrawAmount")), String.valueOf(rs.getDouble("RemainingAmount")),String.valueOf(rs.getDouble("tax")), rs.getString("Date"), rs.getString("Time") ) );
                    }
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Fetching Data.");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
            }
            
            withdrawTable.setItems(list);
    }
    
    public void deposit(){
            depositAccountNo.setCellValueFactory(new PropertyValueFactory<History,String>("account"));
            depositAmount.setCellValueFactory(new PropertyValueFactory<History,String>("amount"));
            depositNewAmount.setCellValueFactory(new PropertyValueFactory<History,String>("generic"));
            depositDate.setCellValueFactory(new PropertyValueFactory<History,String>("date"));
            depositTime.setCellValueFactory(new PropertyValueFactory<History,String>("time"));
            depositTax.setCellValueFactory(new PropertyValueFactory<History,String>("tax"));
            
            ObservableList<History> list = FXCollections.observableArrayList();
            
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM deposit WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                          list.add( new History( String.valueOf(rs.getInt("AccountNo")), String.valueOf(rs.getDouble("DepositAmount")), String.valueOf(rs.getDouble("NewAmount")), String.valueOf(rs.getDouble("tax")), rs.getString("Date"), rs.getString("Time")) );
                    }
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Fetching Data.");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
            }
            
            depositTable.setItems(list);
    }
    
    public void transfer(){
            transferAccountNo.setCellValueFactory(new PropertyValueFactory<History,String>("account"));
            transferAmount.setCellValueFactory(new PropertyValueFactory<History,String>("amount"));
            sendTo.setCellValueFactory(new PropertyValueFactory<History,String>("generic"));
            transferDate.setCellValueFactory(new PropertyValueFactory<History,String>("date"));
            transferTime.setCellValueFactory(new PropertyValueFactory<History,String>("time"));
            transferTax.setCellValueFactory(new PropertyValueFactory<History,String>("tax"));
            
            ObservableList<History> list = FXCollections.observableArrayList();
            
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM transferamount WHERE AccountNo=?";
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                          list.add( new History( String.valueOf(rs.getInt("AccountNo")), String.valueOf(rs.getDouble("Amount")), String.valueOf(rs.getInt("SendTo")), String.valueOf(rs.getDouble("tax")), rs.getString("Date"), rs.getString("Time")) );
                    }
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Fetching Data.");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
            }
            
            transferTable.setItems(list);
    }
    
    
    public void receive(){
            receiveAccountNo.setCellValueFactory(new PropertyValueFactory<History,String>("account"));
            receiveAmount.setCellValueFactory(new PropertyValueFactory<History,String>("amount"));
            receiveAmountFrom.setCellValueFactory(new PropertyValueFactory<History,String>("generic"));
            receiveDate.setCellValueFactory(new PropertyValueFactory<History,String>("date"));
            receiveTime.setCellValueFactory(new PropertyValueFactory<History,String>("time"));
            
            ObservableList<History> list = FXCollections.observableArrayList();
            
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                    String query = "SELECT * FROM transferamount WHERE SendTo=?";   //is mein send to mn humara account no huga
                    ps = con.prepareStatement(query);
                    
                    ps.setInt(1, LoginScreenController.accNo);
                    
                    rs = ps.executeQuery();
                    while(rs.next()){
                          list.add( new History( String.valueOf(rs.getInt("SendTo")), String.valueOf(rs.getDouble("Amount")), String.valueOf(rs.getInt("AccountNo")), rs.getString("Date"), rs.getString("Time")) );
                    }
            }
            catch(Exception e){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Fetching Data.");
                    a.setContentText("There is some technical issue." + e.getMessage());
                    a.showAndWait();
            }
            
            receiveTable.setItems(list);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            withdraw();
            deposit();
            transfer();
            receive();
    }    
    
}
