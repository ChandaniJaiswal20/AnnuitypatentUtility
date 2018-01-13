package annuityPatentUtility;


import java.sql.Connection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ipas.commons.CPatent;
import org.ipas.commons.CProcessId;
import org.ipas.commons.CUserdoc;
import org.ipas.commons.CommonsProxyFactory;
import org.ipas.proxy.IpasException;
import org.ipas.proxy.IpasInteger;

public class UtilityManager {

	 static{
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
	        System.setProperty("current.date.time", dateFormat.format(new Date()));
	    }
	

	 private static UtilityManager uniqueInstance =	null;
	
	
	  private UtilityConfiguration configuration 	=	null;
	  private 	final	DBdataFetcher	dataFetcher	; // composition (part of ) should be initialise in constructor
	  private	PatentSpecialAction	patentSpecialAction			=	new PatentSpecialAction();
	  private	UserDocumnetActionInsertion	userDocumentAction	=	new UserDocumnetActionInsertion();
	  private	HashMap<String,String>  userDocumentInfoMap	=	 new 	HashMap<String, String>();
	  private	CommonsProxyFactory commonsProxyFactory;
	  private	ArrayList <HashMap<String,String>> list_of_patent_detail_map=	new ArrayList<HashMap<String,String>>();
	  private	ArrayList <HashMap<String,String>> list_of_userDoc_detail_map=	new ArrayList<HashMap<String,String>>();
	  private 	static final	String  LOG4JCONFIGFILE	= "log4j.properties";
	  private	static Connection conn	=null;
	  private 	static Logger logger	=	Logger.getLogger(UtilityManager.class);
	  

	private UtilityManager()
	{

		PropertyConfigurator.configure(LOG4JCONFIGFILE);
		logger.info("Initializing Utility...........................................!!!!!");
		logger.debug("################################################");
		logger.debug("=================================================");
		
		logger.debug("LOG4JCONFIGFILE:::"+LOG4JCONFIGFILE);
		configuration 	=	new UtilityConfiguration();	//aggregation (has a)
		configuration.loadConfiguration();
		configuration.checkValuesOfConfigFile();
		commonsProxyFactory = new CommonsProxyFactory(configuration.iiopHost +":"+configuration.iiopPort);
		logger.debug("Obtaining Database Connection....");
		dataFetcher	= new DBdataFetcher(configuration.databaseHost,configuration.databasePort,
					  configuration.databaseTool,configuration.databaseName,configuration.userName,configuration.password);
		
		logger.info("Initializing Completed...............................................!!!!!!");		
		logger.debug("=================================================");
		logger.debug("################################################");
	}
	

	
		public static  UtilityManager getInstance(){
			
					if(uniqueInstance==null)
					{	
						uniqueInstance	=	 new UtilityManager();
					}
					
					
			return uniqueInstance;
					
		}
	
		public UtilityConfiguration getConfiguration(){
			return configuration;
						
		}
	


	
	public void configureLogger(){
	
		PropertyConfigurator.configure(LOG4JCONFIGFILE);
			
	}
		


	public void proceesAllPatent() {
		
		logger.info("Going to execute Special action on All Patent..........................................!!!!! ");
		logger.debug("################################################");
		for(HashMap<String, String> patent_detail_map:list_of_patent_detail_map){
			
			performSpecialAction(patent_detail_map.get("FILE_SEQ"),patent_detail_map.get("FILE_TYP"),patent_detail_map.get("FILE_SER"),patent_detail_map.get("FILE_NBR"),configuration.patentSpecialActionType,configuration.patentActionDate,configuration.patentGrantedStatusCode,configuration.patentUserID);
			logger.debug("=================================================");
					
		}
		
		logger.info("Special Action has been executed on ALL patents.............................................!!!!! ");
		logger.debug("=================================================");
		logger.info("################################################");
		logger.info("Going to execute UserDocument action on All UserDoc..........................................!!!!! ");
		for(HashMap<String, String> userDoc_detail_map:list_of_userDoc_detail_map ){
			
			
			logger.debug("=================================================");
				performUserDocumentAction(userDoc_detail_map.get("DOC_ORI"),userDoc_detail_map.get("DOC_LOG"),userDoc_detail_map.get("DOC_SER"),userDoc_detail_map.get("DOC_NBR"));
				logger.debug("=================================================");
						
		}
	
		logger.debug("UserDocument Action has been executed on ALL UserDoc.............................................!!!!! ");
		logger.debug("=================================================");
		logger.info("################################################");
		//stopUtility();
	}




	public void performUserDocumentAction(String docORI,String docLOG, String docSER, String docNbr)  {
		
			try {
				
						userDocumentInfoMap	= dataFetcher.getUserDocumentRelatedInfo(docORI,docLOG,docSER,docNbr);
				
						userDocumentAction.processUserDocumentInsertion(docORI, docLOG, docSER, docNbr,
						configuration.userDocumentAcceptedActionType,configuration.userDocumentActionDate,configuration.userDocumentUserID,userDocumentInfoMap.get("PROC_TYP"),userDocumentInfoMap.get("PROC_NBR"),commonsProxyFactory);
						
					} 
					
					catch(IpasException e){
				
						logger.error("Error occured while insertUserDocumentAction " + e.getMessage()+"\r\n");
						rollbackPatentSpecialActions();
						logger.info("=================================================");
						rollbackUserDocumentActions();
						dataFetcher.closeConnection();
						throw new UserDocumentActionExcecutionFailedException("Exception occurred while exceuting  action on User Document::"+docORI+"/"+docLOG+"/"+docSER+"/"+docNbr);
						
					}
					
		
									
		}

		






	public void performSpecialAction(String fileSeq, String fileType, String fileSeries, String fileNumber, String actionType, String actionDate, String patentGrantedStatusCode, String userID) {
	try{
		
		patentSpecialAction.processSpecialActionInsertion(fileSeq, fileType, fileSeries, fileNumber, actionType, actionDate,patentGrantedStatusCode, userID,commonsProxyFactory);
		
	}
	
	catch(IpasException e){
		//if exception occurs then it should delete special action executed on all patent
		logger.error("Error occured while insertPatentSpecialAction " + e.getMessage()+"\r\n");
	
		rollbackPatentSpecialActions();
		dataFetcher.closeConnection();
		throw new SpecialActionExcecutionFailedException("Exception occurred while exceuting special action on patent::"+fileSeq+"/"+fileType+"/"+fileSeries+"/"+fileNumber);
		
	}
	

	
	}
	
	public void rollbackPatentSpecialActions(){
		logger.info("Going to rollback special action executed on all patents ...............................................!!!!");
		if(PatentSpecialAction.getSpecialActionExecutedList().size()>0){
		Iterator<CPatent> itr	=	PatentSpecialAction.getSpecialActionExecutedList().iterator();
			while(itr.hasNext())
			{
				
				CProcessId patentProcessId =	itr.next().getFile().getProcessId();
				
				logger.debug("deleting special action on patent with process id::"+patentProcessId.getProcessNbr().toString());
				
		
				int processNumber	=	Integer.valueOf(patentProcessId.getProcessNbr().toString());
				int actionNumber 	=	getDeleteRelatedInfo(processNumber,configuration.patentSpecialActionType);
				logger.debug("=================================================");
			patentSpecialAction.deletePatentAction(actionNumber,patentProcessId);
			logger.debug("=================================================");
			}
			
			logger.info("All special action on patent has been rollbacked.......................................!!!!! ");
		}
		
	else{
			
			logger.info("No records to rollback as no special action on patent has been executed yet......................");
			
		}
	}
	
	public void rollbackUserDocumentActions(){
		logger.info("Going to rollback user document  actions executed on all user socument ...........................");
		if(UserDocumnetActionInsertion.getUserDocumentActionExecutedList().size()>0){
			Iterator<CUserdoc> itr	=	UserDocumnetActionInsertion.getUserDocumentActionExecutedList().iterator();
			while(itr.hasNext())
			{
				CProcessId userDocProcessId =	itr.next().getUserdocProcessId();
				logger.debug("deleting userDocument action on userDoc with process id::"+userDocProcessId.getProcessNbr().toString());
				int  processNumber	=	Integer.valueOf(userDocProcessId.getProcessNbr().toString());
				
				int actionNumber 	=	getDeleteRelatedInfo(processNumber,configuration.userDocumentAcceptedActionType);
				logger.debug("=================================================");
				userDocumentAction.deleteUserDocumentAction(actionNumber,userDocProcessId);
				logger.debug("=================================================");
			}
			
			logger.info("All user document action has been rollbacked.................................!!!!! ");
		}
		
		else{
			
			logger.info("No records to rollback as no user document action has been executed yet......");
			
			
		}
	}
	
	
	
	public int getDeleteRelatedInfo(int processNumber,String actiontype){
		
	return	dataFetcher.readDeleteActionRelatedInfo(processNumber,actiontype);
		
	}


	public	void  processPatentWithToBeLapsedStatus() {
		
		list_of_patent_detail_map	=	dataFetcher.readAffectedFileIDList(configuration.userdocumentType,configuration.toBeLapsedStatusCode);
		logger.debug("################################################");
		logger.debug("=================================================");
		
		logger.debug("List of Patents that are in toBeLapsedStatus::"+list_of_patent_detail_map);
		logger.debug("=================================================");
		logger.debug("################################################");
		if(list_of_patent_detail_map.size()==0)
		{
			logger.info("Their are no patent which are in  \"To Be Lapsed\" status");
			
			stopUtility();
			System.exit(0);
		}
	}


	
	public	void  processUserDocumentOfAnnuityType() {
		
		
		list_of_userDoc_detail_map	=	dataFetcher.readUserDocumentOfTypeAnnuity(configuration.userdocumentType, configuration.pendingStatusCode);
		logger.debug("################################################");
		logger.debug("=================================================");
		logger.debug("User Document of annuity type::"+list_of_userDoc_detail_map);
		logger.debug("=================================================");
		logger.debug("################################################");
		
		if(list_of_userDoc_detail_map.size()==0)
		{
			logger.info("Their are no user document  of annuity type that are in \"Pending\" status.");
			stopUtility();
			System.exit(0);
		}
		
	}                                                                                                                                                        


	
	public void stopUtility(){
		if(dataFetcher.getConnection()!=null){
			logger.debug("Closing database connection..........!!");
			dataFetcher.closeConnection();
			logger.debug("Database connection closed..........!!");
			
		}
	
		logger.info("Processing Completed.......................!!!");
		logger.info("=================================================");
		logger.info("Please check processed details in log file generated in log folder");
		logger.info("********************************************************");
	}
	
}
