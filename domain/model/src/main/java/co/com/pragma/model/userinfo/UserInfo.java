package co.com.pragma.model.userinfo;
import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserInfo {
    private String documentId;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal baseSalary;
}
