
package fr.fortytwo.sockets.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan("fr.fortytwo.sockets.server")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String password;

    @Value("${db.driver.name}")
    private String driverClassName;

    @Bean("bcryptPasswordEncoder")
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("hikariDataSource")
    @Scope("singleton")
    public DataSource getHikariDataSource() {
        final HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
        return ds;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate getNamedParameterJdbcTemplate() {
        return new JdbcTemplate(getHikariDataSource());
    }
}