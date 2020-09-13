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
- E0001 | ONLY_SHOW_YOURSELF....| 뿌린 사람 자신만 조회할 수 있습니다.
- E0002 | EXPIRE_SHOW...........| 조회 기간이 경과되었습니다.
- E0003 | EXSIST_TOKEN..........| 토큰이 중복 됩니다. 다시 시도해 주세요.
- E0004 | NOT_EXSIST_USER.......| 존재하지 않는 유저 입니다.
- E0005 | NOT_EXIIST_ROOM.......| 카카오톡 방이 존재하지 않습니다. 방을 생성해주세요.
- E0006 | EXPIRE_RECEIVE........| 받기 유효시간이 경과하였습니다.(10분)
- E0007 | ONLY_GET_OTHER........| 자신이 뿌리기 한 건은 자신이 받을 수 없습니다.
- E0008 | NOT_EXSIST_ROOM_USER..| 카카오톡 방의 참여자가 아닙니다.
- E0009 | MUST_PICKUP_ONCE......| 뿌리기당 한 사용자는 한번만 받을수 있습니다.
```

## 테스트케이스
<img width="90%" src="https://user-images.githubusercontent.com/71118725/93023484-41ba2900-f62a-11ea-9677-e28ab2104b6f.png"></img>
<img width="90%" src="https://user-images.githubusercontent.com/71118725/93023486-441c8300-f62a-11ea-85b3-9bb54dbc909d.png"></img>

##### 1. 방 생성 완료(2개)
##### 2. 유저 생성 완료(첫번째 방 유저 5명, 두번째 방 유저 5명, 방이 없는 유저 1명)
##### 3. 20건 뿌리기 테스트 결과
```
SprinkleMoneyResponse(sprinkleMoney=SprinkleMoneyEntity(id=1, token=vox, createdAt=2020-09-14T01:30:28.931, updatedAt=2020-09-14T01:30:28.931, payMoneyAmount=1000, userId=1, roomEntity=RoomEntity(id=6fdf970d-866a-4db1-819c-0db2bde21d92), pickUpMoneyEntities=[PickUpMoneyEntity(id=1, payMoneyAmount=13, createdAt=2020-09-14T01:30:28.939, updatedAt=2020-09-14T01:30:28.939, userEntity=null), PickUpMoneyEntity(id=2, payMoneyAmount=47, createdAt=2020-09-14T01:30:28.942, updatedAt=2020-09-14T01:30:28.942, userEntity=null), PickUpMoneyEntity(id=3, payMoneyAmount=4, createdAt=2020-09-14T01:30:28.943, updatedAt=2020-09-14T01:30:28.943, userEntity=null), PickUpMoneyEntity(id=4, payMoneyAmount=20, createdAt=2020-09-14T01:30:28.944, updatedAt=2020-09-14T01:30:28.944, userEntity=null), PickUpMoneyEntity(id=5, payMoneyAmount=18, createdAt=2020-09-14T01:30:28.945, updatedAt=2020-09-14T01:30:28.945, userEntity=null), PickUpMoneyEntity(id=6, payMoneyAmount=43, createdAt=2020-09-14T01:30:28.946, updatedAt=2020-09-14T01:30:28.946, userEntity=null), PickUpMoneyEntity(id=7, payMoneyAmount=23, createdAt=2020-09-14T01:30:28.946, updatedAt=2020-09-14T01:30:28.946, userEntity=null), PickUpMoneyEntity(id=8, payMoneyAmount=46, createdAt=2020-09-14T01:30:28.947, updatedAt=2020-09-14T01:30:28.947, userEntity=null), PickUpMoneyEntity(id=9, payMoneyAmount=21, createdAt=2020-09-14T01:30:28.947, updatedAt=2020-09-14T01:30:28.947, userEntity=null), PickUpMoneyEntity(id=10, payMoneyAmount=33, createdAt=2020-09-14T01:30:28.947, updatedAt=2020-09-14T01:30:28.947, userEntity=null), PickUpMoneyEntity(id=11, payMoneyAmount=23, createdAt=2020-09-14T01:30:28.948, updatedAt=2020-09-14T01:30:28.948, userEntity=null), PickUpMoneyEntity(id=12, payMoneyAmount=36, createdAt=2020-09-14T01:30:28.948, updatedAt=2020-09-14T01:30:28.948, userEntity=null), PickUpMoneyEntity(id=13, payMoneyAmount=25, createdAt=2020-09-14T01:30:28.948, updatedAt=2020-09-14T01:30:28.948, userEntity=null), PickUpMoneyEntity(id=14, payMoneyAmount=40, createdAt=2020-09-14T01:30:28.949, updatedAt=2020-09-14T01:30:28.949, userEntity=null), PickUpMoneyEntity(id=15, payMoneyAmount=29, createdAt=2020-09-14T01:30:28.949, updatedAt=2020-09-14T01:30:28.949, userEntity=null), PickUpMoneyEntity(id=16, payMoneyAmount=13, createdAt=2020-09-14T01:30:28.950, updatedAt=2020-09-14T01:30:28.950, userEntity=null), PickUpMoneyEntity(id=17, payMoneyAmount=21, createdAt=2020-09-14T01:30:28.951, updatedAt=2020-09-14T01:30:28.951, userEntity=null), PickUpMoneyEntity(id=18, payMoneyAmount=3, createdAt=2020-09-14T01:30:28.951, updatedAt=2020-09-14T01:30:28.951, userEntity=null), PickUpMoneyEntity(id=19, payMoneyAmount=12, createdAt=2020-09-14T01:30:28.952, updatedAt=2020-09-14T01:30:28.952, userEntity=null), PickUpMoneyEntity(id=20, payMoneyAmount=530, createdAt=2020-09-14T01:30:28.952, updatedAt=2020-09-14T01:30:28.952, userEntity=null)]))
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
##### 6. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받은 후 또 받으려고 할 (Exception 케이스)
###### 6-1. 뿌리기(3건)
###### 6-2. 3번 유저 받기
###### 6-3. 3번 유저 받기 retry
###### 6-4. 받기 테스트 결과
뿌리기당 한 사용자는 한번만 받을수 있습니다. - com.kakaopay.project.exception.ProjectServiceException
##### 7. 받기 - 참가한 방이 없는 유저가 1번방 뿌리기를 받으려고 할때(Exception 케이스)
###### 7-1. 뿌리기(3건)
###### 7-2. 받기 테스트 결과
```
카카오톡 방의 참여자가 아닙니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 8. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때 10분 지남(Exception 케이스)
###### 8-1. 뿌리기(3건)
###### 8-2. 뿌리기 시간-11분으로 업데이트
###### 8-3. 받기 테스트 결과
받기 유효시간이 경과하였습니다.(10분) - com.kakaopay.project.exception.ProjectServiceException
##### 9. 받기 - 1번방 유저가 1번방 유저의 뿌리기를 받으려고 할 때(정상 케이스)
###### 9-1. 뿌리기(3건)
###### 9-2. 받기 테스트 결과
```
성공 - PickUpMoneyResponse(pickUpMoney=PickUpMoneyEntity(id=36, payMoneyAmount=233, createdAt=2020-09-14T01:30:29.118, updatedAt=2020-09-14T01:30:29.125, userEntity=UserEntity(id=3, payMoney=1421, roomEntity=RoomEntity(id=6fdf970d-866a-4db1-819c-0db2bde21d92))))
```
##### 10. 조회 - 뿌린 사람 자신만 조회를 할 수 있습니다(Exception 케이스)
###### 10-1. 뿌리기(3건)
###### 10-2. 받기 테스트 결과
```
뿌린 사람 자신만 조회할 수 있습니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 11. 조회 - 조회 기간이 경과되었습니다(Exception 케이스)
###### 11-1. 뿌리기(3건)
###### 11-2. 뿌리기 일자-8일로 업데이트
###### 11-3. 조회 테스트 결과
```
조회 기간이 경과되었습니다. - com.kakaopay.project.exception.ProjectServiceException
```
##### 12. 조회 - 정상 케이스
###### 12-1. 뿌리기(3건)
###### 12-2. 조회 테스트 결과
```
성공 - SprinkleMoneyResponse(sprinkleMoney=SprinkleMoneyEntity(id=10, token=muu, createdAt=2020-09-14T01:30:29.218, updatedAt=2020-09-14T01:30:29.218, payMoneyAmount=1000, userId=1, roomEntity=RoomEntity(id=6fdf970d-866a-4db1-819c-0db2bde21d92), pickUpMoneyEntities=[PickUpMoneyEntity(id=45, payMoneyAmount=299, createdAt=2020-09-14T01:30:29.218, updatedAt=2020-09-14T01:30:29.218, userEntity=null), PickUpMoneyEntity(id=46, payMoneyAmount=164, createdAt=2020-09-14T01:30:29.219, updatedAt=2020-09-14T01:30:29.219, userEntity=null), PickUpMoneyEntity(id=47, payMoneyAmount=537, createdAt=2020-09-14T01:30:29.219, updatedAt=2020-09-14T01:30:29.219, userEntity=null)]))
```
