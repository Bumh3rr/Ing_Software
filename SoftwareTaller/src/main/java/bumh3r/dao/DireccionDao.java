package bumh3r.dao;

import bumh3r.model.New.DireccionN;
import jakarta.persistence.EntityManager;

public class DireccionDao {
    // CREATE - Guardar un producto
    public void guardar(DireccionN direccion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(direccion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public DireccionN buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        DireccionN direccionN = null;
        try {
            direccionN = em.find(DireccionN.class, id);
        } finally {
            em.close();
        }
        return direccionN;
    }

}
