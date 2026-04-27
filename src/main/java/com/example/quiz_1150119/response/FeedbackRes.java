package com.example.quiz_1150119.response;

import java.util.List;

import com.example.quiz_1150119.vo.FeedbackVo;

public class FeedbackRes extends BasicRes {

	private int quizId;

	/* 一個 FeedbackVo 表示一位使用者對一張問卷的填答 */
	private List<FeedbackVo> feedbackVoList;

	public FeedbackRes() {
		super();
	}

	public FeedbackRes(int code, String message) {
		super(code, message);
	}

	public FeedbackRes(int code, String message, int quizId, List<FeedbackVo> feedbackVoList) {
		super(code, message);
		this.quizId = quizId;
		this.feedbackVoList = feedbackVoList;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public List<FeedbackVo> getFeedbackVoList() {
		return feedbackVoList;
	}

	public void setFeedbackVoList(List<FeedbackVo> feedbackVoList) {
		this.feedbackVoList = feedbackVoList;
	}

}
