
package fr.fortytwo.spring.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

import fr.fortytwo.spring.service.services.UsersService;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.annotation.ComponentScan;

@PropertySource("classpath:db.properties")
@Configuration
@ComponentScan(basePackages = "fr.fotytwo.spring.service")
public class ApplicationConfig {

    @Value("${db.driver.name}")
    private String driverName;

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String name;

    @Value("${db.password}")
    private String password;

    @Bean(name = "hikariDataSource")
    public DataSource getHikariDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverName);
        ds.setJdbcUrl(url);
        ds.setUsername(name);
        ds.setPassword(password);
        return ds;
    }

    @Bean(name = "driverManagerDataSource")
    public DataSource getDriverManagerDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverName);
        ds.setUrl(url);
        ds.setUsername(name);
        ds.setPassword(password);
        return ds;
    }

}
