package io.spring.batch.database.example4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class Customer {

    @Id
    private Long id;

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "middleInitial")
    private String middleInitial;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zipCode")
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
