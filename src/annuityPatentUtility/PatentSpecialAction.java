package annuityPatentUtility;




import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.ipas.commons.CAckMessageNbrList;
import org.ipas.commons.CAckMessageTextList;
import org.ipas.commons.CActionId;
import org.ipas.commons.CActionTypeId;
import org.ipas.commons.CFileId;
import org.ipas.commons.CMark;
import org.ipas.commons.COptionList;
import org.ipas.commons.CPatent;
import org.ipas.commons.CProcessId;
import org.ipas.commons.CUserId;
import org.ipas.commons.CommonsProxyFactory;
import org.ipas.commons.IMark;
import org.ipas.commons.IPatent;
import org.ipas.commons.IProcess;
import org.ipas.proxy.IpasDateTime;
import org.ipas.proxy.IpasException;
import org.ipas.proxy.IpasInteger;

public class PatentSpecialAction
{

	private static Logger logger	=	Logger.getLogger(PatentSpecialAction.class);
	private	static ArrayList<CPatent> specialActionExecutedList	=	 new ArrayList<CPatent>();
	private IProcess  iProcess = null;
	private IPatent iPatent =	null;
	private CommonsProxyFactory commonsProxyFactory	=	null;

//	public static CommonsProxyFactory commonsProxyFactory;
//
//	public static void main(String[] args) {
//	
//		// create proxy factory based on the IIOP service address and port 
//		commonsProxyFactory = new CommonsProxyFactory("localhost:3700");
//			PatentSpecialAction psa = new PatentSpecialAction();
//		//	psa.processSpecialActionInsertion("MK", "P", "2016", "1", "1352","13/06/2016","565","4",commonsProxyFactory);
//			psa.deletePatentAction();
//	
//	}
	
	public static ArrayList<CPatent> getSpecialActionExecutedList(){
		
		return specialActionExecutedList;
		
	}
	
	public  void processSpecialActionInsertion(String fileSeq,String fileType,String fileSeries,
			String fileNumber,String actionType,String action_date,String patentGrantedStatusCode, String userNumber,CommonsProxyFactory commonsProxyFactory ) throws IpasException {
		
		logger.debug("Going to excecuting Special action on patent:::" + fileSeq+"/"+fileType+"/"+fileSeries+"/"+fileNumber );
			
		this.commonsProxyFactory	=	commonsProxyFactory;
		CFileId fileId = new CFileId();
		fileId.setFileSeq(fileSeq);
		fileId.setFileType(fileType);
		fileId.setFileSeries(new IpasInteger(fileSeries));
		fileId.setFileNbr(new IpasInteger(fileNumber));
	//	"19/05/2016"
		IpasDateTime actionDate = new IpasDateTime(action_date);
		// user nbr
		IpasInteger userNbr = new IpasInteger(userNumber);

	//	try {
			insertPatentSpecialAction(fileId, actionType, actionDate,patentGrantedStatusCode, userNbr);
	//	} 
//		catch (IpasException e) {
//			logger.error("insertPatentSpecialAction error:" + e.getMessage()+"\r\n");
//		}
//		
	}
	public  void insertPatentSpecialAction(
			// id of the mark
			CFileId fileId,		 
			// type of the action
			String actionType,
			// date of the action
			IpasDateTime actionDate,
			// user who took decision
			
			String patentGrantedStatusCode,
			IpasInteger userNbr
			
	) throws IpasException {
		
		CActionTypeId actionTypeId = null;
		CPatent patent	=	null;
		CProcessId patentProcessId = null;
		CUserId userId = null;
		CAckMessageNbrList ackMessageNbrList = null;
		CAckMessageTextList ackMessageTextList = null;
		COptionList optionList =  null;
		String notes1 = "";
		String notes2 = "";
		String notes3 = "";
		String notes4 = "";
		String notes5 = "";
		String notes = "";
		if(iPatent==null){
		 iPatent = commonsProxyFactory.getIPatent();
		}
		if(iProcess==null){
		 iProcess = commonsProxyFactory.getIProcess();
		}
		
		try {
			patent = iPatent.mRead(fileId, false, false);
		} catch (IpasException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured while reading the patent from database mread:::" + e.getMessage()+"\r\n");
		}
	
		 patentProcessId = patent.getFile().getProcessId();

		logger.debug("process id::"+patentProcessId.getProcessNbr().toString());
		// build action type for the decision
		 actionTypeId = new CActionTypeId();
		 actionTypeId.setActionType(actionType);

		// build user who took decision
		 userId = new CUserId();
		 userId.setUserNbr(userNbr);

		// build extra parameters which will not be used 

		 optionList = new COptionList();
			 ackMessageNbrList = new  CAckMessageNbrList();
		 ackMessageTextList = new  CAckMessageTextList();
		 //	add action
		try {
			
			iProcess.mInsertSpecialAction(patentProcessId, actionTypeId,patentGrantedStatusCode, actionDate, actionDate, userId, optionList, notes1, notes2, notes3, notes4, notes5, notes, ackMessageNbrList, ackMessageTextList, userId);
			
			logger.debug("Special Action on patent is executed successfully");
			//
			specialActionExecutedList.add(patent);
			
		//} catch (IpasException e) {
			// TODO Auto-generated catch block
		//	logger.error("inse  rtPatentSpecialAction error:" + e.getMessage()+"\r\n");
		}
		
		finally{
			
			
		}

		
		
	}
	
	
	public  void deletePatentAction(int actionNumber,CProcessId processId){
		CActionId actionId 	=	null;
		if(iProcess==null){
			 iProcess = commonsProxyFactory.getIProcess();
			}
		actionId 	=	 new CActionId();
		actionId.getProcessId().setProcessType(processId.getProcessType());
		
		actionId.getProcessId().setProcessNbr(new IpasInteger(processId.getProcessNbr()));
		
		actionId.setActionNbr(new IpasInteger(actionNumber));
		
		try {
			iProcess.mDeleteAction(actionId);
			
			logger.debug("Action rollbacked for processId::: "+processId.getProcessNbr().toString());
		} catch (IpasException e) {
			logger.error("Action cannot be rollbacked for processId::: "+processId.getProcessNbr().toString(),e);
			
		}
		
	}
	
	

}
