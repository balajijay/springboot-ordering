package com.balaji.orderservice.exception;

public class OrderServiceException extends RuntimeException {

	private static final long serialVersionUID = 1716888913275413198L;
	
	private int statusCode = 0;
	
	
	public OrderServiceException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public OrderServiceException(String message, int statusCode, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
        return statusCode;
    }
}
