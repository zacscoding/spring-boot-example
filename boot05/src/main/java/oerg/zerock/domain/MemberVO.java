package oerg.zerock.domain;

import lombok.*;

import java.sql.Timestamp;

/**
 * @author zacconding
 * @Date ${date}
 * @GitHub : https://github.com/zacscoding
 */
@Data@AllArgsConstructor
public class MemberVO {
    private int mno;
    private String mid;
    private String mpw;
    private String mname;
    private Timestamp regdate;
}
