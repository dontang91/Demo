package com.don;

import com.don.controller.FileController;
import com.don.model.FileInfo;
import com.don.model.ResponseVO;
import com.don.service.AlfrescoService;
import com.don.service.FileService;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
// @SpringBootTest
@WebMvcTest(controllers = FileController.class)
public class DemoTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	FileService fileService;

	@MockBean
	AlfrescoService alfrescoService;

	@Test
	public void testGetFileInfoById() throws Exception {
		FileInfo a = new FileInfo();
		FileInfo b = new FileInfo();
		ArrayList<FileInfo> list = new ArrayList<>();
		list.add(a);
		list.add(b);
		given(this.fileService.getAllFileInfo()).willReturn(list);
		this.mockMvc.perform(get("/files/info").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(2)));
	}
	
	
	@Test
	public void testDownloadFile() throws Exception {

		HttpServletResponse response = mock(HttpServletResponse.class);
		given(this.fileService.downloadFile(1,response)).willReturn(new ResponseVO("success"));
		this.mockMvc.perform(get("/file/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
}
