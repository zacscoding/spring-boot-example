package statemachine.orm;

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
import statemachine.state.component.ComponentState;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class ComponentEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private ComponentState state;
}
