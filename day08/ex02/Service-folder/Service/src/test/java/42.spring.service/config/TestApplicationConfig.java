
package fortytwo.spring.service.config;

import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import fortytwo.spring.service.config.ApplicationConfig;

@Configuration
public class TestApplicationConfig {
    private EmbeddedDatabase ds;
    private JdbcTemplate jdbcTemplate;

    // creating a only one instance of ApplicationContext
    private static final ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

    // private void removeTable() {
    // // remove and reset the table
    // jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    // }

    @BeforeEach
    public void init() {
        EmbeddedDatabase ds = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
        this.ds = ds;
        this.jdbcTemplate = new JdbcTemplate(ds);
        // this.removeTable();
        jdbcTemplate.execute(
                "CREATE TABLE users (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,email VARCHAR(255))");
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(jdbcTemplate);
    }



    @Test
    public void testGetHikariDataSource() {
        Connection connection = null;
        try {
            HikariDataSource ds = context.getBean(HikariDataSource.class);
            connection = ds.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testDriverManagerDataSource() {
        Connection connection = null;
        try {
            DriverManagerDataSource ds = context.getBean(DriverManagerDataSource.class);
            connection = ds.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            System.err.println("Err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}