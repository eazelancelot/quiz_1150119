package com.example.quiz_1150119.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1150119.request.AddInfoReq;
import com.example.quiz_1150119.response.BasicRes;
import com.example.quiz_1150119.service.UserService;

/**
 * @RequestMapping(value = "/user"): 表示此 controller 底下的所有 API 路徑的前綴會是以 /user 開頭 <br>
 * 即預設的路徑會是 localhost:8080/user， 所以 addInfo 的路徑是: localhost:8080/user/register
 * */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@PostMapping(value = "/register")
	public BasicRes addInfo(@RequestBody AddInfoReq req) {
		return userService.addInfo(req.getEmail(), req.getName(), req.getPhone(), req.getAge());
	}
	
	/* 此服務的請求路徑: 	http://localhost:8080/user/login?email=xxx@xcdffg*/
	@GetMapping(value = "/login")
	public BasicRes login(@RequestParam("email") String email) {
		return userService.login(email);
	}
}
