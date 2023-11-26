
package fr._42.numbers.repositories;

import java.util.List;
import fr._42.numbers.models.Product;
import java.util.Optional;

public interface ProductsRepository {

    public List<Product> findAll();

    public Optional<Product> findById(long id);

    public void update(Product product);

    public void save(Product product);

    public void delete(long id);
}

