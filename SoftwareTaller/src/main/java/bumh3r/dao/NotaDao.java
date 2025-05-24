package bumh3r.dao;

import bumh3r.model.New.DispositivoN;
import bumh3r.model.New.NotaN;
import bumh3r.model.New.ReparacionN;
import bumh3r.request.DispositivoRequest;
import bumh3r.request.NotaRequest;
import bumh3r.request.ReparacionRequest;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class NotaDao {
    public List<NotaN> findAll() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM NotaN e", NotaN.class)
                .getResultList();
    }

    public NotaN findById(Long id) {
        return JPAUtil.getEntityManager().find(NotaN.class, id);
    }

    public Long save(NotaRequest value) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            NotaN nota = NotaN.builder()
                    .folio(String.valueOf(new Random().nextInt(100000)))
                    .empleado(value.empleado())
                    .cliente(value.cliente())
                    .fecha_registro(LocalDateTime.now())
                    .estado(NotaN.EstadoNota.EN_PROCESO)
                    .build();
            em.persist(nota);
            em.flush();

            for (DispositivoRequest device : value.dispositivos()) {
                DispositivoN dispositivo = DispositivoN.builder()
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
                    ReparacionN reparacion = ReparacionN.builder()
                            .categoria(repair.categoria())
                            .reparacion(repair.reparacion())
                            .observacion(repair.observacion())
                            .precio(repair.precio())
                            .abono(repair.abono())
                            .estado(ReparacionN.EstadoReparacion.PENDIENTE)
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

    public List<NotaN> findByFolio(String folio) {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM NotaN e WHERE e.folio LIKE :folio", NotaN.class)
                .setParameter("folio", "%" + folio + "%")
                .getResultList();
    }

    public List<NotaN> findByDate(LocalDate fecha) {
        LocalDateTime start = fecha.atStartOfDay();
        LocalDateTime end = fecha.plusDays(1).atStartOfDay();
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM NotaN e WHERE e.fecha_registro >= :start AND e.fecha_registro < :end", NotaN.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
