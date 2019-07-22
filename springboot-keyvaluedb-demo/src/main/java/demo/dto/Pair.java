package demo.dto;

import lombok.Getter;
import lombok.ToString;

/**
 *
 */
@Getter
@ToString
public class Pair<F, S> {

    F first;
    S second;

    public static <F, S> Pair<F, S> newInstance(F first, S second) {
        return new Pair<>(first, second);
    }

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
