package bumh3r.dao;


import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.TipoEmpleado;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TipoEmpleadoDAO {

    public List<TipoEmpleado> getList() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<TipoEmpleado> tipoEmpleados = em.createQuery("SELECT e FROM TipoEmpleado e", TipoEmpleado.class).getResultList();
            em.getTransaction().commit();
            return tipoEmpleados;
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
