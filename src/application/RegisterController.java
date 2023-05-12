package application;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.ToggleGroup;
import java.sql.*;

public class RegisterController implements Initializable {   
	@FXML
    private TextField usernameField, emailIdField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;
    
    @FXML
    private RadioButton femaleRadioButton, maleRadioButton;
    
    @FXML
    private TextArea addressField;
    
    @FXML
    private Spinner<Integer> year;
    final int INITIAL_VALUE_YEAR = 2000;
    SpinnerValueFactory<Integer> yearLength = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2040, INITIAL_VALUE_YEAR);
    
    @FXML
    private Spinner<Integer> month;
    final int INITIAL_VALUE_MONTH = 1;
    SpinnerValueFactory<Integer> monthLength = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, INITIAL_VALUE_MONTH);
    
    @FXML
    private Spinner<Integer> date;
    final int INITIAL_VALUE_DATE = 1;
    SpinnerValueFactory<Integer> dateLength = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, INITIAL_VALUE_DATE);
    
    @FXML
    private Spinner<String> dateOfBirthSpinner;
    
    @FXML
    private CheckBox termsAndConditionsCheckbox;
    
    @FXML
    private Stage loginStage;
    
    @FXML
    void handleLoginPageLink(ActionEvent event) {
    	try {
    		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
    		 loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		 loginStage.setTitle("SushiJei - Login");
    		 loginStage.setScene(new Scene(root, 620, 364));
    		 loginStage.show();
       	 } catch (Exception err) {
       		 err.printStackTrace();
       		 err.getCause();
       	 }
    }
    
    @FXML
    private Button registerButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	year.setValueFactory(yearLength);
    	year.setEditable(true);
    	month.setValueFactory(monthLength);
    	month.setEditable(true);
    	date.setValueFactory(dateLength);
    	date.setEditable(true);
    }
    
    
    @FXML
    public void register(ActionEvent event) throws SQLException {
    	Window owner = registerButton.getScene().getWindow();
    	
    	String username = usernameField.getText();
    	String email = emailIdField.getText();
    	String password = passwordField.getText();
    	String confirmPassword = confirmPasswordField.getText();
    	Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
    	String address = addressField.getText();
    	String genderData;
    	LocalDate birthDate = null;
    	String roleUser = "Member";
    	
    	if (username.length() < 5 || username.length() > 20) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Username must be between 5 - 20 characters!");
            return;
    	}
    	
    	if (username.contains(" ")) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Username must not contain any spaces!");
            return;
    	}
    	
    	if (!isValidEmail(email)) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Email must be in valid format!");
            return;
    	}
    	
    	if (password.length() < 8 || password.length() > 20) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Password must 8 - 20 length of character!");
            return;
    	}
    	
    	if (!pattern.matcher(password).matches()) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Password must be alphanumeric!");
            return;
    	}
    	
    	if (!password.equals(confirmPassword)) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Confirm Password must be equal to Password!");
            return;
    	}
    	
    	ToggleGroup genderToggleGroup = new ToggleGroup();
    	femaleRadioButton.setToggleGroup(genderToggleGroup);
    	maleRadioButton.setToggleGroup(genderToggleGroup);
    	
    	if (genderToggleGroup.getSelectedToggle() == null) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Gender must be selected!");
            return;
    	} else {
    		RadioButton selectedRadioButton = (RadioButton) genderToggleGroup.getSelectedToggle();
    		genderData = selectedRadioButton.getText();
    	}
    	
    	if (!address.endsWith(" Street")) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Address must end with ‘ Street’!");
            return;
    	}
    	
    	int yearData = year.getValue();
    	int monthData = month.getValue();
    	int dateData = date.getValue();
    	
    	boolean isValidDate = true;
    	try {
    		birthDate = LocalDate.of(yearData, monthData, dateData);
    	} catch(DateTimeException ex) {
            isValidDate = false;
        }
    	
    	if (!isValidDate) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Date of Birth must be in valid format!");
            return;
    	}
    	
    	Date dob = Date.valueOf(birthDate);
    	
    	if (!termsAndConditionsCheckbox.isSelected()) {
    		showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Terms and Condition must be checked!");
            return;
    	}
    	
        String lastID = "";
        
        String query = "select UserId from user ORDER BY UserId DESC LIMIT 1";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sushijei", "root", "#21012123Op");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                lastID = rs.getString("UserId");
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        int lastNum = 0;
        Pattern patternData = Pattern.compile("^US(\\d{3})$");
        Matcher matcher = patternData.matcher(lastID);
        if (matcher.matches()) {
            lastNum = Integer.parseInt(matcher.group(1));
        }

        int newNum = lastNum + 1;

        String newUserID = String.format("US%03d", newNum);
                
        JdbcDao jdbcDao = new JdbcDao();
        boolean flag = jdbcDao.registerUser(newUserID, username, password, email, address, genderData, dob, roleUser);
        
        if (flag) {
        	try {
       		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
       		 loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
       		 loginStage.setTitle("SushiJei - Login");
       		 loginStage.setScene(new Scene(root, 620, 364));
       		 loginStage.show();
          	 } catch (Exception err) {
          		 err.printStackTrace();
          		 err.getCause();
          	 }
        }
    }
    
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[^@.]+@[^@.]+\\.[^@.]+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        if (email.startsWith("@") || email.startsWith(".") || email.endsWith("@") || email.endsWith(".")) {
            return false;
        }
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        if (email.indexOf(".") < email.indexOf("@")) {
            return false;
        }
        if (email.indexOf(".") == email.indexOf("@") + 1 || email.indexOf(".") == email.indexOf("@") - 1) {
            return false;
        }
        return true;
    }
}
