package demo.dto;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Test;

/**
 * Test validation of MemberRequest
 */
public class MemberRequestValidateTests {

    @Test
    public void temp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        MemberRequest temp = MemberRequest.builder()
            .name(null)
            .email("zacscoding@github.com")
            .phoneNumber("1095122222")
            .birth("190531")
            .build();

        Set<ConstraintViolation<MemberRequest>> resultSet = validator.validate(temp);
        System.out.println(
            resultSet.size()
        );
    }

    private MemberRequest createValidMemberRequest() {
        return MemberRequest.builder()
            .name("zacscoding")
            .email("zacscoding@github.com")
            .phoneNumber("1095122222")
            .birth("190531")
            .build();
    }
}
