package allTestFiles;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
@SuppressWarnings("deprecation")
public class TestResult {

	public static File exeRepFdr = null;
	public static File htmlDIR = null;
	public static File exclDIR = null;
	public static File screenshotDIR = null;
	public static File screenshotMainDIR = null;
	
	public static String runfdr=null;
	
	public static String schotPath = null;
	public static String filepathHTML = null;
	public static String filepathExl = null;
	public static int stepNo = 0;
	public static int stpInExcel = 0;
	public static String detailFilepathHTML = null;
	public static String detailFilepathExl = null;
	public static String finalStatus = null;

	public static double execTime = 0;
	public static long startTime = 0;
	public static long endTime = 0;
	
	public static long StepStartTime = 0;
	public static String stepExecTime;

	
	
	
	public static long summaryStartTime = 0;
	public static long summaryEndTime = 0;
	
	public String summTimeStart=null;
	public String summTimeEnd=null;
	
	public static int passCount;
	public static int failCount;

	public static void reportEvent(String stepName, String StepDescription, String Status) {
		reportEvent_HTML(stepName, StepDescription, Status);
		reportEvent_Excel(stepName, StepDescription, Status);
	}
	public void createFolderStructure(String tstamp) {
		exeRepFdr = new File(driver.path+"\\ExecutionReports\\Run_" + tstamp);// use
																			
		exeRepFdr.mkdirs();// mkdirs
		htmlDIR = new File(exeRepFdr + "\\HTML Reports");
		exclDIR = new File(exeRepFdr + "\\Excel Reports");
		screenshotMainDIR = new File(exeRepFdr + "\\Screenshots");
		runfdr="Run_" + tstamp;	

		if (!htmlDIR.exists()) {
			System.out.println("creating directory: " + htmlDIR);

			try {
				htmlDIR.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} // html fodler done
		if (!exclDIR.exists()) {
			System.out.println("creating directory: " + exclDIR);

			try {
				exclDIR.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		} // excel folder done

		if (!screenshotMainDIR.exists()) {
			System.out.println("creating directory: " + screenshotMainDIR);

			try {
				screenshotMainDIR.mkdir();
			} catch (SecurityException se) {
				reportEvent("Folder Structure Error","Printing StackTrace as "+se,"Fail");
				//se.printStackTrace();
			}
		} // screenshots done
	}
	public void createSummaryFile() {
		filepathHTML = htmlDIR + "\\Summary.html";
		filepathExl = exclDIR + "\\Summary.xlsx";
		passCount=0;
		failCount=0;
		summaryStartTime = System.nanoTime();
		summTimeStart = new SimpleDateFormat("HH:mm:ss a").format(Calendar.getInstance().getTime());
		try {

			File dfile = new File(filepathHTML);
			FileWriter dwriter = new FileWriter(dfile, true);

			dwriter.write("<html>");
			dwriter.write("\r\n");
			dwriter.write("<title>Execution Report</title>");
			dwriter.write("\r\n");
			dwriter.write("<head><style>\r\n" +"a{color: Blue;}\r\n" + 
					"a:visited{color:Purple;}\r\n" + 
					"</style></head>");
			dwriter.write("\r\n");
			dwriter.write("<body>");
			dwriter.write("\r\n");
			dwriter.write("<font face='Tahoma'size='2'>");
			dwriter.write("\r\n");
			dwriter.write("<h1>Test Results Summary</h1>");
			dwriter.write("\r\n");
			dwriter.write("<table border='0' bordercolor='#000000' width='100%' height='47'>");
			dwriter.write("\r\n");
			dwriter.write("<tr>");
			dwriter.write("\r\n");

			String data = "<tr><td width='13%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>TestCaseID"
					+ "</b></font></td><td width='24%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>TC Description"
					+ "</b></font></td><td width='18%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Status"
					+ "</font></td><td width='23%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Execution Time(Mins)</b></font></td>";
			// File file =new File(detailFilepathHTML);
			// FileWriter fileWritter = new FileWriter(dfile,true);
			BufferedWriter bufferWritter = new BufferedWriter(dwriter);
			bufferWritter.write(data);
			bufferWritter.close();
			dwriter.close();

		} catch (Exception e) {
			reportEvent("Create Summary HTML Error","Printing StackTrace as "+e,"Fail");
		}
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			// Create a blank spreadsheet
			XSSFSheet spreadsheet = workbook.createSheet("Summary");

			XSSFCellStyle style = workbook.createCellStyle();
			// style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			style.setFillBackgroundColor(HSSFColor.BLUE.index);
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBold(true);
			font.setItalic(false);

			style.setFont(font);

			style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);

			XSSFRow row = spreadsheet.createRow((short) 0);
			row.createCell(0).setCellValue("TestCaseID");
			row.createCell(1).setCellValue("Description");
			row.createCell(2).setCellValue("Status");
			row.createCell(3).setCellValue("ExecutionTime(Mins)");

			row.getCell(0).setCellStyle(style);
			row.getCell(1).setCellStyle(style);
			row.getCell(2).setCellStyle(style);
			row.getCell(3).setCellStyle(style);

			FileOutputStream out = new FileOutputStream(new File(filepathExl));
			//System.out.println("Excel Summary File Created");

			spreadsheet.autoSizeColumn(0);
			spreadsheet.autoSizeColumn(1);
			spreadsheet.autoSizeColumn(2);
			spreadsheet.autoSizeColumn(3);

			workbook.write(out);
			out.flush();
			out.close();
			workbook.close();
		} catch (Exception ex) {
			reportEvent("Create Summary XLS Error","Printing StackTrace as "+ex,"Fail");
		}
	}
	public void createDetailsFile(String filename) {
		filename=driver.fnID;
		startTime = System.nanoTime();
		StepStartTime= System.nanoTime();
		detailFilepathHTML = htmlDIR + "\\" + filename + ".html";
		detailFilepathExl = exclDIR + "\\" + filename + ".xlsx";
		
		screenshotDIR = new File(screenshotMainDIR + "\\"+filename);
		if (!screenshotDIR.exists()) {
			System.out.println("creating directory: " + screenshotDIR);
		try {
			screenshotDIR.mkdir();
		} catch (SecurityException se) {
			reportEvent("Folder Structure Error","Printing StackTrace as "+se,"Fail");
			//se.printStackTrace();
		}
		}
		
		try {
			stepNo = 1;
			finalStatus = "Pass";
			File dfile = new File(detailFilepathHTML);
			FileWriter dwriter = new FileWriter(dfile, true);

			dwriter.write("<html>");
			dwriter.write("\r\n");
			dwriter.write("<title>" + filename + " Details</title>");
			dwriter.write("\r\n");
			dwriter.write("<head><style>\r\n" + 
					"a{color: Blue;}\r\n" + 
					"a:visited{color:Purple;}\r\n" + 
					"</style></head>");
			dwriter.write("\r\n");
			dwriter.write("<body>");
			dwriter.write("\r\n");
			dwriter.write("<font face='Tahoma'size='2'>");
			dwriter.write("\r\n");
			dwriter.write("<h1>" + filename + " Details Iteration "+driver.iterateRow+"</h1>");
			dwriter.write("\r\n");
			dwriter.write("<table border='1' bordercolor='#000000' width='100%' height='47'>");
			dwriter.write("\r\n");
			dwriter.write("<tr>");
			dwriter.write("\r\n");
			String data = "<tr><td width='4%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Step No."
					+ "</b></font></td><td width='20%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Step Detail"
					+ "</b></font></td><td width='32%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Actual Result"
					+ "</b></font></td><td width='10%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Status"
					+ "</b></font></td><td width='8%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Time"
					+ "</font></td></tr>";
					//+ "<td width='8%' bgcolor='#5D4037' align='center'><font color='#FFFFFF' face='Tahoma' size='2'><b>Screenshot</b></font></td></tr>";
			BufferedWriter bufferWritter = new BufferedWriter(dwriter);
			bufferWritter.write(data);
			bufferWritter.close();
			dwriter.close();
			//System.out.println("Detail file Header Created");
		} catch (Exception e) {
			reportEvent("Create Details HTML","Printing StackTrace as "+e,"Fail");
		}

		// create Excel Detail

		try {
			
			stpInExcel = 1;
			XSSFWorkbook workbook;
			XSSFSheet spreadsheet;
			int exclLastUsedRow;
			
			if(!new File(detailFilepathExl).exists()) {//for new file
				// Create a blank spreadsheet
				workbook = new XSSFWorkbook();
				spreadsheet= workbook.createSheet(filename);
				exclLastUsedRow=-1;
				}else {//for already existing file
					
					FileInputStream inputStream = new FileInputStream(new File(detailFilepathExl));
					workbook = new XSSFWorkbook(inputStream);	
					spreadsheet = workbook.getSheetAt(0);
					exclLastUsedRow=spreadsheet.getLastRowNum();
				}
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillBackgroundColor(HSSFColor.BLUE.index);
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBold(true);
			font.setItalic(false);

			style.setFont(font);

			style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
						
			XSSFRow row0 = spreadsheet.createRow((short)exclLastUsedRow+1);
			row0.createCell(0).setCellValue("Iteration "+driver.iterateRow);
			XSSFRow row = spreadsheet.createRow((short) exclLastUsedRow+2);
			row.createCell(0).setCellValue("Step Number");
			row.createCell(1).setCellValue("Step Detail");
			row.createCell(2).setCellValue("Actual Result");
			row.createCell(3).setCellValue("Status");
			row.createCell(4).setCellValue("Time");
			//row.createCell(5).setCellValue("Screenshot");
			
			row0.getCell(0).setCellStyle(style);
			row.getCell(0).setCellStyle(style);
			row.getCell(1).setCellStyle(style);
			row.getCell(2).setCellStyle(style);
			row.getCell(3).setCellStyle(style);
			row.getCell(4).setCellStyle(style);
			//row.getCell(5).setCellStyle(style);
			
			 FileOutputStream out = new FileOutputStream(new File(detailFilepathExl));
			
			//System.out.println("Excel Detail File Created");

			spreadsheet.autoSizeColumn(0);
			spreadsheet.autoSizeColumn(1);
			spreadsheet.autoSizeColumn(2);
			spreadsheet.autoSizeColumn(3);
			spreadsheet.autoSizeColumn(4);
			//spreadsheet.autoSizeColumn(5);

			workbook.write(out);
			out.flush();
			out.close();
			workbook.close();
		} catch (Exception ex) {
			reportEvent("Create Details XLS Error","Printing StackTrace as "+ex,"Fail");
		}

	}
	public static void reportEvent_HTML(String stepDetail, String ActualResult, String Status) {
		
		//long StepEndTime = System.nanoTime();
		
		//double stepElapsedTime=(StepEndTime - StepStartTime);
		
		stepExecTime= CommonFunctions.getTimestamp().split("_")[1];
		//stepExecTime=stepElapsedTime / 600000000.0;
		//stepExecTime=Math.round(stepExecTime * 100.0) / 100.0;
		//stepExecTime=Math.round(stepExecTime) / 100.0;
		
		String incolor = null;
		try {
			if (Status.equalsIgnoreCase("Fail")) {
				finalStatus = "Fail";
			}
			if (Status.equalsIgnoreCase("Pass")) {
				incolor = "Green";
			} else if(Status.equalsIgnoreCase("JiraException")) {
					incolor = "Red";	
					Status="Fail";
			}else {
				//incolor = "#E9967A";
				incolor = "Red";
			}
				String data;
				schotPath = captureScreenshot();
				if (schotPath!=null) {
			data = "<tr><td width='5%' bgcolor='#ECEFF1' align='center'><font color='#000000' face='Tahoma' size='2'>"
					+ stepNo
					+ "</font></td><td width='24%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'>&nbsp;&nbsp;&nbsp;<a style=\"text-decoration: none;\"HREF=../" + schotPath+">"
					+ "<font face='Tahoma' size='2'><b>"+stepDetail+"</b></font></a>"
					+ "</font></td><td width='28%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'>&nbsp;&nbsp;&nbsp;"
					+ ActualResult + "</font></td><td width='7%' bgcolor='#ECEFF1' align='center'><font color='"
					+ incolor + "' face='Tahoma' size='2'><b>" + Status
					+ "</b></font></td>"
					+"<td width='7%' bgcolor='#ECEFF1' align='center'><font color='Black' face='Tahoma' size='2'>" + stepExecTime					
					+ "</font></td></tr>";
					//+ "<td width='18%' bgcolor='#ECEFF1' align='center'><a HREF=../" + schotPath
					//+ "><font color='Blue' face='Tahoma' size='2'><b>Screenshot</b></font></a></td></tr>";
				} else {
					data = "<tr><td width='5%' bgcolor='#ECEFF1' align='center'><font color='#000000' face='Tahoma' size='2'>"
							+ stepNo
							+ "</font></td><td width='24%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'>&nbsp;&nbsp;&nbsp;"
							+ stepDetail
							+ "</font></td><td width='28%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'>&nbsp;&nbsp;&nbsp;"
							+ ActualResult + "</font></td><td width='7%' bgcolor='#ECEFF1' align='center'><font color='"
							+ incolor + "' face='Tahoma' size='2'><b>" + Status
							+ "</b></font></td>"
							+"<td width='7%' bgcolor='#ECEFF1' align='center'><font color='Black' face='Tahoma' size='2'>" + stepExecTime					
							+ "</font></td></tr>";
						//	+ "<td width='18%' bgcolor='#ECEFF1' align='center'></td></tr>";
				}
			File file = new File(detailFilepathHTML);
			FileWriter fileWritter = new FileWriter(file, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();
			//System.out.println("Written in details-HTML");
			stepNo++;
			
			//StepStartTime=StepEndTime;			
		} catch (Exception e) {
			//StepStartTime=StepEndTime;
			reportEvent("Writing in details HTML error","Printing StackTrace as "+e,"Fail");
		}
	}
	public static void reportEvent_Excel(String stepDetail, String ActualResult, String Status) {
		try {

			File file = new File(detailFilepathExl);

			FileInputStream fIP = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fIP);
			XSSFSheet spreadsheet = workbook.getSheetAt(0);
			int endRow = spreadsheet.getLastRowNum();

			XSSFRow row = spreadsheet.createRow((short) endRow + 1);

			XSSFCell cell;
			row.createCell(0).setCellValue(stpInExcel);
			row.createCell(1).setCellValue(stepDetail);
			row.createCell(2).setCellValue(ActualResult);
			row.createCell(3).setCellValue(Status);
			row.createCell(4).setCellValue(stepExecTime);
			
			if (schotPath!=null) {
			CreationHelper createHelper = workbook.getCreationHelper();
			XSSFCellStyle hlinkstyle = workbook.createCellStyle();
			XSSFFont hlinkfont = workbook.createFont();
			hlinkfont.setUnderline(XSSFFont.U_SINGLE);
			hlinkfont.setColor(HSSFColor.BLUE.index);
			hlinkstyle.setFont(hlinkfont);
			cell = row.createCell((short) 5);
			cell.setCellValue("Screenshot");


			XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_FILE);
			//System.out.println("for schot");
			schotPath = schotPath.replace("\\", "/");
			link.setAddress("../"+schotPath);
			cell.setHyperlink(link);
			cell.setCellStyle(hlinkstyle);
			}
			spreadsheet.autoSizeColumn(0);
			spreadsheet.autoSizeColumn(1);
			spreadsheet.autoSizeColumn(2);
			spreadsheet.autoSizeColumn(3);
			spreadsheet.autoSizeColumn(4);
			spreadsheet.autoSizeColumn(5);

			FileOutputStream out = new FileOutputStream(detailFilepathExl);
			workbook.write(out);
			out.close();
			//System.out.println("Written in details-Excel");
			stpInExcel++;
			workbook.close();

		} catch (Exception ex) {
			TestResult.reportEvent("Writing in details XLS error","Printing StackTrace as "+ex,"Fail");
		}
	}
	public static String captureScreenshot() {
		String scLink=null;
		try {
			boolean hasQuit = Utility.wdriver.toString().contains("(null)");
			if(!hasQuit)
			 {
			        TakesScreenshot ts = (TakesScreenshot)Utility.wdriver;
			        File source = ts.getScreenshotAs(OutputType.FILE);
			        schotPath = screenshotDIR.toString() + "\\TCId_" + CommonFunctions.getTimestamp() + ".png";
			        File destination = new File(schotPath);
			        FileUtils.copyFile(source, destination);
			        scLink=schotPath.split(runfdr)[1];
			     //   return scLink[1];
			       } 
			//else {
//				
//				schotPath = screenshotDIR.toString() + "\\TCId_" + CommonFunctions.getTimestamp() + ".png";
			//	scLink=schotPath.split(runfdr);
				//scLink[1]= "";
				//sclinj=null;
//				System.out.println("screenshot taken and kept at "+scLink[1]);
//				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//				BufferedImage capture = new Robot().createScreenCapture(screenRect);
//				ImageIO.write(capture, "png", new File(schotPath));
			//}
		}
		
		catch (NullPointerException e) {
			//reportEvent("Browser not initialised","Printing StackTrace as "+e,"Fail");
			
		}
		catch (Exception e) {
			reportEvent("Error in taking screenshot","Printing StackTrace as "+e,"Fail");
			e.printStackTrace();
		}
	//return scLink[1];
		return scLink;
	}
	public void addInSummary(String tcID, String desc, long totalTime) {
		endTime = totalTime;
		double elapsedTime = (endTime - startTime);
		execTime = elapsedTime / 60000000000.0;
		execTime = Math.round(execTime * 100.0) / 100.0;
		if(!desc.contains("No test case selected to Execute")) {
		closeDetailFile();
		addInHTML(tcID, desc, totalTime);
		addInExcel(tcID, desc, totalTime);
		createStatusTextFile(tcID);
	}else {addInHTML(tcID, desc, totalTime);
	}
	}
	public void addInHTML(String tcID, String desc, long totalTime) {
		//detailFilepathHTML = detailFilepathHTML.replaceAll("\\s", "%20");
		
		if(desc.contains("No test case selected to Execute")) {
			try {
				String data = "<tr><td width='13%' bgcolor='#ECEFF1' align='center'><font color='#000000' face='Tahoma' size='2'><b>No Test Case Selected"
						+ "</b></font></td><td width='24%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'><b>"
						+ "No test case selected to Execute </b></font></td><td width='18%' bgcolor='#ECEFF1' align='center'>"
						+ "<b>Done"
						+ "</b></font></td><td width='23%' bgcolor='#ECEFF1' align='center'><font color='#000000' face='Tahoma' size='2'>"
						+ execTime + "</font></td></tr>";
				File file = new File(filepathHTML);
				FileWriter fileWritter = new FileWriter(file, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.close();
				System.out.println("Written in Summary-HTML");

			} catch (Exception e) {
				reportEvent("Writing in Summary HTML error","Printing StackTrace as "+e,"Fail");
			}
		}else {
		String detFP=detailFilepathHTML.split("HTML Reports")[1];
		
		String tccolor = null;
		if (finalStatus.equalsIgnoreCase("Pass")) {
			tccolor = "Green";
			passCount++;
		} else {
			tccolor = "Red";
			failCount++;
		}
		try {
			String data = "<tr><td width='13%' bgcolor='#ECEFF1' align='center'><b><a style=text-decoration: none; HREF=." + detFP
		+ "><font face='Tahoma' size='2'>" + tcID
					+ "</font></a></b></td><td width='24%' bgcolor='#ECEFF1' align='justify'><font color='#000000' face='Tahoma' size='2'>"
					+ desc + "</font></td><td width='18%' bgcolor='#ECEFF1' align='center'><font color='" + tccolor
					+ "' face='Tahoma' size='2'><b>" + finalStatus
					+ "</b></font></td><td width='23%' bgcolor='#ECEFF1' align='center'><font color='#000000' face='Tahoma' size='2'>"
					+ execTime + "</font></td></tr>";
			File file = new File(filepathHTML);
			FileWriter fileWritter = new FileWriter(file, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();
			System.out.println("Written in Summary-HTML");

		} catch (Exception e) {
			reportEvent("Writing in Summary HTML error","Printing StackTrace as "+e,"Fail");
		}
		}
	}
	public void addInExcel(String tcID, String desc, long totalTime) {

		try {
			File file = new File(filepathExl);

			FileInputStream fIP = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fIP);

			XSSFSheet spreadsheet = workbook.getSheet("Summary");
			int endRow = spreadsheet.getLastRowNum();

			XSSFRow row = spreadsheet.createRow((short) endRow + 1);

			XSSFCell cell;
			CreationHelper createHelper = workbook.getCreationHelper();
			XSSFCellStyle hlinkstyle = workbook.createCellStyle();
			XSSFFont hlinkfont = workbook.createFont();
			hlinkfont.setUnderline(XSSFFont.U_SINGLE);
			hlinkfont.setColor(HSSFColor.BLUE.index);
			hlinkstyle.setFont(hlinkfont);
			// URL Link
			cell = row.createCell((short) 0);
			cell.setCellValue(tcID);
			XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_FILE);
			String eclDir = exclDIR.toString();
			int lngth = eclDir.length();
			String addrs = detailFilepathExl.substring(lngth + 1);
			link.setAddress(addrs);
			cell.setHyperlink(link);
			cell.setCellStyle(hlinkstyle);
			row.createCell(1).setCellValue(desc);
			row.createCell(2).setCellValue(finalStatus);
			row.createCell(3).setCellValue(execTime);

			spreadsheet.autoSizeColumn(0);
			spreadsheet.autoSizeColumn(1);
			spreadsheet.autoSizeColumn(2);
			spreadsheet.autoSizeColumn(3);

			FileOutputStream out = new FileOutputStream(filepathExl);
			workbook.write(out);
			out.close();
			System.out.println("Written in Summary-Excel");
			workbook.close();
		} catch (Exception ex) {
			reportEvent("Writing in details XLS error","Printing StackTrace as "+ex,"Fail");
		}
	}
	
	public void footerSummary(){
		summaryEndTime = System.nanoTime();
		summTimeEnd = new SimpleDateFormat("HH:mm:ss a").format(Calendar.getInstance().getTime());
		double execEndTime = (summaryEndTime - summaryStartTime);
		double tcExecTime = execEndTime / 60000000000.0;
		tcExecTime = Math.round(tcExecTime * 100.0) / 100.0;
		try {

			String summData="</table><br><br><p style='text-align:center'><img src='http://chart.apis.google.com/chart?chtt=Test+Results&amp;chts=000000,12&amp;chs=600x250&amp;chf=bg,s,ffffff&amp;cht=p3&amp;chd=t:" + passCount+ "," + failCount + "&amp;chl=Tests+Passed(" +passCount+")|Tests+Failed(" +failCount+ ")&amp;chco=2E7D32,B71C1C' alt='Test Results Chart'></p><br><br><br>"
							+ "<table border='0' bordercolor='#000000' width='50%'><tr><td width='100%' colspan='2' bgcolor='#000000'><b><font face='Tahoma' size='2' color='#FFFFFF'>Summary</font></b></td></tr>"
							+"<td width='45%' bgcolor='#81C784'><b><font face='Tahoma' size='2'>Total Tests Passed</font></b></td><td width='55%' bgcolor='#81C784'>" +passCount+"</td></tr>"
							+"<td width='45%' bgcolor='#EF9A9A'><b><font face='Tahoma' size='2'>Total Tests Failed</font></b></td><td width='55%' bgcolor='#EF9A9A'>"+failCount+"</td></tr>"
							+"<td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Executed On</font></b></td><td width='55%' bgcolor= '#FFFFDC'>" + new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date())+ "</td></tr>"
							+"<td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Start Time</font></b></td><td width='55%' bgcolor= '#FFFFDC'>" + summTimeStart + "</td></tr>"
							+"<td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>End Time</font></b></td><td width='55%' bgcolor= '#FFFFDC'>" +summTimeEnd + "</td></tr>"
							+"<td width='45%' bgcolor='#FFFFDC'><b><font face='Tahoma' size='2'>Execution Time in Minutes</font></b></td><td width='55%' bgcolor= '#FFFFDC'>" + tcExecTime + "</td></tr></table>";
							
			File file = new File(filepathHTML);
			FileWriter fileWritter = new FileWriter(file, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(summData);
			bufferWritter.close();
			System.out.println("Footer Summary written");
			
			File htmlFile = new File(filepathHTML);
            Desktop.getDesktop().browse(htmlFile.toURI());


		} catch (Exception e) {
			reportEvent("Error in Writing footer in summary file-HTML","Printing StackTrace as "+e,"Fail");
		}
	}

	public void createStatusTextFile(String testCaseID){
		String finalStatusARTO="";
		try {
			if(finalStatus.equalsIgnoreCase("Pass")){
				finalStatusARTO="PASS";
			}else{
				finalStatusARTO="FAIL";
			}
			
	//	Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Status_"+testCaseID+".txt"), "utf-8"))) {
	 //  writer.write("something");
	   String filepathTXT= driver.path+"\\Status_"+testCaseID+".txt";
	   File file = new File(filepathTXT);
		FileWriter fileWritter = new FileWriter(file, false);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		
		bufferWritter.write(finalStatusARTO);
		bufferWritter.close();
		//System.out.println("Text file created and saved at "+filepathTXT);
		
		} catch (Exception ecd) {
			reportEvent("Error in Writing text file status","Printing StackTrace as "+ecd,"Fail");
		}
		
	}
	public void closeDetailFile() {
		try {
			String data = "</table></body></html>";
			File file = new File(detailFilepathHTML);
			FileWriter fileWritter = new FileWriter(file, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();
		} catch (Exception e) {
			reportEvent("Closing details HTML error","Printing StackTrace as "+e,"Fail");
		}
	}
	
	public static void reportEvent(String stepName, String StepDescription, String Status,String mandataryField ) {
		
		if (!mandataryField.equalsIgnoreCase("TRUE")) {
			Status="Warning";
		}
		
		reportEvent_HTML(stepName, StepDescription, Status);
		reportEvent_Excel(stepName, StepDescription, Status);
	}
	
	
}