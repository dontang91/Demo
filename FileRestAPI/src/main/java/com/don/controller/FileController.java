package com.don.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.don.model.FileInfo;
import com.don.model.ResponseVO;
import com.don.service.AlfrescoService;
import com.don.service.FileService;

@RestController
@SuppressWarnings("unused")
public class FileController {

	@Autowired
	FileService localFileService;

	@Autowired
	AlfrescoService alfrescoService;

	@RequestMapping("/file/{id}/info")
	public FileInfo getFileInfo(@PathVariable Integer id) {
		return localFileService.getFileInfoById(id);
	}

	@RequestMapping("/files/info")
	public List<FileInfo> getAllFileInfo() {
		return localFileService.getAllFileInfo();
	}

	@RequestMapping("/files/name/{name}")
	public List<FileInfo> searchByName(@PathVariable String name) {
		return localFileService.searchByName(name);
	}

	@RequestMapping("/files/search") 
	public List<FileInfo> getAllFileInfqeo(@RequestParam(value = "name", required=true) String name) {
		return localFileService.search(name);
	}

	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public ResponseVO downloadFileHandler(@PathVariable("id") Integer id, HttpServletResponse response)
			throws ClientProtocolException, IOException {

		/* //for alfresco service 
		FileInfo fileinfo = localFileService.getFileInfoById(id);
		if (fileinfo == null) {
			return newResponse("File record not found");
		}
		//Here i stored disk path instead of nodeid
		String nodeId = fileinfo.getPath();
		//return alfrescoService.downloadFile(nodeId);
		*/
		
		return localFileService.downloadFile(id, response);

	}

	@RequestMapping(value = "/file/{id}",method = RequestMethod.POST)
	public ResponseVO uploadFileHandler(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file)
			throws IOException {

		if (!file.isEmpty()) {
			String contentType = file.getContentType();
			String name = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			// return alfrescoService.uploadFile(name,contentType, is);
			return localFileService.saveFile(id, file);
		} else {
			return new ResponseVO("Failed to upload file: => the file was empty.");
		}

	}

}
