package bumh3r.repository;

import bumh3r.model.Direccion;
import jakarta.persistence.EntityManager;

public class DireccionDao {
    // CREATE - Guardar un producto
    public void guardar(Direccion direccion) {
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

    public Direccion buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Direccion direccion = null;
        try {
            direccion = em.find(Direccion.class, id);
        } finally {
            em.close();
        }
        return direccion;
    }

}
