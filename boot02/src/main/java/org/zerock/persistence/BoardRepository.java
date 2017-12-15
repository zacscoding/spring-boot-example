package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Board;

/**
 * @author zacconding
 * @Date 2017-12-16
 * @GitHub : https://github.com/zacscoding
 */
public interface BoardRepository extends CrudRepository<Board,Long> {

}
