package io.spring.batch.domain;

import com.google.common.base.MoreObjects;

import lombok.Getter;

@Getter
public class CustomerAddressUpdate extends CustomerUpdate {

    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final String postalCode;

    public CustomerAddressUpdate(long customerId, String address1, String address2, String city, String state,
                                 String postalCode) {
        super(customerId);
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("customerId", customerId)
                          .add("address1", address1)
                          .add("address2", address2)
                          .add("city", city)
                          .add("state", state)
                          .add("postalCode", postalCode)
                          .toString();
    }
}
