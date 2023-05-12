package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.models.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailIdField;
 
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;
    
    @FXML
    private Stage registerStage;
     
    @FXML
    private Stage homepageStage;
    private Parent root;
    
    String userID, roleUser;
    public User user;
    
    @FXML
    void handleRegisterPageLink(ActionEvent event) {
    	try {
    		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
    		 registerStage = (Stage)((Node)event.getSource()).getScene().getWindow();
           	 registerStage.setTitle("SushiJei - Register");
           	 registerStage.setScene(new Scene(root, 620, 600));
           	 registerStage.show();
       	 } catch (Exception err) {
       		 err.printStackTrace();
       		 err.getCause();
       	 }
    }
    
  
    @FXML
    public void login(ActionEvent event) throws IOException, SQLException {
        Window owner = submitButton.getScene().getWindow();

        System.out.println(emailIdField.getText());
        System.out.println(passwordField.getText());

        if (emailIdField.getText().isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "All field must be filled!");
            return;
        }
        
        if (passwordField.getText().isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "All field must be filled!");
            return;
        }

        String emailId = emailIdField.getText();
        String username = emailIdField.getText();
        String password = passwordField.getText();

        JdbcDao jdbcDao = new JdbcDao();
        boolean flag = jdbcDao.validate(emailId, username, password);
        
        String query = "SELECT UserId, Role FROM user where Email = ? or Username = ?";
		String DATABASE_URL = "jdbc:mysql://localhost:3306/sushijei?useSSL=false";
	    String DATABASE_USERNAME = "root";
	    String DATABASE_PASSWORD = "#21012123Op";
		try (Connection connection = DriverManager
	            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, emailId);
	            preparedStatement.setString(2, username);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	            	userID = resultSet.getString("UserId"); 
	            	roleUser = resultSet.getString("Role");
	            }
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }

        if (!flag) {
        	showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Username or password is incorrect!");
            return;
        }
        
        if ("Member".equals(roleUser)) {
        	 user = new User(userID);
        	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        	 root = loader.load();
        	 HomepageController homepageController = loader.getController();
        	 homepageController.initData(user);
      		 homepageStage = (Stage)((Node)event.getSource()).getScene().getWindow();
      		 homepageStage.setTitle("SushiJei - Home");
      		 homepageStage.setScene(new Scene(root, 900, 570));
      		 homepageStage.show();
        } else if ("Admin".equals(roleUser)) {
	       	 user = new User(userID);
	       	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminpage.fxml"));
	       	 root = loader.load();
	       	 AdminpageController adminpageController = loader.getController();
	       	 adminpageController.initData(user);
	     	 homepageStage = (Stage)((Node)event.getSource()).getScene().getWindow();
     		 homepageStage.setTitle("SushiJei - Home");
     		 homepageStage.setScene(new Scene(root, 900, 570));
     		 homepageStage.show();
        }
    }
    
    public String getUserId() {
        return this.user.getUserId();
    }

	private static void showAlert(Alert.AlertType alertType, Window owner, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
