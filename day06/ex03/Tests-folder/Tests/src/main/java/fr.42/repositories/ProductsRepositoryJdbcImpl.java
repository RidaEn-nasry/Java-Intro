
package fr._42.numbers.repositories;

import fr._42.numbers.repositories.ProductsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import fr._42.numbers.models.Product;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import java.util.List;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {

    private final EmbeddedDatabase ds;
    private final JdbcTemplate jdbcTemplate;

    public ProductsRepositoryJdbcImpl(EmbeddedDatabase ds) {
        this.ds = ds;
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    private RowMapper<Product> RowMapper = new RowMapper<Product>() {
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            return product;
        }
    };

    public List<Product> findAll() {
        try {
            String sql = "SELECT * FROM products";
            List<Product> products = this.jdbcTemplate.query(sql, RowMapper);
            return products;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<Product>();
        }
    }

    public Optional<Product> findById(long id) {
        try {
            String sql = "SELECT * FROM products WHERE id = ?";
            Product product = this.jdbcTemplate.queryForObject(sql, RowMapper, id);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
        try {
            this.jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getId());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
        try {
            this.jdbcTemplate.update(sql, product.getName(), product.getPrice());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try {
            this.jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
