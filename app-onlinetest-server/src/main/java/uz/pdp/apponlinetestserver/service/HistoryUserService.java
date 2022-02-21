package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.Answer;
import uz.pdp.apponlinetestserver.entity.HistoryUser;
import uz.pdp.apponlinetestserver.entity.TestBlock;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.AnswerRepository;
import uz.pdp.apponlinetestserver.repository.HistoryUserRepository;
import uz.pdp.apponlinetestserver.repository.TestBlockRepositrory;
import uz.pdp.apponlinetestserver.repository.TestWithScoreRepository;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryUserService {
    @Autowired
    HistoryUserRepository historyUserRepository;

    @Autowired
    TestBlockRepositrory testBlockRepositrory;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestWithScoreRepository testWithScoreRepository;

    @Autowired
    UserService userService;

    @Autowired
    TestService testService;

    @Autowired
    TestBlockService testBlockService;

    public ApiResponse saveHistory(ReqHistoryUser reqHistoryUser, User user) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            HistoryUser historyUser = new HistoryUser();
            historyUser.setUser(user);
            TestBlock testBlock = testBlockRepositrory.findById(reqHistoryUser.getTestBlockId()).orElseThrow(() -> new ResourceNotFoundException("testblok", "id", reqHistoryUser.getTestBlockId()));
            historyUser.setTestBlock(testBlock);
            historyUser.setFromBot(reqHistoryUser.isFromBot());
            List<Answer> answers = new ArrayList<>();
            double totalScore = 0;
            double maxScore = 0;
            for (UUID uuid : reqHistoryUser.getAnswerId()) {
                Answer answer = answerRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("answer", "id", reqHistoryUser.getAnswerId()));
                answers.add(answer);
                maxScore+=testWithScoreRepository.getScoreByTestBlockAndAnswer(testBlock.getId(),uuid);
                if (answer.isCorrectAnswer()){
                    totalScore+=testWithScoreRepository.getScoreByTestBlockAndAnswer(testBlock.getId(),uuid);
                }
            }
            historyUser.setTotalScore(totalScore);
            historyUser.setMaxScore(maxScore);
            historyUser.setAnswers(answers);
            historyUser.setTotalScore(reqHistoryUser.getTotalScore());
            historyUser.setFinished(true);
            historyUserRepository.save(historyUser);
        } catch (Exception e) {
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }

    public ApiResponse deletHistoryUser(UUID id) {
        try {
            historyUserRepository.deleteById(id);
            return new  ApiResponse("Deleted", true);
        } catch (Exception e){
            return new  ApiResponse("Error", false);
        }
    }

    public ResPageable getHistoryByPageable(Integer page, Integer size){
        Page<HistoryUser> historyUserPage=historyUserRepository.findAll(CommonUtils.getPageableById(page,size));
        return new ResPageable(historyUserPage.getContent().stream().map(this::getResHistoryUser).collect(Collectors.toList()),historyUserPage.getTotalElements(),page);
    }
    public ApiResponseModel getAll() {
        return new ApiResponseModel(true,"Ok",historyUserRepository.findAll());
    }

    public ResHistoryUser getResHistoryUser(HistoryUser historyUser){
        ResHistoryUser resHistoryUser = new ResHistoryUser();
        resHistoryUser.setCreatedAt(historyUser.getCreatedAt());
        resHistoryUser.setResUser(userService.getResUser(historyUser.getUser()));
        resHistoryUser.setFromBot(historyUser.isFromBot());
        resHistoryUser.setMaxScore(historyUser.getMaxScore());
        resHistoryUser.setTotalScore(historyUser.getTotalScore());
        List<ResAnswer> resAnswerList = new ArrayList<>();
        for (Answer answer : historyUser.getAnswers()) {
            resAnswerList.add(testService.getResAnswer(answer));
        }
        resHistoryUser.setResAnswerList(resAnswerList);
        resHistoryUser.setResTestBlock(testBlockService.getResTestBlock(historyUser.getTestBlock()));
        return resHistoryUser;
    }

    public ResPageable getHistoryByPagebleByUser(User user, Integer page, Integer size) {
        Page<HistoryUser> historyUserPage=historyUserRepository.findAllByUser(user,CommonUtils.getPageable(page,size));
        return new ResPageable(historyUserPage.getContent().stream().map(this::getResHistoryUser).collect(Collectors.toList()),historyUserPage.getTotalElements(),page);
    }
}

