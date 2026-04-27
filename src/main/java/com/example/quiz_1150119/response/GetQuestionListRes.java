package com.example.quiz_1150119.response;

import java.util.List;

import com.example.quiz_1150119.vo.QuestionVo;

public class GetQuestionListRes extends BasicRes {

	private List<QuestionVo> questionVoList;

	public GetQuestionListRes() {
		super();
	}

	public GetQuestionListRes(int code, String message) {
		super(code, message);
	}

	public GetQuestionListRes(int code, String message, List<QuestionVo> questionVoList) {
		super(code, message);
		this.questionVoList = questionVoList;
	}

	public List<QuestionVo> getQuestionVoList() {
		return questionVoList;
	}

	public void setQuestionVoList(List<QuestionVo> questionVoList) {
		this.questionVoList = questionVoList;
	}

}
