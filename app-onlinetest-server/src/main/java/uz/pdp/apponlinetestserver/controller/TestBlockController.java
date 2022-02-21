package uz.pdp.apponlinetestserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ApiResponseModel;
import uz.pdp.apponlinetestserver.payload.ReqTestBlock;
import uz.pdp.apponlinetestserver.service.TestBlockService;
import uz.pdp.apponlinetestserver.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/testBlock")
public class TestBlockController {
    @Autowired
    TestBlockService testBlockService;


    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqTestBlock reqTestBlock){
        ApiResponse response=testBlockService.saveOrEdit(reqTestBlock);
        return ResponseEntity.status(response.isSuccess()?response.getMessage().equals("Saved")?
                HttpStatus.CREATED:HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(response);
    }


    @GetMapping("/getByLevelAndBlock")
    public ApiResponseModel getByLevelAndBlock(@RequestParam Level level,
                                               @RequestParam Integer blockId){
        return testBlockService.getByLevelAndBlock(level,blockId);
    }
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id){
        return ResponseEntity.ok(testBlockService.getById(id));
    }

    @GetMapping("/getTestBlockByPageable")
    public HttpEntity<?> getTestBlockByPageable(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                            @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE)Integer size){
        return ResponseEntity.ok(testBlockService.getTestBlockByPageable(page,size));
    }
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteTestBlock(@PathVariable UUID id){
        ApiResponse response =testBlockService.deleteTestBlock(id);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }


    @GetMapping("/getAll")
    public ApiResponseModel getAll(){
        return testBlockService.getAll();
    }


}
