package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.QueueEntry;

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
}
