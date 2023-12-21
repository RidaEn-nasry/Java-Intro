
package fr.fortytwo.sockets.server.config;

import java.io.IOException;
import java.net.ServerSocket;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        assert ds != null;
        return ds;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getHikariDataSource());
    }

    private int parsePort(String str) throws IllegalArgumentException {
        int port = Integer.parseInt(str);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Valid range values for port are 0-65535");
        }
        return port;
    }

    @Bean("serverSocket")
    public ServerSocket getServerSocket() throws IOException, IllegalArgumentException {
        int port = parsePort(System.getProperty("server.port"));
        return new ServerSocket(port);
    }
}