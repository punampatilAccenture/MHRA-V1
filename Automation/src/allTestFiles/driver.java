package allTestFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import views.html.ac.internal.production_doc;

import org.apache.poi.ss.formula.functions.Fixed;
import org.apache.poi.xssf.usermodel.XSSFCell;

@SuppressWarnings("static-access")
public final class driver {
	private static String timeStamp = null;
	private static String description = null;
	private static String fnName = null;
	public static String fnID = null;
	public static int strtIteration = 1;
	public static int endIteration = 1;
	public static int iterateRow=0;
	public static boolean canRunValue=true;
	public static boolean uploadToJira;
	public static long endTime;
	
	public static String path=System.getProperty("user.dir");
	//public static String path="C:\\AutomationSuite";
	public static String runtimePath=driver.path+"\\TestData\\RunTime.xlsx";//createRuntime data file
	
	public static void main(String[] args) throws IOException {
		//Procurement ProcurementRun=new Procurement();
		
		CommonFunctions cfUtil = new CommonFunctions();
		TestResult reports = new TestResult();
		timeStamp = CommonFunctions.getTimestamp();
		
		// ************************initialize reporting here'''''****************************************************************
		cfUtil.createRunTimeFile(runtimePath);
		reports.createFolderStructure(timeStamp);
		reports.createSummaryFile();
		// ************************reporting initialized**************************************************************
		
		String excelFilePath = driver.path+"\\MasterSheet.xlsx";
		//ArrayList<String> executeSheets = new ArrayList<String>();
		ArrayList<String> tcExecuteMethods = new ArrayList<String>();
		ArrayList<String> Description = new ArrayList<String>();
		ArrayList<String> artoID = new ArrayList<String>();
		ArrayList<Integer> stIter = new ArrayList<Integer>();
		ArrayList<Integer> endIter = new ArrayList<Integer>();
		
		try {		
			FileInputStream file = new FileInputStream(new File(excelFilePath));

			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet masterSheet = workbook.getSheet("Master");
			int rows;
			rows = masterSheet.getLastRowNum();
			String trueCase;	
			canRunValue=Boolean.parseBoolean(cfUtil.getCellValue(driver.path+"\\TestData\\TestData.xlsx", "RunSetting",1, 0));
			uploadToJira=Boolean.parseBoolean(cfUtil.getCellValue(driver.path+"\\TestData\\TestData.xlsx", "RunSetting",1, 1));
			System.out.println("run value read is :"+canRunValue);
			
			for(int iTrue=1;iTrue<rows+1;iTrue++) {
				
				XSSFCell cell = masterSheet.getRow(iTrue).getCell(2);
				trueCase = cell.toString();
				if (trueCase.equalsIgnoreCase("TRUE")) {
				fnName = cfUtil.getCellValue(excelFilePath, "Master", iTrue, 5);
				description = cfUtil.getCellValue(excelFilePath, "Master", iTrue, 1);
				strtIteration=Integer.parseInt(cfUtil.getCellValue(excelFilePath, "Master", iTrue, 3));
				endIteration=Integer.parseInt(cfUtil.getCellValue(excelFilePath, "Master", iTrue, 4));
				fnID= cfUtil.getCellValue(excelFilePath, "Master", iTrue, 0);
				
				System.out.println("Added for execution : "+fnName);
				tcExecuteMethods.add(fnName);
				Description.add(description);
				stIter.add(strtIteration);
				endIter.add(endIteration);
				artoID.add(fnID);
			}
			}
			workbook.close();
			file.close();
				
			for (int iRow = 0; iRow <tcExecuteMethods.size() ; iRow++) {
				fnName=tcExecuteMethods.get(iRow);
				description=Description.get(iRow);
				iterateRow=stIter.get(iRow);
				endIteration=endIter.get(iRow);
				iterateRow=strtIteration;
				fnID=artoID.get(iRow);
					//put method names below----
					
					 for (;iterateRow<endIteration+1;iterateRow++)	{
						 switch (fnName) {
//******************S T A R T O F I N I T I A L I S A T I O N******************************	
						 

				}
					//do not change code below
					endTime = System.nanoTime();
				reports.addInSummary(fnID, description, endTime);
				}//ending for
			}
//		}	
			if(fnName!=null) {}else {
				reports.addInSummary("No Test Case Selected", "No test case selected to Execute",0);
			}
			reports.footerSummary();
			
		}	//ending try

		catch (Exception e) {
			reports.reportEvent("Master Sheet Error","Printing StackTrace as "+e,"Fail");
		}
	}
}//end of driver class