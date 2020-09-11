package com.kakaopay.project.controller;

import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.PickUpMoneyResponse;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SprinkleMoneyController {
    @Autowired
    private SprinkleMoneyService sprinkleMoneyService;

    @ApiOperation(value = "뿌리", httpMethod = "POST", notes = "뿌리 API")
    @PostMapping("/api/sprinkle_money")
    public ResponseEntity<SprinkleMoneyResponse> sprinkleMoney(@RequestHeader("X-USER-ID") Long userId,
                                                     @RequestHeader("X-ROOM-ID") Long roomId,
                                                     @RequestBody EventVO eventVO) throws Exception{
        SprinkleMoneyResponse response = sprinkleMoneyService.sprinkleMoney(roomId, userId, eventVO);

        if (response.getCode().equals("A0000")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "받기", httpMethod = "POST", notes = "받기 API")
    @PostMapping("/api/sprinkle_money/{token}")
    public ResponseEntity<PickUpMoneyResponse> pickUpSprinkleMoney(@RequestHeader("X-USER-ID") Long userId,
                                                                     @RequestHeader("X-ROOM-ID") Long roomId,
                                                                     @PathVariable(name = "token") String token) throws Exception{
        PickUpMoneyResponse response = sprinkleMoneyService.pickUpSprinkleMoney(token, roomId, userId);

        if (response.getCode().equals("A0000")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "조회", httpMethod = "GET", notes = "조회 API")
    @GetMapping("/api/sprinkle_money/{token}")
    public ResponseEntity<SprinkleMoneyResponse> sprinkleMoneyInfo(@RequestHeader(name = "x-room-id") long roomId,
                                                              @RequestHeader(name = "x-user-id") long userId,
                                                              @PathVariable(name = "token") String token) throws Exception{

        SprinkleMoneyResponse response = sprinkleMoneyService.sprinkleInfo(token, roomId, userId);

        if (response.getCode().equals("A0000")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


    }

}
