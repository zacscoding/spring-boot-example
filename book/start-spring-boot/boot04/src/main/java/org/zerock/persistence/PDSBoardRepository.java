package org.zerock.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.PDSBoard;

import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public interface PDSBoardRepository extends CrudRepository<PDSBoard, Long> {

    /**
     * 첨부 파일 수정 처리 메소드
     *
     * @Modifying을 통해 @Query에 DML사용 가능
     * "from"은 생략 가능.
     */
    @Modifying
    @Query("UPDATE FROM PDSFile f set f.pdsfile = ?2 WHERE f.fno = ?1")
    public int updatePDSFile(Long fno, String newFileName);

    /**
     * 첨부 파일 삭제 처리 메소드
     * // Hibernate: delete from tbl_pdsfiles where fno=?
     */
    @Modifying
    @Query("DELETE FROM PDSFile f where f.fno = ?1")
    public int deletePDSFile(Long fno);

    /**
     * 자료와 첨부파일의 수를 자료 번호의 역순으로 출력하는 메소드
     * p.files를 엔티티를 이용해 pdsno로 출력
     */
    @Query("SELECT p, count(f) FROM PDSBoard p LEFT OUTER JOIN p.files f " + "where p.pid > 0 GROUP BY p ORDER BY p.pid DESC")
    public List<Object[]> getSummary();

}
