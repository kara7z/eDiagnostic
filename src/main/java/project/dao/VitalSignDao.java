package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.VitalSign;

@ApplicationScoped
public class VitalSignDao extends GenericDao<VitalSign> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<VitalSign> getType() {
        return VitalSign.class;
    }
}
