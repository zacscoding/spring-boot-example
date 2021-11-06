package io.spring.batch.domain;

import com.google.common.base.MoreObjects;

import lombok.Getter;

@Getter
public class CustomerContractUpdate extends CustomerUpdate {

    private final String emailAddress;
    private final String homePhone;
    private final String cellPhone;
    private final String workPhone;
    private final Integer notificationPreferences;

    public CustomerContractUpdate(long customerId, String emailAddress, String homePhone, String cellPhone,
                                  String workPhone, Integer notificationPreferences) {
        super(customerId);
        this.emailAddress = emailAddress;
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
        this.workPhone = workPhone;
        this.notificationPreferences = notificationPreferences;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("customerId", customerId)
                          .add("emailAddress", emailAddress)
                          .add("homePhone", homePhone)
                          .add("cellPhone", cellPhone)
                          .add("workPhone", workPhone)
                          .add("notificationPreferences", notificationPreferences)
                          .toString();
    }
}
