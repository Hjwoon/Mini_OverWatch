# Mini_OverWatch
2학년 동계 방학 개인프로젝트
<br>
- Java 언어 사용
- bullet game 형식의 게임
- XML 파일을 읽어들여 게임을 플레이하는 Player와 나만의 게임을 제작하는 저작도구(Author)로 구성
<br><br>

<hr>
<br><br>

1. 저작도구 프로그램
> 저작도구에서 사용자가 원하는 player, enemy, obstacle, block, coin, background BGM을 설정할 수 있음. 
<br><br>

저작 도구 초기화면 [AuthorFrame]
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/f66a4756-769c-4554-b5be-3e22f9c94949)

- 모든 블록, 플레이어, 적, 장애물, 배경음을 설정한 화면
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/3116e79d-c83a-4edf-ba32-fb1716cc9f63)
  - 블록의 속성창 또는 드래그 이용해 위치 변경 가능

- 게임 프레임의 크기, 게임 XML 파일 이름 설정
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/e5f2c2d3-bb3c-43b5-852e-ad71e4f9349d)
- 디렉터리에 해당 이름의 XML 파일 자동 생성. 브라우저로 열었을 때 다음과 같이 구성됨
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/ad0065af-405c-41ea-b173-4ce8807d0f47)
<br><br>


<hr>
<br><br>

2. 플레이어

> 저작도구로 만든 게임을 직접 플레이
> 초기화면
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/aafe849d-9b5f-41f8-899d-91ef755f7f2c)

> 게임 플레이 화면
![image](https://github.com/Hjwoon/MyOverWatch/assets/100463930/3c70664b-553e-4f1e-8f44-32075f6bb2eb)
- 플레이어를 W,A,D 키를 이용해 이동시키고, 스페이스바를 누르면 총알이 발사된다.
- 적 플레이어를 맞춰 체력을 모두 감소시키거나 적의 총알에 맞아 플레이어의 체력이 모두 감소하면 게임이 종료된다.
- 저작도구에서 설정한 코인의 생명을 설정한 만큼 코인 점수가 증가, 힐팩을 먹으면 체력 회복, 폭탄은 체력이 감소한다.
- 장애물은 계속 반복해서 나타나며 총알은 장애물을 뚫지 못한다. 
