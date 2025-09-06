package co.com.pragma.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionPoolTest {

    @Mock
    private PostgresqlConnectionProperties properties;

    @InjectMocks
    private PostgreSQLConnectionPool pool;

    @Test
    void getConnectionConfigSuccess() {
        when(properties.getUrl()).thenReturn("r2dbc:postgresql://localhost:5432/authentication_db");
        when(properties.getUsername()).thenReturn("postgres");
        when(properties.getPassword()).thenReturn("root");

        ConnectionFactory connectionFactory = pool.getConnectionConfig(properties);
        assertNotNull(connectionFactory);
    }
}