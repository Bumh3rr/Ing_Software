package bumh3r.repository;

import bumh3r.model.Empleado;
import bumh3r.model.MetodoPago;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Cleanup;

public class MetodoPagoDao {

    public List<MetodoPago> findAll(){
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM MetodoPago e", MetodoPago.class)
                .getResultList();
    }
}
