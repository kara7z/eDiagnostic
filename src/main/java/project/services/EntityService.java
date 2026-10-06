package project.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class EntityService {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Transactional
    public <T> T create(T entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    public <T> T update(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public <T> void delete(Class<T> type, Object id) {
        T ref = em.find(type, id);
        if (ref != null) {
            em.remove(ref);
        }
    }

    public <T> T find(Class<T> type, Object id) {
        return em.find(type, id);
    }

    public <T> List<T> findAll(Class<T> type) {
        return em.createQuery(
                "SELECT e FROM " + type.getSimpleName() + " e", type)
                .getResultList();
    }
}
