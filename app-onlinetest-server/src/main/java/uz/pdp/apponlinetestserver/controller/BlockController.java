package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.service.BlockService;
import uz.pdp.apponlinetestserver.utils.AppConstants;


@RestController
@RequestMapping("/api/block")
public class BlockController {
    @Autowired
    BlockService blockService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqBlock reqBlock){
        ApiResponse response = blockService.saveOrEditBlock(reqBlock);
        return ResponseEntity.status(response.isSuccess()? response.getMessage().equals("Saved")? HttpStatus.CREATED:HttpStatus.ACCEPTED:HttpStatus.CONFLICT).body(response);
    }
    @DeleteMapping("/{id}")
    public HttpEntity<?> deletBlock(@PathVariable Integer id){
        ApiResponse response=blockService.deletBlock(id);
        return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
    }
    @GetMapping("/getBlockByPageble")
    public HttpEntity<?> getByPageable(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                       @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE)Integer size){
        return ResponseEntity.ok(blockService.getBlockByPageable(page,size));
    }
    @GetMapping("/getAll")
    public ApiResponseModel getAll(){
        return blockService.getAll();
    }

    @GetMapping("/getBlockByLevel")
    public ApiResponseModel getBlockByLevel(@RequestParam Level level){
        return blockService.getBlockByLevel(level);
    }
}

