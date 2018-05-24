package allTestFiles; 
 
 
 import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;


@SuppressWarnings("all")
 public class ExecuteTestCase { 
	
	 CommonFunctions cf = new CommonFunctions();
 	private static String API_SEARCH_ISSUES = "{SERVER}/rest/api/2/search"; 
 	private static String API_GET_EXECUTIONS = "{SERVER}/public/rest/api/1.0/executions/search/cycle/"; 
 	private static String API_UPDATE_EXECUTION = "{SERVER}/public/rest/api/1.0/execution/"; 
 	private static String API_UPDATE_STEP = "{SERVER}/public/rest/api/1.0/stepresult/";
 	public String jiraBaseURL = cf.GetData("JiraURL");
 	
 	public  String zephyrBaseUrl = cf.GetData("ZephyrBaseUrl");
 	public  String accessKey = cf.GetData("AccessKey");
 	public  String secretKey = cf.GetData("SecretKey");
 	
 	
 	/** Declare parameter values here */ 
 	public  String userName = cf.GetData("JiraUserName");
 	public String password = cf.GetData("JiraPassword");
 	
 	public String cycleId;
// 	boolean flag;
 	 ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName) 
 			.build(); 
 	JwtGenerator jwtGenerator = client.getJwtGenerator(); 

 
	public boolean execute(String executionStatus,String cycleId,String projectId,String versionId,String Executioncomment,String executionId)
			throws JSONException, URISyntaxException, ParseException, IOException { 
		System.out.println(jwtGenerator);
		final String issueSearchUri = API_SEARCH_ISSUES.replace("{SERVER}", jiraBaseURL); 
 		boolean flag=true;
 		Map<String, String> executionIds = new HashMap<String, String>(); 
 		final String getExecutionsUri = API_GET_EXECUTIONS.replace("{SERVER}", zephyrBaseUrl) + cycleId + "?projectId=" 
 				+ projectId + "&versionId=" + versionId; 
 
 
 		executionIds = getExecutionsByCycleId(getExecutionsUri, client, accessKey); 
 		System.out.println(executionIds);
 
 		/** 
 		 * Update Executions with Status by Execution Id 
 		 *  
 		 */ 
 
 
 		JSONObject statusObj = new JSONObject(); 
 		statusObj.put("id", executionStatus); 
 
 
 		JSONObject executeTestsObj = new JSONObject(); 
 		executeTestsObj.put("status", statusObj); 
 		executeTestsObj.put("cycleId", cycleId); 
 		executeTestsObj.put("projectId", projectId); 
 		executeTestsObj.put("versionId", versionId); 
 		executeTestsObj.put("comment", Executioncomment); 

 		final String updateExecutionUri = API_UPDATE_EXECUTION.replace("{SERVER}", zephyrBaseUrl) + executionId; 
 			
 			// System.out.println(executionIds.get(key)); 
 			executeTestsObj.put("issueId", executionIds.get(executionId)); 
 			// System.out.println(executeTestsObj.toString()); 
 			StringEntity executeTestsJSON = null; 
 			try { 
 				executeTestsJSON = new StringEntity(executeTestsObj.toString()); 
 				} catch (UnsupportedEncodingException e1) { 
 				e1.printStackTrace(); 
 				flag=false;
 			} 
 			updateExecutions(updateExecutionUri, client, accessKey, executeTestsJSON); 
 			return  flag; 
 		}
		
 
 		
	private static Map<String, String> getExecutionsByCycleId(String uriStr, ZFJCloudRestClient client, 
 			String accessKey) throws URISyntaxException, JSONException { 
 		Map<String, String> executionIds = new HashMap<String, String>(); 
 		URI uri = new URI(uriStr); 
 		int expirationInSec = 360; 
 		JwtGenerator jwtGenerator = client.getJwtGenerator(); 
 		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec); 
 		// System.out.println(uri.toString()); 
 		// System.out.println(jwt); 
 
 
 		HttpResponse response = null; 
 		HttpClient restClient = new DefaultHttpClient(); 
 		HttpGet httpGet = new HttpGet(uri); 
 		httpGet.setHeader("Authorization", jwt); 
 		httpGet.setHeader("zapiAccessKey", accessKey); 
 
 
 		try { 
 			response = restClient.execute(httpGet); 
 		} catch (ClientProtocolException e1) { 
 			// TODO Auto-generated catch block 
 			e1.printStackTrace(); 
 		} catch (IOException e1) { 
 			// TODO Auto-generated catch block 
 			e1.printStackTrace(); 
 		} 
 
 
 		int statusCode = response.getStatusLine().getStatusCode(); 
 		// System.out.println(statusCode); 
 
 
 		if (statusCode >= 200 && statusCode < 300) { 
 			HttpEntity entity1 = response.getEntity(); 
 			String string1 = null; 
 			try { 
 				string1 = EntityUtils.toString(entity1); 
 			} catch (ParseException e) { 
 				e.printStackTrace(); 
 			} catch (IOException e) { 
 				e.printStackTrace(); 
 			} 
 
 
 			// System.out.println(string1); 
 			JSONObject allIssues = new JSONObject(string1); 
 			JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList"); 
 			// System.out.println(IssuesArray.length()); 
 			if (IssuesArray.length() == 0) { 
 				return executionIds; 
 			} 
 			for (int j = 0; j <= IssuesArray.length() - 1; j++) { 
 				JSONObject jobj = IssuesArray.getJSONObject(j); 
 				JSONObject jobj2 = jobj.getJSONObject("execution"); 
 				String executionId = jobj2.getString("id"); 
 				long IssueId = jobj2.getLong("issueId"); 
 				executionIds.put(executionId, String.valueOf(IssueId)); 
 			} 
 		} 
 		return executionIds; 
 	} 
 
 
 	public static boolean updateExecutions(String uriStr, ZFJCloudRestClient client, String accessKey, 

 			StringEntity executionJSON) throws URISyntaxException, JSONException, ParseException, IOException { 
 		boolean fl=true;
 
 		URI uri = new URI(uriStr); 
 		int expirationInSec = 360; 
 		JwtGenerator jwtGenerator = client.getJwtGenerator(); 
 		String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec); 
 		// System.out.println(uri.toString()); 
 		// System.out.println(jwt); 
 
 
 		HttpResponse response = null; 
 		HttpClient restClient = new DefaultHttpClient(); 
 
 
 		HttpPut executeTest = new HttpPut(uri); 
 		executeTest.addHeader("Content-Type", "application/json"); 
 		executeTest.addHeader("Authorization", jwt); 
 		executeTest.addHeader("zapiAccessKey", accessKey); 
 		executeTest.setEntity(executionJSON); 
 
 
 		try { 
 			response = restClient.execute(executeTest); 
 		} catch (ClientProtocolException e) { 
 			e.printStackTrace(); 
 			fl=false;
 		} catch (IOException e) { 
 			e.printStackTrace(); 
 			fl=false;
 		} 
 
 
 		int statusCode = response.getStatusLine().getStatusCode(); 
 		// System.out.println(statusCode); 
 		String executionStatus = "No Test Executed"; 
 		// System.out.println(response.toString()); 
 		HttpEntity entity = response.getEntity(); 
 
 
 		if (statusCode >= 200 && statusCode < 300) { 
 			String string = null; 
 			try { 
 				string = EntityUtils.toString(entity); 
 				JSONObject executionResponseObj = new JSONObject(string); 
 				//JSONObject descriptionResponseObj = executionResponseObj.getJSONObject("execution"); 
// 				JSONObject statusResponseObj = descriptionResponseObj.getJSONObject("status"); 
// 				executionStatus = statusResponseObj.getString("description"); 
// 				System.out.println(executionResponseObj.get("issueKey") + "--" + executionStatus); 
 			} catch (ParseException e) { 
 				e.printStackTrace(); 
 			} catch (IOException e) { 
 				e.printStackTrace(); 
 			} 
 
 
 		} else { 
 
 
 			try { 
 				String string = null; 
 				string = EntityUtils.toString(entity); 
 				JSONObject executionResponseObj = new JSONObject(string); 
 				//cycleId = executionResponseObj.getString("clientMessage"); 
 				
 				throw new ClientProtocolException("Unexpected response status: " + statusCode); 
 
 
 			} catch (ClientProtocolException e) { 
 				e.printStackTrace(); 
 				
 			} 
 		} 
 		return fl; 
 	} 
 	
 	
 	public boolean executeStep(String executionStatus,String cycleId,String projectId,String versionId,String Executioncomment,
			String executionId,String stepID,String stepResultID, String issueID) throws JSONException, URISyntaxException, ParseException, IOException { 
 		final String issueSearchUri = API_SEARCH_ISSUES.replace("{SERVER}", jiraBaseURL); 
 		 boolean fl=true;
 		 
 		Map<String, String> executionIds = new HashMap<String, String>(); 
 		final String getExecutionsUri = API_GET_EXECUTIONS.replace("{SERVER}", zephyrBaseUrl) + cycleId + "?projectId=" 
 				+ projectId + "&versionId=" + versionId; 
 
 
 		executionIds = getExecutionsByCycleId(getExecutionsUri, client, accessKey); 
 		System.out.println(executionIds);
 
 		/** 
 		 * Update Executions with Status by Execution Id 
 		 *  
 		 */ 
 
 
 		JSONObject statusObj = new JSONObject(); 
 		statusObj.put("id", executionStatus); 
 
 
 		JSONObject executeTestsObj = new JSONObject(); 

 		
 		executeTestsObj.put("status", statusObj); 
 		executeTestsObj.put("issueId", issueID); 
 		executeTestsObj.put("stepId", stepID); 
 		executeTestsObj.put("executionId", executionId); 
 		executeTestsObj.put("comment", Executioncomment); 
 		
 		final String updateExecutionUri = API_UPDATE_STEP.replace("{SERVER}", zephyrBaseUrl) + stepResultID; 
		executeTestsObj.put("issueId", executionIds.get(executionId)); 
 			StringEntity executeTestsJSON = null; 
 			try 
 			{ 
 				executeTestsJSON = new StringEntity(executeTestsObj.toString()); 
			} 
 			catch (UnsupportedEncodingException e1)
 			{ 
 				e1.printStackTrace(); 
 				fl=false;
 			} 
 			updateExecutions(updateExecutionUri, client, accessKey, executeTestsJSON);
			return fl; 
 		} 
 
 
 	
 	final public boolean addAttachment(String cycleId,String versionId,String projectId,
 			String issueId,String entityId,String comment,String fileWithAbsolutePath) 
			throws URISyntaxException, ParseException, IOException, JSONException {
		final String API_ADD_ATTACHMENT = "{SERVER}/public/rest/api/1.0/attachment";
		boolean flg=true;
		 CommonFunctions cf = new CommonFunctions();

		/** Declare JIRA,Zephyr URL,access and secret Keys */

		//final String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
		// zephyr accessKey , we can get from Addons >> zapi section
		//final String accessKey = "OWJlMzNlMGEtMTE4ZC0zODIzLThiY2MtZTU0YmY4MTAyMmZlIGFkbWluIGFydG9zZWxlbml1bQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		//String secretKey = "b-b0eyWyKGPN_8TN3YilK6pmGYXOBwTnJHXnIF8JbVE";

	 	String zephyrBaseUrl = cf.GetData("ZephyrBaseUrl");
	 	String accessKey = cf.GetData("AccessKey");
	 	String secretKey = cf.GetData("SecretKey");
	 	
		
		/** Declare parameter values here */
	 	 String userName = cf.GetData("JiraUserName");
		final String entityName = "execution"; // entityName takes execution/stepResult as parameter value
		
		//final String fileWithAbsolutePath = "C:\\Jyoti\\MLC Client\\ExecutionReports\\Run_05-04-2017_14.00.57\\Screenshots\\TCId_05-04-2017_14.01.02.png";   //Absolute path of the file
		int expirationInSec = 360;
		
		// Add Attachment to a testcase ********DO NOT EDIT ANYTHING BELOW**********
		
		
		final ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();

		// Initializes the URL data type with strURL created above
		String attachmentUri = API_ADD_ATTACHMENT.replace("{SERVER}", zephyrBaseUrl) + "?issueId=" + issueId
				+ "&versionId=" + versionId + "&entityName=" + entityName + "&cycleId=" + cycleId + "&entityId="
				+ entityId + "&projectId=" + projectId  + "&comment=comment";
		URI uri = new URI(attachmentUri);

		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		File file = new File(fileWithAbsolutePath);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("attachment", new FileBody(file));

		HttpPost addAttachmentReq = new HttpPost(uri);
		addAttachmentReq.addHeader("Authorization", jwt);
		addAttachmentReq.addHeader("zapiAccessKey", accessKey);
		addAttachmentReq.setEntity(entity);

		try {
			response = restClient.execute(addAttachmentReq);
			
		} catch (ClientProtocolException e) {
			flg=false;
		} catch (IOException e) {
			e.printStackTrace();
			flg=false;
		}
		HttpEntity entity1 = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);
		// System.out.println(response.toString());
		if (statusCode >= 200 && statusCode < 300) {
			System.out.println("Attachment added Successfully");
		} else {
			try {
				String string = null;
				string = EntityUtils.toString(entity1);
				System.out.println(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				flg=false;
			}
		}

	
 	return flg;
 } 
 }
