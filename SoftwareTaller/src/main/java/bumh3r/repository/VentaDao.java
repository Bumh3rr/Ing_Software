package bumh3r.repository;

import bumh3r.model.DetalleVenta;
import bumh3r.model.Nota;
import bumh3r.model.Pago;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.model.Venta;
import bumh3r.request.VentaRequest;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Cleanup;

public class VentaDao {
    public void save(VentaRequest ventaRequest, Nota nota) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Venta ventaGenerada = Venta.builder()
                    .fecha(LocalDateTime.now())
                    .estado(Objects.equals(ventaRequest.subTotal(), ventaRequest.pago().monto()) ? Venta.Estado.COMPLETADA : Venta.Estado.PENDIENTE)
                    .total(ventaRequest.total())
                    .descuento(ventaRequest.descuento())
                    .abono(ventaRequest.abono())
                    .nota(nota)
                    .build();
            em.persist(ventaGenerada);
            em.flush();

            Pago pago = Pago.builder()
                    .monto(ventaRequest.pago().monto())
                    .fecha(LocalDateTime.now())
                    .venta(ventaGenerada)
                    .metodoPago(ventaRequest.pago().metodoPago())
                    .build();
            em.persist(pago);
            em.flush();

            for (Reparacion reparacion : ventaRequest.reparaciones()) {
                actualizarEstadoReparacion(reparacion, em);
                DetalleVenta detalleVenta = DetalleVenta.builder()
                        .venta(ventaGenerada)
                        .reparacion(reparacion)
                        .build();
                em.persist(detalleVenta);
                em.flush();
            }

            for (Refaccion refaccion : ventaRequest.refacciones()) {
                actualizarStock(refaccion, em);
                DetalleVenta detalleVenta = DetalleVenta.builder()
                        .venta(ventaGenerada)
                        .refaccion(refaccion)
                        .build();
                em.persist(detalleVenta);
                em.flush();
            }
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

    private static void actualizarStock(Refaccion refaccion, EntityManager em) {
        int update_stock = refaccion.getStock() -1;
        refaccion.setStock(update_stock);
        em.merge(refaccion);
        em.flush();
    }

    private static void actualizarEstadoReparacion(Reparacion reparacion, EntityManager em) {
        reparacion.setEstado(Reparacion.EstadoReparacion.COBRADO);
        em.merge(reparacion);
        em.flush();
    }

    public List<Venta> findByDate(LocalDate fecha) {
        LocalDateTime start = fecha.atStartOfDay();
        LocalDateTime end = fecha.plusDays(1).atStartOfDay();
        @Cleanup
        EntityManager entityManager = JPAUtil.getEntityManager();
        return entityManager.createQuery("SELECT e FROM Venta e WHERE e.fecha >= :start AND e.fecha < :end", Venta.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Venta> findById(Long id) {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT e FROM Venta e WHERE e.id = :id", Venta.class)
                .setParameter("id", id)
                .getResultList();
    }


    public void update(Venta venta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(venta);
            em.flush();
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
