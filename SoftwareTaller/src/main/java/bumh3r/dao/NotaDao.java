package bumh3r.dao;

import bumh3r.model.New.NotaN;
import java.util.List;

public class NotaDao {
    public List<NotaN> findAll() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM NotaN e", NotaN.class)
                .getResultList();
    }
}
