package com.don.model;

public class ResponseVO {
	private String Message;
	
	public ResponseVO(String message) {
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
