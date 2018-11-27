package org.zerock.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Profile 엔티티
 *
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString(exclude = "member")
@EqualsAndHashCode(of = "fno")
@Entity
@Table(name = "tbl_profile")
public class Profile {

    // GenerationType.AUTO를 이용하면 hibernate_sequence라는 테이블 생성하고 번호를 유지
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    private String fname;

    private boolean current;

    // Profile - Member는 '다대일' (Member - Profile은 '일대다')
    @ManyToOne
    private Member member;
}
