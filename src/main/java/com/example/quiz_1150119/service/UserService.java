package com.example.quiz_1150119.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz_1150119.constants.ReplyMessage;
import com.example.quiz_1150119.dao.UserDao;
import com.example.quiz_1150119.response.BasicRes;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public BasicRes addInfo(String email, String name, String phone, int age) {
		/* 參數檢查 */
		if(!StringUtils.hasText(email)) { //前面有驚嘆號，等同於 StringUtils.hasText(email) == false
			return new BasicRes(ReplyMessage.PARAM_EMAIL_ERROR.getCode(), //
					ReplyMessage.PARAM_EMAIL_ERROR.getMessage());
		}
		if(!StringUtils.hasText(name)) { //前面有驚嘆號，等同於 StringUtils.hasText(name) == false
			return new BasicRes(ReplyMessage.PARAM_NAME_ERROR.getCode(), //
					ReplyMessage.PARAM_NAME_ERROR.getMessage());
		}
		if(age < 0) {
			return new BasicRes(ReplyMessage.PARAM_AGE_ERROR.getCode(), //
					ReplyMessage.PARAM_AGE_ERROR.getMessage());
		}
		userDao.insert(email, name, phone, age);
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage());
	}
	
	public BasicRes login(String email) {
		if(!StringUtils.hasText(email)) { //前面有驚嘆號，等同於 StringUtils.hasText(email) == false
			return new BasicRes(ReplyMessage.PARAM_EMAIL_ERROR.getCode(), //
					ReplyMessage.PARAM_EMAIL_ERROR.getMessage());
		}
		if(userDao.selectCount(email) != 1) {
			return new BasicRes(ReplyMessage.EMAIL_NOT_FOUND.getCode(), ReplyMessage.EMAIL_NOT_FOUND.getMessage());
		}
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage());
	}

}
