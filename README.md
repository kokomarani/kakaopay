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
