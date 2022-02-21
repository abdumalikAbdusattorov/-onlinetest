package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ApiResponseModel;
import uz.pdp.apponlinetestserver.security.CurrentUser;
import uz.pdp.apponlinetestserver.service.CommentService;
import uz.pdp.apponlinetestserver.payload.ReqComment;
import uz.pdp.apponlinetestserver.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqComment reqComment, @CurrentUser User user) {
        ApiResponse response = commentService.saveOrEditComment(reqComment,user);
        return ResponseEntity.status(response.isSuccess() ? response.getMessage().equals("Saved") ? HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> editViewed(@PathVariable UUID id){
        ApiResponse response= commentService.editViewed(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getViewedCount")
    public HttpEntity<?> getViewedCount(@RequestParam boolean viewed){
        return ResponseEntity.ok(commentService.getViewedCount(viewed));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deletComments(@PathVariable UUID id) {
        ApiResponse response = commentService.deleteComment(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getCommentByPagebleByViewed")
    public HttpEntity<?> getByPageableByViewed(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                               @RequestParam(value = "viewed") boolean viewed) {

        return ResponseEntity.ok(commentService.getCommentByPageableByViewed(page, size, viewed));
    }

    @GetMapping("/getAll")
    public ApiResponseModel getAll() {
        return commentService.getAll();
    }


    @GetMapping("/getCommentByUser")
    public HttpEntity<?> getCommentByUserId(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
                                            @CurrentUser User user){
        return ResponseEntity.ok(commentService.getCommentsByUser(page,size,user));
    }
}
