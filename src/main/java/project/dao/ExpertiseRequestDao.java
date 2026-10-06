package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.ExpertiseRequest;

@ApplicationScoped
public class ExpertiseRequestDao extends GenericDao<ExpertiseRequest> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<ExpertiseRequest> getType() {
        return ExpertiseRequest.class;
    }
}
