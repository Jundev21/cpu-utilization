# JAVA CPU 사용률 측정 서비스



### 프로젝트 정보
- Spring Boot - 3.2.5
- Gradle
- Java - 17

### 의존성
- H2
- JPA
- Lombok
- Validation
- Spring Web
- Swagger
- QueryDsl


### 기능 요구 사항
#### 1. 데이터 수집 및 저장
  - CPU 사용률 수집: 서버의 CPU 사용률을 1분 단위로 수집
    - OperatingSystemMXBean 사용하여 cpu 사용률 추출.
    - cpu 추출값이 0부터 1 사이 값임으로 100퍼센트 기준으로 값을 변경하여 두자리수까지 나타내도록 변경.
    - scheduler 을 사용하여 1분단위로 데이터에 저장.
    

  - 데이터 저장: 수집된 데이터를 데이터베이스에 저장합니다.
      - 임시적으로 h2 데이터를 사용하여 데이터 저장.

####   2. 데이터 조회 API
- ##### 분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회
  - Rest APi : api/cpu/minute?startDate=2024-05-02 01&endDate=2024-05-02 03
  - 분단위 조회는 지정한 시간 구간이 필요함으로 시작 날짜와 종료날짜를 파라미터값으로 전달받습니다. 날짜와 시간이 필요함으로 LocalDateTime 에서 "yyyy-MM-dd HH" 형태로
    전달받습니다.
  - 예시 startDate = 2024-05-01 01 / endDate=2024-05-3 02 일때에 5월 1일 1시부터 5월 2일 3시까지의 모든 분단위 cpu 사용률 데이터 수집
  - front end 에 모든 데이터를 출력해야함으로 시간단위로 모든 분 0분부터 59분까지의 데이터를 퍼센테이지(%) 형태로 응답한다. 데이터가 없을경우에는 0% 로 출력
  - 예시: 1시부터 3시까지의 분단위 데이터를 조회하고 있을경우에는 데이터를 표시 만약 없는 데이터일경우 0으로 표시하여 모든 분단위를 출력한다.
  ![Screenshot 2024-05-26 at 2.32.11 PM.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fzr%2F08by3snj28gbpsxs7rdlk0mw0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_GXRb0D%2FScreenshot%202024-05-26%20at%202.32.11%E2%80%AFPM.png)
   

    


- ##### 시 단위 조회: 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회합니다.
  - Rest APi : api/cpu/hour?pickedDay=2024-05-02
  - 시간단위 조회는 지정 날짜를 파라미터값으로 전달받습니다. 정해진 날짜만 필요함으로 LocalDate 에서 "yyyy-MM-dd" 형태로 전달받습니다.
  - 예시 pickedDay= 2024-05-01 일때에 5월 1일 0시부터 5월 1일 23시까지의 모든 분단위 cpu 사용률의 최소값,최대값,평균값 데이터 수집합니다.
  - front end 에 모든 데이터를 출력해야함으로 시간단위로 모든 시간 0시부터 23시까지의 데이터를 날짜의 데이터를 퍼센테이지(%) 형태로 응답합니다. 데이터가 없을 경우에는 모두 0.0 으로
                표기

  - 예시: 0시부터 23시까지의 분단위 데이터를 조회하여 최소값,최대값,평균값을 출력. 데이터가 있을경우에는 표시 만약 없는 데이터일경우 모두 0으로 표시하여 해당날짜의 모든 시단위를 출력한다.
                ![Screenshot 2024-05-26 at 2.31.50 PM.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fzr%2F08by3snj28gbpsxs7rdlk0mw0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_2OMrL8%2FScreenshot%202024-05-26%20at%202.31.50%E2%80%AFPM.png)




- ##### 일 단위 조회: 지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회합니다.
  - Rest APi : api/cpu/day?startDate=2024-05-01&endDate=2024-05-26
  - 일단위 조회는 지정한 날짜 구간이 필요함으로 시작 날짜와 종료날짜를 파라미터값으로 전달받습니다. 날짜형태만 필요함으로 LocalDate 에서 "yyyy-MM-dd" 형태로 전달받습니다.
  - 예시 startDate = 2024-05-01 / endDate=2024-05-26 일때에 5월 1일 부터 5월 26일 의 cpu 사용률의 최소값,최대값,평균값 데이터를 수집합니다.
  - front end 에 모든 데이터를 출력해야함으로 날짜 구간의 모든 날짜의 데이터를 퍼센테이지(%) 형태로 응답합니다. 데이터가 없을 경우에는 모두 0.0 으로 표기
  - 예시: 5월 1일부터 5월 26일까지의 분단위 데이터를 조회하여 최소값,최대값,평균값을 출력. 데이터가 있을경우에는 표시 만약 없는 데이터일경우 0으로 표시하여 날짜 구간의 모든 날짜를 날짜 단위로 출력한다.
  ![Screenshot 2024-05-26 at 2.39.39 PM.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fzr%2F08by3snj28gbpsxs7rdlk0mw0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_2zk59H%2FScreenshot%202024-05-26%20at%202.39.39%E2%80%AFPM.png)




- ##### Swagger를 사용하여 API 문서화
  - http://localhost:8080//swagger.html 를 통하여 스웨거 접속


### 데이터 제공 기한
   - 분 단위 API : 최근 1주 데이터 제공
     - 시작날짜가 현재날짜 기준으로 1주일이 넘어가는 요청이면 에러 응답
   - 시 단위 API : 최근 3달 데이터 제공
     - 지정날짜가 현재날짜 기준으로 3달이 넘어가는 요청이면 에러 응답
   - 일 단위 API : 최근 1년 데이터 제공
     - 시작날짜가 현재날짜 기준으로 1년이 넘어가는 요청이면 에러 응답

### 예외 처리
   - API 요청 시 잘못된 파라미터에 대한 예외를 처리합니다.
     - 잘못된 시간형태 요청시에 에러 응답.
   
### 테스트
   - 유닛 테스트: controller, service, repository 모든 테스트 케이스 작성





