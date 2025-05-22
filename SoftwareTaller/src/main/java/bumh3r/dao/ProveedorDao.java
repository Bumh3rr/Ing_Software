package bumh3r.dao;

import bumh3r.model.New.ProveedorN;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProveedorDao {
    public ProveedorN save(ProveedorN proveedor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(proveedor);
            em.flush();
            em.getTransaction().commit();

            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(ProveedorN.class, proveedor.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<ProveedorN> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<ProveedorN> proveedores = em.createQuery("SELECT e FROM ProveedorN e", ProveedorN.class).getResultList();
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
