package allTestFiles;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import allTestFiles.TestResult;

public class Utility {

	public static WebDriver wdriver;
	WebDriverWait wait;
	public boolean flag=false;
	

	
	public boolean openBrowser(String browserType) {
		boolean brwOpen=false;
		try {
			
			String brwsrType = browserType.toLowerCase();
			
			switch (brwsrType) {

			case "firefox":
				System.setProperty("webdriver.gecko.driver", driver.path+"\\newSelJars\\geckodriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setJavascriptEnabled(true);
				wdriver = new FirefoxDriver(capabilities);
				wdriver.manage().window().maximize();
				wdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				wait = new WebDriverWait(wdriver, 20);
				brwOpen=true;
				return brwOpen;
				
			case "chrome":
				System.setProperty("webdriver.chrome.driver", driver.path+"\\newSelJars\\chromedriver.exe");
				
				ChromeOptions chOption = new ChromeOptions();
		        chOption.addArguments("--disable-extensions");
		        chOption.addArguments("test-type");
		        chOption.addArguments("disable-infobars");
		        Map<String, Object> prefs = new HashMap<String, Object>();
		        prefs.put("credentials_enable_service", false);
		        prefs.put("profile.password_manager_enabled", false);
		        chOption.setExperimentalOption("prefs", prefs);
		       
				wdriver = new ChromeDriver(chOption);
				wdriver.manage().window().maximize();
				wdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				
				
				wait = new WebDriverWait(wdriver, 20);
				brwOpen=true;
				return brwOpen;
				
			case "ie":
				
				 System.setProperty("webdriver.ie.driver",driver.path+"\\newSelJars\\IEDriverServer.exe");
				// ReadText.getPropertyVal("driverPath")+"\\IEDriverServer.exe");
				wdriver = new InternetExplorerDriver();
				wdriver.manage().window().maximize();
				wdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				wait = new WebDriverWait(wdriver, 20);
				brwOpen=true;
				return brwOpen;
			}

		} catch (Exception e) {
			TestResult.reportEvent("Browser Status", "Browser "+browserType+" Failed to open with exception "+e.getMessage().split("\\(Driver")[0],"Fail");
			brwOpen=false;
			brwOpen=true;
			return brwOpen;
		}
		return brwOpen;
	}

	
	
	public void clickOnElement(String objectName,String objectValue)
	{
		try 
		{
			By locator;
			locator = By.xpath(objectValue);
			WebElement element = wdriver.findElement(locator);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();  
			// Click on Link
			System.out.println(objectName+"  Clicked");
			TestResult.reportEvent(objectName+" should be Clicked",objectName+" clicked successfully","Pass");
			Thread.sleep(1000);
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(objectName+" should be Clicked",objectName+" Link could not be clicked","Fail");
			TestResult.reportEvent("Printing stacktrace for Click Failure",e.getMessage().split("\\(Driver")[0],"Fail");
			return;
		}
	}
	
	
	public void clickOnElement_NoReport(String objectName,String objectValue)
	{
		try 
		{
			By locator;
			locator = By.xpath(objectValue);
			WebElement element = wdriver.findElement(locator);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();  
			Thread.sleep(1000);
			
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(objectName+" should be Clicked",objectName+" Link could not be clicked","Fail");
			TestResult.reportEvent("Printing stacktrace for Click Failure",e.getMessage().split("\\(Driver")[0],"Fail");
			return;
		}
	}
	
	
	public void setText(String objectName,String objectValue,String text)
	{
		try 
		{
			By locator;
			locator = By.xpath(objectValue);
			WebElement element = wdriver.findElement(locator);
			element.clear();
			
			element.sendKeys(text); 
			TestResult.reportEvent(objectName+" should be updated",objectName+" updated with "+text,"Pass");
			Thread.sleep(1000);
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(objectName+" should be updated",objectName+" could not be updated with "+text,"Fail");
			TestResult.reportEvent("Printing stacktrace",e.getMessage().split("\\(Driver")[0],"Fail");
			
		}
	}
	
	public void setText_NoReport(String objectName,String objectValue,String text)
	{
		try 
		{
			By locator;
			locator = By.xpath(objectValue);
			WebElement element = wdriver.findElement(locator);
			element.clear();
			element.sendKeys(text);                                     // Click on Link
			Thread.sleep(1000);
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(objectName+" should be updated",objectName+" could not be updated with "+text,"Fail");
			TestResult.reportEvent("Printing stacktrace",e.getMessage().split("\\(Driver")[0],"Fail");
			
		}
	}

	
	public void setPassword(String objectName,String objectValue,String text)
	{
		try 
		{
			By locator;
			locator = By.xpath(objectValue);
			WebElement element = wdriver.findElement(locator);
			element.clear();
			element.sendKeys(text);                                     // Click on Link
			TestResult.reportEvent("Password should be Entered",objectName+" updated with **********","Pass");
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent("Password should be Entered",objectName+" could not be updated","Fail");
			TestResult.reportEvent("Printing stacktrace",e.getMessage().split("\\(Driver")[0],"Fail");
			
		}
	}


	public void setByVisibleText (String objectName,String objectValue,String text)
	{
		try 
		{  
			
			
			Actions action = new Actions(wdriver);
			By locator;
			locator = By.xpath(objectValue);
			WebElement element1 = wdriver.findElement(locator);
			//wait.until(ExpectedConditions.elementToBeClickable(element1));
			action.moveToElement(element1).click().perform();
			Thread.sleep(1000);
			Select element = new Select(wdriver.findElement(locator));
			element.selectByVisibleText(text);      
			TestResult.reportEvent(objectName+"should be updated",objectName+" updated with "+text,"Pass");
			Thread.sleep(1000);
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(objectName+"should be updated",objectName+" could not be updated with "+text,"Fail");
			TestResult.reportEvent("Printing stacktrace Selecting from drop down",e.getMessage().split("\\(Driver")[0],"Fail");
		}
	}
	
	
	public void setByVisibleTextWarning (String objectName,String objectValue,String text)
	{
		try 
		{  
			
			
			Actions action = new Actions(wdriver);
			By locator;
			locator = By.xpath(objectValue);
			WebElement element1 = wdriver.findElement(locator);
			//wait.until(ExpectedConditions.elementToBeClickable(element1));
			action.moveToElement(element1).click().perform();
			Thread.sleep(1000);
			Select element = new Select(wdriver.findElement(locator));
			element.selectByVisibleText(text);   
			System.out.println("Dropdown Item Selected : "+text);    
			TestResult.reportEvent(text+"Dropdown item should be selected",objectName+" updated with "+text,"Pass");
		} 
		catch (Exception e) 
		{
			TestResult.reportEvent(text+"Dropdown item should be selected",objectName+" could not be updated with "+text,"Warning");
			TestResult.reportEvent("Printing stacktrace Selecting from drop down",e.getMessage().split("\\(Driver")[0],"Warning");
		}
	}
	

	public void highlight (String objectName,String objectValue) throws InterruptedException
	{	
		try 
		{
			 By locator;
			 locator = By.xpath(objectValue);   // return Locator 
				WebElement element = wdriver.findElement(locator);   // Return Element
				JavascriptExecutor js=(JavascriptExecutor)wdriver; 		 
				js.executeScript("arguments[0].setAttribute('style', 'background: grey; border: 2px solid black;');", element);
				js.executeScript("arguments[0].setAttribute('style','border: solid 2px white')", element); 
				 
		
		} 
		catch (Exception e)
		{
			TestResult.reportEvent("Highlight Object","Object Not Found","Fail");
			TestResult.reportEvent("Printing stacktrace for highlight Failure",e.getMessage().split("\\(Driver")[0],"Fail");
			
		}
	
	}
	
	
	
		public void NavigateToURL(String URL) {
			try{
				wdriver.navigate().to(URL);
				System.out.println(URL+" URL launched");
				//TestResult.reportEvent("URL should be navigated","launched successfully","Pass");
			} 
			catch (Exception e) 
			{
				TestResult.reportEvent("URL should be navigated",URL+" could not be launched","Fail");
				TestResult.reportEvent("Printing stacktrace for URL navigation",e.getMessage().split("\\(Driver")[0],"Fail");
			}
		}

			
		public void waitForElement(String objectName,String objectValue)
		{
			try
			{
				By locator;
				locator = By.xpath(objectValue);
				int i = 1;
				WebElement element = wdriver.findElement(locator);
				while(!element.isDisplayed() & i<=10)
				{
				wdriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
				i++;
				}
			}
			catch (Exception e)
			{
				TestResult.reportEvent("Wait for Element",objectName+" Not Found","Fail");
			}
			
		}
		
		
		public void closeBrowser()
		{
			try{
				
			wdriver.quit();
			
			}
			catch(Exception e)
			{}
		}
		
		
	
		
		public String getText(String objectValue) {
			String text="";
			try 
			{
				By locator;
				locator = By.xpath(objectValue);
				WebElement element = wdriver.findElement(locator);
				//text=element.getAttribute("innerHTML");
				text=element.getText();
				System.out.println("Text:"+text);
				Thread.sleep(1000);
				return text;
				
			} 
			catch (Exception e) 
			{
				TestResult.reportEvent("Get Text of an Element","Element Not found","Fail");
				
			}
			return text;
		}

		
		public boolean isDisplayed(String objectName,String objectValue)
		{
			try
			{
			
				By locator;
				locator = By.xpath(objectValue);
				WebElement element = wdriver.findElement(locator);
				if (element.isDisplayed())
				{
					flag=true;
				wait.until(ExpectedConditions.visibilityOfElementLocated( By.xpath(objectValue)));
				}
				System.out.println(flag);
				return flag;
				
				
		
				
		
			}
			catch(Exception e)
			{
				flag=false;
				return flag;
			}
			
		}

		


		public void setFocus(String objectValue) 
		{
			try
			{
				By locator;
				locator = By.xpath(objectValue);
				WebElement element = wdriver.findElement(locator);
				new Actions(wdriver).moveToElement(element).perform();
			}
			catch (Exception e) 
			{
				TestResult.reportEvent("Set Focus on an Element","Element Not found","Fail");
				
			}
			
		}
		
		public void handleChildWindow()
        {
			try{
			Set <String> set1=wdriver.getWindowHandles();
			Iterator <String> win1=set1.iterator();
			@SuppressWarnings("unused")
			String parent=win1.next();
			String child=win1.next();
			wdriver.switchTo().window(child);
			}
			catch (Exception e)
			{
				TestResult.reportEvent("Move control to child window"," Child Window Not found","Fail");
				
			}
}
		
		
		public void handleParentWindow()
		{
		       	   			Set <String> set1=wdriver.getWindowHandles();
		    	   			Iterator <String> win1=set1.iterator();
		    	   			String parent=win1.next();
//		    	   			String child=win1.next();
		    	   			wdriver.switchTo().window(parent);
		    	   	
		    	 
		}  
		
		
		public void SetTextiframe(String objectValue, String Textvalue )

		{
			try{
			  By locator;
				locator = By.xpath(objectValue);      
			
				wdriver.switchTo().frame(wdriver.findElement(locator));
				WebElement printbody = wdriver.switchTo().activeElement();
				printbody.clear();
			    printbody.sendKeys(Textvalue);
			    wdriver.switchTo().defaultContent();   
			}
			catch (Exception e)
			{
				TestResult.reportEvent("Set Text in Text area"," Text area field Not found","Fail");
				
			}
		}


		public void fileUpload(String filepath) throws AWTException
{
	StringSelection ss = new StringSelection(filepath);
	Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	//native key strokes for CTRL, V and ENTER keys
	Robot robot = new Robot();

	robot.keyPress(KeyEvent.VK_CONTROL);
	robot.keyPress(KeyEvent.VK_V);
	robot.keyRelease(KeyEvent.VK_V);
	robot.keyRelease(KeyEvent.VK_CONTROL);
	robot.keyPress(KeyEvent.VK_ENTER);
	robot.keyRelease(KeyEvent.VK_ENTER);
}


		//Selecting a row in a table
		public void selectRow(String objectName,String objectValue, String LinkText)

{
	//String linkname;

	 try{
		 By locator;
			locator = By.xpath(objectValue);
			WebElement tabledata = wdriver.findElement(locator);
			List <WebElement> allrows = tabledata.findElements(By.tagName("tr"));
			   for(WebElement row: allrows){
			        List <WebElement> Cells = row.findElements(By.tagName("td"));
			        for(WebElement Cell:Cells){
			        	if (Cell.getText().contains(LinkText))
			        	{
			        		Cell.click();
			        	
			        		break;
			        	}
			        	
			        }
			        break;
			    }
	 }catch (Exception e) 
		{
			TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
			
		}
	
	
}

		//Getting the cell text when one of the value in the table is known
		public String GetCellText(String objectName,String objectValue, String textvalue)
{
		String x =null;
	 
		 try{
			 By locator;
				locator = By.xpath(objectValue);
				WebElement tabledata = wdriver.findElement(locator);
				List<WebElement> allrows = tabledata.findElements(By.tagName("tr"));
				   for(WebElement row: allrows){
				        List<WebElement> Cells = row.findElements(By.tagName("td"));
				        for(WebElement Cell:Cells){
				        	if (Cell.getText().contains(textvalue))
				        	{
				        	 x=Cell.getText();
				        	
				        		
				        		break;
				        		
				        	}
				        	
				        }TestResult.reportEvent("Cell value","Cell values are "+x ,"Pass");
				        break;
				    }
		 }catch (Exception e) 
			{
				TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
				
			}
		 return x;
	 }
	
		//Verify the values of multiple celss in the table
		public ArrayList<String> VerifyCellText (String objectName,String objectValue, String textvalue1, String textvalue2)
{
	ArrayList<String> rowData=null;
 try{
			 By locator;
				locator = By.xpath(objectValue);
				
				WebElement tabledata = wdriver.findElement(locator);
				List<WebElement> allrows = tabledata.findElements(By.tagName("tr"));
		        rowData = new ArrayList<String>();
				//List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
				   for(WebElement row: allrows){
				        List<WebElement> Cells = row.findElements(By.tagName("td"));
				        for(WebElement Cell:Cells)
				        	
				        	{
				        	rowData.add(Cell.getText().toString());
				        	 	System.out.println(rowData);
				        	 	//strArray = new String[rowData.size()];
					            //rowData.toArray(strArray);
					           // System.out.println(strArray);
				        		if(rowData.contains(textvalue1) && rowData.contains(textvalue2))
				        		{
				        			
				        			
				        			flag=true;
				        			break;
				        			
				        		}   
				        		
				        	}
				        
				        break;
				        		
				        	}
				   
				   if(flag)
				   {
					   TestResult.reportEvent("Cell value","Cell values are present as expected "+textvalue1, "Pass");
	        		   TestResult.reportEvent("Cell value","Cell values are present as expected "+textvalue2, "Pass"); 
				   }
				   else
				   {
	        			TestResult.reportEvent("Cell value","Cell values are not present as expected "+textvalue1, "Fail");
			    		TestResult.reportEvent("Cell value","Cell values are not present as expected "+textvalue2, "Fail");
				   }
					   
			    	
		 }catch (Exception e) 
			{
				TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
				
			}
		 return rowData;	
	 }  

		//Navigation to the default window after doing operation in the frames 
		public void NavigateWindow()
{
	wdriver.switchTo().defaultContent();
}

		public void NavigateFrames(String frameName)

{
	//Commented code is used to get the frame ID and frame name to pass it as a parameter for frame navigation
	
	 /*List<WebElement> ele = wdriver.findElements(By.tagName("iframe"));
	    System.out.println("Number of frames in a page :" + ele.size());
	    for(WebElement el : ele){
	      //Returns the Id of a frame.
	        System.out.println("Frame Id :" + el.getAttribute("id"));
	      //Returns the Name of a frame.
	        System.out.println("Frame name :" + el.getAttribute("name"));
	//wdriver.switchTo().frame(frames);
	//wdriver.switchTo().defaultContent()*/
	        
	        wdriver.switchTo().frame(frameName);
	      	
}

		public void setText(String objectValue, Keys tab) 
{
	
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		element.sendKeys(tab); 
		Thread.sleep(2000);
	} 
	catch (Exception e) 
	{
		//TestResult.reportEvent("Printing stacktrace",e.getMessage().split("\\(Driver")[0],"Fail");
		//StartRun=false;
	}
	
	
}

		/* ==================getIntFromLabel or Span or from Div=================
 * @Author: bibhu.sundar.das
 * @Date: Jan 17, 2016
 * @Param: String
 * @return : Label Text
 * The method gets integer from 
 */	
		public String getIntFromString (String sMsg) {
    String sNum = "";
    try 
    {   String var = sMsg;     
          for (int i=0 ; i< var.length();i++){
                 System.out.println("Char"+ var.charAt(i));
                 
                 if(Character.isDigit(var.charAt(i))){
                        System.out.println("Char : : "+ var.charAt(i));
                        sNum = sNum + var.charAt(i);
                        System.out.println("Number Returned "+ sNum);
                      }               
               }
          return sNum;
          
    } 
    catch (Exception e) 
    {
          TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
          return sNum;
    }

}

		/* ==================getLabelText=================
 * @Author: neeraj.kumar.pandey
 * @Date: Jan 17, 2016
 * @Param: String
 * @return : Label Text
 * The method gets label Text
 */	
		public String getLabelText(String objectName,String objectValue) {
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt=element.getText();
		System.out.println("Text:"+txt);
	
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element Display Status",objectName+"  is Not Displayed","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}

		public void ClickOnLink(String objectValue)
{
	try 
	{
		By locator;
		locator = By.linkText(objectValue);
		WebElement element = wdriver.findElement(locator);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		
		int i = 1;
				
		while(!element.isDisplayed() & i<=10)
		{
		wdriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		i++;
		}
		
						
		TestResult.reportEvent(objectValue+" Link should be Displayed",objectValue+" Displayed On Page","Pass");
		// Click on Link
		element.click();
		
		System.out.println(objectValue+" Link  Clicked");
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent(objectValue+" element should be Clicked",objectValue+" Link could not be clicked","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
}

/* ==================setTextarea=================
 * @Author: neeraj.kumar.pandey
 * @Date: Nov 28, 2016
 * @Param: String
 * @return : NA
 * The method enters Text into a text box
 */
public void setTextArea(String objectName,String objectValue,String text)
{
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		element.sendKeys(Keys.TAB);
		element.click();
				
		element.sendKeys(text);                                     // Click on Link
		System.out.println("Text Entered");
		
		TestResult.reportEvent(text+" text should be Entered",objectName+" updated with text "+text,"Pass");
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent(text+" text should be Entered",objectName+" field could not be updated with text "+text,"Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
		
	}
}

/* ==================pressKeyDown=================
 * @Author: neeraj.kumar.pandey
 * @Date: 25Jan , 2017
 * @Param: String
 * @return : NA
 * Send key for ENTER
 */		
public void pressKeyDown(String objectName,String objectValue) {
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		element.click();// Click on Element
		element.sendKeys(Keys.DOWN);
		element.sendKeys(Keys.ENTER); 
		element.sendKeys(Keys.TAB);
		Thread.sleep(500);
		
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}

}

public void setDropDownByValue (String objectName,String objectValue,String text)
{
	try 
	{
		By locator;
	
		locator = By.xpath(objectValue);
		Select dropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(locator)));
		WebElement element1 = wdriver.findElement(locator);
		wait.until(ExpectedConditions.elementToBeClickable(element1));
		element1.click();
					
		System.out.println("Dropdown Item Selected : "+text);    
		
	
		dropdown.selectByValue(text);
		
		Thread.sleep(1000);
		
		
		TestResult.reportEvent(text+"Dropdown item should be selected",objectName+" updated with "+text,"Pass");
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent(text+"Dropdown item should be selected",objectName+" could not be updated with "+text,"Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
}

/* ==================Get Default Value of TextBox=================
 * @Author: neeraj.kumar.pandey
 * @Date: Jan 31, 2016
 * @Param: String
 * @return : Label Text
 * The method gets label Text
 */	
public String getTextBoxValue(String objectName,String objectValue) {
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt=element.getAttribute("value");
		System.out.println("Text:"+txt);
	
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element Display Status",objectName+"  is Not Displayed","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}

public String getDropDownValue(String objectName,String objectValue) {
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt=element.getAttribute("title");
		System.out.println("Text:"+txt);
	
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element Display Status",objectName+"  is Not Displayed","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}

/* ==================getTitle=================
 * @Author: Dev.kumar.Durgani
 * @Date: March 17, 2017*/

public final String getTitle(String objectName,String objectValue){
	
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt=element.getAttribute("title");
		//txt=element.getText();
		System.out.println("title:"+txt);
	
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element Not Found",objectName+" Element Not Found","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}

/* ==================getValue=================
 * @Author: Dev.kumar.Durgani
 * @Date: March 17, 2017*/

public String getValue(String objectName,String objectValue) {
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt=element.getAttribute("value");
//		txt=element.getText();
		System.out.println("value:"+txt);
	
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element Not Found",objectName+" Element Not Found","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}

public boolean isEnabled(String objectName,String objectValue)
{
	try{
	By locator;
	locator = By.xpath(objectValue);
	WebElement element = wdriver.findElement(locator);
	
	if (element.isDisplayed() && element.isEnabled())
	{
		flag=true;
		wait.until(ExpectedConditions.visibilityOfElementLocated( By.xpath(objectValue)));
	}
	System.out.println(flag);
	return flag;
	}
	catch (Exception e) 
	{
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
		return flag;
	}
}

/* ==================press Tab Key=================
 * @Author: Jyoti Shrivas
 * @Date: Jan 13, 2017
 * @Param: String
 * @return : NA
 * Send key for ENTER
 */		
public void pressKeyBoardKey(String objectName,String objectValue,String key) {
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		
		 String Str = new String(key); 
		switch ( Str.toUpperCase()) {
		
		case "TAB":
			element.sendKeys(Keys.TAB);
			break;
		case "ENTER":
			element.sendKeys(Keys.ENTER);
			break;
		}
	                          

	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}

}
/* ==================get Attribute Value=================
 * @Author: Jyoti Shrivas
 * @Date: Jan 16, 2016
 * @Param: String
 * @return : NA
 * Return the attribute value of an web element
 */	
public String getAttributeValue(String objectName,String objectValue,String aName) {
	String txt=null;
	try 
	{
		By locator;
		locator = By.xpath(objectValue);
		WebElement element = wdriver.findElement(locator);
		txt = element.getAttribute(aName);
		System.out.println("Attribute Value:"+txt);
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent("Element should be Clicked",objectName+" Link could not be clicked","Fail");
		TestResult.reportEvent("Printing Error Message",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	return txt;
}


 
 public void Scrollandclick(String objectName, String objectValue)
 {
 try
 {
        By locator;
        locator = By.xpath(objectValue);
 
        //driver.navigate().to(URL+"directory/companies?trk=hb_ft_companies_dir");
        WebElement element = wdriver.findElement(locator);
        ((JavascriptExecutor) wdriver).executeScript(
          "arguments[0].scrollIntoView();", element);
        Thread.sleep(1000);
        element.click(); 
        
        TestResult.reportEvent(objectName+" should be Clicked",objectName+" clicked successfully","Pass");
 } 
 catch (Exception e) 
 {
        TestResult.reportEvent(objectName+" should be Clicked",objectName+" could not be clicked","Fail");
        TestResult.reportEvent("Printing stacktrace for Click Failure",e.getMessage().split("\\(Driver")[0],"Fail");
        return;
 }
 }
 
 public void Scrollandclick_NoReport(String objectName, String objectValue)
 {
 try
 {
        By locator;
        locator = By.xpath(objectValue);
 
        //driver.navigate().to(URL+"directory/companies?trk=hb_ft_companies_dir");
        WebElement element = wdriver.findElement(locator);
        ((JavascriptExecutor) wdriver).executeScript(
          "arguments[0].scrollIntoView();", element);
        Thread.sleep(1000);
        element.click(); 
        Thread.sleep(1000);
 } 
 catch (Exception e) 
 {
        TestResult.reportEvent(objectName+" should be Clicked",objectName+" could not be clicked","Fail");
        TestResult.reportEvent("Printing stacktrace for Click Failure",e.getMessage().split("\\(Driver")[0],"Fail");
        return;
 }
 }
 
 
 public void ScrollUp() 
 {
        ((JavascriptExecutor) wdriver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
 }

 
 public int randomNumber()
 {
 	Random rand = new Random(); 
 	int value = rand.nextInt(999999999);	
 	return value;
 }

 public String getImageSrc(String objectValue)
 {
	  WebElement img = wdriver.findElement(By.xpath(objectValue));
	 String src = img.getAttribute("src");
	 return src;
 }



public void setByVisibleText_NoReport(String objectName,String objectValue,String text)
{
	try 
	{  
		
		
		Actions action = new Actions(wdriver);
		By locator;
		locator = By.xpath(objectValue);
		WebElement element1 = wdriver.findElement(locator);
		//wait.until(ExpectedConditions.elementToBeClickable(element1));
		action.moveToElement(element1).click().perform();
		Thread.sleep(1000);
		Select element = new Select(wdriver.findElement(locator));
		element.selectByVisibleText(text);      
		
		Thread.sleep(1000);
	} 
	catch (Exception e) 
	{
		TestResult.reportEvent(objectName+"should be updated",objectName+" could not be updated with "+text,"Fail");
		TestResult.reportEvent("Printing stacktrace Selecting from drop down",e.getMessage().split("\\(Driver")[0],"Fail");
	}
	
}
 
public void login(String varBrowser,String varUrl,String varUserName,String varPassword) throws InterruptedException
{
	//******Launch Browser*************//	
	if(openBrowser(varBrowser))
	{
		TestResult.reportEvent("Browser Status", "Browser opened successfully", "Pass");
		System.out.println("Browser launched");
	}
	else
	{
		TestResult.reportEvent("Browser Status", "Browser opened Failed", "Fail");
		return;
	}
	
	//*********Navigate to URL***********//
	NavigateToURL(varUrl);
	System.out.println("launched");

	//*********Sign In********************//
	highlight("User Name", ObjectRepository.hrUserName_TextBox);
	setText("User Name", ObjectRepository.hrUserName_TextBox, varUserName);	
	
	highlight("Password", ObjectRepository.hrPassword_TextBox);
	setPassword("Password", ObjectRepository.hrPassword_TextBox, varPassword);	
	
	clickOnElement_NoReport("Sign In", ObjectRepository.sign_in_Button);

//	waitForElement("Navigator Icon", ObjectRepository.navigatorIcon);
//	clickOnElement("Navigator Icon", ObjectRepository.navigatorIcon);
//	Thread.sleep(2000);
}


public void flexFieldUpdate(String varFieldName,String varLovObject,String searchLink,String varTextbox,String varTable,String varData) throws InterruptedException
{
	Scrollandclick_NoReport(varFieldName+" LOV",varLovObject);
	Thread.sleep(2000);
	
	if(isDisplayed("Search... Link", searchLink))
	{
		
		Scrollandclick_NoReport("Search... Link", searchLink);
		Thread.sleep(1000);
		Thread.sleep(2000);
	   
	}
	else
	{
		System.out.println("No Search Link");
	}
	
	
	waitForElement("Search and Select "+varFieldName+" Text Box", varTextbox);
	setText(varFieldName+" Text Box", varTextbox,varData);
	Thread.sleep(3000);
	
	clickOnElement_NoReport("Search Button", ObjectRepository.searchButtonInFlexField);
	Thread.sleep(3000);
	
	if(isDisplayed("", varTable))
	{
		clickOnElement_NoReport(varFieldName+" Searched", varTable);
		Thread.sleep(3000);
	}
	else
	{
		TestResult.reportEvent(varFieldName+" validation", varFieldName+" not found", "Fail");
		return;
	}
	clickOnElement_NoReport("Search and Select : OK button", ObjectRepository.okButtonInFlexField);
	Thread.sleep(1000);
}

public String randomString()

{

      SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");

      String string = sdf.format(new Date());

      String company="Test"+string;

     

      return company;

}


//function to count rows in web table:
public int getRowCountFunction(String tableXpath)
{
	By locator;
	locator = By.xpath(tableXpath);
	WebElement element = wdriver.findElement(locator);
	List<WebElement> rows_table = element.findElements(By.tagName("tr"));
	int rows_count = rows_table.size();
	System.out.println(rows_count);	
	return rows_count;
}

//function to update account number:
public void updateAccountNumber(String accountIconXpath,String varEntity,String varManagementUnit,String varAccount,String varFullAccountNumber)
{
	try 
	{
		clickOnElement("Account Icon", accountIconXpath);
		Thread.sleep(3000);
		if(isDisplayed("Account Pop up", "//div[text()='Account']"))
		{
			setText("Entity", "//*[contains(@id,'kfSPOP_query:value00::content')]", varEntity);
			setText("//*[contains(@id,'kfSPOP_query:value00::content')]", Keys.TAB);
			
			setText("Management Unit", "//*[contains(@id,'kfSPOP_query:value10::content')]", varManagementUnit);
			setText("//*[contains(@id,'kfSPOP_query:value10::content')]", Keys.TAB);
			
			setText("Account", "//*[contains(@id,'kfSPOP_query:value20::content')]", varAccount);
			setText("//*[contains(@id,'kfSPOP_query:value20::content')]", Keys.TAB);
			
			clickOnElement("Account Dialog:OK button", "(//div[text()='Account']//following::button[contains(text(),'O')])[1]");
			Thread.sleep(3000);
			TestResult.reportEvent("Account field validation", "Account field updated with:"+varFullAccountNumber, "Pass");
		}
		else
		{
			TestResult.reportEvent("Account field validation", "Account field could not be updated", "Fail");
			return;
		}
	}
	catch (InterruptedException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
		public String todaysDate()
		{
			DateFormat dateFormat = new SimpleDateFormat("M/dd/YY");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			String varDate=dateFormat.format(date);
			return varDate;
		}
		
		public void handleChildWindow1()
        {
			try{
				Set <String> set1=wdriver.getWindowHandles();
				Iterator <String> win1=set1.iterator();
				@SuppressWarnings("unused")
				String parent=win1.next();
				String child=win1.next();
				String child1=win1.next();
	        	wdriver.switchTo().window(child1);
			    Thread.sleep(5000);
		    }
			catch (Exception e)
			{
				TestResult.reportEvent("Move control to child window"," Child Window Not found","Fail");
				
			}
}
}