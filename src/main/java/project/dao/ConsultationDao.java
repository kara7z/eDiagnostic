package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.Consultation;

import java.util.List;

@ApplicationScoped
public class ConsultationDao extends GenericDao<Consultation> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<Consultation> getType() {
        return Consultation.class;
    }

    public Consultation findWithPatient(Long id) {
        List<Consultation> list = em.createQuery("SELECT c FROM Consultation c JOIN FETCH c.patient WHERE c.id = :id", Consultation.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Consultation> findByPatient(project.models.Patient patient) {
        return em.createQuery("SELECT c FROM Consultation c JOIN FETCH c.patient WHERE c.patient = :p ORDER BY c.createdAt DESC", Consultation.class)
                .setParameter("p", patient)
                .getResultList();
    }
}
