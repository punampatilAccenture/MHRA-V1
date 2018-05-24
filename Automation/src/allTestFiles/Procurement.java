package allTestFiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import junit.framework.Test;

@SuppressWarnings("all")
public class Procurement implements ObjectRepository
{
	// ******************Start of Initialization for class file *****
			CommonFunctions cfUtil = new CommonFunctions();
			Utility obj = new Utility();
			TestResult reporter = new TestResult();
			String clsname = this.getClass().getSimpleName();
			// ******************End of Initialization for class/ file********************************
			
			//#############Current Date for Unique name#################
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date append = new Date();
			@SuppressWarnings("unused")
			private String varPosition;
			
			//####################################################################
	
//Test cases begin:			
			
		public void withdrawRequisition()
			{
				try 
				{
					obj.closeBrowser();
					// *******************Start Initialization******************************
					String methName = new Object() {}.getClass().getEnclosingMethod().getName();
					System.out.println(methName);
					reporter.createDetailsFile(methName);
					cfUtil.createDataMap(clsname, methName);
					CreateZipAndUpload createUpload=new CreateZipAndUpload();
					// *******************Initialization Done******************************
					TestResult.reportEvent("Execution Started", "Execution Started for "+methName, "Pass");
				
					//##########################Test Data#################################			
					String varBrowser=cfUtil.GetData("Browser");
					String varUrl=cfUtil.GetData("URL");
					String varUserName=cfUtil.GetData("UserName");
					String varPassword=cfUtil.GetData("Password");
					String varRequisitionNumber=cfUtil.GetData("RequisitionNumber");
					//##########################Test Data#################################	
					
					obj.login(varBrowser,varUrl,varUserName,varPassword);
					Thread.sleep(2000);
					
					obj.clickOnElement_NoReport("Home Icon", homeIcon);
					Thread.sleep(2000);
					
					//Receivables
					obj.waitForElement("Procurement link", procurementLink);
					obj.clickOnElement("Procurement link", procurementLink);
					Thread.sleep(1000);
					
					obj.waitForElement("Purchase Requisition link", purchaseRequisitionLink);
					obj.clickOnElement("Purchase Requisition link", purchaseRequisitionLink);
					Thread.sleep(5000);
					
					if(obj.isDisplayed("Requisition Number", "(//a[text()='"+varRequisitionNumber+"'])[1]"))
					{
						obj.clickOnElement("Select requisition: "+varRequisitionNumber, "(//a[text()='"+varRequisitionNumber+"'])[1]//preceding::td[1]");
						Thread.sleep(2000);
						obj.clickOnElement("Actions", requisitionAction);
						Thread.sleep(1000);
						if(obj.isDisplayed("Withdraw and Edit", withDrawAndEditButton))
						{
							obj.clickOnElement("Withdraw and Edit", withDrawAndEditButton);
							Thread.sleep(3000);
						}
						else
						{
							TestResult.reportEvent("Withdraw and Edit validation", "Withdraw and Edit not found", "Fail");
						}
					}
					else
					{
						TestResult.reportEvent("Searched Requisition validation", "Searched Requisition not found", "Fail");
						return;
					}
					
					boolean flag=false;
					String varMsg="";
					if(obj.isDisplayed("Warning Box:Yes Button", yesButton_WarningBox))
					{
						for(int i=9;i>=1;i--)
						{
							 varMsg=obj.getText("(//*[contains(@id,':d3::yes')])[1]//preceding::td["+i+"]");
							
							if(varMsg.contains("This requisition is approved or pending approval. It will be removed from the approval or order creation process so you can make changes"))
							{
								TestResult.reportEvent("Warning message validation", "Warning Message: "+varMsg, "Pass");
								obj.clickOnElement("Warning Box:Yes Button", yesButton_WarningBox);
								flag=true;
								break;
							}
							
						}
						
						if(flag==false)
						{
							TestResult.reportEvent("Warning message validation", "Warning Message: "+varMsg, "Fail");
							return;
						}
					}
					obj.closeBrowser();
				
				}
				
				catch (Exception e) 
				{
					TestResult.reportEvent("Printing Error Message test case",e.getMessage().split("\\(Driver")[0],"Fail");
					e.printStackTrace();
					obj.closeBrowser();
				}
			}
	
		
		}