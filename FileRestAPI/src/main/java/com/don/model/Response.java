package com.don.model;

public class Response {
	private String Message;
	
	public Response(String message) {
		super();
		Message = message;
	}
	
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

}
