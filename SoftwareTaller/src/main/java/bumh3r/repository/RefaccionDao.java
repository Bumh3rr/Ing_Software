package bumh3r.repository;

import bumh3r.model.Refaccion;
import jakarta.persistence.EntityManager;
import java.util.List;

public class RefaccionDao {
    public Refaccion save(Refaccion refaccion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(refaccion);
            em.flush();
            em.getTransaction().commit();

            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(Refaccion.class, refaccion.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Refaccion> findAll() throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT r FROM Refaccion r", Refaccion.class)
                .getResultList();
    }

    public List<Refaccion> findByName(String nombre)throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT r FROM Refaccion r WHERE r.nombre LIKE :nombre", Refaccion.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }
}
