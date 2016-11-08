/**
 * 
 */
package com.hotel.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author braj.kishore
 *
 */

@SuppressWarnings("deprecation")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ResponseJson {
	private int statusCode;
	private String message;
	private Object data;
	
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public ResponseJson setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public ResponseJson setMessage(String message) {
		this.message = message;
		return this;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public ResponseJson setData(Object data) {
		this.data = data;
		
		return this;
	}
	
	
	
}
