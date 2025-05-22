package bumh3r.dao;

import bumh3r.model.New.CategoriaN;
import java.util.List;

public class CategoriaDao {
    public List<CategoriaN> findAll()throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM CategoriaN e", CategoriaN.class)
                .getResultList();
    }
}
