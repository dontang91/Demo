package com.don;

import com.don.service.AlfrescoService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class AlfrescoServiceTest {

	static String ticket;

	static AlfrescoService service;

	@BeforeClass
	public static void login() {
		service = new AlfrescoService();
		ticket = service.login("admin", "123456");
	}

	@Test
	public void testLogin() {
		System.out.println(ticket);
		assertTrue(ticket != null);
	}

	@Test
	public void testFindFile() {
		String res = service.findFile(ticket);
		System.out.println(res);
		assertTrue(res != null);
	}

	@Test
	public void testfindFolder() {
		String res = service.findFolder(ticket);
		System.out.println(res);
		assertTrue(res != null);
	}

	@Test
	public void testUpload() throws FileNotFoundException {
		File payload = new File("");
		String res = service.uploadFile(payload.getName(), "application/pdf", new FileInputStream(payload));
		System.out.println(res);
		assertTrue(res != null);
	}
}
