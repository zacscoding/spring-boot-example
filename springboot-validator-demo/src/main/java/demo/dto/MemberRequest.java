package demo.dto;

import demo.entity.Member;
import demo.validator.annotations.Birth;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * http://jojoldu.tistory.com/129
 */
public class MemberRequest {

    private Long id;

    @NotBlank(message = "Must be not null name")
    private String name;

    @NotBlank(message = "Must be not null phone")
    @Pattern(regexp = "[0-9]{10,11}", message = "Invalid phone format")
    private String phoneNumber;

    @NotBlank(message = "Must be not null email")
    @Email(message = "Invalid email format")
    private String email;

    @Birth // custom validator
    private String birth;

    public Member toEntity() {
        String[] parsedPhone = parsePhone();
        return new Member(name, parsedPhone[0], parsedPhone[1], parsedPhone[2], email, birth);
    }

    public String[] parsePhone() {
        String[] phones = new String[3];

        int mid = phoneNumber.length() == 10 ? 6 : 7;

        phones[0] = phoneNumber.substring(0, 3);
        phones[1] = phoneNumber.substring(3, mid);
        phones[2] = phoneNumber.substring(mid, phoneNumber.length());

        return phones;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth() {
        return birth;
    }
}