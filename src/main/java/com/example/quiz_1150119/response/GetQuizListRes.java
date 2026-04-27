package com.example.quiz_1150119.response;

import java.util.List;

import com.example.quiz_1150119.entity.Quiz;

public class GetQuizListRes extends BasicRes {

	private List<Quiz> quizList;

	public GetQuizListRes() {
		super();
	}

	public GetQuizListRes(int code, String message) {
		super(code, message);
	}

	public GetQuizListRes(int code, String message, List<Quiz> quizList) {
		super(code, message);
		this.quizList = quizList;
	}

	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void setQuizList(List<Quiz> quizList) {
		this.quizList = quizList;
	}

}
