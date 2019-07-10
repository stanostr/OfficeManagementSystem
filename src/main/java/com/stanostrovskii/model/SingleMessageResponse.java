package com.stanostrovskii.model;

/** To return messages wrapped in a JSON object.
 * This is a cheap hack and there must be better ways.
 * @author Stan
 */
public class SingleMessageResponse {
	private String message;

	public String getMessage() {
		return message;
	}

	public SingleMessageResponse(String message)
	{
		this.message = message;
	}
	
}
