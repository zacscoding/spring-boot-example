package org.zerock.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 게시글 엔티티
 *
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString(exclude = "files")
@Entity
@Table(name = "tbl_pds")
@EqualsAndHashCode(of = "pid")
public class PDSBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String pname;
    private String pwriter;


    @OneToMany(cascade = CascadeType.ALL) // '일대다' && 모든 변경에 대한 전이
    @JoinColumn(name = "pdsno") // 관계 테이블을 생성하지 않고, 타 테이블을 참조 -> toString도 변경 해주기
    private List<PDSFile> files;
}
