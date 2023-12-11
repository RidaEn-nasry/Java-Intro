package fortytwo.spring.service.application;

import java.sql.Driver;

import javax.sql.DataSource;

import fortytwo.spring.service.models.User;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/database";
        String username = "postgres";
        String password = "qwerty007";
        // DataSource ds = new DataSource(jdbcUrl, username, password);
    }
}
