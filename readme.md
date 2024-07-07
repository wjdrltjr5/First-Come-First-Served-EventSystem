# 실습으로 배우는 선착순 이벤트 시스템

## [강의주소](https://www.inflearn.com/course/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5/dashboard)


## 요구사항 정의
```javascript
선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다.
이 이벤트는 아래와 같은 조건을 만족하여야 한다.
- 선착순 100명에게만 지급되어야한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야합니다.
```


### 노드
ExecutorService -> 병렬작업(동시성) 지원해주는 자바 API
CountDownLatch -> 다른 스레드 작업이 끝날때까지 기다려주게 함