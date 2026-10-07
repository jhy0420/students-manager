---
name: version
description: 특정 도구의 설치된 버전을 확인
arguments: [tool]
argument-hint: "[tool-name]"
allowed-tools: Bash(* --version)
---

`$tool --version`을 실행하고 아래 형식을 사용하여 결과를 보고하세요.

```
🛠️ $tool version: {version}
```