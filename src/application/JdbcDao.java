package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDao { 

    // Replace below database url, username and password with your actual database credentials
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/sushijei?useSSL=false";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "#21012123Op";
    private static final String SELECT_QUERY = "select Email, Username, Password from user where (Email = ? or Username = ?) and Password = ?";
    private static final String REGISTER_QUERY = "insert into user (UserId, Username, Password, Email, Address, Gender, DOB, Role) "
    		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CART = "insert into cart (UserId, SushiId, Quantity) values (?, ?, ?)";
    private static final String UPDATE_CART = "update cart set Quantity = ? where UserId = ? and SushiId = ?";
    private static final String DELETE_CART = "delete from cart where SushiId = ? and UserId = ?";
    private static final String UPDATE_SUSHI = "update sushi set Name = ?, Description = ?, Price = ?, Stock = ? where SushiId = ?";
    private static final String DELETE_SUSHI = "delete from sushi where SushiId = ?";
    private static final String INSERT_SUSHI = "insert into sushi (SushiId, Name, Description, Price, Stock) values (?, ?, ?, ?, ?)";
    
    public boolean validate(String emailId, String username, String password) throws SQLException {

        // Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager 
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);

            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }
    
    public boolean registerUser(String userID, String username, String password, String email, String address, String gender, 
    		java.sql.Date birthDate, String role) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_QUERY)) {
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, gender);
            preparedStatement.setDate(7, birthDate);
            preparedStatement.setString(8, role);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean insertCart(String userID, String sushiID, int quantity) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CART)) {
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, sushiID);
            preparedStatement.setInt(3, quantity);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean updateCart(int quantity, String userID, String sushiID) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CART)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, sushiID);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean deleteCart(String sushiID, String userID) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CART)) {
            preparedStatement.setString(1, sushiID);
            preparedStatement.setString(2, userID);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean updateSushi(String name, String description, double price, int stock, String sushiID) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUSHI)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);;
            preparedStatement.setInt(4, stock);
            preparedStatement.setString(5, sushiID);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean deleteSushi(String sushiID) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUSHI)) {
            preparedStatement.setString(1, sushiID);

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }
    
    public boolean insertSushi(String sushiID, String Name, String description, double price, int stock) throws SQLException {
    	// Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUSHI)) {
            preparedStatement.setString(1, sushiID);
            preparedStatement.setString(2, Name);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, stock);;

            System.out.println(preparedStatement);
            
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
		return false;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
