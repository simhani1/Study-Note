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

하지만 이는 즉시 로딩을 할 떄와 다른 점은 그저 조회 쿼리를 날리는 시점으 늦춰질 수 있다는 점 뿐이다. 쿼리 로그를 보면 결국 N + 1 문제가 발생했을 때와 똑같다.

그렇다면 JPQL로 Join문을 내가 직접 작성해주면 해결되지 않을까?

### join문 직접 작성하기

```java
@Query("select distinct t from Team t join t.memberList")
List<Team> findAllByJoin();
```
위와 같이 쿼리를 작성하였다.

즉 member들의 team id와 일치하는 모든 Team 엔티티를 조회하는 쿼리이다. 이렇게 조회를 한다면 Team에 소속 멤버들을 한번에 가져올 수 있을 것 같다.

하지만 테스트 결과는 다음과 같다.

![img.png](img/img_5.png)

조회하는 쿼리는 inner join으로 잘 나간듯 하다. 하지만 member를 조회하는 순간 여전히 불필요한 쿼리 두 번이 발생한다.

이유는 JPQL로 작성한 일반적인 join문은 Team의 칼럼들만 조회하고 join 대상의 영속성까지는 관여하지 않기 때문이다.

즉 내가 team의 member를 참조하기 전 까지는 여전히 지연 로딩 전략으로 인해 Team 객체는 프록시 객체인 것이다.

여전히 N + 1 문제가 발생하고 있다.

이를 해결하기 위해 fetch join 전략을 사용해 보자.

### Fetch join

```java
    @Query("select distinct t from Team t join fetch t.memberList")
    List<Team> findAllByFetchJoin();

```
위와 같이 단순 join이 아닌 fetch join으로 쿼리를 작성하였다.

fetch join의 뜻은 대상 엔티티를 한번에 불러 들이겠다는 뜻이다. 즉 Team을 조회하면서 Team에 속한 memeber들도 바로 조회시키는 것이다.

테스트 결과는 다음과 같다.

![img.png](img/img_6.png)

그냥 join문을 사용했을 때랑 차이점은 조회하는 필드에 있다. 

지금 쿼리는 Team의 모든 필드 + Member의 모든 필드를 조회하는 것을 볼 수 있다.

드디어 N + 1 문제를 해결하였다. 하지만 fetch join을 사용할 때 주의할 점이 있다.

![img.png](img/img_7.png)

실행 결과를 보면 조회되는 user의 수가 중복되고 있는 것을 볼 수 있다. 팀의 개수는 2개, 각 팀 당 멤버 수는 5명이므로 조회 시 카티션 곱이 발생하여 총 10개의 팀이 조회되는 것이다.

따라서 이런 중복을 피하기 위해서는 쿼리를 작성할 때 distinct를 반드시 넣어줘야 한다. 또는 엔티티에서 List가 아닌 중복을 제거해주는 Set을 사용하는 방법이 있다.

