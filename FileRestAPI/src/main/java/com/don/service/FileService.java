package com.don.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.don.dao.FileInfoDao;
import com.don.model.FileInfo;

@Service
public class FileService {
	
	@Autowired
	FileInfoDao fileDao;

	public void addFileInfo(FileInfo file) {
		fileDao.save(file);
	}

	public FileInfo getFileInfoById(Integer id) {
		return fileDao.findOne(id);
	}

	public List<FileInfo> getAllFileInfo() {
		List<FileInfo> list = new ArrayList<>();
		fileDao.findAll().forEach(list::add);
		return list;
	}

	public List<FileInfo> getLastHour() {
		List<FileInfo> list = new ArrayList<>();
		fileDao.getLastHour().forEach(list::add);
		return list;
	}

	public List<FileInfo> searchByName(String name) {
		List<FileInfo> list = new ArrayList<>();
		fileDao.searchByName(name).forEach(list::add);
		return list;
	}

	public List<FileInfo> search(String name) {
		List<FileInfo> list = new ArrayList<>();
		fileDao.searchByName(name).forEach(list::add);
		return list;
	}
	
	
	public String downloadFile(Integer id, HttpServletResponse response) {

		FileInfo fileinfo = this.getFileInfoById(id);

		if (fileinfo == null) {
			return "File record not found)";
		}

		String path = fileinfo.getPath();
		
		File file = new File(path);

		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(path);
				response.addHeader("Content-disposition", "attachment;filename=" + fileinfo.getName());
				response.setContentType("application/octet-stream");
				FileCopyUtils.copy(is, response.getOutputStream());
				return "File will be downloaded shortly";
			} catch (IOException e) {
				e.printStackTrace();
				return "IO Exception occured on server";
			}
		}
		return "File in record but does not exist";
	}
	
	
	public String saveFile(Integer id, MultipartFile file) {
		try {
			// Store file to local disk
			File dir = new File("src/main/resources/files");
			String name = file.getOriginalFilename();
			byte[] bytes = file.getBytes();
			String path = dir.getAbsolutePath() + File.separator + name;
			File savedFile = new File(path);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(savedFile));
			stream.write(bytes);
			stream.close();
			
			// Store meta-data to database
			Long size = savedFile.length();
			Date date = new Date(savedFile.lastModified());
			this.addFileInfo(new FileInfo(id,name,size,date,path));
			
			return "Successfully uploaded file: " + name;

		} catch (Exception e) {
			return "Failed to upload file: => " + e.getMessage();
		}
	}


}
