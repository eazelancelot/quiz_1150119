package com.example.quiz_1150119.response;

import java.util.List;

import com.example.quiz_1150119.vo.AnswersVo;

public class StatisticsRes extends BasicRes {

	private List<AnswersVo> answersVoList;

	public StatisticsRes() {
		super();
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
	}

	public StatisticsRes(int code, String message, List<AnswersVo> answersVoList) {
		super(code, message);
		this.answersVoList = answersVoList;
	}

	public List<AnswersVo> getAnswersVoList() {
		return answersVoList;
	}

	public void setAnswersVoList(List<AnswersVo> answersVoList) {
		this.answersVoList = answersVoList;
	}

}
