package fortytwo.spring.service.repositories;

import java.util.List;

public interface CrudRepository<T> {

    public T findById(Long id);

    public List<T> findAll();

    public void save(T entity);

    public void update(T entity);

    public void delete(Long id);

}