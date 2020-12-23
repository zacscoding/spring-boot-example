package demo.sample;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 10)
    private String name;

    @PositiveOrZero
    private int age;

    private List<String> hobbies;
}
