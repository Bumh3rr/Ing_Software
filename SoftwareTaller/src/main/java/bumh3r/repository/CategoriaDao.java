package bumh3r.repository;

import bumh3r.model.Categoria;
import java.util.List;

public class CategoriaDao {
    public List<Categoria> findAll()throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM Categoria e", Categoria.class)
                .getResultList();
    }
}
