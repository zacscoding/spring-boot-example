package org.zerock.persistence;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.PDSBoard;
import org.zerock.domain.PDSFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class PDSBoardTests {
    @Autowired
    PDSBoardRepository pdsBoardRepository;

    @Test
    public void insertPDS() {
        PDSBoard pds = new PDSBoard();
        pds.setPname("DOCUMENT 1 - 2");

        PDSFile file1 = new PDSFile();
        file1.setPdsfile("file1.doc");

        PDSFile file2 = new PDSFile();
        file2.setPdsfile("file2.doc");

        pds.setFiles(Arrays.asList(file1,file2));

        log.info("## try to save PDS : " + pds);

        pdsBoardRepository.save(pds);
    }

    // INFO 6708 --- [           main] o.s.t.c.transaction.TransactionContext   : Began transaction (1) for test context [DefaultTestContext@3b69e7d1 testClass = PDSBoardTests, testInstance = org.zerock.persistence.PDSBoardTests@1ccc1026, testMethod = updateFileName@PDSBoardTests, testException = [null], mergedContextConfiguration = [WebMergedContextConfiguration@815b41f testClass = PDSBoardTests, locations = '{}', classes = '{class org.zerock.Boot04Application}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.SpringBootTestContextCustomizer@5ed828d, org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@1e7c7811, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@a38d7a3, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@c81cdd1], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true]]; transaction manager [org.springframework.orm.jpa.JpaTransactionManager@1491cd6c]; rollback [true]
    // Hibernate: update tbl_pdsfiles set pdsfile=? where fno=?
    @Test
    @Transactional
    public void updateFileName() {
        Long fno = 1L;
        String newName = "updatedFile1.doc";

        int applied = pdsBoardRepository.updatePDSFile(fno,newName);
        log.info("# updated count : " + applied);
    }

    @Test
    @Transactional
    public void updateFileName2() {
        String newName = "updatedFile2.doc";

        // 반드시 번호가 존재하는지 확인
        Optional<PDSBoard> result = pdsBoardRepository.findById(2L);
        result.ifPresent(pds -> {
            log.info("# exist PDSBoard id : 2L");
            PDSFile target = new PDSFile();
            target.setFno(2L);
            target.setPdsfile(newName);

            int idx = pds.getFiles().indexOf(target);
            if(idx > -1) {
                List<PDSFile> list = pds.getFiles();
                list.remove(idx);
                list.add(target);
            }

            pdsBoardRepository.save(pds);
        });
    }

    @Transactional
    @Test
    public void deletePDSFile() {
        // 첨부 파일 번호
        Long fno = 2L;
        int applied = pdsBoardRepository.deletePDSFile(fno);
        log.info("# deleted PDSFILE : " + applied);
    }

    @Test
    public void insertDummies() {
        List<PDSBoard> list = new ArrayList<>(100);
        IntStream.range(1,100).forEach(i -> {
            PDSBoard pds = new PDSBoard();
            pds.setPname("자료"+i);

            PDSFile file1 = new PDSFile();
            file1.setPdsfile("file1.doc");

            PDSFile file2 = new PDSFile();
            file2.setPdsfile("file2.doc");

            pds.setFiles(Arrays.asList(file1,file2));

            log.info("# try to save pds");

            list.add(pds);
        });

        pdsBoardRepository.saveAll(list);
    }

    @Test
    public void viewSummary() {
        pdsBoardRepository.getSummary().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
    }

}
