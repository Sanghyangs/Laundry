package laundry;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DataStore {
 	static Connection conn = null;

	public static boolean connectDB(String dbServer, int dbPort, String dbName, String userId, String userPwd) {
		String dbURL = "jdbc:mariadb://" + dbServer + ":" + dbPort + "/" + dbName + "?useUnicode=yes&characterEncoding=UTF-8";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            
            conn = DriverManager.getConnection(dbURL, userId, userPwd);
        } catch(Exception e) {
        	String strMsg = "DB에 접속할 수 없습니다. 설정을 확인하여야 합니다.\r\n오류메시지는 다음과 같습니다.\r\n(" + e.getLocalizedMessage() + ")";
        	JOptionPane.showMessageDialog(null, strMsg);
        	
        	return false;
        }
        
        return true;
    }
    
	public static boolean insertClothes(String strNickName, String strKind, int period) {
		boolean bSuccess = false;
		try {
        	int index = 1;
        	Calendar calendar = Calendar.getInstance();
        	Date regDate = calendar.getTime();
			String sqlString = "INSERT INTO laundry.tb_laundry (NICKNAME, KIND, REGDATE, PERIOD, LASTDATE, NEXTDATE) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setString(index++, strNickName);
			ps.setString(index++, strKind);
			ps.setDate(index++, new java.sql.Date(regDate.getTime()));
			ps.setInt(index++, period);
			ps.setDate(index++, new java.sql.Date(regDate.getTime()));
			calendar.add(Calendar.DATE, period);
        	Date nextDate = calendar.getTime();
			ps.setDate(index++, new java.sql.Date(nextDate.getTime()));
			
			ps.executeUpdate();
	 
			ps.close();
			
			bSuccess = true;
		} catch (Exception e) {
			e.getMessage();
		}
		
		return bSuccess;
	}
	   
	public static boolean updateLaundry(String strNickName, int year, int month, int day) {
		boolean bSuccess = false;
		int period = getPeriod(strNickName);
		try {
        	int index = 1;
        	Calendar calendar = Calendar.getInstance();
        	calendar.set(year, month - 1, day);
        	Date laundryDate = calendar.getTime();
			String sqlString = "UPDATE laundry.tb_laundry SET LASTDATE = ?, NEXTDATE = ? WHERE NICKNAME = ?";
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setDate(index++, new java.sql.Date(laundryDate.getTime()));
			
			calendar.add(Calendar.DAY_OF_YEAR, period);
        	Date nextDate = calendar.getTime();
			ps.setDate(index++, new java.sql.Date(nextDate.getTime()));

			ps.setString(index++, strNickName);
			
			ps.executeUpdate();
	 
			ps.close();
			
			bSuccess = true;
		} catch (Exception e) {
			e.getMessage();
		}
		
		return bSuccess;
	}
	   
	public static void deleteLaundry(String laundryName) {
	}
	
	public static int fillScheduleTableEntry(DefaultTableModel scheduleTableEntry, int year, int month, int day) {
		scheduleTableEntry.setRowCount(0);
		
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month - 1, day);
		String strDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

		int rowCount = 0;
		try {
			String sqlString = "SELECT * FROM laundry.tb_laundry WHERE NEXTDATE = '" + strDate + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlString);

	        while (rs.next()) {
	        	Object[] rowData = new Object[5];	        	
	        	int rowIndex = 0;
	        	rowData[rowIndex++] = rs.getString("NICKNAME");
	        	rowData[rowIndex++] = rs.getString("KIND");
	        	rowData[rowIndex++] = Integer.toString(rs.getInt("PERIOD"));
	        	rowData[rowIndex++] = rs.getDate("LASTDATE").toLocalDate();
	        	rowData[rowIndex++] = rs.getDate("NEXTDATE").toLocalDate();
	        	
	        	scheduleTableEntry.addRow(rowData);

	        	rowCount++;
	        }
	 
	        rs.close();
	        stmt.close();
		} catch (Exception e) {
		}
		
		return rowCount;
	}
	
	public static int listClothes(DefaultTableModel clothesTableEntry, String strCbKind) {
		clothesTableEntry.setRowCount(0);

		int rowCount = 0;
		try {
			String sqlString = "SELECT * FROM laundry.tb_laundry";
			if (strCbKind.equalsIgnoreCase("전체") == false) {
				sqlString += " WHERE KIND = '" + strCbKind + "'";
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlString);

	        while (rs.next()) {
	        	Object[] rowData = new Object[6];	        	
	        	int rowIndex = 0;
	        	rowData[rowIndex++] = rs.getString("NICKNAME");
	        	rowData[rowIndex++] = rs.getString("KIND");
	        	rowData[rowIndex++] = rs.getDate("REGDATE").toLocalDate();
	        	rowData[rowIndex++] = Integer.toString(rs.getInt("PERIOD"));
	        	rowData[rowIndex++] = rs.getDate("LASTDATE").toLocalDate();
	        	rowData[rowIndex++] = rs.getDate("NEXTDATE").toLocalDate();
	        	
	        	clothesTableEntry.addRow(rowData);

	        	rowCount++;
	        }
	 
	        rs.close();
	        stmt.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return rowCount;
	}

	
	public static ArrayList<String> getNickNameList() {
		ArrayList<String> strNickNameList = new ArrayList<>();
		
		try {
			String sqlString = "SELECT NICKNAME FROM laundry.tb_laundry";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlString);

	        while (rs.next()) {
	        	strNickNameList.add(rs.getString("NICKNAME"));
	        }
	 
	        rs.close();
	        stmt.close();
		} catch (Exception e) {
		}				
		return strNickNameList;
	}
	
	
	private static int getPeriod(String strNickName) {
		int period = -1;
		try {
			String sqlString = "SELECT PERIOD FROM laundry.tb_laundry WHERE NICKNAME = '" + strNickName + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlString);

	        if (rs.next()) {
	        	period = rs.getInt("PERIOD");
	        }
	 
	        rs.close();
	        stmt.close();
		} catch (Exception e) {
		}				
		return period;
	}

 }