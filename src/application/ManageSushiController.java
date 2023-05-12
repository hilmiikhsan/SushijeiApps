package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.models.sushi.Sushi;
import application.models.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ManageSushiController {
	@FXML
	private TableView<Sushi> sushiTable;
	
	@FXML
	private TableColumn<Sushi, String> idCol;
	
	@FXML
	private TableColumn<Sushi, String> nameCol;
	
	@FXML
	private TableColumn<Sushi, String> descCol;
	
	@FXML
	private TableColumn<Sushi, Integer> priceCol;
	
	@FXML
	private TableColumn<Sushi, Integer> stockCol;

	@FXML
    private Stage adminStage;

	@FXML 
	private MenuItem logoutMenu, manageSushiMenu;

	@FXML
    private TextField sushiIdTextField, nameTextField, descriptionTextField, priceTextField, stockTextField;
	
	@FXML
    private Button insertButton, updateButton, deleteButton;
	
	String userId;
	private Parent root;
	User selectUser;
	
	public void initData(User user) {
		selectUser = user;
		userId = selectUser.getUserId();
		loadData();
	}
	
	@FXML
    void handleLogoutPageLink(Event event) { 
    	try {
    		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
    		 adminStage = (Stage) logoutMenu.getParentPopup().getOwnerWindow();
    		 adminStage.setTitle("SushiJei - Home");
    		 adminStage.setScene(new Scene(root, 620, 364));
    		 adminStage.show();
       	 } catch (Exception err) {
       		 err.printStackTrace();
       		 err.getCause();
       	 }
    }
	
	@FXML
    void handlePageManageSushi(ActionEvent event) {
		try {
 		 	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewcart.fxml"));
	       	 root = loader.load();
	       	 ManageSushiController manageController = loader.getController();
	       	 manageController.initData(selectUser);
	       	 adminStage = (Stage) manageSushiMenu.getParentPopup().getOwnerWindow();
	       	 adminStage.setTitle("SushiJei - Home");
	       	 adminStage.setScene(new Scene(root, 900, 570));
	       	 adminStage.show();
    	 } catch (Exception err) {
    		 err.printStackTrace();
    		 err.getCause();
    	 }
    }
	
	ObservableList<Sushi> SushiList = FXCollections.observableArrayList();
	Integer index;
	String sushiID, name, description;
	int stockData, price, stock;
	String existingSushiId;
	String message; 
	
	@FXML
    void getItem(MouseEvent event) {
		index = sushiTable.getSelectionModel().getSelectedIndex();
		
		if (index <= -1) {
			return;
		}
		
		sushiIdTextField.setText(idCol.getCellData(index).toString());
		nameTextField.setText(nameCol.getCellData(index).toString());
		descriptionTextField.setText(descCol.getCellData(index).toString());
		priceTextField.setText(priceCol.getCellData(index).toString());
		stockTextField.setText(stockCol.getCellData(index).toString());
		
		sushiID = idCol.getCellData(index).toString();
    }
	
	private void loadData() {
		SushiList.clear();
		getSushiList();
		
		idCol.setCellValueFactory(new PropertyValueFactory<>("SushiId"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
		stockCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
	}
	
	public void getSushiList() {
		String query = "SELECT * FROM sushi";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sushijei", "root", "#21012123Op");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
            	SushiList.add(new Sushi(
            			rs.getString("SushiId"),
            			rs.getString("Name"),
            			rs.getString("Description"),
            			rs.getInt("Price"),
            			rs.getInt("Stock")
            	));
            	
            	sushiTable.setItems(SushiList);
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }        
	}
	
	@FXML  
	public void insert(ActionEvent event) throws SQLException {
		Window owner = updateButton.getScene().getWindow();
		String sushiName = nameTextField.getText();
		String desc = descriptionTextField.getText();
		String price = priceTextField.getText();
		double priceNum;
		int sushiStock = Integer.parseInt(stockTextField.getText());
		String lastSushiID = "";
		
		if (sushiName.length() < 5 || sushiName.length() > 20) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "New Sushi Name must consist of 5 – 20 characters!");
            return;
		}
		
		if (desc.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "New Susi Description must not be empty!");
            return;
		}
		
		try {
			priceNum = Double.parseDouble(price);
			if (priceNum <= 1000) {
				showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "New Sushi Price must be more than 1000!");
	            return;
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Sushi Price must be a valid number!");
            return;
		}
		
		if (sushiStock <= 1) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "New Sushi Stock must be more than 1!");
            return;
		}
		
		String query = "select SushiId from sushi ORDER BY SushiId DESC LIMIT 1";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sushijei", "root", "#21012123Op");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
            	lastSushiID = rs.getString("SushiId");
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        int lastNum = 0;
        Pattern patternData = Pattern.compile("^SU(\\d{3})$");
        Matcher matcher = patternData.matcher(lastSushiID);
        if (matcher.matches()) {
            lastNum = Integer.parseInt(matcher.group(1));
        }

        int newNum = lastNum + 1;

        String newSushiID = String.format("SU%03d", newNum);
        
        JdbcDao jdbcDao = new JdbcDao();
        boolean flag = jdbcDao.insertSushi(newSushiID, sushiName, desc, priceNum, sushiStock);
        
        if (flag) {
        	sushiIdTextField.setText("");
        	nameTextField.setText("");
        	descriptionTextField.setText("");
        	priceTextField.setText(""); 
        	stockTextField.setText("");
        	
			loadData();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText("Message");
	        alert.setContentText("Sushi inserted with ID: " + newSushiID);
	        alert.showAndWait();
		}
	}
	
	@FXML  
	public void update(ActionEvent event) throws SQLException {
		Window owner = updateButton.getScene().getWindow();
		Sushi selectedSushi = sushiTable.getSelectionModel().getSelectedItem();
		String sushiName = nameTextField.getText();
		String desc = descriptionTextField.getText();
		String price = priceTextField.getText();
		double priceNum;
		int sushiStock = Integer.parseInt(stockTextField.getText());
		
		if (selectedSushi == null) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Please select sushi first!");
            return;
	    }
		
		if (sushiName.length() < 5 || sushiName.length() > 20) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Sushi Name must consist of 5 – 20 characters!");
            return;
		}
		
		if (desc.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Susi Desc must not be empty!");
            return;
		}
		
		try {
			priceNum = Double.parseDouble(price);
			if (priceNum <= 1000) {
				showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Sushi Price must be more than 1000!");
	            return;
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Sushi Price must be a valid number!");
            return;
		}
		
		if (sushiStock <= 1) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Sushi Stock must be more than 1!");
            return;
		}
		
		JdbcDao jdbcDao = new JdbcDao();
		boolean flag = jdbcDao.updateSushi(sushiName, desc, priceNum, sushiStock, sushiID);
		
		if (flag) {
			loadData();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText("Message");
	        alert.setContentText("Sushi with ID: " + sushiID + " updated!");
	        alert.showAndWait();
		}
	}
	
	@FXML  
	public void delete(ActionEvent event) throws SQLException {
		Window owner = deleteButton.getScene().getWindow();
		Sushi selectedSushi = sushiTable.getSelectionModel().getSelectedItem();
		
		if (selectedSushi == null) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Please select sushi first!");
            return;
	    }
		
		JdbcDao jdbcDao = new JdbcDao();
		boolean flag = jdbcDao.deleteSushi(sushiID);
		
		if (flag) {
			loadData();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText("Message");
	        alert.setContentText("Sushi with ID: " + sushiID + " deleted!");
	        alert.showAndWait();
		}
	}
	
	public void getUserID(String userID) {
		userId = userID;
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
