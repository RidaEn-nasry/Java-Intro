
package fr._42.numbers.repositories;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;

public class EmbeddedDataSourceTest {

    private EmbeddedDatabase ds;

    @BeforeEach
    void init() {
        EmbeddedDatabase ds = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        this.ds = ds;
    }

    @Test
    void testConnection() {
        try {
            Connection connection = ds.getConnection();
            assertNotNull(connection);
            connection.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
