package com.example.quiz_1150119.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "fillin")
@IdClass(value = FillinId.class)
public class Fillin {

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Id
	@Column(name = "question_id")
	private int questionId;

	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "answers")
	private String answers;

	@Column(name = "fillin_date")
	private LocalDate fillinDate;

	public Fillin() {
		super();
	}

	public Fillin(int quizId, int questionId, String email, String answers, LocalDate fillinDate) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.email = email;
		this.answers = answers;
		this.fillinDate = fillinDate;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}

}
