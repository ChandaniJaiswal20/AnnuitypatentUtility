package annuityPatentUtility;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;




public class UtilityConfiguration {

	ConfigParam	config	=	null;
	public String iiopPort;
	public String iiopHost;
	public String databaseHost;
	public String databasePort;
	public String databaseTool;
	public String databaseName;
	public String userName;
	public String password;
	public String userdocumentType;
	public String patentSpecialActionType;
	public String patentActionDate;
	public String patentUserID;
	public String userDocumentAcceptedActionType;
	public String userDocumentActionDate;
	public String userDocumentUserID;
	public String patentAnnuityUtilityFolder;
	public String toBeLapsedStatusCode;
	public String pendingStatusCode;
	public String patentGrantedStatusCode;
	public static Logger logger	= Logger.getLogger(UtilityConfiguration.class);

	public UtilityConfiguration loadConfiguration() {
		logger.debug("loading configuration.....");
			config	= new ConfigParam("patentAnnuitityConfig.properties");
		//	patentAnnuityUtilityFolder	=	config.getProperty("patentAnnuityUtilityFolder");
			iiopPort	= config.getProperty("iiopPort");
			iiopHost	= config.getProperty("iiopHost");
			databaseHost	= config.getProperty("databaseHost");
			databasePort	= config.getProperty("databasePort");
			databaseTool	= config.getProperty("databaseTool");
			databaseName	=	config.getProperty("databaseName");
			userName		=	config.getProperty("userName");
			password		=	config.getProperty("password");
			userdocumentType	=	config.getProperty("userdocumentType");
			patentSpecialActionType		=	config.getProperty("patentSpecialActionType");
			patentActionDate		=	config.getProperty("patentActionDate");
			patentUserID		=	config.getProperty("patentUserID");
			patentGrantedStatusCode		=	config.getProperty("patentGrantedStatusCode");
			userDocumentAcceptedActionType		=	config.getProperty("userDocumentAcceptedActionType");
			userDocumentActionDate		=	config.getProperty("userDocumentActionDate");
			userDocumentUserID		=	config.getProperty("userDocumentUserID");
			toBeLapsedStatusCode		=	config.getProperty("toBeLapsedStatusCode");
			pendingStatusCode				=	config.getProperty("pendingStatusCode");
			
			logger.debug("IIOP PORT:::"+iiopPort);
			logger.debug("iiopHost:::"+iiopHost);
			logger.debug("databaseHost:::"+databaseHost);
			logger.debug("databasePort:::"+databasePort);
			logger.debug("databaseTool:::"+databaseTool);
			logger.debug("databaseName:::"+databaseName);
			logger.debug("userName:::"+userName);
			logger.debug("userdocumentType:::"+userdocumentType);
			logger.debug("patentSpecialActionType:::"+patentSpecialActionType);
			logger.debug("patentActionDate:::"+patentActionDate);
			logger.debug("patentUserID:::"+patentUserID);
			logger.debug("patentGrantedStatusCode:::"+patentGrantedStatusCode);
			logger.debug("userDocumentAcceptedActionType:::"+userDocumentAcceptedActionType);
			logger.debug("userDocumentActionDate:::"+userDocumentActionDate);
			logger.debug("userDocumentUserID:::"+userDocumentUserID);
			logger.debug("toBeLapsedStatusCode:::"+toBeLapsedStatusCode);
			logger.debug("pendingStatusCode:::"+pendingStatusCode);
			
			
			return this;
				
	}
	
	
	public void checkValuesOfConfigFile(){
		
		if(pendingStatusCode==null){
			
			logger.error("Please specify value of  pendingStatusCode in patentAnnuitityConfig.properties");
			
			 throw new MissingConfigurationException("Please specify value of  pendingStatusCode in patentAnnuitityConfig.properties");
			
		}
		
//		if(patentAnnuityUtilityFolder==null){
//			
//			logger.error("Please specify value of  patentAnnuityUtilityFolder in patentAnnuitityConfig.properties");
//			throw new MissingConfigurationException("Please specify value of  patentAnnuityUtilityFolder in patentAnnuitityConfig.properties");
//			
//		}
		if(iiopPort==null||iiopPort.isEmpty()){
			
			logger.error("Please specify value of  iiopPort in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  iiopPort in patentAnnuitityConfig.properties");
		}
		
		if(iiopHost==null||iiopHost.isEmpty()){
			
			logger.error("Please specify value of  iiopHost in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  iiopHost in patentAnnuitityConfig.properties");
		}
		
		if(databaseHost==null||databaseHost.isEmpty()){
			
			
			logger.error("Please specify value of  databaseHost in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  databaseHost in patentAnnuitityConfig.properties");
		}
		
		if(databasePort==null||databasePort.isEmpty()){
			
			
			logger.error("Please specify value of  databasePort in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  databasePort in patentAnnuitityConfig.properties");
		}
		
		if(databaseTool==null||databaseTool.isEmpty()){
			
			logger.error("Please specify value of  databaseTool in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  databaseTool in patentAnnuitityConfig.properties");
			
		}
		else if(!(databaseTool.equalsIgnoreCase("sql")||databaseTool.equalsIgnoreCase("oracle"))){
			
			logger.error("Please specify correct database tool in patentAnnuitityConfig.properties ie. for oracle database::<oracle> and for sql database::<sql>");
			throw new MissingConfigurationException("Please specify correct database tool in patentAnnuitityConfig.properties ie. for oracle database::<oracle> and for sql database::<sql>");
		}
		
		if(databaseName==null||databaseName.isEmpty()){
			
			
			logger.error("Please specify value of  databaseName in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  databaseName in patentAnnuitityConfig.properties");
		}
		
		if(userName==null||userName.isEmpty()){
			
			logger.error("Please specify value of  userName in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  userName in patentAnnuitityConfig.properties");
		}
		
		if(password==null||password.isEmpty()){
			
			
			logger.error("Please specify value of  password in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  password in patentAnnuitityConfig.properties");
		}
		
		if(userdocumentType==null||userdocumentType.isEmpty()){
			
			
			logger.error("Please specify value of  userdocumentType in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  userdocumentType in patentAnnuitityConfig.properties");
		}
		

		
		if(patentActionDate==null||patentActionDate.isEmpty()){
			
			
			logger.error("Please specify value of  patentActionDate in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  patentActionDate in patentAnnuitityConfig.properties");
		}
		if(patentUserID==null||patentUserID.isEmpty()){
			
			
			logger.error("Please specify value of  patentUserID in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  patentUserID in patentAnnuitityConfig.properties");
		}
		
		
		
		
		if(patentGrantedStatusCode==null||patentGrantedStatusCode.isEmpty()){
			
			
			logger.error("Please specify value of  patentGrantedStatusCode in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  patentGrantedStatusCode in patentAnnuitityConfig.properties");
		}
		
		
		if(userDocumentAcceptedActionType==null||userDocumentAcceptedActionType.isEmpty()){
			
			logger.error("Please specify value of  userDocumentAcceptedActionType in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  userDocumentAcceptedActionType in patentAnnuitityConfig.properties");
		}
		
		if(userDocumentActionDate==null||userDocumentActionDate.isEmpty()){
			
			
			logger.error("Please specify value of  userDocumentActionDate in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  userDocumentActionDate in patentAnnuitityConfig.properties");
		}
		
		if(userDocumentUserID==null||userDocumentUserID.isEmpty()){
			
			
			logger.error("Please specify value of  userDocumentUserID in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  userDocumentUserID in patentAnnuitityConfig.properties");
		}
		
		if(toBeLapsedStatusCode==null||toBeLapsedStatusCode.isEmpty()){
			
			
			logger.error("Please specify value of  toBeLapsedStatusCode in patentAnnuitityConfig.properties");
			throw new MissingConfigurationException("Please specify value of  toBeLapsedStatusCode in patentAnnuitityConfig.properties");
		}
		
	}
	
	
	
	public void checkDateFormat(){
		
		
		
	}
	
	public static void main(String[] args) {
		
		UtilityConfiguration uc = new UtilityConfiguration();
	
			
	//String		log4jConfigFile	=	System.getProperty("user.dir")+File.separator+"log4j.properties";
		
		String		log4jConfigFile	=	"D://PatentAnnuityUtility//log4j.properties";
			
			PropertyConfigurator.configure(log4jConfigFile);
			logger.info("log4jConfigFile:::"+log4jConfigFile);	
	
		uc.loadConfiguration();
		uc.checkValuesOfConfigFile();
		
		
		//ths 
	}
	
}
