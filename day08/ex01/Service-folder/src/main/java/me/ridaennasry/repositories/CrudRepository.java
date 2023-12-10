package me.ridaennasry.repositories;

public interface CrudRepository<T> {

    T findById(Long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(Long id);

}