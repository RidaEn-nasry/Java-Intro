
package fr.fortytwo.sockets.server.repositories;

import java.util.List;

public interface CrudRepository<T> {

    public T findById(Long id);

    public List<T> findAll();

    public T save(T entity);

    public void update(T entity);

    public void delete(Long id);

}