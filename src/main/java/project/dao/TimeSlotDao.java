package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.TimeSlot;

import java.util.List;

@ApplicationScoped
public class TimeSlotDao extends GenericDao<TimeSlot> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    public List<TimeSlot> findAvailableOrdered() {
        return em.createQuery("SELECT t FROM TimeSlot t JOIN FETCH t.specialist s LEFT JOIN FETCH s.specialty ORDER BY t.date ASC, t.startTime ASC", TimeSlot.class)
                .getResultList();
    }

    public TimeSlot findFull(Long id) {
        List<TimeSlot> list = em.createQuery("SELECT t FROM TimeSlot t JOIN FETCH t.specialist s LEFT JOIN FETCH s.specialty WHERE t.id = :id", TimeSlot.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected Class<TimeSlot> getType() {
        return TimeSlot.class;
    }
}
