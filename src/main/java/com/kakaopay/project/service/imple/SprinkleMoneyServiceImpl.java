package com.kakaopay.project.service.imple;

import com.kakaopay.project.Utils.CalculateUtils;
import com.kakaopay.project.Utils.TokenUtils;
import com.kakaopay.project.entity.PickUpMoneyEntity;
import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.SprinkleMoneyEntity;
import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.exception.ProjectServiceException;
import com.kakaopay.project.repository.PickUpMoneyRepository;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.repository.SprinkleMoneyRepository;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.vo.ErrorCode;
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
import java.util.UUID;
import java.util.stream.Stream;

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
    public SprinkleMoneyResponse sprinkleInfo(String token, String roomId, long userId){
        SprinkleMoneyResponse response = new SprinkleMoneyResponse();
        SprinkleMoneyEntity sprinkleMoneyEntity = sprinkleMoneyRepository.findByTokenAndRoomId(token, roomId);

        if (sprinkleMoneyEntity == null){
            throw new ProjectServiceException("E0000", ErrorCode.Service.NO_DATA);
        }
        if (userId != sprinkleMoneyEntity.getUserId()){
            throw new ProjectServiceException("E0001", ErrorCode.Service.ONLY_SHOW_YOURSELF);
        }
        if (Period.between(sprinkleMoneyEntity.getCreatedAt().toLocalDate(), LocalDateTime.now().toLocalDate()).getDays() > 7){
            throw new ProjectServiceException("E0002", ErrorCode.Service.EXPIRE_SHOW);
        }

        response.setCode("A0000");
        response.setMessage("성공");
        response.setSprinkleMoney(sprinkleMoneyEntity);
        return response;
    }

    @Override
    @Transactional
    public SprinkleMoneyResponse sprinkleMoney(String roomId, long userId, EventVO eventVO) {

        SprinkleMoneyResponse response = new SprinkleMoneyResponse();
        String newToken = validationToken(roomId);
        if (newToken.isEmpty()){
            throw new ProjectServiceException("E0003", ErrorCode.Service.EXSIST_TOKEN);
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            throw new ProjectServiceException("E0004", ErrorCode.Service.NOT_EXSIST_USER);
        }

        Optional<RoomEntity> room = roomRepository.findById(UUID.fromString(roomId));
        if (!room.isPresent()){
            throw new ProjectServiceException("E0005", ErrorCode.Service.NOT_EXIIST_ROOM);
        }

        SprinkleMoneyEntity sprinkleMoney = setSprinkleMoneyEntity(userId, eventVO, newToken, room.get());
        long remain = userEntity.get().getPayMoney() - eventVO.getEventAmount();
        userEntity.get().setPayMoney(remain);
        userRepository.save(userEntity.get());

        response.setCode("A0000");
        response.setMessage("성공");
        response.setSprinkleMoney(sprinkleMoney);
        return response;
    }

    @Override
    @Transactional
    public PickUpMoneyResponse pickUpSprinkleMoney(String token, String roomId, long userId){
        PickUpMoneyResponse response = new PickUpMoneyResponse();

        SprinkleMoneyEntity sprinkleMoneyEntity = sprinkleMoneyRepository.findByTokenAndRoomId( token, roomId);
        if (sprinkleMoneyEntity == null){
            throw new ProjectServiceException("E0000", ErrorCode.Service.NO_DATA);
        }
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            throw new ProjectServiceException("E0004", ErrorCode.Service.NOT_EXSIST_USER);
        }

        if (userEntity.get().getRoomEntity() == null){
            throw new ProjectServiceException("E0018", ErrorCode.Service.NOT_EXSIST_ROOM_USER);
        }

        if (!userEntity.get().getRoomEntity().getId().equals(UUID.fromString(roomId))) {
            throw new ProjectServiceException("E0008", ErrorCode.Service.NOT_EXSIST_ROOM_USER);
        }

        if (sprinkleMoneyEntity.getUserId() == userId){
            throw new ProjectServiceException("E0007", ErrorCode.Service.ONLY_GET_OTHER);
        }

        if (Duration.between(sprinkleMoneyEntity.getCreatedAt(), LocalDateTime.now()).getSeconds() > 600) {
            throw new ProjectServiceException("E0006", ErrorCode.Service.EXPIRE_RECEIVE);
        }

        Stream<PickUpMoneyEntity> stream = sprinkleMoneyEntity.getPickUpMoneyEntities().stream();
        stream.forEach((PickUpMoneyEntity p) -> {
            if(p.getUserEntity() != null && p.getUserEntity().getId() == userId) {
                throw new ProjectServiceException("E0009", ErrorCode.Service.MUST_PICKUP_ONCE);
            }
        });

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

    private String validationToken(String roomId){
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
