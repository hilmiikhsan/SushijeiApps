module SushiJiApps {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.models.sushi to javafx.graphics, javafx.fxml;
	opens application.models.cart to javafx.graphics, javafx.fxml;
	
	exports application.models.sushi;
	exports application.models.cart;
}
