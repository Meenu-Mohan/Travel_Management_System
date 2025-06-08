package com.ibsplc.flightbatchmanagement.exception;

public class BatchProcessingException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public BatchProcessingException(String message) {
        super(message);
    }

    public BatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
