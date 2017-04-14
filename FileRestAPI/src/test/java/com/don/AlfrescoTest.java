package com.don;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.don.exception.FileUploadException;
import com.don.model.Ticket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class AlfrescoTest {
	
	private static final String BASE_PUBLIC_URL = "http://localhost:8080/alfresco/api/-default-/public";
	private static final String LOGIN_URL = BASE_PUBLIC_URL + "/authentication/versions/1/tickets";
	private static final String NODE_URL = BASE_PUBLIC_URL + "/alfresco/versions/1/queries/nodes";
	private static final String NODE_ID = "71aad90f-948b-408b-8998-437d69e82c35";
	private static final String DEMO_FOLDER_ID = "571e5bc8-afe0-4085-b4a3-99df789eb355";
	private static final String ROOT_FOLDER_ID = "f0d63b0c-d93c-423e-87ed-ee3c42882269";
	
	@Test
	public void testLogin() {
		String ticket = this.login();
		assertTrue(ticket!=null);
		System.out.println(ticket);
	}
	
	@Test
	public void testFindFolder() {
		String ticket = this.login();
		String res = this.findFolder(ticket);
		assertTrue(res!=null);
		System.out.println(res);
	}

	@Test
    public void testUploadFile()throws IOException {
		String ticket = this.login();
		String demoFolderId = findFolder(ticket);
		String url = BASE_PUBLIC_URL + "/alfresco/versions/1/nodes/" + demoFolderId + "/children" + "?alf_ticket=" + ticket;
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        File payload = new File("/Users/tang/Documents/bear.png");

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("filedata", payload)
                .addTextBody("name", "demo.png")
                .addTextBody("nodeType", "cm:content")
                .addTextBody("overwrite","true")
                .build();
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }
	
	@Test
	public void testFindFile() {
		String ticket = this.login();
		String res = this.findFile(ticket);
		assertTrue(res!=null);
		System.out.println(res);
	}
	
	@Test
	public void downloadFile() throws ClientProtocolException, IOException {

		String ticket = this.login();
		String demoFileId = findFile(ticket);
		String url = BASE_PUBLIC_URL + "/alfresco/versions/1/nodes/" + demoFileId + "/content" + "?alf_ticket=" + ticket;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");
		request.addHeader("Accept", "application/json");
		HttpResponse response = client.execute(request);
		File destFile = new File("/Users/tang/Documents/download.png");
		
		BufferedInputStream input = new BufferedInputStream(response.getEntity().getContent());
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destFile));
		int data;
		while ((data =input.read()) != -1) {
			output.write(data);
		}
		output.close();
	}
	
	
	public String login() {
		String ticket = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(LOGIN_URL);
		HttpEntity entity = new StringEntity("{\"userId\":\"admin\",\"password\":\"123456\"}",ContentType.APPLICATION_JSON);
		request.addHeader("content-type", "application/json");
		request.addHeader("Accept", "application/json");
		request.setEntity(entity);
		try {
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			Ticket o = (Ticket) new ObjectMapper().readValue(result.toString(), Ticket.class);
			ticket = o.getData().getId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ticket;
	}
	
    public String findFile(String ticket) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet();
        String res = null;
		try {
			URI uri = new URIBuilder(NODE_URL)
			        .addParameter("alf_ticket",ticket)
			        .addParameter("term","demo")
			        .addParameter("nodeType", "cm:content")
			        .build();
			request.setURI(uri);
			request.addHeader("Accept", "application/json");
			HttpResponse response = client.execute(request);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getEntity().getContent());
			JsonNode entries = root.path("list").path("entries");
			for (JsonNode entry : entries) {
				res = entry.path("entry").path("id").asText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
		
    }
	
	public String findFolder(String ticket) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet();
		String res = null;
		try {
			URI uri = new URIBuilder(NODE_URL)
					.addParameter("alf_ticket", ticket)
					.addParameter("term", "Demo")
					.addParameter("nodeType", "cm:folder")
					.build();
			request.setURI(uri);
			request.addHeader("Accept", "application/json");
			HttpResponse response = client.execute(request);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getEntity().getContent());
			JsonNode entries = root.path("list").path("entries");
			for (JsonNode entry : entries) {
				res = entry.path("entry").path("id").asText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
