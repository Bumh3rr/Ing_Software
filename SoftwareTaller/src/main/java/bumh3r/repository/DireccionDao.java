package bumh3r.repository;

import bumh3r.model.Direccion;
import jakarta.persistence.EntityManager;
import lombok.Cleanup;

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
            throw e;
        } finally {
            em.close();
        }
    }

    public Direccion buscarPorId(Long id) {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager.find(Direccion.class, id);
    }

}
