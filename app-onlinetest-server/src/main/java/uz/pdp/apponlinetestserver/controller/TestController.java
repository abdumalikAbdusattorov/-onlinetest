package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apponlinetestserver.entity.Subject;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ApiResponseModel;
import uz.pdp.apponlinetestserver.payload.ReqSolveTes;
import uz.pdp.apponlinetestserver.payload.ReqTest;
import uz.pdp.apponlinetestserver.security.CurrentUser;
import uz.pdp.apponlinetestserver.service.TestService;
import uz.pdp.apponlinetestserver.utils.AppConstants;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")

public class TestController {
    @Autowired
    TestService testService;

    @PostMapping
    public HttpEntity<?> saveOrEditTest(@RequestBody ReqTest reqTest){
        ApiResponse response=testService.saveOrEditTest(reqTest);
        return ResponseEntity.status(response.isSuccess()?response.getMessage().equals("Saved")? HttpStatus.CREATED:HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(response);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> testDelete(@PathVariable UUID id){
        ApiResponse apiResponse = testService.deleteTest(id);
        return ResponseEntity.status(apiResponse.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(apiResponse);
    }
    @GetMapping("/getAll")
    public ApiResponseModel getAll(){
        return testService.getAll();
    }

    @GetMapping("/getTestsByPageable")
    public HttpEntity<?> getTestsByPageable(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                            @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
        return ResponseEntity.ok(testService.getTestsByPageable(page,size));
    }

    @GetMapping("/getTestBySubject")
    public ApiResponseModel getTestBySubject(@RequestParam Integer id,
                                             @RequestParam Level level){
        return testService.getTestBySubject(id,level);
    }
    @PostMapping("/solveTest")
    public HttpEntity<?> solveTest (@RequestBody List <ReqSolveTes> reqSolveTesList, @CurrentUser User user){
        ApiResponse response = testService.solveTest(reqSolveTesList,user);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }
}
