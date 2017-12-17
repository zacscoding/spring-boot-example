package org.zerock.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.FreeBoard;

import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long> {

    public List<FreeBoard> findByBnoGreaterThan(Long bno, Pageable page);

    /**
     * @Query와 Fetch Join을 이용한 처리
     * List :: 행(row) // Object[] :: 열(Column)
     */
    @Query("SELECT b.bno, b.title, count(r) FROM FreeBoard b LEFT OUTER JOIN b.replies r "
            + " WHERE b.bno > 0 GROUP BY b ")
    public List<Object[]> getPage(Pageable page);


}
