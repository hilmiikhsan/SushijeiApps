package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.models.cart.Cart;
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
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ViewCartController {
	@FXML
	private TableView<Cart> cartTable;
	
	@FXML
	private TableColumn<Cart, String> sushiIdCol;
	
	@FXML
	private TableColumn<Cart, String> sushiNameCol;
	
	@FXML
	private TableColumn<Cart, String> sushiPriceCol;
	
	@FXML
	private TableColumn<Cart, String> quantityCol;
	
	@FXML
    private Stage homepageStage;
	
	@FXML 
	private MenuItem logoutMenu, buySushiMenu, viewCartMenu;
	
	@FXML
    private TextField sushiIdTextField;
 
	@FXML
    private Button deleteCartButton, checkoutButton;
	
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
	       	 BuySushiController buySushiController = loader.getController();
	       	 buySushiController.initData(selectUser);
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
	       	 homepageStage = (Stage) viewCartMenu.getParentPopup().getOwnerWindow();
	       	 homepageStage.setTitle("SushiJei - Home");
	       	 homepageStage.setScene(new Scene(root, 900, 570));
	       	 homepageStage.show();
      	 } catch (Exception err) {
      		 err.printStackTrace(); 
      		 err.getCause();
      	 }
    }
	
	ObservableList<Cart> CartList = FXCollections.observableArrayList();
	Integer index;
	String sushiID;
	String existingSushiId;
	String message;
	int quantityData, stockData, totalStock;
	
	@FXML
    void getItem(MouseEvent event) {
		index = cartTable.getSelectionModel().getSelectedIndex();
		
		if (index <= -1) {
			return;
		}
		
		sushiIdTextField.setText(sushiIdCol.getCellData(index).toString());
		
		sushiID = sushiIdCol.getCellData(index).toString();
    }
	
	private void loadData() {
		CartList.clear();
		getCartList();
		
		sushiIdCol.setCellValueFactory(new PropertyValueFactory<>("SushiId"));
		sushiNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		sushiPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
	}
	
	public void getCartList() {
		String query = "select c.SushiId, s.Name, s.Price, c.Quantity\n"
				+ "from cart c\n"
				+ "join sushi s on c.SushiId = s.SushiId \n"
				+ "join user u on u.UserId = c.UserId\n"
				+ "where u.UserId = ?";
		String DATABASE_URL = "jdbc:mysql://localhost:3306/sushijei?useSSL=false";
	    String DATABASE_USERNAME = "root";
	    String DATABASE_PASSWORD = "#21012123Op";
		try (Connection connection = DriverManager
	            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

	            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, userId);
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            while (rs.next()) {
	            	CartList.add(new Cart(
	            			rs.getString("SushiId"),
	            			rs.getString("Name"),
	            			rs.getInt("Price"),
	            			rs.getInt("Quantity")
	            	));
	            	
	            	cartTable.setItems(CartList);
	            }
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
	}
	
	@FXML
	public void deleteCart(ActionEvent event) throws SQLException {
		Window owner = deleteCartButton.getScene().getWindow();
		
		String sushiIdString = sushiIdTextField.getText();
		
		if (sushiIdString.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Please select sushi first!");
            return;
		} 
				
		JdbcDao jdbcDao = new JdbcDao();
		boolean flag = jdbcDao.deleteCart(sushiIdString, userId);
		
		if (flag) {
			loadData();
		}	
	}
	
	@FXML
	public void checkoutCart(ActionEvent event) throws SQLException {
		Window owner = checkoutButton.getScene().getWindow();
		
		String query = "select c.SushiId, s.Name, s.Price, c.Quantity, s.Stock\n"
				+ "from cart c\n"
				+ "join sushi s on c.SushiId = s.SushiId \n"
				+ "join user u on u.UserId = c.UserId\n"
				+ "where u.UserId = ?"; 
		String DATABASE_URL = "jdbc:mysql://localhost:3306/sushijei?useSSL=false";
	    String DATABASE_USERNAME = "root";
	    String DATABASE_PASSWORD = "#21012123Op";
		try (Connection connection = DriverManager
	            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

	            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, userId);
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            if (!rs.next()) {
	            	showAlert(Alert.AlertType.ERROR, owner, "Error", "Error", "Cart is empty, please buy sushi first!");
	                return;
	            } else {
	            	 for (Cart cart : CartList) {
	 	            	String queryUpdateStock = "UPDATE sushi SET Stock = Stock - ? WHERE SushiId = ?";
	 	            	PreparedStatement preparedStatements = connection.prepareStatement(queryUpdateStock);
	 	            	preparedStatements.setInt(1, cart.getQuantity());
	 	            	preparedStatements.setString(2, cart.getSushiId());
	 	            	preparedStatements.executeUpdate();
	 	            }
	            	String queryDeleteCart = "delete from cart where UserId = ?";
	            	PreparedStatement pstmt = connection.prepareStatement(queryDeleteCart);
	            	pstmt.setString(1, userId);
	            	pstmt.executeUpdate();
	            	
	            	CartList.clear();
            	    Alert alert = new Alert(AlertType.INFORMATION);
	 	   	        alert.setTitle("Message");
	 	   	        alert.setHeaderText("Message");
	 	   	        alert.setContentText("Checkout successful!");
	 	   	        alert.showAndWait();
	            }
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
	}
	
	private static void showAlert(Alert.AlertType alertType, Window owner, String title, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
