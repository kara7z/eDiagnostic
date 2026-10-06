package project.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

public abstract class GenericDao<T> {

    protected abstract EntityManager getEm();

    protected abstract Class<T> getType();

    @Transactional
    public T create(T entity) {
        getEm().persist(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        return getEm().merge(entity);
    }

    @Transactional
    public void delete(Long id) {
        T ref = getEm().find(getType(), id);
        if (ref != null) {
            getEm().remove(ref);
        }
    }

    public T find(Long id) {
        return getEm().find(getType(), id);
    }

    public List<T> findAll() {
        return getEm().createQuery(
                "SELECT e FROM " + getType().getSimpleName() + " e", getType())
                .getResultList();
    }
}
