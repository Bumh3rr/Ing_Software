package bumh3r.repository;

import bumh3r.model.Pago;
import bumh3r.model.Venta;
import bumh3r.request.PagoRequest;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Objects;

public class PagoDao {
    private VentaDao ventaDao;

    public PagoDao() {
        this.ventaDao = new VentaDao();
    }

    public Pago save(PagoRequest request, Venta venta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pago pago = Pago.builder()
                    .fecha(LocalDateTime.now())
                    .metodoPago(request.metodoPago())
                    .monto(request.monto())
                    .venta(venta)
                    .build();
            em.persist(pago);
            em.flush();
            em.getTransaction().commit();
            return pago;
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
