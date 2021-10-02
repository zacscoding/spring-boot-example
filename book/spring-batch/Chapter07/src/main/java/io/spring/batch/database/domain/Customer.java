package io.spring.batch.database.domain;

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

    private Long id;

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("firstName", firstName)
                          .add("middleInitial", middleInitial)
                          .add("lastName", lastName)
                          .add("address", address)
                          .add("city", city)
                          .add("state", state)
                          .add("zipCode", zipCode)
                          .toString();
    }
}
