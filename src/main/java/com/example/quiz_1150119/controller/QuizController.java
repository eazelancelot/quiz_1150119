package com.example.quiz_1150119.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1150119.request.CreateQuizReq;
import com.example.quiz_1150119.request.UpdateQuizReq;
import com.example.quiz_1150119.response.BasicRes;
import com.example.quiz_1150119.response.GetQuestionListRes;
import com.example.quiz_1150119.response.GetQuizListRes;
import com.example.quiz_1150119.service.QuizService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizController {

	@Autowired
	private QuizService quizService;
	
	/* @Valid: 是為了要讓 CreateQuizReq 中的屬性限制生效*/
	@PostMapping("quiz/create")
	public BasicRes create(@Valid @RequestBody CreateQuizReq req) {
		return quizService.create(req);
	}
	
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody UpdateQuizReq req) {
		return quizService.update(req);
	}
	
	@GetMapping("quiz/get_quiz_list")
	public GetQuizListRes getList(@RequestParam("isFrontend") boolean isFrontend) {
		return quizService.getList(isFrontend);
	}
	
	/* API 路徑: http://localhost:8080/quiz/get_question_list?quizId=1 
	 * ? 後面的 quizId 是 @RequestParam 中的字串*/
	@GetMapping("quiz/get_questions")
	public GetQuestionListRes getQuestionsByQuizId(@RequestParam("quizId") int quizId) {
		return quizService.getQuestionsByQuizId(quizId);
	}
	
	/*相同的 key 但會有多個值時，API 路徑，問號後面的格式會是: ?quizId=1&quizId=2&quizId=3，
	 * 多個參數時用 & 串接*/
	@GetMapping("quiz/delete")
	public BasicRes delete(@RequestParam("quizId") List<Integer> quizIdList) {
		return quizService.delete(quizIdList);
	}
}
