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
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<EmpleadoN> findById(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            EmpleadoN empleado = em.find(EmpleadoN.class, id);
            em.getTransaction().commit();
            return Optional.ofNullable(empleado);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<EmpleadoN> findAll() throws Exception{
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            List<EmpleadoN> empleados = em.createQuery("SELECT e FROM EmpleadoN e", EmpleadoN.class).getResultList();
            em.getTransaction().commit();
            return empleados;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public EmpleadoN update(Long id, EmpleadoRequest value){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            EmpleadoN empleado = em.find(EmpleadoN.class, id);
            if (empleado == null) throw new IllegalArgumentException("Empleado no encontrado");
            empleado.setNombre(value.nombre());
            empleado.setApellido(value.apellido());
            empleado.setTelefono(value.telefono());
            empleado.setCorreo(value.correo());
            empleado.setGenero(value.genero());
            empleado.setRfc(value.rfc());
            empleado.getDireccion().setCalle( value.direccion().calle());
            empleado.getDireccion().setColonia(value.direccion().colonia());
            empleado.getDireccion().setCodigo_postal(value.direccion().codigo_postal());
            empleado.getDireccion().setEstado(value.direccion().estado());
            empleado.getDireccion().setMunicipio(value.direccion().municipio());
            empleado.setTipoEmpleado(value.tipoEmpleado());
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

    public void update(EmpleadoN empleado) throws Exception{
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
