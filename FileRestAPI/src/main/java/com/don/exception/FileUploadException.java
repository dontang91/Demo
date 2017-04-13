package com.don.exception;


public class FileUploadException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public FileUploadException(Exception e){
        super(e);
    }
}
