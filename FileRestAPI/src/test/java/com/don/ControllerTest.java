package com.don;

import com.don.controller.FileController;
import com.don.model.FileInfo;
import com.don.model.Response;
import com.don.service.AlfrescoService;
import com.don.service.FileService;
import java.util.ArrayList;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
// @SpringBootTest
@WebMvcTest(controllers = FileController.class)
public class ControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	FileService fileService;

	@MockBean
	AlfrescoService alfrescoService;

	@Test
	public void testGetFileInfoById() throws Exception {
		given(this.fileService.getFileInfoById(1)).willReturn(new FileInfo());
		this.mockMvc.perform(get("/file/1/info").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().string("{\"id\":null,\"name\":null,\"size\":null,\"date\":null,\"path\":null}"));
	}
	
	@Test
	public void testGetAllFileInfo() throws Exception {
		given(this.fileService.getAllFileInfo()).willReturn(new ArrayList<FileInfo>());
		this.mockMvc.perform(get("/files/info").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().string("[]"));
	}
	
	@Test
	public void testSearchByName() throws Exception {
		given(this.fileService.searchByName("abc")).willReturn(new ArrayList<FileInfo>());
		this.mockMvc.perform(get("/files/name/abc").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().string("[]"));
	}

	@Test
	public void testUploadFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
		given(this.fileService.saveFile(1, file)).willReturn(new Response("success"));
		this.mockMvc.perform(fileUpload("/file/1").file(file))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(content().string("{\"message\":\"success\"}"));
	}
	
	@Test
	public void testDownloadFile() throws Exception {
		this.mockMvc.perform(get("/file/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
}
