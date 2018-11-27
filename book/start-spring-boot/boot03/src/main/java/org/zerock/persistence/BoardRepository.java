package org.zerock.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Board;

import java.util.Collection;
import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
// find..By // read..By // query..By // get..By // count..By
// 'find' + 'Entity (지정하지 않으면 Repository의 T 타입) + 'By' + '컬럼 이름'
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
public interface BoardRepository extends CrudRepository<Board, Long> {

    // =========================================
    // 기본 쿼리 메소드 이용
    // =========================================
    public List<Board> findBoardByTitle(String title);

    public Collection<Board> findByWriter(String writer);

    // =========================================
    // LIKE 처리
    // =========================================
    // 단순 like == Like // 키워드% == StartingWith // %키워드 == EndingWith // %키워드% == Containing
    // where board0_.writer like %writer%
    public Collection<Board> findByWriterContaining(String writer);

    // =========================================
    // and || or 조건 처리
    // =========================================
    // where title like %title% or content like %content%
    public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);

    // =========================================
    // 부등호 처리
    // =========================================
    // where (board0_.title like ?) and board0_.bno>?
    public Collection<Board> findByTitleContainingAndBnoGreaterThan(String title, Long bno);

    // =========================================
    // Order by 처리
    // 'OrderBy' + 속성 'Asc or Desc'
    // =========================================
    // where board0_.bno>? order by board0_.bno desc
    public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);

    // =========================================
    // 페이징 처리
    // Pageable 인터페이스(new PageRequest :: boot 2.0 이하 // PageRequest.of :: boot 2.0 이상
    // 리턴 타입 : List<T> , Page<T>
    // =========================================
    // where board0_.bno>? order by board0_.bno desc, board0_.bno desc limit ?
    // public List<Board> findByBnoGreaterThan(Long bno, Pageable pageable);

    // =========================================
    // Page<T> 타입
    // =========================================
    // ... where board0_.bno>? order by board0_.bno asc limit ?
    // select count(board0_.bno) as col_0_0_ from tbl_boards board0_ where board0_.bno>?
    public Page<Board> findByBnoGreaterThan(Long bno, Pageable pageable);
}
