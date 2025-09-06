package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("states") // nombre de la tabla en la BD
public class StateEntity {

    @Id
    private Integer id;
    private String name;
    private String description;
}
