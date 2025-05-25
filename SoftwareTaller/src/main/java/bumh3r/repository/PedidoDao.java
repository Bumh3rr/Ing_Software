package bumh3r.repository;

import bumh3r.model.DetallesPedido;
import bumh3r.model.Pedido;
import bumh3r.request.DetallesPedidosRequest;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PedidoDao {

    public Pedido save(Pedido pedido, List<DetallesPedidosRequest> detalles) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pedido);
            em.flush();

            for (DetallesPedidosRequest request : detalles) {
                DetallesPedido detalle = new DetallesPedido();
                detalle.setCantidad(request.cantidad());
                detalle.setRefaccion(request.refaccion());
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

    public List<Pedido> findAll() {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT p FROM Pedido p", Pedido.class)
                .getResultList();
    }

    public List<DetallesPedido> findAllDetalles(Long id) {
        return JPAUtil.getEntityManager()
                .createQuery("SELECT d FROM DetallesPedido d WHERE d.pedido.id = :id", DetallesPedido.class)
                .setParameter("id", id)
                .getResultList();
    }

    public void update(Pedido pedido) {
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
