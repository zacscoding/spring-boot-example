package demo.demo2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * For data type
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DEMO2_DATA_TYPE_CHECK")
public class DataTypeCheckEntity {

    @Id
    @GeneratedValue
    @Column(name = "BOOK_ID")
    private Long id;

    @Column(name = "BYTE_VALUES", length = 4096)
    private byte[] byteValues;

    @Column(name = "STRING_VALUES", length = 4096 * 2)
    private String byteStringValues;

    @Column(name = "BYTE_DEFAULT")
    private byte[] byteDefaults;

    @Lob
    @Column(name = "LOB_BYTES")
    private byte[] lobBytes;

    @Lob
    @Column(name = "LOB_STRING")
    private String lobString;

}
