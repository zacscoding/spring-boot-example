package org.zerock.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.zerock.domain.Board;

import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
// =========================================
// @Query 이용
// => 프로젝트가 로딩될 때 쿼리를 검증 함 (오류가 나면 시작X)
// =========================================
public interface BoardRepositoryByQuery extends CrudRepository<Board, Long> {

    // =========================================
    // @Query 이용 (JPQL 객체 쿼리)
    // table -> Board // ?1 :: 첫번째 param
    // =========================================
    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Board> findByTitle(String title);

    // =========================================
    // @Param 이용
    // =========================================
    @Query("SELECT b FROM Board b WHERE b.content LIKE %:content% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Board> findByContent(@Param("content") String content);

    // =========================================
    // #{#entityName} 이용
    // Repository 인터페이스의 엔티티 타입을 이용
    // =========================================
    @Query("SELECT b FROM #{#entityName} b WHERE b.writer LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Board> findByWriter(String writer);

    // =========================================
    // 필요 한 쿼리만 추출
    // =========================================
    @Query("SELECT b.bno, b.title, b.writer, b.regdate FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    public List<Object[]> findByTitle2(String title);

    // =========================================
    // nativeQuery 사용
    // =========================================
    //@Query(value = "SELECT bno, title, writer FROM #{#entityName} b WHERE b.title LIKE CONCAT('%',?1,'%') AND b.bno > 0 ORDER BY b.bno DESC", nativeQuery = true) // error -> entityName, Board 둘다 안됨
    @Query(value = "SELECT bno, title, writer FROM tbl_boards WHERE title LIKE CONCAT('%',?1,'%') AND bno > 0 ORDER BY bno DESC", nativeQuery = true)
    public List<Object[]> findByTitle3(String title);

    // =========================================
    // Paging 처리 / 정렬
    // 메소드명 뒤에 ByPage는 상관X
    // =========================================
    // where board0_.bno>0 order by board0_.bno DESC limit ?
    @Query("SELECT b FROM Board b WHERE b.bno > 0 ORDER BY b.bno DESC")
    public List<Board> findByPage(Pageable pageable);

}
