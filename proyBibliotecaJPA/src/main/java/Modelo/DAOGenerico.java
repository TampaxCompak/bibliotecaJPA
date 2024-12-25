package Modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class DAOGenerico<T> {
    private Class<T> claseEntidad;

    public DAOGenerico(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    public T guardar(EntityManager em, T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entidad);
            tx.commit();
            return entidad;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public T actualizar(EntityManager em, T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entidadActualizada = em.merge(entidad);
            tx.commit();
            return entidadActualizada;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public void eliminar(EntityManager em, T entidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(entidad);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public T obtenerPorId(EntityManager em, Object id) {
        return em.find(claseEntidad, id);
    }

    public List<T> obtenerTodos(EntityManager em) {
        TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + claseEntidad.getSimpleName() + " e", claseEntidad
        );
        return query.getResultList();
    }

    public T obtenerPorCampo(EntityManager em, String campo, Object valor) {
        TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + claseEntidad.getSimpleName() + " e WHERE e." + campo + " = :valor", claseEntidad
        );
        query.setParameter("valor", valor);
        return query.getResultStream().findFirst().orElse(null);
    }

    public T obtenerPorCampoUnico(EntityManager em, String campo, Object valor) {
        TypedQuery<T> query = em.createQuery(
                "SELECT e FROM " + claseEntidad.getSimpleName() + " e WHERE e." + campo + " = :valor", claseEntidad
        );
        query.setParameter("valor", valor);
        return query.getResultStream().findFirst().orElse(null);
    }

    public List<T> obtenerPorConsulta(EntityManager em, String jpql, String dni) {
        TypedQuery<T> query = em.createQuery(jpql, claseEntidad);
        query.setParameter("dni", dni);
        return query.getResultList();
    }
}