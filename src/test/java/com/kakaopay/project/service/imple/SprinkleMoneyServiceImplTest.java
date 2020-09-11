package com.kakaopay.project.service.imple;

import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.repository.SprinkleMoneyRepository;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.RoomService;
import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.service.UserService;
import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SprinkleMoneyServiceImplTest {
    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SprinkleMoneyRepository sprinkleMoneyRepository;
    @Autowired
    private SprinkleMoneyService sprinkleMoneyService;

    @Test
    void saveRoom() {
        RoomEntity roomEntity = new RoomEntity();
        roomService.saveRoom(roomEntity);
    }

    @Test
    void saveUser() {
        for(int i = 0; i < 20; i ++){
            UserEntity userEntity = new UserEntity();
            userEntity.setPayMoney(1000);

            RoomEntity randomRoom = roomRepository.findById(1);
            userEntity.setRoomEntity(randomRoom);
            userService.saveUser(userEntity);
        }
    }
    @Test
    void newSprinkleMoney() throws Exception{
        long roomId = 1;
        long userId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse sprinkleMoneyResponse = sprinkleMoneyService.sprinkleMoney(roomId, userId, eventVO);


    }

    @Test
    void sprinkleInfo() {
//        sprinkleMoneyRepository.fi
//        sprinkleMoneyService.sprinkleInfo(token, roomId, userId);

    }


}