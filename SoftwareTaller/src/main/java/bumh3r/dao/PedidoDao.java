package bumh3r.dao;

import bumh3r.model.New.DetallesPedidoN;
import bumh3r.model.New.PedidoN;
import bumh3r.request.DetallesPedidosRequest;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PedidoDao {

    public PedidoN save(PedidoN pedido, List<DetallesPedidosRequest> detalles) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pedido);
            em.flush();

            for (DetallesPedidosRequest request : detalles) {
                DetallesPedidoN detalle = new DetallesPedidoN();
                detalle.setCantidad(request.cantidad());
                detalle.setRefaccion(request.refaccionN());
                detalle.setPedido(pedido);
                em.persist(detalle);
            }

            em.getTransaction().commit();
            return pedido;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<PedidoN> findAll() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT p FROM PedidoN p", PedidoN.class)
                .getResultList();
    }

    public List<DetallesPedidoN> findAllDetalles(Long id) {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT d FROM DetallesPedidoN d WHERE d.pedido.id = :id", DetallesPedidoN.class)
                .setParameter("id", id)
                .getResultList();
    }

    public void update(PedidoN pedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pedido);
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
