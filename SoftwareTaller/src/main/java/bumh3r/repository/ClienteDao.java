package bumh3r.repository;

import bumh3r.model.Cliente;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class ClienteDao {

    public Cliente save(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.flush();
            em.getTransaction().commit();

            @Cleanup
            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(Cliente.class, cliente.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Cliente> findAll() throws Exception {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Cliente e", Cliente.class)
                .getResultList();
    }

    public List<Cliente> findByName(String nombre) throws Exception {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Cliente e WHERE e.nombre LIKE :nombre", Cliente.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }
}
