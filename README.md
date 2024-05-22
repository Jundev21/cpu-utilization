1. 프로젝트 정보 
    - Spring Boot - 3.2.5
    - Gradle
    - Java - 17


2. 의존성
    - H2
    - JPA
    - Lombok
    - Validation
    - Spring Web


3. 기능 요구 사항
   1) 데이터 수집 및 저장
      - CPU 사용률 수집: 서버의 CPU 사용률을 분 단위로 수집합니다.
      - 데이터 저장: 수집된 데이터를 데이터베이스에 저장합니다.

   2) 데이터 조회 API
      - 분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.
      - 시 단위 조회: 지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다.
      - 일 단위 조회: 지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다.
      - Swagger를 사용하여 API 문서화를 설정하세요.
 
   3) 데이터 제공 기한
      - 분 단위 API : 최근 1주 데이터 제공
      - 시 단위 API : 최근 3달 데이터 제공
      - 일 단위 API : 최근 1년 데이터 제공



6. 예외 처리
   - 데이터 수집 실패 시 예외를 처리하고 로그를 남깁니다.
   - API 요청 시 잘못된 파라미터에 대한 예외를 처리합니다.


5. 테스트
   - 유닛 테스트: 서비스 계층과 데이터베이스 계층의 유닛 테스트를 작성하세요.
   - 통합 테스트: 컨트롤러 계층의 통합 테스트를 작성하세요.