package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.QueueEntry;

import java.util.List;

@ApplicationScoped
public class QueueEntryDao extends GenericDao<QueueEntry> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<QueueEntry> getType() {
        return QueueEntry.class;
    }

    public QueueEntry findWithPatient(Long id) {
        List<QueueEntry> list = em.createQuery("SELECT q FROM QueueEntry q JOIN FETCH q.patient WHERE q.id = :id", QueueEntry.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<QueueEntry> findAllOrderedByArrival() {
        return em.createQuery("SELECT q FROM QueueEntry q JOIN FETCH q.patient ORDER BY q.arrivalTime ASC", QueueEntry.class)
                .getResultList();
    }
}
