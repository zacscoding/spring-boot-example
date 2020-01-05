package demo.client.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ServiceEntity {

    @Id
    @GeneratedValue
    @Column(name = "service_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String groupName;

    private String schema;

    private String host;

    private int port;
}
