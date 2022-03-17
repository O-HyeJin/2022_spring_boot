# 스프링 VS 스프링 부트: 환경 구축 비교

**과제목표**: 스프링과 스프링 부트의 개발 환경 세팅을 진행한 경험을 토대로 어떻게 다른지 확인

## 스프링의 특징
### 제어의 역전(IOC: Inversion of Control)
프로그램의 생명주기에 대한 제어권이 웹 애플리케이션 컨테이너에 있다.   
사용자가 직접 new 연산자를 통해 인스턴스를 생성하고 메서드를 호출하는 일련의 생명주기에 대한 작업들을 스프링에 위임할 수 있다.   
생성자를 사용하여 객체를 주입받는다. 즉, 외부에서 생성된 객체를 인터페이스를 통해서 넘겨받는다. \> 느슨한 결합도를 만드는 데에 도움이 된다.   
단, IOC는 직관적이지 못하기 때문에 이를 구현하는 방법으로 ``의존성 주입(DI)``이 있다.

### 의존성 주입(DI: Dependency Injection)
객체 사이에 필요한 의존 관계에 대해 스프링 컨테이너가 자동으로 연결해 주는걸 뜻한다.   
스프링 컨테이너는 DI를 이용하여 빈(Bean) 객체를 관리하며, 스프링 컨테이너에 클래스를 등록하면 스프링이 클래스의 인스턴스를 관리해준다.   
아래는 DI의 예시들이다.   
* @Component: BeanFactory 패턴의 구현체에서 Bean 객체가 있는데, 해당 클래스를 그러한 Bean 객체로 두어 스프링 관리하에 두겠다는 어노테이션   
* @AutoWired: 스프링 프레임워크에서 관리하는 Bean 객체와 같은 타입의 객체를 찾아서 자동으로 주입해준다. 해당 객체를 Bean으로 등록해야한다.   

### 관점 지향 프로그래밍(AOP: Aspect Oriented Programming)
기능을 비지니스 로직과 공통 모듈로 구분한 후에 개발자의 코드 밖에서 필요한 시점에 비지니스 로직에 삽입하여 실행되도록 한다.   
*핵심관점(비즈니스 로직) + 횡단관점(트랜잭션, 로그, 권한 체크, 인증, 예외 처리 등)으로 관심의 분리를 실현*   
따라서 핵심 기능에서 중복되는 공통적인 기능을 종단간으로 삽입할 수 있다.

## 스프링 부트의 특징
### Spring Boot Starter
스프링 설정 시에는 버전을 명시 후 버전에 맞는 설정을 했으나, 스프링 부트를 사용하면 스프링 부트에 의해서 라이브러리가 관리된다.   
때문에 종속성이나 호환 버전에 대해 신경 쓸 필요가 없다.

### Embed Tomcat
내장형 톰캣을 가지고 있기 때문에 별도의 톰캣을 설정할 필요가 없어졌다. (Spring Boot App으로 실행)   
독립적으로 실행 가능한 ``jar``로 손쉽게 배포가 가능해졌다.

### Auto Configurator
공통적으로 필요한 DispatcherServlet 등을 어노테이션을 이용하여 대신할 수 있다.   
스프링 부트의 main 메서드는 @SpringBootApplication 어노테이션을 가지고 있다.   
이는 ComponentScan + Configuration + EnableAutoConfiguration을 합친 어노테이션이다.

## 스프링과 스프링 부트 환경 구축을 하며 느낀 차이점
* 스프링에서는 root-context.xml, servlet-context.xml과 같이 pom.xml 외에도 설정할 파일이 많았는데 스프링 부트는 설정 파일이 스프링보다 적다고 느꼈다.
* 스프링 부트는 webapp \> WEB-INF \> view 디렉토리를 직접 생성했다.
* 스프링 부트에서 port 및 datasource를 application.properties 파일에서 간단하게 관리할 수 있었다.
* 스프링과 달리 스프링 부트는 내장 톰캣을 쓰기 때문에 실행 방법이 달랐다. 톰캣 설정이 필요 없었다.


<br>

## Reference
[Spring의 특징과 Spring Boot와의 차이점을 알아보자](https://msyu1207.tistory.com/entry/Spring-VS-Spring-Boot-%EC%B0%A8%EC%9D%B4%EC%A0%90%EC%9D%84-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)   
[스프링 vs 스프링 부트 차이 비교하기!](https://sas-study.tistory.com/274)   
[AOP(관점 지향 프로그래밍)](https://velog.io/@gillog/AOP%EA%B4%80%EC%A0%90-%EC%A7%80%ED%96%A5-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D)   
