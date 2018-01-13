package annuityPatentUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilityController {
	
	
	
	public static void main(String[] args) {
		
				
				
				UtilityManager	um =	UtilityManager.getInstance();
				
			//	um.init();
		
				um.processPatentWithToBeLapsedStatus();
				um.processUserDocumentOfAnnuityType();
				um.proceesAllPatent();
				um.stopUtility();
	
			}
		
	}
	


