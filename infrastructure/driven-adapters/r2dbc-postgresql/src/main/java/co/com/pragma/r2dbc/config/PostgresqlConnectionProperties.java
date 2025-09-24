package co.com.pragma.r2dbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.r2dbc")
public class PostgresqlConnectionProperties {
    // Getters y setters obligatorios
    private  String url;
    private  String username;
    private  String password;

}
