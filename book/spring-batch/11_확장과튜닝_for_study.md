# 목차
- [배치 처리 프로파일링하기](#배치-처리-프로파일링하기)
  - [VisualVM 알아보기](#VisualVM-알아보기)
  - [스프링 배치 애플리케이션 프로파일링하기](#스프링-배치-애플리케이션-프로파일링하기)

---

# 배치 처리 프로파일링하기

## VisualVM 알아보기

**VisualVM 설치하기**

- (1) https://visualvm.github.io/ 에서 다운로그 하기
- (2) `$ brew install --cask visualvm`
- (3) IntelliJ

![VisualVM Main](https://user-images.githubusercontent.com/25560203/140611476-8c77eb9d-372e-401c-8795-3db4fcc379a4.png)  
(VisualVM 메인화면)

현재 로컬에서 실행중인 IntelliJ를 클릭하면 아래와 같은 `Overview`, `Monitor`, `Threads`, `Sampler`, `Profiler`를 확인할 수 있다.

- **Overview**: 

실행 중인 자바 애플리케이션의 개요를 제공한다. main 클래스, 애플리케이션 이름, 프로세스 ID, JVM Arguments 등이 표시된다.

![VisualVM - Overview](https://user-images.githubusercontent.com/25560203/140611592-c506cc59-32dc-43ea-9d9b-cd526f587fc1.png)

- **Monitor**: 

CPU 사용률, 메모리 사용률(Heap과 PermGen), 로딩된 클래스 수, 수행 중인 데몬 스레드 수를 보여주는 차트를 표시한다. GC를 수행할 수 있으며 힙 덤프도 가능한다.

![VisualVM - Monitor](https://user-images.githubusercontent.com/25560203/140611663-3efef801-14ba-4c2e-8eb1-6dc717670126.png)

- **Threads**  

애플리케이션이 실행한 모든 스레드와 해당 스레드가 어떤 작업을 하고 있는지(running, sleeping, waiting 또는 monitoring)와 관련된 정보를 표시한다.  
해당 데이터는 타임라인 형식, 테이블 형식, 세부 정보 형식으로 표시된다.

![VisualVM - Threads](https://user-images.githubusercontent.com/25560203/140611750-595656d2-0c22-4312-b3f6-5e0e38af4638.png)

- **Sampler**  

애플리케이션의 CPU 사용률과 메모리 할당 상태를 순간적으로 잡아내 스냅샷을 만들 수 있다. CPU 사용률은 어떤 메소드가 얼마나 오래 실행되는지를 보여준다.  

메모리 사용률은 어떤 클래스가 얼마나 많은 메모리를 사용하는지 보여준다.

![VisualVM - Sampler](https://user-images.githubusercontent.com/25560203/140611813-95f5ee5c-0d24-4bca-b3b3-83b458811355.png)

---  

## 스프링 배치 애플리케이션 프로파일링하기

애플리케이션에서 프로파일링할 때는 일반적으로 두가지 중 하나를 살펴본다.

- 어떤 부분에서 얼마나 많은 CPU를 사용하는가?
  - CPU가 어떤 작업을 하는가와 연관돼 있다
  - 잡이 어려운 계산을 수행하는가?
  - CPU가 비즈니스 로직이 아닌 다른 곳에 많은 노력을 들이고 있는가?(e.g: 실제 계산 수행보다 파일을 파싱하는데 많은 시간을 소비하는지?)
- 무엇 때문에 얼마나 많은 메모리가 사용되는가?
  - 가용 메모리를 거의 다 소모했는가?
  - 무엇이 메모리를 가득 차지하고 있는가?
  - 컬렉션을 지연 로딩하지 않은 하이버네이트 객체로 인해 메모리가 가득차게 됐는가?


### CPU 프로파일링











