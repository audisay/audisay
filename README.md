# Audisay
---


![logo](https://github.com/user-attachments/assets/e90fadca-2249-43b6-ad62-2f22a99dc3cf){: width="120" height="120"}


Audisay는 시각장애인을 위한 AI 기반 접근성 도서 지원 서비스입니다. 

우리 서비스는 시각장애인이 쉽게 접근할 수 있는 도서 자료를 자동으로 제작하고, 사용자 친화적인 인터페이스를 제공하여 시각장애인 독서의 불편함을 해소하고자 합니다. 


|   이예림          | 강동완          | 강상찬          | 김아름          | 안치숙          | 이지혜          |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |
| [이미지]     | [이미지]     | [이미지]     | [이미지]     | [이미지]     | [이미지]     |
| 팀장 & Backend  | Frontend         | Frontend         | Backend         | Backend          | Backend          |
|  [프로필 링크] | [프로필 링크] | [프로필 링크] | [프로필 링크] | [프로필 링크] | [프로필 링크] |


---

## 목차

---

#### [1. 프로젝트 소개](#1.-프로젝트-소개)


- [🔍 프로젝트 개요](#🔍-프로젝트-개요)

- [🏹 서비스 주제 및 목표](#🏹-서비스-주제-및-목표)

- [🎬 기획 배경](#🎬-기획-배경)

- [🎁 기대 효과 ](#🎁-기대-효과)



#### [2. 프로젝트 정보](#2.-프로젝트-정보)

- [🏡 개발 스택](#🏡-개발-스택)

- [🧩 아키텍처](#🧩-아키텍처)

- [🎨 API 명세서](#🎨-API-명세서)

- [📚 ERD 설계](#📚-ERD-설계)

- [📝 컨벤션](#📝-컨벤션)


#### [3. 프로젝트 결과물](#3.-프로젝트-결과물)

- [🔮 주요 기능](#🔮-주요-기능)

- [🥰 팀원 소개](#🥰-팀원-소개)

- [📽️ UCC](#📽️-UCC)

---

## 1. 프로젝트 소개 

### 🔍 프로젝트 개요

삼성 청년 SW 아카데미(SSAFY) 자율 프로젝트 

주제: 시각장애인을 위한 AI 기반 접근성 도서 지원 서비스 

진행 기간: `2024-10-14 ~ 2024-11-19 (약 5주)`

팀 명 / 진행 인원: 팔자좋조 / 6인 

---

### 🏹 서비스 주제 및 목표

시각장애인을 위한 AI 기반 접근성 도서 지원 서비스

✨ **우리의 목표**
> AI 기술을 활용하여 시각장애인을 위한 대체자료를 자동으로 제작하고, 이를 기반으로 접근성 중심의 독서 앱을 개발하는 것

✨ **Audisay** 
> Audio와 Say를 결합했습니다. TTS 기반 도서 뷰어를 제공하는 우리 서비스를 나타내고자 했습니다. 

---

### 🎬 기획 배경

현재 시각장애인을 위한 접근성 도서의 수는 부족하고 도서 제작 과정도 수작업으로 진행되고 있기 때문에, 이 과정을 자동화하고자 합니다.  

![image](https://github.com/user-attachments/assets/f02127e4-dc1f-4ee9-be27-47475834b6da)

![image](https://github.com/user-attachments/assets/ff3b2856-4e45-4a75-bfaa-8da4e89b911d)

---

### 🎁 기대 효과 

![image](https://github.com/user-attachments/assets/f6dd7eb7-85af-459b-8963-e0f4d384fc17)

---

## 2. 프로젝트 정보 

---

### 🏡 개발 스택 

#### Back-End

<details>
<summary>자세히 보기</summary>

| 항목              | 버전 및 상세 정보                                    |
|-------------------|---------------------------------------------------|
| **Java**          | Liberica JDK 21.0.5+11 2024-10-15 LTS (BellSoft)  |
| **Spring Boot**   | 3.3.5                                             |
| **MySQL**         | 8.0.32                                           |
| **MongoDB**       | 8.0.3                                            |
| **Redis**         | 7.4.1                                            |
| **ElasticSearch** | 8.15.3                                           |
| **Logstash**      | 8.15.3                                           |
| **Kibana**        | 8.15.3                                           |
| **JPA (Hibernate)** | 6.5.3.Final                                    |
| **QueryDSL**      | 5.0.0                                            |
| **Swagger**       | 2.2.0                                            |
| **FastAPI**       | 0.115.4                                          |
| **Django**        | 5.1.2                                            |
| **YOLO**          | DocLayout-YOLO                                   |
| **IntelliJ IDEA** | IDE IntelliJ IDEA 2024.1.4                       |


</details>

#### Front-End

<details>
<summary>자세히 보기</summary>

| 항목                  | 버전 및 상세 정보         |
|-----------------------|--------------------------|
| **React-Native React**| 18.3.1                  |
| **Node.js**           | >=18                    |
| **TypeScript**        | 5.0.4                   |
| **Android Build Tools**| 35.0.0                |
| **Min SDK**           | 24                      |
| **Compile SDK**       | 35                      |
| **Target SDK**        | 34                      |
| **NDK**               | 26.1.10909125           |
| **Visual Studio Code**| 1.92.2                  |


</details>

#### Infra, CI/CD 

<details>
<summary>자세히 보기</summary>

| 항목       | 버전 및 상세 정보                                     |
|------------|-----------------------------------------------------|
| **AWS EC2**| Ubuntu 20.04.6 LTS (GNU/Linux 5.15.0-1063-aws x86_64) |
| **Docker** | 27.2.0                                              |
| **Jenkins**| 2.475                                               |
| **Nginx**  | 1.27.2                                              |


</details>

#### 협업 툴 
 
<details>
<summary>자세히 보기</summary>

| 항목            | 사용 도구                   |
|-----------------|---------------------------|
| **이슈 관리**    | JIRA                      |
| **형상 관리**    | GitLab                    |
| **커뮤니케이션** | Notion, Mattermost        |
| **디자인**       | Figma                     |
| **UCC 제작**     | 모바비, 타임캐스트         |
| **CI/CD**       | Jenkins                   |
| **일정 관리**    | 구글 스프레드시트 (간트차트) |


</details>


---

### 🧩 아키텍처

![아키텍처](https://github.com/user-attachments/assets/c42f1595-a6fc-4e69-98ab-d4c3e3c52e6e)

---

### 🎨 API 명세서

#### 회원 관리

![image](https://github.com/user-attachments/assets/b87670c7-3b95-40df-8ea7-25673fb718df)

#### 출판도서 관리 

![image](https://github.com/user-attachments/assets/e5bd2d0f-09cf-4301-b7d9-4a6d964fbe3c)

#### 도서 등록 

![image](https://github.com/user-attachments/assets/3708925c-4182-4148-8c5a-70a28e24906b)

#### 리뷰 관리

![image](https://github.com/user-attachments/assets/8c1268dd-4653-4ac1-96a3-f37f33927db0)

#### 독서노트

![image](https://github.com/user-attachments/assets/0cf06aa4-55c0-457e-9c57-68e1c7457c24)


--- 

### 📚 ERD 설계 

#### MySQL

![image](https://github.com/user-attachments/assets/85181621-3be6-4c0f-a1ce-40cd4c84a260)

#### MongoDB

![image](https://github.com/user-attachments/assets/b2ef25d0-a42e-457a-aaa0-41208373fa2e)

---

### 📝 컨벤션 

작성중... (mr / mattermost 자동화 언급)

---

## 3. 프로젝트 결과물 

---

### 🔮 주요 기능 

#### 도서 등록

![image](https://github.com/user-attachments/assets/4f4f3fb2-b97c-43a4-80db-c44a72fa0674)

#### 도서 뷰어

![image](https://github.com/user-attachments/assets/72cdf3ad-fc88-416b-9bbd-86ee586b098f)

#### 도서 검색

![image](https://github.com/user-attachments/assets/b8ef11d9-cd5b-40d6-b3c2-28a317e3a5c3)

#### 리뷰, 도서 담기, 좋아요 

![image](https://github.com/user-attachments/assets/00334938-2c5d-4288-875d-b67b345ce8cf)

#### 도서 추천

![image](https://github.com/user-attachments/assets/3c587400-015a-4555-b631-538d7bb994df)

--- 

### 🥰 팀원 소개 

![image](https://github.com/user-attachments/assets/5b331d7f-9d1d-4415-8530-be21b9a346bd)

![image](https://github.com/user-attachments/assets/91b34aa7-331a-4d0a-87c5-4e989f474e64)

--- 

### 📽️ UCC

전체 UCC : https://youtu.be/8yqzWUyeM1c

talkback 영상 : https://youtu.be/oGoeeEfu8DU 


---



---
