package dal;

import java.*;
public class DataAccessLayer{
     private java.sql.Connection  con = null;
     private final String url = "jdbc:sqlserver://";
     private final String serverName= "localhost";
     private final String databaseName= "CardGame";
     // Informs the driver to use server a side-cursor, 
     // which permits more than one active statement 
     // on a connection.
     private final String integratedSecurity = "true"; 
     
     // Constructor
     public DataAccessLayer(){}
     
     private String getConnectionUrl(){
          return url+serverName+";databaseName="+databaseName+";integratedSecurity="+integratedSecurity+";";
     }
     
     private java.sql.Connection getConnection(){
          try{
               Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
               String url = "jdbc:sqlserver://localhost;integratedSecurity=true;";
               con = java.sql.DriverManager.getConnection(url);
               if(con!=null) System.out.println("Connection Successful!");
          }catch(Exception e){
               e.printStackTrace();
               System.out.println("Error Trace in getConnection() : " + e.getMessage());
         }
          return con;
      }

     /*
          Display the driver properties, database details 
     */ 

     public void displayDbProperties(){
          java.sql.DatabaseMetaData dm = null;
          java.sql.ResultSet rs = null;
          try{
               con= this.getConnection();
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
     
     private void closeConnection(){
          try{
               if(con!=null)
                    con.close();
               con=null;
          }catch(Exception e){
               e.printStackTrace();
          }
     }
     public static void main(String[] args) throws Exception
       {
    	 DataAccessLayer myDbTest = new DataAccessLayer();
          myDbTest.displayDbProperties();
       }
}