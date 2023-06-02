# N + 1 문제

## 엔티티 설계

### 즉시 로딩
```java
// Team

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    @Column(name = "teamId")
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<Member> memberList = new ArrayList<>();

    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }
}
```

```java
// Member
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamId", nullable = false)
    private Team team;

    @Builder
    public Member(String name, Team team) {
        this.name = name;
        this.team = team;
        team.getMemberList().add(this);
    }

}
```

#### Test Code
```java
    @Test
    public void 지연로딩_즉시로딩_비교() throws Exception {
        // given
        Team team = Team.builder()
                .teamName("A")
                .build();

        // when
        Team savedTeam = teamRepository.save(team);
        IntStream.range(0, 5)
                .mapToObj(i -> Member.builder()
                        .name("user" + i)
                        .team(savedTeam)
                        .build()).forEach(member -> memberRepository.save(member));
        flushAndClear();

        // then
        log.info("\n=============== findAll() =================");
        List<Team> teamList = teamRepository.findAll();
        flushAndClear();
        log.info("\n=============== findById() =================");
        Team findTeam = teamRepository.findById(savedTeam.getId())
                .orElseThrow(RuntimeException::new);
        log.info("\n=============== 지연로딩 시 user0을 조회하기 위한 쿼리 발생 =================");
        findTeam.getMemberList().get(0);
    }
```
- N + 1 문제 발생

즉시 로딩으로 설정이 되어 있으므로 `teamRepository.findAll()` 호출 시 저장 된 모든 팀 엔티티가 조회된다.

하지만 이때 각 팀은 Member를 리스트로 저장하고 있기 떄문에 Member에 대한 모든 정보도 불러오기 위하여 다음과 같이 쿼리가 두 번 발생한다.

![img.png](img/img_1.png)

쿼리를 살펴보면 team을 조회하는 쿼리가 발생하고 이후에 member를 조회하는 쿼리가 추가적으로 발생하는 것을 볼 수 있다.

이는 즉시 로딩 설정에 의해 내가 원하지 않는 쿼리가 추가적으로 발생하는 것이다.

추가적으로 즉시 로딩을 하기 때문에 내가 조회한 팀에서 첫 번째 Member를 참조하더라도 새롭게 쿼리가 발생하지 않는 것을 확인할 수 있다.

만약 저장된 팀의 개수가 2개라면, 각 팀마다 속한 member를 조회하기 위한 쿼리가 두번 발생할 것이다.

따라서 `팀의 목록을 조회하는 쿼리` + `팀의 수` * `팀에 속한 member를 조회하는 쿼리` 만큼 쿼리가 나가게 되고 이를 `N + 1`문제라고 한다.

> 아래 결과는 위 테스트 코드에서 Team을 두 개 저장하고 각 팀마다 Member를 5명씩 저장했을 때 발생하는 쿼리다. Member 테이블을 조회하는 쿼리가 예상대로 두 번 발생하는 것을 확인할 수 있다.

![img.png](img/img_2.png)

### 지연 로딩

```java
// Team

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    @Column(name = "teamId")
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member> memberList = new ArrayList<>();

    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }
}
```

```java
// Member
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", nullable = false)
    private Team team;

    @Builder
    public Member(String name, Team team) {
        this.name = name;
        this.team = team;
        team.getMemberList().add(this);
    }

}
```

지연 로딩으로 설정을 한 후 동일한 테스트를 돌려보면 결과는 다음과 같다.

![img.png](img/img_3.png)

즉시 로딩을 할 때와는 다르게 findAll(), findById()를 호출할 때 내가 원하는 Team 엔티티 정보만 조회하는 것을 확인할 수 있다.

따라서 불필요한 쿼리가 발생하지 않는다.

또한 지연 로딩을 사용하면 프록시 객체가 반환되기 때문에 team의 memberList를 참조하는 순간에 쿼리가 발생한다.

`N + 1` 문제가 해결된 것처럼 보인다. 하지만 실제로는 그렇지 않다. 다음 예제를 통해 여전히 문제가 남아있음을 확인해 보자.

### Test Code
```java
    @Test
    public void 지연로딩_Team이_여러_개_저장된_경우() throws Exception {
        // given
        Team team1 = Team.builder()
                .teamName("A")
                .build();
        Team team2 = Team.builder()
                .teamName("B")
                .build();

        // when
        Team savedTeam1 = teamRepository.save(team1);
        Team savedTeam2 = teamRepository.save(team2);
        IntStream.range(0, 5)
                .mapToObj(i -> Member.builder()
                        .name("user" + i)
                        .team(savedTeam1)
                        .build()).forEach(member -> memberRepository.save(member));
        IntStream.range(0, 5)
                .mapToObj(i -> Member.builder()
                        .name("user" + i)
                        .team(savedTeam2)
                        .build()).forEach(member -> memberRepository.save(member));
        flushAndClear();

        // then
        log.info("\n=============== findAll() =================");
        List<Team> teamList = teamRepository.findAll();
        log.info("\n=============== 프록시 객체의 user들을 조회하기 위한 쿼리 발생 =================");
        teamList.forEach(
                t -> {
                    t.getMemberList()
                            .forEach(
                                    member -> System.out.println(member.getName())
                            );
                }
        );
    }
```

![img.png](img/img_4.png)

실행 결과를 보면 나중에 각 팀마다 소속된 팀원들의 이름을 출력하기 위해 member를 조회하는 쿼리가 두 번 발생하는 것을 볼 수 있다.

내가 member에 대한 정보를 조회하는 시점에 쿼리가 발생한다는 점에서 즉시 로딩을 하는 경우보다 성능 상 이점이 존재한다.

