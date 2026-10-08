package project.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import project.models.User;

import java.util.List;

@ApplicationScoped
public class UserDao extends GenericDao<User> {

    @PersistenceContext(unitName = "teleExpertisePU")
    private EntityManager em;

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @Override
    protected Class<User> getType() {
        return User.class;
    }

    public List<User> findSpecialists() {
        return em.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.specialty WHERE u.role = project.enums.Role.SPECIALIST AND u.active = true",
                User.class)
                .getResultList();
    }

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
