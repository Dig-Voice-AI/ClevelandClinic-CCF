package ccf.custom.da.impl;

// Note: JDBC driver is here: 
// C:\Cisco\CVP\VXMLServer\Tomcat\lib\sqljdbc4.jar
//

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;

import ccf.custom.da.EMPIDBDa;
import ccf.custom.dm.EMPIResponse;

public class EMPIDBDaImpl extends EMPIDBDa {

	@Override
	public EMPIResponse getEmpiResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

			int matchCount = -73;
			
			try {
				String patientNumber = empi;

				CallableStatement cs = con.prepareCall("{? = call usp_VerifyEMPI (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.INTEGER);

				cs.execute();
				//cs.getInt(1);
				matchCount = cs.getInt(3);
				
				cr.empiResponse = ""+matchCount;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}

	@Override
	public EMPIResponse getEmpiDobResponse(String empi, String dob) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

			int matchCount = -1;
			String patientNumber = "E"+empi;
//			String dobDate=dob.substring(0,2)+"/"+dob.substring(2,4)+"/"+dob.substring(4,8);
			
			try {

				CallableStatement cs = con.prepareCall("{? = call usp_VerifyDOB (?,?,?)}");
				
				cs.setQueryTimeout(responseTimeout);

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.setString(3, dob);
				cs.registerOutParameter(4, Types.INTEGER);

				cs.execute();
				cs.getInt(1);
				matchCount = cs.getInt(4);
				
				cr.empiResponse = ""+matchCount;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}

	 /***** Method to retrive Balance from Database using input EMPI *****/
	@Override
	public EMPIResponse getEmpiBalanceResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

//			int matchCount = -1;
			double balance = 0;
			String patientNumber = "E"+empi;
			
			try {

				CallableStatement cs = con.prepareCall("{? = call usp_ReturnTotalBalance (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.VARCHAR);

				cs.execute();
				cs.getInt(1);
				balance = Double.parseDouble(cs.getString(3));
				String bal = Double.toString(balance);

				cr.empiResponse = bal;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}
 /***** End of Stored Procedure for Balance*****/
	
	 /***** Method to retrive CreditCount from Database using input EMPI *****/
	@Override
	public EMPIResponse getEmpiCreditCountResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

//			int matchCount = -1;
			int creditCount = -1;
			String patientNumber = "E"+empi;
			
			try {

				CallableStatement cs = con.prepareCall("{? = call usp_ReturnCreditCount (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.INTEGER);

				cs.execute();
				cs.getInt(1);
				creditCount = cs.getInt(3);
				String credit = "" + creditCount;

				cr.empiResponse = credit;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}
	
 /***** End of Stored Procedure for Credit Count*****/
	
	/***** Method to retrive PayXDOCId from Database using input EMPI *****/
	@Override
	public EMPIResponse getEmpiDocIDResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

//			int matchCount = -1;
			String patientNumber = "E"+empi;
			
			try {

				CallableStatement cs = con.prepareCall("{? = call usp_PullIPayXDocIDs (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.VARCHAR);

				cs.execute();
				cs.getInt(1);
				String payX = cs.getString(3);

				cr.empiResponse = payX;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}
 /***** End of Stored Procedure for PayXDOCId*****/
	
	
	/***** Method to retrieve guarantor ID from database using input EMPI *****/
	@Override
	public EMPIResponse getEmpiPullGuarantorIdForOnBaseResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				throw new Exception("Failed to get connection to database: " + dbURL);
			}

//			int matchCount = -1;
			String patientNumber = "E"+empi;
			
			try {

				CallableStatement cs = con.prepareCall("{call usp_PullGuarantorIdForOnBase (?,?)}");

//				cs.registerOutParameter(1, Types.INTEGER);
//				cs.setString(2, patientNumber);
//				cs.registerOutParameter(3, Types.VARCHAR);
				
				cs.setString(1, patientNumber);
				cs.registerOutParameter(2, java.sql.Types.VARCHAR);

				cs.execute();
//				cs.getInt(1);
				String payX = cs.getString(2);

				cr.empiResponse = payX;
				
				logInfo("EMPIDBDaImpl.getEmpiPullGuarantorIdForOnBaseResponse()=" + cr.empiResponse);

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}
 /***** End of Stored Procedure for usp_DBPullOnBaseDocIDs*****/
	
	
	
	/***** Method to retrive Request ISV2 from Database using input EMPI *****/
	@Override
	public EMPIResponse getEmpiISV2Response(String empi) throws Exception
	{
		// Get key from Properties file
		
		logInfo("Enter getEmpiISV2Response");

		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			
			logInfo("Get connection to database");
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				logInfo("Conection failure");
				System.exit(0);
			}

//			int matchCount = -1;
			String patientNumber = "E"+empi;
			
			try {

				logInfo("Prepare statement request for " + patientNumber);
				CallableStatement cs = con.prepareCall("{? = call usp_RequestISv2 (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.INTEGER);

				logInfo("Execute statement");
				cs.execute();
				cs.getInt(1);
				String billingSys = "" + cs.getInt(3);

				cr.empiResponse = billingSys;

			}
			catch( Exception x ) {
				logInfo("Exception 1");
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			logInfo("Exception 2");
			se.printStackTrace();
		}catch( Exception e ) {
			logInfo("Exception 3");
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		logInfo("Return result: " + cr.empiResponse);
		return cr;
	}
 /***** End of Stored Procedure for Request ISV2*****/

	@Override
	public EMPIResponse getEmpiCollectionsCountResponse(String empi) throws Exception
	{
		// Get key from Properties file
		
		EMPIResponse cr = new EMPIResponse();
		
		Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
		
			//int timeout = 10;
			
			String dbURL = "jdbc:sqlserver://" 
				+ serverName + ":" 
				+ port + ";DatabaseName=" + databaseName;
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DriverManager.setLoginTimeout(connectTimeout);
			con = DriverManager.getConnection (dbURL,username,password);

			if(con == null)
			{
				System.exit(0);
			}

//			int matchCount = -1;
			int collectionCount = -1;
			String patientNumber = "E"+empi;
			
			try {

				CallableStatement cs = con.prepareCall("{? = call usp_ReturnCollectionsCount (?,?)}");

				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, patientNumber);
				cs.registerOutParameter(3, Types.INTEGER);

				cs.execute();
				cs.getInt(1);
				collectionCount = cs.getInt(3);
				String collection = "" + collectionCount;

				cr.empiResponse = collection;

			}
			catch( Exception x ) {
				x.printStackTrace();
			}
			
		}catch(SQLException se){
			se.printStackTrace();
		}catch( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }

                if (con != null) {
                    con.close();
                }
			}catch(Exception e) {
				//do nothing
			}
		}
		
		return cr;
	}
	
 /***** End of Stored Procedure for Collections Count*****/

	
	
	public String url;

	public int connectTimeout;
	public int responseTimeout;

	public boolean logXML = false;

	public boolean logResponse = false;

	public boolean connectionDisconnect = false;

	public boolean sendConnectionClose = false;

	public String username;
	public String password;
	public String serverName;
	public String databaseName;
	public String port;
	
	public String debug;
	
	public void setDebug(String debug) {
		this.debug=debug;
	}
	
	public void setConnectTimeout(String connectTimeout) {
		this.connectTimeout = Integer.parseInt(connectTimeout);
	}

	public void setResponseTimeout(String responseTimeout) {
		this.responseTimeout = Integer.parseInt(responseTimeout);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setConnectionDisconnect(String connectionDisconnect) {
		this.connectionDisconnect = Boolean.parseBoolean(connectionDisconnect);
	}

	public void setSendConnectionClose(String sendConnectionClose) {
		this.sendConnectionClose = Boolean.parseBoolean(sendConnectionClose);
	}

	public void setLogXML(String logXML) {
		this.logXML = Boolean.parseBoolean(logXML);
	}

	public void setLogResponse(String logResponse) {
		this.logResponse = Boolean.parseBoolean(logResponse);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	

}
