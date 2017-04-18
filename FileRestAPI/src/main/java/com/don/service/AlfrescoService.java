package com.don.service;

import com.don.exception.FileUploadException;
import com.don.model.Ticket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AlfrescoService {

	private static final String BASE_PUBLIC_URL = "http://localhost:8080/alfresco/api/-default-/public";
	private static final String LOGIN_URL = BASE_PUBLIC_URL + "/authentication/versions/1/tickets";
	private static final String NODE_URL = BASE_PUBLIC_URL + "/alfresco/versions/1/queries/nodes";

	public String login(String userName, String password) {
		String ticket = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(LOGIN_URL);
		HttpEntity entity = new StringEntity("{\"userId\":\"" + userName + "\",\"password\":\"" + password + "\"}",
				ContentType.APPLICATION_JSON);
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

	public String uploadFile(String fileName, String contentType, InputStream inputStream) {
		String res = null;
		String ticket = this.login("username", "password");
		String folderId = findFolder(ticket);
		String url = BASE_PUBLIC_URL + "/alfresco/versions/1/nodes/" + folderId + "/children";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost();

		URI uri = null;
		try {
			uri = new URIBuilder(url).addParameter("alf_ticket", ticket).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new FileUploadException(e);
		}
		request.setURI(uri);
		HttpEntity entity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addBinaryBody("filedata", inputStream, ContentType.create(contentType), fileName)
				//.addTextBody("name", "file.file")
				.addTextBody("nodeType", "cm:content")
				.addTextBody("autoRename", "true")
				.addTextBody("fields", "name,id")
				//.addTextBody("overwrite", "true")
				.build();
		request.setEntity(entity);
		HttpResponse response = null;
		try {
			response = client.execute(request);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getEntity().getContent());
			JsonNode entry = root.path("entry").path("id");
			res = entry.asText();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileUploadException(e);
		}
		return res;
	}

	public InputStream downloadFile(String nodeId) throws ClientProtocolException, IOException, URISyntaxException {

		String ticket = this.login("username", "password");

		String url = BASE_PUBLIC_URL + "/alfresco/versions/1/nodes/" + nodeId + "/content" + "?alf_ticket=" + ticket;
		//URI u = new URIBuilder(url).addParameter("attachment", "true").build();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");
		request.addHeader("Accept", "application/json");
		HttpResponse response = client.execute(request);
		InputStream is = new BufferedInputStream(response.getEntity().getContent());
		return is;
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
			throw new FileUploadException(e);
		}
		return res;
	}

}
