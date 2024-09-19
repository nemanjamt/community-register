package community.register.dao;

public interface CrudDAO<T> {
    T create(T t);
    T read(Long id);
    T update(T t);
    boolean delete(Long id);
}
