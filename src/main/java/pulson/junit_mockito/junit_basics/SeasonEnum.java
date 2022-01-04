package pulson.junit_mockito.junit_basics;

import lombok.Getter;

@Getter
public enum SeasonEnum {
    SPRING(1), SUMMER(2), AUTUMN(3), WINTER(4);

    private final int number;

    SeasonEnum(int number) {
        this.number = number;
    }
}
