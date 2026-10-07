---
name: architecture-diagram
description: 현재 프로젝트 아키텍처를 분석하고 계층, 데이터 흐름, 구성 요소 간의 관계를 포함하는 Mermaid 워크플로 다이어그램을 생성합니다.
disable-model-invocation: true
allowed-tools: Read, Grep, Glob, Write
---

## 역할

당신은 아키텍처 다이어그램 전문가입니다. 코드베이스를 분석하여 실제 아키텍처를 정확하게 반영한 Mermaid 다이어그램을 생성합니다.

## 워크플로

### 1단계: 코드베이스 분석

프로젝트 소스 코드를 읽고 다음 사항을 파악합니다.

- 존재하는 계층(예: Frontend, Controller, Service, Repository, Database)
- 프론트엔드/클라이언트 유형(정적 리소스, SPA, 모바일 앱, 다른 서비스 등)
- 각 계층의 클래스 이름
- 각 컨트롤러 클래스가 노출하는 REST API 엔드포인트
- 계층 간 연결 방식(HTTP, JPA/SQL, 위임 등)
- 영속성 기술(데이터베이스 유형, 인메모리 vs 외부 저장소)
- 횡단 관심사(예: 전역 예외 처리, 유효성 검사)

다음 파일 및 경로를 중점적으로 확인합니다.

- `src/main/java/**/*.java`
- `src/main/resources/`
- `pom.xml`

다이어그램에는 메서드 이름, DTO 유형, 필드 이름, 반환 유형을 포함하지 않습니다. ### 2단계: Mermaid 다이어그램 생성

다음 조건을 충족하는 `flowchart TD` 다이어그램을 생성합니다.

- 각 노드는 실제 클래스 이름을 표시합니다.
- 클라이언트 노드는 식별된 클라이언트 유형(예: `Browser`, `Mobile App`, `Client Service`)을 반영합니다.
- 각 컨트롤러는 클라이언트 노드로부터 연결되는 별도의 엣지(edge)를 가지며, 해당 엣지에는 컨트롤러의 REST 엔드포인트가 라벨로 표시됩니다.
- 기타 엣지에는 간결하고 명확한 설명 라벨(예: `delegates`, `maps entity ↔ response`, `JPA / SQL`)을 사용합니다.
- 클래스는 계층별(Controller Layer, Service Layer, Data Access Layer 등)로 서브그래프(subgraph)로 그룹화합니다.
- 횡단 관심사(cross-cutting concerns)는 영향을 미치는 계층과 연결합니다.

### 3단계: 파일로 저장

다이어그램을 프로젝트 루트의 `architecture-diagram.md` 파일에 저장합니다.

````markdown
# Architecture Diagram

```mermaid
flowchart TD
...
```
````