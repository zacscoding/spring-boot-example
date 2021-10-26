package io.spring.batch.example6;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
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