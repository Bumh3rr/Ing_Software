package bumh3r.dao;

import bumh3r.model.New.RefaccionN;
import jakarta.persistence.EntityManager;
import java.util.List;

public class RefaccionDao {
    public RefaccionN save(RefaccionN refaccion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(refaccion);
            em.flush();
            em.getTransaction().commit();

            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(RefaccionN.class, refaccion.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<RefaccionN> findAll() throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT r FROM RefaccionN r", RefaccionN.class)
                .getResultList();
    }

    public List<RefaccionN> findByName(String nombre)throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT r FROM RefaccionN r WHERE r.nombre LIKE :nombre", RefaccionN.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }
}
