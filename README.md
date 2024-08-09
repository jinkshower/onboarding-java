## 온보딩 과제

Spring Security와 Jwt를 사용하여 토큰 기반 인증, 인가를 수행하는 백엔드 서버를 구현한다.

## 요구사항

- Junit을 이용한 테스트 작성
- Spring Security를 이용한 Filter 인가 구현
- Jwt를 이용한 토큰 발급 및 검증
- Access Token과 Refresh Token을 발급
- EC2 배포

## SwaggerUI
```
http://ec2-43-203-119-0.ap-northeast-2.compute.amazonaws.com:8080/swagger
```

## 질답

- Filter, Interceptor, AOP

filter, interceptor 모두 HTTP 요청, 응답을 가로채어 원하는 로직을 적용할 수 있다.  
Filter는 Servlet의 스펙으로 모든 요청과 응답을 조작할 수 있는 기능을 제공한다.   
Interceptor는 Spring이 제공하는 기능으로 DispatcherServlet의 요청 처리 전후 작업을 할 수 있다.  

Spring 공식문서는 보안처리에 Filter를 사용하길 권장한다. Filter는 사용자 요청시 가장 앞 단에서 요청을 검사할 수 있기 때문에   
웹 컨텍스트에서 필요하지 않은 요청을 미리 걸러 낼 수 있어 스프링 컨텍스트의 부하를 줄일 수 있을 것이다. Interceptor는 세밀한 url 매칭을 제공하기 때문에  
필요한 요청에만 적용할 수 있다. 

AOP도 Spring이 제공하는 기능으로 핵심 로직이외 부가 기능을 분리하여 메서드, 클래스에 공통으로 적용할 수 있다.  
인증과 인가처리에 AOP를 사용할 수도 있지만 대다수의 보안 처리에는 HTTP 메시지가 필요하기에 구현체로 httprequest, httpresponse를 받을 수 있는  
filter와 interceptor가 좀 더 적합하다 생각한다.  

- Spring Security란?

다양한 인증, 인가 방식을 하나로 추상화하여 사용할 수 있게 한 스프링의 인증,인가 PSA 프레임워크.
Security Filter Chain을 구현하여 Proxy를 기반으로 Servlet의 Filter Chain에 Security Filter Chain을 추가하여 사용한다.
Authentication을 ThreadLocal에 저장하여 쓰레드마다의 세션유지를 가능하게 한다.  

- Jwt란?

JSON 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token  


## EC2 배포 완료

![Pasted image 20240809131540](https://github.com/user-attachments/assets/2376bd2a-0009-4f6d-a8f0-e0fc289a054a)

## How to run

Git Clone this repository

Suppose you have docker-compose installed.

```
docker-compose up -d
```

You can check the api document by visiting `/swagger`

## Project Structure

```
├── OnboardingApplication.java
├── global
│   ├── auth
│   │   ├── AuthDTO.java
│   │   ├── AuthException.java
│   │   ├── CustomAccessDeniedHandler.java
│   │   ├── CustomAuthenticationEntryPoint.java
│   │   ├── CustomUserDetails.java
│   │   └── jwt
│   │       ├── JwtAuthFilter.java
│   │       └── JwtProvider.java
│   ├── config
│   │   ├── EmbeddedRedisConfig.java
│   │   ├── RedisConfig.java
│   │   ├── SecurityConfig.java
│   │   └── SwaggerConfig.java
│   ├── exception
│   │   ├── ErrorResponse.java
│   │   └── GlobalExceptionHandler.java
│   └── util
│       └── DataInitializer.java
└── user
    ├── application
    │   ├── TokenService.java
    │   └── UserService.java
    ├── domain
    │   ├── Authority.java
    │   ├── RefreshToken.java
    │   └── User.java
    ├── exception
    │   └── UserException.java
    ├── presentation
    │   ├── TokenRestController.java
    │   ├── UserRestController.java
    │   ├── request
    │   │   ├── ReissueTokenRequest.java
    │   │   ├── UserSignRequest.java
    │   │   └── UserSignupRequest.java
    │   └── response
    │       ├── AuthorityResponse.java
    │       ├── TokenResponse.java
    │       └── UserResponse.java
    └── repository
        ├── AuthorityRepository.java
        ├── RefreshTokenRepository.java
        └── UserRepository.java

```
