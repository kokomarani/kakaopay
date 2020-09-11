package com.kakaopay.project.service;

import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.PickUpMoneyResponse;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;

public interface SprinkleMoneyService {
    SprinkleMoneyResponse sprinkleInfo(String token, long roomId, long userId) throws Exception;
    SprinkleMoneyResponse sprinkleMoney(long roomId, long userId, EventVO eventVO) throws Exception;
    PickUpMoneyResponse pickUpSprinkleMoney(String token, long roomId, long userId) throws Exception;

}
