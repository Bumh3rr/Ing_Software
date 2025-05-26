package bumh3r.repository;

import bumh3r.model.Dispositivo;
import bumh3r.model.Nota;
import bumh3r.model.Reparacion;
import bumh3r.request.DispositivoRequest;
import bumh3r.request.NotaRequest;
import bumh3r.request.ReparacionRequest;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import lombok.Cleanup;

public class NotaDao {
    public List<Nota> findAll() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM Nota e", Nota.class)
                .getResultList();
    }

    public Nota findById(Long id) {
        return JPAUtil.getEntityManager().find(Nota.class, id);
    }

    public Long save(NotaRequest value) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Nota nota = Nota.builder()
                    .folio(String.valueOf(new Random().nextInt(100000)))
                    .empleado(value.empleado())
                    .cliente(value.cliente())
                    .fecha_registro(LocalDateTime.now())
                    .estado(Nota.EstadoNota.EN_PROCESO)
                    .build();
            em.persist(nota);
            em.flush();

            for (DispositivoRequest device : value.dispositivos()) {
                Dispositivo dispositivo = Dispositivo.builder()
                        .tipo_dispositivo(device.getTipo_dispositivo())
                        .marca(device.getMarca())
                        .modelo(device.getModelo())
                        .imei(device.getImei())
                        .utils(device.getUtils())
                        .observaciones(device.getObservaciones())
                        .nota(nota)
                        .build();
                em.persist(dispositivo);
                em.flush();

                for (ReparacionRequest repair : device.getReparaciones()) {
                    Reparacion reparacion = Reparacion.builder()
                            .categoria(repair.categoria())
                            .reparacion(repair.reparacion())
                            .observacion(repair.observacion())
                            .precio(repair.precio())
                            .abono(repair.abono())
                            .estado(Reparacion.EstadoReparacion.PENDIENTE)
                            .dispositivo(dispositivo)
                            .empleado(repair.tecnico())
                            .build();
                    em.persist(reparacion);
                    em.flush();
                }
            }
            em.getTransaction().commit();
            return nota.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Nota> findByFolio(String folio) {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM Nota e WHERE e.folio LIKE :folio", Nota.class)
                .setParameter("folio", "%" + folio + "%")
                .getResultList();
    }

    public List<Nota> findByDate(LocalDate fecha) {
        LocalDateTime start = fecha.atStartOfDay();
        LocalDateTime end = fecha.plusDays(1).atStartOfDay();
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager.createQuery("SELECT e FROM Nota e WHERE e.fecha_registro >= :start AND e.fecha_registro < :end", Nota.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public void update(Reparacion reparacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(reparacion);
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

    public void update(Nota nota) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(nota);
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
