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
* 줍기
    - (POST) http://localhost:8080/api/sprinkle_money/{token}
* 데이터 조회
    - (GET) http://localhost:8080/api/sprinkle_money/{token}     
