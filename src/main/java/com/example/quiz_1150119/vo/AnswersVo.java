package com.example.quiz_1150119.vo;

import java.util.List;

import com.example.quiz_1150119.constants.ValidMessage;

import jakarta.validation.constraints.Min;

/* 一個 AnswersVo 表示一個問題的答案*/
public class AnswersVo {

	@Min(value = 1, message = ValidMessage.QUESTION_ID_ERROR)
	private int questionId;

	/* 有非必填的類型，所以不一定會有答案，也就不用限制 */
	private List<String> answersList;

	public AnswersVo() {
		super();
	}

	public AnswersVo(int questionId, List<String> answersList) {
		super();
		this.questionId = questionId;
		this.answersList = answersList;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public List<String> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<String> answersList) {
		this.answersList = answersList;
	}

}
