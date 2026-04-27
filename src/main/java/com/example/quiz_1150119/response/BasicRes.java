package com.example.quiz_1150119.response;

public class BasicRes {
	
	/* status code: 請求服務的結果代碼*/
	private int code;
	
	/* status code: 請求服務的結果訊息*/
	private String message;
	
	public BasicRes() {
		super();
	}

	public BasicRes(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
