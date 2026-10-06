package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.TimeSlot;

@ApplicationScoped
public class TimeSlotDao extends GenericDao<TimeSlot> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<TimeSlot> getType() {
        return TimeSlot.class;
    }
}
