package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.models.Patient;

import java.util.List;

@ApplicationScoped
public class PatientDao extends GenericDao<Patient> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<Patient> getType() {
        return Patient.class;
    }

    public Patient findByHealthNumber(String healthNumber) {
        try {
            return em.createQuery("SELECT p FROM Patient p WHERE p.healthNumber = :hn", Patient.class)
                    .setParameter("hn", healthNumber)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    public List<Patient> search(String q) {
        String like = "%" + q.toLowerCase() + "%";
        return em.createQuery(
                "SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE :q OR LOWER(p.lastName) LIKE :q OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE :q OR p.healthNumber LIKE :q",
                Patient.class)
                .setParameter("q", like)
                .getResultList();
    }
}
