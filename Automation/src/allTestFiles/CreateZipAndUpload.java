package allTestFiles;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
public class CreateZipAndUpload {
	List<String> filesListInDir = new ArrayList<String>();
		
    public void createTemp(File basePath,String methname) {
    	File dir,dir2;
    	//Create JiraUploads Folder
    	dir2 = new File(basePath+"\\JiraUploads");
    	dir2.mkdir();
    	
    	dir = new File(basePath+"\\JiraUploads\\Screenshots");
    	dir.mkdir();
    	
    	dir = new File(basePath+"\\JiraUploads\\DetailResult");
    	dir.mkdir();
    	
    	try {
    		File srcScreenshot= new File(basePath+"\\Screenshots\\"+methname);
    		File destScreenshot=new File(basePath+"\\JiraUploads\\Screenshots\\"+methname);
    		
    		File srcDetails= new File(basePath+"\\HTML Reports\\"+methname+".html");
    		File destDetails=new File(basePath+"\\JiraUploads\\DetailResult");
    		
    		
    		FileUtils.copyDirectory(srcScreenshot, destScreenshot);
    		FileUtils.copyFileToDirectory(srcDetails, destDetails);
    		
    		createZip(dir2, basePath+"\\Results.zip"); 
    		deleteFolder(dir2);
    		TestResult.reportEvent("Jira Uploading","Uploading assets to Jira Started","Pass");
    		
			} catch (IOException e) {
			e.printStackTrace();
		//	TestResult.reportEvent("Jira Uploading","Uploading assets to Jira failed","Fail");
		}
    }
     private void zipDirectory(File dir, String zipDirName) {
         try {
             populateFilesList(dir);
             FileOutputStream fos = new FileOutputStream(zipDirName);
             ZipOutputStream zos = new ZipOutputStream(fos);
             for(String filePath : filesListInDir){
               
                 ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                 zos.putNextEntry(ze);
                 FileInputStream fis = new FileInputStream(filePath);
                 byte[] buffer = new byte[1024];
                 int len;
                 while ((len = fis.read(buffer)) > 0) {
                     zos.write(buffer, 0, len);
                 }
                 zos.closeEntry();
                 fis.close();
                 System.out.println("Files Zipped");
             }
             filesListInDir.clear();
             zos.close();
             fos.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     
     private void populateFilesList(File dir) throws IOException {
         File[] files = dir.listFiles();
         for(File file : files){
             if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
             else populateFilesList(file);
         }
     }
     
     public  void createZip(File dir, String zipDirName) {
          zipDirectory(dir, zipDirName);
      }
         public void deleteZip(String path)
         {
         	try{

         		File file = new File(path);

         		if(file.delete()){
         			System.out.println(file.getName() + " is deleted!");
         			TestResult.reportEvent("Jira Upload", "Jira File Deleted Successfully","Pass");
         		}else{
         			System.out.println("Delete operation failed.");
         		}

         	}catch(Exception e){

         		e.printStackTrace();
         	}

         }
         public void deleteFolder(File dir) throws IOException
         {
        	 FileUtils.deleteDirectory(dir);
         }
     
// public void uploadToJira(String fStatus,String cycleId,String projectId,String issueId,String versionId,String executionComment,String executionId,File basePath,String stepID,String stepResultID) {
         public void uploadToJira(String fStatus,String cycleId,String projectId,String issueId,String versionId,String executionComment,String executionId,File basePath) {
	ExecuteTestCase ex=new ExecuteTestCase();
		try {
	//***********************JIRA UPDATE************************************\\	
	String executionStatus= fStatus;
	if(executionStatus.equalsIgnoreCase("Pass"))
 		executionStatus="1";
 	else if(executionStatus.equalsIgnoreCase("Fail"))
 		executionStatus="2";
 	else
 		TestResult.reportEvent("Execution Status to be updated in JIRA","Invalid Execution Status","JiraException");
 	  
 	boolean overall= ex.execute(executionStatus, cycleId, projectId, versionId, executionComment, executionId);
 
 	//boolean stepE= ex.executeStep(executionStatus, cycleId, projectId, versionId, executionComment, executionId,stepID,stepResultID,issueId);			        
 	boolean attach=ex.addAttachment(cycleId, versionId, projectId, issueId, executionId,executionComment,basePath+"\\Results.zip");
 	
	if(overall && attach)

 	{
 		TestResult.reportEvent("Jira Upload", "Jira Upload Successfull","Pass");
 	}
 	else
 	{
 		TestResult.reportEvent("Jira Upload", "Jira Upload Failed","JiraException");
 	}
 	
 	
	deleteZip(basePath+"\\Results.zip");
	
	}
	catch (Exception eup) {
		TestResult.reportEvent("Jira Upload", "Jira Upload Failed","JiraException");
		TestResult.reportEvent("Printing Error Message",eup.getMessage().split("\\(Driver")[0],"JiraException");
	}
}
}