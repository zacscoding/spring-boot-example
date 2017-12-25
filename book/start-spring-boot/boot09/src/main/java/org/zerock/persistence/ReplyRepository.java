package org.zerock.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;

import java.util.List;

/**
 * 댓글 Ropository
 *
 * 별도의 검색을 하지 않으므로, QuerydslPredicateExcuter를 사용X
 * @author zacconding
 * @Date 2017-12-23
 * @GitHub : https://github.com/zacscoding
 */
public interface ReplyRepository extends CrudRepository<WebReply, Long> {
    @Query("SELECT r FROM WebReply r WHERE r.board = ?1 AND r.rno > 0 ORDER BY r.rno ASC")
    public List<WebReply> getRepliesOfBoard(WebBoard webBoard);

}
