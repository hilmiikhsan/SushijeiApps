package application;

import application.models.user.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomepageController{
	@FXML
    private Menu logoutPage;
	
	@FXML
    private Button submitButton;
	
	@FXML
    private Stage homepageStage, viewcartPage;
	
	@FXML 
	private MenuItem logoutMenu, buySushiMenu, viewCartMenu;
	
	@FXML
	private AnchorPane contentPane;
	
	@FXML
	private Label userLabel;
	
	private String userId;
	private Parent root;
	User selectUser;
	
	public void initData(User user) { 
		selectUser = user;
		userId = selectUser.getUserId(); 
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
    
    public String getUserID() {
    	return userId;
	}
}
