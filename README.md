# 카카오페이 뿌리기 기능 구현하기
 
## 개발 환경
- Spring Boot 2.3.3.RELEASE
- JPA
- H2
- Gradle

## 테이블 명세
<img src="https://user-images.githubusercontent.com/71118725/92926619-41c5f780-f477-11ea-8c83-9405d104d881.png" width="90%"></img>

<img src="https://user-images.githubusercontent.com/71118725/92926568-2d81fa80-f477-11ea-8054-a9999feef1fd.png" width="90%"></img>

## API 명세
#### API 기본 정보
* 기본 요청 헤더
```
X-USER-ID: 사용자의 식별값(숫자형)
X-ROOM-ID: 대화방의 식별값(문자형)
```

* 뿌리기
    - (PUT) http://localhost:8080/api/sprinkle_money 
* 받기
    - (POST) http://localhost:8080/api/sprinkle_money/{token}
* 데이터 조회
    - (GET) http://localhost:8080/api/sprinkle_money/{token}     

* 에러 코드 정의
```
- E0000 | NO_DATA...............| 데이터가 존재하지 않습니다.
- E0001 | ONLY_SHOW_OTHER.......| 뿌린 사람 자신만 조회할 수 있습니다.
- E0002 | EXPIRE_SHOW...........| 조회 기간이 경과되었습니다.
- E0003 | EXSIST_TOKEN..........| 토큰이 중복 됩니다. 다시 시도해 주세요.
- E0004 | NOT_EXSIST_USER.......| 존재하지 않는 유저 입니다.
- E0005 | NOT_EXIIST_ROOM.......| 카카오톡 방이 존재하지 않습니다. 방을 생성해주세요.
- E0006 | EXPIRE_RECEIVE........| 받기 유효시간이 경과하였습니다.(10분)
- E0007 | ONLY_GET_OTHER........| 자신이 뿌리기 한 건은 자신이 받을 수 없습니다.
- E0008 | NOT_EXSIST_ROOM_USER..| 카카오톡 방의 참여자가 아닙니다.
```

## 테스트케이스
<img src="https://user-images.githubusercontent.com/71118725/93021611-7627e800-f61e-11ea-8a61-22e8e0aa0e8d.png" width="90%"></img>

<img src="https://user-images.githubusercontent.com/71118725/93021604-71fbca80-f61e-11ea-8a59-6b0785c023a4.png" width="90%"></img>

##### 1. 방 생성 완료(2개)
##### 2. 유저 생성 완료(첫번째 방 유저 5명, 두번째 방 유저 5명, 방이 없는 유저 1명)
##### 3. 20건 뿌리기 테스트 결과
```
SprinkleMoneyResponse(sprinkleMoney=SprinkleMoneyEntity(id=1, token=vrc, createdAt=2020-09-13T23:58:06.279, updatedAt=2020-09-13T23:58:06.279, payMoneyAmount=1000, userId=1, roomEntity=RoomEntity(id=3576d87f-1263-4906-8eb7-93bbe522c929), pickUpMoneyEntities=[PickUpMoneyEntity(id=1, payMoneyAmount=34, createdAt=2020-09-13T23:58:06.285, updatedAt=2020-09-13T23:58:06.285, userEntity=null), PickUpMoneyEntity(id=2, payMoneyAmount=39, createdAt=2020-09-13T23:58:06.286, updatedAt=2020-09-13T23:58:06.286, userEntity=null), PickUpMoneyEntity(id=3, payMoneyAmount=43, createdAt=2020-09-13T23:58:06.287, updatedAt=2020-09-13T23:58:06.287, userEntity=null), PickUpMoneyEntity(id=4, payMoneyAmount=26, createdAt=2020-09-13T23:58:06.287, updatedAt=2020-09-13T23:58:06.287, userEntity=null), PickUpMoneyEntity(id=5, payMoneyAmount=30, createdAt=2020-09-13T23:58:06.288, updatedAt=2020-09-13T23:58:06.288, userEntity=null), PickUpMoneyEntity(id=6, payMoneyAmount=45, createdAt=2020-09-13T23:58:06.288, updatedAt=2020-09-13T23:58:06.288, userEntity=null), PickUpMoneyEntity(id=7, payMoneyAmount=21, createdAt=2020-09-13T23:58:06.289, updatedAt=2020-09-13T23:58:06.289, userEntity=null), PickUpMoneyEntity(id=8, payMoneyAmount=48, createdAt=2020-09-13T23:58:06.289, updatedAt=2020-09-13T23:58:06.289, userEntity=null), PickUpMoneyEntity(id=9, payMoneyAmount=18, createdAt=2020-09-13T23:58:06.290, updatedAt=2020-09-13T23:58:06.290, userEntity=null), PickUpMoneyEntity(id=10, payMoneyAmount=2, createdAt=2020-09-13T23:58:06.290, updatedAt=2020-09-13T23:58:06.290, userEntity=null), PickUpMoneyEntity(id=11, payMoneyAmount=12, createdAt=2020-09-13T23:58:06.291, updatedAt=2020-09-13T23:58:06.291, userEntity=null), PickUpMoneyEntity(id=12, payMoneyAmount=2, createdAt=2020-09-13T23:58:06.291, updatedAt=2020-09-13T23:58:06.291, userEntity=null), PickUpMoneyEntity(id=13, payMoneyAmount=16, createdAt=2020-09-13T23:58:06.292, updatedAt=2020-09-13T23:58:06.292, userEntity=null), PickUpMoneyEntity(id=14, payMoneyAmount=45, createdAt=2020-09-13T23:58:06.292, updatedAt=2020-09-13T23:58:06.292, userEntity=null), PickUpMoneyEntity(id=15, payMoneyAmount=11, createdAt=2020-09-13T23:58:06.293, updatedAt=2020-09-13T23:58:06.293, userEntity=null), PickUpMoneyEntity(id=16, payMoneyAmount=18, createdAt=2020-09-13T23:58:06.293, updatedAt=2020-09-13T23:58:06.293, userEntity=null), PickUpMoneyEntity(id=17, payMoneyAmount=9, createdAt=2020-09-13T23:58:06.293, updatedAt=2020-09-13T23:58:06.293, userEntity=null), PickUpMoneyEntity(id=18, payMoneyAmount=7, createdAt=2020-09-13T23:58:06.294, updatedAt=2020-09-13T23:58:06.294, userEntity=null), PickUpMoneyEntity(id=19, payMoneyAmount=46, createdAt=2020-09-13T23:58:06.294, updatedAt=2020-09-13T23:58:06.294, userEntity=null), PickUpMoneyEntity(id=20, payMoneyAmount=528, createdAt=2020-09-13T23:58:06.295, updatedAt=2020-09-13T23:58:06.295, userEntity=null)]))
```
##### 4. 받기 - 뿌리기 한 사람이 받기(Exception 케이스)
###### 4-1. 뿌리기(3건)
###### 4-2. 받기 테스트 결과
```
자신이 뿌리기 한 건은 자신이 받을 수 없습니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 5. 받기 - 2번방 유저가 1번방 뿌리기를 받으려고 할 때(Exception 케이스)
###### 5-1. 뿌리기(3건)
###### 5-2. 받기 테스트 결과
```
카카오톡 방의 참여자가 아닙니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 6. 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)
###### 6-1. 뿌리기(3건)
###### 6-2. 받기 테스트 결과
```
카카오톡 방의 참여자가 아닙니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 7. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때스(정상 케이스)
###### 7-1. 뿌리기(3건)
###### 7-2. 받기 테스트 결과
```
성공 - PickUpMoneyResponse(pickUpMoney=PickUpMoneyEntity(id=30, payMoneyAmount=331, createdAt=2020-09-13T23:58:06.413, updatedAt=2020-09-13T23:58:06.424, userEntity=UserEntity(id=3, payMoney=1331, roomEntity=RoomEntity(id=3576d87f-1263-4906-8eb7-93bbe522c929))))
```

