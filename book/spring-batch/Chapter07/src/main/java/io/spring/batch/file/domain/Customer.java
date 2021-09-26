package io.spring.batch.file.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;

import com.google.common.base.MoreObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
public class Customer {

    /** 이름 */
    private String firstName;
    /** 가운데 이름의 첫 글자 */
    private String middleInitial;
    /** 성 */
    private String lastName;
    /** 주소(건물 번호 + 거리 이름) */
    private String address;
    /** 건물 번호 */
    private String addressNumber;
    /** 거리 이름 */
    private String street;
    /** 도시 */
    private String city;
    /** CA, TX 등 주의 두자리 약자 */
    private String state;
    /** 우편번호 */
    private String zipCode;
    private List<Transaction> transactions;

    @XmlElementWrapper(name = "transactions")
    @XmlElement(name = "transaction")
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this)
                                                       .add("firstName", firstName)
                                                       .add("middleInitial", middleInitial)
                                                       .add("lastName", lastName);

        if (StringUtils.hasText(address)) {
            helper.add("address", address);
        } else {
            helper.add("addressNumber", addressNumber)
                  .add("street", street);
        }

        return helper.add("city", city)
                     .add("state", state)
                     .add("zipCode", zipCode)
                     .add("transactions", transactions).toString();
    }
}
