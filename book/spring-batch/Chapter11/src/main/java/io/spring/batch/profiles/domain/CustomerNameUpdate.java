package io.spring.batch.profiles.domain;

import com.google.common.base.MoreObjects;

import lombok.Getter;

@Getter
public class CustomerNameUpdate extends CustomerUpdate {

    private final String firstName;
    private final String middleName;
    private final String lastName;

    public CustomerNameUpdate(long customerId, String firstName, String middleName, String lastName) {
        super(customerId);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("customerId", customerId)
                          .add("firstName", firstName)
                          .add("middleName", middleName)
                          .add("lastName", lastName)
                          .toString();
    }
}
