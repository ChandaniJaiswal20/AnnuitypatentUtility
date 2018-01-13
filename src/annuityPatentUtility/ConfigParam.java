package annuityPatentUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigParam {
	
	private static Properties prop	=	null;
	private   String fileName	=	null;
	public ConfigParam(String fileName){
		
		this.fileName	=	fileName;
		
	}
	
	
	public  void initialise() {
		
		FileReader fr = null;
		try {
			
			
			fr = new FileReader(fileName);
		
			prop	=	new Properties();
			
			prop.load(fr);

			fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	public String getProperty(String property) {
		
		String proper="";
		if(prop==null)
			initialise();
		proper	=		prop.getProperty(property);
		return proper;
		
		
		
	}
	
	
	public static void main(String[] args) {
		ConfigParam pm=new ConfigParam("patentAnnuitityConfig.properties");
		
		System.out.println(pm.getProperty("iiopHost"));
		System.out.println(pm.getProperty("iiopPort"));
		System.out.println(pm.getProperty("databaseHost"));
		System.out.println(pm.getProperty("databaseTool")) ;
		System.out.println(pm.getProperty("databasePort"));
		System.out.println(pm.getProperty("databaseName"));
		System.out.println(pm.getProperty("userName"));
		System.out.println(pm.getProperty("password"));
		System.out.println(pm.getProperty("userdocumentType"));
		
		System.out.println(pm.getProperty("userID"));
		
	}
	

}
