
package dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.BankingApplication;
import login.LoginScreenController;
import static login.LoginScreenController.accNo;


public class DashboardController implements Initializable {
    
    //public static String accountNo = LoginScreenController.accNo;
    
    private double xoffset = 0.0;
    private double yoffset = 0.0;

    @FXML
    private Circle profilePic;
    @FXML
    private Text userName;
    @FXML
    private FontAwesomeIconView ico;
    @FXML
    private Pane dashboardMain;
    
    @FXML
    private void closeApp(MouseEvent event) {
            Platform.exit();
            System.exit(0);
    }
    
    @FXML
    private void minimizeApp(MouseEvent event) {
            Stage stage = (Stage) ico.getScene().getWindow();
            stage.setIconified(true);
    }
    
    public void setData(){
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
                        userName.setText(rs.getString("Name"));
                        InputStream is = rs.getBinaryStream("ProfilePic");
                        OutputStream os = new FileOutputStream(new File("pic.jpg"));  //pic.jpg is temporary picture which is automaticall created by compiler
                        byte []content=new byte[1024];
                        int size=0;
                        while( (size=is.read(content)) != -1 ){
                                os.write(content,0,size); //temporary image jo bnayi thi us mn humari set hujyegi ab 
                        }
                        os.close();
                        is.close();
                        
                        Image img = new Image("file:pic.jpg",false);  
                        profilePic.setFill(new ImagePattern(img));
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
    public void click(MouseEvent event){
            xoffset = event.getSceneX();
            yoffset = event.getSceneY();
    }
    @FXML
    public void drag(MouseEvent event){
            LoginScreenController.stage.setX(event.getScreenX() - xoffset);
            LoginScreenController.stage.setY(event.getScreenY() - yoffset);
    }
    @FXML
    private void accountInformation(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/accountinfo/AccountInformation.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
    }
    @FXML
    public void mainScreen() throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
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
    @FXML
    public void logout(MouseEvent event) throws IOException{
            ( (Node) event.getSource() ).getScene().getWindow().hide();   //The dashboard will hide
            Parent root = FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/design/design.css").toExternalForm());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
            
            root.setOnMousePressed(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                    xoffset = event.getSceneX();
                    yoffset = event.getSceneY();
            }
                
        });
        
         root.setOnMouseDragged(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xoffset);
                    stage.setY(event.getScreenY() - yoffset);
            }
                
        });
            
    }
    @FXML
    private void pinChange(MouseEvent event) throws IOException {
            Parent fxml = FXMLLoader.load(getClass().getResource("/changepin/ChangePIN.fxml"));
            dashboardMain.getChildren().removeAll();
            dashboardMain.getChildren().addAll(fxml);
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
            setData();
            try {
                mainScreen();
            } 
            catch (IOException ex) {
                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }    
    
}
