package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.ConsultationProcedure;

@ApplicationScoped
public class ConsultationProcedureDao extends GenericDao<ConsultationProcedure> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<ConsultationProcedure> getType() {
        return ConsultationProcedure.class;
    }
}
