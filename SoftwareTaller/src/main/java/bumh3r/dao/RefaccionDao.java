package bumh3r.dao;

import bumh3r.model.New.RefaccionN;
import jakarta.persistence.EntityManager;

public class RefaccionDao {
    public RefaccionN save(RefaccionN refaccion){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(refaccion);
            em.flush();
            em.getTransaction().commit();

            EntityManager emNew = JPAUtil.getEntityManager();
            return emNew.find(RefaccionN.class, refaccion.getId());
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
