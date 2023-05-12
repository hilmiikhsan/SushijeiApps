package application;

import application.models.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class AdminpageController {
	@FXML
    private MenuItem logoutMenu;

    @FXML
    private MenuItem manageSushiMenu;
    
    @FXML
    private Stage adminStage;																																																																																																						
    
    private String userId;
    private Parent root;
    User selectUser;
	
	public void initData(User user) { 
		selectUser = user;
		userId = selectUser.getUserId(); 
	}

    @FXML
    void handleLogoutPageLink(ActionEvent event) {
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
  		 	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/managesushi.fxml"));
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
    
    public String getUserID() {
    	return userId;
	}
}
