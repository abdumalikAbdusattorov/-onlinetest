package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.Comment;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.CommentRepository;
import uz.pdp.apponlinetestserver.repository.QuestionRepository;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserService userService;

    @Autowired
    TestService testService;




    public ApiResponse saveOrEditComment(ReqComment reqComment, User user) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            Comment comment = new Comment();
            if (reqComment.getId() != null) {
                comment = commentRepository.findById(reqComment.getId()).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", reqComment.getId()));
                apiResponse.setMessage("Edited");
                comment.setViewed(false);
            }
            comment.setUser(user);
            comment.setQuestion(questionRepository.findById(reqComment.getQuestionId()).orElseThrow(() -> new ResourceNotFoundException("question","id",reqComment.getQuestionId())));
            comment.setCommentText(reqComment.getCommentText());
            commentRepository.save(comment);

        } catch (Exception e) {
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }
    public ApiResponse deleteComment(UUID id) {
        try {
            commentRepository.deleteById(id);
            return new  ApiResponse("Deleted", true);
        } catch (Exception e){
            return new  ApiResponse("Error", false);
        }
    }

    public ResPageable getCommentByPageableByViewed(Integer page, Integer size,boolean viewed){
        Page<Comment> commentPage=commentRepository.findAllByViewed(CommonUtils.getPageable(page,size),viewed);
        return new ResPageable(commentPage.getContent().stream().map(this::getResComment),commentPage.getTotalElements(),page);
    }

    public ResPageable getCommentByPageble(Integer page, Integer size){
        Page<Comment> commentPage=commentRepository.findAll(CommonUtils.getPageable(page,size));
        return new ResPageable(commentPage.getContent(),commentPage.getTotalElements(),page);
    }

    public ApiResponseModel getAll() {
        return new ApiResponseModel(true,"Ok",commentRepository.findAll());
    }

    public ApiResponse editViewed(UUID id) {
        try{
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", "id", id));
            comment.setViewed(true);
            commentRepository.save(comment);
            return new ApiResponse("Viewed Edited",true);
        }catch (Exception e){
            return new  ApiResponse("Error", false);
        }
    }

    public ApiResponseModel getViewedCount(boolean viewed) {
        return new ApiResponseModel(true,"OK",commentRepository.countComment(viewed));
    }

    public  ResPageable getCommentsByUser(Integer page, Integer size, User user) {
        Page<Comment> allByUser = commentRepository.findAllByUser(user,CommonUtils.getPageable(page,size));
        return new ResPageable(allByUser.getContent().stream().map(this::getResComment),allByUser.getTotalElements(),page);
    }
    public ResComment getResComment(Comment comment){
        ResComment resComment = new ResComment();
        resComment.setId(comment.getId());
        resComment.setCommentText(comment.getCommentText());
        resComment.setViewed(comment.isViewed());
        resComment.setResUser(userService.getResUser(comment.getUser()));
        resComment.setResQuestion(testService.getResQuestion(comment.getQuestion()));
        return resComment;
    }
}
