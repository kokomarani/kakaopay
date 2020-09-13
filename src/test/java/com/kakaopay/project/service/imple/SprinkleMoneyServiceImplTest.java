package com.kakaopay.project.service.imple;

import com.kakaopay.project.entity.RoomEntity;
import com.kakaopay.project.entity.SprinkleMoneyEntity;
import com.kakaopay.project.entity.UserEntity;
import com.kakaopay.project.exception.ProjectException;
import com.kakaopay.project.repository.RoomRepository;
import com.kakaopay.project.repository.SprinkleMoneyRepository;
import com.kakaopay.project.repository.UserRepository;
import com.kakaopay.project.service.SprinkleMoneyService;
import com.kakaopay.project.vo.EventVO;
import com.kakaopay.project.vo.response.PickUpMoneyResponse;
import com.kakaopay.project.vo.response.SprinkleMoneyResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void beforeRoom() {
        //1번방
        RoomEntity roomEntity = new RoomEntity();
        roomRepository.save(roomEntity);

        //2번방
        RoomEntity roomEntity2 = new RoomEntity();
        roomRepository.save(roomEntity2);
        System.out.println("##### 1. 방 생성 완료(2개)");
    }

    /**
     * 유저 생성
     * **/
    @Test
    @Order(2)
    void beforeUser() {
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
        System.out.println("##### 2. 유저 생성 완료(첫번째 방 유저 5명, 두번째 방 유저 5명, 방이 없는 유저 1명)");
    }

    /****
     * 뿌리기
     * @throws Exception
     */
    @Test
    @Order(3)
    void sprinkle() throws Exception{
        //1번방의 1번 유저가 뿌리기
        long userId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(20);

        SprinkleMoneyResponse response = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), userId, eventVO);
        System.out.println("##### 3. 20건 뿌리기 테스트 결과");
        System.out.println(response.toString());
    }

    /****
     * 받기 - 뿌리기 한 사람이 받기(Exception케이스)
     * @throws Exception
     */
    @Test
    @Order(4)
    void sprinkleAndPickUpSelf() throws Exception{
        long sprinkleAndPickUpUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleAndPickUpUserId, eventVO);
        System.out.println("##### 4. 받기 - 뿌리기 한 사람이 받기(Exception 케이스)");
        System.out.println("###### 4-1. 뿌리기(3건)");

        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), sprinkleAndPickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 4-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0007"));
        }
    }

    /****
     * 받기 - 2번방 유저가 1번방 뿌리기를 받으려고 할 때(Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(5)
    void sprinkleAndPickUpOtherRoom() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 5. 받기 - 2번방 유저가 1번방 뿌리기를 받으려고 할 때(Exception 케이스)");
        System.out.println("###### 5-1. 뿌리기(3건)");

        long pickUpUserId = 6;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 5-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0008"));
        }
    }

    /****
     * 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받은 후 또 받으려고 할때 (Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(6)
    void sprinkleAndPickUpMustOnce() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 6. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받은 후 또 받으려고 할 (Exception 케이스)");
        System.out.println("###### 6-1. 뿌리기(3건)");

        long pickUpUserId = 3;

        try{
            System.out.println("###### 6-2. 3번 유저 받기");
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            System.out.println("###### 6-3. 3번 유저 받기 retry");
            PickUpMoneyResponse retryPickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 6-4. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0009"));
        }
    }

    /****
     * 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(7)
    void sprinkleAndPickUpNotParticipate() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 7. 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)");
        System.out.println("###### 7-1. 뿌리기(3건)");

        long pickUpUserId = 11;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 7-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0018"));
        }
    }


    /****
     * 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때 10분 지남(Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(8)
    void sprinkleAndPickUpExpire() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 8. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때 10분 지남(Exception 케이스)");
        System.out.println("###### 8-1. 뿌리기(3건)");

        System.out.println("###### 8-2. 뿌리기 시간-11분으로 업데이트");
        Optional<SprinkleMoneyEntity> selectSprinkleMoney = sprinkleMoneyRepository.findById(sprinkleMoney.getSprinkleMoney().getId());
        selectSprinkleMoney.get().setCreatedAt(LocalDateTime.now().minusMinutes(11));
        sprinkleMoneyRepository.save(selectSprinkleMoney.get());

        long pickUpUserId = 3;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 8-3. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0006"));
        }
    }

    /****
     * 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때(정상 케이스)
     * @throws Exception
     */
    @Test
    @Order(9)
    void sprinkleAndPickUpSuccess() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 9. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때(정상 케이스)");
        System.out.println("###### 9-1. 뿌리기(3건)");

        long pickUpUserId = 3;
        try{
            PickUpMoneyResponse pickUpSprinkleMoney = sprinkleMoneyService.pickUpSprinkleMoney(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), pickUpUserId);
            System.out.println("###### 9-2. 받기 테스트 결과");
            System.out.println(pickUpSprinkleMoney.getMessage() +" - " +pickUpSprinkleMoney.toString());
            assertThat(pickUpSprinkleMoney.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            assertThat(p.getResultCode(), equalTo("E0007"));
        }
    }

    /****
     * 조회 - 뿌린 사람 자신만 조회를 할 수 있습니다(Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(10)
    void sprinkleInfoOnlyShowYourself() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 10. 조회 - 뿌린 사람 자신만 조회를 할 수 있습니다(Exception 케이스)");
        System.out.println("###### 10-1. 뿌리기(3건)");

        long selectUserId = 3;
        try{
            SprinkleMoneyResponse sprinkleMoneyResponse = sprinkleMoneyService.sprinkleInfo(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), selectUserId);
            assertThat(sprinkleMoneyResponse.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 10-2. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0001"));
        }
    }
    /****
     * 조회 - 조회 기간이 경과되었습니다(Exception 케이스)
     * @throws Exception
     */
    @Test
    @Order(11)
    void sprinkleInfoExpireShow() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 11. 조회 - 조회 기간이 경과되었습니다(Exception 케이스)");
        System.out.println("###### 11-1. 뿌리기(3건)");

        System.out.println("###### 11-2. 뿌리기 일자-8일로 업데이트");
        Optional<SprinkleMoneyEntity> selectSprinkleMoney = sprinkleMoneyRepository.findById(sprinkleMoney.getSprinkleMoney().getId());
        selectSprinkleMoney.get().setCreatedAt(LocalDateTime.now().minusDays(8));
        sprinkleMoneyRepository.save(selectSprinkleMoney.get());

        try{
            SprinkleMoneyResponse sprinkleMoneyResponse = sprinkleMoneyService.sprinkleInfo(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), sprinkleUserId);
            assertThat(sprinkleMoneyResponse.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            System.out.println("###### 11-3. 받기 테스트 결과");
            System.out.println(p.getDefaultMsg() +" - "+ p.toString());
            assertThat(p.getResultCode(), equalTo("E0002"));
        }
    }
    /****
     * 조회 - 정상 케이스
     * @throws Exception
     */
    @Test
    @Order(12)
    void sprinkleInfo() throws Exception{
        long sprinkleUserId = 1;
        EventVO eventVO = new EventVO();
        eventVO.setEventAmount(1000);
        eventVO.setEventUserCount(3);

        SprinkleMoneyResponse sprinkleMoney = sprinkleMoneyService.sprinkleMoney(roomRepository.findTopByOrderByIdAsc().getId().toString(), sprinkleUserId, eventVO);
        System.out.println("##### 12. 조회 - 정상 케이스");
        System.out.println("###### 12-1. 뿌리기(3건)");

        try{
            SprinkleMoneyResponse sprinkleMoneyResponse = sprinkleMoneyService.sprinkleInfo(sprinkleMoney.getSprinkleMoney().getToken(), sprinkleMoney.getSprinkleMoney().getRoomEntity().getId().toString(), sprinkleUserId);
            System.out.println("###### 12-2. 받기 테스트 결과");
            System.out.println(sprinkleMoneyResponse.getMessage() +" - " +sprinkleMoneyResponse.toString());
            assertThat(sprinkleMoneyResponse.getCode(), equalTo("A0000"));
        }catch (ProjectException p){
            assertThat(p.getResultCode(), equalTo("E0001"));
        }
    }
}