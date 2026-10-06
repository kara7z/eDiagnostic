package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.MedicalProcedure;

@ApplicationScoped
public class MedicalProcedureDao extends GenericDao<MedicalProcedure> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<MedicalProcedure> getType() {
        return MedicalProcedure.class;
    }
}
