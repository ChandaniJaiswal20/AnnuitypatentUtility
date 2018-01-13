package annuityPatentUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class DBdataFetcher {
	
	UtilityConfiguration  uc	=	null;
	
	
	private String databaseHost	= null; 
	private String databasePort	=	null;
	private String databaseTool	=	null;
	private String databaseName	=	null;
	private	String userName		=	null;
	private	String password		=	 null;
	private	Connection connection	=	null;
	
	public static Logger logger	=	Logger.getLogger(DBdataFetcher.class);
	
	
	
	
	public DBdataFetcher(String databaseHost, String databasePort,
			String databaseTool, String databaseName,String userName,String password ){
		
		
		 this.databaseHost	= databaseHost; 
		 this.databasePort	=	databasePort;
		 this.databaseTool	=	databaseTool;
		 this.databaseName	=	databaseName;
		 this.userName		=	userName;
		 this.password		=	 password;
		 
			init();
		
	}
	
	public void init(){
		
		if(connection==null)
			configureConnection();
		
	}
	
	public Connection getConnection(){
		
		return connection;
	}
	
	
	public void closeConnection(){
		
		 if(connection!=null){
		 
		 try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
		
	}
	
	
	
	public  void 	configureConnection() {
		// TODO Auto-generated method stub
		//Connection conn = null;
		if( databaseTool.equalsIgnoreCase("oracle") ){
			
			logger.info("Database Type is Oracle....");
			
		//("jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":"+databaseName,userName,password  );
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection	=	DriverManager.getConnection("jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":"+databaseName,userName,password  );
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			
			logger.error("Error Occured while loading OracleDriver class ", e);
			//e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while obtaining connection ", e);
		//	e.printStackTrace();
		}
		}
		else if(databaseTool.equalsIgnoreCase("sql")){
			
			logger.info("Database Type is MSSQL....");
			
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				
				connection	=	DriverManager.getConnection( "jdbc:sqlserver://"+databaseHost+ ":" + databasePort +";" +
			    		  "databaseName="+databaseName+";"+"user="+userName+";"+"password="+password);
			} catch (ClassNotFoundException e) {
				logger.error("Error Occured while loading SQLDriver class ", e);
			//	e.printStackTrace();
			} catch (SQLException e) {
				logger.error("Error Occured While obtaining  Connection: ", e);
			//	e.printStackTrace();
			}
			
			
		}
		
		
	
		
		
	}
	
	
	public   ArrayList <HashMap<String,String>> readAffectedFileIDList(String userdocumentType,String toBeLapsedStatusCode) {
		// TODO Auto-generated method stub
		logger.info("Reading Files that are in toBeLapsedStatus and Annuity userDocument has been recevied against them....");
		
		ArrayList <HashMap<String,String>> list_of_patent_detail_map=	new ArrayList<HashMap<String,String>>();
		PreparedStatement preStmt	=	null;
		ResultSet rs				=	null;
		String query	="select i1.FILE_SEQ,i1.FILE_TYP,i1.FILE_SER,i1.FILE_NBR,i2.DOC_ORI,i2.DOC_LOG,i2.DOC_SER,i2.DOC_NBR "+
				"from ip_proc  i1, ip_proc i2 "+ 
				"where i2.USERDOC_TYP =?  AND  i1.FILE_SEQ=i2.USERDOC_FILE_SEQ AND "+
				"i1.FILE_TYP =i2.USERDOC_FILE_TYP AND i1.FILE_SER =i2.USERDOC_FILE_SER AND i1.FILE_NBR =i2.USERDOC_FILE_NBR AND i1.STATUS_CODE =? ";
			
		try {
			
			init();
			
			
			 preStmt	=		connection.prepareStatement(query);
				
			preStmt.setString(1, userdocumentType);
			preStmt.setString(2, toBeLapsedStatusCode);
			
			rs	=	  preStmt.executeQuery();
			
			while(rs.next()){
				
				
				HashMap	<String,String> patent_detail_map	=	new HashMap<String ,String>();
				
				patent_detail_map.put("FILE_SEQ", rs.getString("FILE_SEQ"));
				patent_detail_map.put("FILE_TYP", rs.getString("FILE_TYP"));
				patent_detail_map.put("FILE_SER", rs.getString("FILE_SER"));
				patent_detail_map.put("FILE_NBR", rs.getString("FILE_NBR"));
				patent_detail_map.put("DOC_ORI", rs.getString("DOC_ORI"));
				patent_detail_map.put("DOC_LOG", rs.getString("DOC_LOG"));
				patent_detail_map.put("DOC_SER", rs.getString("DOC_SER"));
				patent_detail_map.put("DOC_NBR", rs.getString("DOC_NBR"));
				
		//		logger.debug("patent_detail_map::"+ patent_detail_map);
				
				list_of_patent_detail_map.add(patent_detail_map);
				
			}
					
		//	logger.debug("list of affected files are"+ list_of_patent_detail_map);
				
			} catch (SQLException e) {
				logger.error("Error Occured while reading Files that are in toBeLapsedStatus and Annuity userDocument has been recevied against them.... ", e);
			//	e.printStackTrace();
			}
		
		finally{
			 if (rs != null){
				 
				 try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
			 
			 if(preStmt!=null){
			 
				 try {
					preStmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
	}
			
		return list_of_patent_detail_map;
	}

	
	
	
	public ArrayList <HashMap<String,String>>  readUserDocumentOfTypeAnnuity(String userdocumentType,String pendingStatusCode){
		logger.info("Reading UserDocument which are  of Annuity Type and are in pending status .....");
		
		ArrayList <HashMap<String,String>> list_of_userDoc_annuity_map=	new ArrayList<HashMap<String,String>>();
		PreparedStatement preStmt	=	null;
		ResultSet rs				=	null;
		String query	="select DOC_ORI ,DOC_LOG , DOC_SER , DOC_NBR , USERDOC_FILE_SEQ ,USERDOC_FILE_TYP ,USERDOC_FILE_SER ,USERDOC_FILE_NBR  from IP_PROC where USERDOC_TYP = ? AND STATUS_CODE = ? ";
			
		try {
			
			init();
			
			 preStmt	=		connection.prepareStatement(query);
				
			preStmt.setString(1, userdocumentType);
			preStmt.setString(2, pendingStatusCode);
			
			
			rs	=	  preStmt.executeQuery();
			
			while(rs.next()){
				
				HashMap	<String,String> userDoc_detail_map	=	new HashMap<String ,String>();
					
				userDoc_detail_map.put("DOC_ORI", rs.getString("DOC_ORI"));
				userDoc_detail_map.put("DOC_LOG", rs.getString("DOC_LOG"));
				userDoc_detail_map.put("DOC_SER", rs.getString("DOC_SER"));
				userDoc_detail_map.put("DOC_NBR", rs.getString("DOC_NBR"));
				userDoc_detail_map.put("FILE_SEQ", rs.getString("USERDOC_FILE_SEQ"));
				userDoc_detail_map.put("FILE_TYP", rs.getString("USERDOC_FILE_TYP"));
				userDoc_detail_map.put("FILE_SER", rs.getString("USERDOC_FILE_SER"));
				userDoc_detail_map.put("FILE_NBR", rs.getString("USERDOC_FILE_NBR"));
				
			//	logger.debug("userDoc_detail_map:::"+userDoc_detail_map);
				
				list_of_userDoc_annuity_map.add(userDoc_detail_map);
				
			}
					
		//	logger.debug("list_of_userDoc_annuity_map:::"+list_of_userDoc_annuity_map);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Error Occured while Reading UserDocument of Annuity Type..... ", e);
			}
			
		finally{
			 if (rs != null){
				 
				 try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
			 
			 if(preStmt!=null){
			 
				 try {
					preStmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
//			 if(connection!=null){
//				 
//				 try {
//					 connection.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			 }
	}
		
		return list_of_userDoc_annuity_map;
	}
		
	

	public HashMap<String,String> getUserDocumentRelatedInfo( String docORI, String docLOG,String docSER, String docNbr) {
		//init();
		
		int docNBR	=	Integer.parseInt(docNbr);
		int docSERIES	=	Integer.parseInt(docSER);
		ResultSet rs	= null;
		PreparedStatement ps	= null;
		HashMap<String,String>	userDocumentInfoMap	=	new HashMap<String, String>();
		String query	=	"select PROC_TYP,PROC_NBR from IP_USERDOC_PROCS where DOC_ORI=? and  DOC_LOG=? and DOC_SER=? and DOC_NBR=?";
		
					
		try {
		init();
			 ps	=	connection.prepareStatement(query);
			ps.setString(1, docORI);
			ps.setString(2, docLOG);
			ps.setInt(3, docSERIES);
			ps.setInt(4, docNBR);
			
			
			 rs	=	 ps.executeQuery();
			
			
			while(rs.next()){
				
				
				userDocumentInfoMap.put("PROC_TYP", rs.getString("PROC_TYP"))	;
				userDocumentInfoMap.put("PROC_NBR", rs.getString("PROC_NBR"))	;
				
				
			}
			
			logger.debug("UserDocument ::::"+docORI+"/"+docLOG+"/"+docSERIES+"/"+docNBR+":::::PROC_TYP::"+userDocumentInfoMap.get("PROC_TYP")+":::::PROC_NBR::"+userDocumentInfoMap.get("PROC_NBR"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while Reading ProcType and ProcNumber of UserDocument . ", e);
		}
		
		finally{
			 if (rs != null){
				 
				 try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 
			 
			 if(ps!=null){
			 
				 try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }

	}
		
		
		return userDocumentInfoMap;
		
	
	}

		public int readDeleteActionRelatedInfo(int procNumber,String actionType){
			
			ResultSet rs	= null;
			PreparedStatement ps	= null;
			HashMap<String,String>	userDocumentInfoMap	=	new HashMap<String, String>();
			String query	=	"select MAX(ACTION_NBR) as ACTION_NBR from IP_ACTION where PROC_NBR  = ? and ACTION_TYP  = ?";
			int actionNumber	=	0;
		
	
		 try {
			 init();
			ps	=	connection.prepareStatement(query);
			
			ps.setInt(1, procNumber);
			ps.setString(2, actionType);
			
			rs	=	 ps.executeQuery();
			
			while(rs.next()){
				
			actionNumber	=	rs.getInt("ACTION_NBR");
	
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error Occured while Reading max action number of particular action type from IP_ACTION table. ", e);
			e.printStackTrace();
		}
		 
			finally{
				 if (rs != null){
					 
					 try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
				 if(ps!=null){
				 
					 try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
				 
		
		}
		 
		return actionNumber;
		 
	
			
		}

}
