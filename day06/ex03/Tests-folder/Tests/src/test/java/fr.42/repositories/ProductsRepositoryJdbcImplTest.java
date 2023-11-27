
package fr._42.numbers.repositories;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import fr._42.numbers.models.Product;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {

    private EmbeddedDatabase ds;
    private final List<Product> EXPECTED_FIND_ALL_PRODUCTS = Arrays.asList(
            new Product(1, "Apple", 1.99),
            new Product(2, "Banana", 2.99),
            new Product(3, "Orange", 3.99),
            new Product(4, "Pineapple", 4.99),
            new Product(5, "Mango", 5.99));

    private final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1, "Apple", 1.99);
    private final Product EXPECTED_UPDATE_PRODUCT = new Product(1, "Apple", 2.99);
    private final Product EXPECTED_SAVE_PRODUCT = new Product(6, "Kiwi", 6.99);
    private final List<Product> EXPECTED_DELETE_PRODUCT = Arrays.asList(
            new Product(2, "Banana", 2.99),
            new Product(3, "Orange", 3.99),
            new Product(4, "Pineapple", 4.99),
            new Product(5, "Mango", 5.99),
            new Product(6, "Kiwi", 6.99));

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
    void testFindAll() {
        ProductsRepositoryJdbcImpl productsRepositoryJdbcImpl = new ProductsRepositoryJdbcImpl(ds);
        List<Product> actual = productsRepositoryJdbcImpl.findAll();
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, actual);
    }

    @Test
    void testFindById() {
        ProductsRepositoryJdbcImpl productsRepositoryJdbcImpl = new ProductsRepositoryJdbcImpl(ds);
        Optional<Product> actual = productsRepositoryJdbcImpl.findById(1);
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, actual.get());
    }

    @Test
    void testUpdate() {
        ProductsRepositoryJdbcImpl productsRepositoryJdbcImpl = new ProductsRepositoryJdbcImpl(ds);
        productsRepositoryJdbcImpl.update(new Product(1, "Apple", 2.99));
        Product actual = productsRepositoryJdbcImpl.findById(1).get();
        assertEquals(EXPECTED_UPDATE_PRODUCT, actual);
    }

    @Test
    void testSave() {
        ProductsRepositoryJdbcImpl productsRepositoryJdbcImpl = new ProductsRepositoryJdbcImpl(ds);
        productsRepositoryJdbcImpl.save(new Product(6, "Kiwi", 6.99));
        Product actual = productsRepositoryJdbcImpl.findById(6).get();
        assertEquals(EXPECTED_SAVE_PRODUCT, actual);
    }

    @Test
    void testDelete() {
        ProductsRepositoryJdbcImpl productsRepositoryJdbcImpl = new ProductsRepositoryJdbcImpl(ds);
        productsRepositoryJdbcImpl.delete(1);
        List<Product> actual = productsRepositoryJdbcImpl.findAll();
        assertEquals(EXPECTED_DELETE_PRODUCT, actual);
    }

    @AfterEach
    void close() {
        ds.shutdown();
    }
}