package bumh3r.dao;

import bumh3r.model.New.EmpleadoN;
import bumh3r.model.Usuario;
import jakarta.persistence.EntityManager;
import java.util.List;

public class UsuarioDao {

    // CREATE - Guardar un producto
    public void guardar(Usuario producto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(producto);
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

    public List<Usuario> getList() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<Usuario> usuarios = em.createQuery("SELECT e FROM Usuario e", Usuario.class).getResultList();
            em.getTransaction().commit();
            return usuarios;
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


}
