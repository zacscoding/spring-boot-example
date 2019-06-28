package demo.demo2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Random;
import javax.sql.DataSource;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
// @DataJpaTest
public class DataTypeCheckTest {

    @Autowired
    private DataTypeCheckRepository repository;

    @Autowired
    private DataSource dataSource;

    @Test
    public void insertBytes() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println(metaData);
            System.out.println("l");
        }

        byte[] sample = new byte[4096];
        for (int i = 0; i < 4096; i++) {
            sample[i] = (byte) new Random().nextInt(100);
        }

        DataTypeCheckEntity entity = DataTypeCheckEntity.builder()
            .byteValues(sample)
            .byteStringValues(Hex.toHexString(sample))
            .lobBytes(sample)
            .lobString(Hex.toHexString(sample))
            .build();

        DataTypeCheckEntity saved = repository.save(entity);
        DataTypeCheckEntity find = repository.findById(saved.getId()).get();

        System.out.println("Saved :: " + saved.getId());
        System.out.println(find.getByteValues().length);
        System.out.println(find.getByteStringValues().length());
    }
}
