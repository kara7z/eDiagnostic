package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.Patient;
import project.models.VitalSign;

import java.util.List;

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

    public VitalSign findLatestByPatient(Patient patient) {
        List<VitalSign> list = em.createQuery(
                "SELECT v FROM VitalSign v WHERE v.patient = :p ORDER BY v.measuredAt DESC", VitalSign.class)
                .setParameter("p", patient)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
