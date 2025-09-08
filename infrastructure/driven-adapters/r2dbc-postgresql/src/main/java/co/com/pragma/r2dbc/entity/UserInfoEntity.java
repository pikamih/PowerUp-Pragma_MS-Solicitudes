package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users") // nombre de la tabla en la BD
public class UserInfoEntity {

    @Id
    private String documentId;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal baseSalary;

}
