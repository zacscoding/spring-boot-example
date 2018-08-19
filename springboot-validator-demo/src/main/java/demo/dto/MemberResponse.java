package demo.dto;

import demo.entity.Member;

/**
 * http://jojoldu.tistory.com/129
 */
public class MemberResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String birth;

    public MemberResponse() {
    }

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.phoneNumber = toStringPhone(member.getPhone1(), member.getPhone2(), member.getPhone3());
        this.email = member.getEmail();
        this.birth = member.getBirth();
    }

    public String toStringPhone(String p1, String p2, String p3) {
        return p1 + "-" + p2 + "-" + p3;
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