package bumh3r.dao;

import bumh3r.model.Cliente;
import bumh3r.model.New.ClienteN;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class ClienteDao {

    public ClienteN save(ClienteN cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.flush();
            em.getTransaction().commit();

            @Cleanup
            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(ClienteN.class, cliente.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public List<ClienteN> findAll() throws Exception{
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<ClienteN> clientes = em.createQuery("SELECT e FROM ClienteN e", ClienteN.class).getResultList();
            em.getTransaction().commit();
            return clientes;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public List<ClienteN> findByName(String nombre)throws Exception  {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<ClienteN> clientes = em.createQuery("SELECT e FROM ClienteN e WHERE e.nombre LIKE :nombre", ClienteN.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();
            em.getTransaction().commit();
            return clientes;
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
