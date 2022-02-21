package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ApiResponseModel;
import uz.pdp.apponlinetestserver.payload.ReqSubject;
import uz.pdp.apponlinetestserver.service.SubjectService;
import uz.pdp.apponlinetestserver.utils.AppConstants;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {


    @Autowired
    SubjectService subjectService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqSubject reqSubject){
        ApiResponse response = subjectService.saveOrEdit(reqSubject);
        return ResponseEntity.status(response.isSuccess()?response.getMessage().equals("Saved")? HttpStatus.CREATED:HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSubject(@PathVariable Integer id){
        ApiResponse response = subjectService.deleteSubject(id);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }
    @GetMapping("/getSubjectByPageable")
    public HttpEntity<?> getByPageable(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                       @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
        return ResponseEntity.ok(subjectService.getSubjectByPageable(page,size));
    }

    @GetMapping("/getAll")
    public ApiResponseModel getAll(){
        return subjectService.getAll();
    }

}
