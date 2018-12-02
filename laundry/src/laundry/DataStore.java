package laundry;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

public class DataStore {
 	static Connection con = null;

	public static void connectDB(String dbServer, int dbPort, String dbName, String userId, String userPwd) {
		String dbURL = "jdbc:mariadb://" + dbServer + ":" + dbPort + "/" + dbName;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            
            con = DriverManager.getConnection(dbURL, userId, userPwd);
            System.out.println("connected !!!");
        } catch(Exception e) {
            System.out.println("not connected !!!");
            e.printStackTrace();
        }		
    }
    
	public static void insertLaundry(String laundryName, int period) {
	}
	   
	public static void updateLaundry(String laundryName, int period) {
	}
	   
	public static void deleteLaundry(String laundryName) {
	}
	
	public static void selectLaundry(Date dt) {		
	}
 }