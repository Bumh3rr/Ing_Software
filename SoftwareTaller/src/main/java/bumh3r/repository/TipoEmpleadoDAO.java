package bumh3r.repository;


import bumh3r.model.TipoEmpleado;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class TipoEmpleadoDAO {

    public List<TipoEmpleado> getList() {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM TipoEmpleado e", TipoEmpleado.class)
                .getResultList();
    }

}
