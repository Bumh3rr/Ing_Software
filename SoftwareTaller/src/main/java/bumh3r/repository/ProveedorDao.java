package bumh3r.repository;

import bumh3r.model.Proveedor;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class ProveedorDao {
    public Proveedor save(Proveedor proveedor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(proveedor);
            em.flush();
            em.getTransaction().commit();

            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(Proveedor.class, proveedor.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Proveedor> findAll() {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Proveedor e", Proveedor.class)
                .getResultList();
    }
}
