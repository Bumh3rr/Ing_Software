package bumh3r.dao;

import bumh3r.model.New.EmpleadoN;
import bumh3r.model.Usuario;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class UsuarioDao {

    public Usuario save(Usuario usuario)throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.flush();
            em.getTransaction().commit();

            @Cleanup
            EntityManager emNew = JPAUtil.getEntityManager();
            Usuario usuarioRefresh = emNew.find(Usuario.class, usuario.getId());
            return usuarioRefresh;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Usuario> getList() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u WHERE u.isAdmin = false", Usuario.class).getResultList();
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
