package org.zerock.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.QWebBoard;
import org.zerock.domain.WebBoard;

/**
 * @author zacconding
 * @Date : 2017-12-20
 * @GitHub : https://github.com/zacscoding
 */
public interface WebBoardRepository extends CrudRepository<WebBoard, Long> ,
                                        QuerydslPredicateExecutor<WebBoard> {

    public default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QWebBoard board = QWebBoard.webBoard;

        // check type
        if(type == null) {
            return builder;
        }

        switch(type) {
            case "t" :
                builder.and(board.title.like("%" + keyword + "%"));
                break;
            case "c" :
                builder.and(board.content.like("%" + keyword + "%"));
                break;
            case "w" :
                builder.and(board.writer.like("%" + keyword + "%"));
                break;
        }

        // bno > 0
        builder.and(board.bno.gt(0));

        return builder;
    }



}
