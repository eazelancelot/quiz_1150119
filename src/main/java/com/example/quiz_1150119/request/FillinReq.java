package com.example.quiz_1150119.request;

import java.util.List;

import com.example.quiz_1150119.constants.ValidMessage;
import com.example.quiz_1150119.vo.AnswersVo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class FillinReq {

	@Min(value = 1, message = ValidMessage.QUID_ID_ERROR)
	private int quizId;

	@NotEmpty(message = ValidMessage.EMAIL_ERROR)
	private String email;

	/* answersVoList 不用限制，因為有可能全部都是非必答題且都沒回答 */
	@Valid // 嵌套驗證: 因為 AnswersVo 中的 questionId 有檢查
	private List<AnswersVo> answersVoList;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AnswersVo> getAnswersVoList() {
		return answersVoList;
	}

	public void setAnswersVoList(List<AnswersVo> answersVoList) {
		this.answersVoList = answersVoList;
	}

}
