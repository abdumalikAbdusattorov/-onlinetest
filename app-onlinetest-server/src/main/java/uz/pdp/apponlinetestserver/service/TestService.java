package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.*;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.*;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;

    @Autowired
    SubjectRepositrory subjectRepositrory;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    SubjectService subjectService;

    @Autowired
    TestBlockRepositrory testBlockRepositrory;

    @Autowired
    TestWithScoreRepository testWithScoreRepository;

    @Autowired
    HistoryUserRepository historyUserRepository;

//    public ApiResponse saveOrEditTest(ReqTest reqTest) {
//        ApiResponse apiResponse = new ApiResponse();
//        try {
//            apiResponse.setMessage("Saved");
//            apiResponse.setSuccess(true);
//            Test test = new Test();
//            if (reqTest.getId() != null) {
//                test = testRepository.findById(reqTest.getId()).orElseThrow(() -> new ResourceNotFoundException("Test", "id", reqTest.getId()));
//                apiResponse.setMessage("Test Edited");
//                List<Question> questionList = test.getQuestions();
//                for (Question question : questionList) {
//                    questionRepository.delete(question);
//                }
//            }
//            test.setLevel(reqTest.getLevel());
//            if (reqTest.getSubjectId() != null) {
//                test.setSubject(subjectRepositrory.findById(reqTest.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject", "id", reqTest.getSubjectId())));
//            }
//            test.setTitle(reqTest.getTitle());
//            Test savedTest = testRepository.save(test);
//            for (ReqQuestion reqQuestion : reqTest.getReqQuestionList()) {
//                Question question1 = new Question();
//                question1.setTest(savedTest);
//                question1.setQuestion(reqQuestion.getQuestion());
//                Question savedQuestion = questionRepository.save(question1);
//                for (ReqAnswer reqAnswer : reqQuestion.getReqAnswerList()) {
//                    Answer answer = new Answer();
//                    answer.setAnswer(reqAnswer.getAnswer());
//                    answer.setCorrectAnswer(reqAnswer.isСorrectAnswer());
//                    answer.setQuestion(savedQuestion);
//                    answerRepository.save(answer);
//                }
//            }
//
//
//        } catch (Exception e) {
//            apiResponse.setMessage("Error");
//            apiResponse.setSuccess(false);
//        }
//        return apiResponse;
//    }

    public ApiResponse saveOrEditTest(ReqTest reqTest) {
        ApiResponse apiResponse=new ApiResponse();
        try{
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            Test test = new Test();
            if (reqTest.getId()!=null){
//                test = testRepository.findById(reqTest.getId()).orElseThrow(() -> new ResourceNotFoundException("Test", "id", reqTest.getId()));
                testRepository.deleteById(reqTest.getId());
                apiResponse.setMessage("Test Edited");
//                List<Question> questionList = test.getQuestions();
//                for (Question question : questionList) {
//                questionRepository.deleteAnswersByQuestionId(question.getId());
//                }
//                for (Question question : questionList) {
//                   questionRepository.deleteQuestionByQuestionId(question.getId());
//                    questionRepository.deleteById(question.getId());
//                }
            }
            test.setLevel(reqTest.getLevel());
            if (reqTest.getSubjectId()!=null){
                test.setSubject(subjectRepositrory.findById(reqTest.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject","id",reqTest.getSubjectId())));
            }
            test.setTitle(reqTest.getTitle());
            Test savedTest=testRepository.save(test);
            for (ReqQuestion reqQuestion : reqTest.getReqQuestionList()) {
                Question question1=new Question();
                question1.setTest(savedTest);
                question1.setQuestion(reqQuestion.getQuestion());
                Question savedQuestion=questionRepository.save(question1);
                for (ReqAnswer reqAnswer : reqQuestion.getReqAnswerList()) {
                    Answer answer=new Answer();
                    answer.setAnswer(reqAnswer.getAnswer());
                    answer.setCorrectAnswer(reqAnswer.isCorrectAnswer());
                    answer.setQuestion(savedQuestion);
                    answerRepository.save(answer);
                }
            }


        }catch (Exception e){
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }

    public ApiResponse deleteTest(UUID id) {
        try {
            testRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    public ApiResponseModel getAll() {
        List<ResTest> resTestList = new ArrayList<>();
        List<Test> all = testRepository.findAll();
        for (Test test : all) {
            resTestList.add(getResTest(test));
        }
        return new ApiResponseModel(true, "Ok", resTestList);

    }

    public ResTest getResTest(Test test) {
        ResTest resTest = new ResTest();
        resTest.setId(test.getId());
        resTest.setTitle(test.getTitle());
        resTest.setLevel(test.getLevel());
        resTest.setResSubject(subjectService.getResSubject(test.getSubject()));
        List<ResQuestion> resQuestions = new ArrayList<>();
        for (Question question : test.getQuestions()) {
            resQuestions.add(getResQuestion(question));
        }
        resTest.setResQuestionList(resQuestions);
        return resTest;

    }

    public ResQuestion getResQuestion(Question question) {
        ResQuestion question1 = new ResQuestion();
        question1.setId(question.getId());
        question1.setQuestion(question.getQuestion());
        List<ResAnswer> resAnswers = new ArrayList<>();
        for (Answer answer : question.getAnswers()) {
            resAnswers.add(getResAnswer(answer));
        }
        question1.setResAnswers(resAnswers);
        return question1;
    }

    public ResAnswer getResAnswer(Answer answer) {
        ResAnswer answer1 = new ResAnswer();
        answer1.setId(answer.getId());
        answer1.setAnswer(answer.getAnswer());
        answer1.setCorrect(answer.isCorrectAnswer());
        return answer1;
    }

    public ResPageable getTestsByPageable(Integer page, Integer size) {
        Page<Test> testPage = testRepository.findAll(CommonUtils.getPageableById(page, size));
        return new ResPageable(testPage.getContent().stream().map(this::getResTest).collect(Collectors.toList()), testPage.getTotalElements(), page);
    }

    public ApiResponseModel getTestBySubject(Integer subjectId, Level level) {
        return new ApiResponseModel(true,"Ok",testRepository.findAllBySubjectIdAndLevel(subjectId,level).stream().map(this::getResTest).collect(Collectors.toList()));
    }

    public ApiResponse solveTest(List<ReqSolveTes> reqSolveTesList, User user) {
        try {
            double totalScore=0;
            double maxScore=0;
            List<Answer> answerList=new ArrayList<>();
            UUID testBlockId=null;
            for (ReqSolveTes reqSolveTes : reqSolveTesList) {
                Answer answer = answerRepository.findById(reqSolveTes.getAnswerId()).orElseThrow(() -> new ResourceNotFoundException("answer", "id", reqSolveTes.getAnswerId()));
                if (answer.isCorrectAnswer()){
                    totalScore+=reqSolveTes.getScore();
                }
                testBlockId=reqSolveTes.getTestBlockId();
                answerList.add(answer);
                maxScore+=testWithScoreRepository.getScoreByTestBlockAndAnswer(reqSolveTes.getTestBlockId(),reqSolveTes.getAnswerId());
            }
            HistoryUser historyUser=new HistoryUser();
            historyUser.setUser(user);
            historyUser.setAnswers(answerList);
            historyUser.setFromBot(false);
            historyUser.setTotalScore(totalScore);
            historyUser.setTestBlock(testBlockRepositrory.findById(testBlockId).orElseThrow(() -> new ResourceNotFoundException("testBlockId","id",null)));
            historyUser.setMaxScore(maxScore);
            historyUserRepository.save(historyUser);
            return new ApiResponse("Ok",true);
        }catch (Exception e){
            return new ApiResponse("Error",false);
        }
    }
}
