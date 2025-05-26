package bumh3r.repository;

import bumh3r.model.Usuario;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.Cleanup;

public class UsuarioDao {

    public Usuario save(Usuario usuario) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.flush();
            em.getTransaction().commit();

            @Cleanup
            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(Usuario.class, usuario.getId());
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
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT u FROM Usuario u WHERE u.isAdmin = false", Usuario.class)
                .getResultList();
    }


    public void update(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
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

    public void delete(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuarioToDelete = em.find(Usuario.class, usuario.getId());
            if (usuarioToDelete != null) {
                em.remove(usuarioToDelete);
            }
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

    public Usuario verificarUsuario(String username, String password)throws Exception {
        @Cleanup
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password", Usuario.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }
}
