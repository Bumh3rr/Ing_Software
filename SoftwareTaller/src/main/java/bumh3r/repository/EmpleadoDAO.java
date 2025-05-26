package bumh3r.repository;

import bumh3r.model.Empleado;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.Cleanup;

public class EmpleadoDAO {
    public Empleado save(Empleado empleado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(empleado.getDireccion());
            empleado.setTipoEmpleado(em.merge(empleado.getTipoEmpleado())); // <- Obtenemos el tipo de empleado
            em.persist(empleado);
            em.flush();
            em.getTransaction().commit();

            @Cleanup
            EntityManager emNew = JPAUtil.getEntityManager();
            Empleado empleadoFresh = emNew.find(Empleado.class, empleado.getId());
            return empleadoFresh;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Empleado> findById(Long id) {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return Optional.ofNullable(entityManager.find(Empleado.class, id));
    }

    public List<Empleado> findAll() throws Exception {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultList();
    }

    public List<Empleado> findAllActive() {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Empleado e WHERE  e.isActivo = true", Empleado.class)
                .getResultList();
    }

    public List<Empleado> findAllTechnician() {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Empleado e WHERE e.tipoEmpleado.id = 2 AND e.isActivo = true", Empleado.class)
                .getResultList();
    }

    public List<Empleado> findAllNoUser() throws Exception {
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager
                .createQuery("SELECT e FROM Empleado e " +
                                "LEFT JOIN Usuario u ON e.id = u.empleado.id " +
                                "WHERE u.empleado.id IS NULL AND e.isActivo = true AND e.tipoEmpleado.id != 2",
                        Empleado.class)
                .getResultList();

    }

    public Empleado update(Long id, Empleado value) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Empleado empleado = em.find(Empleado.class, id);
            if (empleado == null) throw new IllegalArgumentException("Empleado no encontrado");
            empleado.setNombre(value.getNombre());
            empleado.setApellido(value.getApellido());
            empleado.setTelefono(value.getTelefono());
            empleado.setCorreo(value.getCorreo());
            empleado.setGenero(value.getGenero());
            empleado.setRfc(value.getRfc());
            empleado.getDireccion().setCalle(value.getDireccion().getCalle());
            empleado.getDireccion().setColonia(value.getDireccion().getColonia());
            empleado.getDireccion().setCodigo_postal(value.getDireccion().getCodigo_postal());
            empleado.getDireccion().setEstado(value.getDireccion().getEstado());
            empleado.getDireccion().setMunicipio(value.getDireccion().getMunicipio());
            empleado.setTipoEmpleado(value.getTipoEmpleado());
            em.merge(empleado);
            em.getTransaction().commit();
            return empleado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Empleado empleado) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(empleado);
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

}
