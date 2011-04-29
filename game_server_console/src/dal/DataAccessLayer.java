package dal;

import java.sql.*;

public class DataAccessLayer{
    private java.sql.Connection  con = null;
     
    // Constructor
    public DataAccessLayer(){
    	try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
			String url = "jdbc:sqlserver://localhost:1433;databaseName=CardGame;selectMethod=cursor;";
			con = java.sql.DriverManager.getConnection(url, "cisc370", "finalproject");
			if(con!=null) System.out.println("Connection to database successful!");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error Trace in getConnection() : " + e.getMessage());
		}
    }

	public void closeConnection(){
	    try{
	         if(con!=null)
	              con.close();
	         con=null;
	    }catch(Exception e){
	         e.printStackTrace();
	    }
	}
     
	public boolean isAlreadyUser(String userName){
	    Statement stmt = null;
	    String query = "SELECT Name FROM Users WHERE Name = '" + userName +"'";
	    try{
	    	stmt = con.createStatement();
	    	ResultSet rs = stmt.executeQuery(query);
	    	if(rs.next()){
	    		return true;
	    	}
	    	
	    } catch (SQLException e){
	    	System.out.println("No user exists by the name of: " + userName);
	    	return false;
	    }
		return false; 
	}
     
    public boolean login(String userName, String password){
    	Statement stmt = null;
    	String query = "SELECT Password FROM Users WHERE Name = '" + userName + "' AND Password = '"+ password + "'";
    	try{
	    	stmt = con.createStatement();
	    	ResultSet rs = stmt.executeQuery(query);
	    	while(rs.next()){
	    		return true;
	    	}
	    	
	    } catch (SQLException e){
	    	System.out.println("Invalid user name and/or password");
	    	return false;
	    }
		return false;
    }
    
    public boolean register(String userName, String password, String eMail){
    	if(userName.equals(null) || userName.equals("") ||
    			password.equals(null) || password.equals("") ||
    			eMail.equals(null) || eMail.equals(""))
    		return false;
    	
    	if(this.isAlreadyUser(userName)){
    		System.out.println("Already a user by the name of: " + userName);
    		return false;
    	}
    	
    	Statement stmt = null;
    	int updateQuery = 0;
    	String query = "INSERT INTO Users (Name, Password, eMail)" +
    					"VALUES ('"+userName+"','"+password+"','"+eMail+"')"+
    					"INSERT INTO Money (UserName, Money)" + 
    					"VALUES ('"+userName+"', '1000.00')" +
    					"INSERT INTO Stats (UserName, Wins, Losses, Pushes, Total)" + 
    					"VALUES ('"+userName+"', '0', '0', '0', '0')";
    	
    	try{
    		stmt = con.createStatement();
    		updateQuery = stmt.executeUpdate(query);
    		if(updateQuery != 0)
    			return true;
    	}catch (SQLException e){
    		System.out.println(e.getMessage());
	    	return false;
	    }
    	
    	return false;
    }
    
    /*
         Display the driver properties, database details 
    */ 
    public void displayDbProperties(){
         java.sql.DatabaseMetaData dm = null;
         java.sql.ResultSet rs = null;
         try{
              if(con!=null){
                   dm = con.getMetaData();
                   System.out.println("Driver Information");
                   System.out.println("\tDriver Name: "+ dm.getDriverName());
                   System.out.println("\tDriver Version: "+ dm.getDriverVersion ());
                   System.out.println("\nDatabase Information ");
                   System.out.println("\tDatabase Name: "+ dm.getDatabaseProductName());
                   System.out.println("\tDatabase Version: "+ dm.getDatabaseProductVersion());
                   System.out.println("Avalilable Catalogs ");
                   rs = dm.getCatalogs();
                   while(rs.next()){
                        System.out.println("\tcatalog: "+ rs.getString(1));
                   } 
                   rs.close();
                   rs = null;
                   closeConnection();
              }else System.out.println("Error: No active Connection");
         }catch(Exception e){
              e.printStackTrace();
         }
         dm=null;
    }     
     
     //Testing purposes.
    public static void main(String[] args) throws Exception{
    	DataAccessLayer myDbTest = new DataAccessLayer();
    	
    	System.out.println(myDbTest.register("Test", "test", "test"));
    	
    	System.out.println(myDbTest.login("Test", "test"));
    	
    	//myDbTest.displayDbProperties();
    }
}