package co.com.pragma.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PostgreSQLConnectionPool {

    public static final int INITIAL_SIZE = 12;
    public static final int MAX_SIZE = 15;
    public static final int MAX_IDLE_TIME = 30;

    @Bean
    public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
        // r2dbc URL: r2dbc:postgresql://localhost:5432/authentication_db
        String url = properties.getUrl().replace("r2dbc:postgresql://", "");
        String[] parts = url.split("/", 2); // ["localhost:5432", "authentication_db"]
        String[] hostPort = parts[0].split(":"); // ["localhost", "5432"]

        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);
        String database = parts[1];

        PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}
