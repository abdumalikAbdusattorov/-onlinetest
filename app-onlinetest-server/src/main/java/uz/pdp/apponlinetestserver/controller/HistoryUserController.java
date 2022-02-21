package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ApiResponseModel;
import uz.pdp.apponlinetestserver.payload.ReqHistoryUser;
import uz.pdp.apponlinetestserver.security.CurrentUser;
import uz.pdp.apponlinetestserver.service.HistoryUserService;
import uz.pdp.apponlinetestserver.utils.AppConstants;

import java.util.UUID;


@RestController
@RequestMapping("/api/historyUser")
public class HistoryUserController {
    @Autowired
    HistoryUserService historyUserService;

    @PostMapping
    public HttpEntity<?> saveOrEdit(@RequestBody ReqHistoryUser reqHistoryUser, @CurrentUser User user) {
        ApiResponse response = historyUserService.saveHistory(reqHistoryUser, user);
        return ResponseEntity.status(response.isSuccess() ? response.getMessage().equals("Saved") ? HttpStatus.CREATED : HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deletHistoryUser(@PathVariable UUID id) {
        ApiResponse response = historyUserService.deletHistoryUser(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/getHistoryByPageble")
    public HttpEntity<?> getByPageable(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(historyUserService.getHistoryByPageable(page, size));
    }

    @GetMapping("/getHistoryByPagebleByUser")
    public HttpEntity<?> getHistoryByPagebleByUser(
            @CurrentUser User user,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(historyUserService.getHistoryByPagebleByUser(user,page, size));
    }


    @GetMapping("/getAll")
    public ApiResponseModel getAll() {
        return historyUserService.getAll();
    }
}
