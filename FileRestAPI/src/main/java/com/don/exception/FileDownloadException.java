package com.don.exception;


public class FileDownloadException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public FileDownloadException(Exception e){
        super(e);
    }
	
}
