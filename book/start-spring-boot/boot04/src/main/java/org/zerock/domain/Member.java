package org.zerock.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 멤버 엔티티
 *
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@Getter @Setter @ToString @EqualsAndHashCode(of="uid")
@Entity
@Table(name="tbl_members")
public class Member {
    @Id
    private String uid;
    private String upw;
    private String uname;
}