package org.zerock.persistence;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Board;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public interface BoardRepositoryByQuerydsl extends CrudRepository<Board,Long>, QueryDslPredicateExecutor<Board> {

}
