package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class BuySushiController {
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
    private Stage homepageStage;

	@FXML 
	private MenuItem logoutMenu, buySushiMenu, viewCartMenu;

	@FXML
    private TextField sushiIdTextField, quantityTextField;
	
	@FXML
    private Button addCartButton;
	
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
    		 homepageStage = (Stage) logoutMenu.getParentPopup().getOwnerWindow();
    		 homepageStage.setTitle("SushiJei - Home");
    		 homepageStage.setScene(new Scene(root, 620, 364));
    		 homepageStage.show();
       	 } catch (Exception err) {
       		 err.printStackTrace();
       		 err.getCause();
       	 }
    }
	
	@FXML
    void handlePageBuySushi(Event event) {
    	try {
    		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/buysushi.fxml"));
	       	 root = loader.load();
    		 homepageStage = (Stage) buySushiMenu.getParentPopup().getOwnerWindow();
    		 homepageStage.setTitle("SushiJei - Home");
    		 homepageStage.setScene(new Scene(root, 900, 570));
    		 homepageStage.show();
       	 } catch (Exception err) {
       		 err.printStackTrace();
       		 err.getCause();
       	 }
    }
	
	@FXML
    void handlePageViewCart(Event event) {
    	try {
   		 	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewcart.fxml"));
	       	 root = loader.load();
	       	 ViewCartController viewCartController = loader.getController();
	       	 viewCartController.initData(selectUser);
	       	 homepageStage = (Stage) viewCartMenu.getParentPopup().getOwnerWindow();
	       	 homepageStage.setTitle("SushiJei - Home");
	       	 homepageStage.setScene(new Scene(root, 900, 570));
	       	 homepageStage.show();
      	 } catch (Exception err) {
      		 err.printStackTrace();
      		 err.getCause();
      	 }
    }
	
	ObservableList<Sushi> SushiList = FXCollections.observableArrayList();
	Integer index;
	String sushiID;
	int stockData;
	String existingSushiId;
	String message; 
	
	@FXML
    void getItem(MouseEvent event) {
		index = sushiTable.getSelectionModel().getSelectedIndex();
		
		if (index <= -1) {
			return;
		}
		
		sushiIdTextField.setText(idCol.getCellData(index).toString());
		
		sushiID = idCol.getCellData(index).toString();
    }
	
	private void loadData() {
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
	public void addCart(ActionEvent event) throws SQLException {
		Window owner = addCartButton.getScene().getWindow();
		
		String quantityString = quantityTextField.getText();
										
		if (sushiID == null) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Please select sushi first!");
            return;
		}
		
		if (quantityString.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Quantity invalid!");
            return;
		}
		
		int quantity = 0;
		try {
			quantity = Integer.parseInt(quantityString);
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Quantity invalid!");
            return;
		}
		
		if (quantity < 1) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Quantity that can’t be less than 1!");
            return;
		}
				
		String query = "SELECT SushiId, Stock FROM sushi where SushiId = ?";
		String DATABASE_URL = "jdbc:mysql://localhost:3306/sushijei?useSSL=false";
	    String DATABASE_USERNAME = "root";
	    String DATABASE_PASSWORD = "#21012123Op";
		try (Connection connection = DriverManager
	            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

	            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, sushiID);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	            	stockData = resultSet.getInt("Stock");
	            }
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
		
		if (quantity > stockData) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Quantity can’t be more than its Sushi Stock!");
            return;
		}
		
		String queryGetSushi = "select UserId, SushiId from cart where UserId = ? and SushiId = ?";
		try (Connection connection = DriverManager
	            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

	            PreparedStatement preparedStatement = connection.prepareStatement(queryGetSushi)) {
	            preparedStatement.setString(1, userId);
	            preparedStatement.setString(2, sushiID);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	            	existingSushiId = resultSet.getString("SushiId");
	            }
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
		
		JdbcDao jdbcDao = new JdbcDao();
		boolean flag;
				
		if (existingSushiId != null) {
			flag = jdbcDao.updateCart(quantity, userId, sushiID);
			message = "Sushi " + sushiID + " quantity updated!";
		} else {
			flag = jdbcDao.insertCart(userId, sushiID, quantity);
			message = "Sushi " + sushiID + " added to cart!";
		}
		
		if (flag) {
			quantityTextField.setText("");
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText("Message");
	        alert.setContentText(message);
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
