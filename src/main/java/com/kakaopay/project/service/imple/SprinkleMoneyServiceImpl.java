package com.kakaopay.project.service.imple;

import com.kakaopay.project.Utils.CalculateUtils;
import com.kakaopay.project.Utils.TokenUtils;
import com.kakaopay.project.entity.PickUpMoneyEntity;
import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.SprinkleMoneyEntity;
import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.repository.PickUpMoneyRepository;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.repository.SprinkleMoneyRepository;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.PickUpMoneyResponse;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SprinkleMoneyServiceImpl implements SprinkleMoneyService {
    @Autowired
    private SprinkleMoneyRepository sprinkleMoneyRepository;

    @Autowired
    private PickUpMoneyRepository pickUpMoneyRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    static int TOKEN_LENGTH = 3;

    @Override
    public SprinkleMoneyResponse sprinkleInfo(String token, long roomId, long userId){
        SprinkleMoneyResponse response = new SprinkleMoneyResponse();
        SprinkleMoneyEntity sprinkleMoneyEntity = sprinkleMoneyRepository.findByTokenAndRoomId(token, roomId);

        if (sprinkleMoneyEntity == null){
            response.setCode("E0000");
            response.setMessage("데이터가 존재하지 않습니다");
            return response;
        }
        if (userId != sprinkleMoneyEntity.getUserId()){
            response.setCode("E0001");
            response.setMessage("뿌린 사람 자신만 조회할 수 있습니다.");
            return response;
        }

        if (Period.between(sprinkleMoneyEntity.getCreatedAt().toLocalDate(), LocalDateTime.now().toLocalDate()).getDays() > 7){
            response.setCode("E0002");
            response.setMessage("조회 기간이 경과되었습니다.");
            return response;
        }

        response.setCode("A0000");
        response.setMessage("성공");
        response.setSprinkleMoney(sprinkleMoneyEntity);
        return response;
    }

    @Override
    @Transactional
    public SprinkleMoneyResponse sprinkleMoney(long roomId, long userId, EventVO eventVO) {
        SprinkleMoneyResponse response = new SprinkleMoneyResponse();
        String newToken = validationToken(roomId);
        if (newToken.isEmpty()){
            response.setCode("E0003");
            response.setMessage("토큰이 중복 됩니다. 다시 시도해 주세요.");
            return response;
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            response.setCode("E0004");
            response.setMessage("존재하지 않는 유저 입니다.");
            return response;
        }

        long remain = userEntity.get().getPayMoney() - eventVO.getEventAmount();
        if (remain < 0){
            response.setCode("E0005");
            response.setMessage("뿌릴 돈이 모자랍니다.");
            return response;
        }

        RoomEntity room = roomRepository.findById(roomId);
        if (room == null){
            response.setCode("E0006");
            response.setMessage("카카오톡 방이 존재하지 않습니다. 방을 생성해주세요.");
            return response;
        }

        SprinkleMoneyEntity sprinkleMoney = setSprinkleMoneyEntity(userId, eventVO, newToken, room);

        userEntity.get().setPayMoney(remain);
        userRepository.save(userEntity.get());

        response.setCode("A0000");
        response.setMessage("성공");
        response.setSprinkleMoney(sprinkleMoney);
        return response;
    }

    @Override
    @Transactional
    public PickUpMoneyResponse pickUpSprinkleMoney(String token, long roomId, long userId){
        PickUpMoneyResponse response = new PickUpMoneyResponse();

        SprinkleMoneyEntity sprinkleMoneyEntity = sprinkleMoneyRepository.findByTokenAndRoomId(token,roomId);
        if (sprinkleMoneyEntity == null){
            response.setCode("E0000");
            response.setMessage("데이터가 존재하지 않습니다");
            return response;
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            response.setCode("E0004");
            response.setMessage("존재하지 않는 유저 입니다.");
            return response;
        }

        if (Duration.between(sprinkleMoneyEntity.getCreatedAt(), LocalDateTime.now()).getSeconds() > 600) {
            response.setCode("E0007");
            response.setMessage("받기 유효시간이 경과하였습니다.(10분)");
            return response;
        }

        if (sprinkleMoneyEntity.getUserId() == userId){
            response.setCode("E0008");
            response.setMessage("자신이 뿌리기 한 건은 자신이 받을 수 없습니다.");
            return response;
        }

        for(PickUpMoneyEntity pickUpMoneyEntity : sprinkleMoneyEntity.getPickUpMoneyEntities()){
            if (pickUpMoneyEntity.getUserEntity() == null){
                pickUpMoneyEntity.setUserEntity(userEntity.get());
                pickUpMoneyRepository.save(pickUpMoneyEntity);

                long remain = userEntity.get().getPayMoney() + pickUpMoneyEntity.getPayMoneyAmount();
                userEntity.get().setPayMoney(remain);
                userRepository.save(userEntity.get());
                response.setPickUpMoney(pickUpMoneyEntity);
                break;
            }

        }
        response.setCode("A0000");
        response.setMessage("성공");
        return response;
    }

    private SprinkleMoneyEntity setSprinkleMoneyEntity(long userId, EventVO eventVO, String newToken, RoomEntity room) {
        SprinkleMoneyEntity sprinkleMoney = new SprinkleMoneyEntity();
        sprinkleMoney.setToken(newToken);
        sprinkleMoney.setUserId(userId);
        sprinkleMoney.setCreatedAt(LocalDateTime.now());
        sprinkleMoney.setUpdatedAt(LocalDateTime.now());
        sprinkleMoney.setPayMoneyAmount(eventVO.getEventAmount());
        sprinkleMoney.setRoomEntity(room);

        long[] arrDivide = CalculateUtils.divideMoney(eventVO.getEventAmount(), eventVO.getEventUserCount());
        List<PickUpMoneyEntity> pickUpMoneyList = new ArrayList<>();

        for(int i = 0; i < arrDivide.length; i++){
            PickUpMoneyEntity pickUpMoney = new PickUpMoneyEntity();
            pickUpMoney.setPayMoneyAmount(arrDivide[i]);
            pickUpMoney.setCreatedAt(LocalDateTime.now());
            pickUpMoneyList.add(pickUpMoney);
        }

        sprinkleMoney.setPickUpMoneyEntities(pickUpMoneyList);
        sprinkleMoneyRepository.save(sprinkleMoney);
        return sprinkleMoney;
    }

    private String validationToken(long roomId){
        //5번 정도 재시도, 이후 중복이면 에러코드 리턴
        int findLimit = 5;
        int i = 0;
        String token = "";
        while(i < findLimit){

            token = TokenUtils.generateToken(TOKEN_LENGTH);
            if (sprinkleMoneyRepository.findByTokenAndRoomId(token, roomId) == null) {
                return token;
            }
            i++;
        }

        return token;
    }

}
