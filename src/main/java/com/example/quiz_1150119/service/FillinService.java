package com.example.quiz_1150119.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_1150119.constants.ReplyMessage;
import com.example.quiz_1150119.constants.Type;
import com.example.quiz_1150119.dao.FillinDao;
import com.example.quiz_1150119.dao.QuestionDao;
import com.example.quiz_1150119.dao.QuizDao;
import com.example.quiz_1150119.dao.UserDao;
import com.example.quiz_1150119.entity.Fillin;
import com.example.quiz_1150119.entity.Question;
import com.example.quiz_1150119.entity.Quiz;
import com.example.quiz_1150119.entity.User;
import com.example.quiz_1150119.request.FillinReq;
import com.example.quiz_1150119.response.BasicRes;
import com.example.quiz_1150119.response.FeedbackRes;
import com.example.quiz_1150119.response.StatisticsRes;
import com.example.quiz_1150119.vo.AnswersVo;
import com.example.quiz_1150119.vo.FeedbackVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FillinService {
	
	/* 物件(類別)、字串轉換工具*/
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private QuizDao quizDao;
	
	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private FillinDao fillinDao;
	
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public BasicRes fillin(FillinReq req) {
		int quizId = req.getQuizId();
		LocalDate today = LocalDate.now();
		/* 確認填問卷的日期(今天)是否還能填問卷以及該問卷是否已發佈的狀態 */
		Quiz quiz = quizDao.getPublishedQuizdBetween(quizId, today);
		if(quiz == null) {
			return new BasicRes(ReplyMessage.QUIZ_NOT_FOUND.getCode(), //
					ReplyMessage.QUIZ_NOT_FOUND.getMessage());
		}
		/* 把 AnswersVo 轉成 map */
		Map<Integer, List<String>> voMap = new HashMap<>();
		for(AnswersVo answersVo : req.getAnswersVoList()) {
			voMap.put(answersVo.getQuestionId(), answersVo.getAnswersList());
		}
		/* 檢查 question */
		List<Question> questionList = questionDao.getByQuizId(quizId);
		for(Question question : questionList) {
			/* 檢查必填但沒有答案*/
			if(question.isRequired() && !voMap.containsKey(question.getQuestionId())) {
				return new BasicRes(ReplyMessage.ANSWER_IS_REQUIRED.getCode(), //
						ReplyMessage.ANSWER_IS_REQUIRED.getMessage());
			}
			/* 檢查單選但有多個選項 */
			List<String> answersList = voMap.get(question.getQuestionId());
			if(Type.isSingleType(question.getType()) && answersList.size() > 1 ) {
					return new BasicRes(ReplyMessage.ONLY_ONE_ANSWER_ALLOWED.getCode(), //
							ReplyMessage.ONLY_ONE_ANSWER_ALLOWED.getMessage());
			}
			/* 檢查是選擇類型，答案跟選項要符合 --> 拿答案來跟選項比對*/			
			try {
				if(!Type.isChoice(question.getType())) {
					continue;
				}
				/* 1. 先把 Question 中的 options 字串轉換成 List<String>， 因為寫進 DB 的字串值
				 * 就是由 List<String> 轉換來的*/
				List<String> optionsList = mapper.readValue(question.getOptions(), new TypeReference<>() {
				});
				/* 2. 拿答案來跟選項比對:  大集合.containsAll(要比對的小集合) */
				if(!optionsList.containsAll(answersList)) {
					return new BasicRes(ReplyMessage.OPTIONS_ANSWERS_MISMATCH.getCode(), //
							ReplyMessage.OPTIONS_ANSWERS_MISMATCH.getMessage());
				}
			} catch (Exception e) {
				return new BasicRes(ReplyMessage.OPTIONS_PARSER_ERROR.getCode(), //
						ReplyMessage.OPTIONS_PARSER_ERROR.getMessage());
			}
		}
		/* 將資料寫入 DB: 使用 spring data jpa 的 save() */
		/* spring date jpa 的 save(): 新增與更新都使用這個方法，在執行 save() 方法時，會先利用 entity
		 * 的 ID(PK) 來查詢資料是否存在；若存在就執行更新，不存在就執行新增*/
		for(AnswersVo vo : req.getAnswersVoList()) {
			/* 先把 vo 中的 List<String> answersList 轉成 字串*/
			try {
				String str = mapper.writeValueAsString(vo.getAnswersList());
				Fillin fillin = new Fillin(quizId, vo.getQuestionId(), req.getEmail(), str, today);
				fillinDao.save(fillin);
			} catch (Exception e) {
				return new BasicRes(ReplyMessage.ANSWERS_PARSER_ERROR.getCode(), //
						ReplyMessage.ANSWERS_PARSER_ERROR.getMessage());
			}
		}
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage());
	}
	
	public FeedbackRes feedback(int quizId) {
		if(quizId <= 0) {
			return new FeedbackRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}
		List<Fillin> fillinList = fillinDao.getByQuizId(quizId);
		/* 將 email 和每個使用者的同一張問卷所有填答 轉成 map */
		Map<String, List<AnswersVo>> map = new HashMap<>();
		Map<String, LocalDate> dateMap = new HashMap<>();
		for(Fillin fillin : fillinList) {
			/* answers 的字串轉成 List<String>*/
			try {
				List<AnswersVo> answersVoList = new ArrayList<>();
				List<String> answersList = mapper.readValue(fillin.getAnswers(), new TypeReference<>() {
				});
				AnswersVo vo = new AnswersVo(fillin.getQuestionId(), answersList);
				/* 查看 map 的 key(email) 是否已存在，若不檢查，迴圈每次新的一run 時，
				 * answersVoList 都是新的(空的)，等執行到 map.put() 方法時，因為 map 對相同的 key 值，
				 * 其對應的 value 會後蓋前，所以要把已存在的 key 對應的 answersVoList 取出後再把新的
				 * answersVo 加入到裡面 */
				if(map.containsKey(fillin.getEmail())) {
					/* map 中的 email 已存在 --> 取出 map 中已存的 answersVoList */
					/* map 中的 email 不存在 --> 使用上面 new 出來的 answersVoList */
					answersVoList = map.get(fillin.getEmail());
				}
				answersVoList.add(vo);
				map.put(fillin.getEmail(), answersVoList);
				/* 紀錄填寫日期給後續使用 */
				dateMap.put(fillin.getEmail(), fillin.getFillinDate());
			} catch (Exception e) {
				return new FeedbackRes(ReplyMessage.ANSWERS_PARSER_ERROR.getCode(), //
						ReplyMessage.ANSWERS_PARSER_ERROR.getMessage());
			}
		}
		List<FeedbackVo> feedbackVoList = new ArrayList<>();
		/* 遍歷對象不是 fillinList 而是 Map<String, List<AnswersVo>> map，兩者的差異在於
		 * map 已經是整理過後的資料 */
		for(String email : map.keySet()) {
			User user = userDao.getByEmail(email);
			FeedbackVo vo = new FeedbackVo(user, map.get(email), dateMap.get(email));
			feedbackVoList.add(vo);
		}
		return new FeedbackRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage(),//
				quizId, feedbackVoList);
	}
	
	public StatisticsRes statistics(int quizId) {
		if(quizId <= 0) {
			return new StatisticsRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}
		List<Fillin> fillinList = fillinDao.getByQuizId(quizId);
		/* Map<QuestionId, answersList>*/
		Map<Integer, List<String>> map = new HashMap<>();
		for(Fillin fillin : fillinList) {
			try {
				List<String> answersList = new ArrayList<>();
				/* map 中若 questionId 已存在 --> 取出其他人的填答 */
				/* map 中若 questionId 不存在 --> 使用新的 answersList */
				if(map.containsKey(fillin.getQuestionId())) {
					answersList = map.get(fillin.getQuestionId());
				}
				answersList.add(mapper.readValue(fillin.getAnswers(), new TypeReference<>() {
				}));
			} catch (Exception e) {
				return new StatisticsRes(ReplyMessage.ANSWERS_PARSER_ERROR.getCode(), //
						ReplyMessage.ANSWERS_PARSER_ERROR.getMessage());
			}
		}
		List<AnswersVo> answersVoList = new ArrayList<>();
		for(Integer questionId: map.keySet()) {
			answersVoList.add(new AnswersVo(questionId, map.get(questionId)));
		}
		return new StatisticsRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage(),//
				answersVoList);
	}
}
