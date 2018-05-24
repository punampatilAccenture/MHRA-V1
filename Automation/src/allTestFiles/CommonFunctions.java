package allTestFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CommonFunctions   {
	public static String value = null;
	static Map<String, String> hm = new HashMap<String, String>();
	Utility obj = new Utility();
	public String getCellValue(String filename, String sheetname, int row, int col) {
		try {
//			value="";
			FileInputStream fTORead = new FileInputStream(new File(filename));
			XSSFWorkbook workbook = new XSSFWorkbook(fTORead);
			XSSFSheet sheet = workbook.getSheet(sheetname);
			XSSFCell cell = sheet.getRow(row).getCell(col);
			if (cell==null){ value="";}
			//if(!cell.equals("")) {
			else{			value = cell.toString();}//}else {value="";}
			workbook.close();
			fTORead.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}
		public static String getTimestamp() {
		String timeStamp = new SimpleDateFormat("MM-dd-yyyy_HH.mm.ss").format(new java.util.Date());
		return timeStamp;
	}
	public void createDataMap(String sheetname, String methodName){

		String testDataFile =driver.path+"\\TestData\\TestData.xlsx";
		try {
			FileInputStream fs = new FileInputStream(testDataFile);
			XSSFWorkbook fworkbook = new XSSFWorkbook(fs);
			XSSFSheet sh = fworkbook.getSheet(sheetname);
			boolean dataFound=false;
			int inCounter=0;
			String readTCname;
			String readIteration;
			
			do {
				inCounter=inCounter+1;
				readTCname=getCellValue(testDataFile,sheetname,inCounter,0);
				if(readTCname.equals(methodName)) {
					readIteration=getCellValue(testDataFile,sheetname,inCounter,1);
					if(Integer.parseInt(readIteration)==(driver.iterateRow)) {
						dataFound=true;
						break;
					}
				}
			}while(!dataFound);
			int totalNoOfCols = sh.getRow(inCounter).getLastCellNum();
		        for(int i=0; i < totalNoOfCols; i++){
		        	hm.put(getCellValue(testDataFile,sheetname,0,i ),getCellValue(testDataFile,sheetname, inCounter, i));
		        }
			//System.out.println(hm);
		        fworkbook.close();
		        fs.close();
		        
		} catch (IOException ex) {
		TestResult.reportEvent("Error in reading test data sheet", "Printing StackTrace as "+ex, "Fail");
			ex.printStackTrace();
		}

	}

	public String GetData(String ColumnName) {

		String valueToRead = null;
		try{
		
		valueToRead = hm.get(ColumnName).toString();
		}
		catch (Exception ex) {
			TestResult.reportEvent("Error in reading Column as "+ColumnName, "Printing StackTrace as "+ex, "Fail");
				ex.printStackTrace();
			}
		return valueToRead;

	}
	public static void createRunTimeFile(String pathtoCreate){
	try {
		
		File runtimefile = new File(pathtoCreate);
		if(!runtimefile.exists()){
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("RunTime");
		FileOutputStream out = new FileOutputStream(new File(pathtoCreate));
		sheet.createRow((short)0);
		sheet.createRow((short)1);
		workbook.write(out);
		out.flush();
		out.close();
		workbook.close();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public static void putValueInRuntime(String columnName, String valueToPut){
		try {
		File file = new File(driver.runtimePath);

		FileInputStream fIP = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fIP);
		XSSFSheet spreadsheet = workbook.getSheet("RunTime");
		
		XSSFRow row=spreadsheet.getRow(0);
		XSSFRow row2=spreadsheet.getRow(1);
		if (row2==null){
			row2=spreadsheet.createRow(1);
		}
		int endCol = spreadsheet.getRow(0).getLastCellNum();
		if(endCol<0){
			endCol=0;
		}else{
		 //int colNum = 0; 
			
		int lastCellInRowFind = row.getCell(row.getLastCellNum() - 1).getColumnIndex();	
		for (int icolFind=0;icolFind<lastCellInRowFind+1;icolFind++){
			XSSFCell cell = row.getCell(icolFind); 
		if(cell.toString().equals(columnName)){
			endCol=cell.getColumnIndex();
			break;
		}}
		}
		
		row.createCell(endCol).setCellValue(columnName);
		row2.createCell(endCol).setCellValue(valueToPut);		
		FileOutputStream out = new FileOutputStream(driver.runtimePath);
		workbook.write(out);
		out.close();
		//System.out.println("Written in details-Excel");
		workbook.close();

	} catch (Exception ex) {
		TestResult.reportEvent("Writing in RunTime XLS error","Writing in RunTime xls thorws error","Fail");
		TestResult.reportEvent("Writing in RunTime XLS error","Printing StackTrace as "+ex,"Fail");
		
	}
	}
	
public static String getValueFromRunTime(String columnName){
	String valueToReturn=null;
	try {
	FileInputStream fsR = new FileInputStream(driver.runtimePath);
	XSSFWorkbook workbookR = new XSSFWorkbook(fsR);
	XSSFSheet shR = workbookR.getSheet("RunTime");
	XSSFRow row=shR.getRow(0);	
	int lastCellInRow = row.getCell(row.getLastCellNum() - 1).getColumnIndex();	
	for (int icol=0;icol<lastCellInRow+1;icol++){
		XSSFCell cell = row.getCell(icol); 
	if(cell.toString().equals(columnName)){
		XSSFCell cellValue = shR.getRow(1).getCell(icol);
		valueToReturn=cellValue.toString();
		System.out.println(valueToReturn);
		break;
	}
	}
	workbookR.close();
	} catch (IOException ex) {
		TestResult.reportEvent("Error in reading Run time data sheet", "Printing StackTrace as "+ex, "Fail");
			ex.printStackTrace();
		}
	return valueToReturn;
}
}