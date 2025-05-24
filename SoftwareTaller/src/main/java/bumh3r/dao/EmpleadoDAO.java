package bumh3r.dao;

import bumh3r.model.New.EmpleadoN;
import bumh3r.request.EmpleadoRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.Cleanup;

public class EmpleadoDAO {
    public EmpleadoN save(EmpleadoN empleado) {
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
            EmpleadoN empleadoFresh = emNew.find(EmpleadoN.class, empleado.getId());
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

    public Optional<EmpleadoN> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        return Optional.ofNullable(em.find(EmpleadoN.class, id));
    }

    public List<EmpleadoN> findAll() throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM EmpleadoN e", EmpleadoN.class)
                .getResultList();
    }

    public List<EmpleadoN> findAllTechnician() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM EmpleadoN e WHERE e.tipoEmpleado.id = 2", EmpleadoN.class)
                .getResultList();
    }

    public List<EmpleadoN> findAllNoUser() throws Exception {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM EmpleadoN e LEFT JOIN Usuario u ON e.id = u.empleado.id WHERE u.empleado.id IS NULL", EmpleadoN.class)
                .getResultList();

    }

    public EmpleadoN update(Long id, EmpleadoN value) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            EmpleadoN empleado = em.find(EmpleadoN.class, id);
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

    public void update(EmpleadoN empleado) throws Exception {
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
