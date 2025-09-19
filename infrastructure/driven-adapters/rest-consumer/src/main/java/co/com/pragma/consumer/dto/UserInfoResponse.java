package co.com.pragma.consumer.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserInfoResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String documentId;
    private String email;
    private BigDecimal baseSalary;
}
