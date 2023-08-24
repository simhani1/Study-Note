## 데이터 중심 설계

객체 내부에 저장되는 데이터를 기반으로 시스템을 만들어 나가는 방식이다. 객체가 저장해야 하는 데이터가 무엇인가를 생각하며 설계를 진행시켜 나가는 방식이다.
아래 영화 예매 서비스 로직을 보자.

```java
public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Movie movie = screening.getMovie();

        boolean discountable = false;
        for (DiscountCondition condition : movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                        condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                        condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }

        Money fee;
        if (discountable) {
            Money discountAmount = Money.ZERO;
            switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }

            fee = movie.getFee().minus(discountAmount);
        } else {
            fee = movie.getFee();
        }

        return new Reservation(customer, screening, fee, audienceCount);
    }
}
```

`Reservation` 객체를 생성하기 위해 할인 여부를 판단하고 할인이 가능한 경우 적절한 할인 조건을 찾아 영화의 금액을 계산하는 로직이 하나의 메서드에 작성되어있다.
위 코드는 변경에 취약하고 캡슐화가 되어있지 않았다. 그리고 거의 대부분의 데이터에 의존하고 있다. Screening, Movie, DiscountCondition 등 모든 데이터를 참조하고 있고 메서드를 사용하고 있다.
그 중 어느 하나의 클래스에 변경이 생긴다면 ReservationAgency 클래스에는 변경이 필연적으로 발생할 수 밖에 없다. 너무 높은 결합도로 인해 발생한 문제이다.

```java
        Movie movie = screening.getMovie();

        boolean discountable = false;
        for (DiscountCondition condition : movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                        condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                        condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }
```

위 코드에서 알 수 있는 사실로는 `DiscountCondition` 클래스 내부에 어떤 필드들이 존재하는지 get 메서드를 통해 파악할 수 있다는 점이다.
이는 내부 구현이 퍼블릭 인터페이스에 노출되어 있음을 의미한다. 또한 할인 조건의 타입을 `ReservationAgency` 객체가 알고 있어야 하는지도 의문이다.
만약 할인 조건이 바뀐다면 위 코드의 조건문도 변경되어야 할 것이다.

```java
        Money fee;
        if (discountable) {
            Money discountAmount = Money.ZERO;
            switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }

            fee = movie.getFee().minus(discountAmount);
        } else {
            fee = movie.getFee();
        }
```

위 코드는 할인이 가능하다면 영화의 타입에 따라 적절한 할인 정책에 따라 가격에 할인이 되는 로직이다.
여기서도 `Movie` 클래스 내부에는 `fee`라는 필드가 존재함을 알 수 있다. 마찬가지로 이는 캡슐화가 제대로 되어 있지 않음을 의미한다. 만약 fee가 아닌 다른 변수를 사용한다면? `getFee()` 메서드를 사용하는 모든 클래스의 코드에는 수정이 불가피하다.
`getFee()` 메서드 하나로 인해 Movie 클래스 내부를 마음대로 변경하기 부담스러운 상황이다.
또한 변경에 매우 취약하다.
할인 정책은 변경 가능성이 매우 높다. 그러나 위 코드에서는 할인 조건에 따라 다른 할인을 적용하기 위해 조건문을 사용하고 있다.
할인 조건을 추가한다면 우리는 `ReservationAgency` 클래스의 코드를 반드시 수정할 수 밖에 없다.
즉 변경이 힘든 코드로 인해 할인정책을 마음대로 추가하거나 삭제할 수 없는 상황이다.

이 모든 문제점들은 캠슐화가 제대로 안되어 있기 때문이고, 그로 인해 응집도는 낮아지고 결합도는 높은 상태가 된 것이다.
 
지금부터는 캡슐화 원칙을 지키며 변경에 용이하도록 코드를 변경해 볼 것이다.

## 책임 주도 설계

위에서 본 ReservationAgency 클래스는 너무 높은 결합도로 인해 변경에 취약한 상태이다. 책임을 적절하게 분산시켜 결합도를 낮추고 캡슐화를 지키는 코드로 변경할 것이다.

### 👉 캡슐화를 지켜라

속성을 private으로 설정하였지만 접근자, 수정자를 통해 외부로 속성을 제공한다면 캡슐화를 위반한 것이다.

### 👉 스스로 자신의 데이터를 책임지는 객체

- DiscountCondition

순번 할인을 적용하기 위해서는 sequence, 기간 할인을 적용하기 위해서는 dayOfWeek, time값이 필요하다.
이 값들은 모두 DiscountCondition 객체가 가지고 있는 데이터이다.
따라서 할인 조건을 만족하는지 검사하는 메서드를 DiscountCondition 클래스로 이동시키자.

```java
    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
        if (type != DiscountConditionType.PERIOD) {
            throw new IllegalArgumentException();
        }

        return this.dayOfWeek.equals(dayOfWeek) &&
                this.startTime.compareTo(time) <= 0 &&
                this.endTime.compareTo(time) >= 0;
    }

    public boolean isDiscountable(int sequence) {
        if (type != DiscountConditionType.SEQUENCE) {
            throw new IllegalArgumentException();
        }

        return this.sequence == sequence;
    }
```


