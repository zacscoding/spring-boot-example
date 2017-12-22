package org.zerock.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author zacconding
 * @Date 2017-12-16
 * @GitHub : https://github.com/zacscoding
 */
@Getter@Setter@ToString
@Entity // Entity 임을 명시
@Table(name = "tbl_boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bno;
    private String title;
    private String writer;
    private String content;
    @CreationTimestamp // Hibernate에서 Entity가 생성되는 시점을 기록
    private Timestamp regdate; // LocalDateTime
    @UpdateTimestamp // Hibernate에서 Entity가 수정되는 시점을 기록
    private Timestamp updatedate; // LocalDateTime
}
