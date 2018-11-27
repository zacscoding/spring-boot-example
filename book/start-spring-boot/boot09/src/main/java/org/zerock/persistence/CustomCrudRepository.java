package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.WebBoard;

/**
 * @author zacconding
 * @Date 2017-12-23
 * @GitHub : https://github.com/zacscoding
 */
public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard {

}
