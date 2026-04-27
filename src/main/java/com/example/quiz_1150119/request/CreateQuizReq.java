package com.example.quiz_1150119.request;

import java.util.List;

import com.example.quiz_1150119.constants.ValidMessage;
import com.example.quiz_1150119.entity.Quiz;
import com.example.quiz_1150119.vo.QuestionVo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateQuizReq {

	/* 檢查的對象是基本資料型態時 */
//	@Min(18)
//	private int age;
	
	/* 檢查的對象是字串時 */
//	@NotBlank(message = "字串不能為空!!")
//	private String test;
	
	/* 檢查的對象是類別時，通常類別是自定義的: 
	 * 1. 類別本身要檢查，因為他是 CreateQuizReq 的屬性之一
	 * 2. 類別中的屬性也要檢查 
	 * 3. 嵌套驗證: 屬性中加上 @Valid，是為了讓屬性中的屬性限制生效*/
	@Valid
	@NotNull(message = ValidMessage.QUIZ_IS_NULL)
	private Quiz quiz;

	/* @NotEmpty: 限制屬性不能是 null 或長度為0(對象可以是字串或集合類)*/
	@Valid
	@NotEmpty(message = ValidMessage.QUESTIONVO_IS_EMPTY)
	private List<QuestionVo> questionVoList;

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public List<QuestionVo> getQuestionVoList() {
		return questionVoList;
	}

	public void setQuestionVoList(List<QuestionVo> questionVoList) {
		this.questionVoList = questionVoList;
	}

}
