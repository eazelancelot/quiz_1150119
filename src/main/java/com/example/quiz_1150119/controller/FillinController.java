package com.example.quiz_1150119.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1150119.request.FillinReq;
import com.example.quiz_1150119.response.BasicRes;
import com.example.quiz_1150119.response.FeedbackRes;
import com.example.quiz_1150119.response.StatisticsRes;
import com.example.quiz_1150119.service.FillinService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class FillinController {

	@Autowired
	private FillinService fillinService;
	
	@PostMapping("quiz/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) {
		return fillinService.fillin(req);
	}
	
	@GetMapping("quiz/feedback")
	public FeedbackRes feedback(@RequestParam("quizId") int quizId) {
		return fillinService.feedback(quizId);
	}
	
	@GetMapping("quiz/statistics")
	public StatisticsRes statistics(@RequestParam("quizId") int quizId) {
		return fillinService.statistics(quizId);
	}
}
