package io.spring.batch.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @NotNull(message = "First name is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must be alphabetical")
    private String firstName;

    // size와 pattern을 별도로 지정하여 에러 메시지를 구분하고 디버깅이 유용해진다.
    @Size(min = 1, max = 1)
    @Pattern(regexp = "[a-zA-Z]", message = "Middle initial must be alphabetical")
    private String middleInitial;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must be alphabeticla")
    private String lastName;

    @NotNull(message = "Address is required")
    @Pattern(regexp = "[0-9a-zA-Z\\. ]+")
    private String address;

    @NotNull(message = "City is required")
    @Pattern(regexp = "[a-zA-Z\\. ]+")
    private String city;

    @NotNull(message = "State is required")
    @Size(min = 2, max = 2)
    @Pattern(regexp = "[A-Z]{2}")
    private String state;

    @NotNull(message = "Zip is required")
    @Size(min = 5, max = 5)
    @Pattern(regexp = "\\d{5}")
    private String zip;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("firstName", firstName)
                          .add("middleInitial", middleInitial)
                          .add("lastName", lastName)
                          .add("address", address)
                          .add("city", city)
                          .add("state", state)
                          .add("zip", zip)
                          .toString();
    }
}