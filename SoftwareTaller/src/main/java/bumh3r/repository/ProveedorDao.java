package bumh3r.repository;

import bumh3r.model.Proveedor;
import jakarta.persistence.EntityManager;
import java.util.List;

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
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<Proveedor> proveedores = em.createQuery("SELECT e FROM Proveedor e", Proveedor.class).getResultList();
            em.getTransaction().commit();
            return proveedores;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
