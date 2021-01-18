
package createaccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import login.BankingApplication;

public class CreateAccountController implements Initializable {
    
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtICN;
    @FXML
    private TextField txtMobileNo;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtAccountNo;
    @FXML
    private TextField txtBalance;
    @FXML
    private TextField txtPIN;
    @FXML
    private TextField txtAnswer;
    @FXML
    private DatePicker dpDOB;
    @FXML
    private ComboBox<String> cboxGender;
    @FXML
    private ComboBox<String> cboxMartialStatus;
    @FXML
    private ComboBox<String> cboxReligion;
    @FXML
    private ComboBox<String> cboxAccountType;
    @FXML
    private ComboBox<String> cboxSecurityQuestion;
    
    ObservableList<String> list1 = FXCollections.observableArrayList("Male","Female","Other");
    ObservableList<String> list2 = FXCollections.observableArrayList("Single","Married");
    ObservableList<String> list3 = FXCollections.observableArrayList("Islam","Christian","Hindu","Other");
    ObservableList<String> list4 = FXCollections.observableArrayList("Saving","Current");
    ObservableList<String> list5 = FXCollections.observableArrayList("What is your nickname?","What is your Childhood town?","What is your lucky number?");
    
    private FileChooser fileChooser = new FileChooser();
    private File file;
    private FileInputStream fis;
    @FXML
    private ImageView profilePic;
    
    public void setupProfilePic(MouseEvent evt){
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Images Files","*.png","*.jpg","*.jpeg"));
            file = fileChooser.showOpenDialog(null);
            if(file!=null){
                    Image img = new Image(file.toURI().toString(), 150, 150, true, true);
                    profilePic.setImage(img);
                    profilePic.setPreserveRatio(true);
            }
    }
    @FXML
    private void backToLogin(MouseEvent evt) throws IOException{
            BankingApplication.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }
    @FXML
    private void closeApp(MouseEvent evt){
            Platform.exit();
            System.exit(0);
    }
    
    //Creating my own method for checking already existing account number
    public int getAccNo(){
            Connection con= null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            int an=0;
            try{
                 Class.forName("com.mysql.cj.jdbc.Driver");
                 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                 String query = "SELECT AccountNo FROM customer";
                 ps = con.prepareStatement(query);
                 rs = ps.executeQuery();
                 if(rs.next()){
                        an = rs.getInt("AccountNo");
                 }
                 return an;
                 
            }
            catch(Exception e){
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in creating account.");
                a.setContentText("Your account is not created. There is some technical issue." + e.getMessage());
                a.showAndWait();
                return 0;
            }
            
    }
    //Creating my own method for checking already existing Id card no number
    public String getIDCardNo(){
            Connection con= null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String s = null;
            try{
                 Class.forName("com.mysql.cj.jdbc.Driver");
                 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                 String query = "SELECT ICN FROM customer";
                 ps = con.prepareStatement(query);
                 rs = ps.executeQuery();
                  if(rs.next()){
                        s = rs.getString("ICN");
                        System.out.println(s);
                 }
                 return s;
            }
            catch(Exception e){
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in creating account.");
                a.setContentText("Your account is not created. There is some technical issue." + e.getMessage());
                a.showAndWait();
                return null;
            }
    }
    
    public boolean validateName(){
            Pattern p = Pattern.compile("[a-z A-z]+");
            Matcher m = p.matcher(txtName.getText());
            if(m.find() && m.group().equals(txtName.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in Name.");
                a.setContentText("Invalid Name");
                a.showAndWait();
                return false;
            }
    }
    public boolean validateMobileNo(){
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(txtMobileNo.getText());
            if(m.find() && m.group().equals(txtMobileNo.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in Mobile No.");
                a.setContentText("Invalid Mobile No.");
                a.showAndWait();
                return false;
            }
    }
    public boolean validatePIN(){
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(txtPIN.getText());
            if(m.find() && m.group().equals(txtPIN.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in PIN");
                a.setContentText("PIN cannot contain any character.");
                a.showAndWait();
                return false;
            }
    }
    public boolean validateICN(){
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(txtICN.getText());
            if(m.find() && m.group().equals(txtICN.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in ICN.");
                a.setContentText("Invalid ID Card No.");
                a.showAndWait();
                return false;
            }
    }
    public boolean validateCity(){
            Pattern p = Pattern.compile("[a-z A-z]+");
            Matcher m = p.matcher(txtCity.getText());
            if(m.find() && m.group().equals(txtCity.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in City.");
                a.setContentText("Invalid City Name");
                a.showAndWait();
                return false;
            }
    }
    public boolean validateBalance(){
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(txtBalance.getText());
            if(m.find() && m.group().equals(txtBalance.getText())){
                return true;
            }
            else{
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in Balance.");
                a.setContentText("Invalid Balance.");
                a.showAndWait();
                return false;
            }
    }
    
    public void clearAllFields(){
            txtName.clear();
            txtICN.clear();
            txtMobileNo.clear();
            txtAddress.clear();
            txtCity.clear();
            txtAccountNo.clear();
            txtBalance.clear();
            txtAnswer.clear();
            txtPIN.clear();
            cboxGender.getSelectionModel().clearSelection();
            cboxMartialStatus.getSelectionModel().clearSelection();
            cboxReligion.getSelectionModel().clearSelection();
            cboxAccountType.getSelectionModel().clearSelection();
            cboxSecurityQuestion.getSelectionModel().clearSelection();
            dpDOB.getEditor().clear();
            Image img = new Image("/images/150x150.gif");
            profilePic.setImage(img);
            txtAccountNo.setText(String.valueOf(generateAccoutNo()));
    }
    
    public int generateAccoutNo(){
            Random rand = new Random();
            int num=rand.nextInt(899999) + 100000;
            return num;
    }
    
    public void newAccount(MouseEvent evt){
             if( !(txtName.getText().isEmpty()) && !(txtICN.getText().isEmpty()) && !(txtMobileNo.getText().isEmpty()) && !(txtCity.getText().isEmpty())
                && !(txtAddress.getText().isEmpty()) && !(txtAccountNo.getText().isEmpty()) && !(txtPIN.getText().isEmpty()) 
                && !(txtBalance.getText().isEmpty()) && !(txtAnswer.getText().isEmpty()) && !(( (TextField) dpDOB.getEditor() ).getText().isEmpty()) 
                && (cboxGender.getValue()!=null) && (cboxMartialStatus.getValue()!=null) && (cboxReligion.getValue()!=null) 
                && (cboxAccountType.getValue()!=null) && (cboxSecurityQuestion.getValue()!=null) && (file!=null)){
                 
                 if(validateName() && validateICN() && validateMobileNo() && validateCity() && validateBalance() && validatePIN()){
                 
                    Double balance = Double.parseDouble(txtBalance.getText());
                    if(balance>=500.0){

                        int accno = getAccNo();
                        String idcardno = getIDCardNo();
                        if( accno!=Integer.parseInt(txtAccountNo.getText()) && !(txtICN.getText().equals(idcardno)) ){
                            Connection con = null;
                            PreparedStatement ps = null;
                            try{
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                                String query = "INSERT INTO customer(Name,ICN,MobileNo,Gender,MartialStatus,Religion,DOB,City,Address,AccountNo,PIN,AccountType,Balance,SecurityQuestion,Answer,ProfilePic) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                ps = con.prepareStatement(query);

                                ps.setString(1, txtName.getText());
                                ps.setString(2, txtICN.getText());
                                ps.setString(3, txtMobileNo.getText());
                                ps.setString(4, cboxGender.getValue());
                                ps.setString(5, cboxMartialStatus.getValue());
                                ps.setString(6, cboxReligion.getValue());
                                ps.setString(7, ( (TextField) dpDOB.getEditor() ).getText() );
                                ps.setString(8, txtCity.getText());
                                ps.setString(9, txtAddress.getText());
                                ps.setInt(10, Integer.parseInt(txtAccountNo.getText()));
                                ps.setString(11, txtPIN.getText());
                                ps.setString(12, cboxAccountType.getValue());
                                ps.setDouble(13, Double.parseDouble(txtBalance.getText()));
                                ps.setString(14, cboxSecurityQuestion.getValue());
                                ps.setString(15, txtAnswer.getText());
                                fis = new FileInputStream(file);
                                ps.setBinaryStream(16, (InputStream)fis , (int)file.length());

                                int i = ps.executeUpdate();
                                if(i>0){
                                    Alert a = new Alert(AlertType.INFORMATION);
                                    a.setTitle("Account Creation");
                                    a.setHeaderText("Account Created Successfully");
                                    a.setContentText("Your account has been created successfully. You can now login with your AccountNo and PIN.");
                                    a.showAndWait();
                                    clearAllFields();
                                }
                                else{
                                    Alert a = new Alert(AlertType.ERROR);
                                    a.setTitle("Error");
                                    a.setHeaderText("Account not created");
                                    a.setContentText("Your account is not created. There is some error. Try Again!!");
                                    a.showAndWait();
                                }


                            }
                            catch(Exception e){
                                Alert a = new Alert(AlertType.ERROR);
                                a.setTitle("Error");
                                a.setHeaderText("Error in creating account.");
                                a.setContentText("Your account is not created. There is some technical issue." + e.getMessage());
                                a.showAndWait();

                            }
                        }
                        else{
                                Alert a = new Alert(AlertType.WARNING);
                                a.setTitle("Account Creation");
                                a.setHeaderText("Account Creation Failed.");
                                a.setContentText("Account No OR ICN already exists.");
                                a.showAndWait();
                                txtAccountNo.setText(String.valueOf(generateAccoutNo()));
                        }
                    } 
                    else{
                            Alert a = new Alert(AlertType.WARNING);
                            a.setTitle("Account Creation");
                            a.setHeaderText("Account Creation Failed.");
                            a.setContentText("Please!! Deposit amount (balance) greater or equal to 500.0(Five Hundred).");
                            a.showAndWait();
                    } 
                }
            } //end of main if
             else{
                    Alert a = new Alert(AlertType.WARNING);
                    a.setTitle("Account Creation");
                    a.setHeaderText("Account Creation Failed.");
                    a.setContentText("Please!! Fill all the fields to create your account.");
                    a.showAndWait();
             }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            cboxGender.setItems(list1);
            cboxMartialStatus.setItems(list2);
            cboxReligion.setItems(list3);
            cboxAccountType.setItems(list4);
            cboxSecurityQuestion.setItems(list5);
            txtAccountNo.setText(String.valueOf(generateAccoutNo()));
            txtAccountNo.setEditable(false);
            //deleteAfterTesting();
            
    }    
    
    
    /*private void deleteAfterTesting(){
            Connection con = null;
            PreparedStatement ps = null;
            try{
                 Class.forName("com.mysql.cj.jdbc.Driver");
                 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankappfx","root","Excellentmind123");
                 ps = con.prepareStatement("Delete From customer");
                 ps.executeUpdate();
                 ps = con.prepareStatement("Delete From withdraw");
                 ps.executeUpdate();
                 ps = con.prepareStatement("Delete From deposit");
                 ps.executeUpdate();
                 ps = con.prepareStatement("Delete From transferamount");
                 ps.executeUpdate();
                 
            }
            catch(Exception e){
                System.out.println(e);
            }
    }*/
    
}
