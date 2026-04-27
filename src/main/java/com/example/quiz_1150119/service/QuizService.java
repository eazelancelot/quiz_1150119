package com.example.quiz_1150119.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.quiz_1150119.constants.ReplyMessage;
import com.example.quiz_1150119.constants.Type;
import com.example.quiz_1150119.dao.QuestionDao;
import com.example.quiz_1150119.dao.QuizDao;
import com.example.quiz_1150119.entity.Question;
import com.example.quiz_1150119.entity.Quiz;
import com.example.quiz_1150119.request.CreateQuizReq;
import com.example.quiz_1150119.request.UpdateQuizReq;
import com.example.quiz_1150119.response.BasicRes;
import com.example.quiz_1150119.response.GetQuestionListRes;
import com.example.quiz_1150119.response.GetQuizListRes;
import com.example.quiz_1150119.vo.QuestionVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizService {
	
	/* 物件(類別)、字串轉換工具*/
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	/* 一個方法中若有使用到多個 Dao 或是同一個 Dao 有呼叫多次去對資料作變更(新增、修改、刪除)，
	 * 必須要用@Transactional，因為這些 Dao 的操作，都屬於同一次的操作，因此資料的變更要嘛全部成功，
	 * 不然就全部都不成功，回溯到尚未變更之前*/
	@Transactional
	public BasicRes create(CreateQuizReq req) {
		Quiz quiz = req.getQuiz();
		List<QuestionVo> questionVoList = req.getQuestionVoList();
		BasicRes checkRes = checkDateAndType(quiz, questionVoList);
		if(checkRes != null) {
			return checkRes;
		}
		/* 1. 儲存 Quiz: id 是流水號，不用帶 */
		quizDao.insert(quiz.getTitle(), quiz.getDescription(), quiz.getStartDate(), //
				quiz.getEndDate(), quiz.isPublished());
		/* 2. 取得 quiz 最新的 id 編號*/
		int quizId = quizDao.getMaxId();
		for(QuestionVo vo : questionVoList) {
			/* 3. 把 List<String> 轉換成 String，因為 MySQL 不能存 List */
			// 類別中都會有 toString() 這個方法，這裡不能使用 toString() 將 vo.getOptionsList() 
			// 直接轉成字串，因為使用此方式轉換得到的字串無法再被轉回成原本的資料型態(基本資料型態除外)
			try {
				String options = mapper.writeValueAsString(vo.getOptionsList());
				/* 4. 存 question */
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), vo.isRequired(), options);
			} catch (Exception e) {
				return new BasicRes(ReplyMessage.OPTIONS_PARSER_ERROR.getCode(), //
						ReplyMessage.OPTIONS_PARSER_ERROR.getMessage());
			}
		}
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage());
	}
	
	private BasicRes checkDateAndType(Quiz quiz, List<QuestionVo> questionVoList) {
		/* 開始日期不能在結束日期之後*/
		if(quiz.getStartDate().isAfter(quiz.getEndDate())) {
			return new BasicRes(ReplyMessage.START_DATE_IS_AFTER_END_DATE.getCode(), //
					ReplyMessage.START_DATE_IS_AFTER_END_DATE.getMessage());
		}
		/* 開始日期只能從今天開始*/
		if(quiz.getStartDate().isBefore(LocalDate.now())) {
			return new BasicRes(ReplyMessage.START_DATE_IS_AFTER_TODAY.getCode(), //
					ReplyMessage.START_DATE_IS_AFTER_TODAY.getMessage());
		}
		/* 判斷 type 是否為定義的種類*/
		for(QuestionVo vo : questionVoList) {
			if(Type.check(vo.getType()) == false) {
				return new BasicRes(ReplyMessage.TYPE_ERROR.getCode(), //
						ReplyMessage.TYPE_ERROR.getMessage());
			}
			/* 選擇型的情形下，一定要有選項(選項不能為null 或 空)
			 * --> 選擇類型且選項是(null 或 空)  --> 回傳錯誤訊息*/
			if(Type.isChoice(vo.getType()) && CollectionUtils.isEmpty(vo.getOptionsList())) {
				return new BasicRes(ReplyMessage.OPTIONS_IS_EMPTY.getCode(), //
						ReplyMessage.OPTIONS_IS_EMPTY.getMessage());
			}
		}
		return null;
	}
	
	public BasicRes update(UpdateQuizReq req) {
		/* 檢查 quiz 中的 id*/
		int quizId = req.getQuiz().getId();
		if(quizId <= 0) {
			return new BasicRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}
		/* 檢查 questionVo 中的 quizId 是否等於 quiz 中的 id*/
		for(QuestionVo vo : req.getQuestionVoList()) {
			if(vo.getQuizId() != quizId) {
				return new BasicRes(ReplyMessage.QUIZ_ID_MISMATCH.getCode(), //
						ReplyMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}
		Quiz quiz = req.getQuiz();
		List<QuestionVo> questionVoList = req.getQuestionVoList();
		BasicRes checkRes = checkDateAndType(quiz, questionVoList);
		if(checkRes != null) {
			return checkRes;
		}
		/* 檢查問卷是否可以更新: 問卷是已發布且今天不能是在開始日期當天以及之後*/
		if(quizDao.getPublishedQuizdAfter(quizId, LocalDate.now()) != null) {
			/* dao 的結果 != null，表示有找到符合的資料 --> 因為要更新，所以應該不能有找到資料 */
			return new BasicRes(ReplyMessage.QUIZ_UPDATE_NOT_ALLOWED.getCode(), //
					ReplyMessage.QUIZ_UPDATE_NOT_ALLOWED.getMessage());
		}
		/* 更新資料 */
		/* 1. 更新 quiz */
		quizDao.update(quizId, quiz.getTitle(), quiz.getDescription(), quiz.getStartDate(), //
				quiz.getEndDate(), quiz.isPublished());
		/* 2. 更新 question: 先刪除舊資料，再新增更新後的資料(更新後的資料也算是全新的資料) */
		questionDao.delete(new ArrayList<>(quizId));
		for(QuestionVo vo : questionVoList) {
			/* 3. 把 List<String> 轉換成 String，因為 MySQL 不能存 List */
			try {
				String options = mapper.writeValueAsString(vo.getOptionsList());
				/* 4. 存 question */
				questionDao.insert(quizId, vo.getQuestionId(), vo.getQuestion(), //
						vo.getType(), vo.isRequired(), options);
			} catch (Exception e) {
				return new BasicRes(ReplyMessage.OPTIONS_PARSER_ERROR.getCode(), //
						ReplyMessage.OPTIONS_PARSER_ERROR.getMessage());
			}
		}
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage());
	}
	
	public GetQuizListRes getList(boolean isFrontend) {
		if(isFrontend) { // 等同於 isFrontend == true
			/* 前台列表 --> 取得已發佈的問卷 */
			return new GetQuizListRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage(), //
					quizDao.getAllPublished());
		}
		return new GetQuizListRes(ReplyMessage.SUCCESS.getCode(), ReplyMessage.SUCCESS.getMessage(), //
				quizDao.getAll());
	}
	
	public GetQuestionListRes getQuestionsByQuizId(int quizId) {
		/*檢查參數*/
		if(quizId <= 0) {
			return new GetQuestionListRes(ReplyMessage.QUIZ_ID_ERROR.getCode(), //
					ReplyMessage.QUIZ_ID_ERROR.getMessage());
		}
		/* 取同一張問卷下的所有問題*/
		List<Question> questionList = questionDao.getByQuizId(quizId);
		/* 把 Question 轉換成 QuestionVo */
		List<QuestionVo> voList = new ArrayList<>();
		for(Question item : questionList) {
			/* 把 Question 的字串 options 轉換回 List<String>*/
			try {
				List<String> optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				/* 把 Question 轉換成 QuestionVo */
				QuestionVo vo = new QuestionVo(quizId, item.getQuestionId(), item.getQuestion(),//
						item.getType(), item.isRequired(), optionsList);
				/* 把轉換後的每個 vo 新增到 voList 中*/
				voList.add(vo);
			} catch (Exception e) {
				return new GetQuestionListRes(ReplyMessage.OPTIONS_PARSER_ERROR.getCode(), //
						ReplyMessage.OPTIONS_PARSER_ERROR.getMessage());
			}
		}
		return new GetQuestionListRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage(), voList);
	}
	
	@Transactional
	public BasicRes delete(List<Integer> quizIdList) {
		quizDao.delete(quizIdList);
		questionDao.delete(quizIdList);
		return new BasicRes(ReplyMessage.SUCCESS.getCode(), //
				ReplyMessage.SUCCESS.getMessage());
	}
	
	
	
	
	
	
	
}
