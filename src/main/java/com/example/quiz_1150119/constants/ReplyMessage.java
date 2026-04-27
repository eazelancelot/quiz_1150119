package com.example.quiz_1150119.constants;

public enum ReplyMessage {

	SUCCESS(200, "Success!!"), //
	LOGIN_SUCCESS(200, "Login success!!"),//
	PARAM_EMAIL_ERROR(400, "Param email error!!"),//
	PARAM_NAME_ERROR(400, "Param name error!!"),//
	PARAM_AGE_ERROR(400, "Param age error!!"),//
	EMAIL_NOT_FOUND(404, "Email not found!!"),//
	START_DATE_IS_AFTER_END_DATE(400, "Start date is after end date!!"),//
	START_DATE_IS_AFTER_TODAY(400, "Start date is after today!!"),//
	TYPE_ERROR(400, "Type error!!"),//
	OPTIONS_IS_EMPTY(400, "Options is empty!!"),//
	OPTIONS_PARSER_ERROR(400, "Options parser error!!"),//
	QUIZ_ID_ERROR(400, "Quiz id error!!"),//
	QUIZ_ID_MISMATCH(400, "Quiz id mismatch!!"),//
	QUIZ_NOT_FOUND(404, "Quiz not found!!"),//
	ANSWER_IS_REQUIRED(400, "Answer is required!!"),//
	ONLY_ONE_ANSWER_ALLOWED(400, "Only one answer allowed!!"),//
	OPTIONS_ANSWERS_MISMATCH(400, "Options answers mismatch!!"),//
	ANSWERS_PARSER_ERROR(400, "Answers parser error!!"),//
	QUIZ_UPDATE_NOT_ALLOWED(400, "Quiz update not allowed!!");

	private int code;

	private String message;

	private ReplyMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
