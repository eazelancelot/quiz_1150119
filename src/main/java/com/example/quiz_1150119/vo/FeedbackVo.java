package com.example.quiz_1150119.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz_1150119.entity.User;

/* 一個 FeedbackVo 表示一位使用者對一張問卷的填答 */
public class FeedbackVo {

	private User user;

	private List<AnswersVo> answersVoList;

	private LocalDate fillinDate;

	public FeedbackVo() {
		super();
	}

	public FeedbackVo(User user, List<AnswersVo> answersVoList, LocalDate fillinDate) {
		super();
		this.user = user;
		this.answersVoList = answersVoList;
		this.fillinDate = fillinDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<AnswersVo> getAnswersVoList() {
		return answersVoList;
	}

	public void setAnswersVoList(List<AnswersVo> answersVoList) {
		this.answersVoList = answersVoList;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}

}
