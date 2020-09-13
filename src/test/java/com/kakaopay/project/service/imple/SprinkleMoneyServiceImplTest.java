package com.kakaopay.project.service.imple;

import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.exception.ProjectException;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.repository.SprinkleMoneyRepository;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.PickUpMoneyResponse;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SprinkleMoneyServiceImplTest {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SprinkleMoneyRepository sprinkleMoneyRepository;
    @Autowired
    private SprinkleMoneyService sprinkleMoneyService;

    /**
     * 방 생성
     * **/
    @Test
    void test_beforeRoom() {
        //1번방
        RoomEntity roomEntity = new RoomEntity();
        roomRepository.save(roomEntity);

        //2번방
        RoomEntity roomEntity2 = new RoomEntity();
        roomRepository.save(roomEntity2);
        System.out.println("1. 방 생성 완료");
    }

    /**
     * 유저 생성
     * **/
    @Test
    void test_beforeUser() {
        //첫번째방 유저들(5명)
        for(int i = 0; i < 5; i ++){
            UserEntity userEntity = new UserEntity();
            userEntity.setPayMoney(1000);

            RoomEntity randomRoom = roomRepository.findTopByOrderByIdAsc();
            userEntity.setRoomEntity(randomRoom);
            userRepository.save(userEntity);
        }

        //두번째방 유저들(5명)
        for(int i = 0; i < 5; i ++){
            UserEntity userEntity2 = new UserEntity();
            userEntity2.setPayMoney(500);

            RoomEntity randomRoom = roomRepository.findTopByOrderByIdDesc();
            userEntity2.setRoomEntity(randomRoom);
            userRepository.save(userEntity2);
        }

        //방이 없는 유저 1명
        UserEntity userEntity3 = new UserEntity();
        userEntity3.setPayMoney(100);
        userRepository.save(userEntity3);
        System.out.println("2. 유저 생성 완료");
    }

    /****
     * 뿌리기
     * @throws Exception
     */
    @Test
    void test_sprinkleAnd() throws Exception{
        //1번방의 1번 유저가 뿌리기
        long userId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse response = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), userId, eventVO);
        System.out.println("3. 뿌리기 테스트 결과");
        System.out.println(response.toString());
    }

    /****
     * 받기 - 뿌리기 한 사람이 받기(Exception케이스)
     * @throws Exception
     */
    @Test
    void test_sprinkleAndPickUpSelf() throws Exception{
        long sprinkleAndPickUpUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleAndPickUpUserId, eventVO);
        System.out.println("4. 받기 - 뿌리기 한 사람이 받기(Exception 케이스)");
        System.out.println("4-1. 뿌리기 데이터 결과");
        System.out.println(sprinkleMoney.toString());

        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), sprinkleAndPickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("4-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0007"));
        }
    }

    /****
     * 받기 - 2번방 유저가 1번방 뿌리기를 받으려고 할 때(Exception 케이스)
     * @throws Exception
     */
    @Test
    void test_sprinkleAndPickUpOtherRoom() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("5. 받기 - 2번방 유저가 1번방 뿌리기를 받으려고 할 때(Exception 케이스)");
        System.out.println("5-1. 뿌리기 데이터 결과");
        System.out.println(sprinkleMoney.toString());

        long pickUpUserId = 6;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("5-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0008"));
        }
    }

    /****
     * 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)
     * @throws Exception
     */
    @Test
    void test_sprinkleAndPickUpNotParticipate() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("6. 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)");
        System.out.println("6-1. 뿌리기 데이터 결과");
        System.out.println(sprinkleMoney.toString());

        long pickUpUserId = 11;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("6-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0018"));
        }
    }

    /****
     * 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때(정상 케이스)
     * @throws Exception
     */
    @Test
    void test_sprinkleAndPickUpSuccess() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("7. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때스(정상 케이스)");
        System.out.println("7-1. 뿌리기 데이터 결과");
        System.out.println(sprinkleMoney.toString());

        long pickUpUserId = 3;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            System.out.println("7-2. 받기 테스트 결과");
            System.out.println(pickUpSprinkleMoney.getMessage() +" - " +pickUpSprinkleMoney.toString());
            System.out.println(pickUpSprinkleMoney.toString());
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            assertThat(p.getResultCode(), equalTo("E0007"));
        }
    }

}