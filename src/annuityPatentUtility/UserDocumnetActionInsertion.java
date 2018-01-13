package annuityPatentUtility;

import java.util.ArrayList;

import org.ipas.commons.CAckMessageNbrList;
import org.ipas.commons.CAckMessageTextList;
import org.ipas.commons.CActionId;
import org.ipas.commons.CActionTypeId;
import org.ipas.commons.CDocumentId;
import org.ipas.commons.COptionList;

import org.ipas.commons.CProcessId;
import org.ipas.commons.CUserId;
import org.ipas.commons.CUserdoc;
import org.ipas.commons.CUserdocType;
import org.ipas.commons.CommonsProxyFactory;

import org.ipas.commons.IProcess;
import org.ipas.commons.IUserdoc;

import org.ipas.proxy.IpasDateTime;
import org.ipas.proxy.IpasException;
import org.ipas.proxy.IpasInteger;
import org.apache.log4j.Logger;
//HANDLE ALL NULL POINTER EXCEPTION
public class UserDocumnetActionInsertion
{
	
	private IUserdoc iUserdoc= null;
	private IProcess  iProcess = null;
	private CommonsProxyFactory commonsProxyFactory 	=	null;
	private static ArrayList<CUserdoc> userDocumentActionExecutedList	=	 new ArrayList<CUserdoc>();
	private static Logger logger	=	Logger.getLogger(UserDocumnetActionInsertion.class);
//	public static void main(String[] args) {
//	//	 dbcfProxyFactory = new DBCFProxyFactory("localhost:8080");
//		// create proxy factory based on the IIOP service address and port 
//			commonsProxyFactory = new CommonsProxyFactory("localhost:3700");
//			UserDocumnetActionInsertion psa = new UserDocumnetActionInsertion();
//		
//		psa.processUserDocumentInsertion("MK", "E", "2016", "2","1116","19/05/2016","4","200","6",commonsProxyFactory );
//
//	}

	
	public static ArrayList<CUserdoc> getUserDocumentActionExecutedList(){
		
		return userDocumentActionExecutedList;
		
	}
	
	public  void processUserDocumentInsertion(String docORI,String docLOG,String docSER,String docNBR,
			String actionType,String actionDATE,String userNBR,	String procType,String procNbr,CommonsProxyFactory commonsProxyFactory ) throws IpasException{
		// get API objects implementing interfaces
		
		logger.debug("Executing action on Userdocument:::"+docORI+"/"+docLOG+"/"+docSER+"/"+docNBR+" :::from pending to accepted.....  ");
		this.commonsProxyFactory	=	commonsProxyFactory;
		CDocumentId cDocumentId = new CDocumentId();
		cDocumentId.setDocLog(docLOG);
		cDocumentId.setDocNbr(new IpasInteger(docNBR));
		cDocumentId.setDocOrigin(docORI);
		cDocumentId.setDocSeries(new IpasInteger(docSER));

		IpasDateTime actionDate = new IpasDateTime(actionDATE);
		// user nbr
		IpasInteger userNbr = new IpasInteger(userNBR);
		
	//	try {
			insertUserDocumentAction(cDocumentId, actionType, actionDate, userNbr,procType,procNbr);
//		} catch (IpasException e) {
			
	//		logger.error("insertUserDocumentAction error:" + e.getMessage()+"\r\n");
	//	}

	}
	
	

	public  void insertUserDocumentAction(CDocumentId cDocumentId,String actionType,
			IpasDateTime actionDate,IpasInteger userNbr,String procType,String procNbr ) throws IpasException  {
	
		logger.debug("Executing action on Userdocument from pending to accepted.....  ");
		
		CUserdoc userDocument 		= 	null;
		CUserdocType userdocType	=	null;
		CActionTypeId actionTypeId	=	null; 
		CUserId 		userId 		=	null;
		COptionList optionList 		=	null;
		CAckMessageNbrList ackMessageNbrList = null;
		CAckMessageTextList ackMessageTextList = null;
		String notes1 = "";
		String notes2 = "";
		String notes3 = "";
		String notes4 = "";
		String notes5 = "";
		String notes = "";
		if(iUserdoc==null){
		 iUserdoc= commonsProxyFactory.getIUserdoc();
		}
		if(iProcess==null){
		iProcess = commonsProxyFactory.getIProcess();
		}
		
		try {
			userDocument = iUserdoc.mRead(cDocumentId);
		} catch (IpasException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured while reading the userdocument from database mread:::" + e.getMessage()+"\r\n");
		}
	//	System.out.println("userDocument read::"+userDocument.getAffectedFileIdList().toString());
		
		if(userDocument.getFilingData().getUserdocTypeList().size()>0){
			
			logger.info("getting user name and userdocument type.....");
			
			
    		 userdocType=userDocument.getFilingData().getUserdocTypeList().get(0);
    		
    		
    		logger.debug("userdoc name:::"+userdocType.getUserdocName().toString());
    		logger.debug("userdoc Type::::"+userdocType.getUserdocName().toString());
    		
    	
	
			 
			CProcessId userDocumentProcessID =	userDocument.getUserdocProcessId();
			userDocumentProcessID.setProcessNbr(new IpasInteger(procNbr));
			userDocumentProcessID.setProcessType(procType);
			


//		// build action type for the decision
		 actionTypeId = new CActionTypeId();
		 actionTypeId.setActionType(actionType);

		// build user who took decision
		 userId = new CUserId();
		 userId.setUserNbr(userNbr);

		// build extra parameters which will not be used 


		 optionList = new COptionList();
	
		 ackMessageNbrList = new  CAckMessageNbrList();
		 ackMessageTextList = new  CAckMessageTextList();
		
		logger.debug("Going to insert user doc accepted action....");
	//	try {
			iProcess.mInsertAction(userDocumentProcessID, actionTypeId, actionDate, actionDate, userId, optionList, notes1, notes2, notes3, notes4, notes5, notes, ackMessageNbrList, ackMessageTextList, userId, userNbr);
			
			logger.debug("User Document Action inserted successfully");
			
			userDocumentActionExecutedList.add(userDocument);
//		} catch (IpasException e) {
//			// TODO Auto-generated catch block
//			logger.error("insertUserDocumentAction error:" + e.getMessage()+"\r\n");
//		}

	
	}
	

	
	else{
		
		logger.warn("User Document Action is not executed as no such user document can be found.....");
		
	}
		
		
}
	
	
	
		public  void deleteUserDocumentAction(int actionNumber,CProcessId processId){
			
			if(iProcess==null){
				iProcess = commonsProxyFactory.getIProcess();
				}
			CActionId actionId 	=	 new CActionId();
			actionId.getProcessId().setProcessType(processId.getProcessType());
			actionId.getProcessId().setProcessNbr(processId.getProcessNbr());
			actionId.setActionNbr(new IpasInteger(actionNumber));
				
		
				try {
					iProcess.mDeleteAction(actionId);
					
					logger.debug("UserDocumnet Action rollbacked for processId::: "+processId.toString());
				} catch (IpasException e) {
					logger.error("UserDocumnet Action cannot be rollbacked for processId::: "+processId.toString(),e);
					
				}
				
			}
	
	
	
	
}
